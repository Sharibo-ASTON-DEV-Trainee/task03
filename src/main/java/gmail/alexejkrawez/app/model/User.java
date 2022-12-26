package gmail.alexejkrawez.app.model;

import java.util.List;

public class User {
    private int userId;
    private String email;
    private String login;
    private String password;
//    private List<Note> userNotes;

    public User() {}

    public User(int userId) {
        this.userId = this.userId;
    }

    public User(int userId, String email, String login, String password) {
        this.userId = this.userId;
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

    @Override
    public String toString() {
        return "{" +
                "\"user_id\":\"" + userId + "\"" +
                ", \"email\":\"" + email + "\"" +
                ", \"login\":\"" + login + "\"" +
                ", \"password\":\"" + password + "\"" +
                '}';
    }


}
