package gmail.alexejkrawez.app.servlets;

import gmail.alexejkrawez.app.entities.CardDAO;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import static gmail.alexejkrawez.app.entities.ConnectionDAO.logger;
import static java.lang.Integer.parseInt;
import static javax.xml.bind.DatatypeConverter.parseDouble;
import static javax.xml.bind.DatatypeConverter.parseString;

@WebServlet("/create_card")
public class CreateCard extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

//        HttpSession session = req.getSession(false);
//        Client client = (Client) session.getAttribute("client");

        StringBuilder sb = new StringBuilder();
        BufferedReader br = req.getReader();
        String str;

        while ((str = br.readLine()) != null) {
            sb.append(str);
        }

        JSONObject jsonObj = new JSONObject();

        String reqClientId = null;
        String reqCardType = null;
        String reqCardNumber = null;
        String reqMoney = null;
        String reqDateStart = null;
        String reqDateEnd = null;
        try {
            jsonObj = (JSONObject) JSONValue.parseWithException(sb.toString());
            reqClientId = jsonObj.get("clientId").toString();
            reqCardType = jsonObj.get("cardType").toString();
            reqCardNumber = jsonObj.get("cardNumber").toString();
            reqMoney = jsonObj.get("money").toString();
            reqDateStart = jsonObj.get("dateStart").toString();
            reqDateEnd = jsonObj.get("dateEnd").toString();

            jsonObj.clear();
        } catch (ParseException | NullPointerException e) {
            logger.error(e.getMessage(), e);
        }


        int clientId = parseInt(reqClientId);
        String cardType = parseString(reqCardType);
        int cardNumber = parseInt(reqCardNumber);
        double money = parseDouble(reqMoney);
        String dateStart = parseString(reqDateStart);
        String dateEnd = parseString(reqDateEnd);

        int cardTypeEnum = 0;
        if (cardType.toLowerCase().matches("mastercard")) {
            cardTypeEnum = 1;
        }

        boolean down = CardDAO.createCard(clientId, cardTypeEnum, cardNumber, money, dateStart, dateEnd);

        if (down) {
            jsonObj.put("status", true);
        } else {
            jsonObj.put("status", false);
        }

        try (PrintWriter writer = resp.getWriter()) {
            jsonObj.writeJSONString(writer);
        } catch (NullPointerException | IOException e) {
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }


}




