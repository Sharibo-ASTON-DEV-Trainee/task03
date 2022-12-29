package com.gmail.alexejkrawez.servlets.todo;

import com.gmail.alexejkrawez.entities.NoteDAO;
import com.gmail.alexejkrawez.model.Note;
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
import java.util.Iterator;
import java.util.List;

import static com.gmail.alexejkrawez.entities.ConnectionDAO.logger;
import static java.lang.Integer.parseInt;

/**
 * Web Servlet.
 * Updates the note's current status in the DBMS to status 5 = completed.
 * Notes with this status are stored in the DBMS, but are never shown to the
 * client again. On success, also deletes the note in the
 * list of notes in the User object stored in the session.
 * Returns a message indicating the success of the operation.
 *
 * <p>package: com.gmail.alexejkrawez.servlets.todo</p>
 * <p>email: AlexejKrawez@gmail.com</p>
 * <p>created: 30.12.2022</p>
 *
 * @since Java v1.8
 *
 * @author Alexej Krawez
 * @version 1.0
 */
@WebServlet("/TODO/statusOk")
public class SetStatusOk extends HttpServlet {

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

        boolean status = NoteDAO.setStatusOk(noteUserId, noteId);

        if (status) {
            HttpSession session = req.getSession(false);
            List<Note> notes = ( (User) session.getAttribute("user") ).getUserNotes();

            Iterator<Note> iter = notes.iterator();
            while (iter.hasNext()) {
                Note note = iter.next();
                if (note.getId() == noteId) {
                    notes.remove(note);
                    break;
                }
            }

            jsonObj.put("status", true);
            resp.setStatus(HttpServletResponse.SC_OK);
        } else {
            jsonObj.put("status", false);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        try (PrintWriter writer = resp.getWriter()) {
            jsonObj.writeJSONString(writer);
            logger.info("SetStatusOk response: " + jsonObj);
        } catch (NullPointerException | IOException e) {
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }

}
