package commands;

import daba.DataBaseManager;
import models.User;

import java.io.IOException;
import java.io.Serializable;

public class Authorization extends ServerCommand implements Serializable {
    public Authorization(){
        super("register", "авторизация пользователя.");
    }

    @Override
    public Object executionForResponse(Object value) throws IOException {
        DataBaseManager dataBaseManager = new DataBaseManager();
        User user = (User) value;
        if (dataBaseManager.checkUser(user.getUserName()) && dataBaseManager.checkPassword(user.getUserName(), user.getPassword())){
            return "You successfully log in!";
        } else {
            return "This user doesn't exist!";
        }
    }
}
