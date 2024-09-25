package com.example.easylinux;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

public class login extends AppCompatActivity {
    //declarations
    private EditText edit_uname,edit_pwd;
    private DbHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("Session", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", null);
        if (username != null) {
            // User is already logged in, proceed to the menu or home activity
            Intent intent = new Intent(login.this, menu.class);
            intent.putExtra("username", username);  // Pass the username to the next activity
            startActivity(intent);
            finish();
        }else {
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_login);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
            //initialize widgets
            edit_uname = findViewById(R.id.UserName);
            edit_pwd = findViewById(R.id.pass);
            //create an object for DBHandler class
            dbHandler = new DbHandler(login.this);
        }
    }

    //onclick method for Login Button
    public void loginCheck(View v)
    {
        //retrieve the values from edittext fields
        String username=edit_uname.getText().toString();
        String password=edit_pwd.getText().toString();
        //validate textfields
        if(username.equals("") || password.equals(""))
        {
            Toast.makeText(login.this,"All fields are manadatory",Toast.LENGTH_LONG).show();
        }
        else {
            //call checkEmailPassword method of DBHandler Class, returns true on successful login
            Boolean check= dbHandler.checkEmailPassword(username, password);
            //clear textfields

            edit_pwd.setText("");

            if(check == true) {
                edit_uname.setText("");
                Toast.makeText(login.this,"Successful Login",Toast.LENGTH_LONG).show();
                SharedPreferences sharedPreferences = getSharedPreferences("Session", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("username", username);
                editor.apply();
                Intent i = new Intent(login.this, menu.class);
                i.putExtra("username",username);
                startActivity(i);
                finish();
            }
            else
            {
                Toast.makeText(login.this,"Invalid Credentials",Toast.LENGTH_LONG).show();
            }
        }
    }
}