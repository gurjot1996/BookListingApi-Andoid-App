package com.example.android.googleapibooklistingapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

public class BooksMainActivity extends AppCompatActivity {
    private static final String TAG=BooksMainActivity.class.getName();

    //google api general format query for searching book
    private static String Google_Api="https://www.googleapis.com/books/v1/volumes?q=";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.books_main);
        final ListView listviewobj=(ListView)findViewById(R.id.list);

        //connection check on startup of application
        ConnectivityManager cm =
                (ConnectivityManager)BooksMainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if(!(activeNetwork!=null && activeNetwork.isConnected())) {
            listviewobj.setVisibility(View.INVISIBLE);
            TextView t1=(TextView)findViewById(R.id.empty);
            t1.setText("NO INTERNET CONNECTION");
            Toast.makeText(BooksMainActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
            return;
        }
        listviewobj.setEmptyView(findViewById(R.id.empty));

        //fetching reference to searh button
        Button b1=(Button)findViewById(R.id.searchbooks);

        //setting onclicklistener for the search button
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //checking if connection has been established when search button is clicked
                ConnectivityManager cm =
                        (ConnectivityManager) BooksMainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                final NetworkInfo activeNet = cm.getActiveNetworkInfo();
                if (!(activeNet != null && activeNet.isConnected())) {
                    listviewobj.setVisibility(View.INVISIBLE);
                    TextView t = (TextView) findViewById(R.id.empty);
                    t.setText("NO INTERNET CONNECTION");
                    Toast.makeText(BooksMainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    ProgressBar bar = (ProgressBar) findViewById(R.id.indeterminateBar);
                    bar.setVisibility(ProgressBar.VISIBLE);

                    //fetching reference to Edittext field
                    EditText o1 = (EditText) findViewById(R.id.editText);

                    //fetching user input
                    String query = o1.getText().toString();

                    //if user has not entered any query
                    if (query.isEmpty()) {
                        ListView obj = (ListView) findViewById(R.id.list);
                        obj.setVisibility(View.INVISIBLE);
                        bar.setVisibility(View.INVISIBLE);
                        return;
                    }

                    //replacing blank spaces if any with '+' to qualify the url criteria
                    query = query.replaceAll(" ", "+");

                    BooksAsynctask task = new BooksAsynctask();
                    String Booksquery = Google_Api + query;

                    //task is executed on a background thread
                    task.execute(Booksquery);
                }
            }
        });
    }

    //BooksAsynctask is a subclass of the Asynctask class which performs some functions on the background thread
    private class BooksAsynctask extends AsyncTask<String ,Void, ArrayList<Books>>
    {
        //this task will be performed in background and the results i.e an ArrayList will be returned as a parameter to OnpostExecute method
        @Override
        protected ArrayList<Books> doInBackground(String... params) {

            ArrayList<Books> a1 = BooksNetworkClass.BooksQuery(params[0]);
            return a1;
        }

        @Override
        protected void onPostExecute(ArrayList<Books> books) {
            //ArrayList returned from doInBackground is used to update the ListView
            //creating an object of the custom BooksAdapter class
            //context of current activity and arraylist to be used to update the list view are passed as arguments to the
            //custom contructor of the BooksAdapter class
            ProgressBar bar=(ProgressBar)findViewById(R.id.indeterminateBar);

            bar.setVisibility(ProgressBar.INVISIBLE);

            ListView l1=(ListView)findViewById(R.id.list);
            BooksAdapter booksAdapter=new BooksAdapter(BooksMainActivity.this,books);
            //adapter is set for the list view this calls the getView method of the BooksAdapter
            l1.setAdapter(booksAdapter);
        }
    }
}
