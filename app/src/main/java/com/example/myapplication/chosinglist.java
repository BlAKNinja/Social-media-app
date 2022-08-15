package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class chosinglist extends AppCompatActivity {

    ArrayList<String> names=new ArrayList<>();
    ArrayAdapter adapter;

    public void nextFn(View view){
        Intent intent = new Intent(getApplicationContext(), photolist.class);
        intent.putExtra("abc","0");
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chosinglist);

        setTitle("Chose User to Follow them");

        ListView listView=(ListView) findViewById(R.id.listView);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        adapter =new ArrayAdapter(chosinglist.this, android.R.layout.simple_list_item_checked,names);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CheckedTextView checkedTextView=(CheckedTextView) view;
                if(checkedTextView.isChecked()){
                    ParseUser.getCurrentUser().add("isFollowing",names.get(i));

                }else{
                    ParseUser.getCurrentUser().getList("isFollowing").remove(names.get(i));
                    List a=ParseUser.getCurrentUser().getList("isFollowing");
                    ParseUser.getCurrentUser().remove("isFollowing");
                    ParseUser.getCurrentUser().put("isFollowing",a);

                }
                ParseUser.getCurrentUser().saveInBackground();

            }
        });
        ParseQuery<ParseUser> query=ParseUser.getQuery();
        query.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
        query.addAscendingOrder("username");

        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e==null){
                    if(objects.size()>0){
                        for (ParseUser user :objects){
                            names.add(user.getUsername());
                        }
                        adapter.notifyDataSetChanged();
                        for (String name:names){
                            if(ParseUser.getCurrentUser().getList("isFollowing").contains(name)){
                                listView.setItemChecked(names.indexOf(name),true);
                            }
                        }
                    }
                }else{
                    e.printStackTrace();
                }
            }
        });
        adapter.notifyDataSetChanged();
    }
}