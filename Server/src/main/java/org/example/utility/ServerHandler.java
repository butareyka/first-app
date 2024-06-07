    package org.example.utility;

    import org.apache.commons.lang3.SerializationUtils;
    import org.example.models.StudyGroup;
    import org.example.models.User;

    import java.io.IOException;
    import java.net.Socket;
    import java.nio.ByteBuffer;
    import java.nio.charset.StandardCharsets;
    import java.util.ArrayList;
    import java.util.Arrays;
    import java.util.Collections;
    import java.util.List;
    import java.util.concurrent.ExecutorService;
    import java.util.concurrent.Executors;
    import java.util.concurrent.locks.ReentrantLock;
    import java.util.logging.Level;
    import java.util.logging.Logger;

    public class ServerHandler implements Runnable {
        private Socket clientSocket;
        private Logger LOGGER;
        private ReentrantLock locker;
        private User user;
        static ExecutorService ftp = Executors.newFixedThreadPool(10);

        public ServerHandler(Socket clientSocket, Logger logger, ReentrantLock reentrantLock) {
            this.clientSocket = clientSocket;
            this.LOGGER = logger;
            this.locker = reentrantLock;
        }

        public ServerHandler(){}

        @Override
        public void run() {
            try {
                // locker.lock();
                while (clientSocket.isConnected()) {
                    // Чтение длины команды
                    byte[] lengthBytes = new byte[Integer.BYTES];
                    int bytesRead = clientSocket.getInputStream().read(lengthBytes);
                    if (bytesRead == -1) {
                        LOGGER.log(Level.INFO, String.format("End of stream reached for port - %s\n", clientSocket.getPort()));
                        break;
                    }

                    int commandLength = ByteBuffer.wrap(lengthBytes).getInt();
                    LOGGER.log(Level.INFO, String.format("Command length: %d", commandLength));
                    if (commandLength == -1) {
                        LOGGER.log(Level.INFO, String.format("Get the end of buffer of port - %s\n", clientSocket.getPort()));
                    } else {
                        // Чтение команды
                        byte[] commandBytes = new byte[commandLength];
                        bytesRead = clientSocket.getInputStream().read(commandBytes);
                        if (bytesRead != commandLength) {
                            LOGGER.log(Level.WARNING, String.format("Expected to read %d bytes, but read %d bytes", commandLength, bytesRead));
                            break;
                        }
                        String request = new String(commandBytes, StandardCharsets.UTF_8);
                        List<String> requestParts = requestHanding(request);
                        String entryCommand = requestParts.get(0);

                        // Чтение пользователя
                        byte[] userBytes = new byte[4096];
                        int userBytesRead = clientSocket.getInputStream().read(userBytes);
                        User user = SerializationUtils.deserialize(Arrays.copyOf(userBytes, userBytesRead));
                        LOGGER.log(Level.INFO, String.format("Server received a request - %s from user - %s", request, user));

                        if (ServerCommandManager.serverCommandsContainsObject.contains(entryCommand)) {
                            // Обработка команды с объектом

                            byte[] dataOutput = new byte[4096];
                            int bytesReadForObject = clientSocket.getInputStream().read(dataOutput);
                            Object commandObject = SerializationUtils.deserialize(Arrays.copyOf(dataOutput, bytesReadForObject));
                            LOGGER.log(Level.INFO, String.format("Command with object - %s", commandObject));
                            ftp.execute(new ServerResponser(clientSocket, LOGGER, locker, request, entryCommand, commandObject, user));
                        } else if (ServerCommandManager.serverCommandsContainsValueAndObject.contains(entryCommand)) {
                            // Обработка команды с значением и объектом
                            byte[] dataOutput = new byte[4096];
                            int bytesReadForObject = clientSocket.getInputStream().read(dataOutput);
                            StudyGroup commandObject = SerializationUtils.deserialize(Arrays.copyOf(dataOutput, bytesReadForObject));
                            commandObject.setGroupId(Long.valueOf(requestParts.get(1)));
                            LOGGER.log(Level.INFO, String.format("Command with StudyGroup object - %s", commandObject));
                            ftp.execute(new ServerResponser(clientSocket, LOGGER, locker, request, entryCommand, commandObject, user));
                        } else {
                            // Обработка команды без объекта
                            LOGGER.log(Level.INFO, String.format("Server got request - %s from user - %s", request, user));
                            ftp.execute(new ServerResponser(clientSocket, LOGGER, locker, request, entryCommand, user));
                        }
                    }
                }
                clientSocket.close();
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, String.format("IOException: %s: port - %s", e.getMessage(), clientSocket.getPort()));
            } finally {
                // locker.unlock();
            }
        }

        public Socket getClientSocket(){
            return clientSocket;
        }

        public List requestHanding(String request){
            List<String> list = new ArrayList<>();

            String[] bufferCommandAndValue;
            bufferCommandAndValue = request.split(" ");
            Collections.addAll(list, bufferCommandAndValue);
            return list;
        }
    }