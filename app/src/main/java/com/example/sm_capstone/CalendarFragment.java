package com.example.sm_capstone;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */



public class CalendarFragment extends Fragment {
    private static final String TAG = CalendarFragment.class.getSimpleName();

    private CalendarAdapter adapter;
    private TextView monthText;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CalendarFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalendarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

//        setContentView(R.layout.fragment_calendar);

//        adapter = new CalendarAdapter(this);
////        GridView monthView = this.findViewById(R.id.monthView);
//        GridView monthView = getView().findViewById(R.id.monthView);
//        monthView.setAdapter(adapter);
//        monthView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                MonthItem selectedItem = (MonthItem) adapter.getItem(position);
//                int day = selectedItem.getDay();
//                Log.d(TAG, String.format("Selected Day: %s", day));
//
//                adapter.setSelectedPosition(position);
//                adapter.notifyDataSetChanged();
//            }
//        });
//
////        monthText = this.findViewById(R.id.monthText);
//        monthText = getView().findViewById(R.id.monthText);
//        this.setMonthText();
//
////        Button monthPrevious = this.findViewById(R.id.monthPrevious);
//        Button monthPrevious = getView().findViewById(R.id.monthPrevious);
//        monthPrevious.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                adapter.setPreviousMonth();
//                adapter.notifyDataSetChanged();
//                setMonthText();
//            }
//        });
//
////        Button monthNext = this.findViewById(R.id.monthNext);
//        Button monthNext = getView().findViewById(R.id.monthNext);
//        monthNext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                adapter.setNextMonth();
//                adapter.notifyDataSetChanged();
//                setMonthText();
//            }
//        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

//        adapter = new CalendarAdapter(this);
        adapter = new CalendarAdapter();
//        GridView monthView = this.findViewById(R.id.monthView);
        GridView monthView = getView().findViewById(R.id.monthView);
        monthView.setAdapter(adapter);
        monthView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                MonthItem selectedItem = (MonthItem) adapter.getItem(position);
                int day = selectedItem.getDay();
                Log.d(TAG, String.format("Selected Day: %s", day));

                adapter.setSelectedPosition(position);
                adapter.notifyDataSetChanged();
            }
        });

//        monthText = this.findViewById(R.id.monthText);
        monthText = getView().findViewById(R.id.monthText);
        this.setMonthText();

//        Button monthPrevious = this.findViewById(R.id.monthPrevious);
        Button monthPrevious = getView().findViewById(R.id.monthPrevious);
        monthPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.setPreviousMonth();
                adapter.notifyDataSetChanged();
                setMonthText();
            }
        });

//        Button monthNext = this.findViewById(R.id.monthNext);
        Button monthNext = getView().findViewById(R.id.monthNext);
        monthNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.setNextMonth();
                adapter.notifyDataSetChanged();
                setMonthText();
            }
        });
        return view;
    }

    ////////////////////////// .................
    private void setMonthText(){
        int year = adapter.getYear();
        int month = adapter.getMonth();
        monthText.setText(String.format("%s-%s", year, month+1));
    }
    ///////////////////////////..................
}