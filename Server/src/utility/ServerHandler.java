package utility;

import models.StudyGroup;
import org.apache.commons.lang3.SerializationUtils;

import java.util.concurrent.locks.ReentrantLock;
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
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerHandler implements Runnable {
    private Socket clientSocket;
    private Logger LOGGER;
    private ReentrantLock locker;
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
//            locker.lock();
            while (clientSocket.isConnected()){
                byte[] data = new byte[4096];
                int bytesRead = clientSocket.getInputStream().read(data);
                if (bytesRead == -1){
                    LOGGER.log(Level.INFO, String.format("Get the end off buffer of port - %s\n", clientSocket.getPort()));
                } else {
                    ByteBuffer buffer = ByteBuffer.wrap(data, 0, bytesRead);
                    buffer.rewind();
                    String request = StandardCharsets.UTF_8.decode(buffer).toString().trim();
                    buffer.clear();

                    String entryCommand = (String) requestHanding(request).get(0);
                    LOGGER.log(Level.INFO, String.format("Server received a request - %s from port %s\n", request, clientSocket.getPort()));

                    if (ServerCommandManager.serverCommandsContainsObject.contains(entryCommand)) {
                        byte[] dataOutput = new byte[4096];
                        int bytesReadForObject = clientSocket.getInputStream().read(dataOutput);
                        Object commandObject = SerializationUtils.deserialize(Arrays.copyOf(dataOutput, bytesReadForObject));
                        LOGGER.log(Level.INFO, String.format("Server got request - %s with object - %s\n", entryCommand, commandObject));
                        ftp.execute(new ServerResponser(clientSocket, LOGGER, locker, request, entryCommand, commandObject));
                    } else if (ServerCommandManager.serverCommandsContainsValueAndObject.contains(entryCommand)) {
                        byte[] dataOutput = new byte[4096];
                        int bytesReadForObject = clientSocket.getInputStream().read(dataOutput);
                        StudyGroup commandObject = SerializationUtils.deserialize(Arrays.copyOf(dataOutput, bytesReadForObject));
                        commandObject.setGroupId(Long.valueOf((String) requestHanding(request).get(1)));
                        LOGGER.log(Level.INFO, String.format("Server got request - %s with object - %s\n", request, commandObject));
                        ftp.execute(new ServerResponser(clientSocket, LOGGER, locker, request, entryCommand, commandObject));
                    } else {
                        ftp.execute(new ServerResponser(clientSocket, LOGGER, locker, request, entryCommand));
                    }
                }
            }
            clientSocket.close();
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, String.format("IOException: %s: port - %s\n", e.getMessage(), clientSocket.getPort()));
        } finally {
//            locker.unlock();
        }
    }

    public List requestHanding(String request){
        List<String> list = new ArrayList<>();

        String[] bufferCommandAndValue;
        bufferCommandAndValue = request.split(" ");
        Collections.addAll(list, bufferCommandAndValue);
        return list;
    }
}