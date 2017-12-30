package com.example.parfait.bauloy;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

public class HdjChoiceDialog extends DialogFragment {
    /* The activity that creates an instance of this dialog fragment must
    * implement this interface in order to receive event callbacks.
    * Each method passes the DialogFragment in case the host needs to query it. */
    interface HdjChoiceDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    HdjChoiceDialogListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the HdjChoiceDialogListener so we can send events to the host
            mListener = (HdjChoiceDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getTargetFragment().toString()
                    + " must implement HdjChoiceDialogListener");
        }
        final ArrayList<String> mSelectedItems = new ArrayList();  // Where we track the selected items

        final SharedPreferences sharedPref = getContext().getSharedPreferences(
                ""+R.string.preference_file_resident, Context.MODE_PRIVATE);
        String defaultValue = "defaultResident";
        Map<String, String> residentMap = (Map<String, String>)sharedPref.getAll();
        ArrayList<String> residentList = new ArrayList<>(residentMap.values()),
                residentNameList = new ArrayList<String>();
        for (String resident:residentList) {
            if(resident.split(":").length != 3)
                residentNameList.add("0");
            else
                residentNameList.add(resident.split(":")[0]);
        }
        final String[] residents = residentNameList.toArray(new String[residentNameList.size()]);
        // Set the dialog title
        builder.setTitle(R.string.pick_toppings_hdj)
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(residents, null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    mSelectedItems.add(residents[which]);
                                } else if (mSelectedItems.contains(residents[which])) {
                                    // Else, if the item is already in the array, remove it
                                    mSelectedItems.remove(residents[which]);
                                }
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
                                ""+R.string.preference_file_hdj, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        int minDate = getArguments().getInt(getString(R.string.MIN_DATE));
                        int maxDate = getArguments().getInt(getString(R.string.MAX_DATE));

                        for(int d = minDate; d < maxDate; d++ ){
                            editor.putStringSet(String.valueOf(d), new HashSet<String>(mSelectedItems));
                        }
                        editor.commit();
                        mListener.onDialogPositiveClick(HdjChoiceDialog.this);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick(HdjChoiceDialog.this);
                    }
                })
                .setNeutralButton(R.string.deselect_all, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog
                        Context context = getActivity();
                        SharedPreferences sharedPref = context.getSharedPreferences(
                                "" + R.string.preference_file_hdj, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        int minDate = getArguments().getInt(getString(R.string.MIN_DATE));
                        int maxDate = getArguments().getInt(getString(R.string.MAX_DATE));

                        for (int d = minDate; d < maxDate; d++) {
                            editor.remove(String.valueOf(d));
                        }
                        editor.commit();
                        mListener.onDialogPositiveClick(HdjChoiceDialog.this);
                    }
                });

        return builder.create();
    }
}
