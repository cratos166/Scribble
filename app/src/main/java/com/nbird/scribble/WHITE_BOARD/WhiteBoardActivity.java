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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.slider.RangeSlider;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nbird.scribble.GUESS_DRAWING.GuessDrawingActivity;
import com.nbird.scribble.MAIN_MENU.Activity.MainActivity;
import com.nbird.scribble.R;
import com.nbird.scribble.WHITE_BOARD.Dialog.DialogSelectDrawingObject;
import com.nbird.scribble.WHITE_BOARD.DrawView.DrawView;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_white_board);

        arrayList=new ArrayList<>();

        Random random=new Random();

        for(int i=0;i<4;i++){
            int num=random.nextInt(10)+1;
            arrayList.add(num);
        }






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


        countDownTimer=new CountDownTimer(40*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerTexView.setText(String.valueOf(timer));
                timer--;
            }

            @Override
            public void onFinish() {

                Intent intent=new Intent(WhiteBoardActivity.this, GuessDrawingActivity.class);
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

}