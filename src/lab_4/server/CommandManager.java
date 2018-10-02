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
                        case "help":
                        case "aaa":
                            StringBuilder helper = new StringBuilder();
                            helper.append("The command-line interface to the programme\n");
                            helper.append("Type: \t");
                            helper.append((char) 27 + "[35mload " + (char) 27 + "[30mto load the collection of propellers\n");
                            helper.append("\t \t" + (char) 27 + "[35msave " + (char) 27 + "[30mto save the collection of propellers in csv file\n");
                            helper.append("\t \t" + (char) 27 + "[35mtoJson " + (char) 27 + "[30mto save the collection of propellers in json file\n");
                            helper.append("\t \t" + (char) 27 + "[35mclear " + (char) 27 + "[30mto clear the collection of propellers\n");
                            helper.append("\t \t" + (char) 27 + "[35mtell_me " + (char) 27 + "[30mto see the story\n");
                            helper.append("\t \t" + (char) 27 + "[35mremove {propeller_model} " + (char) 27 + "[30mto remove certain propeller\n");
                            helper.append("\t \t" + (char) 27 + "[35mremoveGreaterKey {propeller_model} " + (char) 27 + "[30mto remove all propellers, who`s model`s names are after the specified one`s\n");
                            helper.append("\t \t" + (char) 27 + "[35maddIfMax " + (char) 27 + "[30mto add propeller from json file if it`s size is maximal in the collection\n");

                            oos.writeObject(helper);
                            break;

                        case "info":
                            break;

                        case "tell_me":
                            if (propellerCollection.getPropellerMap().isEmpty())
                                oos.writeObject("* Hang on! Add at least one propeller.\n  No propeller - no story! *");
                            else try {
                                story.tellStory(propellerCollection, oos);
                            } catch (Throwable t) {/**/}
                            break;

                        case "":
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