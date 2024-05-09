package commands;

import java.io.Serializable;

public class Register extends ServerCommand implements Serializable {
    public Register(){
        super("register", "регистрация пользователя.");
    }

    @Override
    public Object executionForResponse(Object value){
        return null;
    }
}
