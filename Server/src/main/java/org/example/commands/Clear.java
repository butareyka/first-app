package org.example.commands;

import org.example.daba.DataBaseManager;
import org.example.models.User;
import org.example.utility.ServerCollectionManager;

import java.io.IOException;
import java.io.Serializable;

public class Clear extends ServerCommand implements Serializable {
    public Clear(){
        super("clear", "очистить коллекцию.");
    }

    @Override
    public Object executionForResponse(Object value, User user) throws IOException {
        DataBaseManager dataBaseManager = new DataBaseManager();
        if (dataBaseManager.checkUser(user.getUserName())){
            ServerCollectionManager.group.clear();
            new Save().executionForResponse(null, user);
            return "1";
        } else {
            return "0";
        }
    }
}
