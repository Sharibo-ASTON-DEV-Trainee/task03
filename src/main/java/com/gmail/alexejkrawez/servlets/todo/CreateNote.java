package com.gmail.alexejkrawez.servlets.todo;

import com.gmail.alexejkrawez.entities.NoteDAO;
import com.gmail.alexejkrawez.model.Note;
import com.gmail.alexejkrawez.model.User;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

import static java.lang.Integer.parseInt;

/**
 * A new note that came in the request is written to the database.
 * If the note contains an attached file, initializes its download.
 * The file is downloaded to the directory: userFiles/user_id/ .
 * 4 random digits are added to the beginning of the file name,
 * thereby ensuring the uniqueness of the files of a particular user.
 * Upon successful writing to the DBMS, it places this note in the
 * User object stored in the session.
 * On success, returns that note's data to the client.
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
@WebServlet("/TODO/message")
public class CreateNote extends HttpServlet {

    /**
     * Provides logging to the console and to a file.
     */
    public static final Logger logger = LogManager.getLogger(CreateNote.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");

        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(1024 * 1024 * 2); // буфер данных в байтах
        File tempDir = new File("/webapp/");
        factory.setRepository(tempDir);

        ServletFileUpload upload = new ServletFileUpload(factory); // загрузчик
        upload.setSizeMax(1024 * 1024 * 10); // файл максимум 10Mb


        JSONObject jsonObj = new JSONObject();
        String text = null;
        String status = "1";
        String fileName = null;
        boolean down;

        try {
            List items = upload.parseRequest(req);
            System.out.println(items);
            Iterator iter = items.iterator();

            while (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();
                System.out.println(item);

                if (item.isFormField()) {
                    jsonObj = (JSONObject) JSONValue.parseWithException(item.getString(StandardCharsets.UTF_8.name()));

                    text = jsonObj.get("text").toString();
                    text = JSONValue.escape(text);
                    status = jsonObj.get("status").toString();

                    jsonObj.clear();
                } else {
                    fileName = processUploadingFile(user.getUserId(), item);
                }
            }

        } catch (FileUploadBase.SizeLimitExceededException e) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            logger.error(e.getMessage(), e);
        }

        if (fileName == null) {
            down = NoteDAO.createNote(user.getUserId(), text, parseInt(status));
        } else {
            down = NoteDAO.createNoteWithFile(user.getUserId(), text, fileName, parseInt(status));
        }

        if (down) {
            Note note = NoteDAO.getLastNote(user.getUserId());
            if (note != null) {
                user.getUserNotes().add(note);
                session.setAttribute("user", user);
                jsonObj.put("note", note);
                resp.setStatus(HttpServletResponse.SC_CREATED);
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

        } else {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        try (PrintWriter writer = resp.getWriter()) {
            jsonObj.writeJSONString(writer);
            logger.info("CreateNote response: " + jsonObj);
        } catch (NullPointerException | IOException e) {
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }

    private String processUploadingFile(int userId, FileItem item) throws Exception {

        Path path = Paths.get(getServletContext().getRealPath(new StringBuilder().append(File.separator)
                .append("usersFiles").append(File.separator).append(userId).toString()));

        if (!path.toFile().exists()) {
            path.toFile().mkdirs();
        }

        int random = (int) (Math.random() * 10000);
        if (random <= 999) {
            random += 999;
        }

        String fileName = random + item.getName();

        File downloading = new File(path + File.separator + fileName);
        item.write(downloading);

        return fileName;
    }

}




