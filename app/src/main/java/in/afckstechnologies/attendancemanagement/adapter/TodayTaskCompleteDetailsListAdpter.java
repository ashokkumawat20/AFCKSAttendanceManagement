package in.afckstechnologies.attendancemanagement.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import in.afckstechnologies.attendancemanagement.R;
import in.afckstechnologies.attendancemanagement.models.TodayTaskCompleteDetailsDAO;



public class TodayTaskCompleteDetailsListAdpter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    List<TodayTaskCompleteDetailsDAO> data;
    TodayTaskCompleteDetailsDAO current;


    // create constructor to innitilize context and data sent from MainActivity
    public TodayTaskCompleteDetailsListAdpter(Context context, List<TodayTaskCompleteDetailsDAO> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;

    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_today_task_complete_details, parent, false);
        MyHolder holder = new MyHolder(view);

        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final int pos = position;
        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder = (MyHolder) holder;
        current = data.get(position);


        myHolder.txt_subject.setText(current.getRequest_subject());
        myHolder.txt_subject.setTag(position);

        myHolder.sno.setText(current.getNumbers());
        myHolder.sno.setTag(position);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            String str = "Description  " + "<a>:-<font color='black'> " + current.getRequest_body() + "</font> </a>";
            myHolder.txt_body.setText(Html.fromHtml(str, Html.FROM_HTML_MODE_COMPACT));
            myHolder.txt_body.setTag(position);

        } else {
            //String str = "<a>"+rs+" <font color='black'> "+rb+"</font> </a>";
            String str = "Description  " + "<a>:-<font color='black'> " + current.getRequest_body() + "</font> </a>";
            myHolder.txt_body.setText(Html.fromHtml(str));
            myHolder.txt_body.setTag(position);


        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            String str = "Comments  " + "<a>:-<font color='black'> " + current.getTicket_comments() + "</font> </a>";
            myHolder.txt_comments.setText(Html.fromHtml(str, Html.FROM_HTML_MODE_COMPACT));
            myHolder.txt_comments.setTag(position);

        } else {
            //String str = "<a>"+rs+" <font color='black'> "+rb+"</font> </a>";
            String str = "Comments  " + "<a>:-<font color='black'> " + current.getTicket_comments() + "</font> </a>";
            myHolder.txt_comments.setText(Html.fromHtml(str));
            myHolder.txt_comments.setTag(position);


        }

    }


    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder {

        TextView txt_subject, sno, txt_body, txt_comments;


        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            txt_subject = (TextView) itemView.findViewById(R.id.txt_subject);
            sno = (TextView) itemView.findViewById(R.id.sno);
            txt_body = (TextView) itemView.findViewById(R.id.txt_body);

            txt_comments = (TextView) itemView.findViewById(R.id.txt_comments);


        }

    }

    public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate) {

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {
            //LOGE(TAG, "ParseException - dateFormat");
        }

        return outputDate;

    }

}
