package com.gmail.alexejkrawez.model;

/**
 * POJO class describing the user's todu-notes.
 *
 * <p>package: com.gmail.alexejkrawez.model</p>
 * <p>email: AlexejKrawez@gmail.com</p>
 * <p>created: 30.12.2022</p>
 *
 * @since Java v1.8
 *
 * @author Alexej Krawez
 * @version 1.0
 */
public class Note {

    private int userId;
    private int id;
    private String date;
    private String targetDate;
    private String note;
    private String filePath;
    private int status;

    public Note(int userId, int id, String date, String targetDate, String note, String filePath, int status) {
        this.userId = userId;
        this.id = id;
        this.date = date;
        this.targetDate = targetDate;
        this.note = note;
        this.filePath = filePath;
        this.status = status;
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getTargetDate() {
        return targetDate;
    }
    public void setTargetDate(String targetDate) {
        this.targetDate = targetDate;
    }

    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }

    public String getFilePath() {
        return filePath;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }


    @Override
    public String toString() {
        return "\n{" +
                "\"user_id\":\"" + userId + "\"" +
                ", \"id\":\"" + id + "\"" +
                ", \"date\":\"" + date + "\"" +
                ", \"target_date\":\"" + targetDate + "\"" +
                ", \"note\":\"" + note + "\"" +
                ", \"file_path\":\"" + filePath + "\"" +
                ", \"status\":\"" + status + "\"" +
                "}";
    }
}
