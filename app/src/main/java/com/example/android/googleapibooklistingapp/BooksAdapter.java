package com.example.android.googleapibooklistingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by gurjot on 6/8/17.
 */

public class BooksAdapter extends ArrayAdapter<Books>{
    public BooksAdapter(Context context, ArrayList<Books> arr)
    {
        super(context,0,arr);
    }
    @NonNull
    @Override
    public View getView(int Position, View conView, ViewGroup Parent) {
        View Listview = conView;
        //inflating the list view
        if (Listview == null) {
            Listview = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, Parent, false);
        }
        //creating an instance of the current object in the arraylist of Books class
        Books curobj = (Books) getItem(Position);

        //updating text of the title Textview field
        TextView t1 = (TextView) Listview.findViewById(R.id.titlebar);
        t1.setText(curobj.getTitle());

        //updating text of the author name textview field
        TextView t2 = (TextView) Listview.findViewById(R.id.authorname);
        t2.setText(curobj.getAuthor_name());

        return Listview;
    }
}
