package com.example.ds2024part2.ui.rate;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ds2024part2.R;
import com.example.ds2024part2.TcpClient;
import com.example.ds2024part2.TcpClientCallback;
import com.example.ds2024part2.databinding.FragmentRateBinding;

import java.util.List;
import java.util.UUID;

import model.Property;
import model.RoomRating;

public class RateFragment extends Fragment implements TcpClientCallback{

    private FragmentRateBinding binding;
    private EditText editTextAccommodationId;
    private EditText editTextRating;
    private Button buttonSubmitRating;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rate, container, false);

        editTextAccommodationId = view.findViewById(R.id.editTextAccommodationId);
        editTextRating = view.findViewById(R.id.editTextRating);
        buttonSubmitRating = view.findViewById(R.id.buttonSubmitRating);

        buttonSubmitRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uuid = UUID.randomUUID().toString();
                rateAccommodation(uuid);
            }
        });

        return view;
    }

    private void rateAccommodation(String uuid) {
        String accommodationIdStr = editTextAccommodationId.getText().toString().trim();
        String ratingStr = editTextRating.getText().toString().trim();

        if (TextUtils.isEmpty(accommodationIdStr) || TextUtils.isEmpty(ratingStr)) {
            Toast.makeText(getActivity(), "Please enter both Accommodation ID and Rating", Toast.LENGTH_SHORT).show();
            return;
        }

        final int rating = Integer.parseInt(ratingStr);

        RoomRating roomRating = new RoomRating();
        roomRating.setRoomName(accommodationIdStr);
        roomRating.setRating(rating);

        // Create and send TCP client request
        TcpClient tcpClient = new TcpClient(this);
        tcpClient.sendAccomodationRating(roomRating, uuid, 3);

        Toast.makeText(getActivity(), "Rating submitted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onPropertiesReceived(List<Property> properties) {

    }

    @Override
    public void onRatingResponseReceived(final String response) {
        // Show response in a dialog
        if (getActivity() == null) return;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Rating Response")
                        .setMessage(response)
                        .setPositiveButton("OK", null)
                        .show();
            }
        });
    }
}