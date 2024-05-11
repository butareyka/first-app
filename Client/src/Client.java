import commands.*;
import exceptions.ServerUnavailableException;
import utility.ClientCommandManager;
import utility.ClientHandler;
import utility.ClientRequester;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.*;

import static utility.ClientInvoker.clientInvoker;

public class Client {
    private static String SERVER_ADDRESS;
    private static int SERVER_PORT;
    private static final int CONNECTION_TIMEOUT = 5000;
    static ClientRequester clientRequester = new ClientRequester();
    static ClientHandler clientHandler = new ClientHandler();

    public static void main(String[] args) {
        new ClientCommandManager() {{
            register("insertObject", new InsertObject());
            register("exit", new Exit());
            register("execute_script", new ExecuteScript());
            register("log_in", new ClientAuthorization());
            register("register", new ClientRegister());
        }};

        new ClientCommandManager() {{
            registerClientCommandsContainsObject("insert");
            registerClientCommandsContainsObject("remove_greater");
            registerClientCommandsContainsObject("remove_lower");
            registerClientCommandsContainsObject("log_in");
            registerClientCommandsContainsObject("register");
        }};

        new ClientCommandManager() {{
            registerClientCommandsContainsValueAndObject("update");
        }};
        connecting();
    }

    public static void connecting() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Print server address: ");
            SERVER_ADDRESS = scanner.nextLine();
            System.out.println("Print server port: ");
            SERVER_PORT = scanner.nextInt();

            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress(SERVER_ADDRESS, SERVER_PORT));

            ByteBuffer byteBuffer = ByteBuffer.allocate(2048);
            clientInvoker.setBuffer(byteBuffer);
            clientInvoker.setSocketChannel(socketChannel);

            if (checkFirstConnection()){
                System.out.println("Connection to the server is established");
            } else {
                System.out.println("Connection to the server is not established");
                connecting();
            }

            while (true) {
                if (ping(SERVER_ADDRESS, SERVER_PORT, CONNECTION_TIMEOUT)){
                    System.out.println("What do you wanna do with massage:\n1 - send\n2 - receive");
                    String actionWithMassage = scanner.next();
                    if (actionWithMassage.equals("1") || actionWithMassage.equals("send") && socketChannel.isConnected()) {
                        System.out.println("What command do you wanna send the server?");
                        String request = clientRequester.makeRequest();
                        clientRequester.sendRequest(request);
                    }
                    if (actionWithMassage.equals("2") || actionWithMassage.equals("receive")) {
                        System.out.println(clientHandler.receiveResponse());
                    }
                } else {
                    throw new ServerUnavailableException();
                }
            }
        } catch (IOException | ServerUnavailableException | ClassNotFoundException e) {
            System.out.println("IOException - " + e.getMessage());
            connecting();
        }
    }

    public static boolean ping(String host, int port, int timeout) {
        try {
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.socket().setSoTimeout(timeout);
            socketChannel.connect(new InetSocketAddress(host, port));

            ByteBuffer buffer = ByteBuffer.allocate(5);
            buffer.put("OKAY?".getBytes());
            buffer.flip();
            socketChannel.write(buffer);
            buffer.clear();

            int bytesRead = socketChannel.read(buffer);
            socketChannel.close();

            return bytesRead > 0;
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean checkFirstConnection(){
        try {
            Selector selector = Selector.open();
            clientInvoker.getSocketChannel().register(selector, SelectionKey.OP_CONNECT);
            selector.select(CONNECTION_TIMEOUT);
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            if (selectedKeys.isEmpty()) {
                System.out.println("Connection timeout");
            } else {
                clientInvoker.getSocketChannel().finishConnect();
            }
            selector.close();
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        }
        return ping(SERVER_ADDRESS, SERVER_PORT, CONNECTION_TIMEOUT);
    }

//    public static boolean authorization(){
//        Scanner scanner = new Scanner(System.in);
//        try {
//            System.out.println("Enter username:");
//            String userName = scanner.next();
//
//            Terminal terminal = TerminalBuilder.builder().system(true).build();
//            LineReader reader = LineReaderBuilder.builder().terminal(terminal).build();
//            String password = reader.readLine("Enter password:", '*');
//            String passwordAgain = reader.readLine("Enter password again:", '*');
//            if (passwordAgain.equals(password)){
//                clientRequester.sendRequest("authorization");
//            } else {
//                authorization();
//            }
//
//            if (clientHandler.receiveResponse().equals("Don't exist")){
//                System.out.printf("User with name %s doesn't exist or you made mistake in name or password. Try again!", userName);
//                return false;
//            } else {
//                System.out.printf("You successfully logged in as %s%n", userName);
//                return true;
//            }
//
//        } catch (IOException | ClassNotFoundException | ServerUnavailableException e) {
//            throw new RuntimeException(e);
//        }
//    }

//    public static void register(String startWith) {
//        try {
//            if (clientHandler.receiveResponse().equals("Enter new user's name: ")){
//                String newName = clientRequester.makeRequest();
//                clientRequester.sendRequest(newName);
//            } else if (clientHandler.receiveResponse().equals("New user was successfully register!")){
//                System.out.println("New user was successfully register!");
//            } else {
////                register();
//            }
//        } catch (IOException | ServerUnavailableException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
