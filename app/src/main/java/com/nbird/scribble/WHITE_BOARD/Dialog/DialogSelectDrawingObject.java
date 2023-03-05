package com.nbird.scribble.WHITE_BOARD.Dialog;

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
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nbird.scribble.DATA.AppString;
import com.nbird.scribble.R;
import com.nbird.scribble.WHITE_BOARD.Adapter.ObjectAdapter;
import com.nbird.scribble.WHITE_BOARD.Model.ObjectModel;

import java.util.ArrayList;

public class DialogSelectDrawingObject {

    Context context;

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference table_user = database.getReference();
    FirebaseAuth mAuth= FirebaseAuth.getInstance();

    RecyclerView recyclerview;

    ArrayList<ObjectModel> objectData;

    ObjectAdapter myAdapter;



    ArrayList<Integer> arrayList;
    TextView headingTextView;
    AlertDialog alertDialog;

    int totalObject=0;
    TextView wordTextView;
    NativeAd NATIVE_ADS;
    public DialogSelectDrawingObject(Context context, ArrayList<Integer> arrayList, TextView wordTextView) {
        this.context = context;
        this.arrayList=arrayList;
        this.wordTextView=wordTextView;
    }

    public void start(View v){
        AlertDialog.Builder builderFact=new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        final View viewFact= LayoutInflater.from(context).inflate(R.layout.dialog_select_object,(ConstraintLayout) v.findViewById(R.id.layoutDialogContainer),false);
        builderFact.setView(viewFact);
        builderFact.setCancelable(false);

         alertDialog=builderFact.create();

        recyclerview = (RecyclerView) viewFact.findViewById(R.id.recyclerView);
        ImageView cancelButton = (ImageView) viewFact.findViewById(R.id.cancel);
        headingTextView=(TextView) viewFact.findViewById(R.id.textTitle);


        objectData=new ArrayList<>();



        MobileAds.initialize(context);
        AdLoader adLoader = new AdLoader.Builder(context, AppString.NATIVE_ID)
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {
                        ColorDrawable cd = new ColorDrawable(0x393F4E);

                        NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(cd).build();
                        TemplateView template = viewFact.findViewById(R.id.my_template);
                        template.setStyles(styles);
                        template.setNativeAd(nativeAd);
                        template.setVisibility(View.VISIBLE);
                        NATIVE_ADS=nativeAd;
                    }
                })
                .build();

        adLoader.loadAd(new AdRequest.Builder().build());


        if(alertDialog.getWindow()!=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        try{
            alertDialog.show();
        }catch (Exception e){

        }

        myAdapter=new ObjectAdapter(context,objectData,wordTextView,alertDialog);
        recyclerview.setLayoutManager(new GridLayoutManager(context,2));
        recyclerview.setAdapter(myAdapter);



        for(int i=0;i<4;i++){
            table_user.child("Object").child(String.valueOf(arrayList.get(i))).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try{
                        totalObject++;
                        objectData.add(snapshot.getValue(ObjectModel.class));
                        if(totalObject==4){
                            myAdapter.notifyDataSetChanged();
                        }
                    }catch (Exception e){

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }






    }





}



