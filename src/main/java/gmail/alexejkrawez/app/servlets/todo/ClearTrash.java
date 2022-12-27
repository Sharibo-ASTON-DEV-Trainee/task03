package gmail.alexejkrawez.app.servlets.todo;

import gmail.alexejkrawez.app.entities.NoteDAO;
import gmail.alexejkrawez.app.model.Note;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static gmail.alexejkrawez.app.entities.ConnectionDAO.logger;
import static java.lang.Integer.parseInt;

@WebServlet("/TODO/clearTrash")
public class ClearTrash extends HttpServlet {

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
        String reqUserId = null;
        try {
            jsonObj = (JSONObject) JSONValue.parseWithException(sb.toString());
            reqUserId = jsonObj.get("noteUserId").toString();

            jsonObj.clear();
        } catch (ParseException | NullPointerException e) {
            logger.error(e.getMessage(), e);
        }


        int noteUserId = parseInt(reqUserId);

        boolean status = NoteDAO.deleteDeletedNotes(noteUserId);

        if (status) {
            HttpSession session = req.getSession(false);
            List<Note> notes = (ArrayList<Note>) session.getAttribute("notes");
            List<Note> notes2 = new ArrayList<Note>(notes);

            Iterator<Note> iter = notes2.iterator();
            while (iter.hasNext()) {
                Note note = iter.next();
                if (note.getStatus() == 4) {

                    boolean fileStatus = true;
                    if (note.getFilePath() != null) {
                        fileStatus = new File(System.getenv("CATALINA_HOME") + "/webapps/task03/usersFiles/" +
                                File.separator + noteUserId + File.separator + note.getFilePath()).delete();
                    }

                    if (fileStatus) {
                        notes.remove(note);
                    } else {
                        notes.remove(note);

                        logger.error("File " + note.getFilePath() + " by user " + note.getUserId() + " is not delete!");
                    }
                }

            }

            session.setAttribute("notes", notes2);
            jsonObj.put("status", true);
        } else {
            jsonObj.put("status", false);
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
