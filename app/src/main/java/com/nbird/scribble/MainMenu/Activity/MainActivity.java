package com.nbird.scribble.MainMenu.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nbird.scribble.DATA.AppData;
import com.nbird.scribble.DATA.AppString;
import com.nbird.scribble.MainMenu.Model.PlayerInfo;
import com.nbird.scribble.R;
import com.nbird.scribble.UNIVERSAL.DIALOG.LoadingAlertDialog;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    CardView cardView1,cardView2,cardView3,cardView4,cardView5,cardView6,cardView7,cardView8,cardView9,cardView10;
    CardView cardView11,cardView12,cardView13,cardView14,cardView15,cardView16,cardView17,cardView18,cardView19,cardView20;
    CardView cardView21,cardView22,cardView23,cardView24,cardView25,cardView26;

    LinearLayout linearLayout1,linearLayout2,linearLayout3,linearLayout4,linearLayout5,linearLayout6,linearLayout7,linearLayout8,linearLayout9,linearLayout10;
    LinearLayout linearLayout11,linearLayout12,linearLayout13,linearLayout14,linearLayout15,linearLayout16,linearLayout17,linearLayout18,linearLayout19,linearLayout20;
    LinearLayout linearLayout21,linearLayout22,linearLayout23,linearLayout24,linearLayout25,linearLayout26;

    ImageView nav_image;
    TextInputEditText usernameEditText;



    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    boolean firstTime;

    Uri imageUri;

    StorageReference ref;
    FirebaseStorage storage;
    StorageReference storageReference;
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    String myNameStr,myPicStr,myUIDstr;

    TextView myNameTextView;
    ImageView imageView;

    LottieAnimationView profilebutton;

    Button play,how_to_play,about_us;



    AppData appData;

    ArrayList<CardView> listCardView;
    ArrayList<LinearLayout> listLinearLayout;
    ArrayList<String> listAvaURL;



    LoadingAlertDialog loadingAlertDialog;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listCardView=new ArrayList<>();
        listLinearLayout=new ArrayList<>();
        listAvaURL=new ArrayList<>();

        loadingAlertDialog=new LoadingAlertDialog(MainActivity.this);


        appData=new AppData(MainActivity.this);



        myNameTextView=(TextView) findViewById(R.id.myName);
        imageView=(ImageView) findViewById(R.id.imageView);
        profilebutton=(LottieAnimationView) findViewById(R.id.profilebutton);

        play=(Button) findViewById(R.id.play);
        how_to_play=(Button) findViewById(R.id.how_to_play);
        about_us=(Button) findViewById(R.id.about_us);




        firstTime=appData.getSharedPreferencesBoolean(AppString.SP_IS_FIRST);

        listAvaURL.add(AppString.urlAva1); listAvaURL.add(AppString.urlAva2); listAvaURL.add(AppString.urlAva3); listAvaURL.add(AppString.urlAva4); listAvaURL.add(AppString.urlAva5);
        listAvaURL.add(AppString.urlAva6); listAvaURL.add(AppString.urlAva7); listAvaURL.add(AppString.urlAva8); listAvaURL.add(AppString.urlAva9); listAvaURL.add(AppString.urlAva10);
        listAvaURL.add(AppString.urlAva11); listAvaURL.add(AppString.urlAva12); listAvaURL.add(AppString.urlAva13); listAvaURL.add(AppString.urlAva14); listAvaURL.add(AppString.urlAva15);
        listAvaURL.add(AppString.urlAva16); listAvaURL.add(AppString.urlAva17); listAvaURL.add(AppString.urlAva18); listAvaURL.add(AppString.urlAva19); listAvaURL.add(AppString.urlAva20);
        listAvaURL.add(AppString.urlAva21); listAvaURL.add(AppString.urlAva22); listAvaURL.add(AppString.urlAva23); listAvaURL.add(AppString.urlAva24); listAvaURL.add(AppString.urlAva25);
        listAvaURL.add(AppString.urlAva26);

        if(firstTime){
            Random r=new Random();
            int num=r.nextInt(100000000);
            myNameStr="Player"+num;

            appData.setSharedPreferencesString(AppString.SP_MY_NAME,myNameStr);


            appData.setSharedPreferencesString(AppString.SP_MY_PIC,AppString.defaultPic);

            myPicStr=AppString.defaultPic;


            myUIDstr= UUID.randomUUID().toString();
            appData.setSharedPreferencesString(AppString.SP_MY_UID,myUIDstr);








            preLoad();

            PlayerInfo playerInfo=new PlayerInfo(myNameStr,myPicStr,myUIDstr,false);

            loadingAlertDialog.showLoadingDialog();

            myRef.child("USER").child(myUIDstr).setValue(playerInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    loadingAlertDialog.dismissLoadingDialog();

                    appData.setSharedPreferencesBoolean(AppString.SP_IS_FIRST,false);
                    firstTime=false;
                    avatarSelectionDialog();

                }
            });




        }else{
            myNameStr=appData.getSharedPreferencesString(AppString.SP_MY_NAME,MainActivity.this);
            myPicStr=appData.getSharedPreferencesString(AppString.SP_MY_PIC,MainActivity.this);
            myUIDstr=appData.getSharedPreferencesString(AppString.SP_MY_UID,MainActivity.this);
        }
        myNameTextView.setText(myNameStr);
        Glide.with(getBaseContext()).load(myPicStr).apply(RequestOptions
                        .bitmapTransform(new RoundedCorners(14)))
                .into(imageView);


        profilebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    alertDialog.show();
                }catch (Exception e){
                    avatarSelectionDialog();
                }
            }
        });


    }

    public void avatarSelectionDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogTheme);

        final View view1= LayoutInflater.from(MainActivity.this).inflate(R.layout.profile_selection_layout,(ConstraintLayout) findViewById(R.id.layoutDialogContainer));
        builder.setView(view1);
        builder.setCancelable(true);




        nav_image=((ImageView) view1.findViewById(R.id.propic));




        usernameEditText=((TextInputEditText) view1.findViewById(R.id.username));
        usernameEditText.setText(myNameStr);

        cardView1= (CardView) view1.findViewById(R.id.cardview1);
        cardView2= (CardView) view1.findViewById(R.id.cardview2);
        cardView3= (CardView) view1.findViewById(R.id.cardview3);
        cardView4= (CardView) view1.findViewById(R.id.cardview4);
        cardView5= (CardView) view1.findViewById(R.id.cardview5);
        cardView6= (CardView) view1.findViewById(R.id.cardview6);
        cardView7= (CardView) view1.findViewById(R.id.cardview7);
        cardView8= (CardView) view1.findViewById(R.id.cardview8);
        cardView9= (CardView) view1.findViewById(R.id.cardview9);
        cardView10= (CardView) view1.findViewById(R.id.cardview10);
        cardView11= (CardView) view1.findViewById(R.id.cardview11);
        cardView12= (CardView) view1.findViewById(R.id.cardview12);
        cardView13= (CardView) view1.findViewById(R.id.cardview13);
        cardView14= (CardView) view1.findViewById(R.id.cardview14);
        cardView15= (CardView) view1.findViewById(R.id.cardview15);
        cardView16= (CardView) view1.findViewById(R.id.cardview16);
        cardView17= (CardView) view1.findViewById(R.id.cardview17);
        cardView18= (CardView) view1.findViewById(R.id.cardview18);
        cardView19= (CardView) view1.findViewById(R.id.cardview19);
        cardView20= (CardView) view1.findViewById(R.id.cardview20);
        cardView21= (CardView) view1.findViewById(R.id.cardview21);
        cardView22= (CardView) view1.findViewById(R.id.cardview22);
        cardView23= (CardView) view1.findViewById(R.id.cardview23);
        cardView24= (CardView) view1.findViewById(R.id.cardview24);
        cardView25= (CardView) view1.findViewById(R.id.cardview25);
        cardView26= (CardView) view1.findViewById(R.id.cardview26);


        listCardView.add(cardView1);listCardView.add(cardView2);listCardView.add(cardView3);listCardView.add(cardView4);listCardView.add(cardView5);listCardView.add(cardView6);listCardView.add(cardView7);
        listCardView.add(cardView8);listCardView.add(cardView9);listCardView.add(cardView10);listCardView.add(cardView11);listCardView.add(cardView12);listCardView.add(cardView13);listCardView.add(cardView14);
        listCardView.add(cardView15);listCardView.add(cardView16);listCardView.add(cardView17);listCardView.add(cardView18);listCardView.add(cardView19);listCardView.add(cardView20);listCardView.add(cardView21);
        listCardView.add(cardView22);listCardView.add(cardView23);listCardView.add(cardView24);listCardView.add(cardView25);listCardView.add(cardView26);

        linearLayout1=(LinearLayout) view1.findViewById(R.id.linearLayout1);
        linearLayout2=(LinearLayout) view1.findViewById(R.id.linearLayout2);
        linearLayout3=(LinearLayout) view1.findViewById(R.id.linearLayout3);
        linearLayout4=(LinearLayout) view1.findViewById(R.id.linearLayout4);
        linearLayout5=(LinearLayout) view1.findViewById(R.id.linearLayout5);
        linearLayout6=(LinearLayout) view1.findViewById(R.id.linearLayout6);
        linearLayout7=(LinearLayout) view1.findViewById(R.id.linearLayout7);
        linearLayout8=(LinearLayout) view1.findViewById(R.id.linearLayout8);
        linearLayout9=(LinearLayout) view1.findViewById(R.id.linearLayout9);
        linearLayout10=(LinearLayout) view1.findViewById(R.id.linearLayout10);
        linearLayout11=(LinearLayout) view1.findViewById(R.id.linearLayout11);
        linearLayout12=(LinearLayout) view1.findViewById(R.id.linearLayout12);
        linearLayout13=(LinearLayout) view1.findViewById(R.id.linearLayout13);
        linearLayout14=(LinearLayout) view1.findViewById(R.id.linearLayout14);
        linearLayout15=(LinearLayout) view1.findViewById(R.id.linearLayout15);
        linearLayout16=(LinearLayout) view1.findViewById(R.id.linearLayout16);
        linearLayout17=(LinearLayout) view1.findViewById(R.id.linearLayout17);
        linearLayout18=(LinearLayout) view1.findViewById(R.id.linearLayout18);
        linearLayout19=(LinearLayout) view1.findViewById(R.id.linearLayout19);
        linearLayout20=(LinearLayout) view1.findViewById(R.id.linearLayout20);
        linearLayout21=(LinearLayout) view1.findViewById(R.id.linearLayout21);
        linearLayout22=(LinearLayout) view1.findViewById(R.id.linearLayout22);
        linearLayout23=(LinearLayout) view1.findViewById(R.id.linearLayout23);
        linearLayout24=(LinearLayout) view1.findViewById(R.id.linearLayout24);
        linearLayout25=(LinearLayout) view1.findViewById(R.id.linearLayout25);
        linearLayout26=(LinearLayout) view1.findViewById(R.id.linearLayout26);


        listLinearLayout.add(linearLayout1);listLinearLayout.add(linearLayout2);listLinearLayout.add(linearLayout3);listLinearLayout.add(linearLayout4);listLinearLayout.add(linearLayout5);
        listLinearLayout.add(linearLayout6);listLinearLayout.add(linearLayout7);listLinearLayout.add(linearLayout8);listLinearLayout.add(linearLayout9);listLinearLayout.add(linearLayout10);
        listLinearLayout.add(linearLayout11);listLinearLayout.add(linearLayout12);listLinearLayout.add(linearLayout13);listLinearLayout.add(linearLayout14);listLinearLayout.add(linearLayout15);
        listLinearLayout.add(linearLayout16);listLinearLayout.add(linearLayout17);listLinearLayout.add(linearLayout18);listLinearLayout.add(linearLayout19);listLinearLayout.add(linearLayout20);
        listLinearLayout.add(linearLayout21);listLinearLayout.add(linearLayout22);listLinearLayout.add(linearLayout23);listLinearLayout.add(linearLayout24);listLinearLayout.add(linearLayout25);
        listLinearLayout.add(linearLayout26);


        if(!firstTime){
            if(myPicStr.equals(AppString.urlAva1)){
                linearLayout1.setBackgroundResource(R.drawable.uniform_button);
            }else if(myPicStr.equals(AppString.urlAva2)){
                linearLayout2.setBackgroundResource(R.drawable.uniform_button);
            }else if(myPicStr.equals(AppString.urlAva3)){
                linearLayout3.setBackgroundResource(R.drawable.uniform_button);
            }else if(myPicStr.equals(AppString.urlAva4)){
                linearLayout4.setBackgroundResource(R.drawable.uniform_button);
            }else if(myPicStr.equals(AppString.urlAva5)){
                linearLayout5.setBackgroundResource(R.drawable.uniform_button);
            }else if(myPicStr.equals(AppString.urlAva6)){
                linearLayout6.setBackgroundResource(R.drawable.uniform_button);
            }else if(myPicStr.equals(AppString.urlAva7)){
                linearLayout7.setBackgroundResource(R.drawable.uniform_button);
            }else if(myPicStr.equals(AppString.urlAva8)){
                linearLayout8.setBackgroundResource(R.drawable.uniform_button);
            }else if(myPicStr.equals(AppString.urlAva9)){
                linearLayout9.setBackgroundResource(R.drawable.uniform_button);
            }else if(myPicStr.equals(AppString.urlAva10)){
                linearLayout10.setBackgroundResource(R.drawable.uniform_button);
            }else if(myPicStr.equals(AppString.urlAva11)){
                linearLayout11.setBackgroundResource(R.drawable.uniform_button);
            }else if(myPicStr.equals(AppString.urlAva12)){
                linearLayout12.setBackgroundResource(R.drawable.uniform_button);
            }else if(myPicStr.equals(AppString.urlAva13)){
                linearLayout13.setBackgroundResource(R.drawable.uniform_button);
            }else if(myPicStr.equals(AppString.urlAva14)){
                linearLayout14.setBackgroundResource(R.drawable.uniform_button);
            }else if(myPicStr.equals(AppString.urlAva15)){
                linearLayout15.setBackgroundResource(R.drawable.uniform_button);
            }else if(myPicStr.equals(AppString.urlAva16)){
                linearLayout16.setBackgroundResource(R.drawable.uniform_button);
            }else if(myPicStr.equals(AppString.urlAva17)){
                linearLayout17.setBackgroundResource(R.drawable.uniform_button);
            }else if(myPicStr.equals(AppString.urlAva18)){
                linearLayout18.setBackgroundResource(R.drawable.uniform_button);
            }else if(myPicStr.equals(AppString.urlAva19)){
                linearLayout19.setBackgroundResource(R.drawable.uniform_button);
            }else if(myPicStr.equals(AppString.urlAva20)){
                linearLayout20.setBackgroundResource(R.drawable.uniform_button);
            }else if(myPicStr.equals(AppString.urlAva21)){
                linearLayout21.setBackgroundResource(R.drawable.uniform_button);
            }else if(myPicStr.equals(AppString.urlAva22)){
                linearLayout22.setBackgroundResource(R.drawable.uniform_button);
            }else if(myPicStr.equals(AppString.urlAva23)){
                linearLayout23.setBackgroundResource(R.drawable.uniform_button);
            }else if(myPicStr.equals(AppString.urlAva24)){
                linearLayout24.setBackgroundResource(R.drawable.uniform_button);
            }else if(myPicStr.equals(AppString.urlAva25)){
                linearLayout25.setBackgroundResource(R.drawable.uniform_button);
            }else if(myPicStr.equals(AppString.urlAva26)){
                linearLayout26.setBackgroundResource(R.drawable.uniform_button);
            }else{

                Glide.with(MainActivity.this)
                        .load(myPicStr)
                        .into(nav_image);
            }
        }else {

        }


        for(int i=0;i<listCardView.size();i++){
            int finalI = i;
            listCardView.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    settingDefault();
                    listLinearLayout.get(finalI).setBackgroundResource(R.drawable.uniform_button);
                    myPicStr=listAvaURL.get(finalI);
                }
            });
        }

        nav_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage(MainActivity.this);
            }
        });


         alertDialog=builder.create();
        if(alertDialog.getWindow()!=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        try{
            alertDialog.show();
        }catch (Exception e){

        }

        usernameEditText.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            usernameUploader(alertDialog);
                    }
                }
                return false;
            }
        });

        view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usernameUploader(alertDialog);
            }
        });

    }


    private void usernameUploader(AlertDialog alertDialog){

        if(username()) {

                PlayerInfo playerInfo=new PlayerInfo(usernameEditText.getText().toString(),myPicStr,myUIDstr,firstTime);


                loadingAlertDialog.showLoadingDialog();

                myRef.child("USER").child(myUIDstr).setValue(playerInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        loadingAlertDialog.dismissLoadingDialog();

                        alertDialog.dismiss();

                        appData.setSharedPreferencesString(AppString.SP_MY_NAME,usernameEditText.getText().toString());
                        appData.setSharedPreferencesString(AppString.SP_MY_PIC,myPicStr);
                        myNameStr=usernameEditText.getText().toString();
                        myNameTextView.setText(myNameStr);



                        Glide.with(getBaseContext()).load(myPicStr).apply(RequestOptions
                                        .bitmapTransform(new RoundedCorners(14)))
                                .into(imageView);

                    }
                });


        }
    }



    private void settingDefault(){
        for (int i=0;i<listLinearLayout.size();i++){
            listLinearLayout.get(i).setBackgroundResource(R.drawable.grey_black);
        }
    }


    private void selectImage(Context context) {
        final CharSequence[] options = {"Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

               /* if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } */if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
              /*  case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        nav_image.setImageBitmap(selectedImage);
                        uploadImage();
                    }
                    break;*/
                case 1:
                    if (resultCode == RESULT_OK) {
                        try {
                            imageUri = data.getData();
                            final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                            try{
                                nav_image.setBackgroundResource(0);
                                nav_image.setImageBitmap(selectedImage);
                            }catch (Exception e){

                            }


                            uploadImage();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                        }

                    }else {
                        Toast.makeText(MainActivity.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    }

    private void uploadImage()
    {



        if (imageUri != null) {

            try {
                settingDefault();
            }catch (Exception e){

            }


            // Code for showing progressDialog while uploading
            final ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            storage = FirebaseStorage.getInstance();
            storageReference = storage.getReference();




            ref = storageReference.child("USER_IMAGES/" + myUIDstr+"/");



            // adding listeners on upload
            // or failure of image
            ref.putFile(imageUri)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast.makeText(MainActivity.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                                    try{

                                        StorageReference urlref = storageRef.child("USER_IMAGES/" + myUIDstr+"/");
                                        urlref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                                        {
                                            @Override
                                            public void onSuccess(Uri downloadUrl)
                                            {

                                                myPicStr=downloadUrl.toString();
                                                Glide.with(getBaseContext()).load(myPicStr).apply(RequestOptions
                                                                .bitmapTransform(new RoundedCorners(14)))
                                                        .into(nav_image);
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
                                    .makeText(MainActivity.this,
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

    private boolean username(){
        String name1=usernameEditText.getText().toString();
        //String noWhihteSpaces=("\\A\\w{4,20}\\z");

        if(name1.isEmpty()){
            usernameEditText.setError("Field cannot be empty");
            return false;
        }else if(name1.length()>15){
            usernameEditText.setError("Username should be less than 15 characters");
            return false;
        }else if(name1.length()<3){
            usernameEditText.setError("Username should be more than 2 characters");
            return false;
        }
        else
            usernameEditText.setError(null);
        return true;
    }

    private void preLoad(){


        for(int i=0;i<26;i++){
            try{
                Glide.with(getBaseContext())
                        .load(listAvaURL.get(i)).error((Drawable) Glide.with(getBaseContext()).load(listAvaURL.get(i)).error((Drawable) Glide.with(getBaseContext()).load(listAvaURL.get(i)).error((Drawable) Glide.with(getBaseContext()).load(listAvaURL.get(i)).preload(20,10)).preload(20,10)).preload(20,10))
                        .preload(20, 10);
            }catch (Exception e){

            }
        }

    }

}