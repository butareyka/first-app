package commands;

import daba.DataBaseManager;

import java.io.Serializable;

public class Authorization extends ServerCommand implements Serializable {
    public Authorization(){
        super("register", "авторизация пользователя.");
    }

    @Override
    public Object executionForResponse(Object value){
        DataBaseManager dataBaseManager = new DataBaseManager();
        return null;
    }
}
