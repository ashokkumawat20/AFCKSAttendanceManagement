package in.afckstechnologies.attendancemanagement.models;

/**
 * Created by Ashok Kumawat on 12/6/2017.
 */

public class EmployeeAttendanceDAO {
    String id = "";
    String first_name = "";
    String user_id = "";
    String address = "";
    String logout_location = "";
    String datetime = "";
    String mobile_no = "";
    String login_time = "";
    String logout_time = "";

    public EmployeeAttendanceDAO() {

    }

    public EmployeeAttendanceDAO(String id, String first_name, String user_id, String address, String logout_location, String datetime, String mobile_no, String login_time, String logout_time) {
        this.id = id;
        this.first_name = first_name;
        this.user_id = user_id;
        this.address = address;
        this.logout_location = logout_location;
        this.datetime = datetime;
        this.mobile_no = mobile_no;
        this.login_time = login_time;
        this.logout_time = logout_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLogout_location() {
        return logout_location;
    }

    public void setLogout_location(String logout_location) {
        this.logout_location = logout_location;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getLogin_time() {
        return login_time;
    }

    public void setLogin_time(String login_time) {
        this.login_time = login_time;
    }

    public String getLogout_time() {
        return logout_time;
    }

    public void setLogout_time(String logout_time) {
        this.logout_time = logout_time;
    }
}
