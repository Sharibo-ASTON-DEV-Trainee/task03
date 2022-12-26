package gmail.alexejkrawez.app.entities;

import gmail.alexejkrawez.app.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientDAO extends ConnectionDAO {

    private static final String CREATE_CLIENT = "INSERT INTO clients " +
            "(first_name, last_name, pass_series, pass_id, email) " +
            "VALUES(?, ?, ?, ?, ?);";


//    private static final String
//    private static final String
//    private static final String
//    private static final String
//    private static final String
//    private static final String
//    private static final String
//    private static final String
//    private static final String
//    private static final String
//    private static final String
//    private static final String
//    private static final String
//    private static final String
//    private static final String


    synchronized public static boolean createClient(String firstName, String lastName,
                                                    String passSeries, int passId, String email) {

        try (PreparedStatement ps = getConnection().prepareStatement(CREATE_CLIENT)) {
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, passSeries);
            ps.setInt(4, passId);
            ps.setString(5, email);
            ps.executeUpdate();
            logger.info("Client " + firstName + " " + lastName + " created.");
        } catch (SQLException e) {
            logger.error(firstName + " " + lastName + ": ");
            logger.error(e.getMessage(), e);
            return false;
        }

        return true;
    }























}
