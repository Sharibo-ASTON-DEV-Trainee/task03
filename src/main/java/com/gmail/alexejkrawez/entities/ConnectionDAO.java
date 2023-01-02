package com.gmail.alexejkrawez.entities;

import com.gmail.alexejkrawez.DBConnector;

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
 * @version 1.1
 */
public class ConnectionDAO {

    /**
     * Gives a DBMS connection object.
     *
     * @return returns an instance of the DBMS connection object.
     */
    static Connection getConnection() {
        return DBConnector.getInstance().getConnection();
    }
}