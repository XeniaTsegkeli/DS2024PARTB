package com.example.ds2024part2.ui.gallery;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ds2024part2.R;
import com.example.ds2024part2.databinding.FragmentGalleryBinding;

import java.util.Calendar;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    private Calendar checkinCalendar;
    private Calendar checkoutCalendar;
    private Calendar todayCalendar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Inflate the search layout
        View searchLayout = inflater.inflate(R.layout.search_screen, container, false);

        // Add the inflated layout to the FrameLayout container
        ViewGroup containerSearch = root.findViewById(R.id.container_search);
        containerSearch.addView(searchLayout);

        // Initialize Calendars
        checkinCalendar = Calendar.getInstance();
        checkoutCalendar = Calendar.getInstance();
        todayCalendar = Calendar.getInstance();

        // Setup DatePickerDialog for date selection
        EditText etCheckinDate = searchLayout.findViewById(R.id.et_checkin_date);
        EditText etCheckoutDate = searchLayout.findViewById(R.id.et_checkout_date);

        etCheckinDate.setOnClickListener(v -> showCheckinDatePickerDialog(etCheckinDate));
        etCheckoutDate.setOnClickListener(v -> showCheckoutDatePickerDialog(etCheckoutDate));

        return root;
    }

    private void showCheckinDatePickerDialog(final EditText editText) {
        int year = checkinCalendar.get(Calendar.YEAR);
        int month = checkinCalendar.get(Calendar.MONTH);
        int day = checkinCalendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, year1, month1, dayOfMonth) -> {
                    checkinCalendar.set(year1, month1, dayOfMonth);
                    String selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                    editText.setText(selectedDate);

                    // Set the minimum date for checkoutCalendar
                    checkoutCalendar.set(year1, month1, dayOfMonth + 1);
                },
                year, month, day
        );
        // Set minimum date for check-in to today's date
        datePickerDialog.getDatePicker().setMinDate(todayCalendar.getTimeInMillis());
        datePickerDialog.show();
    }

    private void showCheckoutDatePickerDialog(final EditText editText) {
        int year = checkoutCalendar.get(Calendar.YEAR);
        int month = checkoutCalendar.get(Calendar.MONTH);
        int day = checkoutCalendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, year1, month1, dayOfMonth) -> {
                    checkoutCalendar.set(year1, month1, dayOfMonth);
                    String selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                    editText.setText(selectedDate);
                },
                year, month, day
        );

        // Set minimum date for checkout to be after check-in date
        datePickerDialog.getDatePicker().setMinDate(checkinCalendar.getTimeInMillis() + (24 * 60 * 60 * 1000)); // Add one day to check-in date
        datePickerDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}