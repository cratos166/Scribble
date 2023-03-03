package com.nbird.scribble.DATA_MAKER;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nbird.scribble.WHITE_BOARD.Model.ObjectModel;

public class JsonMaker {

    int min;
    int max;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    public JsonMaker() {
    }

    public JsonMaker(int min, int max) {
        this.min = min;
        this.max = max;
    }



    public void objectJSON(){
        for(int i=min;i<=max;i++){
            ObjectModel objectModel=new ObjectModel(i,"");
            myRef.child("Object").child(String.valueOf(i)).setValue(objectModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                }
            });
        }
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }




}
