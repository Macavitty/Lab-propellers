package lab_4.orm;

import com.google.gson.Gson;
import lab_4.PropellerCollection;
import lab_4.story_components.Karlson;

import java.io.File;
import java.sql.*;

import java.sql.SQLException;
import java.util.Map;

public class DBManager {
    static String dbURL = "jdbc:postgresql://localhost/proga";
    static String username = "general";
    static String password = "bezoar";
    static Connection conn;
    static Statement statement;
    private PropellerCollection collection;

    public DBManager() {

    }
    public DBManager(PropellerCollection collection) {
        this.collection = collection;
    }

    public void initDB() {
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(dbURL, username, password);
            statement = conn.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void fromFileToDB(PropellerCollection collection) {
        fromFileToDB(collection.loadMapFromFile());

    }
    public void fromFileToDB(Map<String, Karlson.Propeller> propellersMap) {
        if (propellersMap != null)
            propellersMap.forEach((m, p) -> insertProp(p));

    }

    public void insertProp(Karlson.Propeller p) {
        String insert = "INSERT INTO propellers VALUES (?,?,?,?,?,?,?)";
        try {
            PreparedStatement s = conn.prepareStatement(insert);
            s.setString(1, p.getModel());
            s.setInt(2, p.getSize());
            s.setInt(3, p.getMaxWeight());
            s.setInt(4, p.getSpeed());
            s.setInt(5, p.getYear());
            s.setString(6, p.getColor());
            s.setString(7, p.getFans().toString());
            s.executeUpdate();
            s.close();
        } catch (SQLException e) {
           //
            // e.printStackTrace();
        }
    }

    public void insertProp(String s) {
        Gson gson = new Gson();
        Karlson.Propeller p = gson.fromJson(s, Karlson.Propeller.class);
        insertProp(p);
    }

    public PropellerCollection getCollectionFromDB() {
        String select = "SELECT model, size, max, speed, year, color, fans FROM propellers;";
        try {
            ResultSet propellerDB = statement.executeQuery(select);
            if (collection == null){
               collection = new PropellerCollection();
            }
            collection.clear();
            while (propellerDB.next()) {
                collection.add(toJson(propellerDB));
            }
            System.out.println("got from db");
            System.out.println(collection.getPropellerMap().toString());
            return collection;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String toJson(ResultSet propellerDB) throws SQLException {
        return
                "{\"model\":\""
                        + propellerDB.getString("model")
                        + "\",\"year\":"
                        + propellerDB.getString("year")
                        + ",\"size\":"
                        + propellerDB.getString("size")
                        + ",\"speed\":"
                        + propellerDB.getString("speed")
                        + ",\"maxWeight\":"
                        + propellerDB.getString("max")
                        + ",\"color\":\""
                        + propellerDB.getString("color")
                        + "\",\"fans\":[\""
                        + propellerDB.getString("fans")
                        + "\"]}";
    }

    public void deleteRow(String model) {
        String delete = "DELETE FROM propellers WHERE propellers.model = ?";

        try {
            PreparedStatement statement = conn.prepareStatement(delete);
            statement.setString(1, model);
            statement.executeUpdate();
            getCollectionFromDB();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAllRow(PropellerCollection collection) {
        String delete = "DELETE FROM propellers WHERE propellers.model = ?";

        Map<String, Karlson.Propeller> propellersMap = collection.loadMapFromFile();
        if (propellersMap != null)
            propellersMap.forEach((m, p) -> {
                try {
                    PreparedStatement statement = conn.prepareStatement(delete);
                    statement.setString(1, m);
                    statement.executeUpdate();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
    }
}
/*
DROP TABLE propellers;
CREATE TABLE propellers (
	model text PRIMARY KEY,
	size integer,
	max integer,
	speed integer,
	year integer,
	color text,
	fans text
);

*/
