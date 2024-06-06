package com.example.ds2024part2;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import model.Booking;

public class BookingActivity extends AppCompatActivity {

    private EditText etCheckinDate, etCheckoutDate, etRenterName, etPropertyName;
    private Button btnBook;
    private Calendar checkinCalendar, checkoutCalendar;
    private String propertyName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        etCheckinDate = findViewById(R.id.et_checkin_date);
        etCheckoutDate = findViewById(R.id.et_checkout_date);
        etRenterName = findViewById(R.id.et_renter_name);
        etPropertyName = findViewById(R.id.et_property_name);
        btnBook = findViewById(R.id.btn_book);

        propertyName = getIntent().getStringExtra("PROPERTY_NAME");
        etPropertyName.setText(propertyName);

        checkinCalendar = Calendar.getInstance();
        checkoutCalendar = Calendar.getInstance();

        etCheckinDate.setOnClickListener(v -> showDatePickerDialog(etCheckinDate, checkinCalendar));
        etCheckoutDate.setOnClickListener(v -> showDatePickerDialog(etCheckoutDate, checkoutCalendar));

        btnBook.setOnClickListener(v -> bookAccommodation());
    }

    private void showDatePickerDialog(EditText editText, Calendar calendar) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                    editText.setText(selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void bookAccommodation() {
        String renterName = etRenterName.getText().toString().trim();
        String checkinDate = etCheckinDate.getText().toString().trim();
        String checkoutDate = etCheckoutDate.getText().toString().trim();

        if (renterName.isEmpty() || checkinDate.isEmpty() || checkoutDate.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Booking booking = new Booking();
        booking.setRenterName(renterName);
        booking.setRoomName(propertyName);
        String dates = checkinDate + " - " + checkoutDate;
        booking.setDates(dates);

        // TODO: Send booking to server
    }
}
