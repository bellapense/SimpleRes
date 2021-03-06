package com.example.simpleres;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class PastDatePopup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_date_popup);

        //setting the size of the pop-up window
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*0.5), (int)(height*0.5));

        //fields
        final TextView displayDate = findViewById(R.id.displayPastDate);
        final TextView displayCovers = findViewById(R.id.displayCoversCompleted);
        final ImageButton closePopup = findViewById(R.id.closePastPopup);
        final CoverDatabaseHelper cdb = new CoverDatabaseHelper(this);
        Cover selectedCover = new Cover();
        final String dateSelected = getIntent().getStringExtra("DATE_SELECTED");

        try {
            selectedCover = cdb.getCover(dateSelected);
        } catch(Exception e) {
            System.out.println("error getting Cover info from database");
            System.out.println("adding Cover in nested try/catch block");
            try {
                selectedCover = new Cover(0,dateSelected);
                cdb.addCover(selectedCover);
            }
            catch(Exception x) {
                System.out.println("error adding Cover to database");
            }
        }

        //use the dateSelected to lookup that date in the DB
        String coversCompleted = selectedCover.getDailyCover()+"";
        if (coversCompleted.compareTo("0") == 0)
            displayCovers.setText("N/A");
        else
            displayCovers.setText(coversCompleted);

        //reformat dateSelected to display for example November 14, 2019 for 2019-11-14
        assert dateSelected != null;
        String month = FutureDatePopup.getMonthString(dateSelected);
        String day = FutureDatePopup.getDayString(dateSelected);
        String year = FutureDatePopup.getYearString(dateSelected);

        //display formatted date
        String formattedDate = month + " " + day + ", " + year;
        displayDate.setText(formattedDate);

        //used to end the activity
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId() == R.id.closePastPopup){
                    finish();
                }
            }
        };

        closePopup.setOnClickListener(listener);
    }
}
