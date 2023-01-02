package com.gmail.alexejkrawez.servlets.todo;

import com.gmail.alexejkrawez.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Closes the user's active session, also nullifying all links
 * associated with this user.
 * Returns a message indicating the success of the operation.
 *
 * <p>package: com.gmail.alexejkrawez.servlets.todo</p>
 * <p>email: AlexejKrawez@gmail.com</p>
 * <p>created: 30.12.2022</p>
 *
 * @since Java v1.8
 *
 * @author Alexej Krawez
 * @version 1.1
 */
@WebServlet("/TODO/exit")
public class CloseSession extends HttpServlet {

    /**
     * Provides logging to the console and to a file.
     */
    public static final Logger logger = LogManager.getLogger(CloseSession.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        JSONObject jsonObj = new JSONObject();

        HttpSession session = req.getSession(false);
        User user;
        if (session != null & req.isRequestedSessionIdValid()) {
            user = (User) session.getAttribute("user");
            session.invalidate();
            jsonObj.put("status", true);

            logger.info("Session by user " + user.getUserId() + " closed.");
        } else {
            jsonObj.put("status", true);

            logger.warn("Session is already closed with invalidation.");
        }

        user = null;
        resp.setStatus(HttpServletResponse.SC_OK);

        try (PrintWriter writer = resp.getWriter()) {
            jsonObj.writeJSONString(writer);
            logger.info("CloseSession response: " + jsonObj);
        } catch (NullPointerException | IOException e) {
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }
}
