/**
 * Name: Nathan Almeida,
 * Date Created: 05/05/2021,
 * Purpose: Allow the user to Sign Up for the Application
 * Code References: AllCodingTutorials- YouTube: https://www.youtube.com/watch?v=8obgNNlj3Eo&ab_channel=AllCodingTutorials
 */

package com.example.geodoge;

import androidx.appcompat.app.AppCompatActivity;import android.content.Intent;import android.os.Bundle;import android.view.View;import android.widget.Button;import android.widget.EditText;import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {
    //Declare UI Objects and DB Helper
    EditText username, password, repassword;
    Button btnSignUp, btnSignIn;
    DBHelper DB;

    static String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //Setting object by finding their ID's
        username = (EditText) findViewById(R.id.username1);
        password = (EditText) findViewById(R.id.password1);
        repassword = (EditText) findViewById(R.id.repassword1);
        btnSignUp = (Button) findViewById(R.id.btnSignUp1);
        btnSignIn = (Button) findViewById(R.id.btnSignIn1);
        DB = new DBHelper(this);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Convert users inputted data into a string
                user = username.getText().toString();
                String pass = password.getText().toString();
                String repass = repassword.getText().toString();
                //Check if any fields are null or empty
                if (user.equals("") || pass.equals("") || repass.equals("")) {
                    Toast.makeText(SignUpActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    //Check if retyped password is correct
                    if(pass.equals(repass)) {
                        Boolean checkuser = DB.checkUserName(user);
                        //If user is not found, insert into database
                        if(checkuser == false) {
                            Boolean insert = DB.insertData(user, pass);
                            //Bring user to Main Page if registered successfully
                            if (insert == true) {
                                Toast.makeText(SignUpActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                startActivity(intent);
                            //Account for any failures
                            } else {
                                Toast.makeText(SignUpActivity.this, "Registered  failed", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SignUpActivity.this, "User already exists, please sign in", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SignUpActivity.this, "Passwords not matching", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //Bring user to Sign In page if needed
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}