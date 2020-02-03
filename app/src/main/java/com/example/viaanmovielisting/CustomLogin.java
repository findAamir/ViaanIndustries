package com.example.viaanmovielisting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import classes.DataBaseHelper;

public class CustomLogin extends AppCompatActivity {
    EditText email,password;
    TextView btnLogin,btnCreate;
    DataBaseHelper dataBaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_login);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btnLogin = findViewById(R.id.login);
        btnCreate = findViewById(R.id.create);
        dataBaseHelper = new DataBaseHelper(CustomLogin.this);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        final SharedPreferences.Editor editor = pref.edit();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validation()){
                    try {
                        if(dataBaseHelper.login(email.getText().toString(),password.getText().toString())){
                            editor.putString("user_email", email.getText().toString());
                            editor.commit();
                            startActivity(new Intent(CustomLogin.this,MainActivity.class));
                            finish();
                        }else{
                            alertUserAboutError("User not Exists ","It seems you are not register...!");
                        }
                    }catch (Exception ex){
                        Log.e("Error in login",ex.getMessage());
                    }
                }
            }
        });
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomLogin.this,SignUp.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(CustomLogin.this,Login.class));
        finish();
    }

    private void alertUserAboutError(String title, String titleMessage) {

        AlertDialogFragment alertDialogFragment = new AlertDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("error_title",title);
        bundle.putString("error_message",titleMessage);
        alertDialogFragment.setArguments(bundle);
        alertDialogFragment.show(getSupportFragmentManager(),"error_dialog");
    }

    private boolean validation() {
        String emailId = email.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (emailId.matches(emailPattern) || !emailId.isEmpty() || !emailId.equals(""))
        {
            return true;
        }
        else {
            alertUserAboutError("Invalid Email","Please enter a valid email");
        }
        if(password.getText().toString().length()>=6 || !password.getText().toString().equals("") || !password.getText().toString().isEmpty()){
            return true;
        }
        else{
            alertUserAboutError("Invalid Password","Please enter a 6 digit long password or password cannot be blank");
        }
        return false;
    }
}
