package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class user_list extends AppCompatActivity {


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.logout:
                ParseUser.logOut();
                Intent intent =new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.tweet:
                AlertDialog.Builder builder=new AlertDialog.Builder(user_list.this);
                builder.setTitle("Send a Tweet");
                EditText tweetertextview=new EditText(user_list.this);
                builder.setView(tweetertextview);
                builder.setPositiveButton("Tweet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ParseObject object=new ParseObject("Tweet");
                        object.put("tweet",tweetertextview.getText().toString());
                        object.put("username",ParseUser.getCurrentUser().getUsername());
                        object.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException ex) {
                                if(ex==null){
                                    Toast.makeText(user_list.this, "Tweet Posted", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(user_list.this, "Tweet Posting Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                return true;
            case R.id.explore:
                Intent inte = new Intent(getApplicationContext(), user_list.class);
                inte.putExtra("abc","0");
                startActivity(inte);
                return true;
            case  R.id.friends:
                Intent inter = new Intent(getApplicationContext(), user_list.class);
                inter.putExtra("abc",ParseUser.getCurrentUser().getUsername());
                startActivity(inter);
                return true;
            case R.id.you:
                Intent i = new Intent(getApplicationContext(), photolist.class);
                i.putExtra("abc",ParseUser.getCurrentUser().getUsername());
                startActivity(i);
                return true;
            case R.id.home:
                Intent intentt = new Intent(getApplicationContext(), photolist.class);
                intentt.putExtra("abc","0");
                startActivity(intentt);





            default:
                return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        final ListView listView=(ListView) findViewById(R.id.lsitView);
        final ArrayList<String>names=new ArrayList<>();
        final ArrayAdapter arrayAdapter=new ArrayAdapter(user_list.this,android.R.layout.simple_list_item_1,names);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getApplicationContext(),photolist.class);
                intent.putExtra("abc",names.get(i).toString());
                Log.i("names",names.get(i));
                startActivity(intent);
            }
        });


        ParseQuery<ParseUser>query=ParseUser.getQuery();
        Intent ak=getIntent();
        String s=ak.getStringExtra("abc");
        if(s.matches("0")){
            setTitle("User List");
        query.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
        }else{
            setTitle("Friends List");
            query.whereContainedIn("username", ParseUser.getCurrentUser().getList("isFollowing"));

        }
        query.addAscendingOrder("username");

        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e==null){
                    if(objects.size()>0){
                        for (ParseUser user :objects){
                            names.add(user.getUsername());
                        }
                        listView.setAdapter(arrayAdapter);
                    }
                }else{
                    e.printStackTrace();
                }
            }
        });
        arrayAdapter.notifyDataSetChanged();




    }
}