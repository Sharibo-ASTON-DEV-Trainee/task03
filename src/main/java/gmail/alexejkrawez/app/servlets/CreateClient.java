package gmail.alexejkrawez.app.servlets;

import gmail.alexejkrawez.app.entities.ClientDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static gmail.alexejkrawez.app.entities.ConnectionDAO.logger;

@WebServlet("/create_client")
public class CreateClient extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

//
//        String firstName = (String) req.getAttribute("firstName");
//        String lastName = (String) req.getAttribute("lastName");
//        String passSeries = (String) req.getAttribute("passSeries");
//        Integer passId = (Integer) req.getAttribute("passId");
//        String email = (String) req.getAttribute("email");
//
//
//        boolean validEmail = email.matches("^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$");
//        boolean validPassSeries = passSeries.matches("^[a-zA-Z]{2}$");
//        boolean validPassId = passId.toString().matches("^[0-9]{7}$");

//        jsonObj.put("validEmail", validEmail);
//        jsonObj.put("validLogin", validPassSeries);
//        jsonObj.put("validPassword", validPassId);

//        boolean dbResp = false;
//        if (validEmail & validPassSeries & validPassId) {
//            dbResp = ClientDAO.createClient(firstName, lastName,
//                    passSeries, passId, email);
//        }


        resp.setContentType("text/html");
        resp.setBufferSize(8192);


        try (PrintWriter writer = resp.getWriter()) {
            writer.println("YES!");
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

    }


}