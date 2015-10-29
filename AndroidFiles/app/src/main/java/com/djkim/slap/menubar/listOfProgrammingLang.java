package com.djkim.slap.menubar;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

/**
 * Created by Calvin on 10/28/15.
 */
public class listOfProgrammingLang extends ListActivity {

    //static final String[] langs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Find a way to feed in array langs into listviewLayout
        //setListAdapter(new ArrayAdapter<String>(this, R.layout.programmingLangs, langs));
    }

    private String[] feedString(String[] list) {
        //Iterate through array and add each item to langs
        return null;
    }
}
