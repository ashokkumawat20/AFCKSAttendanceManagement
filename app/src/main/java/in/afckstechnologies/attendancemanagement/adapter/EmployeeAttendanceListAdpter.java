package in.afckstechnologies.attendancemanagement.adapter;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import in.afckstechnologies.attendancemanagement.R;
import in.afckstechnologies.attendancemanagement.models.EmployeeAttendanceDAO;
import in.afckstechnologies.attendancemanagement.view.TodayTaskCompleteDetailsView;
import in.afckstechnologies.attendancemanagement.view.UpdateEmployeeLateAttendanceByAdminView;
import in.afckstechnologies.attendancemanagement.view.UpdateEmployeeLateAttendanceView;


public class EmployeeAttendanceListAdpter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<EmployeeAttendanceDAO> data;
    EmployeeAttendanceDAO current;
    int currentPos = 0;
    String id, id1;
    String centerId;
    int ID;

    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;

    ProgressDialog mProgressDialog;
    private JSONObject jsonLeadObj;
    JSONArray jsonArray;
    String centerListResponse = "";
    boolean status;
    String message = "";
    String msg = "";
    int clickflag = 0;


    // create constructor to innitilize context and data sent from MainActivity
    public EmployeeAttendanceListAdpter(Context context, List<EmployeeAttendanceDAO> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        preferences = context.getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_employee_attendance_details, parent, false);
        EmployeeAttendanceListAdpter.MyHolder holder = new EmployeeAttendanceListAdpter.MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final int pos = position;
        // Get current position of item in recyclerview to bind data and assign values from list
        final EmployeeAttendanceListAdpter.MyHolder myHolder = (EmployeeAttendanceListAdpter.MyHolder) holder;
        current = data.get(position);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        final String date = format.format(cal.getTime());
        myHolder.view_Address.setText(current.getAddress());
        myHolder.view_Address.setTag(position);

        myHolder.completeFlag.setTag(position);
        myHolder.whatsapp.setTag(position);
        myHolder.calling.setTag(position);
        myHolder.create_task.setTag(position);
        myHolder.clickForWhatsap.setTag(position);
        myHolder.lead_Layout.setTag(position);
        myHolder.todayTask.setTag(position);


        if (!current.getLogout_location().equals("null")) {
            myHolder.editEAttendance.setVisibility(View.GONE);
            myHolder.editEAttendance.setTag(position);
        } else {
            myHolder.editEAttendance.setVisibility(View.VISIBLE);
            myHolder.editEAttendance.setTag(position);
        }

        myHolder.editEAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                prefEditor.putString("logout_date", current.getDatetime());
                prefEditor.putString("id", current.getId());
                prefEditor.putString("e_user_id", current.getUser_id());
                prefEditor.putString("cur_date", current.getDatetime());
                prefEditor.commit();
                //  Toast.makeText(context, "" + current.getId(), Toast.LENGTH_LONG).show();
                UpdateEmployeeLateAttendanceView updateEmployeeLateAttendanceView = new UpdateEmployeeLateAttendanceView();
                updateEmployeeLateAttendanceView.show(((FragmentActivity) context).getSupportFragmentManager(), "updateEmployeeLateAttendanceView");

            }
        });

        myHolder.whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);

                PackageManager packageManager = context.getPackageManager();
                Intent i = new Intent(Intent.ACTION_VIEW);
                try {
                    String url = "https://api.whatsapp.com/send?phone=" + "91" + current.getMobile_no() + "&text=" + URLEncoder.encode("", "UTF-8");
                    if (preferences.getString("attendance_user_id", "").equals("AT")) {
                        i.setPackage("com.whatsapp.w4b");
                    } else {
                        i.setPackage("com.whatsapp");
                    }
                    i.setData(Uri.parse(url));
                    if (i.resolveActivity(packageManager) != null) {
                        context.startActivity(i);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        myHolder.calling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                Log.i("call ph no", "" + current.getMobile_no());
                callIntent.setData(Uri.parse("tel:" + current.getMobile_no()));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                context.startActivity(callIntent);
            }
        });
        myHolder.clickForWhatsap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (clickflag == 0) {
                    myHolder.completeFlag.setVisibility(View.VISIBLE);
                    clickflag = 1;
                } else {
                    clickflag = 0;
                    myHolder.completeFlag.setVisibility(View.GONE);
                }


            }
        });

       /* myHolder.create_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                Intent intent = new Intent(context, RequestChangeActivity.class);
                intent.putExtra("user_id", current.getUser_id());
                intent.putExtra("user_name", current.getFirst_name());
                intent.putExtra("share_data", "");
                context.startActivity(intent);
            }
        });*/

        myHolder.clickForWhatsap.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                       prefEditor.putString("logout_date", current.getDatetime());
                    prefEditor.putString("id", current.getId());
                    prefEditor.putString("e_user_id", current.getUser_id());
                    prefEditor.putString("cur_date", current.getDatetime());
                    prefEditor.putString("cur_login_time", current.getLogin_time());
                    prefEditor.putString("cur_logout_time", current.getLogout_time());
                    prefEditor.commit();
                    UpdateEmployeeLateAttendanceByAdminView updateEmployeeLateAttendanceByAdminView = new UpdateEmployeeLateAttendanceByAdminView();
                    updateEmployeeLateAttendanceByAdminView.show(((FragmentActivity) context).getSupportFragmentManager(), "updateEmployeeLateAttendanceView");

                    //  Toast.makeText(context, current.getLogin_time(), Toast.LENGTH_SHORT).show();


                return true;
            }
        });
        myHolder.todayTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                prefEditor.putString("ticket_close_date", current.getDatetime());
                prefEditor.putString("assign_to_user_id", current.getUser_id());
                prefEditor.commit();
                TodayTaskCompleteDetailsView todayTaskCompleteDetailsView = new TodayTaskCompleteDetailsView();
                todayTaskCompleteDetailsView.show(((FragmentActivity) context).getSupportFragmentManager(), "todayTaskCompleteDetailsView");
                // Toast.makeText(context, current.getDatetime() + current.getUser_id(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder {

        TextView view_entry_date, view_Address;
        ImageView maplocation, editEAttendance, whatsapp, calling, create_task, todayTask;
        LinearLayout clickForWhatsap, completeFlag, lead_Layout;


        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            view_Address = (TextView) itemView.findViewById(R.id.view_Address);

            editEAttendance = (ImageView) itemView.findViewById(R.id.editEAttendance);
            clickForWhatsap = (LinearLayout) itemView.findViewById(R.id.clickForWhatsap);
            completeFlag = (LinearLayout) itemView.findViewById(R.id.completeFlag);
            whatsapp = (ImageView) itemView.findViewById(R.id.whatsapp);
            calling = (ImageView) itemView.findViewById(R.id.calling);
            create_task = (ImageView) itemView.findViewById(R.id.create_task);
            lead_Layout = (LinearLayout) itemView.findViewById(R.id.lead_Layout);
            todayTask = (ImageView) itemView.findViewById(R.id.todayTask);
        }

    }

    public String getContactDetails(String phoneNumber1) {
        String searchNumber = phoneNumber1;
        String phoneNumber = "", emailAddress = "", name = "";
        StringBuffer sb = new StringBuffer();
        // Cursor c =  getContentResolver().query(contactData, null, null, null, null);
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(searchNumber));
        Cursor c = context.getContentResolver().query(uri, null, null, null, null);
        if (c.moveToFirst()) {


            name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
            //http://stackoverflow.com/questions/866769/how-to-call-android-contacts-list   our upvoted answer

            String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

            if (hasPhone.equalsIgnoreCase("1"))
                hasPhone = "true";
            else
                hasPhone = "false";

            if (Boolean.parseBoolean(hasPhone)) {
                Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                while (phones.moveToNext()) {
                    phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                }
                phones.close();
            }

            // Find Email Addresses
            Cursor emails = context.getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId, null, null);
            while (emails.moveToNext()) {
                emailAddress = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
            }
            emails.close();
            sb.append("\nUser Name:--- " + name + " \nCall Type:--- "
                    + " \nMobile Number:--- " + phoneNumber
                    + " \nEmail Id:--- " + emailAddress);
            sb.append("\n----------------------------------");


// add elements to al, including duplicates


            Log.d("curs", name + " num" + phoneNumber + " " + "mail" + emailAddress);
        }
        c.close();
        return name;
    }

}
