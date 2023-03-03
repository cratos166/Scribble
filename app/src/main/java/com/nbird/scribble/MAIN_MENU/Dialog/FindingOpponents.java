package com.nbird.scribble.MAIN_MENU.Dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.nbird.scribble.BOT.AvatarLink;
import com.nbird.scribble.BOT.BOTName;
import com.nbird.scribble.MAIN_MENU.Adapter.PlayerDataAdapter;
import com.nbird.scribble.MAIN_MENU.Model.PlayerDetails;
import com.nbird.scribble.R;
import com.nbird.scribble.WHITE_BOARD.WhiteBoardActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class FindingOpponents {

    Context context;

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference table_user = database.getReference();
    FirebaseAuth mAuth= FirebaseAuth.getInstance();

    RecyclerView recyclerview;

    ArrayList<PlayerDetails> playerDetailsArrayList;

    ValueEventListener playerDetailsValueEventListener;
    PlayerDataAdapter myAdapter;

    CountDownTimer countDownTimer;

    int numberOfPlayers=1;
    DownloadTask task;
    String myName,myImage,myUID;
    TextView numberOfPlayersJoinedTextView,headingTextView;
    AlertDialog alertDialog;

    public FindingOpponents(Context context,String myName,String myImage,String myUID) {
        this.context = context;
        this.myName=myName;
        this.myImage=myImage;
        this.myUID=myUID;
    }

    public void start(View v){
        AlertDialog.Builder builderFact=new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        final View viewFact= LayoutInflater.from(context).inflate(R.layout.dialog_finding_opponent,(ConstraintLayout) v.findViewById(R.id.layoutDialogContainer),false);
        builderFact.setView(viewFact);
        builderFact.setCancelable(false);

         alertDialog=builderFact.create();

        recyclerview = (RecyclerView) viewFact.findViewById(R.id.recyclerView);
        ImageView cancelButton = (ImageView) viewFact.findViewById(R.id.cancel);
        headingTextView=(TextView) viewFact.findViewById(R.id.textTitle);

        numberOfPlayersJoinedTextView=(TextView) viewFact.findViewById(R.id.numberOfPlayersJoinedTextView);





        playerDetailsArrayList=new ArrayList<>();





        PlayerDetails playerDetails=new PlayerDetails(myName,myImage,myUID);

        table_user.child("ROOM").child(myUID).child(myUID).setValue(playerDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                dataReceiver();
                timerCaller();
            }
        });








        if(alertDialog.getWindow()!=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        try{
            alertDialog.show();
        }catch (Exception e){

        }

        myAdapter=new PlayerDataAdapter(context,playerDetailsArrayList);
        recyclerview.setLayoutManager(new GridLayoutManager(context,2));
        recyclerview.setAdapter(myAdapter);




        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    alertDialog.dismiss();
                }catch (Exception e){

                }

                try{
                    countDownTimer.cancel();
                }catch (Exception e){

                }

                try{table_user.child("ROOM").child(myUID).removeEventListener(playerDetailsValueEventListener);}catch (Exception e){}

            }
        });





    }


    private void timerCaller(){
        Random random=new Random();
        int num=random.nextInt(5)+3;
        countDownTimer=new CountDownTimer(num*1000,1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {



                BOTFinder();


                numberOfPlayers++;

                if(numberOfPlayers!=4){
                    timerCaller();
                }else{


                    intent();

                }

            }
        }.start();
    }


    private void intent(){
        countDownTimer=new CountDownTimer(10000,1000) {
            @Override
            public void onTick(long l) {
                headingTextView.setText("Game starts in "+l/1000+" sec");
            }

            @Override
            public void onFinish() {

                try { alertDialog.dismiss();}catch (Exception e){}

                try{
                    countDownTimer.cancel();
                }catch (Exception e){

                }

                try{table_user.child("ROOM").child(myUID).removeEventListener(playerDetailsValueEventListener);}catch (Exception e){}


                Intent intent=new Intent(context, WhiteBoardActivity.class);
                intent.putExtra("myName",myName);
                intent.putExtra("myImage",myImage);
                intent.putExtra("myUID",myUID);
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        }.start();
    }


    private void dataReceiver(){





        playerDetailsValueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                try{

                    playerDetailsArrayList.clear();

                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){

                        PlayerDetails playerDetails=dataSnapshot.getValue(PlayerDetails.class);
                        playerDetailsArrayList.add(playerDetails);
                    }

                    numberOfPlayersJoinedTextView.setText(playerDetailsArrayList.size()+"/4");

                    recyclerview.smoothScrollToPosition(recyclerview.getAdapter().getItemCount());
                    myAdapter.notifyDataSetChanged();

                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        table_user.child("ROOM").child(myUID).addValueEventListener(playerDetailsValueEventListener);

    }


    private void opponentUploader(PlayerDetails playerDetails){

        table_user.child("ROOM").child(myUID).child(playerDetails.getUID()).setValue(playerDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }

    private void BOTFinder(){

        Random r= new Random();
        int  tom=r.nextInt(25);
        AvatarLink avatarLink=new AvatarLink();

        int tt=r.nextInt(20)+1;


        if(tt<13){
            String image= avatarLink.getArr().get(tom);
            String name=new BOTName().start();
            UUID uuid = UUID.randomUUID();
            PlayerDetails playerDetails=new PlayerDetails(name,image,String.valueOf(uuid));
            opponentUploader(playerDetails);

        }else{
            task = new DownloadTask();
            task.execute("https://randomuser.me/api/?format=JSON");
        }

    }


    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {



                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo = jsonObject.getString("results");
                JSONArray arr = new JSONArray(weatherInfo);







                String oppoURL=jsonObject.getJSONArray("results")
                        .getJSONObject(0)
                        .getJSONObject("picture")
                        .optString("medium");



                String name=new BOTName().start();
                UUID uuid = UUID.randomUUID();
                PlayerDetails playerDetails=new PlayerDetails(name,oppoURL,String.valueOf(uuid));
                opponentUploader(playerDetails);



            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}



