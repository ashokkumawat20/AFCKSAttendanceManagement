package in.afckstechnologies.attendancemanagement.models;

/**
 * Created by admin on 3/7/2017.
 */

public class RequestChangeUsersNameDAO {
    String id = "";
    String user_name = "";
    String work_from_status = "";
    String wfh_applicable_status = "";
    String leaved_txt="";
    public RequestChangeUsersNameDAO() {

    }

    public RequestChangeUsersNameDAO(String id, String user_name, String work_from_status, String wfh_applicable_status) {
        this.id = id;
        this.user_name = user_name;
        this.work_from_status = work_from_status;
        this.wfh_applicable_status = wfh_applicable_status;
    }

    public RequestChangeUsersNameDAO(String id, String user_name, String work_from_status, String wfh_applicable_status, String leaved_txt) {
        this.id = id;
        this.user_name = user_name;
        this.work_from_status = work_from_status;
        this.wfh_applicable_status = wfh_applicable_status;
        this.leaved_txt = leaved_txt;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getWork_from_status() {
        return work_from_status;
    }

    public void setWork_from_status(String work_from_status) {
        this.work_from_status = work_from_status;
    }

    public String getWfh_applicable_status() {
        return wfh_applicable_status;
    }

    public void setWfh_applicable_status(String wfh_applicable_status) {
        this.wfh_applicable_status = wfh_applicable_status;
    }

    public String getLeaved_txt() {
        return leaved_txt;
    }

    public void setLeaved_txt(String leaved_txt) {
        this.leaved_txt = leaved_txt;
    }

    @Override
    public String toString() {
        return user_name;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RequestChangeUsersNameDAO) {
            RequestChangeUsersNameDAO c = (RequestChangeUsersNameDAO) obj;
            if (c.getUser_name().equals(user_name) && c.getId() == id) return true;
        }

        return false;
    }
}
