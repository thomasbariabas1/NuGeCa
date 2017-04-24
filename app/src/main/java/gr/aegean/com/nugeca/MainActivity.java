package gr.aegean.com.nugeca;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

import static java.text.DateFormat.getDateInstance;

public class MainActivity extends AppCompatActivity {


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
    private double tc99mhalflife=6.01;
    private double typicalyeld=0.9;
    private double branchingfactor=0.862;
    private Calendar calendar;
    private Button calculate;
    Button button;
    private Button button1;
    private Button button2;
    private int year, month, day;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button2.setText("15");
        button1.setText(""+2);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(MainActivity.this, button1);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.menu1, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        button1.setText(item.getTitle());
                        return true;
                    }
                });

                popup.show(); //showing popup menu
            }
        }); //closing the setOnClickListener method
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(MainActivity.this, button2);

                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.menu2, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        button2.setText(item.getTitle());
                        return true;
                    }
                });

                popup.show(); //showing popup menu
            }
        }); //closing the setOnClickListener method
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


        try {
            initiate();
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
        hourstoref=calculateHoursFromTime(eluriontime.getText().toString(),"12:00")-Integer.parseInt(button1.getText().toString());
        totalhours=daystoref+hourstoref;
        daysincelastelurion=calculateHours(eluriondate.getText().toString(),lasteluriondate.getText().toString());
        hourssincelastelurion=calculateHoursFromTime(eluriontime.getText().toString(),lasteluriontime.getText().toString());
        totalsincelastelurion=daysincelastelurion+hourssincelastelurion;

        l1=Math.log(2)/mo99halflife;
        l2=Math.log(2)/tc99mhalflife;
        gensizeelution=Double.parseDouble(button2.getText().toString())*Math.exp(-Math.log(2)*totalhours/mo99halflife);
        gensizelastelutions=gensizeelution*Math.exp(Math.log(2)*totalsincelastelurion/mo99halflife);
        double temp2 = (l2/(l2-l1))*gensizelastelutions;
        double temp1=(Math.exp(-(l1*totalsincelastelurion))-Math.exp(-(l2*totalsincelastelurion)));
        growth=temp2*temp1*branchingfactor;
        double results =growth*typicalyeld;
        double temp=results/37*1000;
        if(!(round(results, 2)<0)&&!(round(temp, 2)<0)) {
            results1.setText("" + round(results, 2));
            results2.setText("" + round(temp, 2));
        }else{
            results1.setText("WRONG DATE");
            results2.setText("CHECK DATES");
        }
    }
    public void initiate() throws ParseException {


        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        SimpleDateFormat formattime = new SimpleDateFormat("HH:mm", Locale.US);
        Date myDate = new Date();
        Date newDate = new Date(myDate.getTime() - 24 * 60 * 60 * 1000);
        Date newDate2 = new Date(myDate.getTime() - 2*24 * 60 * 60 * 1000);
        referencedate.setText(format.format(newDate2));
        eluriondate.setText(format.format(new Date()));

        eluriontime.setText(formattime.format(new Date()));

        lasteluriondate.setText(format.format(newDate));
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
                .append(month+1).append("/").append(year));
    }
    private void showTime(int hourOfDay,int minute){
        if(minute<10)
            button.setText(new StringBuilder().append(hourOfDay).append(":0").append(minute));
        else
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
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
