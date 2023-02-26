package com.nbird.scribble.GUESS_DRAWING.Dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
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
import com.nbird.scribble.R;
import com.nbird.scribble.WHITE_BOARD.Adapter.ObjectAdapter;
import com.nbird.scribble.WHITE_BOARD.Model.ObjectModel;

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

    public DialogResult(Context context,ArrayList<ResultModel> resultModelArrayList) {
        this.context = context;
        this.resultModelArrayList=resultModelArrayList;
    }

    public void start(View v){
        AlertDialog.Builder builderFact=new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        final View viewFact= LayoutInflater.from(context).inflate(R.layout.dialog_result,(ConstraintLayout) v.findViewById(R.id.layoutDialogContainer),false);
        builderFact.setView(viewFact);
        builderFact.setCancelable(false);

         alertDialog=builderFact.create();

        recyclerview = (RecyclerView) viewFact.findViewById(R.id.recyclerView);









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





    }





}



