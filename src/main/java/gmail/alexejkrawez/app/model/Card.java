package gmail.alexejkrawez.app.model;

public class Card {

    private int id;
    private Enum<CardType> card_type;
    private int cardNumber;
    private int money;
    private String date;
    private String target_date;

    public Card() {
    }

    public Card(int id, Enum<CardType> card_type, int cardNumber, int money, String date, String target_date) {
        this.id = id;
        this.card_type = card_type;
        this.cardNumber = cardNumber;
        this.money = money;
        this.date = date;
        this.target_date = target_date;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Enum<CardType> getCard_type() {
        return card_type;
    }

    public void setCard_type(Enum<CardType> card_type) {
        this.card_type = card_type;
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTarget_date() {
        return target_date;
    }

    public void setTarget_date(String target_date) {
        this.target_date = target_date;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", card_type=" + card_type +
                ", cardNumber=" + cardNumber +
                ", money=" + money +
                ", date='" + date + '\'' +
                ", target_date='" + target_date + '\'' +
                '}';
    }


}
