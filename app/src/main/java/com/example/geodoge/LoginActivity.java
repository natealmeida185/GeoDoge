/**
 * Name: Nathan Almeida,
 * Date Created: 05/05/2021,
 * Purpose: Allow the user to Login if already registered
 * Code References: AllCodingTutorials- YouTube: https://www.youtube.com/watch?v=8obgNNlj3Eo&ab_channel=AllCodingTutorials
 */

package com.example.geodoge;

import androidx.appcompat.app.AppCompatActivity;import android.content.Intent;import android.os.Bundle;import android.view.View;import android.widget.Button;import android.widget.EditText;import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    //Declare needed UI objects and DB Helper
    EditText username, password;
    Button btnLogin, btnSignup;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Setting object by finding their ID's
        username = (EditText) findViewById(R.id.username2);
        password = (EditText) findViewById(R.id.password2);
        btnLogin = (Button) findViewById(R.id.btnSignIn2);
        btnSignup = (Button) findViewById(R.id.btnSignUp1);
        DB = new DBHelper(this);

        //Setting a Login Button Listener
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Convert the users input into a string
                String user = username.getText().toString();
                String pass = password.getText().toString();
                //Checking if users inputted fields are null or empty
                if(user.equals("") || pass.equals("")) {
                    Toast.makeText(LoginActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    //Check if Username and Password is valid within the DB
                    Boolean checkuserpass = DB.checkUsernamePassword(user, pass);
                    if(checkuserpass == true) {
                        Toast.makeText(LoginActivity.this, "Sign in successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //Bring User to the SignUp Page if needed
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}