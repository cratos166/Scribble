package com.nbird.scribble.UNIVERSAL.DIALOG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.nativead.NativeAd;
import com.nbird.scribble.DATA.AppString;
import com.nbird.scribble.MAIN_MENU.Activity.MainActivity;
import com.nbird.scribble.R;

public class QuitAskerDialog {

    Context context;




    AlertDialog alertDialog;
    NativeAd NATIVE_ADS;
    CountDownTimer countDownTimer;
    public QuitAskerDialog(Context context, CountDownTimer countDownTimer) {
        this.context = context;
        this.countDownTimer = countDownTimer;
    }

    public void start(View v){
        AlertDialog.Builder builderFact=new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        final View viewFact= LayoutInflater.from(context).inflate(R.layout.dialog_model_2,(ConstraintLayout) v.findViewById(R.id.layoutDialogContainer),false);
        builderFact.setView(viewFact);
        builderFact.setCancelable(false);

        alertDialog=builderFact.create();






        Button buttonNo = (Button) viewFact.findViewById(R.id.buttonNo);
        Button buttonYes = (Button) viewFact.findViewById(R.id.buttonYes);



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


        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    alertDialog.dismiss();
                }catch (Exception e){

                }
            }
        });


        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    countDownTimer.cancel();
                }catch (Exception e){

                }


                Intent intent=new Intent(context, MainActivity.class);
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });




        }










}


