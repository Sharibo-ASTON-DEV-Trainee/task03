package com.gmail.alexejkrawez.entities;

import com.gmail.alexejkrawez.DBConnector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;

/**
 * Initializes the creation of a connection to the DBMS.
 * Also creates a logger.
 *
 * <p>package: com.gmail.alexejkrawez.entities</p>
 * <p>email: AlexejKrawez@gmail.com</p>
 * <p>created: 30.12.2022</p>
 *
 * @since Java v1.8
 *
 * @author Alexej Krawez
 * @version 1.0
 */
public class ConnectionDAO {

    /**
     * Provides logging to the console and to a file.
     */
    public static final Logger logger = LogManager.getLogger(ConnectionDAO.class);

    /**
     * Gives a DBMS connection object.
     *
     * @return returns an instance of the DBMS connection object.
     */
    static Connection getConnection() {
        return DBConnector.getInstance().getConnection();
    } // TODO насчёт статика не уверен
}
