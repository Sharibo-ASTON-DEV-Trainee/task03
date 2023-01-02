package com.gmail.alexejkrawez.model;

import java.util.List;
import java.util.Objects;

/**
 * A POJO class that describes the users of the service.
 *
 * <p>package: com.gmail.alexejkrawez.model</p>
 * <p>email: AlexejKrawez@gmail.com</p>
 * <p>created: 30.12.2022</p>
 *
 * @since Java v1.8
 *
 * @author Alexej Krawez
 * @version 1.1
 */
public class User {
    private int userId;
    private String email;
    private String login;
    private String password;
    private List<Note> userNotes;

    public User() {}

    public User(int userId) {
        this.userId = userId;
    }

    public User(int userId, String email, String login, String password, List<Note> userNotes) {
        this.userId = userId;
        this.email = email;
        this.login = login;
        this.password = password;
        this.userNotes = userNotes;
    }

    public User(int userId, String email, String login, String password) {
        this.userId = userId;
        this.email = email;
        this.login = login;
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int user_id) {
        this.userId = user_id;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public List<Note> getUserNotes() {
        return userNotes;
    }

    public void setUserNotes(List<Note> userNotes) {
        this.userNotes = userNotes;
    }

    @Override
    public String toString() {
        return "\n{" +
                "\"user_id\":\"" + userId + "\"" +
                ", \"email\":\"" + email + "\"" +
                ", \"login\":\"" + login + "\"" +
                ", \"password\":\"" + password + "\"" +
                ", \n" +
                ", \"user_notes\":" + userNotes +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return userId == user.userId &&
                Objects.equals(email, user.email) &&
                Objects.equals(login, user.login) &&
                Objects.equals(password, user.password) &&
                Objects.equals(userNotes, user.userNotes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                userId,
                email,
                login,
                password,
                userNotes
        );
    }


}
