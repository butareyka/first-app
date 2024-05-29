package commands;

import models.User;
import org.apache.commons.lang3.SerializationUtils;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.io.Serializable;
import java.util.Scanner;

public class ClientRegister extends ClientCommand implements Serializable {
    @Override
    public Object executionForRequestReturn(Object object){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter new user's name:");
        String userName = scanner.next();
        Terminal terminal = null;
        try {
            terminal = TerminalBuilder.builder().system(true).build();
        } catch (IOException e) {
            System.out.println("IOException in builder by jline" + e.getMessage());
        }
        LineReader reader = LineReaderBuilder.builder().terminal(terminal).build();
        String password = reader.readLine("Enter new user's password:\n", '*');
        String passwordAgain = reader.readLine("Enter new user's password again:\n", '*');

        if (passwordAgain.equals(password)){
            return new User(userName, password);
        } else {
            executionForRequestReturn(null);
        }
        return null;
    }
}
