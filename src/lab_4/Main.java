package lab_4;
import lab_4.story_components.Story;

import java.io.*;
import java.util.*;

/*to read doc in russian use -encoding utf8 -docencoding utf8 -charset utf8*/

public class Main {

    public static void main(String[] args) {

       /* Scanner consoleCommand = new Scanner(System.in);
        PropellerCollection propellerCollection = new PropellerCollection();
        propellerCollection.load(System.getenv("whereArePropellers"));
        Story story = new Story();
        System.out.print("Type " + (char)27 +"[35mhelp " + (char)27 + "[30mfor help \n");

        /*
        * Управление коллекцией в интерактивном режиме.
        * */
        /*String input;
        while ((input = consoleCommand.nextLine()) != null) {
            String command = input, argument = "";
            if (command.matches(".* .*")){
                int i = input.indexOf(" ");
                command = input.substring(0, i);
                argument = input.substring(i + 1, input.length());
            }
            switch (command) {
                case "help":
                {   System.out.println("The command-line interface to the programme");
                    System.out.print("Type: \t");
                    System.out.println((char)27 + "[35mload " + (char)27 + "[30mto load the collection of propellers");
                    System.out.println("\t \t" + (char)27 + "[35msave " + (char)27 + "[30mto save the collection of propellers in csv file");
                    System.out.println("\t \t" + (char)27 + "[35mtoJson " + (char)27 + "[30mto save the collection of propellers in json file");
                    System.out.println("\t \t" + (char)27 + "[35mclear " + (char)27 + "[30mto clear the collection of propellers");
                    System.out.println("\t \t" + (char)27 + "[35mplay " + (char)27 + "[30mto see the story");
                    System.out.println("\t \t" + (char)27 + "[35mremove propeller_model " + (char)27 + "[30mto remove certain propeller");
                    System.out.println("\t \t" + (char)27 + "[35mremoveGreaterKey propeller_model " + (char)27 + "[30mto remove all propellers, who`s model`s names are after the specified one`s ");
                    System.out.println("\t \t" + (char)27 + "[35maddIfMax " + (char)27 + "[30mto add propeller from json file if it`s efficiency is maximal in the collection");
                    System.out.println("\t \t" + (char)27 + "[35mexit " + (char)27 + "[30mor " + (char)27 +  "[35me " + (char)27 + "[30mto exit \n");}
                    continue;

                case "load":
                    PropellerCollection.load(System.getenv("whereArePropellers")); // change to whereArePropellers / temporaryVar / savedPropellers
                    continue;

                case "save":
                    PropellerCollection.save(System.getenv("savedPropellers"));
                    continue;

                case "clear":
                    PropellerCollection.clear();
                    continue;

                case "exit":
                    return;

                case "remove":
                    PropellerCollection.remove(argument);
                    continue;

                case "e":
                    return;

                case "removeGreaterKey":
                    PropellerCollection.removeGreaterKey(argument);
                    continue;

                case "addIfMax":
                    try {
                        PropellerCollection.addIfMax(System.getenv("from_json"));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    continue;

                case "toJson":
                    PropellerCollection.toJson(System.getenv("to_json"));
                    continue;

                case "play":
                    story.tellStory();
                    continue;
                case "":
                    continue;

                default:
                    System.err.println("Неверный формат команды");
            }
        }

        //Runtime.getRuntime().addShutdownHook(new Thread(()->{PropellerCollection.save(System.getenv("savedPropellers"));}));
        Runtime.getRuntime().addShutdownHook(
            new Thread() {
                public void run() {
                    PropellerCollection.save(System.getenv("savedPropellers"));
                }
            }
        );

    }

    @Override
    public void finalize() throws Throwable {
        PropellerCollection.save(System.getenv("savedPropellers"));
    }*/
    }
}




