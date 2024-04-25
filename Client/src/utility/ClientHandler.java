package utility;

import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

import static utility.ClientInvoker.clientInvoker;

public class ClientHandler {
    public void receiveResponse() throws IOException, ClassNotFoundException {
        SocketChannel channel = clientInvoker.getSocketChannel();
        ByteBuffer buffer = ByteBuffer.allocate(4096);
        int bytesRead = channel.read(buffer);
        if (bytesRead == -1) {
            System.err.println("Connection closed by server");
        }
        buffer.rewind();
        String response = StandardCharsets.UTF_8.decode(buffer).toString().trim();
        System.out.println(responseProcessing(response));
        buffer.clear();
    }

    public String responseProcessing(String in){
        String out;
        if (in.contains("\"")){
            out = in.replace("\", ", "}\n{");

        } else {
            out = in.replace("., ", ",\n");
            out = out.replace("{", "");
            out = out.replace("}", "");
        }
        return out;
    }
}