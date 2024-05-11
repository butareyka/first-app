package commands;

import java.io.IOException;
import java.io.Serializable;

public abstract class ServerCommand implements Serializable {
    private final String commandName;
    private final String description;

    public ServerCommand(String commandName, String description){
        this.commandName = commandName;
        this.description = description;
    }



    public String getCommandName(){
        return commandName;
    }

    public String getDescription(){
        return description;
    }
    

    public Object executionForResponse(Object object) throws IOException {
        return null;
    }

    @Override
    public String toString(){
        return getDescription();
    }
}
