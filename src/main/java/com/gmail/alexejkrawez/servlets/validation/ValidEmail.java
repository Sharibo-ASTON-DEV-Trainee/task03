package com.gmail.alexejkrawez.servlets.validation;

import com.gmail.alexejkrawez.entities.ConnectionDAO;
import com.gmail.alexejkrawez.entities.UserDAO;
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

/**
 * Web Servlet.
 * Validates user input against an email template. Also queries the DBMS
 * if the email exists.
 * Returns the user input's validity status and whether such an email exists
 * in the database.
 *
 * <p>package: com.gmail.alexejkrawez.servlets.validation</p>
 * <p>email: AlexejKrawez@gmail.com</p>
 * <p>created: 30.12.2022</p>
 *
 * @since Java v1.8
 *
 * @author Alexej Krawez
 * @version 1.0
 */
@WebServlet("/validation/email")
public class ValidEmail extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        StringBuilder sb = new StringBuilder();
        BufferedReader br = req.getReader();
        String str;

        while ((str = br.readLine()) != null) {
            sb.append(str);
        }

        JSONObject jsonObj = new JSONObject();
        String email = null;
        try {
            jsonObj = (JSONObject) JSONValue.parseWithException(sb.toString());
            email = jsonObj.get("email").toString();

            jsonObj.clear();

            boolean valid = email.matches("^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$");
            jsonObj.put("validation", valid);
        } catch (ParseException | NullPointerException e) {
            ConnectionDAO.logger.error(e.getMessage(), e);
        }

        String exists = UserDAO.isEmailExist(email);
        if (exists != null) {
            jsonObj.put("exists", true);
        } else {
            jsonObj.put("exists", false);
        }

        try (PrintWriter writer = resp.getWriter()) {
            jsonObj.writeJSONString(writer);
        } catch (NullPointerException | IOException e) {
            ConnectionDAO.logger.error(e.getMessage(), e);
        } catch (Exception e) {
            ConnectionDAO.logger.error(e.getMessage(), e);
        }

    }
}
