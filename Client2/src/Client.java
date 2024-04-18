import commands.ExecuteScript;
import commands.Exit;
import commands.InsertObject;
import utility.ClientCommandManager;
import utility.ClientHandler;
import utility.ClientRequester;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Scanner;
import java.util.Set;

import static utility.ClientInvoker.clientInvoker;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 45000;
    private static final int CONNECTION_TIMEOUT = 5000;

    public static void main(String[] args) {

        ClientRequester clientRequester = new ClientRequester();
        ClientHandler clientHandler = new ClientHandler();

        new ClientCommandManager() {{
            register("insertObject", new InsertObject());
            register("exit", new Exit());
            register("execute_script", new ExecuteScript());
        }};

        new ClientCommandManager() {{
            registerClientCommandsContainsObject("insert");
            registerClientCommandsContainsObject("remove_greater");
            registerClientCommandsContainsObject("remove_lower");

        }};

        new ClientCommandManager() {{
            registerClientCommandsContainsValueAndObject("update");
        }};

        try {
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress(SERVER_ADDRESS, SERVER_PORT));

            Selector selector = Selector.open();
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
            selector.select(CONNECTION_TIMEOUT);
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            if (selectedKeys.isEmpty()) {
                System.out.println("Connection timeout");
                return;
            } else {
                System.out.println("Client successfully connected!");
                socketChannel.finishConnect();
            }
            selector.close();

            ByteBuffer byteBuffer = ByteBuffer.allocate(2048);
            clientInvoker.setBuffer(byteBuffer);
            clientInvoker.setSocketChannel(socketChannel);
            while (true) {
                System.out.println("What do you wanna do with massage:\n1 - send\n2 - receive");
                Scanner scanner = new Scanner(System.in);
                String actionWithMassage = scanner.nextLine();
                if (actionWithMassage.equals("1") || actionWithMassage.equals("send")){
                    System.out.println("What command do you wanna send the server?");
                    String request = clientRequester.makeRequest();
                    clientRequester.sendRequest(request);
                }
                if (actionWithMassage.equals("2") || actionWithMassage.equals("receive")){
                    clientHandler.receiveResponse();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
