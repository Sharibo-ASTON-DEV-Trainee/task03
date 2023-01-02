package com.gmail.alexejkrawez.servlets.validation;

import com.gmail.alexejkrawez.entities.UserDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
 * Validates user input against a regular expression. The length of the
 * login is also checked: at least 6 and no more than 30 characters.
 * Also queries the DBMS if such a login exists in the database.
 * Returns the validity status of the data entered by the user,
 * the correspondence of the login in length and the presence of such
 * a login in the database
 *
 * <p>package: com.gmail.alexejkrawez.servlets.validation</p>
 * <p>email: AlexejKrawez@gmail.com</p>
 * <p>created: 30.12.2022</p>
 *
 * @since Java v1.8
 *
 * @author Alexej Krawez
 * @version 1.1
 */
@WebServlet("/validation/login_r")
public class ValidLoginR extends HttpServlet {

    /**
     * Provides logging to the console and to a file.
     */
    public static final Logger logger = LogManager.getLogger(ValidLoginR.class);

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
        String login = null;
        try {
            jsonObj = (JSONObject) JSONValue.parseWithException(sb.toString());
            login = jsonObj.get("login").toString();

            jsonObj.clear();

            boolean valid = login.matches("^[a-zA-Z0-9_.-]+$");
            jsonObj.put("validation", valid);
        } catch (ParseException | NullPointerException e) {
            logger.error(e.getMessage(), e);
        }

        if (login != null) {
            if (login.length() < 6 | login.length() > 30) {
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

        boolean exists = UserDAO.isLoginExist(login);
        if (exists) {
            jsonObj.put("exists", true);
        } else {
            jsonObj.put("exists", false);
        }

        try (PrintWriter writer = resp.getWriter()) {
            jsonObj.writeJSONString(writer);
            logger.info("ValidLoginR response: " + jsonObj);
        } catch (NullPointerException | IOException e) {
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }
}