package com.example.android.googleapibooklistingapp;

/**
 * Created by gurjot on 6/8/17.
 */

public class Books {
    //string to store title and author name of the books
    String title;
    String author_name;
    //constructor
    public Books(String book_title,String author_name)
    {
        this.title=book_title;
        this.author_name=author_name;
    }
    //getter method for title name
    public String getTitle()
    {
        return title;
    }
    //getter method for author name
    public String getAuthor_name()
    {
        return author_name;
    }
}
