package gr.aegean.com.nugeca;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


    private EditText timedifference;
    private EditText generatorsize;
    private Button referencedate;
    private Button eluriondate;
    private Button eluriontime;
    private Button lasteluriondate;
    private Button lasteluriontime;
    private TextView results1;
    private TextView results2;
    private double daystoref;
    private double hourstoref;
    private double totalhours;
    private double daysincelastelurion;
    private double hourssincelastelurion;
    private double totalsincelastelurion;
    private double l1;
    private double l2;
    private double gensizeelution;
    private double gensizelastelutions;
    private double growth;
    private double mo99halflife=66;
    private double tc99mhalflife=6.02;
    private double typicalyeld=0.9;
    private double branchingfactor=0.862;
    private Calendar calendar;
    private Button calculate;
    Button button;
    private int year, month, day;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timedifference = (EditText) findViewById(R.id.timedifference);
        generatorsize = (EditText) findViewById(R.id.generatorsize);
        referencedate = (Button) findViewById(R.id.referencedate);
        eluriondate = (Button) findViewById(R.id.eluriondate);
        eluriontime = (Button) findViewById(R.id.elutiontime);
        lasteluriondate = (Button) findViewById(R.id.lastelutiondate);
        lasteluriontime = (Button) findViewById(R.id.lastelutiontime);
        calculate = (Button) findViewById(R.id.calculate);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);


        results1 = (TextView) findViewById(R.id.results1);
        results2 = (TextView) findViewById(R.id.results2);


        initiate();
       // calculations();
        referencedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(v,referencedate);

            }
        });
        eluriondate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(v,eluriondate);
            }
        });
        lasteluriondate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(v,lasteluriondate);
            }
        });
        eluriontime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime(v,eluriontime);
            }
        });
        lasteluriontime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime(v,lasteluriontime);
            }
        });
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculations();
            }
        });
    }

    public void calculations(){

        daystoref=calculateHours(eluriondate.getText().toString(),referencedate.getText().toString());
        Log.e("Days to ref",""+daystoref);
        hourstoref=calculateHoursFromTime(eluriontime.getText().toString(),"12:00")-Integer.parseInt(timedifference.getText().toString());
        Log.e("Hours to ref",""+hourstoref);
        totalhours=daystoref+hourstoref;
        Log.e("Total to ref",""+totalhours);
        daysincelastelurion=calculateHours(eluriondate.getText().toString(),lasteluriondate.getText().toString());
        Log.e("Days since",""+daysincelastelurion);
        hourssincelastelurion=calculateHoursFromTime(eluriontime.getText().toString(),lasteluriontime.getText().toString());
        Log.e("Hours since",""+hourssincelastelurion);
        totalsincelastelurion=daysincelastelurion+hourssincelastelurion;
        Log.e("Total since",""+totalsincelastelurion);

        l1=Math.log(2)/mo99halflife;
        Log.e("l1",""+l1);
        l2=Math.log(2)/tc99mhalflife;
        Log.e("l2",""+l2);
        gensizeelution=Double.parseDouble(generatorsize.getText().toString())*Math.exp(-Math.log(2)*totalhours/mo99halflife);
        Log.e("Gen size elu",""+gensizeelution);
        gensizelastelutions=gensizeelution*Math.exp(Math.log(2)*totalsincelastelurion/mo99halflife);
        Log.e("Gen size last elu",""+gensizelastelutions);
        double temp2 = (l2/(l2-l1))*gensizelastelutions;
        Log.e("temp2",""+temp2);
        double temp1=(Math.exp(-(l1*totalsincelastelurion))-Math.exp(-(l2*totalsincelastelurion)));
        Log.e("temp1",""+temp1);
        growth=temp2*temp1*branchingfactor;
        Log.e("Growth",""+growth);
        double results =growth*typicalyeld;
        results1.setText(""+results);
        results2.setText(""+results/37*1000);
    }
    public void initiate(){
        timedifference.setText("2");
        referencedate.setText("26/03/2015");
        eluriondate.setText("31/03/2015");
        eluriontime.setText("9:00");
        lasteluriondate.setText("30/03/2015");
        lasteluriontime.setText("9:00");

    }

    @SuppressWarnings("deprecation")
    public void setDate(View view,Button button) {
        this.button=button;
        showDialog(999);

    }

    @SuppressWarnings("deprecation")
    public void setTime(View view,Button button){
        this.button=button;
        showDialog(998);
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        } else if (id==998) {
            return new TimePickerDialog(this,myTimeListener,0,0,true);
        }
        return null;
    }

    private TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            showTime(hourOfDay,minute);
        }
    };
    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day

                    showDate(arg1,arg2,arg3);
                }
            };

    private void showDate(int year, int month, int day ) {
        button.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }
    private void showTime(int hourOfDay,int minute){
        button.setText(new StringBuilder().append(hourOfDay).append(":").append(minute));
    }


    private int calculateHours(String date1,String date2){
        String[] tmp1=date1.split("/");
        String[] tmp2=date2.split("/");
        int hours = (Integer.parseInt(tmp1[0])- Integer.parseInt(tmp2[0]))*24 +
                ((Integer.parseInt(tmp1[1])- Integer.parseInt(tmp2[1]))*30*24+((Integer.parseInt(tmp1[2])- Integer.parseInt(tmp2[2]))*365*24));
        return hours;
    }
    private int calculateHoursFromTime(String time1,String time2){
        String[] tmp1=time1.split(":");
        String[] tmp2=time2.split(":");
        return Integer.parseInt(tmp1[0])-Integer.parseInt(tmp2[0]);
    }
}