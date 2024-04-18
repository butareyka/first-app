package commands;

import models.Command;
import models.StudyGroup;
import utility.CollectionManager;
import utility.FileManager;
import utility.SortManager;

import java.util.*;

/**
 * Команда 'remove_lower'. Удаляет из коллекции все элементы, меньшие, чем заданный.
 * @author butareyka
 */
public class RemoveLower extends Command {
    public RemoveLower(){
        super("remove_lower", "удалить из коллекции все элементы, меньшие, чем заданный");
    }

    /**
     * Выполняет команду
     */
    @Override
    public void executionResponse(String value) {
        System.out.println("Введите новый элемент коллекции: ");
        CollectionManager collectionManager = new CollectionManager();
        StudyGroup gr = new StudyGroup(collectionManager.addGroupId(), collectionManager.addGroupName(), collectionManager.addCreationDate(), collectionManager.addCoordinates(), collectionManager.addStudentsCount(), collectionManager.addExpelledStudents(), collectionManager.addTransferredStudents(), collectionManager.addFormOfEducation(), collectionManager.addGroupAdmin());
        CollectionManager.group.put(gr.getGroupId(), gr);
        new Save().executionResponse("");

        new FileManager().convertStringToCollection();
        Comparator<StudyGroup> groupComparator = new SortManager().sortExpelledStudents();
        List<StudyGroup> sortedGroup = new ArrayList<>(CollectionManager.group.values());
        List<StudyGroup> sortedFileGroup =  new ArrayList<>(FileManager.fileGroup.values());

        Collections.sort(sortedGroup, groupComparator);
        Collections.sort(sortedFileGroup, groupComparator);

        ListIterator<StudyGroup> iterator1 = sortedGroup.listIterator(sortedGroup.size());
        ListIterator<StudyGroup> iterator2 = sortedFileGroup.listIterator(sortedFileGroup.size());


        while (iterator1.hasPrevious()){
            StudyGroup item = iterator1.previous();
            Long key = item.getGroupId();

            if (!Objects.equals(key, gr.getGroupId())){
                iterator1.remove();
            }
            else {
                break;
            }
        }

        while (iterator2.hasPrevious()){
            StudyGroup item = iterator2.previous();
            Long key = item.getGroupId();

            if (!Objects.equals(key, gr.getGroupId())){
                iterator2.remove();
            }
            else {
                break;
            }
        }

        System.out.println("Элементы коллекции: ");
        for (StudyGroup item: sortedGroup){
            System.out.println("{" + item.getGroupId() + "=" + item + "}");
        }
        System.out.println("Сохраненные элементы коллекции: ");
        for (StudyGroup item: sortedFileGroup){
            System.out.println("{" + item.getGroupId() + "=" + item + "}");
        }
    }
}
