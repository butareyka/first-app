package commands;

import models.User;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.apache.commons.lang3.SerializationUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.Scanner;

public class ClientAuthorization extends ClientCommand implements Serializable {
    @Override
    public Object executionForRequestReturn(Object object){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter username:");
        String userName = scanner.next();
        Terminal terminal = null;
        try {
            terminal = TerminalBuilder.builder().system(true).build();
        } catch (IOException e) {
            System.out.println("IOException in builder by jline" + e.getMessage());
        }
        LineReader reader = LineReaderBuilder.builder().terminal(terminal).build();
        String password = reader.readLine("Enter password:", '*');
        String passwordAgain = reader.readLine("Enter password again:", '*');

        if (passwordAgain.equals(password)){
            return SerializationUtils.serialize(new User(userName, password));
        } else {
            executionForRequestReturn(null);
        }
        return null;
    }
}

//            if (clientHandler.receiveResponse().equals("Don't exist")){
//                System.out.printf("User with name %s doesn't exist or you made mistake in name or password. Try again!", userName);
//                return false;
//            } else {
//                System.out.printf("You successfully logged in as %s%n", userName);
//                return true;
//            }
