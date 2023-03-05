package com.nbird.scribble.GUESS_DRAWING.Dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.util.Log;
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

import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nbird.scribble.DATA.AppData;
import com.nbird.scribble.DATA.AppString;
import com.nbird.scribble.GUESS_DRAWING.Adapter.ChatAdapter;
import com.nbird.scribble.GUESS_DRAWING.Adapter.ResultAdapter;
import com.nbird.scribble.GUESS_DRAWING.MODEL.ResultModel;
import com.nbird.scribble.MAIN_MENU.Activity.MainActivity;
import com.nbird.scribble.R;
import com.nbird.scribble.WHITE_BOARD.Adapter.ObjectAdapter;
import com.nbird.scribble.WHITE_BOARD.Model.ObjectModel;
import com.nbird.scribble.WHITE_BOARD.WhiteBoardActivity;

import java.util.ArrayList;
import java.util.Random;

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


    private InterstitialAd mInterstitialAd;
    NativeAd NATIVE_ADS;
    private void loadAds(){


        String key= AppString.INTERSTITIAL_ID;

        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(context, key, adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i("TAG", "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.d("TAG", loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });


    }



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

        final int[] sec = {30};



        Random random=new Random();
        int num=random.nextInt(3);

        if(num==1){
            loadAds();
        }



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



        countDownTimer=new CountDownTimer(30*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                sec[0]--;
                next.setText("Next game starts in "+ sec[0] +" sec");
            }

            @Override
            public void onFinish() {



                if(mInterstitialAd!=null) {mInterstitialAd.show((Activity) context);
                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {super.onAdFailedToShowFullScreenContent(adError);intentFun();}
                        @Override public void onAdDismissedFullScreenContent() {super.onAdDismissedFullScreenContent();intentFun();}
                    });
                }else{intentFun();}



            }
        }.start();




        MobileAds.initialize(context);
        AdLoader adLoaderd = new AdLoader.Builder(context, AppString.NATIVE_ID)
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

        adLoaderd.loadAd(new AdRequest.Builder().build());




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


    public void intentFun(){
        try{
            countDownTimer.cancel();
        }catch (Exception e){

        }

        Intent intent=new Intent(context, WhiteBoardActivity.class);
        intent.putExtra("myName",myName);
        intent.putExtra("myImage",myImage);
        intent.putExtra("myUID",myUID);
        context.startActivity(intent);
        ((Activity) context).finish();
    }



}



