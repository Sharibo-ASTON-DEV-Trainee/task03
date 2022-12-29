package gmail.alexejkrawez.app.servlets.todo;

import gmail.alexejkrawez.app.model.User;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

import static gmail.alexejkrawez.app.entities.ConnectionDAO.logger;

@WebServlet("/TODO/exit")
public class CloseSession extends HttpServlet {

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
