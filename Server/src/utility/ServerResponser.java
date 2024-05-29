package utility;

import models.User;

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
    private User user;
    private ReentrantLock locker;
    private Object commandObject;

    public ServerResponser(Socket socket, Logger logger, ReentrantLock reentrantLock, String req, String command, User usr){
        this.clientSocket = socket;
        this.LOGGER = logger;
        this.locker = reentrantLock;
        this.request = req;
        this.entryCommand = command;
        this.user = usr;
    }

    public ServerResponser(Socket socket, Logger logger, ReentrantLock reentrantLock, String req, String command, Object object, User usr){
        this.clientSocket = socket;
        this.LOGGER = logger;
        this.locker = reentrantLock;
        this.request = req;
        this.entryCommand = command;
        this.commandObject = object;
        this.user = usr;
    }

    @Override
    public void run() {
        ServerInvoker serverInvoker = new ServerInvoker(locker);
        try {
            locker.lock();
            if (entryCommand.equalsIgnoreCase("quit")) {
                clientSocket.getOutputStream().write(serverInvoker.invoke(request, null, user).getBytes());
                clientSocket.getOutputStream().flush();
                LOGGER.log(Level.INFO, String.format("Server sent a response of command - %s to port %s\n", entryCommand, clientSocket.getPort()));
            }
            else if (ServerCommandManager.serverCommandsContainsObject.contains(entryCommand)) {
                clientSocket.getOutputStream().write(serverInvoker.invoke(request, commandObject, user).getBytes());
                clientSocket.getOutputStream().flush();
                LOGGER.log(Level.INFO, String.format("Server sent a response of command - %s to port %s\n", entryCommand, clientSocket.getPort()));
            }
            else if (ServerCommandManager.serverCommandsContainsValueAndObject.contains(entryCommand)){
                clientSocket.getOutputStream().write(serverInvoker.invoke(request, commandObject, user).getBytes());
                clientSocket.getOutputStream().flush();
                LOGGER.log(Level.INFO, String.format("Server sent a response of command - %s to port %s\n", entryCommand, clientSocket.getPort()));
            }
            else {
                System.out.println(user);
                clientSocket.getOutputStream().write(serverInvoker.invoke(request, null, user).getBytes());
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
