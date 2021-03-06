package com.example.simpleres;

import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;

public class PopupCreateReservation extends MainInterface implements AdapterView.OnItemSelectedListener , DatePickerDialog.OnDateSetListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_create_reservation);

        //setting the size of the pop-up window
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*0.8), (int)(height*0.8));

        //button to close the window, create reservation
        final ImageButton exitCreateRes = findViewById(R.id.exitCreateRes);
        final Button createRes = findViewById(R.id.create_res_button);

        //button to select a reservation date
        final Button selectDate = findViewById(R.id.select_date);
        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerActivity();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        //spinner for reservation times
        Spinner time_spinner = findViewById(R.id.reservation_times);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.reservation_times,
                R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        time_spinner.setAdapter(adapter);
        time_spinner.setOnItemSelectedListener(this);

        View.OnClickListener listener = new View.OnClickListener() {
            //method for which actions are taken when a button is clicked
            @Override
            public void onClick(View view) {
                //used for input validation toasts
                boolean showDateToast = true;
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;
                int yOffset = 80;

                switch(view.getId()){
                    case R.id.exitCreateRes:
                        finish();
                        break;
                    case R.id.create_res_button:
                        try{
                            final EditText nameField = findViewById(R.id.enter_name);
                            final EditText sizeField = findViewById(R.id.enter_party_size);
                            final EditText phoneField = findViewById(R.id.enter_number);

                            if (nameField.getText().toString().equals("")) {
                                Toast toast = Toast.makeText(context, "Please enter party name!", duration);
                                toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, yOffset);
                                toast.show();

                                showDateToast = false;
                                throw new IllegalArgumentException("Cannot have name fields blank!");
                            }  else if (phoneField.getText().toString().length() != 10){
                                Toast toast = Toast.makeText(context, "Please enter valid phone number!", duration);
                                toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, yOffset);
                                toast.show();

                                showDateToast = false;
                                throw new IllegalArgumentException("Cannot have incomplete phone number!");

                            } else if(sizeField.getText().toString().equals("")){
                                Toast toast = Toast.makeText(context, "Please enter party size!", duration);
                                toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, yOffset);
                                toast.show();

                                showDateToast = false;
                                throw new IllegalArgumentException("Cannot have party size fields blank!");
                            }

                            //get date
                            final TextView reservationDate = findViewById(R.id.display_date);
                            String displayedDate = reservationDate.getText().toString();
                            System.out.println("date stored as: "+displayedDate);

                            String[] dateValues = displayedDate.split("/");
                            int month = Integer.parseInt(dateValues[0]);
                            int day = Integer.parseInt(dateValues[1]);
                            int year = Integer.parseInt(dateValues[2]);

                            //get time
                            final Spinner reservationTime = findViewById(R.id.reservation_times);
                            String time = reservationTime.getSelectedItem().toString().replaceAll("pm","");
                            String [] timeValues = time.split(":");

                            int hour = Integer.parseInt(timeValues[0])+12;//adding 12 because dinner only
                            int minute = Integer.parseInt(timeValues[1]);

                            LocalDate  localDate = LocalDate.of(year,month,day);
                            LocalTime localTime = LocalTime.of(hour,minute,0);
                            LocalDateTime localDateTime = LocalDateTime.of(localDate,localTime);

                            String dateTime = WaitlistEntry.formatDate(localDateTime);

                            String name = nameField.getText().toString();
                            String phone = phoneField.getText().toString();
                            int size = Integer.parseInt(sizeField.getText().toString());

                            final EditText notesField = findViewById(R.id.enter_res_notes);
                            String notes = notesField.getText().toString();

                            System.out.println("Creating entry with parameters (name="+name+",phone="+phone+",size="+size+",dateTime="+dateTime+",localDate="+localDateTime.toString()+")");
                            returnWaitlistEntry(name,phone,size,dateTime,localDateTime,notes);
                        }
                        catch(IllegalArgumentException x){
                            if(showDateToast) {
                                Toast toast = Toast.makeText(context, "Please select a date!", duration);
                                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, yOffset);
                                toast.show();
                            }
                            System.out.println("IllegalArgument encountered");
                            break;
                        }
                        catch(Exception e){
                            System.out.println("Exception encountered");
                        }

                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("result",1);
                        setResult(RESULT_OK,returnIntent);
                        finish();
                }
            }
        };

        exitCreateRes.setOnClickListener(listener);
        createRes.setOnClickListener(listener);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        month = month + 1;

        TextView mDisplayDate = findViewById(R.id.display_date);

        String date = month + "/" + dayOfMonth + "/" + year;
        mDisplayDate.setText(date);
    }

    private void returnWaitlistEntry(String Name, String Telephone, int NumberOfPeople, String FormattedDateTime, LocalDateTime DateTime, String ReservationNotes){
        WaitlistDatabaseHelper wdb = new WaitlistDatabaseHelper(this);
        WaitlistEntry entry = new WaitlistEntry(Name,Telephone,NumberOfPeople, DateTime,1,ReservationNotes);
        wdb.addWaitlistEntry(entry);
        entry.createId(wdb);
        System.out.println("Waitlist Entry created in database with id:" + entry.getId());
    }
}



