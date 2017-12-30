package com.example.parfait.bauloy;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.parfait.bauloy.caldroidCustom.CaldroidSampleCustomFragment;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Homme_dj.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Homme_dj#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Homme_dj extends Fragment  implements HdjChoiceDialog.HdjChoiceDialogListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    static final CaldroidFragment caldroidFragment = new CaldroidSampleCustomFragment();
    Date firstSelected, minDate, maxDate;
    static Calendar cal = Calendar.getInstance();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Homme_dj() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Homme_dj.
     */
    // TODO: Rename and change types and number of parameters
    public static Homme_dj newInstance(String param1, String param2) {
        Homme_dj fragment = new Homme_dj();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);

        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        args.putInt(CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.MONDAY); // Tuesday
        args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, false);
        args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, false);
        caldroidFragment.setArguments(args);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        } else {
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            args.putInt(CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.MONDAY); // Tuesday
            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, false);
            args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, false);
            caldroidFragment.setArguments(args);
        }
       // if(savedInstanceState != null)
         //   caldroidFragment.restoreStatesFromKey(savedInstanceState, "CALDROID_SAVED_STATE");
        // To set the extraData : prefFile
        HashMap<String, Object> extraData = (HashMap<String, Object>) caldroidFragment.getExtraData();
        extraData.put("prefFile", R.string.preference_file_hdj);

        caldroidFragment.setMinDate(cal.getTime());
        final CaldroidListener listener = new CaldroidListener() {
            int times = 0;

            @Override
            public void onSelectDate(Date date, View view) {
                if (times == 0) {
                    unselect(minDate, maxDate);
                    select(date, date);
                    minDate = maxDate = firstSelected = date;
                    times++;
                } else if (times == 1) {
                    if (date.before(firstSelected)) {
                        minDate = date;
                        maxDate = firstSelected;
                    } else {
                        minDate = firstSelected;
                        maxDate = date;
                    }
                    select(minDate, maxDate);
                    displayChoiceDialog(minDate, maxDate);
                    times = 0;
                }
            }

            @Override
            public void onChangeMonth(int month, int year) {

            }

            @Override
            public void onLongClickDate(Date date, View view) {
                unselect(minDate, maxDate);
                select(date, date);
                minDate = maxDate = date;
                displayChoiceDialog(date, date);
                times = 0;
            }

            @Override
            public void onCaldroidViewCreated() {

            }
        };
        caldroidFragment.setCaldroidListener(listener);
        FragmentTransaction t = getFragmentManager().beginTransaction();
        t.replace(R.id.caldroid_container1, caldroidFragment);
        t.commit();
        setHasOptionsMenu(true);
    }

    public void displayChoiceDialog(Date begin, Date end){
        android.support.v4.app.DialogFragment dialogFragment = new HdjChoiceDialog();
        Bundle args = new Bundle();
        Calendar calMin = Calendar.getInstance();
        Calendar calMax = Calendar.getInstance();
        calMin.setTime(begin);
        calMax.setTime(end);
        calMax.add(Calendar.DATE, 1);
        args.putInt(getString(R.string.MIN_DATE), calMin.get(Calendar.DAY_OF_YEAR));
        args.putInt(getString(R.string.MAX_DATE), calMax.get(Calendar.DAY_OF_YEAR));
        dialogFragment.setArguments(args);
        dialogFragment.setTargetFragment(this, 0);
        dialogFragment.show(getFragmentManager(), "Homme du Jour");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.homme_dj, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // User touched the dialog's positive button
        unselect(minDate, maxDate);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
        unselect(minDate, maxDate);
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public  void unselect(Date begin, Date end){
        if(begin == null || end == null){
            return;
        }
        Calendar minCal = Calendar.getInstance();
        Calendar maxCal = Calendar.getInstance();
        minCal.setTime(begin);
        maxCal.setTime(end);
        maxCal.add(Calendar.DATE, 1);
        for (; minCal.before(maxCal); minCal.add(Calendar.DATE, 1)) {
            caldroidFragment.clearBackgroundDrawableForDate(minCal.getTime());
        }
        caldroidFragment.refreshView();
    }

    public  void select(Date begin, Date end){
        if(begin == null || end == null){
            return;
        }
        ColorDrawable green = new ColorDrawable(Color.GREEN);
        Calendar minCal = Calendar.getInstance();
        Calendar maxCal = Calendar.getInstance();
        minCal.setTime(begin);
        maxCal.setTime(end);
        maxCal.add(Calendar.DATE, 1);
        for (; minCal.before(maxCal); minCal.add(Calendar.DATE, 1)) {
            caldroidFragment.setBackgroundDrawableForDate(green, minCal.getTime());
        }
        caldroidFragment.refreshView();
    }
}
