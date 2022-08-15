package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.security.PublicKey;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener ,View.OnKeyListener{



    Boolean bool=true;

    public  void showuserlist(){
        Log.i("kkkkiiii","kkkkiiii");
        if(bool==true) {
            Intent intent = new Intent(getApplicationContext(), chosinglist.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(getApplicationContext(), photolist.class);
            intent.putExtra("abc","0");
            startActivity(intent);

        }


    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        Log.i("hi","Ankit Kikknfv");
        if(i==KeyEvent.KEYCODE_ENTER && keyEvent.getAction()==KeyEvent.ACTION_DOWN){

            oclickFn(view);

        }

        return false;
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.textView){
            if(bool==true) {
                bool=false;
                Button button = (Button) findViewById(R.id.button);
                TextView textView = (TextView) findViewById(R.id.textView);
                textView.setText("or Signin");
                button.setText("Login");
            }else{
                bool=true;
                Button button = (Button) findViewById(R.id.button);
                TextView textView = (TextView) findViewById(R.id.textView);
                textView.setText("or Login");
                button.setText("Signin");

            }

        }else if (view.getId() == R.id.imageView2 || view.getId() == R.id.iiii) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
    }

    public  void oclickFn(View view){
        setTitle("Login Page");
        Log.i("clic","clic");
        EditText name=(EditText) findViewById(R.id.editTextName);
        EditText pass=(EditText) findViewById(R.id.editTextpass);

        if(name.getText().toString().matches("")|| pass.getText().toString().matches("")){
            Toast.makeText(MainActivity.this, "A Username and A Password is requred", Toast.LENGTH_SHORT).show();
        }else{
            if(bool==true){
                Log.i("kkk","kkkk");
            ParseUser user=new ParseUser();
            user.setUsername(name.getText().toString());
            user.setPassword(pass.getText().toString());
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null){
                        showuserlist();
                    }else{
                        Toast.makeText(MainActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            }else{
                ParseUser.logInInBackground(name.getText().toString(), pass.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if(user!=null){
                            showuserlist();

                        }else{
                            Toast.makeText(MainActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView imageView=findViewById(R.id.imageView2);
        EditText pass=(EditText)findViewById(R.id.editTextpass);
        ConstraintLayout background=(ConstraintLayout) findViewById(R.id.iiii);
        background.setOnClickListener(this);
        pass.setOnClickListener(this);
        imageView.setOnClickListener(this);
        pass.setOnKeyListener(this);

        if(ParseUser.getCurrentUser()!=null){
            Intent intent = new Intent(getApplicationContext(), photolist.class);
            intent.putExtra("abc","0");
            startActivity(intent);
        }
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}