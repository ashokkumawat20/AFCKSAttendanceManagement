package in.afckstechnologies.attendancemanagement.models;

/**
 * Created by Ashok on 4/18/2017.
 */

public class TodayTaskCompleteDetailsDAO {
    String id = "";
    String request_subject = "";
    String request_body = "";
    String ticket_comments = "";
    String numbers = "";

    public TodayTaskCompleteDetailsDAO() {
    }

    public TodayTaskCompleteDetailsDAO(String id, String request_subject, String request_body, String ticket_comments, String numbers) {
        this.id = id;
        this.request_subject = request_subject;
        this.request_body = request_body;
        this.ticket_comments = ticket_comments;
        this.numbers = numbers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRequest_subject() {
        return request_subject;
    }

    public void setRequest_subject(String request_subject) {
        this.request_subject = request_subject;
    }

    public String getRequest_body() {
        return request_body;
    }

    public void setRequest_body(String request_body) {
        this.request_body = request_body;
    }

    public String getTicket_comments() {
        return ticket_comments;
    }

    public void setTicket_comments(String ticket_comments) {
        this.ticket_comments = ticket_comments;
    }

    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }
}
