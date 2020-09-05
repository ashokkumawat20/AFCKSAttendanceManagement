package in.afckstechnologies.attendancemanagement.view;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import in.afckstechnologies.attendancemanagement.R;
import in.afckstechnologies.attendancemanagement.models.LocationDAO;
import in.afckstechnologies.attendancemanagement.utils.AppStatus;
import in.afckstechnologies.attendancemanagement.utils.Config;
import in.afckstechnologies.attendancemanagement.utils.Constant;
import in.afckstechnologies.attendancemanagement.utils.SmsListener;
import in.afckstechnologies.attendancemanagement.utils.WebClient;


/**
 * Created by Ashok Kumawat on 11/29/2017.
 */

public class UpdateEmployeeLateAttendanceByAdminView extends DialogFragment {

    Button saveBtn, editBtn;
    private EditText commentEdtTxt;
    Context context;
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    String loginResponse = "";

    String message;
    int user_id;
    boolean click = true;
    String studentcommentListResponse = "";
    JSONObject jsonObj;
    Boolean status;
    int count = 0;
    View registerView;
    private JSONObject jsonLeadObj;
    ProgressDialog mProgressDialog;
    JSONArray jsonArray;
    public static SmsListener mListener;
    String cooments = "";
    ArrayList<LocationDAO> locationlist;
    String centerResponse = "";
    ArrayList<String> strings;
    String center_id = "";
    Spinner spinnerCustom;
    private EditText LoginTimePick, LogoutTimePick;

    String logouttime = "", logintime = "";
    private static EditText datePickerLoginDate, datePickerLogoutDate;
    static String login_date = "", logout_date = "";
    ImageView clearDate, clearStarttime, clearEndtime, clearNextClassDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        View registerView = inflater.inflate(R.layout.dialog_update_emp_attendance_by_admin, null);

        context = getActivity();
        Window window = getDialog().getWindow();

        // set "origin" to top left corner, so to speak
        window.setGravity(Gravity.CENTER | Gravity.CENTER);

        // after that, setting values for x and y works "naturally"
        WindowManager.LayoutParams params = window.getAttributes();

        params.y = 50;
        window.setAttributes(params);


        preferences = getActivity().getSharedPreferences("Prefrence", getActivity().MODE_PRIVATE);
        prefEditor = preferences.edit();
        spinnerCustom = (Spinner) registerView.findViewById(R.id.spinnerBranch);
        saveBtn = (Button) registerView.findViewById(R.id.saveBtn);
        datePickerLoginDate = (EditText) registerView.findViewById(R.id.datePickerLoginDate);
        datePickerLogoutDate = (EditText) registerView.findViewById(R.id.datePickerLogoutDate);
        LoginTimePick = (EditText) registerView.findViewById(R.id.LoginTimePick);
        LoginTimePick.setText(preferences.getString("cur_login_time", ""));
        logintime = preferences.getString("cur_login_time", "");
        datePickerLoginDate.setText(formateDateFromstring("yyyy-MM-dd", "dd-MMM-yyyy", preferences.getString("cur_date", "")));
        login_date = preferences.getString("cur_date", "");
        LogoutTimePick = (EditText) registerView.findViewById(R.id.LogoutTimePick);
        if (!preferences.getString("cur_logout_time", "").equals("null")) {
            if (!preferences.getString("cur_logout_time", "").equals("")) {
                LogoutTimePick.setText(preferences.getString("cur_logout_time", ""));
                logouttime = preferences.getString("cur_logout_time", "");
                logout_date = preferences.getString("cur_date", "");
                datePickerLogoutDate.setText(formateDateFromstring("yyyy-MM-dd", "dd-MMM-yyyy", preferences.getString("cur_date", "")));
            }
        }


        clearDate = (ImageView) registerView.findViewById(R.id.clearDate);
        clearStarttime = (ImageView) registerView.findViewById(R.id.clearStarttime);
        clearEndtime = (ImageView) registerView.findViewById(R.id.clearEndtime);
        clearNextClassDate = (ImageView) registerView.findViewById(R.id.clearNextClassDate);
        LoginTimePick.setOnClickListener(new View.OnClickListener() {
            private int mHours, mMinutes;

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                final Calendar mcurrentTime = Calendar.getInstance();
                mHours = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                mMinutes = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // TODO Auto-generated method stub
                        logintime = String.format("%02d:%02d", hourOfDay, minute);
                        Log.d("output", logintime);
                        //logout.setText("" + hourOfDay + ":" + minute);
                        LoginTimePick.setText(logintime);
                    }
                }, mHours, mMinutes, false);
                mTimePickerDialog.setTitle("Select Time");
                mTimePickerDialog.show();
            }
        });

        LogoutTimePick.setOnClickListener(new View.OnClickListener() {
            private int mHours, mMinutes;

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                final Calendar mcurrentTime = Calendar.getInstance();
                mHours = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                mMinutes = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // TODO Auto-generated method stub
                        logouttime = String.format("%02d:%02d", hourOfDay, minute);
                        Log.d("output", logouttime);
                        //logout.setText("" + hourOfDay + ":" + minute);
                        LogoutTimePick.setText(logouttime);
                    }
                }, mHours, mMinutes, false);
                mTimePickerDialog.setTitle("Select Time");
                mTimePickerDialog.show();
            }
        });


        datePickerLoginDate.setOnClickListener(new View.OnClickListener() {
            private int mYear, mMonth, mDay;
            private SimpleDateFormat dateFormatter;

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                final Calendar mcurrentDate = Calendar.getInstance();
                mYear = mcurrentDate.get(Calendar.YEAR);
                mMonth = mcurrentDate.get(Calendar.MONTH);
                mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
                dateFormatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);

                final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                        mcurrentDate.set(selectedyear, selectedmonth, selectedday);
                        datePickerLoginDate.setText(dateFormatter.format(mcurrentDate.getTime()));
                        login_date = format.format(mcurrentDate.getTime());

                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select Date");
                mDatePicker.show();
            }
        });

        datePickerLogoutDate.setOnClickListener(new View.OnClickListener() {
            private int mYear, mMonth, mDay;
            private SimpleDateFormat dateFormatter;

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                final Calendar mcurrentDate = Calendar.getInstance();
                mYear = mcurrentDate.get(Calendar.YEAR);
                mMonth = mcurrentDate.get(Calendar.MONTH);
                mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
                dateFormatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);

                final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                        mcurrentDate.set(selectedyear, selectedmonth, selectedday);
                        datePickerLogoutDate.setText(dateFormatter.format(mcurrentDate.getTime()));
                        logout_date = format.format(mcurrentDate.getTime());

                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select Date");
                mDatePicker.show();
            }
        });

        clearDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerLoginDate.setText("");
                login_date = "";

            }
        });
        clearStarttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginTimePick.setText("");
                logintime = "";

            }
        });
        clearEndtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogoutTimePick.setText("");
                logouttime = "";

            }
        });
        clearNextClassDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerLogoutDate.setText("");
                logout_date = "";
            }
        });
        //  new initBranchSpinner().execute();
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setCancelable(false);

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                if ((keyCode == KeyEvent.KEYCODE_BACK)) {
                    //Hide your keyboard here!!!
                    // Toast.makeText(getActivity(), "PLease enter your information to get us connected with you.", Toast.LENGTH_LONG).show();
                    return true; // pretend we've processed it
                } else
                    return false; // pass on to be processed as normal
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                if (AppStatus.getInstance(context).isOnline()) {
                    if (click) {
                        if (validate(login_date, logintime)) {
                            sendData(logouttime);
                            click = false;
                        }
                    } else {
                        Toast.makeText(context, "Please Wait...", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    Toast.makeText(context, Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                }


            }
        });


        return registerView;
    }

    //
    private class initBranchSpinner extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(getActivity());
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
                        //  put("user_id", "" + preferences.getInt("user_id", 0));
                        // put("branch_id", flag);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };
            WebClient serviceAccess = new WebClient();

            //  String baseURL = "http://192.168.1.13:8088/lms/api/lead/showlead";
            Log.i("json", "json" + jsonLeadObj);
            centerResponse = serviceAccess.SendHttpPost(Config.URL_DISPLAY_CENTER, jsonLeadObj);
            Log.i("resp", "leadListResponse" + centerResponse);

            if (centerResponse.compareTo("") != 0) {
                if (isJSONValid(centerResponse)) {

                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            try {

                                locationlist = new ArrayList<>();
                                //  locationlist.add(new LocationDAO("0", "All"));

                                strings = new ArrayList<String>();
                                //  strings.add("All");
                                JSONArray LeadSourceJsonObj = new JSONArray(centerResponse);
                                for (int i = 0; i < LeadSourceJsonObj.length(); i++) {
                                    JSONObject json_data = LeadSourceJsonObj.getJSONObject(i);
                                    locationlist.add(new LocationDAO(json_data.getString("id"), json_data.getString("branch_name")));
                                    strings.add(json_data.getString("branch_name"));
                                }

                                jsonArray = new JSONArray(centerResponse);

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    });

                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "Please check your network connection", Toast.LENGTH_LONG).show();
                        }
                    });

                    return null;
                }
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Please check your network connection.", Toast.LENGTH_LONG).show();
                    }
                });

                return null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            if (centerResponse.compareTo("") != 0) {


                ArrayAdapter<LocationDAO> adapter = new ArrayAdapter<LocationDAO>(getActivity(), android.R.layout.simple_spinner_dropdown_item, locationlist);
                // MyAdapter adapter = new MyAdapter(StudentsListActivity.this,R.layout.spinner_item,locationlist);
                spinnerCustom.setAdapter(adapter);
                spinnerCustom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#1c5fab"));
                        LocationDAO LeadSource = (LocationDAO) parent.getSelectedItem();
                        //  Toast.makeText(getApplicationContext(), "Source ID: " + LeadSource.getId() + ",  Source Name : " + LeadSource.getBranch_name(), Toast.LENGTH_SHORT).show();
                        center_id = LeadSource.getId();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }


                });
                mProgressDialog.dismiss();
            } else {
                // Close the progressdialog
                mProgressDialog.dismiss();
            }
        }
    }

    public boolean validate(String login_date, String logintime) {
        boolean isValidate = false;
        if (login_date.equals("")) {
            Toast.makeText(getActivity(), "Please select login date.", Toast.LENGTH_LONG).show();
            isValidate = false;

        }

        if (logintime.equals("")) {
            Toast.makeText(getActivity(), "Please select login time.", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else {
            isValidate = true;
        }
        return isValidate;
    }


    public void sendData(final String comment) {


        jsonObj = new JSONObject() {
            {
                try {

                    put("user_id", preferences.getString("e_user_id", ""));
                    put("login_date", preferences.getString("cur_date", ""));
                    put("login_datetime", login_date + " " + logintime + ":00");
                    if (!logout_date.equals("")) {
                        put("logout_datetime", logout_date + " " + logouttime + ":00");
                    } else {
                        put("logout_datetime", "null");
                    }
                    put("id", preferences.getString("id", ""));


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Thread objectThread = new Thread(new Runnable() {
            public void run() {
                // TODO Auto-generated method stub
                WebClient serviceAccess = new WebClient();

                Log.i("jsonObj", "jsonObj" + jsonObj);
                loginResponse = serviceAccess.SendHttpPost(Config.URL_UPDATEEMPATTENDETAILSBYADMIN, jsonObj);
                Log.i("loginResponse", "loginResponse" + loginResponse);

                final Handler handler = new Handler(Looper.getMainLooper());
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() { // This thread runs in the UI
                            @Override
                            public void run() {
                                if (loginResponse.compareTo("") == 0) {
                                    Toast.makeText(getActivity(), "", Toast.LENGTH_LONG).show();
                                } else {

                                    try {
                                        JSONObject jObject = new JSONObject(loginResponse);
                                        status = jObject.getBoolean("status");
                                        message = jObject.getString("message");
                                        if (status) {
                                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                                            mListener.messageReceived(message);
                                            dismiss();
                                            //Intent intent = new Intent(context, BookSeatActivity.class);
                                            //startActivity(intent);
                                        } else {
                                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                                            dismiss();
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

    @Override
    public void onResume() {
        super.onResume();

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {

                if ((keyCode == KeyEvent.KEYCODE_BACK)) {
                    //This is the filter
                    if (event.getAction() != KeyEvent.ACTION_DOWN) {
                        update();

                        // Toast.makeText(getActivity(), "Your information is valuable for us and won't be misused.", Toast.LENGTH_LONG).show();
                        return true;
                    } else {
                        //Hide your keyboard here!!!!!!
                        return true; // pretend we've processed it
                    }
                } else
                    return false; // pass on to be processed as normal
            }
        });
    }

    private void update() {


        dismiss();
    }

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }

    public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate) {

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {
            //LOGE(TAG, "ParseException - dateFormat");
        }

        return outputDate;

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


}