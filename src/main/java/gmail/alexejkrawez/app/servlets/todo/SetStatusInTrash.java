package gmail.alexejkrawez.app.servlets.todo;

import gmail.alexejkrawez.app.entities.NoteDAO;
import gmail.alexejkrawez.app.model.Note;
import gmail.alexejkrawez.app.model.User;
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
import java.util.List;

import static gmail.alexejkrawez.app.entities.ConnectionDAO.logger;
import static java.lang.Integer.parseInt;

@WebServlet("/TODO/statusInTrash")
public class SetStatusInTrash extends HttpServlet {

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        StringBuilder sb = new StringBuilder();
        BufferedReader br = req.getReader();
        String str;

        while ((str = br.readLine()) != null) {
            sb.append(str);
        }

        JSONObject jsonObj = new JSONObject();

        String reqUserId = null;
        String reqId = null;
        try {
            jsonObj = (JSONObject) JSONValue.parseWithException(sb.toString());
            reqUserId = jsonObj.get("noteUserId").toString();
            reqId = jsonObj.get("noteId").toString();

            jsonObj.clear();
        } catch (ParseException | NullPointerException e) {
            logger.error(e.getMessage(), e);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }


        int noteUserId = parseInt(reqUserId);
        int noteId = parseInt(reqId);

        boolean status = NoteDAO.setStatusInTrash(noteUserId, noteId);

        if (status) {
            HttpSession session = req.getSession(false);
            User user = (User) session.getAttribute("user");

            for (Note note : user.getUserNotes()) {
                if (note.getId() == noteId) {
                    note.setStatus(4);
                    break;
                }
            }

            session.setAttribute("user", user);
            jsonObj.put("status", true);
            resp.setStatus(HttpServletResponse.SC_OK);
        } else {
            jsonObj.put("status", false);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        try (PrintWriter writer = resp.getWriter()) {
            jsonObj.writeJSONString(writer);
            logger.info("SetStatusInTrash response: " + jsonObj);
        } catch (NullPointerException | IOException e) {
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }

}