package quotationsoftware.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 * Utility class for database specific operations.
 */
public class DbHandler {

    private static final String DATABASE = "jdbc:hsqldb:file:db/easyquote";
    private static final String USERNAME = "";
    private static final String PASSWORD = "";
    private static Connection connection = null;

    /**
     * Returns a connection to database.
     *
     * @return
     * @throws java.lang.Exception
     */
    public static Connection getConnection() throws Exception {
        if (connection == null) {
            Class.forName("org.hsqldb.jdbcDriver");
            connection = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
            connection.setAutoCommit(false);
        }
        return connection;
    }

    /**
     * Unlocks and shuts down the database.
     *
     */
    public synchronized static void shutdown() {
        try {
            if (!connection.isClosed()) {
                try (Statement statement = connection.createStatement();) {
                    statement.execute("SHUTDOWN");
                }
                connection.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Convenient class which returns sql scripts from the class path by name.
     *
     * @param fileName
     * @return
     * @throws java.io.IOException
     */
    public static String getSqlScript(String fileName) throws IOException {
        Scanner s;
        String query;
        try (InputStream is = DbHandler.class.getResourceAsStream(fileName)) {
            s = new Scanner(is).useDelimiter("\\A");
            query = s.hasNext() ? s.next() : "";
        }
        return query;
    }

    /**
     * Convenient class which return the next free autoincrement id from the
     * specifed table.
     *
     * @param table
     * @return
     */
    public static int getNextId(String table) {
        int nextId = 1;
        try {
            try (Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.
                            executeQuery(Keys.MAX_ID_QUERY + table);) {
                if (resultSet.next()) {
                    nextId = resultSet.getInt("next_id");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nextId;
    }
}