package gmail.alexejkrawez.app.entities;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CardDAO extends ConnectionDAO {

    private static final String CREATE_CARD = "INSERT INTO cards " +
            "(client_id, card_type, card_number, money, date_start, date_end) " +
            "VALUES(?, ?, ?, ?, ?, ?);";


//    private static final String
//    private static final String
//    private static final String
//    private static final String
//    private static final String
    private static final String SELECT = "SELECT client_id, id, card_type, card_number, money, date_start, date_end" +
            "FROM cards;";
//    private static final String
//    private static final String
//    private static final String
//    private static final String
//    private static final String


    synchronized public static boolean createCard(int client_id, int card_type,
                                                  int card_number, double money,
                                                  String date_start, String date_end) {

        try (PreparedStatement ps = getConnection().prepareStatement(CREATE_CARD)) {
            ps.setInt(1, client_id);
            ps.setInt(2, card_type);
            ps.setInt(3, card_number);
            ps.setDouble(2, money);
            ps.setString(2, date_start);
            ps.setString(2, date_end);
            ps.executeUpdate();
            logger.info("Card is created.");
            return true;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }

        return false;
    }
















}
