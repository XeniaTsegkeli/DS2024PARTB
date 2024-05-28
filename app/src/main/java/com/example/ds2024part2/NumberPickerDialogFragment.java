package com.example.ds2024part2;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.text.DecimalFormat;

public class NumberPickerDialogFragment extends DialogFragment {

    public interface OnNumberPickerValueSelectedListener {
        void onValueSelected(double value);
    }

    private OnNumberPickerValueSelectedListener listener;

    public void setOnNumberPickerValueSelectedListener(OnNumberPickerValueSelectedListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_number_picker, container, false);

        NumberPicker numberPicker = view.findViewById(R.id.number_picker);
        numberPicker.setMinValue(0); // Representing 0.0
        numberPicker.setMaxValue(20); // Representing 10.0
        numberPicker.setDisplayedValues(null); // Clear any displayed values
        numberPicker.setFormatter(value -> String.valueOf(value / 2.0)); // Display values as 0, 0.5, 1, 1.5, ...
        numberPicker.setValue(0); // Default value representing 0.0

        Button buttonOk = view.findViewById(R.id.button_ok);
        buttonOk.setOnClickListener(v -> {
            if (listener != null) {
                double selectedValue = numberPicker.getValue() / 2.0;
                listener.onValueSelected(selectedValue);
            }
            dismiss();
        });

        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(requireContext());
    }
}
