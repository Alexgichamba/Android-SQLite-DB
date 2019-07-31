package com.alex.mysqlitedbapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText mFname, mLname, mID;
    Button mSave, mView, mDelete;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFname = findViewById(R.id.edtFName);
        mLname = findViewById(R.id.edtLName);
        mID = findViewById(R.id.edtIdNo);
        mSave = findViewById(R.id.btnSave);
        mView = findViewById(R.id.btn_view);
        mDelete = findViewById(R.id.btn_delete);

        //Create the database
        db = openOrCreateDatabase("huduma",MODE_PRIVATE, null);

        //Create a table in your database
        db.execSQL("CREATE TABLE IF NOT EXISTS citizens(first_name VARCHAR, last_name VARCHAR, id_number INTEGER )");

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get the data from the user first
                String firstName = mFname.getText().toString();
                String lastName = mLname.getText().toString();
                String idNumber = mID.getText().toString().trim();

            //Check if the user is attempting to submit empty fields
                if(firstName.isEmpty()){
                    messages("First Name Error","Please enter your First Name");
                }else if (lastName.isEmpty()){
                    messages("Last Name Error","Please enter your last name");
                }else if (idNumber.isEmpty()){
                    messages("ID Number error","Please enter your ID number");
                }else {
                    //Proceed to receive your data into your db called huduma
                    db.execSQL("INSERT INTO citizens VALUES('"+firstName+"', '"+lastName+"', '"+idNumber+"')");

                        messages("Success","User saved successfully");

                        //Clear input fields for next entry
                    mFname.setText("");
                    mLname.setText("");
                    mID.setText("");
                }
            }
        });

        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Use a cursor to query and select data from your db table
                Cursor cursor = db.rawQuery("SELECT * FROM citizens", null);

                //Check if the cursor found any data in the db
                if(cursor.getCount()==0){
                    messages("Empty Database", "Sorry, no data was found");
                }else{
                    //Proceed to display the selected data
                    //User the String buffer to append and display the records
                    StringBuffer buffer = new StringBuffer();

                    //Loop through the selected data that is on your cursor to display
                    while (cursor.moveToNext()){
                        buffer.append(cursor.getString(0)+"\t"); //Zero is a column for fname
                        buffer.append(cursor.getString(1)+"\t"); //One is a column for lname
                        buffer.append(cursor.getString(2)+"\n"); //Two is a column for id
                    }
                    //Display yout data using the string buffer on the message dialog
                    messages("DATABASE RECORDS",buffer.toString());

                }
            }
        });

        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get the id to use as a unique identifier to delete any row
                String id = mID.getText().toString();
                //Check if the user is attempting to delete with an empty ID field
                if(id.isEmpty()){
                    messages("ID Error", "Please enter the ID number");
                }else{
                    Cursor cursor = db.rawQuery("SELECT * FROM citizens WHERE id_number = '"+id+"' ",null);
                    //Proceed to delete
                    if(cursor.moveToFirst()){
                        db.execSQL("DELETE FROM citizens WHERE id_number = '"+id+"' ");
                        messages("SUCCESS","User deleted successfully");
                        mID.setText("");
                    }
                }
            }
        });
    }
    //Message display function
    public  void messages(String Title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(Title);
        builder.setMessage(message);
        builder.create().show();

    }
}
