package com.gmail.alexejkrawez.entities;

import com.gmail.alexejkrawez.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO for queries in the DBMS of the POJO User.class.
 *
 * <p>package: com.gmail.alexejkrawez.entities</p>
 * <p>email: AlexejKrawez@gmail.com</p>
 * <p>created: 30.12.2022</p>
 *
 * @author Alexej Krawez
 * @version 1.1
 * @since Java v1.8
 */
public class UserDAO extends ConnectionDAO {

    /**
     * Provides logging to the console and to a file.
     */
    public static final Logger logger = LogManager.getLogger(UserDAO.class);

    private static final String ADD_USER = "INSERT INTO users (email, login, password) VALUES (?, ?, ?);";

    private static final String SELECT_BY_EMAIL_LOGIN_PASSWORD = "SELECT user_id, email, login, password " +
            "FROM users " +
            "WHERE email = ? AND login = ? AND password = ?;";
    private static final String SELECT_BY_LOGIN_PASSWORD = "SELECT user_id, email, login, password " +
            "FROM users " +
            "WHERE login = ? AND password = ? ";

    private static final String SELECT_EMAIL = "SELECT true AS email FROM users WHERE email = ? LIMIT 1;";
    private static final String SELECT_LOGIN = "SELECT true AS login FROM users WHERE login = ? LIMIT 1;";


    synchronized public static int createUser(String email, String login, String password) {
        boolean notValidEmail = isEmailExist(email);
        boolean notValidLogin = isLoginExist(login);

        if (notValidEmail & notValidLogin) {
            logger.warn("User " + login + " is already exist.");
            return 1;

        } else if (notValidEmail) {
            logger.warn("ValidEmail " + email + " is already exist.");
            return 2;
        } else if (notValidLogin) {
            logger.warn("User " + login + " is already exist.");
            return 3;
        }

        try (PreparedStatement ps = getConnection().prepareStatement(ADD_USER)) {
            ps.setString(1, email);
            ps.setString(2, login);
            ps.setString(3, password);
            ps.executeUpdate();
            logger.info("User " + login + " is created.");
        } catch (SQLException e) {
            logger.error(email + ", " + login + ": ");
            logger.error(e.getMessage(), e);
            return 1;
        }

        return 0;
    }

    synchronized public static boolean isEmailExist(String email) {
        boolean dbEmail = false;
        try (PreparedStatement ps = getConnection().prepareStatement(SELECT_EMAIL)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                dbEmail = rs.getBoolean("email");
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return dbEmail;
    }

    synchronized public static boolean isLoginExist(String login) {
        boolean dbLogin = false;
        try (PreparedStatement ps = getConnection().prepareStatement(SELECT_LOGIN)) {
            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                dbLogin = rs.getBoolean("login");
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return dbLogin;
    }


    synchronized public static User selectByLoginPassword(String login, String password) {

        User user = new User();
        try (PreparedStatement ps = getConnection().prepareStatement(SELECT_BY_LOGIN_PASSWORD)) {
            ps.setString(1, login);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            user = fillUserFields(user, rs);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }

        if (user.getUserId() != 0) {
            return user;
        }

        logger.warn("User " + login + " is not exist.");
        return null;
    }

    synchronized private static User selectByEmailLoginPassword(String email, String login, String password) {

        User user = new User();
        try (PreparedStatement ps = getConnection().prepareStatement(SELECT_BY_EMAIL_LOGIN_PASSWORD)) {
            ps.setString(1, email);
            ps.setString(2, login);
            ps.setString(3, password);

            ResultSet rs = ps.executeQuery();
            user = fillUserFields(user, rs);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }

        if (user.getUserId() != 0) {
            return user;
        }

        return null;
    }

    synchronized private static User fillUserFields(User user, ResultSet rs) throws SQLException {
        while (rs.next()) {
            int dbUserId = rs.getInt("user_id");
            String dbEmail = rs.getString("email");
            String dbLogin = rs.getString("login");
            String dbPassword = rs.getString("password");
            user = new User(dbUserId, dbEmail, dbLogin, dbPassword);
            return user;
        }
        return user; // userId == 0
    }

}