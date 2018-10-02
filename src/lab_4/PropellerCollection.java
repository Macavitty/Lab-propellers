package lab_4;

import com.google.gson.*;
import lab_4.story_components.Karlson;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


/**
 * Класс для управления коллекцией пропеллеров.
 *
 * @author Прилуцкая Т.
 */

public class PropellerCollection implements Serializable {
    private Map<String, Karlson.Propeller> propellerMap = new ConcurrentHashMap<>();
    private String result = "";

    public Map<String, Karlson.Propeller> getPropellerMap() {
        return propellerMap;
    }

    /**
     * Метод для записи пропеллеров из файла в формате csv в коллекцию
     */
    public String load() {

        /*
         * using InputStreamReader
         *
         * The header:
         * MODEL, YEAR, SIZE, SPEED, MAXWEIGHT, FANS
         * */

        /*BufferedReader reader = null;
        try{
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName))); //BR in = new BR(new FileReader(fileName)); , StandardCharsets.UTF_8
        }catch(FileNotFoundException e) {
            System.out.printf("Файла с  именем  %s не существует. \n", fileName);
        }*/
        String fileName = System.getenv("whereArePropellers");
        String line = "";
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)))) {
            line = reader.readLine(); // skip the header
            propellerMap.clear();
            while (true) {
                line = reader.readLine();
                if (line != null) {
                    List<String> parameters = LameCSVParser.parseItPlease(line);

                    Karlson.Propeller propeller = new Karlson.Propeller();

                    propeller.setModel(parameters.get(0));
                    propeller.setYear(Integer.parseInt(parameters.get(1)));
                    propeller.setSize(Integer.parseInt(parameters.get(2)));
                    propeller.setSpeed(Integer.parseInt(parameters.get(3)));
                    propeller.setMaxWeight(Integer.parseInt(parameters.get(4)));
                    propeller.setColor(parameters.get(5));
                    propeller.setFans(new ArrayList<>());
                    ArrayList<String> fan = LameCSVParser.parseItPlease(parameters.get(6));
                    for (String s : fan) {
                        propeller.getFans().add(s);
                    }
                    propellerMap.put(parameters.get(0), propeller); // remove at the end
                    save();
                } else break;
            }
            result = "Loaded";
            return result;

        } catch (FileNotFoundException e) {
            System.err.printf("File %s doesn`t exists \n", fileName);
            result = "";
            return result;
        } catch (IOException e) {
            result = "collection hasn`t been loaded";
            e.printStackTrace();
            return result;
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return "Sorry, smth went wrong";
        }

    }

    public Map<String, Object> info() {
        Map<String, Object> map = new HashMap<>();

        Gson gson = new Gson();
        map.put("collection", new ArrayList<String>(propellerMap.values().stream().map(propeller -> {
            return gson.toJson(propeller);
        }).collect(Collectors.toList())));

        return map;
    }

    /**
     * Метод для сохранения коллекции в файл в формате csv.
     */
    public String save() {

        String fileName = System.getenv("savedPropellers");

        String separator = ",";
        char quote = '"';
        String newLine = "\n";
        String header = "MODEL,YEAR,SIZE,SPEED,MAX_WEIGHT,COLOR,FANS";
        try {
            BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(fileName));
            boolean hasSpace = false;
            output.write(header.getBytes());
            for (String key : propellerMap.keySet()) {

                // tellStory new line
                output.write(newLine.getBytes());

                // put model
                if (propellerMap.get(key).getModel().matches((".*\\s.*"))
                        || propellerMap.get(key).getModel().matches(".*\".*")) {
                    output.write(quote);
                    hasSpace = true;
                }
                output.write(propellerMap.get(key).getModel().getBytes());
                if (hasSpace) {
                    output.write(quote);
                    hasSpace = false;
                }

                // separator
                output.write(separator.getBytes());

                // put year
                output.write((propellerMap.get(key).getYear() + separator).getBytes());

                // put size
                output.write((propellerMap.get(key).getSize() + separator).getBytes());

                // put speed
                output.write((propellerMap.get(key).getSpeed() + separator).getBytes());

                // put max_weight
                output.write((propellerMap.get(key).getMaxWeight() + separator).getBytes());

                // put color
                if (propellerMap.get(key).getColor().matches((".*\\s.*"))
                        || propellerMap.get(key).getColor().matches(".*\".*")) {
                    output.write(quote);
                    hasSpace = true;
                }
                output.write(propellerMap.get(key).getColor().getBytes());
                if (hasSpace) {
                    output.write(quote);
                    hasSpace = false;
                }

                // separator
                output.write(separator.getBytes());

                // put fans
                String fan = propellerMap.get(key).getFans().toString();
                output.write(quote);
                output.write(fan.substring(1, fan.length() - 1).getBytes());
                output.write(quote);
                // separator

            }
            result = "Saved";
            output.flush();
            output.close();
            return result;
        } catch (IOException e) {
            System.out.printf("Файла с  именем  %s не существует /n", fileName);
            return "Файла с  именем " + fileName + " не существует";
        }
    }


    /**
     * Метод для удаления из коллекции
     * пропеллера, модель которого
     * указана во входной строке.
     *
     * @param key модель пропеллера, который нужно удалить
     */
    public String remove(String key) {
        try {
            if (!propellerMap.containsKey(key))
                result = "* propeller \"" + key + "\" does not exists *";
            else {
                propellerMap.remove(key);
                result = "* propeller \"" + key + "\" has been removed *";
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            result = "";
        }
        save();
        return result;
    }


    /**
     * Метод для удаления из коллекции
     * пропеллеров, название модели которых стоит
     * ниже по алфовитному порядку от
     * указанного во входной строке.
     *
     * @param greatKey модель для сравнения
     */
    public String removeGreaterKey(String greatKey) {

        int tmp = 0;
        if (propellerMap.keySet().removeIf(a -> a.compareTo(greatKey) > 0)) tmp++;
        result = tmp + " pripeller(s) has(ve) been removed";
        save();
        return result;
    }

    /**
     * Метод, добавляющий в коллекцию новый элемент,
     * если его значение превышает значение наибольшего
     * элемента этой коллекции.
     * Єлемент должен быть в формате json.
     */
    public String addIfMax(String s) {
        Gson gson = new Gson();

        Karlson.Propeller propeller = gson.fromJson(s, Karlson.Propeller.class);
        if (propellerMap.keySet().stream().max(String::compareTo).get().compareTo(propeller.getModel()) > 0) {
            propellerMap.put(propeller.getModel(), propeller);
            result = "Propeller " + propeller.getModel() + " has been added";
        }
        save();
        return result;
    }

    public String add(String s) {
        Gson gson = new Gson();
        Karlson.Propeller propeller = gson.fromJson(s, Karlson.Propeller.class);
        if (propellerMap.keySet().stream().noneMatch(a -> a.compareTo(propeller.getModel()) == 0)) {
            propellerMap.put(propeller.getModel(), propeller);
            result = "Propeller " + propeller.getModel() + " has been added";
            save();
        } else result = "We already have propeller " + propeller.getModel() + "\n Send another one";
        return result;
    }

    /*
     * метод записывающий обекты коллекции в файл в формате json
     * */

    public String toJson(String model) {
        String fileName = System.getenv("to_json");
        try (BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(fileName))) {
            Gson gson = new GsonBuilder().create();
            output.write(gson.toJson(propellerMap.get(model)).getBytes());
            result = "Done";
        } catch (IOException e) {
            e.printStackTrace();
            result = "Sorry smth went wrong";
        }
        return result;
    }

    public String clear() {
        propellerMap.clear();
        result = "Done: collection is empty";
        return result;
    }

    public String size() {
        result = "* size is " + propellerMap.size() + " *";
        return result;
    }

    public int getSize() {
        return propellerMap.size();
    }


}