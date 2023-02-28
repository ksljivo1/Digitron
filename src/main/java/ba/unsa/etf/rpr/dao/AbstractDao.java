package ba.unsa.etf.rpr.dao;

import ba.unsa.etf.rpr.domain.Idable;
import ba.unsa.etf.rpr.exceptions.DigitronException;

import java.sql.*;
import java.util.*;

/**
 * Abstract class that implements core DAO CRUD methods for every entity
 *
 * @author Dino Keco
 */

public abstract class AbstractDao<T extends Idable> implements Dao<T> {
    protected static Connection connection;
    private String tableName;

    public AbstractDao(String tableName) {
        this.tableName = tableName;
        createConnection();
    }

    protected static void createConnection() {
        if(connection != null) return;
        try {
            Properties p = new Properties();
            p.load(ClassLoader.getSystemResource("application.properties").openStream());
            String url = p.getProperty("db.connection_string");
            String username = p.getProperty("db.username");
            String password = p.getProperty("db.password");
            connection = DriverManager.getConnection(url, username, password);
        }
        catch(Exception e) {
            System.out.println("Nemoguce uspostaviti konekciju na bazu");
            e.printStackTrace();
        }
        finally {
            /*Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });*/
        }
    }

    public static Connection getConnection(){
        return connection;
    }

    public abstract T row2object(ResultSet rs) throws DigitronException, SQLException;

    public abstract Map<String, Object> object2row(T object);

    public T getById(int id) throws DigitronException {
        String query = "SELECT * FROM " + tableName + " WHERE id = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                T result = row2object(rs);
                rs.close();
                return result;
            }
            else {
                throw new DigitronException("Object not found");

            }
        }
        catch(SQLException e) {
            if(e.getMessage().contains("inactivity")) {
                connection = null;
                createConnection();
                getById(id);
            }
            else throw new DigitronException(e.getMessage(), e);
        }
        return null;
    }

    public List<T> getAll() throws DigitronException {
        String query = "SELECT * FROM " + tableName;
        List<T> results = new ArrayList<T>();
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                T object = row2object(rs);
                results.add(object);
            }
            rs.close();
            return results;
        }
        catch(SQLException e) {
            if(e.getMessage().contains("inactivity")) {
                connection = null;
                createConnection();
                return getAll();
            }
           else throw new DigitronException(e.getMessage(), e);
        }
    }

    public void delete(int id) throws DigitronException {
        String sql = "DELETE FROM " + tableName + " WHERE id = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setObject(1, id);
            stmt.executeUpdate();
        }
        catch(SQLException e) {
            if(e.getMessage().contains("inactivity")) {
                connection = null;
                createConnection();
                delete(id);
            }
            else throw new DigitronException(e.getMessage(), e);
        }
    }

    private Map.Entry<String, String> prepareInsertParts(Map<String, Object> row) {
        StringBuilder columns = new StringBuilder();
        StringBuilder questions = new StringBuilder();
        int counter = 0;
        for(Map.Entry<String, Object> entry : row.entrySet()) {
            counter = counter + 1;
            if(entry.getKey().equals("id")) continue;
            columns.append(entry.getKey());
            questions.append("?");
            if(row.size() != counter) {
                columns.append(",");
                questions.append(",");
            }
        }
        return new AbstractMap.SimpleEntry<>(columns.toString(), questions.toString());
    }

    public T add(T item) throws DigitronException {
        Map<String, Object> row = object2row(item);
        Map.Entry<String, String> columns = prepareInsertParts(row);
        StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO ").append(tableName);
        builder.append(" (").append(columns.getKey()).append(") ");
        builder.append("VALUES (").append(columns.getValue()).append(")");
        try {
            PreparedStatement stmt = connection.prepareStatement(builder.toString(), Statement.RETURN_GENERATED_KEYS);
            int counter = 1;
            for (Map.Entry<String, Object> entry: row.entrySet()) {
                if (entry.getKey().equals("id")) continue;
                stmt.setObject(counter, entry.getValue());
                counter = counter + 1;
            }
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            item.setId(rs.getInt(1));
            return item;
        }
        catch(SQLException e) {
            if(e.getMessage().contains("inactivity")) {
                connection = null;
                createConnection();
                return add(item);
            }
            else throw new DigitronException(e.getMessage(), e);
        }
    }

    private String prepareUpdateParts(Map<String, Object> row){
        StringBuilder columns = new StringBuilder();

        int counter = 0;
        for(Map.Entry<String, Object> entry : row.entrySet()) {
            counter = counter + 1;
            if(entry.getKey().equals("id")) continue;
            columns.append(entry.getKey()).append("= ?");
            if(row.size() != counter) {
                columns.append(",");
            }
        }
        return columns.toString();
    }

    public T update(T item) throws DigitronException {
        Map<String, Object> row = object2row(item);
        String updateColumns = prepareUpdateParts(row);
        StringBuilder builder = new StringBuilder();
        builder.append("UPDATE ")
                .append(tableName)
                .append(" SET ")
                .append(updateColumns)
                .append(" WHERE id = ?");
        try {
            PreparedStatement stmt = connection.prepareStatement(builder.toString());
            int counter = 1;
            for(Map.Entry<String, Object> entry: row.entrySet()) {
                if (entry.getKey().equals("id")) continue;
                stmt.setObject(counter, entry.getValue());
                counter = counter + 1;
            }
            stmt.setObject(counter, item.getId());
            stmt.executeUpdate();
            return item;
        }
        catch(SQLException e) {
            if(e.getMessage().contains("inactivity")) {
                connection = null;
                createConnection();
                return update(item);
            }
            else throw new DigitronException(e.getMessage(), e);
        }
    }
}
