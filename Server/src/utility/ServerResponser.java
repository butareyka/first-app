package utility;

import models.StudyGroup;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerResponser implements Runnable{
    private final Socket clientSocket;
    private final Logger LOGGER;
    private final String request;
    private final String entryCommand;
    private ReentrantLock locker;
    private Object commandObject;

    public ServerResponser(Socket socket, Logger logger, ReentrantLock reentrantLock, String req, String command){
        this.clientSocket = socket;
        this.LOGGER = logger;
        this.locker = reentrantLock;
        this.request = req;
        this.entryCommand = command;
    }

    public ServerResponser(Socket socket, Logger logger, ReentrantLock reentrantLock, String req, String command, Object object){
        this.clientSocket = socket;
        this.LOGGER = logger;
        this.locker = reentrantLock;
        this.request = req;
        this.entryCommand = command;
        this.commandObject = object;
    }

    @Override
    public void run() {
        ServerInvoker serverInvoker = new ServerInvoker();
        try {
            locker.lock();
            if (entryCommand.equals("OKAY?")) {
                clientSocket.getOutputStream().write("ALL OKAY ILON MASK".getBytes());
                clientSocket.getOutputStream().flush();
                LOGGER.log(Level.INFO, "Unpredictable request!\n");
                clientSocket.close();
                LOGGER.log(Level.INFO, String.format("Connection closed for port - %s\n", clientSocket.getPort()));
            }
            else if (entryCommand.equalsIgnoreCase("quit")) {
                clientSocket.getOutputStream().write(serverInvoker.invoke(request, null).getBytes());
                clientSocket.getOutputStream().flush();
                LOGGER.log(Level.INFO, String.format("Server sent a response of command - %s to port %s\n", entryCommand, clientSocket.getPort()));
            }
            else if (ServerCommandManager.serverCommandsContainsObject.contains(entryCommand)) {
                clientSocket.getOutputStream().write(serverInvoker.invoke(request, commandObject).getBytes());
                clientSocket.getOutputStream().flush();
                LOGGER.log(Level.INFO, String.format("Server sent a response of command - %s to port %s\n", entryCommand, clientSocket.getPort()));
            }
            else if (ServerCommandManager.serverCommandsContainsValueAndObject.contains(entryCommand)){
                clientSocket.getOutputStream().write(serverInvoker.invoke(request, commandObject).getBytes());
                clientSocket.getOutputStream().flush();
                LOGGER.log(Level.INFO, String.format("Server sent a response of command - %s to port %s\n", entryCommand, clientSocket.getPort()));
            }
            else {
                clientSocket.getOutputStream().write(serverInvoker.invoke(request, null).getBytes());
                clientSocket.getOutputStream().flush();
                LOGGER.log(Level.INFO, String.format("Server sent a response of command - %s to port %s\n", entryCommand, clientSocket.getPort()));
            }
        } catch (IOException e){
            throw new RuntimeException();
        } finally {
            locker.unlock();
        }
    }
}
