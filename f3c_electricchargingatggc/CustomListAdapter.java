package com.example.gp189.f3c_electricchargingatggc;


import android.graphics.Color;
import android.widget.ArrayAdapter;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.GREEN;


public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    public String[]timeStamps;
    public  String[]availableChargers;
    public String[] myLocations;
    public  String[] titles;

    public CustomListAdapter(Activity context, String[] timeStamps, String[] availableChargers, String[] myLocations, String[] titles ) {
        super(context, R.layout.mylist, timeStamps);

        this.context = context;
        this.timeStamps = timeStamps;
        this.availableChargers = availableChargers;
        this.myLocations = myLocations;
        this.titles = titles;
    }


    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.mylist, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
        TextView sideTxt = (TextView) rowView.findViewById(R.id.itemSide);
        //ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);
        TextView extraextratxt = (TextView) rowView.findViewById(R.id.textView2);


        txtTitle.setText(titles[position]);



        sideTxt.setTextColor(GREEN);
        sideTxt.setText(availableChargers[position]);

        extratxt.setText(myLocations[position]);
        extraextratxt.setText(timeStamps[position]);



        return rowView;
    }

    ;
}
