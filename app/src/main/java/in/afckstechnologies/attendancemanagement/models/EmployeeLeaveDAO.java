package in.afckstechnologies.attendancemanagement.models;

/**
 * Created by Ashok Kumawat on 12/6/2017.
 */

public class EmployeeLeaveDAO {
    String id="";
    String emp_id="";
    String leave_remarks="";
    String approval_status="";
    String leave_date="";
    String leave_use_status="";
    String day="";
    public EmployeeLeaveDAO()
    {

    }

    public EmployeeLeaveDAO(String id, String emp_id, String leave_remarks, String approval_status, String leave_date, String leave_use_status, String day) {
        this.id = id;
        this.emp_id = emp_id;
        this.leave_remarks = leave_remarks;
        this.approval_status = approval_status;
        this.leave_date = leave_date;
        this.leave_use_status = leave_use_status;
        this.day = day;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public String getLeave_remarks() {
        return leave_remarks;
    }

    public void setLeave_remarks(String leave_remarks) {
        this.leave_remarks = leave_remarks;
    }

    public String getApproval_status() {
        return approval_status;
    }

    public void setApproval_status(String approval_status) {
        this.approval_status = approval_status;
    }

    public String getLeave_date() {
        return leave_date;
    }

    public void setLeave_date(String leave_date) {
        this.leave_date = leave_date;
    }

    public String getLeave_use_status() {
        return leave_use_status;
    }

    public void setLeave_use_status(String leave_use_status) {
        this.leave_use_status = leave_use_status;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
