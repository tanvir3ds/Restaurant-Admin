package com.example.restadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

public class Admin_Login extends AppCompatActivity {

        EditText nameEditText, passwordEditText;
        Button loginButtonAdmin;
    private ProgressBar progressBar;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);


            //remove the title bar
            requestWindowFeature(Window.FEATURE_NO_TITLE);

            //remove the title bar
            getWindow() .setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            );

            setContentView(R.layout.activity_admin__login);

            progressBar =(ProgressBar) findViewById(R.id.SignUpProgressBer_Id);




            //      Login admin..................................................................
            nameEditText=(EditText) findViewById(R.id.adminEditUserText_ID);
            passwordEditText=(EditText) findViewById(R.id.adminEditTextPassword_ID);

            loginButtonAdmin=(Button)findViewById(R.id.AdminLoginButton);


            //input email password from java class this

            final String name =nameEditText.getText().toString().trim();
            final String password =passwordEditText.getText().toString().trim();

            loginButtonAdmin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adminLogin();
                }
            });


        }

    private void adminLogin() {


        //input email passwor fron java class this

        String email =nameEditText.getText().toString().trim();
        String password =passwordEditText.getText().toString().trim();





        //checking the validity of the email
        if(email.isEmpty())
        {
            nameEditText.setError("Enter an email address");
            nameEditText.requestFocus();
            return;
        }


        //checking the validity of the password
        if(password.isEmpty())
        {
            passwordEditText.setError("Enter a password");
            passwordEditText.requestFocus();
            return;
        }

        if (password.length()<6){
            passwordEditText.setError("Minimum lenth of a password should be 6");
            passwordEditText.requestFocus();
            return;
        }
        if (email.equals("admin") && password.equals("123456") || email.equals("Admin") && password.equals("123456") ){
            progressBar.setVisibility(View.VISIBLE);
            Intent intent = new Intent(getApplicationContext(), AdminDataInsertUpdateDelete.class);
            startActivity(intent);
            Toast.makeText(getApplicationContext(),"Login successful", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }else{

            Toast.makeText(getApplicationContext(),"Email Password not matching", Toast.LENGTH_SHORT).show();
            nameEditText.requestFocus();

        }

    }
}
