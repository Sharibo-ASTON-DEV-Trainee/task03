package com.gmail.alexejkrawez.servlets.todo;

import com.gmail.alexejkrawez.entities.NoteDAO;
import com.gmail.alexejkrawez.model.Note;
import com.gmail.alexejkrawez.model.User;
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
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import static java.lang.Integer.parseInt;

/**
 * Deletes a specific note from the DBMS. On success, it also finds the given note
 * in the User object stored in the session. If the note contained a file, deletes
 * the file attached to it from the server. Finally, removes the note from the list
 * of notes in the User object.
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
@WebServlet("/TODO/delete")
public class DeleteNote extends HttpServlet {

    /**
     * Provides logging to the console and to a file.
     */
    public static final Logger logger = LogManager.getLogger(DeleteNote.class);

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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

        boolean status = NoteDAO.deleteNote(noteUserId, noteId);

        if (status) {
            HttpSession session = req.getSession(false);
            User user = (User) session.getAttribute("user");
            List<Note> notes = user.getUserNotes();

            Iterator<Note> iter = notes.iterator();
            while (iter.hasNext()) {
                Note note = iter.next();
                if (note.getId() == noteId) {

                    boolean fileStatus = true;
                    if (note.getFilePath() != null) {
                        fileStatus = new File(System.getenv("CATALINA_HOME") + "/webapps/task03/usersFiles/" +
                                File.separator + noteUserId + File.separator + note.getFilePath()).delete();
                    }

                    if (fileStatus) {
                        notes.remove(note);
                        break;
                    } else {
                        notes.remove(note);

                        logger.error("File " + note.getFilePath() + "in note" + note.getId() +
                                " by user " + note.getUserId() + " is not delete!");
                        break;
                    }
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
        } catch (NullPointerException | IOException e) {
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }

}