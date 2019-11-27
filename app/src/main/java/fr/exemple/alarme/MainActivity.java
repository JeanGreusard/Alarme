package fr.exemple.alarme;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private TextView textViewDate;
    private TextView textViewHeure;
    private Button buttonDate;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private Button buttonHeure;
    private TimePickerDialog.OnTimeSetListener onTimeSetListener;
    private EditText editTextNote;
    private Button buttonActiver;
    public static final int REQUEST_CODE=101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewDate=(TextView)findViewById(R.id.textViewDate);
        textViewHeure=(TextView)findViewById(R.id.textViewHeure);
        buttonDate=(Button)findViewById(R.id.ButtonDate);
        buttonHeure=(Button)findViewById(R.id.buttonHeure);
        buttonActiver=(Button)findViewById(R.id.buttonActiver);
        editTextNote=(EditText)findViewById(R.id.editTextNote);

        clickButtonDate();
        clickButtonHeure();
        clickButtonActiver();
    }

  private void clickButtonDate()
  {
      // le calendrier pour sélectionner une date
      buttonDate.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Calendar calendar=Calendar.getInstance();
              int annee=calendar.get(Calendar.YEAR);
              int mois=calendar.get(Calendar.MONTH);
              int jour=calendar.get(Calendar.DAY_OF_MONTH);
              DatePickerDialog datePickerDialog=new DatePickerDialog(MainActivity.this,
                      R.style.Theme_AppCompat_Light_Dialog_MinWidth,
                      onDateSetListener,annee,mois,jour);
              datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
              datePickerDialog.show();
          }
      });
      onDateSetListener=new DatePickerDialog.OnDateSetListener() {
          @Override
          public void onDateSet(DatePicker datePicker, int annee, int mois, int jour) {
              String date=jour+"/"+mois+"/"+annee;
              textViewDate.setText(date);
          }
      };
  }

  private void clickButtonHeure()
  {
      // l'horloge pour sélectionner une heure
      buttonHeure.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Calendar calendar=Calendar.getInstance();
              int heure=calendar.get(Calendar.HOUR_OF_DAY);
              int min=calendar.get(Calendar.MINUTE);
              TimePickerDialog time=new TimePickerDialog(MainActivity.this,onTimeSetListener,heure,min,
                      android.text.format.DateFormat.is24HourFormat(MainActivity.this));
              time.show();
          }
      });

      onTimeSetListener=new TimePickerDialog.OnTimeSetListener() {
          @Override
          public void onTimeSet(TimePicker timePicker, int heure, int min) {
              String heure2 = heure + ":"+min;
              textViewHeure.setText(heure2);
              AlarmManager alarmManager= (AlarmManager) MainActivity.this.getSystemService(ALARM_SERVICE);
              Intent intent=new Intent(MainActivity.this,MyReceiver.class);
              PendingIntent waitingIntent= PendingIntent.getBroadcast(MainActivity.this,REQUEST_CODE,intent,PendingIntent.FLAG_UPDATE_CURRENT);
              Calendar calendar = Calendar.getInstance();
              calendar.setTimeInMillis(System.currentTimeMillis());
              calendar.set(Calendar.HOUR_OF_DAY, heure);
              calendar.set(Calendar.MINUTE, min);

              alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, waitingIntent);

          }
      };
  }

  private void clickButtonActiver()
  {
      // demande de notification en faisant appel à la classe NotificationHelper
      buttonActiver.setOnClickListener(new View.OnClickListener() {
          @RequiresApi(api = Build.VERSION_CODES.O)
          @Override
          public void onClick(View view) {
              NotificationHelper notificationHelper = new NotificationHelper(MainActivity.this);
              notificationHelper.notify(1, false, "Date : "+textViewDate.getText().toString()+" Heure : "+textViewHeure.getText().toString(),"Note :"+editTextNote.getText().toString());
          }
      });

  }
}
