package gmail.alexejkrawez.app.model;

import java.util.List;

public class Client {

    private int clientId;
    private String firstName;
    private String lastName;
    private String passSeries;
    private int pass_id;
    private String email;
    private List<Card> userCards;

    public Client() {}

    public Client(int clientId) {
        this.clientId = clientId;
    }

    public Client(int clientId, String firstName, String lastName, String passSeries, int pass_id, String email, List<Card> userCards) {
        this.clientId = clientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.passSeries = passSeries;
        this.pass_id = pass_id;
        this.email = email;
        this.userCards = userCards;
    }


    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassSeries() {
        return passSeries;
    }

    public void setPassSeries(String passSeries) {
        this.passSeries = passSeries;
    }

    public int getPass_id() {
        return pass_id;
    }

    public void setPass_id(int pass_id) {
        this.pass_id = pass_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Card> getUserCards() {
        return userCards;
    }

    public void setUserCards(List<Card> userCards) {
        this.userCards = userCards;
    }


    @Override
    public String toString() {
        return "Client{" +
                "clientId=" + clientId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", passSeries='" + passSeries + '\'' +
                ", pass_id=" + pass_id +
                ", email='" + email + '\'' +
                ", userCards=" + userCards +
                '}';
    }


}
