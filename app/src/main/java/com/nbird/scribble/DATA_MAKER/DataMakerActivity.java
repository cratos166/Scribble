package com.nbird.scribble.DATA_MAKER;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nbird.scribble.GUESS_DRAWING.GuessDrawingActivity;
import com.nbird.scribble.GUESS_DRAWING.MODEL.DrawingDataModel;
import com.nbird.scribble.MAIN_MENU.Activity.MainActivity;
import com.nbird.scribble.R;
import com.nbird.scribble.WHITE_BOARD.Dialog.DialogSelectDrawingObject;
import com.nbird.scribble.WHITE_BOARD.DrawView.DrawView;
import com.nbird.scribble.WHITE_BOARD.WhiteBoardActivity;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Random;

import petrov.kristiyan.colorpicker.ColorPicker;

public class DataMakerActivity extends AppCompatActivity {
    private DrawView paint;
    CardView cardViewUndo, cardViewBrush, cardViewColor, cardViewSave;
    private int flipStroke;
    float strokeWidthRemember = 10;
    TextView wordTextView;

    TextInputEditText edit;
    Button send;

    StorageReference ref;
    FirebaseStorage storage;
    StorageReference storageReference;
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();


    String CATEGORY="FRUITS";

    ValueEventListener valueEventListener;

    int ObjectCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_maker);



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
        edit=(TextInputEditText) findViewById(R.id.edit);
        send=(Button) findViewById(R.id.send);


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
                            wordTextView.setText(edit.getText().toString());
                            uploadData();
                    }
                }
                return false;
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wordTextView.setText(edit.getText().toString());
                uploadData();
            }
        });




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


                paint.clearDrawing();


            }

        });
        //the color button will allow the user to select the color of his brush
        cardViewColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ColorPicker colorPicker = new ColorPicker(DataMakerActivity.this);
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
        String path = MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), bmp, "Title", null);
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




            ref = storageReference.child("OBJECT/" + CATEGORY+"/"+edit.getText().toString()+"/"+ObjectCount);



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
                                    Toast.makeText(DataMakerActivity.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                                    try{

                                        StorageReference urlref = storageRef.child("OBJECT/" + CATEGORY+"/"+edit.getText().toString()+"/"+ObjectCount);
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
                                                                    DrawingDataModel drawingDataModel=new DrawingDataModel(ObjectCount,edit.getText().toString(),objImage);

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
                                    .makeText(DataMakerActivity.this,
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


    private void brushAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DataMakerActivity.this, R.style.AlertDialogTheme);

        final View view1 = LayoutInflater.from(DataMakerActivity.this).inflate(R.layout.dialog_brush, (ConstraintLayout) findViewById(R.id.layoutDialogContainer));
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