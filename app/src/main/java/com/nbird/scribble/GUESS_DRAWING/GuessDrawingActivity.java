package com.nbird.scribble.GUESS_DRAWING;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.nbird.scribble.GUESS_DRAWING.Adapter.ChatAdapter;
import com.nbird.scribble.GUESS_DRAWING.MODEL.DrawingDataModel;
import com.nbird.scribble.R;

import java.util.ArrayList;

public class GuessDrawingActivity extends AppCompatActivity {

    RecyclerView recyclerView2;
    LinearLayoutManager linearLayoutManager1;
    ChatAdapter chatAdapter;
    ArrayList<String> chatListArray;
    TextInputEditText edit;
    Button send;
    ArrayList<DrawingDataModel> dataModelArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_drawing);


        chatListArray=new ArrayList<>();
        dataModelArrayList=new ArrayList<>();

        recyclerView2=(RecyclerView) findViewById(R.id.recyclerView2);
        edit=(TextInputEditText) findViewById(R.id.edit);
        send=(Button) findViewById(R.id.send);



        linearLayoutManager1 = new LinearLayoutManager(this);
        linearLayoutManager1.setStackFromEnd(true);
        linearLayoutManager1.setOrientation(recyclerView2.VERTICAL);
        recyclerView2.setLayoutManager(linearLayoutManager1);
        chatAdapter = new ChatAdapter(this, chatListArray);
        recyclerView2.setAdapter(chatAdapter);


        edit.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            chatListArray.add(edit.getText().toString());
                            chatAdapter.notifyDataSetChanged();
                            edit.setText("");
                            recyclerView2.smoothScrollToPosition(recyclerView2.getAdapter().getItemCount());
                    }
                }
                return false;
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatListArray.add(edit.getText().toString());
                chatAdapter.notifyDataSetChanged();
                edit.setText("");
                recyclerView2.smoothScrollToPosition(recyclerView2.getAdapter().getItemCount());
            }
        });




    }





}