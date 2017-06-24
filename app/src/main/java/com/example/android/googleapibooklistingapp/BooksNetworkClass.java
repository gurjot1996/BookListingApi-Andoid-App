package com.example.android.googleapibooklistingapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by gurjot on 6/8/17.
 */

public class BooksNetworkClass {
    private static final String TAG="BooksNetworkClass";
    //firstly a string is received
    //converted to an url
    //http connection is established
    //jsonresponse is fetched using inputstream,inputstreamReader,BufferedReader
    //from this json response arraylist is populated
    //this arraylist is returned as result which is used to update the ui
    public static ArrayList<Books> BooksQuery(String BOOKS_URL)
    {
        String jsonresp=null;
        //invalid string case
        if(BOOKS_URL==null){return null;}
        //creating a valid url from a string
        URL url=create(BOOKS_URL);
        try{
            //establishing a http connection and fetching the json response
            jsonresp=httprequest(url);
        }catch(IOException e)
        {
            Log.e(TAG,"error parsing json",e);
        }
        //fetching an arraylist based on the jsonresponse received
        ArrayList<Books> arr=getBooksData(jsonresp);
        return arr;
    }

    private static URL create(String BOOKS_URL)
    {
        //creating a new url object
        URL generatedurl=null;
        try{
            if(BOOKS_URL!=null)
            {
                generatedurl=new URL(BOOKS_URL);
            }
        }catch(MalformedURLException e)
        {
            Log.e(TAG,"url exception",e);
        }
        return generatedurl;
    }

    private static String httprequest(URL books_url) throws IOException
    {
        String json=null;
        //invalid url case
        if (books_url == null) {
            return null;
        }
        HttpURLConnection URLConnection=null;
        InputStream inputStream=null;
        try{
            //establishing a http connection
            URLConnection=(HttpURLConnection) books_url.openConnection();
            URLConnection.setReadTimeout(15000);
            URLConnection.setConnectTimeout(15000);
            URLConnection.connect();
            //response code is checked to be valid or not
            //200-valid
            //300-page moved permanently
            //400-page not found
            //500-internal server error
            if(URLConnection.getResponseCode()==200)
            {
                //receiving input stream if connection is valid
                inputStream=URLConnection.getInputStream();
                //fetching json response converting binary to readable-text
                json= jsonResponse(inputStream);
            }

        }catch(IOException e)
        {
            Log.e(TAG,"error in http connection");
        }
        return json;
    }

    private static String jsonResponse(InputStream istream)
    {
     //string builder can be manipulated easily
        StringBuilder out=new StringBuilder();
        try
        {
            if(istream!=null)
            {
                InputStreamReader inputStreamReader=new InputStreamReader(istream, Charset.forName("UTF-8"));
                BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
                String first=bufferedReader.readLine();
                //reading and appending line by line
                while(first!=null)
                {
                    out.append(first);
                    first=bufferedReader.readLine();
                }
            }
        }
        catch(IOException e)
        {
            Log.e(TAG,"error",e);
        }
        return out.toString();
    }

    private static ArrayList<Books> getBooksData(String jsonresponse)
    {
        ArrayList<Books> arrayList=new ArrayList<Books>();
        try {
            //reading title and array of author names from json data string
            JSONObject root = new JSONObject(jsonresponse);
            JSONArray jsonArray=root.getJSONArray("items");
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject currobj=jsonArray.getJSONObject(i);
                JSONObject volinfo=currobj.getJSONObject("volumeInfo");
                String title=volinfo.getString("title");
                JSONArray authors=volinfo.getJSONArray("authors");
                if(authors.length()==0){
                    throw new JSONException("Authors array is empty");
                }
                StringBuilder s1=new StringBuilder();
                for(int j=0;j<authors.length();j++)
                {
                    String s2=authors.getString(j);
                    s1.append(s2+"\n");
                }
                String authorname=s1.toString();
                arrayList.add(new Books(title,authorname));
            }
        }catch(JSONException e)
        {
            Log.e(TAG,"error exception",e);
        }
        //returning arraylist of fetched results
        return arrayList;
    }
}
