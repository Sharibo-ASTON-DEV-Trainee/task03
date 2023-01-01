package com.gmail.alexejkrawez.entities;

import com.gmail.alexejkrawez.model.Note;
import com.gmail.alexejkrawez.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
class NoteDAOTest {


    private static final String CREATE_USERS_TABLE_PATH = Thread.currentThread().getContextClassLoader()
            .getResource("1_CREATE_USERS_TABLE.sql").getPath();

    private static final String CREATE_NOTES_TABLE_PATH = Thread.currentThread().getContextClassLoader()
            .getResource("2_CREATE_NOTES_TABLE.sql").getPath();
    private static final String FILL_DB_PATH = Thread.currentThread().getContextClassLoader()
            .getResource("4_FILL_DB.sql").getPath();

    private String port = "3306";

    // will be shared between test methods
    @Container
    private static final MySQLContainer<?> MY_SQL_CONTAINER = new MySQLContainer<>("mysql:latest")
            .withDatabaseName("alexej_krawez_todo_db")
            .withUsername("deus")
            .withPassword("exmachina");

    private static final String DB_PROPERTIES_PATH = Thread.currentThread().getContextClassLoader()
            .getResource("db.properties").getPath();

    private final Properties dbProperties = new Properties();

    @BeforeAll
    public static void fillDB() {
        MY_SQL_CONTAINER
                .withInitScript(CREATE_USERS_TABLE_PATH)
                .withInitScript(CREATE_NOTES_TABLE_PATH)
                .withInitScript(FILL_DB_PATH);
    }


    @BeforeEach
    public void loadProperties() {
        try {
            dbProperties.load(new FileInputStream(DB_PROPERTIES_PATH));
            Pattern pat = Pattern.compile("[0-9]{4,}");
            Matcher mat = pat.matcher(MY_SQL_CONTAINER.getJdbcUrl());
            if (mat.find() && !mat.group().equals(port)) {
                String dbUrl = dbProperties.getProperty("url");
                dbUrl = dbUrl.replace(port, mat.group());
                dbProperties.setProperty("url", dbUrl);
                dbProperties.store(new FileOutputStream(DB_PROPERTIES_PATH), null);
                port = mat.group();
            }

        } catch (IOException e) {
            ConnectionDAO.logger.error("Cannot load or save db.properties.");
            ConnectionDAO.logger.error(e.getMessage(), e);
        }

    }


    @Test
    @Order(1)
    void getNotesOrderDate() {
        Note[] notesArray = {
                new Note(2, 1, "2022-12-30 09:26:00", "2022-12-31 00:00:00",
                        "breakfast", "8888Bacon_sandwich.jpg", 2),
                new Note(2, 2, "2022-12-30 09:28:00", "2022-12-31 00:00:00",
                        "training", "null", 2),
                new Note(2, 3, "2022-12-30 09:40:34", "2022-12-31 00:00:00",
                        "lunch", "6633Pasta_salad.jpg", 2),
                new Note(2, 4, "2022-12-30 10:42:16", "2022-12-31 00:00:00",
                        "KENO: 18 21 23 2 49 37 26 43 41 45\\n20 52 28 47 17 5 30 25 48 38",
                        "null", 2), //TODO как экранировать?
                new Note(2, 5, "2022-12-30 10:48:48", "2022-12-31 00:00:00",
                        "training", "null", 2),
                new Note(2, 6, "2022-12-30 10:54:03", "2022-12-31 00:00:00",
                        "dinner", "2212Ratatouille_sheet_pan_dinner_with_sausage.png", 2),
                new Note(2, 7, "2022-12-30 11:01:28", "2022-12-31 00:00:00",
                        "anime: megalo box s03-06", "null", 2)};

        List<Note> notes = Arrays.asList(notesArray);

        User user = new User(2, "ivan_pupkin@gmail.com", "Ivan0", "donotpupkin");
        boolean dbStatus = NoteDAO.getNotesOrderDate(user); // user_id = 2

        int i = 0;
        for (Note note : notes) {
            assertEquals(note, user.getUserNotes().get(i++));
        }

    }

    @Test
    @Order(2)
    void getLastNote() {
        Note note = new Note(2, 7, "2022-12-30 11:01:28", "2022-12-31 00:00:00",
                "anime: megalo box s03-06", "null", 2);
        Note dbNote = NoteDAO.getLastNote(2); // user_id = 2, note = 7

        assertEquals(note, dbNote);
    }

    @Test
    @Order(3)
    void setStatusOk() {
        boolean dbStatus = NoteDAO.setStatusOk(3, 26);
        assertTrue(dbStatus);
    }

    @Test
    @Order(4)
    void setStatusInTrash() {
        boolean dbStatus = NoteDAO.setStatusInTrash(3, 21);
        assertTrue(dbStatus);
    }

    @Test
    @Order(5)
    void setStatusToday() {
        boolean dbStatus = NoteDAO.setStatusToday(3, 20);
        assertTrue(dbStatus);
    }

    @Test
    @Order(6)
    void deleteNote() {
        boolean dbStatus = NoteDAO.deleteNote(3, 19);
        assertTrue(dbStatus);
    }

    @Test
    @Order(7)
    void deleteNoteFile() {
        boolean dbStatus = NoteDAO.deleteNoteFile(3, 18);
        assertTrue(dbStatus);
    }

    @Test
    @Order(8)
    void deleteDeletedNotes() {
        boolean dbStatus = NoteDAO.deleteDeletedNotes(3);
        assertTrue(dbStatus);
    }

    @Test
    @Order(9)
    void updateStatusByDate() {
        boolean dbStatus = NoteDAO.updateStatusByDate(2);
        assertTrue(dbStatus);
    }

    @Test
    @Order(10)
    void createNote() {
        boolean dbNote = NoteDAO.createNote(1, "Tomas is here!", 1);
        assertTrue(dbNote);
    }

    @Order(11)
    @Test
    void createNoteWithFile() {
        boolean dbNote = NoteDAO.createNoteWithFile(1, "Tomas has the file!", "0000Tomas.gif", 1);
        assertTrue(dbNote);
    }

}