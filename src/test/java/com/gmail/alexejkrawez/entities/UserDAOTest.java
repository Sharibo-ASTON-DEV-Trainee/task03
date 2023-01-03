package com.gmail.alexejkrawez.entities;

import com.gmail.alexejkrawez.DBConnector;
import com.gmail.alexejkrawez.model.Note;
import com.gmail.alexejkrawez.model.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest extends DBContainer {

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
    void createUser() {

        int dbStatus0 = UserDAO.createUser(
                "maximiliamus@maximus.com",
                "Maximiliamus",
                "123456789"
        );

        int dbStatus1 = UserDAO.createUser(
                "tomas24@mail.ru",     // email exist
                "Tomas",                    // login exist
                "123456789"
        ); // this user most likely exists in the DBMS

        int dbStatus2 = UserDAO.createUser(
                "tomas24@mail.ru",     // email exist
                "Valeriarmus",
                "123456789"
        );

        int dbStatus3 = UserDAO.createUser(
                "Valeriarmus@valerius.com",
                "Tomas", // login exist
                "123456789"
        );

        User user = UserDAO.selectByLoginPassword("Maximiliamus", "123456789");

        assertEquals(0, dbStatus0); // user created successfully
        assertEquals(1, dbStatus1); // user most likely exists in the DBMS
        assertEquals(2, dbStatus2); // user did not create, email just exist in DBMS
        assertEquals(3, dbStatus3); // user did not create, login exist in DBMS

    }

    @Test
    void selectByLoginPassword() {

        User user1 = new User(
                1,
                "tomas24@mail.ru",
                "Tomas",
                "usaForever123"
        );

        User dbUser1 = UserDAO.selectByLoginPassword(
                "Tomas",
                "usaForever123"
        );


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

        User user2 = new User(
                2,
                "ivan_pupkin@gmail.com",
                "Pupkin",
                "donotpupkin",
                Arrays.asList(notesArray)
        );

        User dbUser2 = UserDAO.selectByLoginPassword(
                "Pupkin",
                "donotpupkin"
        );

        NoteDAO.getNotesOrderDate(dbUser2);


        assertEquals(user1, dbUser1); // without notes
        assertEquals(user2, dbUser2); // with notes
    }

    @Test
    void isEmailExist() {
        boolean dbStatus1 = UserDAO.isEmailExist("375251881818@invest.net");
        boolean dbStatus2 = UserDAO.isEmailExist("ivan@ivanov.net"); // does not exist
        assertTrue(dbStatus1);
        assertFalse(dbStatus2);
    }

    @Test
    void isLoginExist() {
        boolean dbStatus1 = UserDAO.isLoginExist("InvestProject");
        boolean dbStatus2 = UserDAO.isLoginExist("ProjectInvest"); // does not exist
        assertTrue(dbStatus1);
        assertFalse(dbStatus2);
    }

}