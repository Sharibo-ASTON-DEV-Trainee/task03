package com.gmail.alexejkrawez.servlets;

import com.gmail.alexejkrawez.entities.NoteDAO;
import com.gmail.alexejkrawez.entities.UserDAO;
import com.gmail.alexejkrawez.model.User;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import static com.gmail.alexejkrawez.entities.ConnectionDAO.logger;

/**
 * Web Servlet.
 * Requests a user from the DBMS by login and password. If the data
 * from the DBMS is successfully received, it creates a session for
 * the given user. It also makes a request to the DBMS to get the notes
 * of this user. The list with notes is assigned to the object by the User,
 * and the User object is placed into the session. The value list = 1 is
 * also placed in the session. Upon successful login, the user will be
 * shown a list of today's notes, this is necessary to synchronize the
 * client and server code.
 * Returns the success status of the login operation.
 *
 * <p>package: com.gmail.alexejkrawez.servlets</p>
 * <p>email: AlexejKrawez@gmail.com</p>
 * <p>created: 30.12.2022</p>
 *
 * @since Java v1.8
 *
 * @author Alexej Krawez
 * @version 1.0
 */
@WebServlet("/login")
public class Login extends HttpServlet {

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
        String password = null;
        try {
            jsonObj = (JSONObject) JSONValue.parseWithException(sb.toString());
            login = jsonObj.get("login").toString();
            password = jsonObj.get("password").toString();

            jsonObj.clear();
        } catch (ParseException | NullPointerException e) {
            logger.error(e.getMessage(), e);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        User user = UserDAO.getUser(login, password);

        if (user == null) {
            jsonObj.put("user", false);
        } else {
            HttpSession session = req.getSession();
            jsonObj.put("user", true);

            boolean status = NoteDAO.getNotesOrderDate(user);
            if (status) {
                session.setAttribute("user", user);
                session.setAttribute("list", 1);
                resp.setStatus(HttpServletResponse.SC_OK);

                logger.info("User into session: " + user);
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                logger.warn("Failure to receive notes.");
            }
        }

        try (PrintWriter writer = resp.getWriter()) {
            jsonObj.writeJSONString(writer);
            logger.info("Login response: " + jsonObj);
        } catch (NullPointerException | IOException e) {
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
