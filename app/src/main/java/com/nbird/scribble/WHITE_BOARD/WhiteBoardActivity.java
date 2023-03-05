package com.nbird.scribble.WHITE_BOARD;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.slider.RangeSlider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nbird.scribble.DATA.AppString;
import com.nbird.scribble.DATA_MAKER.DataMakerActivity;
import com.nbird.scribble.GUESS_DRAWING.GuessDrawingActivity;
import com.nbird.scribble.GUESS_DRAWING.MODEL.DrawingDataModel;
import com.nbird.scribble.MAIN_MENU.Activity.MainActivity;
import com.nbird.scribble.R;
import com.nbird.scribble.UNIVERSAL.DIALOG.QuitAskerDialog;
import com.nbird.scribble.WHITE_BOARD.Dialog.DialogSelectDrawingObject;
import com.nbird.scribble.WHITE_BOARD.DrawView.DrawView;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Random;

import petrov.kristiyan.colorpicker.ColorPicker;

public class WhiteBoardActivity extends AppCompatActivity {
    private DrawView paint;
    CardView cardViewUndo, cardViewBrush, cardViewColor, cardViewSave;
    private int flipStroke;
    float strokeWidthRemember = 10;
    ArrayList<Integer> arrayList;
    TextView wordTextView,timerTexView;
    CountDownTimer countDownTimer;
    int timer=40;
    String myName,myImage,myUID;
    final static int TOTAL_OBJECT=200;

    StorageReference ref;
    FirebaseStorage storage;
    StorageReference storageReference;
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    String CATEGORY="USER_MAKER";

    ValueEventListener valueEventListener;

    int ObjectCount;
    NativeAd NATIVE_ADS;
    AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_white_board);

        arrayList=new ArrayList<>();

        Random random=new Random();

        for(int i=0;i<4;i++){
            int num=random.nextInt(TOTAL_OBJECT)+1;
            arrayList.add(num);
        }


        myName=getIntent().getStringExtra("myName");
        myImage=getIntent().getStringExtra("myImage");
        myUID=getIntent().getStringExtra("myUID");




        paint = (DrawView) findViewById(R.id.draw_view);

        ViewTreeObserver vto = paint.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                paint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width = paint.getMeasuredWidth();
                int height = paint.getMeasuredHeight();
                paint.init(height, width);
            }
        });


        cardViewUndo = (CardView) findViewById(R.id.card1);
        cardViewBrush = (CardView) findViewById(R.id.card4);
        cardViewColor = (CardView) findViewById(R.id.card3);
        cardViewSave = (CardView) findViewById(R.id.card2);

        wordTextView=(TextView) findViewById(R.id.wordTextView);
        timerTexView=(TextView) findViewById(R.id.timerTexView);



        mAdView = findViewById(R.id.adView);
        mAdView.setVisibility(View.VISIBLE);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        MobileAds.initialize(WhiteBoardActivity.this);
        AdLoader adLoaderd = new AdLoader.Builder(WhiteBoardActivity.this, AppString.NATIVE_ID)
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


        valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    ObjectCount=snapshot.getValue(Integer.class);
                }catch (Exception e){
                    ObjectCount=0;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        myRef.child("OBJECT_COUNT").addValueEventListener(valueEventListener);




        countDownTimer=new CountDownTimer(40*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerTexView.setText(String.valueOf(timer));
                timer--;
            }

            @Override
            public void onFinish() {


                if(!paint.isEmpty()){
                    uploadData();
                }



                Intent intent=new Intent(WhiteBoardActivity.this, GuessDrawingActivity.class);
                intent.putExtra("myName",myName);
                intent.putExtra("myImage",myImage);
                intent.putExtra("myUID",myUID);
                startActivity(intent);
                finish();

            }
        }.start();




        DialogSelectDrawingObject dialogSelectDrawingObject=new DialogSelectDrawingObject(WhiteBoardActivity.this,arrayList,wordTextView);
        dialogSelectDrawingObject.start(cardViewUndo);


        //the undo button will remove the most recent stroke from the canvas
        cardViewUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paint.undo();
            }
        });
        //the save button will save the current canvas which is actually a bitmap
        //in form of PNG, in the storage
        cardViewSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getStoragePermission();

            }

        });
        //the color button will allow the user to select the color of his brush
        cardViewColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ColorPicker colorPicker = new ColorPicker(WhiteBoardActivity.this);
                colorPicker.setOnFastChooseColorListener(new ColorPicker.OnFastChooseColorListener() {
                            @Override
                            public void setOnFastChooseColorListener(int position, int color) {
                                //get the integer value of color selected from the dialog box and
                                // set it as the stroke color
                                paint.setColor(color);

                            }

                            @Override
                            public void onCancel() {

                                colorPicker.dismissDialog();
                            }
                        })
                        //set the number of color columns you want  to show in dialog.
                        .setColumns(5)
                        //set a default color selected in the dialog
                        .setDefaultColorButton(Color.parseColor("#000000"))
                        .show();
            }
        });
        // the button will toggle the visibility of the RangeBar/RangeSlider
        flipStroke = 0;
        cardViewBrush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                brushAlertDialog();
           /*     if(flipStroke==0){

                    setStroke.setVisibility(View.VISIBLE);
                    flipStroke=1;
                }

                else{
                    setStroke.setVisibility(View.GONE);
                    flipStroke=0;
                }*/

            }
        });

    }


    private void uploadData(){
        Bitmap bmp = paint.save();
        //opening a OutputStream to write into the file
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        Random random=new Random();
        int kk=random.nextInt();
        String path = MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), bmp, String.valueOf(kk), null);
        Uri uri=Uri.parse(path);

        if (uri != null) {




            // Code for showing progressDialog while uploading
            final ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            storage = FirebaseStorage.getInstance();
            storageReference = storage.getReference();




            ref = storageReference.child("OBJECT/" + CATEGORY+"/"+wordTextView.getText().toString()+"/"+ObjectCount);



            // adding listeners on upload
            // or failure of image
            ref.putFile(uri)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    try{

                                        StorageReference urlref = storageRef.child("OBJECT/" + CATEGORY+"/"+wordTextView.getText().toString()+"/"+ObjectCount);
                                        urlref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                                        {
                                            @Override
                                            public void onSuccess(Uri downloadUrl)
                                            {

                                                String objImage=downloadUrl.toString();

                                                ObjectCount++;


                                                myRef.child("OBJECT_COUNT").setValue(ObjectCount).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        DrawingDataModel drawingDataModel=new DrawingDataModel(ObjectCount,wordTextView.getText().toString(),objImage);

                                                        myRef.child("OBJECT_DATA").child(String.valueOf(ObjectCount)).setValue(drawingDataModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                            }
                                                        });

                                                    }
                                                });

                                            }
                                        });

                                    }catch (Exception e){

                                    }
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(WhiteBoardActivity.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int)progress + "%");
                                }
                            });
        }


    }


    private void getStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted");
                //Begin
                saveDrawing();

            } else {

                Log.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted");
            //Begin
            saveDrawing();

        }

    }




    private void saveDrawing() {

        //Save drawn picture in phone's storage
        Toast.makeText(WhiteBoardActivity.this, "Drawing Saved!", Toast.LENGTH_SHORT).show();

        //getting the bitmap from DrawView class
        Bitmap bmp = paint.save();
        //opening a OutputStream to write into the file
        OutputStream imageOutStream = null;

        ContentValues cv = new ContentValues();
        //name of the file
        cv.put(MediaStore.Images.Media.DISPLAY_NAME, "drawing.png");
        //type of the file
        cv.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        //location of the file to be saved
        cv.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);

        //ge the Uri of the file which is to be v=created in the storage
        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);
        try {
            //open the output stream with the above uri
            imageOutStream = getContentResolver().openOutputStream(uri);
            //this method writes the files in storage
            bmp.compress(Bitmap.CompressFormat.PNG, 100, imageOutStream);


            //close the output stream after use
            imageOutStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void brushAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(WhiteBoardActivity.this, R.style.AlertDialogTheme);

        final View view1 = LayoutInflater.from(WhiteBoardActivity.this).inflate(R.layout.dialog_brush, (ConstraintLayout) findViewById(R.id.layoutDialogContainer));
        builder.setView(view1);
        builder.setCancelable(false);

        final AlertDialog alertDialog = builder.create();
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        try {
            alertDialog.show();
        } catch (Exception e) {

        }
        RangeSlider rangeSlider = (RangeSlider) view1.findViewById(R.id.rangebar);

        //set the range of the RangeSlider
        rangeSlider.setValueFrom(0.0f);
        rangeSlider.setValueTo(100.0f);

        rangeSlider.setValues(strokeWidthRemember);

        paint.setStrokeWidth((int) strokeWidthRemember);
        //adding a OnChangeListener which will change the stroke width
        //as soon as the user slides the slider
        rangeSlider.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                paint.setStrokeWidth((int) value);
                strokeWidthRemember = value;

            }
        });

        view1.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{mAdView.destroy();}catch (Exception e){}
        try{countDownTimer.cancel();}catch (Exception e){}

    }

    public void onBackPressed() {
        QuitAskerDialog dialog=new QuitAskerDialog(WhiteBoardActivity.this,countDownTimer);
        dialog.start(paint);
    }

}