package com.example.android_parse_basic_tutorial;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Upload Data---------------------------
        //Score is the table name. if 'Score' does not exist then its create one.
        ParseObject score = new ParseObject("Score");
        score.put("userName","Sakib"); //Column name and value
        score.put("score",45);
        score.saveInBackground(new SaveCallback() { //upload file to the parse server
            //using this call back function will return extra information like if it failed or succeed to upload the file
            @Override
            public void done(ParseException e) {
                if (e==null) { // no error occurred
                    Log.i("MainActivity","Succeed");
                } else {
                    e.getMessage();
                    e.getStackTrace();
                }
            }
        });

        //Fetch Data---------------------------
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Score");
        query.getInBackground("", new GetCallback<ParseObject>() { //objectId is the primary key
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e==null && object != null) {
                    object.get("userName"); //or
                    object.getString("userName");
                }
            }
        });



        //Update Data----------------------------

        //get the whole row of the data
        ParseQuery<ParseObject> queryU  = new ParseQuery<ParseObject>("Table name");
        queryU.getInBackground("objectId", new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    object.put("Column name","Value");
                    object.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e==null){
                                //update success
                            } else {
                                e.getStackTrace();
                            }
                        }
                    });
                } else {

                }
            }
        });

        //Delete a Row--------------------------
        ParseQuery<ParseObject> queryD = ParseQuery.getQuery("Table Name");
        queryD.getInBackground("objectId", new GetCallback<ParseObject>() { //objectId is the primary key
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e==null && object != null) {
                    object.deleteInBackground(new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e==null){
                               //delete success
                            } else {
                               e.getMessage();
                            }
                        }
                    });
                }
            }
        });


        //Advance query-----------------------------
        //SELECT * FROM 'TABLE' WHERE 'COLUMN' = 'VALUE' LIMIT 1 ; --implementation
        ParseQuery<ParseObject> query1  = new ParseQuery<ParseObject>("Table name");
        query1.whereEqualTo("column name"," value");
        query1.whereEqualTo("note","some note");
        query1.whereGreaterThan("","");
        query1.setLimit(1);
        query1.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e==null){
                    if (objects.size()>0){
                        String objectId = "";
                        for (ParseObject object:objects){
                            objectId = object.getObjectId();
                        }
                    }
                }

            }
        });

        //Signup parse user ------------------------------
        ParseUser user = new ParseUser();
        user.setUsername(name.getText().toString());
        user.setPassword(password.getText().toString());
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e==null){
                    Toast.makeText(getApplicationContext(),"Sign up success", Toast.LENGTH_SHORT).show();
                    startActivity();
                } else {
                    Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });



        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }
}