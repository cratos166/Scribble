package com.nbird.scribble.GUESS_DRAWING;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nbird.scribble.DATA.AppString;
import com.nbird.scribble.GUESS_DRAWING.Adapter.ChatAdapter;
import com.nbird.scribble.GUESS_DRAWING.Adapter.WordsAdapter;
import com.nbird.scribble.GUESS_DRAWING.Dialog.DialogResult;
import com.nbird.scribble.GUESS_DRAWING.MODEL.ChatModel;
import com.nbird.scribble.GUESS_DRAWING.MODEL.DrawingDataModel;
import com.nbird.scribble.GUESS_DRAWING.MODEL.ResultModel;
import com.nbird.scribble.MAIN_MENU.Model.PlayerDetails;
import com.nbird.scribble.R;
import com.nbird.scribble.UNIVERSAL.DIALOG.LoadingAlertDialog;
import com.nbird.scribble.UNIVERSAL.DIALOG.QuitAskerDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;

public class GuessDrawingActivity extends AppCompatActivity {



    RecyclerView recyclerView2,wordsRecycler;
    LinearLayoutManager linearLayoutManager1;
    ChatAdapter chatAdapter;
    ArrayList<ChatModel> chatModelArrayList;
    TextInputEditText edit;
    Button send;
    ArrayList<DrawingDataModel> dataModelArrayList;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();


    int kk=0,currentPosition=0;

    ImageView picture;

    LoadingAlertDialog loadingAlertDialog;
    ArrayList<PlayerDetails> playerDetailsArrayList;

    String myName,myImage,myUID;

    CountDownTimer countDownTimer,countDownTimerWordsDisplay;
    TextView timerTexView;
    int timeSec=60;

    ArrayList<ResultModel> resultModelArrayList;

    HashMap<Integer,Boolean> myAns;
    HashMap<Integer,Integer> extraPoint;

    CardView shiftCard;

    ArrayList<String> wordsListArray;
    WordsAdapter wordsAdapter;
    int TOTAL_OBJECT=200;
    HashMap<Integer, String> mapWord;
    NativeAd NATIVE_ADS;
    AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_drawing);


        chatModelArrayList=new ArrayList<>();
        dataModelArrayList=new ArrayList<>();
        playerDetailsArrayList=new ArrayList<>();
        resultModelArrayList=new ArrayList<>();
        wordsListArray=new ArrayList<>();
        myAns=new HashMap<>();
        extraPoint=new HashMap<>();
        mapWord = new HashMap<>();

        for(int i=0;i<4;i++){
            extraPoint.put(i,0);
        }






        myAns.put(0,false); myAns.put(1,false); myAns.put(2,false);

        myName=getIntent().getStringExtra("myName");
        myImage=getIntent().getStringExtra("myImage");
        myUID=getIntent().getStringExtra("myUID");


        recyclerView2=(RecyclerView) findViewById(R.id.recyclerView2);
        wordsRecycler=(RecyclerView) findViewById(R.id.wordsRecycler);
        edit=(TextInputEditText) findViewById(R.id.edit);
        send=(Button) findViewById(R.id.send);
        picture=(ImageView) findViewById(R.id.picture);
        timerTexView=(TextView) findViewById(R.id.timerTexView);
        shiftCard=(CardView) findViewById(R.id.shiftCard);



        linearLayoutManager1 = new LinearLayoutManager(this);
        linearLayoutManager1.setStackFromEnd(true);
        linearLayoutManager1.setOrientation(recyclerView2.VERTICAL);
        recyclerView2.setLayoutManager(linearLayoutManager1);
        chatAdapter = new ChatAdapter(this, chatModelArrayList);
        recyclerView2.setAdapter(chatAdapter);


        loadingAlertDialog=new LoadingAlertDialog(GuessDrawingActivity.this);
        loadingAlertDialog.showLoadingDialog();


        LinearLayoutManager linearLayoutManagerWords = new LinearLayoutManager(this);
        linearLayoutManagerWords.setOrientation(wordsRecycler.HORIZONTAL);
        wordsRecycler.setLayoutManager(linearLayoutManagerWords);
        wordsAdapter = new WordsAdapter(this, wordsListArray);
        wordsRecycler.setAdapter(wordsAdapter);




        mAdView = findViewById(R.id.adView);
        mAdView.setVisibility(View.VISIBLE);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        MobileAds.initialize(GuessDrawingActivity.this);
        AdLoader adLoaderd = new AdLoader.Builder(GuessDrawingActivity.this, AppString.NATIVE_ID)
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {
                        ColorDrawable cd = new ColorDrawable(0x393F4E);

                        NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(cd).build();
                        TemplateView template = findViewById(R.id.my_template);
                        template.setStyles(styles);
                        template.setNativeAd(nativeAd);
                        template.setVisibility(View.VISIBLE);
                        NATIVE_ADS=nativeAd;
                    }
                })
                .build();

        adLoaderd.loadAd(new AdRequest.Builder().build());


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
                            if(edit.getText().toString().equals("")){edit.setError("Input field is empty!");}else{messageSender();}
                    }
                }
                return false;
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit.getText().toString().equals("")){edit.setError("Input field is empty!");}else{messageSender();}
            }
        });


        shiftCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatModel chatModel=new ChatModel(1,"You skipped the drawing of "+playerDetailsArrayList.get(currentPosition).getMyName());
                chatModelArrayList.add(chatModel);

                ChatModel chatModel5=new ChatModel(1,"The word was "+dataModelArrayList.get(currentPosition).getName());
                chatModelArrayList.add(chatModel5);

                myAns.put(currentPosition,false);

                currentPosition++;

                if(currentPosition!=3){
                    try{ countDownTimerWordsDisplay.cancel();}catch (Exception e){}
                    wordSetter();
                    ChatModel chatModel1=new ChatModel(3,"Guess the drawing by "+playerDetailsArrayList.get(currentPosition).getMyName());
                    chatModelArrayList.add(chatModel1);

                    try{
                        Glide.with(getBaseContext())
                                .load(dataModelArrayList.get(currentPosition).getImageUrl())
                                .error(Glide.with(getBaseContext()).load(dataModelArrayList.get(currentPosition).getImageUrl()).error(Glide.with(getBaseContext()).load(dataModelArrayList.get(0).getImageUrl()).error(Glide.with(getBaseContext()).load(dataModelArrayList.get(0).getImageUrl()))))
                                .into(picture);
                    }catch (Exception e){

                    }
                }else{
                    wordsListArray.clear();
                    wordsAdapter.notifyDataSetChanged();
                    picture.setBackgroundResource(R.color.white);
                    ChatModel chatModel1=new ChatModel(3,"Done");
                    chatModelArrayList.add(chatModel1);

                    ChatModel chatModel2=new ChatModel(3,"Please wait unit time is over");
                    chatModelArrayList.add(chatModel2);

                    ChatModel chatModel6=new ChatModel(1,"The word was "+dataModelArrayList.get(currentPosition).getName());
                    chatModelArrayList.add(chatModel6);

                    edit.setVisibility(View.GONE);
                    send.setVisibility(View.GONE);
                    shiftCard.setVisibility(View.GONE);

                }

                chatAdapter.notifyDataSetChanged();
                edit.setText("");
                recyclerView2.smoothScrollToPosition(recyclerView2.getAdapter().getItemCount());


            }
        });



        myRef.child("ROOM").child(myUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                        if(!myUID.equals(dataSnapshot.getValue(PlayerDetails.class).getUID())){
                            PlayerDetails playerDetails= dataSnapshot.getValue(PlayerDetails.class);
                            playerDetailsArrayList.add(playerDetails);
                        }
                    }

                    myRef.child("OBJ_COUNTER").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            TOTAL_OBJECT=snapshot.getValue(Integer.class);
                            objectDataGetter();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                ChatModel chatModel1=new ChatModel(3,"Guess the drawing by "+playerDetailsArrayList.get(currentPosition).getMyName());
                chatModelArrayList.add(chatModel1);
                chatAdapter.notifyDataSetChanged();
                recyclerView2.smoothScrollToPosition(recyclerView2.getAdapter().getItemCount());



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        countDownTimer=new CountDownTimer(timeSec*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerTexView.setText(String.valueOf(millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {




                String str1="";
                String str2="";
                String str3="";
                String p1="+0",p2="+0",p3="+0";

                int totalPoints=0;



                if(myAns.get(0)){
                    str1="Your guessed the drawing of "+playerDetailsArrayList.get(0).getMyName();
                    p1="+10";
                    totalPoints+=10;
                    extraPoint.put(1,extraPoint.get(1)+10);
                }else{
                    str1="You were unable to guess the drawing of "+playerDetailsArrayList.get(0).getMyName();
                }

                if(myAns.get(1)){
                    str2="Your guessed the drawing of "+playerDetailsArrayList.get(1).getMyName();
                    p2="+10";
                    totalPoints+=10;
                    extraPoint.put(2,extraPoint.get(2)+10);
                }else{
                    str2="You were unable to guess the drawing of "+playerDetailsArrayList.get(1).getMyName();
                }

                if(myAns.get(2)){
                    str3="Your guessed the drawing of "+playerDetailsArrayList.get(2).getMyName();
                    p3="+10";
                    totalPoints+=10;
                    extraPoint.put(3,extraPoint.get(3)+10);
                }else{
                    str3="You were unable to guess the drawing of "+playerDetailsArrayList.get(2).getMyName();
                }

                ResultModel resultModel =new ResultModel(myName,myImage,str1,str2,str3,p1,p2,p3,totalPoints,0);



                resultModelArrayList.add(resultModel);


                for(int i=0;i<playerDetailsArrayList.size();i++){

                    Boolean b1,b2,b3;
                    int sc=0;
                    String l1="+0",l2="+0",l3="+0";
                    String gp1,gp2,gp3;


                    Random random=new Random();

                    b1=random.nextBoolean();
                    if(b1){
                        gp1="Your guessed the drawing of "+myName;
                        sc+=10;
                        l1="+10";
                        extraPoint.put(0,extraPoint.get(0)+10);
                    }else{
                        gp1="You were unable to guess the drawing of "+myName;
                    }


                    b2=random.nextBoolean();
                    if(b2){
                        if(i==0){
                            gp2="Your guessed the drawing of "+playerDetailsArrayList.get(1).getMyName();
                            extraPoint.put(2,extraPoint.get(2)+10);
                        }else if(i==1){
                            gp2="Your guessed the drawing of "+playerDetailsArrayList.get(2).getMyName();
                            extraPoint.put(3,extraPoint.get(3)+10);
                        }else{
                            gp2="Your guessed the drawing of "+playerDetailsArrayList.get(0).getMyName();
                            extraPoint.put(1,extraPoint.get(1)+10);
                        }

                        sc+=10;
                        l2="+10";
                    }else{
                        if(i==0){
                            gp2="You were unable to guess the drawing of "+playerDetailsArrayList.get(1).getMyName();

                        }else if(i==1){
                            gp2="You were unable to guess the drawing of "+playerDetailsArrayList.get(2).getMyName();

                        }else{
                            gp2="You were unable to guess the drawing of "+playerDetailsArrayList.get(0).getMyName();

                        }

                    }

                    b3=random.nextBoolean();
                    if(b3){
                        if(i==0){
                            gp3="Your guessed the drawing of "+playerDetailsArrayList.get(2).getMyName();
                            extraPoint.put(3,extraPoint.get(3)+10);
                        }else if(i==1){
                            gp3="Your guessed the drawing of "+playerDetailsArrayList.get(0).getMyName();
                            extraPoint.put(1,extraPoint.get(1)+10);
                        }else{
                            gp3="Your guessed the drawing of "+playerDetailsArrayList.get(1).getMyName();
                            extraPoint.put(2,extraPoint.get(2)+10);
                        }
                        sc+=10;
                        l3="+10";
                    }else{
                        if(i==0){
                            gp3="You were unable to guess the drawing of "+playerDetailsArrayList.get(2).getMyName();
                        }else if(i==1){
                            gp3="You were unable to guess the drawing of "+playerDetailsArrayList.get(0).getMyName();
                        }else{
                            gp3="You were unable to guess the drawing of "+playerDetailsArrayList.get(1).getMyName();
                        }
                    }




                    ResultModel result =new ResultModel(playerDetailsArrayList.get(i).getMyName(),playerDetailsArrayList.get(i).getMyImage(),gp1,gp2,gp3,l1,l2,l3,sc,0);
                    resultModelArrayList.add(result);
                }


                resultModelArrayList.get(0).setPoints(resultModelArrayList.get(0).getPoints()+extraPoint.get(0));
                resultModelArrayList.get(1).setPoints(resultModelArrayList.get(1).getPoints()+extraPoint.get(1));
                resultModelArrayList.get(2).setPoints(resultModelArrayList.get(2).getPoints()+extraPoint.get(2));
                resultModelArrayList.get(3).setPoints(resultModelArrayList.get(3).getPoints()+extraPoint.get(3));


                resultModelArrayList.get(0).setExtraPoint(extraPoint.get(0));
                resultModelArrayList.get(1).setExtraPoint(extraPoint.get(1));
                resultModelArrayList.get(2).setExtraPoint(extraPoint.get(2));
                resultModelArrayList.get(3).setExtraPoint(extraPoint.get(3));


                resultComparator();
                Collections.reverse(resultModelArrayList);




                for (int i = 0; i < resultModelArrayList.size(); i++) {
                    Log.i("getImageURL", String.valueOf(resultModelArrayList.get(i).getImageURL()));
                    Log.i("getPlayer2Str", String.valueOf(resultModelArrayList.get(i).getPlayer2Str()));
                    Log.i("getPlayer3Str", String.valueOf(resultModelArrayList.get(i).getPlayer3Str()));
                    Log.i("getPlayer4Str", String.valueOf(resultModelArrayList.get(i).getPlayer4Str()));

                    Log.i("getPoint2", String.valueOf(resultModelArrayList.get(i).getPoint2()));
                    Log.i("getPoint3", String.valueOf(resultModelArrayList.get(i).getPoint3()));
                    Log.i("getPoint4", String.valueOf(resultModelArrayList.get(i).getPoint4()));
                    Log.i("getName", String.valueOf(resultModelArrayList.get(i).getName()));

                    Log.i("getPoints", String.valueOf(resultModelArrayList.get(i).getPoints()));

                }



                DialogResult dialogResult=new DialogResult(GuessDrawingActivity.this,resultModelArrayList,myName,myImage,myUID);
                dialogResult.start(send);



            }
        }.start();


    }


    private void wordSetter(){
        mapWord.clear();
        wordsListArray.clear();
        int num = dataModelArrayList.get(currentPosition).getName().length();
        for (int i = 0; i < num; i++) {
            wordsListArray.add(" _ ");
        }
        wordsAdapter.notifyDataSetChanged();

        int k = 7;

        characterReveal(k, dataModelArrayList.get(currentPosition).getName(), num, mapWord);

    }


    private void characterReveal(int k, String str, int num, HashMap<Integer, String> mapWord) {
        countDownTimerWordsDisplay = new CountDownTimer(1000 * k, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                wordsListArray.clear();

                Random random = new Random();
                int anInt = random.nextInt(str.length() - 1);
                char ch = str.charAt(anInt);

                mapWord.put(anInt, String.valueOf(ch));


                for (int i = 0; i < num; i++) {
                    try {
                        if (mapWord.get(i).equals(null)) {
                            wordsListArray.add(" _ ");
                        } else {
                            wordsListArray.add(" " + mapWord.get(i) + " ");
                        }
                    } catch (Exception e) {
                        wordsListArray.add(" _ ");
                    }
                }
                wordsAdapter.notifyDataSetChanged();
                characterReveal(k, str, num, mapWord);
            }
        }.start();
    }

    private void resultComparator() {

        Collections.sort(resultModelArrayList, new Comparator<ResultModel>() {
            @Override
            public int compare(ResultModel a1, ResultModel a2) {
                return a1.getPoints() - a2.getPoints();
            }
        });

    }

    private void messageSender(){
        if(equalIgnoreCase(edit.getText().toString().trim(),dataModelArrayList.get(currentPosition).getName())){
            ChatModel chatModel=new ChatModel(1,"You guessed the word!");
            chatModelArrayList.add(chatModel);

            myAns.put(currentPosition,true);

            currentPosition++;

            if(currentPosition!=3){

                try{ countDownTimerWordsDisplay.cancel();}catch (Exception e){}
                wordSetter();




                ChatModel chatModel1=new ChatModel(3,"Guess the drawing by "+playerDetailsArrayList.get(currentPosition).getMyName());
                chatModelArrayList.add(chatModel1);

                try{
                    Glide.with(getBaseContext())
                            .load(dataModelArrayList.get(currentPosition).getImageUrl())
                            .error(Glide.with(getBaseContext()).load(dataModelArrayList.get(currentPosition).getImageUrl()).error(Glide.with(getBaseContext()).load(dataModelArrayList.get(0).getImageUrl()).error(Glide.with(getBaseContext()).load(dataModelArrayList.get(0).getImageUrl()))))
                            .into(picture);
                }catch (Exception e){

                }
            }else{
                wordsListArray.clear();
                wordsAdapter.notifyDataSetChanged();
                picture.setBackgroundResource(R.color.white);
                ChatModel chatModel1=new ChatModel(3,"Done");
                chatModelArrayList.add(chatModel1);

                ChatModel chatModel2=new ChatModel(3,"Please wait unit time is over");
                chatModelArrayList.add(chatModel2);

                edit.setVisibility(View.GONE);
                send.setVisibility(View.GONE);
                shiftCard.setVisibility(View.GONE);

            }


        }else{
            ChatModel chatModel=new ChatModel(2,edit.getText().toString().trim());
            chatModelArrayList.add(chatModel);
        }
        chatAdapter.notifyDataSetChanged();
        edit.setText("");
        recyclerView2.smoothScrollToPosition(recyclerView2.getAdapter().getItemCount());
    }

    private void objectDataGetter(){

        Random random=new Random();

        for(int i=0;i<3;i++){
            int num=random.nextInt(TOTAL_OBJECT)+1;
            int finalI = i;
            myRef.child("OBJECT_DATA").child(String.valueOf(num)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    dataModelArrayList.add(snapshot.getValue(DrawingDataModel.class));
                    kk++;

                    try{
                        Glide.with(getBaseContext())
                                .load(dataModelArrayList.get(finalI).getImageUrl()).error((Drawable) Glide.with(getBaseContext()).load(dataModelArrayList.get(finalI).getImageUrl()).error((Drawable) Glide.with(getBaseContext()).load(dataModelArrayList.get(finalI).getImageUrl()).error((Drawable) Glide.with(getBaseContext()).load(dataModelArrayList.get(finalI).getImageUrl()).preload(20,10)).preload(20,10)).preload(20,10))
                                .preload(20, 10);
                    }catch (Exception e){

                    }

                    if(kk==3){
                        loadingAlertDialog.dismissLoadingDialog();
                        try{
                            Glide.with(getBaseContext())
                                    .load(dataModelArrayList.get(0).getImageUrl())
                                    .error(Glide.with(getBaseContext()).load(dataModelArrayList.get(currentPosition).getImageUrl()).error(Glide.with(getBaseContext()).load(dataModelArrayList.get(0).getImageUrl()).error(Glide.with(getBaseContext()).load(dataModelArrayList.get(0).getImageUrl()))))
                                    .into(picture);
                        }catch (Exception e){

                        }

                        wordSetter();


                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }




    }


    private boolean equalIgnoreCase(String str1, String str2)
    {
        int i = 0;

        try {

            // length of first string
            int len1 = str1.length();

            // length of second string
            int len2 = str2.length();

            // if length is not same
            // simply return false since both string
            // can not be same if length is not equal
            if (len1 != len2)
                return false;

            // loop to match one by one
            // all characters of both string
            while (i < len1) {

                // if current characters of both string are same,
                // increase value of i to compare next character
                if (str1.charAt(i) == str2.charAt(i)) {
                    i++;
                }

                // if any character of first string
                // is some special character
                // or numeric character and
                // not same as corresponding character
                // of second string then return false
                else if (!((str1.charAt(i) >= 'a' && str1.charAt(i) <= 'z')
                        || (str1.charAt(i) >= 'A' && str1.charAt(i) <= 'Z'))) {
                    return false;
                }

                // do the same for second string
                else if (!((str2.charAt(i) >= 'a' && str2.charAt(i) <= 'z')
                        || (str2.charAt(i) >= 'A' && str2.charAt(i) <= 'Z'))) {
                    return false;
                }

                // this block of code will be executed
                // if characters of both strings
                // are of different cases
                else {
                    // compare characters by ASCII value
                    if (str1.charAt(i) >= 'a' && str1.charAt(i) <= 'z') {
                        if (str1.charAt(i) - 32 != str2.charAt(i))
                            return false;
                    } else if (str1.charAt(i) >= 'A' && str1.charAt(i) <= 'Z') {
                        if (str1.charAt(i) + 32 != str2.charAt(i))
                            return false;
                    }

                    // if characters matched,
                    // increase the value of i to compare next char
                    i++;

                } // end of outer else block

            } // end of while loop

            // if all characters of the first string
            // are matched with corresponding
            // characters of the second string,
            // then return true
            return true;
        }
        catch (Exception e)
        {
            Log.v(ChatAdapter.class.getSimpleName(),e.getMessage());
            return false;
        }

    } // end of equalIgnoreCase function


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{mAdView.destroy();}catch (Exception e){}
        try{countDownTimer.cancel();}catch (Exception e){}
        try{countDownTimerWordsDisplay.cancel();}catch (Exception e){}


    }

    public void onBackPressed() {
        QuitAskerDialog dialog=new QuitAskerDialog(GuessDrawingActivity.this, countDownTimer);
        dialog.start(edit);
    }

}