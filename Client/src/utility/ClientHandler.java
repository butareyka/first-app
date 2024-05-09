package utility;

import exceptions.ServerUnavailableException;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

import static utility.ClientInvoker.clientInvoker;

public class ClientHandler {
    public String receiveResponse() throws IOException, ClassNotFoundException, ServerUnavailableException {
        SocketChannel channel = clientInvoker.getSocketChannel();
        ByteBuffer buffer = ByteBuffer.allocate(4096);
        int bytesRead = channel.read(buffer);
        if (bytesRead == -1) {
            throw new ServerUnavailableException();
        }
        buffer.rewind();
        String response = StandardCharsets.UTF_8.decode(buffer).toString().trim();
        buffer.clear();
        return responseProcessing(response);
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
