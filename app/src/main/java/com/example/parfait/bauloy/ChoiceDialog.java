package com.example.parfait.bauloy;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ChoiceDialog extends DialogFragment {
    /* The activity that creates an instance of this dialog fragment must
    * implement this interface in order to receive event callbacks.
    * Each method passes the DialogFragment in case the host needs to query it. */
    interface ChoiceDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    ChoiceDialogListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        final Integer[] SelectedItem = {0};  // Where we track the selected items
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the ChoiceDialogListener so we can send events to the host
            mListener = (ChoiceDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getTargetFragment().toString()
                    + " must implement ChoiceDialogListener");
        }
        // Set the dialog title
        builder.setTitle(R.string.pick_toppings)
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setSingleChoiceItems(R.array.toppings, 0,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SelectedItem[0] = which;
                            }
                        })
                // Set the action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog
                        Context context = getActivity();
                        SharedPreferences sharedPref = context.getSharedPreferences(
                                ""+R.string.preference_file_notation, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        int minDate = getArguments().getInt(getString(R.string.MIN_DATE));
                        int maxDate = getArguments().getInt(getString(R.string.MAX_DATE));
                        String choice ="";
                        switch (SelectedItem[0]){
                            case 0:
                                choice = "";
                                break;
                            case 1:
                                choice = "D";
                                break;
                            case 2:
                                choice = "S";
                                break;
                            case 3:
                                choice = "D+S";
                                break;
                        }
                        for(int d = minDate; d < maxDate; d++ ){
                            editor.putString(String.valueOf(d), choice);
                        }
                        editor.commit();
                        mListener.onDialogPositiveClick(ChoiceDialog.this);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick(ChoiceDialog.this);
                    }
                });

        return builder.create();
    }
}
