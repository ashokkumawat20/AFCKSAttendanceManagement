package in.afckstechnologies.attendancemanagement.jsonparser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.afckstechnologies.attendancemanagement.models.EmployeeAttendanceDAO;
import in.afckstechnologies.attendancemanagement.models.EmployeeLeaveDAO;
import in.afckstechnologies.attendancemanagement.models.TodayTaskCompleteDetailsDAO;



public class JsonHelper {


    private ArrayList<TodayTaskCompleteDetailsDAO> todayTaskCompleteDetailsDAOS = new ArrayList<TodayTaskCompleteDetailsDAO>();
    private TodayTaskCompleteDetailsDAO todayTaskCompleteDetailsDAO;

    private ArrayList<EmployeeAttendanceDAO> employeeAttendanceDAOArrayList = new ArrayList<EmployeeAttendanceDAO>();
    private EmployeeAttendanceDAO employeeAttendanceDAO;

    private ArrayList<EmployeeLeaveDAO> leaveDAOArrayList = new ArrayList<EmployeeLeaveDAO>();
    private EmployeeLeaveDAO employeeLeaveDAO;

    //studentPaser
    public ArrayList<EmployeeAttendanceDAO> parseShowEmployeeAttendanceList(String rawLeadListResponse) {
        // TODO Auto-generated method stub
        Log.d("scheduleListResponse", rawLeadListResponse);
        try {
            JSONArray leadJsonObj = new JSONArray(rawLeadListResponse);

            for (int i = 0; i < leadJsonObj.length(); i++) {
                employeeAttendanceDAO = new EmployeeAttendanceDAO();
                JSONObject json_data = leadJsonObj.getJSONObject(i);
                employeeAttendanceDAO.setId(json_data.getString("id"));
                employeeAttendanceDAO.setFirst_name(json_data.getString("first_name"));
                employeeAttendanceDAO.setUser_id(json_data.getString("user_id"));
                employeeAttendanceDAO.setAddress(json_data.getString("TXT"));
                employeeAttendanceDAO.setDatetime(json_data.getString("datetime"));
                employeeAttendanceDAO.setLogout_location(json_data.getString("logout_location"));
                employeeAttendanceDAO.setMobile_no(json_data.getString("mobile_no"));
                employeeAttendanceDAO.setLogin_time(json_data.getString("login_time"));
                employeeAttendanceDAO.setLogout_time(json_data.getString("logout_time"));
                employeeAttendanceDAOArrayList.add(employeeAttendanceDAO);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return employeeAttendanceDAOArrayList;
    }

    //userLeavePaser
    public ArrayList<EmployeeLeaveDAO> parseShowEmployeeLeaveList(String rawLeadListResponse) {
        // TODO Auto-generated method stub
        Log.d("scheduleListResponse", rawLeadListResponse);
        try {
            JSONArray leadJsonObj = new JSONArray(rawLeadListResponse);

            for (int i = 0; i < leadJsonObj.length(); i++) {
                employeeLeaveDAO = new EmployeeLeaveDAO();
                JSONObject json_data = leadJsonObj.getJSONObject(i);
                employeeLeaveDAO.setId(json_data.getString("id"));
                employeeLeaveDAO.setEmp_id(json_data.getString("emp_id"));
                employeeLeaveDAO.setApproval_status(json_data.getString("approval_status"));
                employeeLeaveDAO.setLeave_use_status(json_data.getString("leave_use_status"));
                employeeLeaveDAO.setLeave_date(json_data.getString("leave_date"));
                employeeLeaveDAO.setLeave_remarks(json_data.getString("leave_remarks"));
                employeeLeaveDAO.setDay(json_data.getString("day"));
                leaveDAOArrayList.add(employeeLeaveDAO);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return leaveDAOArrayList;
    }


    //TotalBatchTimedetailsrPaser
    public ArrayList<TodayTaskCompleteDetailsDAO> parseTodayTaskCompleteDetailsList(String rawLeadListResponse) {
        // TODO Auto-generated method stub
        Log.d("scheduleListResponse", rawLeadListResponse);
        try {
            JSONArray leadJsonObj = new JSONArray(rawLeadListResponse);
            int len = leadJsonObj.length();
            for (int i = 0; i < leadJsonObj.length(); i++) {
                String sequence = String.format("%03d", len--);
                todayTaskCompleteDetailsDAO = new TodayTaskCompleteDetailsDAO();
                JSONObject json_data = leadJsonObj.getJSONObject(i);
                todayTaskCompleteDetailsDAO.setId(json_data.getString("id"));
                todayTaskCompleteDetailsDAO.setRequest_subject(json_data.getString("request_subject"));
                todayTaskCompleteDetailsDAO.setRequest_body(json_data.getString("request_body"));
                todayTaskCompleteDetailsDAO.setTicket_comments(json_data.getString("ticket_comments"));
                todayTaskCompleteDetailsDAO.setNumbers("" + sequence);
                todayTaskCompleteDetailsDAOS.add(todayTaskCompleteDetailsDAO);


            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return todayTaskCompleteDetailsDAOS;
    }


}
