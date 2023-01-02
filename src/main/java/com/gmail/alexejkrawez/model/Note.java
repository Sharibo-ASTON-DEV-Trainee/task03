package com.gmail.alexejkrawez.model;

import java.util.Objects;

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
 * @version 1.1
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Note)) return false;
        Note note1 = (Note) o;
        return userId == note1.userId &&
                id == note1.id &&
                status == note1.status &&
                date.equals(note1.date) &&
                targetDate.equals(note1.targetDate) &&
                Objects.equals(note, note1.note) &&
                Objects.equals(filePath, note1.filePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                userId,
                id,
                date,
                targetDate,
                note,
                filePath,
                status
        );
    }


}
