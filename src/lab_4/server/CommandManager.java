package lab_4.server;

import com.google.gson.Gson;
import lab_4.LameCSVParser;
import lab_4.PropellerCollection;
import lab_4.story_components.Story;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

public class CommandManager {

    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private String userInput = "";
    private String command = "";
    private String argument = "";
    private PropellerCollection propellerCollection;
    Story story = new Story();

    public CommandManager(ObjectOutputStream oos, ObjectInputStream ois, PropellerCollection p) {
        this.oos = oos;
        this.ois = ois;
        propellerCollection = p;
        propellerCollection.load();


        String result;
        Method method;

        try {
            while ((userInput = (String) ois.readObject()) != null) {
                System.out.println("got from client: " + userInput);
                command = userInput;
                argument = "";
                // parsing command
                if (command.matches(".* .*")) {
                    int i = userInput.indexOf(" ");
                    command = userInput.substring(0, i);
                    argument = userInput.substring(i + 1, userInput.length());
                }

                try {
                    if (argument.equals("")) {
                        method = PropellerCollection.class.getMethod(command);
                        result = (String) method.invoke(propellerCollection);
                    } else {
                        method = PropellerCollection.class.getMethod(command, String.class);
                        result = (String) method.invoke(propellerCollection, argument);
                    }
                    oos.writeObject(result);
                } catch (NoSuchMethodException e) {
                    switch (command) {

                        case "tell_me":
                            if (propellerCollection.getPropellerMap().isEmpty())
                                oos.writeObject("* Hang on! Add at least one propeller.\n  No propeller - no story! *");
                            else try {
                                story.tellStory(propellerCollection, oos);
                            } catch (Throwable t) {/**/}
                            break;

                        case "getMap":
                            oos.writeObject(propellerCollection.getPropellerMap());
                            break;

                        default:
                            oos.writeObject("* wrong command format *");
                    }

                } catch (InvocationTargetException | IllegalAccessException t) {
                    //t.printStackTrace();
                    System.out.println("can not invoke method");

                }
            }
        } catch (IOException e) {
            System.out.println("client was disconnected");
        } catch (ClassNotFoundException ce) {
            try {
                oos.writeObject("have no idea what to do with your input");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}