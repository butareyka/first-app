package org.example.commands;

import org.example.daba.DataBaseManager;
import org.example.models.StudyGroup;
import org.example.models.User;
import org.example.utility.ServerCollectionManager;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

public class MaxByCoordinates extends ServerCommand implements Serializable {
    public MaxByCoordinates() {
        super("max_by_coordinates", "вывести любой объект из коллекции, значение поля coordinates которого является максимальным.");
    }

    @Override
    public Object executionForResponse(Object value, User user) throws IOException {
        DataBaseManager dataBaseManager = new DataBaseManager();
        if (dataBaseManager.checkUser(user.getUserName())) {
            Long idElementWithMaxCoordinates = 0L;
            double xMax = 0;
            int yMax = 0;
            double prevXMax;
            for (Map.Entry<Long, StudyGroup> item : ServerCollectionManager.group.entrySet()) {
                if (item.getValue().getCoordinates().getX() > xMax) {
                    prevXMax = xMax;
                    xMax = item.getValue().getCoordinates().getX();
                } else {
                    continue;
                }
                if (item.getValue().getCoordinates().getY() > yMax) {
                    yMax = item.getValue().getCoordinates().getY();
                    idElementWithMaxCoordinates = item.getKey();
                } else {
                    xMax = prevXMax;
                }
            }
            return "{" + idElementWithMaxCoordinates + "=" + ServerCollectionManager.group.get(idElementWithMaxCoordinates) + "}";
        } else {
            return "You need to reg or log_in";
        }
    }

}

