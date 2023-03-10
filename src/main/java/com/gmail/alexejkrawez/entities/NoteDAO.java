package com.gmail.alexejkrawez.entities;

import com.gmail.alexejkrawez.model.Note;
import com.gmail.alexejkrawez.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for queries in the DBMS of the POJO Note.class.
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
public class NoteDAO extends ConnectionDAO {

    /**
     * Provides logging to the console and to a file.
     */
    public static final Logger logger = LogManager.getLogger(NoteDAO.class);

    private static final String ADD_NOTE = "INSERT INTO notes (user_id, note, status, target_date) " +
        "VALUES (?, ?, ?, CASE " +
        "WHEN status = 1 THEN DATE_FORMAT((CURRENT_TIMESTAMP), '%Y-%m-%d 00:00:00') " +
        "WHEN status = 2 THEN DATE_FORMAT((CURRENT_TIMESTAMP + INTERVAL 1 DAY), '%Y-%m-%d 00:00:00') " +
        "WHEN status = 3 THEN DATE_FORMAT((CURRENT_TIMESTAMP - INTERVAL 1 DAY), '%Y-%m-%d 00:00:00') END);";

    private static final String ADD_NOTE_FILE = "INSERT INTO notes (user_id, note, file_path, status, target_date) " +
            "VALUES (?, ?, ?, ?, CASE " +
            "WHEN status = 1 THEN DATE_FORMAT((CURRENT_TIMESTAMP), '%Y-%m-%d 00:00:00') " +
            "WHEN status = 2 THEN DATE_FORMAT((CURRENT_TIMESTAMP + INTERVAL 1 DAY), '%Y-%m-%d 00:00:00') " +
            "WHEN status = 3 THEN DATE_FORMAT((CURRENT_TIMESTAMP - INTERVAL 1 DAY), '%Y-%m-%d 00:00:00') END);";

    private static final String UPDATE_STATUS_BY_DATE = "UPDATE notes SET notes.status = CASE " +
            "WHEN notes.user_id = ? AND notes.status != 4 AND notes.status != 5 " +
            "AND DATEDIFF(DATE_FORMAT((CURRENT_TIMESTAMP), '%Y-%m-%d 00:00:00'), DATE_FORMAT((notes.target_date), '%Y-%m-%d 00:00:00')) = 0 " +
            "THEN 1 " +
            "WHEN notes.user_id = ? AND notes.status != 4 AND notes.status != 5 " +
            "AND DATEDIFF(DATE_FORMAT((CURRENT_TIMESTAMP), '%Y-%m-%d 00:00:00'), DATE_FORMAT((notes.target_date), '%Y-%m-%d 00:00:00')) = -1 " +
            "THEN 2 " +
            "WHEN notes.user_id = ? AND notes.status != 4 AND notes.status != 5 " +
            "AND DATEDIFF(DATE_FORMAT((CURRENT_TIMESTAMP), '%Y-%m-%d 00:00:00'), DATE_FORMAT((notes.target_date), '%Y-%m-%d 00:00:00')) != 0 " +
            "AND DATEDIFF(DATE_FORMAT((CURRENT_TIMESTAMP), '%Y-%m-%d 00:00:00'), DATE_FORMAT((notes.target_date), '%Y-%m-%d 00:00:00')) != -1 " +
            "THEN 3 " +
            "ELSE notes.status END " +
            "ORDER BY notes.date;";

    private static final String UPDATE_STATUS_OK = "UPDATE notes SET notes.status = 5, notes.date = CURRENT_TIMESTAMP " +
            "WHERE notes.user_id = ? AND notes.id = ? " +
            "ORDER BY notes.id DESC LIMIT 1;";

    private static final String UPDATE_STATUS_IN_TRASH = "UPDATE notes SET notes.status = 4, notes.date = CURRENT_TIMESTAMP " +
            "WHERE notes.user_id = ? AND notes.id = ? " +
            "ORDER BY notes.id DESC LIMIT 1;";

    private static final String UPDATE_STATUS_TODAY = "UPDATE notes SET notes.status = 1, notes.date = CURRENT_TIMESTAMP " +
            "WHERE notes.user_id = ? AND notes.id = ? " +
            "ORDER BY notes.id DESC LIMIT 1;";

    private static final String UPDATE_NOTE_FILE = "UPDATE notes SET notes.file_path = null, notes.date = CURRENT_TIMESTAMP " +
            "WHERE notes.user_id = ? AND notes.id = ? " +
            "ORDER BY notes.id DESC LIMIT 1;";

    private static final String DELETE_NOTE = "DELETE FROM notes WHERE notes.user_id = ? AND notes.id = ?;";

    private static final String DELETE_NOTES_STATUS_IN_TRASH = "DELETE FROM notes WHERE notes.user_id = ? AND notes.status = 4;";

    private static final String SELECT_USER_NOTES_ORDER_DATE = "SELECT notes.user_id, notes.id, notes.date, " +
            "notes.target_date, notes.note, notes.file_path, notes.status " +
            "FROM notes " +
            "WHERE notes.user_id = ? AND notes.status != 5 " +
            "ORDER BY notes.date;";

    private static final String SELECT_LAST_USER_NOTE = "SELECT notes.user_id, notes.id, notes.date, " +
            "notes.target_date, notes.note, notes.file_path, notes.status " +
            "FROM notes " +
            "WHERE notes.user_id = ? " +
            "ORDER BY id DESC LIMIT 1;";


    synchronized public static boolean createNote(int userId, String text, int status) {

        try (PreparedStatement ps = getConnection().prepareStatement(ADD_NOTE)) {
            ps.setInt(1, userId);
            ps.setString(2, text);
            ps.setInt(3, status);
            ps.executeUpdate();
            logger.info("Note is created.");
            return true;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }

        return false;
    }

    synchronized public static boolean createNoteWithFile(int userId, String text, String filePath, int status) {

        try (PreparedStatement ps = getConnection().prepareStatement(ADD_NOTE_FILE)) {
            ps.setInt(1, userId);
            ps.setString(2, text);
            ps.setString(3, filePath);
            ps.setInt(4, status);
            ps.executeUpdate();
            logger.info("Note with file is created.");
            return true;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }

        return false;
    }

    synchronized public static boolean getNotesOrderDate(User user) {
        List<Note> notes = new ArrayList<>();
        boolean updateStatus = updateStatusByDate(user.getUserId());

        if (updateStatus) {
            try (PreparedStatement ps = getConnection().prepareStatement(SELECT_USER_NOTES_ORDER_DATE)) {
                ps.setInt(1, user.getUserId());

                ResultSet rs = ps.executeQuery();
                fillList(notes, rs);
                user.setUserNotes(notes);

                return true;
            } catch (SQLException e) {
                logger.error("user_id: " + user.getUserId());
                logger.error(e.getMessage(), e);
            }
        }

        return false;
    }

    synchronized public static Note getLastNote(int userId) {
        List<Note> notes = new ArrayList<>();

        try (PreparedStatement ps = getConnection().prepareStatement(SELECT_LAST_USER_NOTE)) {
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();
            fillList(notes, rs);
        } catch (SQLException e) {
            logger.error("user_id: " + userId);
            logger.error(e.getMessage(), e);
        }

        if (!notes.isEmpty()) {
            return notes.get(0);
        }

        return null;
    }

    synchronized public static boolean setStatusOk(int userId, int id) {

        try (PreparedStatement ps = getConnection().prepareStatement(UPDATE_STATUS_OK)) {
            ps.setInt(1, userId);
            ps.setInt(2, id);
            ps.executeUpdate();
            logger.info("Note " + id + " status is ok.");
            return true;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }

        return false;
    }

    synchronized public static boolean setStatusInTrash(int userId, int id) {

        try (PreparedStatement ps = getConnection().prepareStatement(UPDATE_STATUS_IN_TRASH)) {
            ps.setInt(1, userId);
            ps.setInt(2, id);
            ps.executeUpdate();
            logger.info("Note " + id + " status is delete.");
            return true;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }

        return false;
    }

    synchronized public static boolean setStatusToday(int userId, int id) {

        try (PreparedStatement ps = getConnection().prepareStatement(UPDATE_STATUS_TODAY)) {
            ps.setInt(1, userId);
            ps.setInt(2, id);
            ps.executeUpdate();
            logger.info("Note " + id + " status is today.");
            return true;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }

        return false;
    }

    synchronized public static boolean deleteNote(int userId, int id) {

        try (PreparedStatement ps = getConnection().prepareStatement(DELETE_NOTE)) {
            ps.setInt(1, userId);
            ps.setInt(2, id);
            ps.executeUpdate();
            logger.info("With status 4 note " + id + " deleted.");
            return true;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }

        return false;
    }

    synchronized public static boolean deleteNoteFile(int userId, int id) {

        try (PreparedStatement ps = getConnection().prepareStatement(UPDATE_NOTE_FILE)) {
            ps.setInt(1, userId);
            ps.setInt(2, id);
            ps.executeUpdate();
            logger.info("File in note " + id + " deleted.");
            return true;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }

        return false;
    }

    synchronized public static boolean deleteDeletedNotes(int userId) {

        try (PreparedStatement ps = getConnection().prepareStatement(DELETE_NOTES_STATUS_IN_TRASH)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
            logger.info("All notes status 4 by user " + userId + " deleted.");
            return true;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }

        return false;
    }

    synchronized public static boolean updateStatusByDate(int userId) {

        try (PreparedStatement ps = getConnection().prepareStatement(UPDATE_STATUS_BY_DATE)) {
            ps.setInt(1, userId);
            ps.setInt(2, userId);
            ps.setInt(3, userId);
            ps.executeUpdate();
            logger.info("Status is updated.");
            return true;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }

        return false;
    }

    synchronized private static void fillList(List<Note> notes, ResultSet rs) throws SQLException {
        while (rs.next()) {
            int dbUserId = rs.getInt("user_id");
            int dbId = rs.getInt("id");
            String dbDate = rs.getString("date");
            String dbTargetDate = rs.getString("target_date");
            String dbText = rs.getString("note");
            String dbFile_path = rs.getString("file_path");
            int dbStatus = rs.getInt("status");
            notes.add(new Note(dbUserId, dbId, dbDate, dbTargetDate, dbText, dbFile_path, dbStatus));
        }
    }


}
