package com.example.viaanmovielisting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import classes.DataBaseHelper;

public class SignUp extends AppCompatActivity {
    EditText userName, email, password;
    TextView btnLogin;
    ProgressBar loader;
    DataBaseHelper dataBaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        userName = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btnLogin = findViewById(R.id.login);
        loader = findViewById(R.id.loader);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loader.setVisibility(View.VISIBLE);
                RegisterUser(userName.getText().toString(),email.getText().toString(),password.getText().toString());
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SignUp.this,Login.class));
        finish();
    }
    private void RegisterUser(String username, String email, String password) {
        dataBaseHelper = new DataBaseHelper(SignUp.this);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        final SharedPreferences.Editor editor = pref.edit();
        if (dataBaseHelper.checkExists(email)) {
           alertUserAboutError("USer Exists","User already exists please login...!");
        }else{
            if(validation()){
                loader.setVisibility(View.GONE);
                if(dataBaseHelper.insert(username,email,password)){
                    editor.putString("user_email", email);
                    editor.commit();
                    startActivity(new Intent(SignUp.this,MainActivity.class));
                    finish();
                }
                else{
                    alertUserAboutError("Something went Wrong","Error While Inserting please try again");
                }
            }
        }
    }
    private void alertUserAboutError(String title,String titleMessage) {

        AlertDialogFragment alertDialogFragment = new AlertDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("error_title",title);
        bundle.putString("error_message",titleMessage);
        alertDialogFragment.setArguments(bundle);
        alertDialogFragment.show(getSupportFragmentManager(),"error_dialog");
    }
    private boolean validation(){
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
