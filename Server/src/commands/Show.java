package commands;

import daba.DataBaseManager;
import models.StudyGroup;
import utility.ServerCollectionManager;
import utility.SortManager;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class Show extends ServerCommand implements Serializable {
    public Show() {
        super("show", "вывести в стандартный поток вывода все элементы коллекции в строковом представлении.");
    }

    @Override
    public Object executionForResponse(Object value) {
        try {
            DataBaseManager dataBaseManager = new DataBaseManager();
            return dataBaseManager.readFromDataBase();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        Comparator<StudyGroup> groupComparator = new SortManager().sortLocation();
//        File file = new File(System.getenv("MYFILE"));
//        List<StudyGroup> sortedGroupList = ServerCollectionManager.group.values().stream()
//                .sorted(groupComparator).toList();
//
//        if (file.length() == 0 && sortedGroupList.isEmpty()) {
//            return "Collection is empty!";
//        } else {
//            return sortedGroupList.stream()
//                    .collect(Collectors.toMap(StudyGroup::getGroupId, item -> item, (a, b) -> a, LinkedHashMap::new));
//        }
    }
}

