package gmail.alexejkrawez.db;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import static gmail.alexejkrawez.app.entities.ConnectionDAO.logger;


public class DBConnector {

    private final static String DRIVER = "driver";
    private final static String URL = "url";
    private final static String USERNAME = "username";
    private final static String PASSWORD = "password";

    private static volatile DBConnector instance;
    private Connection connection;


    private static final String DATABASE_NAME = "database_name";

    private static final String CREATE_DATABASE_1 = "CREATE DATABASE IF NOT EXISTS ";
    private static final String CREATE_DATABASE_2 = " CHARACTER SET utf8 COLLATE utf8_general_ci;";

//    private static final String USE_DATABASE = "USE " + DATABASE_NAME + ";";

    private static final String CREATE_USERS_TABLE = "CREATE TABLE users (" +
            "user_id int(9) PRIMARY KEY NOT NULL AUTO_INCREMENT," +
            "email char(50) NOT NULL," +
            "login char(30) NOT NULL," +
            "password char(30) NOT NULL" +
            ") ENGINE=InnoDB;";

    private static final String CREATE_UNIQUE_USERS_ID_INDEX = "CREATE UNIQUE INDEX users_user_id_uindex ON users (user_id);";

    private static final String CREATE_UNIQUE_EMAIL_INDEX = "CREATE UNIQUE INDEX users_email_uindex ON users (email);";

    private static final String CREATE_UNIQUE_LOGIN_INDEX = "CREATE UNIQUE INDEX users_login_uindex ON users (login);";

    private static final String SET_TIME_ZONE = "SET time_zone = '+00:00';";

    private static final String CREATE_NOTES_TABLE = "CREATE TABLE notes (" +
            "user_id int(9) NOT NULL," +
            "id int(9) PRIMARY KEY NOT NULL AUTO_INCREMENT," +
            "date DATETIME DEFAULT CURRENT_TIMESTAMP," +
            "target_date DATETIME DEFAULT CURRENT_TIMESTAMP," +
            "note TEXT(500) DEFAULT NULL," +
            "file_path varchar(255) DEFAULT NULL," +
            "status TINYINT(1) DEFAULT 1 NOT NULL," +
            "FOREIGN KEY notes_users__fk(user_id)" +
            "REFERENCES users(user_id)" +
            "ON DELETE CASCADE " +
            "ON UPDATE CASCADE" +
            ") ENGINE=InnoDB;";

    private static final String CREATE_UNIQUE_NOTES_ID_INDEX = "CREATE UNIQUE INDEX notes_id_uindex ON notes (id);";

    private static final String DB_PATH = Thread.currentThread().getContextClassLoader().getResource("db.properties").getPath();

    private final Properties dbProperties = new Properties();

    private void loadSQLScript() {
        try {
            String line;
            Process p = Runtime.getRuntime().exec
                    ("psql -U username -d dbname -h serverhost -f scripfile.sql");
            BufferedReader input =
                    new BufferedReader
                            (new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
                System.out.println(line);
            }
            input.close();
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    private void loadProperties() {
        try {
            dbProperties.load(new FileInputStream(DB_PATH));
        } catch (IOException e) {
            logger.error("Cannot load db.properties.");
            logger.error(e.getMessage(), e);
        }

    }

    public static DBConnector getInstance() {
        if (instance == null) {
            synchronized (DBConnector.class) {
                if (instance == null) {
                    instance = new DBConnector();
                }
            }
        }

        return instance;
    }


    private DBConnector() {
        try {
            loadProperties();
            registerDBDriver();
            getConnection(dbProperties.getProperty(URL),
                          dbProperties.getProperty(USERNAME),
                          dbProperties.getProperty(PASSWORD));

            if (isDBExists()) {
                logger.info("The database has already been created.");
            } else {
                try (Statement s = connection.createStatement();) {
                    s.executeUpdate(CREATE_DATABASE_1 + dbProperties.getProperty(DATABASE_NAME) + CREATE_DATABASE_2);
                    s.executeUpdate("USE " + dbProperties.getProperty(DATABASE_NAME) + ";");
                    s.executeUpdate(CREATE_USERS_TABLE);
                    s.executeUpdate(CREATE_UNIQUE_USERS_ID_INDEX);
                    s.executeUpdate(CREATE_UNIQUE_EMAIL_INDEX);
                    s.executeUpdate(CREATE_UNIQUE_LOGIN_INDEX);
                    s.executeUpdate(SET_TIME_ZONE);
                    s.executeUpdate(CREATE_NOTES_TABLE);
                    s.executeUpdate(CREATE_UNIQUE_NOTES_ID_INDEX);

                    logger.info("Database is created.");
                } catch (SQLException e) {
                    logger.error(e.getMessage(), e);
                }
            }

        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
    }


    private void registerDBDriver() {
        try {
            Class.forName(dbProperties.getProperty(DRIVER));
            logger.info("Loading driver success.");
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
        }
    }


    private Connection getConnection(String url, String userName, String password) throws SQLException {
        connection = DriverManager.getConnection(url, userName, password);
        return connection;
    }

    public Connection getConnection() {
        return connection;
    }


    private boolean isDBExists() {
        try (Statement s = connection.createStatement();) {
            ResultSet rs = connection.getMetaData().getCatalogs();

            while (rs.next()) {
                String databaseName = rs.getString("TABLE_CAT");
                if (databaseName.equalsIgnoreCase(dbProperties.getProperty(DATABASE_NAME))) {
                    s.executeUpdate("USE " + dbProperties.getProperty(DATABASE_NAME) + ";");
                    return true;
                }
            }

        } catch (Exception e) {
            logger.error("isDBExists() has error.");
            logger.error(e.getMessage(), e);
        }

        return false;
    }

    protected void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

}
