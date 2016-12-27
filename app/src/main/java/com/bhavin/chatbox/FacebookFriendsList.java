// File: FacebookFriendsList.java
package com.bhavin.chatbox;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;

public class FacebookFriendsList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_friends_list);

        Intent intent = getIntent();
        String jsondata = intent.getStringExtra("jsonFriends");

        JSONArray friendslist;
        ArrayList<String> friends = new ArrayList<String>();
        try{
            friendslist = new JSONArray(jsondata);
            for(int i = 0; i <friendslist.length(); i++)
                friends.add(friendslist.getJSONObject(i).getString("name"));
        } catch (JSONException e){
            e.printStackTrace();
        }
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, friends);
        ListView listView = (ListView) findViewById(R.id.friendsList);
        listView.setAdapter(adapter);
    }
}
