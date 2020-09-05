package in.afckstechnologies.attendancemanagement.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import in.afckstechnologies.attendancemanagement.R;
import in.afckstechnologies.attendancemanagement.utils.Config;
import in.afckstechnologies.attendancemanagement.utils.SmsListener;
import in.afckstechnologies.attendancemanagement.utils.WebClient;


public class ApplyEmployeeLeaveTodayByAdminView extends DialogFragment {

    Button applyBtn;
    EditText leaveReasonEdtTxt;
    Context context;
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    String loginResponse = "";

    String message;
    int user_id;
    boolean click = true;
    String todayLeaveResponse = "";

    JSONObject jsonObj;
    Boolean status;
    int count = 0;
    View registerView;
    private JSONObject jsonObject;
    ProgressDialog mProgressDialog;
    JSONArray jsonArray;
    public static SmsListener mListener;

    String msg = "", reason = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        View registerView = inflater.inflate(R.layout.dialog_emp_leave_today, null);

        context = getActivity();
        Window window = getDialog().getWindow();

        // set "origin" to top left corner, so to speak
        window.setGravity(Gravity.CENTER | Gravity.CENTER);

        // after that, setting values for x and y works "naturally"
        WindowManager.LayoutParams params = window.getAttributes();

        params.y = 50;
        window.setAttributes(params);
        leaveReasonEdtTxt = (EditText) registerView.findViewById(R.id.leaveReasonEdtTxt);

        preferences = getActivity().getSharedPreferences("Prefrence", getActivity().MODE_PRIVATE);
        prefEditor = preferences.edit();
        applyBtn = (Button) registerView.findViewById(R.id.applyBtn);
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


        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reason = leaveReasonEdtTxt.getText().toString().trim();
                if (validate(reason)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Do you want to Apply Leave For Today ?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();

                                    new applyCurrentDayLeave().execute();

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
                    alert.setTitle("Appling Leave");
                    alert.show();
                } else {

                }
            }
        });

        return registerView;
    }

    public boolean validate(String reason) {
        boolean isValidate = false;
        if (reason.trim().equals("")) {
            Toast.makeText(getActivity(), "Please enter leave reason.", Toast.LENGTH_LONG).show();
        } else {
            isValidate = true;
        }

        return isValidate;
    }

    private class applyCurrentDayLeave extends AsyncTask<Void, Void, Void> {
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
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");//yyyy-MM-dd hh:mm:ss
            Calendar cal = Calendar.getInstance();
            final String date = format.format(cal.getTime());
            jsonObj = new JSONObject() {
                {
                    try {
                        put("user_id", preferences.getString("emp_id_l", ""));
                        put("current_date", date);
                        put("LeaveReason", reason);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            WebClient serviceAccess = new WebClient();
            Log.i("json", "json" + jsonObj);
            todayLeaveResponse = serviceAccess.SendHttpPost(Config.URL_APPLYCURRENTDAYLEAVE, jsonObj);
            Log.i("resp", "todayLeaveResponse" + todayLeaveResponse);


            if (todayLeaveResponse.compareTo("") != 0) {
                if (isJSONValid(todayLeaveResponse)) {


                    try {

                        jsonObject = new JSONObject(todayLeaveResponse);
                        status = jsonObject.getBoolean("status");
                        msg = jsonObject.getString("message");

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                } else {
                    Toast.makeText(getActivity(), "Please check your webservice", Toast.LENGTH_LONG).show();
                }
            } else {

                Toast.makeText(getActivity(), "Please check your network connection.", Toast.LENGTH_LONG).show();

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            if (status) {

                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                // Close the progressdialog
                dismiss();
                mListener.messageReceived(message);
                mProgressDialog.dismiss();
            } else {

                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                dismiss();
                mProgressDialog.dismiss();

            }
        }
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