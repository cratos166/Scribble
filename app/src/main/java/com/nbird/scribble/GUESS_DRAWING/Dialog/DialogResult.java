package com.nbird.scribble.GUESS_DRAWING.Dialog;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nbird.scribble.GUESS_DRAWING.Adapter.ChatAdapter;
import com.nbird.scribble.GUESS_DRAWING.Adapter.ResultAdapter;
import com.nbird.scribble.GUESS_DRAWING.MODEL.ResultModel;
import com.nbird.scribble.MAIN_MENU.Activity.MainActivity;
import com.nbird.scribble.R;
import com.nbird.scribble.WHITE_BOARD.Adapter.ObjectAdapter;
import com.nbird.scribble.WHITE_BOARD.Model.ObjectModel;
import com.nbird.scribble.WHITE_BOARD.WhiteBoardActivity;

import java.util.ArrayList;

public class DialogResult {

    Context context;

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference table_user = database.getReference();

    RecyclerView recyclerview;

    ArrayList<ResultModel> resultModelArrayList;


    ResultAdapter myAdapter;
    LinearLayoutManager linearLayoutManager1;
    AlertDialog alertDialog;
    CountDownTimer countDownTimer;
    String myName; String myImage; String myUID;

    public DialogResult(Context context, ArrayList<ResultModel> resultModelArrayList, String myName, String myImage, String myUID) {
        this.context = context;
        this.resultModelArrayList=resultModelArrayList;
        this.myName=myName;
        this.myImage=myImage;
        this.myUID=myUID;

    }

    public void start(View v){
        AlertDialog.Builder builderFact=new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        final View viewFact= LayoutInflater.from(context).inflate(R.layout.dialog_result,(ConstraintLayout) v.findViewById(R.id.layoutDialogContainer),false);
        builderFact.setView(viewFact);
        builderFact.setCancelable(false);

         alertDialog=builderFact.create();

        recyclerview = (RecyclerView) viewFact.findViewById(R.id.recyclerView);

        Button next=(Button) viewFact.findViewById(R.id.next);
        ImageView cancel=(ImageView) viewFact.findViewById(R.id.cancel);

        final int[] sec = {15};

        countDownTimer=new CountDownTimer(15*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                sec[0]--;
                next.setText("Next game starts in "+ sec[0] +" sec");
            }

            @Override
            public void onFinish() {

                try{
                    countDownTimer.cancel();
                }catch (Exception e){

                }

                Intent intent=new Intent(context, WhiteBoardActivity.class);
                intent.putExtra("myName",myName);
                intent.putExtra("myImage",myImage);
                intent.putExtra("myUID",myUID);
                context.startActivity(intent);

            }
        }.start();









        if(alertDialog.getWindow()!=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        try{
            alertDialog.show();
        }catch (Exception e){

        }


        linearLayoutManager1 = new LinearLayoutManager(context);
        linearLayoutManager1.setOrientation(recyclerview.VERTICAL);
        recyclerview.setLayoutManager(linearLayoutManager1);
        myAdapter=new ResultAdapter(context,resultModelArrayList,alertDialog);
        recyclerview.setAdapter(myAdapter);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    alertDialog.dismiss();
                }catch (Exception e){

                }

                try{
                    countDownTimer.cancel();
                }catch (Exception e){

                }

                Intent intent=new Intent(context, MainActivity.class);
                context.startActivity(intent);
            }
        });



    }





}



