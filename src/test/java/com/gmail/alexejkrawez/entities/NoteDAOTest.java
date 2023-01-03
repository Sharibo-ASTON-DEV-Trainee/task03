package com.gmail.alexejkrawez.entities;

import com.gmail.alexejkrawez.DBConnector;
import com.gmail.alexejkrawez.model.Note;
import com.gmail.alexejkrawez.model.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class NoteDAOTest extends DBContainer {

    @BeforeEach
    @Override
    public void overwriteURL() {
        super.overwriteURL();
    }

    @AfterAll
    public static void closeConnection() {
        try {
            DBConnector.getInstance().closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    void getNotesOrderDate() {
        Note[] notesArray = {
                new Note(2, 1, "2022-12-30 09:26:00", "2022-12-31 09:26:00",
                        "breakfast", "8888Bacon_sandwich.jpg", 3),
                new Note(2, 2, "2022-12-30 09:28:00", "2022-12-31 09:28:00",
                        "training", "null", 3),
                new Note(2, 3, "2022-12-30 09:40:34", "2022-12-31 09:40:34",
                        "lunch", "6633Pasta_salad.jpg", 3),
                new Note(2, 4, "2022-12-30 10:42:16", "2022-12-31 10:42:16",
                        "KENO: 18 21 23 2 49 37 26 43 41 45\\n20 52 28 47 17 5 30 25 48 38",
                        "null", 3),
                new Note(2, 5, "2022-12-30 10:48:48", "2022-12-31 10:48:48",
                        "training", "null", 3),
                new Note(2, 6, "2022-12-30 10:54:03", "2022-12-31 10:54:03",
                        "dinner", "2212Ratatouille_sheet_pan_dinner_with_sausage.png", 3),
                new Note(2, 7, "2022-12-30 11:01:28", "2022-12-31 11:01:28",
                        "anime: megalo box s03-06", "null", 3)
        };

        List<Note> notes = Arrays.asList(notesArray);

        User user = new User(2, "ivan_pupkin@gmail.com", "Ivan0", "donotpupkin");
        boolean dbStatus = NoteDAO.getNotesOrderDate(user); // user_id = 2

        assertTrue(dbStatus);

        int i = 0;
        for (Note note : notes) {
            assertEquals(note, user.getUserNotes().get(i++));
        }

    }

    @Test
    void getLastNote() {
        Note note = new Note(2, 7, "2022-12-30 11:01:28", "2022-12-31 11:01:28",
                "anime: megalo box s03-06", "null", 3);
        Note dbNote = NoteDAO.getLastNote(2); // user_id = 2, note = 7

        assertEquals(note, dbNote);
    }

    @Test
    void setStatusOk() {
        boolean dbStatus = NoteDAO.setStatusOk(3, 26);
        assertTrue(dbStatus);
    }

    @Test
    void setStatusInTrash() {
        boolean dbStatus = NoteDAO.setStatusInTrash(3, 21);
        assertTrue(dbStatus);
    }

    @Test
    void setStatusToday() {
        boolean dbStatus = NoteDAO.setStatusToday(3, 20);
        assertTrue(dbStatus);
    }

    @Test
    void deleteNote() {
        boolean dbStatus = NoteDAO.deleteNote(3, 19);
        assertTrue(dbStatus);
    }

    @Test
    void deleteNoteFile() {
        boolean dbStatus = NoteDAO.deleteNoteFile(3, 18);
        assertTrue(dbStatus);
    }

    @Test
    void deleteDeletedNotes() {
        boolean dbStatus = NoteDAO.deleteDeletedNotes(3);
        assertTrue(dbStatus);
    }

    @Test
    void updateStatusByDate() {
        boolean dbStatus = NoteDAO.updateStatusByDate(2);
        assertTrue(dbStatus);
    }

    @Test
    void createNote() {
        boolean dbNote = NoteDAO.createNote(1, "Tomas is here!", 1);
        assertTrue(dbNote);
    }

    @Test
    void createNoteWithFile() {
        boolean dbNote = NoteDAO.createNoteWithFile(1, "Tomas has the file!", "0000Tomas.gif", 1);
        assertTrue(dbNote);
    }

}