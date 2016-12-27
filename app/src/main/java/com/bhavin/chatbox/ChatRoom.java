// File: ChatRoom.java
package com.bhavin.chatbox;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class ChatRoom extends AppCompatActivity {

    private FirebaseListAdapter<ChatMessage> adapter;
    private String stringJsonFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        Intent intent = getIntent();
        stringJsonFriends = intent.getStringExtra("jsonFriends");

        // load chat room
        displayChatMessages();

        Button sendButton = (Button)findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText)findViewById(R.id.inputEditText);

                // push instance of ChatMessage
                // to Firebase database
                FirebaseDatabase.getInstance()
                        .getReference()
                        .push()
                        .setValue(new ChatMessage(input.getText().toString(),
                                FirebaseAuth.getInstance()
                                        .getCurrentUser()
                                        .getDisplayName())
                        );

                // clear input
                input.setText("");
            }
        });
    }

    private void displayChatMessages() {

        ListView listOfMessages = (ListView)findViewById(R.id.message_list);
        adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,
                R.layout.message, FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {

                // get references to the views of message.xml
                TextView messageText = (TextView)v.findViewById(R.id.message_text);
                TextView messageUser = (TextView)v.findViewById(R.id.message_user);
                TextView messageTime = (TextView)v.findViewById(R.id.message_time);
                // set message text
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                // set message date
                messageTime.setText(DateFormat.format("MM-dd-yyyy (HH:mm:ss)", model.getMessageTime()));
            }
        };
        listOfMessages.setAdapter(adapter);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate menu items
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chat_room_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // handle clicks on menu items
        switch (item.getItemId()) {
            // sign out leads back
            // to log-in screen
            case R.id.sign_out_menu_item:
                FirebaseAuth.getInstance().signOut();
                finish();
                return true;
            case R.id.friends_menu_item:
                Intent intent = new Intent(ChatRoom.this, FacebookFriendsList.class);
                intent.putExtra("jsonFriends", stringJsonFriends);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onBackPressed(){
        moveTaskToBack(true);
    }
}
