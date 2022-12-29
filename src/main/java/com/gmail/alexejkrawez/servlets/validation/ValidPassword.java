package com.gmail.alexejkrawez.servlets.validation;

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

import static com.gmail.alexejkrawez.entities.ConnectionDAO.logger;

/**
 * Web Servlet.
 * Validates user input against a regular expression. The length of the
 * password is also checked: at least 8 and no more than 30 characters.
 * Returns the validity status of the data entered by the user and
 * the length match of the password.
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
@WebServlet("/validation/password")
public class ValidPassword extends HttpServlet {

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
        String password = null;
        try {
            jsonObj = (JSONObject) JSONValue.parseWithException(sb.toString());
            password = jsonObj.get("password").toString();

            jsonObj.clear();

            boolean valid = password.matches("^[a-zA-Z0-9]+$");
            jsonObj.put("validation", valid);
        } catch (ParseException | NullPointerException e) {
            logger.error(e.getMessage(), e);
        }

        if (password != null) {
            if (password.length() < 8 | password.length() > 30) {
                jsonObj.put("length", false);

                try (PrintWriter writer = resp.getWriter()) {
                    jsonObj.writeJSONString(writer);
                } catch (NullPointerException | IOException e) {
                    logger.error(e.getMessage(), e);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }

            } else {
                jsonObj.put("length", true);
            }

        } else {
            jsonObj.put("length", false);
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
