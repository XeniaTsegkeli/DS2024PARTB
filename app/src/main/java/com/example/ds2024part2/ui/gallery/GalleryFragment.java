package com.example.ds2024part2.ui.gallery;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ds2024part2.NumberPickerDialogFragment;
import com.example.ds2024part2.PropertyAdapter;
import com.example.ds2024part2.R;
import com.example.ds2024part2.TcpClientCallback;
import com.example.ds2024part2.databinding.FragmentGalleryBinding;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import com.example.ds2024part2.TcpClient;
import model.Filters;
import model.Property;


public class GalleryFragment extends Fragment implements TcpClientCallback {

    private FragmentGalleryBinding binding;
    private Calendar checkinCalendar;
    private Calendar checkoutCalendar;
    private Calendar todayCalendar;
    private View searchLayout;
    private RecyclerView recyclerView;
    private PropertyAdapter propertyAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel = new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Inflate the search layout
        searchLayout = inflater.inflate(R.layout.search_screen, container, false);

        // Add the inflated layout to the FrameLayout container
        ViewGroup containerSearch = root.findViewById(R.id.container_search);
        containerSearch.addView(searchLayout);

        // Initialize RecyclerView
        recyclerView = root.findViewById(R.id.recycler_view);
        propertyAdapter = new PropertyAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(propertyAdapter);

        // Initialize Calendars
        checkinCalendar = Calendar.getInstance();
        checkoutCalendar = Calendar.getInstance();
        todayCalendar = Calendar.getInstance();

        // Setup DatePickerDialog for date selection
        EditText etCheckinDate = searchLayout.findViewById(R.id.et_checkin_date);
        EditText etCheckoutDate = searchLayout.findViewById(R.id.et_checkout_date);

        etCheckinDate.setOnClickListener(v -> showCheckinDatePickerDialog(etCheckinDate));
        etCheckoutDate.setOnClickListener(v -> showCheckoutDatePickerDialog(etCheckoutDate));

        // Setup NumberPickerDialog for review ratings
        EditText etReviews = searchLayout.findViewById(R.id.et_reviews);
        etReviews.setOnClickListener(v -> showNumberPickerDialog(etReviews));

        // Setup search button
        Button searchButton = searchLayout.findViewById(R.id.btn_search);
        searchButton.setOnClickListener(v -> {
            recyclerView.setVisibility(View.GONE); // Hide RecyclerView when a new search is initiated
            collectInputAndSendAsJson();
        });

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

    private void showNumberPickerDialog(final EditText editText) {
        FragmentManager fragmentManager = getParentFragmentManager();
        NumberPickerDialogFragment dialogFragment = new NumberPickerDialogFragment();
        dialogFragment.setOnNumberPickerValueSelectedListener(value -> {
            DecimalFormat decimalFormat = new DecimalFormat("0.0");
            String formattedValue = decimalFormat.format(value);
            editText.setText(formattedValue);
        });
        dialogFragment.show(fragmentManager, "numberPicker");
    }

    private void collectInputAndSendAsJson() {
        String uuid = UUID.randomUUID().toString();
        System.out.println("Start collecting input");
        try {
            // Get the input values from EditText fields
            EditText etArea = searchLayout.findViewById(R.id.et_area);
            EditText etCheckinDate = searchLayout.findViewById(R.id.et_checkin_date);
            EditText etCheckoutDate = searchLayout.findViewById(R.id.et_checkout_date);
            EditText etReviews = searchLayout.findViewById(R.id.et_reviews);
            EditText etPrice = searchLayout.findViewById(R.id.et_price);
            EditText etNumPeople = searchLayout.findViewById(R.id.et_people);
            Spinner spinnerStars = searchLayout.findViewById(R.id.spinner_stars);

            String area = etArea.getText().toString().trim();
            String checkinDate = etCheckinDate.getText().toString().trim();
            String checkoutDate = etCheckoutDate.getText().toString().trim();
            double reviews = Double.parseDouble(etReviews.getText().toString().trim());
            double price = Double.parseDouble(etPrice.getText().toString().trim());
            int numPeople = Integer.parseInt(etNumPeople.getText().toString().trim());
            int stars = Integer.parseInt(spinnerStars.getSelectedItem().toString().trim());

            String dateRange = checkinDate + " - " + checkoutDate;
            Filters filters = new Filters(area, dateRange, numPeople, price, stars, uuid);

            // Log the filters for debugging
            Log.d("GalleryFragment", "Filters: " + filters.toString());

            // Create and send TCP client request
            TcpClient tcpClient = new TcpClient(this);
            tcpClient.sendJsonOverTcp(filters, uuid, 2);

        } catch (NumberFormatException e) {
            e.printStackTrace();
            Log.e("GalleryFragment", "Invalid input format: " + e.getMessage());
        }
    }

    @Override
    public void onPropertiesReceived(List<Property> properties) {
        Log.d("GalleryFragment", "Received properties: " + properties.toString());
        getActivity().runOnUiThread(() -> {
            propertyAdapter.setProperties(properties);
            recyclerView.setVisibility(View.VISIBLE); // Show RecyclerView when properties are received
            searchLayout.setVisibility(View.GONE); // Hide search layout when properties are displayed
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}