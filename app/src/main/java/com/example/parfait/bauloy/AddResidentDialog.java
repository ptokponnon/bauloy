package com.example.parfait.bauloy;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.parfait.bauloy.residentList.ResidentListContent;

/**
 * Created by Parfait on 01/05/2017.
 */

public class AddResidentDialog  extends DialogFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    interface AddResidentDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    AddResidentDialogListener mListener;

    public AddResidentDialog() {
        super();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Inscription.
     */
    // TODO: Rename and change types and number of parameters
    public static AddResidentDialog newInstance(String param1, String param2) {
        AddResidentDialog fragment = new AddResidentDialog();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the ChoiceDialogListener so we can send events to the host
            mListener = (AddResidentDialogListener) getActivity();
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getTargetFragment().toString()
                    + " must implement ChoiceDialogListener");
        }
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.resident_add, null);
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.add_new_resident)
                .setView(dialogView)
                .setPositiveButton(R.string.add_resident, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences sharedPref = getActivity().getSharedPreferences(
                                ""+R.string.preference_file_resident, Context.MODE_PRIVATE);
                        int nextKey = ResidentListContent.getSize();
                        SharedPreferences.Editor editor = sharedPref.edit();
                        String name = ((TextView) dialogView.findViewById(R.id.resident_name)).getText().toString();
                        String phone = ((TextView) dialogView.findViewById(R.id.resident_phone)).getText().toString();
                        String birthday = ((TextView) dialogView.findViewById(R.id.resident_birthday)).getText().toString();
                        String value = name +":"+phone+":"+birthday;
                        editor.putString(""+nextKey, value);
                        editor.commit();
                        mListener.onDialogPositiveClick(AddResidentDialog.this);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        mListener.onDialogNegativeClick(AddResidentDialog.this);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
