package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class photolist extends AppCompatActivity {

    String kkj;

    public void func(View view){
        Button button=(Button) findViewById(R.id.fallow_unfallow);
        String str=button.getText().toString();
        if(str.matches("Follow")){
            button.setText("Unfollow");
            ParseUser.getCurrentUser().add("isFollowing",kkj);
        }else{
            button.setText("Follow");
            ParseUser.getCurrentUser().getList("isFollowing").remove(kkj);
            List a=ParseUser.getCurrentUser().getList("isFollowing");
            ParseUser.getCurrentUser().remove("isFollowing");
            ParseUser.getCurrentUser().put("isFollowing",a);

        }
    }
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
                AlertDialog.Builder builder=new AlertDialog.Builder(photolist.this);
                builder.setTitle("Send a Tweet");
                EditText tweetertextview=new EditText(photolist.this);
                builder.setView(tweetertextview);
                builder.setPositiveButton("Tweet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ParseObject object=new ParseObject("Tweetn");
                        object.put("tweet",tweetertextview.getText().toString());
                        object.put("username",ParseUser.getCurrentUser().getUsername());
                        object.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException ex) {
                                if(ex==null){
                                    Toast.makeText(photolist.this, "Tweet Posted", Toast.LENGTH_SHORT).show();
                                }else{
                                    ex.printStackTrace();
                                    Toast.makeText(photolist.this, "Tweet Posting Failed", Toast.LENGTH_SHORT).show();
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
                builder.show();
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
        setContentView(R.layout.activity_photolist);

        Log.i("jjj","jj");



        final ListView listView =(ListView) findViewById(R.id.llll);
        final List<Map<String,String>> tweetdata=new ArrayList<>();
        ParseQuery<ParseObject>query=ParseQuery.getQuery("Tweetn");



        Intent in=getIntent();
        kkj=in.getStringExtra("abc");
        if(in.getStringExtra("abc").matches("0")) {
            setTitle("Home");
            query.whereContainedIn("username", ParseUser.getCurrentUser().getList("isFollowing"));
            Button button=(Button) findViewById(R.id.fallow_unfallow);
            button.setVisibility(View.INVISIBLE);
        }else{
            setTitle(kkj+"'s Tweets");
            query.whereEqualTo("username",in.getStringExtra("abc"));
            if(in.getStringExtra("abc").matches(ParseUser.getCurrentUser().getUsername())){
                Button button=(Button) findViewById(R.id.fallow_unfallow);
                button.setEnabled(false);
                button.setVisibility(View.INVISIBLE);
            }else{
                Button button=(Button) findViewById(R.id.fallow_unfallow);
                if(ParseUser.getCurrentUser().getList("isFollowing").contains(in.getStringExtra("abc"))){
                    button.setText("Unfollow");
                }else{
                    button.setText("Follow");
                }

            }

        }
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){
                    for (ParseObject tweet:objects){
                        Log.i("name",tweet.getString("tweet"));
                        Map<String,String> tweetinfo=new HashMap<>();
                        tweetinfo.put("content",tweet.getString("tweet"));
                        tweetinfo.put("username",tweet.getString("username"));
                        tweetdata.add(tweetinfo);
                        Log.i("kkkk","kkkkk");
                    }

                    if(tweetdata.size()>0){
                    SimpleAdapter simpleAdapter=new SimpleAdapter(photolist.this,tweetdata, android.R.layout.simple_list_item_2,new String[]{"content","username"},new int[] {android.R.id.text1,android.R.id.text2});
                    listView.setAdapter(simpleAdapter);
                    simpleAdapter.notifyDataSetChanged();}else{
                        TextView textView=(TextView)findViewById(R.id.textView2);
                        textView.setText("No Tweets");
                    }
                }else{
                    e.printStackTrace();
                }
            }
        });

        if(in.getStringExtra("abc").matches("0")){

        }else if(in.getStringExtra("abc").matches(ParseUser.getCurrentUser().getUsername())){
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int index, long l) {
                    new AlertDialog.Builder(photolist.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Are You Sure")
                            .setMessage("This will delete this note")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int j) {
                                    ParseQuery<ParseObject>query=ParseQuery.getQuery("Tweetn");
                                    query.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
                                    query.findInBackground(new FindCallback<ParseObject>() {
                                        @Override
                                        public void done(List<ParseObject> objects, ParseException e) {
                                            if(e==null){
                                                for (ParseObject tweet:objects) {
                                                    if(tweet.getString("tweet").matches(tweetdata.get(index).get("content").toString()))
                                                    tweet.deleteInBackground(new DeleteCallback() {
                                                        @Override
                                                        public void done(ParseException exr) {
                                                            if(exr==null){
                                                                Toast.makeText(photolist.this, "Tweet Deleted Sucessfully", Toast.LENGTH_SHORT).show();
                                                            }else{
                                                                Toast.makeText(photolist.this, "Fail to delete Tweet", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });

                                                }
                                            }else{
                                                e.printStackTrace();
                                            }
                                        }
                                    });


                                }
                            })
                            .setNegativeButton("NO",null).show();

                    return true;
                }
            });
        }





    }
}