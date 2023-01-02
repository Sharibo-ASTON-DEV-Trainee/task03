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

import static java.lang.Integer.parseInt;

/**
 * Deletes the file related to a particular note from the DBMS. If successful,
 * it also finds this note in the User object stored in the session
 * and sets the value of the filePath field to null.
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
@WebServlet("/TODO/deleteFile")
public class DeleteFile extends HttpServlet {

    /**
     * Provides logging to the console and to a file.
     */
    public static final Logger logger = LogManager.getLogger(DeleteFile.class);

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

        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");

        boolean status = false;
        int position = -1;

        for (Note note : user.getUserNotes()) {
            if (note.getId() == noteId) {
                position = user.getUserNotes().indexOf(note);
                if (JSONValue.parse(note.getNote()) != null) {
                    status = NoteDAO.deleteNoteFile(noteUserId, noteId);
                    break;
                } else {
                    status = NoteDAO.deleteNote(noteUserId, noteId);
                    break;
                }
            }
        }

        if (status) {

            if (position > -1) {
                Note note = user.getUserNotes().get(position);

                boolean fileStatus = true;
                if (note.getFilePath() != null) {
                    fileStatus = new File(System.getenv("CATALINA_HOME") + "/webapps/task03/usersFiles/" +
                            File.separator + noteUserId + File.separator + note.getFilePath())
                            .delete();
                }

                if (fileStatus) {
                    note.setFilePath(null);
                } else {
                    note.setFilePath(null);

                    logger.error("File " + note.getFilePath() + " in note " + note.getId() +
                            " by user " + note.getUserId() + " is not delete!");
                }

            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }


            if (user.getUserNotes().get(position).getNote() == null) {
                user.getUserNotes().remove(user.getUserNotes().get(position));
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
            logger.info("DeleteFile response: " + jsonObj);
        } catch (NullPointerException | IOException e) {
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }

}