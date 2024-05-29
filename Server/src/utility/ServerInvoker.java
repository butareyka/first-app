
package utility;

import models.User;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class ServerInvoker implements Serializable {
    public static List<String> list = new ArrayList<>();
    ServerHandler serverHandler = new ServerHandler();
    private ReentrantLock locker;

    public ServerInvoker(ReentrantLock reentrantLock){
        this.locker = reentrantLock;
    }

    public String invoke(String request, Object object, User user) throws IOException {
        try{
            locker.lock();
            String entryCommand = (String) serverHandler.requestHanding(request).get(0);
            if (entryCommand.equalsIgnoreCase("quit")) {
                ServerCommandManager.serverCommands.get("save").executionForResponseQuit(null, user, serverHandler.getClientSocket());
                return "\nServer reply - " + entryCommand + "\nChanges from this session were successfully saved!";
            }
            else if (entryCommand.equalsIgnoreCase("save")) {
                return "\nClient does not have access rights to use the save collection command";
            }
            else if (!ServerCommandManager.serverCommands.containsKey(entryCommand)) {
                return "\nCommand doesn't exist - " + entryCommand + "\n";
            }
            else if (ServerCommandManager.serverCommandsContainsValue.contains(entryCommand)) {
                String entryValue = (String) serverHandler.requestHanding(request).get(1);
                return "\nThe server responds to the command: \n" + ServerCommandManager.serverCommands.get(entryCommand).executionForResponse(entryValue, user) + "\n";
            }
            else if (ServerCommandManager.serverCommandsContainsObject.contains(entryCommand)) {
                return "\nThe server responds to the command: \n" + ServerCommandManager.serverCommands.get(entryCommand).executionForResponse(object, user) + "\n";
            }
            else if (ServerCommandManager.serverCommandsContainsValueAndObject.contains(entryCommand)){
                return "\nThe server responds to the command: \n" + ServerCommandManager.serverCommands.get(entryCommand).executionForResponse(object, user) + "\n";
            }
            else {
                return "\nThe server responds to the command: \n" + ServerCommandManager.serverCommands.get(entryCommand).executionForResponse(null, user) + "\n";
            }
        } finally {
            locker.unlock();
        }
    }
}
