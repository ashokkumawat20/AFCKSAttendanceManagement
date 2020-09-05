package in.afckstechnologies.attendancemanagement.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import in.afckstechnologies.attendancemanagement.R;
import in.afckstechnologies.attendancemanagement.adapter.EmployeeAttendanceListAdpter;
import in.afckstechnologies.attendancemanagement.adapter.EmployeeLeaveListAdminAdpter;
import in.afckstechnologies.attendancemanagement.jsonparser.JsonHelper;
import in.afckstechnologies.attendancemanagement.models.EmployeeAttendanceDAO;
import in.afckstechnologies.attendancemanagement.models.EmployeeDatesDAO;
import in.afckstechnologies.attendancemanagement.models.EmployeeLeaveDAO;
import in.afckstechnologies.attendancemanagement.models.MonthNameDAO;
import in.afckstechnologies.attendancemanagement.models.RequestChangeUsersNameDAO;
import in.afckstechnologies.attendancemanagement.utils.AppStatus;
import in.afckstechnologies.attendancemanagement.utils.Config;
import in.afckstechnologies.attendancemanagement.utils.Constant;
import in.afckstechnologies.attendancemanagement.utils.FeesListener;
import in.afckstechnologies.attendancemanagement.utils.SmsListener;
import in.afckstechnologies.attendancemanagement.utils.VersionChecker;
import in.afckstechnologies.attendancemanagement.utils.WebClient;
import in.afckstechnologies.attendancemanagement.view.ApplyEmployeeLeaveTodayByAdminView;
import in.afckstechnologies.attendancemanagement.view.TakeEmpAttendanceByAdminView;
import in.afckstechnologies.attendancemanagement.view.UpdateEmployeeLateAttendanceByAdminView;
import in.afckstechnologies.attendancemanagement.view.UpdateEmployeeLateAttendanceView;

public class ShowEmployeeAttendanceActivity extends AppCompatActivity {
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    Spinner usersName, spinnerMonth, spinnerCurrentDate;
    ProgressDialog mProgressDialog;
    private JSONObject jsonLeadObj,jsonObj1;
    String usersResponse = "", employeeLeaveRespone = "", addEAttendanceRespone = "";
    String userNameId = "";
    String monthId = "";
    JSONArray jsonArray;
    ArrayList<RequestChangeUsersNameDAO> userslist;
    ArrayList<EmployeeDatesDAO> dateslist;
    ArrayList<MonthNameDAO> monthlist;
    String employeeAttendanceRespone = "", monthResponse = "", datesResponse = "", date_name = "", leavependingCountResponse = "", leavePendingCount = "";
    List<EmployeeAttendanceDAO> data;
    EmployeeAttendanceListAdpter employeeAttendanceListAdpter;
    private RecyclerView mstudentList, employeeLeaveList;
    //Edit lead
    HashMap<String, String> edit_LeadLOCList;
    //Keys
    Set keys_Loc;
    Button showLeaveAdmin, showAttendanceAdmin;
    ArrayList<EmployeeLeaveDAO> leaveDAOArrayList;
    EmployeeLeaveListAdminAdpter employeeLeaveListAdpter;
    int backCount = 0;
    int al_flag = 1;
    TextView workFromHome, loginByAdmin, CurrentLeave, showAllLeave;
    boolean status;
    String message = "";
    String msg = "";

    String verifyMobileDeviceIdResponse = "";
    boolean statusv;
    String mobileDeviceId = "";
    JSONObject jsonObj;


    private String latestVersion = "", smspassResponse = "", getRoleusersResponse = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_employee_attendance);
        preferences = getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();
        usersName = (Spinner) findViewById(R.id.spinnerUsers);
        spinnerMonth = (Spinner) findViewById(R.id.spinnerMonth);
        spinnerCurrentDate = (Spinner) findViewById(R.id.spinnerCurrentDate);
        mstudentList = (RecyclerView) findViewById(R.id.employeeAttendanceList);
        showLeaveAdmin = (Button) findViewById(R.id.showLeaveAdmin);
        showAttendanceAdmin = (Button) findViewById(R.id.showAttendanceAdmin);
        employeeLeaveList = (RecyclerView) findViewById(R.id.employeeLeaveList);
        workFromHome = (TextView) findViewById(R.id.workFromHome);
        loginByAdmin = (TextView) findViewById(R.id.loginByAdmin);
        CurrentLeave = (TextView) findViewById(R.id.CurrentLeave);
        showAllLeave = (TextView) findViewById(R.id.showAllLeave);
        if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
            verifyMobileDeviceId();
            new initUsersSpinner().execute();
            new initLeavePendingCount().execute();
        } else {

            Toast.makeText(getApplicationContext(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
        }
        //  Calendar cal=Calendar.getInstance();
        //String dayName = new DateFormatSymbols().getWeekdays()[cal.get(Calendar.DAY_OF_WEEK)];
        //Log.d("day",dayName);
        UpdateEmployeeLateAttendanceView.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                new getEmployeeAttendanceList().execute();
            }
        });

        EmployeeLeaveListAdminAdpter.bindListener(new FeesListener() {
            @Override
            public void messageReceived(String messageText) {
                new getEmployeeLeaveList().execute();
            }
        });

        TakeEmpAttendanceByAdminView.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                new initUsersSpinner().execute();
                new initMonthSpinner().execute();
                new initLeavePendingCount().execute();
            }
        });
        ApplyEmployeeLeaveTodayByAdminView.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                new initUsersSpinner().execute();
                new initMonthSpinner().execute();
                new initLeavePendingCount().execute();
            }
        });
        UpdateEmployeeLateAttendanceByAdminView.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                new getEmployeeAttendanceList().execute();
            }
        });
        showLeaveAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                al_flag = 2;
                showLeaveAdmin.setVisibility(View.GONE);
                showAttendanceAdmin.setVisibility(View.VISIBLE);
                new getEmployeeLeaveList().execute();
            }
        });
        showAttendanceAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                al_flag = 1;
                showLeaveAdmin.setVisibility(View.VISIBLE);
                showAttendanceAdmin.setVisibility(View.GONE);
                new getEmployeeAttendanceList().execute();
            }
        });
        workFromHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ShowEmployeeAttendanceActivity.this);
                builder.setMessage("Do you want to mark Attendance ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                new submitData().execute();

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();
                            }
                        });

                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("Work From Home");
                alert.show();
            }
        });
        CurrentLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ShowEmployeeAttendanceActivity.this);
                builder.setMessage("Do you want to mark Current Leave ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                prefEditor.putString("emp_id_l", userNameId);
                                prefEditor.commit();
                                ApplyEmployeeLeaveTodayByAdminView applyEmployeeLeaveTodayView = new ApplyEmployeeLeaveTodayByAdminView();
                                applyEmployeeLeaveTodayView.show(getSupportFragmentManager(), "applyEmployeeLeaveTodayView");


                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();
                            }
                        });

                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("Current Leave");
                alert.show();
            }
        });
        loginByAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!userNameId.equals("0")) {
                    prefEditor.putString("emp_id_a", userNameId);
                    prefEditor.commit();
                    TakeEmpAttendanceByAdminView takeEmpAttendanceByAdminView = new TakeEmpAttendanceByAdminView();
                    takeEmpAttendanceByAdminView.show(getSupportFragmentManager(), "studentFeesEntryView");
                } else {
                    Toast.makeText(getApplicationContext(), "Please select user !", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    /**
     * Called when the activity is about to become visible.
     */
    @Override
    protected void onStart() {
        super.onStart();

        if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
            getUserRoles();


        }


    }
    //
    private class initUsersSpinner extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            //   mProgressDialog = new ProgressDialog(FixedAssetsActivity.this);
            // Set progressdialog title
            //   mProgressDialog.setTitle("Please Wait...");
            // Set progressdialog message
            // mProgressDialog.setMessage("Loading...");
            //mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            //  mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            jsonLeadObj = new JSONObject() {
                {
                    try {
                        put("user_id", preferences.getString("attendance_m_user_id", ""));

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };
            WebClient serviceAccess = new WebClient();

            //  String baseURL = "http://192.168.1.13:8088/lms/api/lead/showlead";
            Log.i("json", "json" + jsonLeadObj);
            usersResponse = serviceAccess.SendHttpPost(Config.URL_GETALLREQUESTEMPLOYEENAME, jsonLeadObj);
            Log.i("resp", "leadListResponse" + usersResponse);

            if (usersResponse.compareTo("") != 0) {
                if (isJSONValid(usersResponse)) {

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            try {

                                userslist = new ArrayList<>();
                                userslist.add(new RequestChangeUsersNameDAO("0", "All User", "0", "0"));
                                JSONArray LeadSourceJsonObj = new JSONArray(usersResponse);
                                for (int i = 0; i < LeadSourceJsonObj.length(); i++) {
                                    JSONObject json_data = LeadSourceJsonObj.getJSONObject(i);
                                    userslist.add(new RequestChangeUsersNameDAO(json_data.getString("id"), json_data.getString("first_name") + " " + json_data.getString("last_name"), json_data.getString("work_from_status"), json_data.getString("wfh_applicable_status"), json_data.getString("leaved_txt")));

                                }

                                jsonArray = new JSONArray(usersResponse);

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    });

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Please check your network connection", Toast.LENGTH_LONG).show();
                        }
                    });

                    return null;
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Please check your network connection.", Toast.LENGTH_LONG).show();
                    }
                });

                return null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            if (usersResponse.compareTo("") != 0) {

                // Spinner spinnerCustom = (Spinner) findViewById(R.id.spinnerBranch);
                ArrayAdapter<RequestChangeUsersNameDAO> adapter = new ArrayAdapter<RequestChangeUsersNameDAO>(ShowEmployeeAttendanceActivity.this, android.R.layout.simple_spinner_dropdown_item, userslist);
                // MyAdapter adapter = new MyAdapter(StudentsListActivity.this,R.layout.spinner_item,locationlist);
                usersName.setAdapter(adapter);
                usersName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#1c5fab"));
                        RequestChangeUsersNameDAO LeadSource = (RequestChangeUsersNameDAO) parent.getSelectedItem();
                        // Toast.makeText(getApplicationContext(), "Source ID: " + LeadSource.getId() + ",  Source Name : " + LeadSource.getLocation_name(), Toast.LENGTH_SHORT).show();
                        userNameId = LeadSource.getId();
                        if (userNameId == "0") {
                            showAllLeave.setVisibility(View.GONE);
                            showAllLeave.setText("");
                        } else {
                            showAllLeave.setVisibility(View.VISIBLE);
                            showAllLeave.setText(LeadSource.getLeaved_txt());
                        }
                        if (LeadSource.getWork_from_status().equals("1") && LeadSource.getWfh_applicable_status().equals("1")) {
                            workFromHome.setVisibility(View.VISIBLE);

                        } else {
                            workFromHome.setVisibility(View.GONE);

                        }
                        if (LeadSource.getWork_from_status().equals("1")) {

                            loginByAdmin.setVisibility(View.VISIBLE);
                            CurrentLeave.setVisibility(View.VISIBLE);
                        } else {

                            loginByAdmin.setVisibility(View.GONE);
                            CurrentLeave.setVisibility(View.GONE);
                        }
                        if (userNameId == "0") {

                            new getEmployeeAttendanceList().execute();
                        }
                        if (userNameId != "0") {
                            new getEmployeeAttendanceList().execute();
                        }
                        if (al_flag == 1) {
                            new getEmployeeAttendanceList().execute();
                        }
                        if (al_flag == 2) {

                            new getEmployeeLeaveList().execute();
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }


                });

                new initMonthSpinner().execute();
                //  mProgressDialog.dismiss();
            } else {
                // Close the progressdialog
                // mProgressDialog.dismiss();
            }
        }
    }

    private class initCurrentDateSpinner extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            //   mProgressDialog = new ProgressDialog(FixedAssetsActivity.this);
            // Set progressdialog title
            //   mProgressDialog.setTitle("Please Wait...");
            // Set progressdialog message
            // mProgressDialog.setMessage("Loading...");
            //mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            //  mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            jsonLeadObj = new JSONObject() {
                {
                    try {
                        put("month", monthId);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };
            WebClient serviceAccess = new WebClient();

            //  String baseURL = "http://192.168.1.13:8088/lms/api/lead/showlead";
            Log.i("json", "json" + jsonLeadObj);
            datesResponse = serviceAccess.SendHttpPost(Config.URL_GETALLEMPLOYEEDATES, jsonLeadObj);
            Log.i("resp", "datesResponse" + datesResponse);

            if (datesResponse.compareTo("") != 0) {
                if (isJSONValid(datesResponse)) {

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            try {

                                SimpleDateFormat format = new SimpleDateFormat("dd");
                                Calendar cal = Calendar.getInstance();
                                final String date = format.format(cal.getTime());

                                dateslist = new ArrayList<>();
                                dateslist.add(new EmployeeDatesDAO("0", "All"));
                                JSONArray LeadSourceJsonObj = new JSONArray(datesResponse);
                                for (int i = 0; i < LeadSourceJsonObj.length(); i++) {
                                    JSONObject json_data = LeadSourceJsonObj.getJSONObject(i);
                                    dateslist.add(new EmployeeDatesDAO(json_data.getString("day"), json_data.getString("login_datetime")));

                                }
                                edit_LeadLOCList = new HashMap<String, String>();
                                edit_LeadLOCList.put("", date);
                                keys_Loc = edit_LeadLOCList.keySet();
                                jsonArray = new JSONArray(datesResponse);

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    });

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Please check your network connection", Toast.LENGTH_LONG).show();
                        }
                    });

                    return null;
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Please check your network connection.", Toast.LENGTH_LONG).show();
                    }
                });

                return null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            if (datesResponse.compareTo("") != 0) {
                String key_loc = "";
                String value_loc = "";
                if (!keys_Loc.isEmpty()) {
                    for (Iterator i = keys_Loc.iterator(); i.hasNext(); ) {
                        key_loc = (String) i.next();
                        value_loc = (String) edit_LeadLOCList.get(key_loc);
                        Log.d("keys ", "" + key_loc + " = " + value_loc);
                    }

                }
                // Spinner spinnerCustom = (Spinner) findViewById(R.id.spinnerBranch);
                ArrayAdapter<EmployeeDatesDAO> adapter = new ArrayAdapter<EmployeeDatesDAO>(ShowEmployeeAttendanceActivity.this, android.R.layout.simple_spinner_dropdown_item, dateslist);
                // MyAdapter adapter = new MyAdapter(StudentsListActivity.this,R.layout.spinner_item,locationlist);
                spinnerCurrentDate.setAdapter(adapter);
                selectSpinnerItemByValue(spinnerCurrentDate, value_loc);
                spinnerCurrentDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#1c5fab"));
                        EmployeeDatesDAO LeadSource = (EmployeeDatesDAO) parent.getSelectedItem();
                        // Toast.makeText(getApplicationContext(), "Source ID: " + LeadSource.getId() + ",  Source Name : " + LeadSource.getLocation_name(), Toast.LENGTH_SHORT).show();
                        date_name = LeadSource.getDates();

                        new getEmployeeAttendanceList().execute();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }


                });
                //  mProgressDialog.dismiss();
            } else {
                // Close the progressdialog
                // mProgressDialog.dismiss();
            }
        }
    }

    //
    private class initMonthSpinner extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            //   mProgressDialog = new ProgressDialog(FixedAssetsActivity.this);
            // Set progressdialog title
            //   mProgressDialog.setTitle("Please Wait...");
            // Set progressdialog message
            // mProgressDialog.setMessage("Loading...");
            //mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            //  mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            jsonLeadObj = new JSONObject() {
                {
                    try {


                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };
            WebClient serviceAccess = new WebClient();

            //  String baseURL = "http://192.168.1.13:8088/lms/api/lead/showlead";
            Log.i("json", "json" + jsonLeadObj);
            monthResponse = serviceAccess.SendHttpPost(Config.URL_GETALLEMPLOYEEAMONTH, jsonLeadObj);
            Log.i("resp", "leadListResponse" + monthResponse);

            if (monthResponse.compareTo("") != 0) {
                if (isJSONValid(monthResponse)) {

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            try {

                                monthlist = new ArrayList<>();
                                //   monthlist.add(new MonthNameDAO("0", "Select Month"));
                                JSONArray LeadSourceJsonObj = new JSONArray(monthResponse);
                                for (int i = 0; i < LeadSourceJsonObj.length(); i++) {
                                    JSONObject json_data = LeadSourceJsonObj.getJSONObject(i);
                                    monthlist.add(new MonthNameDAO("" + i + 1, json_data.getString("MMYY")));

                                }

                                jsonArray = new JSONArray(monthResponse);

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    });

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Please check your network connection", Toast.LENGTH_LONG).show();
                        }
                    });

                    return null;
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Please check your network connection.", Toast.LENGTH_LONG).show();
                    }
                });

                return null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            if (usersResponse.compareTo("") != 0) {

                // Spinner spinnerCustom = (Spinner) findViewById(R.id.spinnerBranch);
                ArrayAdapter<MonthNameDAO> adapter = new ArrayAdapter<MonthNameDAO>(ShowEmployeeAttendanceActivity.this, android.R.layout.simple_spinner_dropdown_item, monthlist);
                // MyAdapter adapter = new MyAdapter(StudentsListActivity.this,R.layout.spinner_item,locationlist);
                spinnerMonth.setAdapter(adapter);
                spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#1c5fab"));
                        MonthNameDAO LeadSource = (MonthNameDAO) parent.getSelectedItem();
                        // Toast.makeText(getApplicationContext(), "Source ID: " + LeadSource.getId() + ",  Source Name : " + LeadSource.getLocation_name(), Toast.LENGTH_SHORT).show();
                        monthId = LeadSource.getMonth();
                        new initCurrentDateSpinner().execute();
                        if (al_flag == 1) {
                            new getEmployeeAttendanceList().execute();
                        }
                        if (al_flag == 2) {
                            new getEmployeeLeaveList().execute();
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }


                });

                //  mProgressDialog.dismiss();
            } else {
                // Close the progressdialog
                // mProgressDialog.dismiss();
            }
        }
    }

    //
    private class getEmployeeAttendanceList extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
          /*  mProgressDialog = new ProgressDialog(ShowEmployeeAttendanceActivity.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Please Wait...");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            //mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();*/
        }

        @Override
        protected Void doInBackground(Void... params) {

            jsonLeadObj = new JSONObject() {
                {
                    try {
                        put("user_id", userNameId);
                        put("monthId", monthId);
                        put("login_datetime", date_name);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };
            WebClient serviceAccess = new WebClient();

            //  String baseURL = "http://192.168.1.13:8088/srujanlms_new/api/Leadraw/showleadraw";
            Log.i("json", "json" + jsonLeadObj);
            employeeAttendanceRespone = serviceAccess.SendHttpPost(Config.URL_GETALLEMPLOYEEATTENDANCEBYUSERID, jsonLeadObj);
            Log.i("resp", "batchesListResponse" + employeeAttendanceRespone);
            if (employeeAttendanceRespone.compareTo("") != 0) {
                if (isJSONValid(employeeAttendanceRespone)) {

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            try {

                                data = new ArrayList<>();
                                JsonHelper jsonHelper = new JsonHelper();
                                data = jsonHelper.parseShowEmployeeAttendanceList(employeeAttendanceRespone);
                                jsonArray = new JSONArray(employeeAttendanceRespone);

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    });

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Please check your webservice", Toast.LENGTH_LONG).show();
                        }
                    });

                    return null;
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Please check your network connection.", Toast.LENGTH_LONG).show();
                    }
                });

                return null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            if (employeeAttendanceRespone.compareTo("") != 0) {
                mstudentList.setVisibility(View.VISIBLE);
                employeeLeaveList.setVisibility(View.GONE);
                employeeAttendanceListAdpter = new EmployeeAttendanceListAdpter(ShowEmployeeAttendanceActivity.this, data);
                mstudentList.setAdapter(employeeAttendanceListAdpter);
                mstudentList.setLayoutManager(new LinearLayoutManager(ShowEmployeeAttendanceActivity.this));
                employeeAttendanceListAdpter.notifyDataSetChanged();

                //  mProgressDialog.dismiss();
            } else {
                // Close the progressdialog
                //mProgressDialog.dismiss();
            }
        }
    }

    private void selectSpinnerItemByValue(Spinner spinner, String value) {
        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    private class getEmployeeLeaveList extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(ShowEmployeeAttendanceActivity.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Please Wait...");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            //mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            jsonLeadObj = new JSONObject() {
                {
                    try {
                        put("user_id", userNameId);
                        put("monthId", monthId);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };
            WebClient serviceAccess = new WebClient();

            //  String baseURL = "http://192.168.1.13:8088/srujanlms_new/api/Leadraw/showleadraw";
            Log.i("json", "json" + jsonLeadObj);
            employeeLeaveRespone = serviceAccess.SendHttpPost(Config.URL_GETALLEMPLOYEELEAVEBYADMIN, jsonLeadObj);
            Log.i("resp", "employeeLeaveRespone" + employeeLeaveRespone);
            if (employeeLeaveRespone.compareTo("") != 0) {
                if (isJSONValid(employeeLeaveRespone)) {

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            try {

                                leaveDAOArrayList = new ArrayList<>();
                                JsonHelper jsonHelper = new JsonHelper();
                                leaveDAOArrayList = jsonHelper.parseShowEmployeeLeaveList(employeeLeaveRespone);
                                jsonArray = new JSONArray(employeeLeaveRespone);

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    });

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Please check your webservice", Toast.LENGTH_LONG).show();
                        }
                    });

                    return null;
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Please check your network connection.", Toast.LENGTH_LONG).show();
                    }
                });

                return null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            if (employeeLeaveRespone.compareTo("") != 0) {
                mstudentList.setVisibility(View.GONE);
                employeeLeaveList.setVisibility(View.VISIBLE);
                employeeLeaveListAdpter = new EmployeeLeaveListAdminAdpter(ShowEmployeeAttendanceActivity.this, leaveDAOArrayList);
                employeeLeaveList.setAdapter(employeeLeaveListAdpter);
                employeeLeaveList.setLayoutManager(new LinearLayoutManager(ShowEmployeeAttendanceActivity.this));
                employeeLeaveListAdpter.notifyDataSetChanged();

                mProgressDialog.dismiss();
            } else {
                // Close the progressdialog
                mProgressDialog.dismiss();
            }
        }
    }

    private class initLeavePendingCount extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(ShowEmployeeAttendanceActivity.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Please Wait...");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            //mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            jsonLeadObj = new JSONObject() {
                {
                    try {


                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };
            WebClient serviceAccess = new WebClient();

            //  String baseURL = "http://192.168.1.13:8088/lms/api/lead/showlead";
            Log.i("json", "json" + jsonLeadObj);
            leavependingCountResponse = serviceAccess.SendHttpPost(Config.URL_GETCOUNTPENDINGLEAVEUSERS, jsonLeadObj);
            Log.i("resp", "leavependingCountResponse" + leavependingCountResponse);

            if (leavependingCountResponse.compareTo("") != 0) {
                if (isJSONValid(leavependingCountResponse)) {

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            try {


                                JSONObject jsonObject = new JSONObject(leavependingCountResponse);
                                leavePendingCount = jsonObject.getString("totalleavepending");


                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    });

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Please check your network connection", Toast.LENGTH_LONG).show();
                        }
                    });

                    return null;
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Please check your network connection.", Toast.LENGTH_LONG).show();
                    }
                });

                return null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            if (leavependingCountResponse.compareTo("") != 0) {
                showLeaveAdmin.setText("Leave " + leavePendingCount);

                mProgressDialog.dismiss();


            } else {
                // Close the progressdialog
                mProgressDialog.dismiss();
            }
        }
    }

    private class submitData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(ShowEmployeeAttendanceActivity.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Please Wait...");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            //mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            jsonLeadObj = new JSONObject() {
                {
                    try {
                        put("user_id", userNameId);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };
            WebClient serviceAccess = new WebClient();


            Log.i("json", "json" + jsonLeadObj);
            addEAttendanceRespone = serviceAccess.SendHttpPost(Config.URL_ADDEMPLOYEEATTENDANCEBYADMIN, jsonLeadObj);
            Log.i("resp", "addStudentRespone" + addEAttendanceRespone);


            if (addEAttendanceRespone.compareTo("") != 0) {
                if (isJSONValid(addEAttendanceRespone)) {


                    try {

                        JSONObject jsonObject = new JSONObject(addEAttendanceRespone);
                        status = jsonObject.getBoolean("status");
                        msg = jsonObject.getString("message");
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                } else {


                    Toast.makeText(getApplicationContext(), "Please check your webservice", Toast.LENGTH_LONG).show();


                }
            } else {

                Toast.makeText(getApplicationContext(), "Please check your network connection.", Toast.LENGTH_LONG).show();

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            if (status) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();


                // Close the progressdialog
                mProgressDialog.dismiss();
                new initUsersSpinner().execute();

            } else {
                // Close the progressdialog
                mProgressDialog.dismiss();

            }
        }
    }

    protected boolean isJSONValid(String callReoprtResponse2) {
        // TODO Auto-generated method stub
        try {
            new JSONObject(callReoprtResponse2);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(callReoprtResponse2);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

//

    public void verifyMobileDeviceId() {


        jsonObj = new JSONObject() {
            {
                try {
                    put("pDeviceID", preferences.getString("attendance_m_mobile_deviceid", ""));
                    put("role_id", "32");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Thread objectThread = new Thread(new Runnable() {
            public void run() {
                // TODO Auto-generated method stub
                WebClient serviceAccess = new WebClient();
                verifyMobileDeviceIdResponse = serviceAccess.SendHttpPost(Config.URL_GETAVAILABLEMOBILEDEVICES, jsonObj);
                Log.i("loginResponse", "verifyMobileDeviceIdResponse" + verifyMobileDeviceIdResponse);
                final Handler handler = new Handler(Looper.getMainLooper());
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() { // This thread runs in the UI
                            @Override
                            public void run() {
                                if (verifyMobileDeviceIdResponse.compareTo("") == 0) {

                                } else {

                                    try {
                                        JSONObject jObject = new JSONObject(verifyMobileDeviceIdResponse);
                                        statusv = jObject.getBoolean("status");

                                        if (statusv) {
                                        }

                                        else {
                                            finish();
                                            prefEditor.putString("attendance_m_user_id", "");
                                            prefEditor.commit();
                                            Intent i = new Intent(ShowEmployeeAttendanceActivity.this, SplashScreenActivity.class);
                                            startActivity(i);
                                        }
                                    } catch (JSONException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    }
                };

                new Thread(runnable).start();
            }
        });
        objectThread.start();
    }

    public void getUserRoles() {


        jsonObj1 = new JSONObject() {
            {
                try {
                    put("role_id", "32");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Thread objectThread = new Thread(new Runnable() {
            public void run() {
                // TODO Auto-generated method stub
                WebClient serviceAccess = new WebClient();
                getRoleusersResponse = serviceAccess.SendHttpPost(Config.URL_GETAVAILABLEROLES, jsonObj1);
                Log.i("getRoleusersResponse", getRoleusersResponse);
                final Handler handler = new Handler(Looper.getMainLooper());
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() { // This thread runs in the UI
                            @Override
                            public void run() {
                                if (getRoleusersResponse.compareTo("") == 0) {

                                } else {

                                    try {
                                        JSONObject jObject = new JSONObject(getRoleusersResponse);
                                        status = jObject.getBoolean("status");

                                        if (status) {
                                            forceUpdate();
                                        }
                                    } catch (JSONException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    }
                };

                new Thread(runnable).start();
            }
        });
        objectThread.start();
    }

    public void forceUpdate() {
        //  int playStoreVersionCode = FirebaseRemoteConfig.getInstance().getString("android_latest_version_code");
        VersionChecker versionChecker = new VersionChecker();
        try {
            latestVersion = versionChecker.execute().get();
            /*if (latestVersion.length() > 0) {
                latestVersion = latestVersion.substring(50, 58);
                latestVersion = latestVersion.trim();
            }*/


            Log.d("versoncode", "" + latestVersion);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        PackageManager packageManager = this.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        //  String currentVersion = packageInfo.versionName;
        String currentVersion = packageInfo.versionName;

        new ForceUpdateAsync(currentVersion, ShowEmployeeAttendanceActivity.this).execute();

    }

    public class ForceUpdateAsync extends AsyncTask<String, String, JSONObject> {


        private String currentVersion;
        private Context context;

        public ForceUpdateAsync(String currentVersion, Context context) {
            this.currentVersion = currentVersion;
            this.context = context;
        }

        @Override
        protected JSONObject doInBackground(String... params) {


            return new JSONObject();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (latestVersion != null) {
                if (!latestVersion.equals("")) {
                    if (!currentVersion.equalsIgnoreCase(latestVersion)) {
                        // Toast.makeText(context,"update is available.",Toast.LENGTH_LONG).show();

                        if (!((Activity) context).isFinishing()) {
                            showForceUpdateDialog();
                        }


                    }
                } else {
                    if (AppStatus.getInstance(getApplicationContext()).isOnline()) {

                        // AppUpdater appUpdater = new AppUpdater((Activity) context);
                        //  appUpdater.start();
                    } else {

                        Toast.makeText(getApplicationContext(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                    }

                }
            }
            super.onPostExecute(jsonObject);
        }

        public void showForceUpdateDialog() {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AppTheme));

            alertDialogBuilder.setTitle(context.getString(R.string.youAreNotUpdatedTitle));
            alertDialogBuilder.setMessage(context.getString(R.string.youAreNotUpdatedMessage) + " " + latestVersion + context.getString(R.string.youAreNotUpdatedMessage1));
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.getPackageName())));
                    dialog.cancel();
                }
            });
            alertDialogBuilder.show();
        }
    }
    //
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }
}

