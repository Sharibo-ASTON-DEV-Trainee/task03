package com.gmail.alexejkrawez.servlets.todo;

import com.gmail.alexejkrawez.model.Note;
import com.gmail.alexejkrawez.model.User;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static com.gmail.alexejkrawez.entities.ConnectionDAO.logger;

/**
 * Web Servlet.
 * Calls the User object stored in the session and gets a list of notes.
 * Returns a list of notes.
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
@WebServlet("/TODO/getNotes")
public class GetNotes extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);

        Integer list = (Integer) session.getAttribute("list");
        List<Note> notes = ( (User) session.getAttribute("user") ).getUserNotes();

        JSONObject jsonObj = new JSONObject();

        if (notes.isEmpty()) {
            jsonObj.put("0", null);
        } else {
            Integer i = 0;
            for (Note note : notes) {
                jsonObj.put(i.toString(), note);
                i++;
            }
        }

        jsonObj.put("list", list.toString());
        resp.setStatus(HttpServletResponse.SC_OK);

        try (PrintWriter writer = resp.getWriter()) {
            jsonObj.writeJSONString(writer);
            logger.info("GetNotes response: " + jsonObj);
        } catch (NullPointerException | IOException e) {
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }

}