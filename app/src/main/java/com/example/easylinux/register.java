package com.example.easylinux;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


//import the following
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class register extends AppCompatActivity {
    //declare the edittexts
    private EditText edit1,edit2;
    //declare an object for DBHandler
    private DbHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //initialize edittext objects
        edit1=findViewById(R.id.reguser);
        edit2=findViewById(R.id.regpass);
        //create an object for DBHandler
        dbHandler=new DbHandler(register.this);
    }

    //onclick method for submit button
    public void adduser(View v)
    {
        //retrieve the user input from EditTexts
        String user_uname=edit1.getText().toString();
        String user_pwd=edit2.getText().toString();

        //call the addNewUser of DBHandler class
        String msg = dbHandler.addNewUser(user_uname,user_pwd);

        //print a toast
        Toast.makeText(register.this,msg,Toast.LENGTH_LONG).show();

        //clear the EditTexts after user is added
        edit1.setText("");
        edit2.setText("");
    }
    //onclick method for back to home button
    public void backtohome(View v)
    {
        //clicking on BACK TO HOME launches MainActivity
        Intent i = new Intent(register.this,MainActivity.class);
        startActivity(i);
    }
}