package com.example.restadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class AdminFoodAddActivity extends AppCompatActivity {
    private Button chooseImage,saveButton;
    private EditText FoodName, FoodPrice, FoodDescription;
    private Spinner FoodItem;
    private ImageView imageView;
    private Uri imageUri;
    ProgressBar progressBar;
    private static final int IMAGE_REQUEST=1;

    DatabaseReference databaseReference;
    StorageReference storageReference;
    StorageTask uploadTask;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Admin ");
        setContentView(R.layout.activity_admin_food_add);





        //   choose Save button ...........................................................................................................
        saveButton=findViewById(R.id.SavePostButton_ID);


        //   choose edit text ...........................................................................................................
        FoodName = findViewById(R.id.FoodNameEditText_ID);
        FoodPrice = findViewById(R.id.FoodPriceEditText_ID);
        FoodDescription = findViewById(R.id.FoodDescriptionEditText_ID);
        FoodItem=findViewById(R.id.FoodItemSpinner_ID);

        //Image View Find................................................................................................................
        imageView = findViewById(R.id.ImageView_ID);

        progressBar=findViewById(R.id.ProgressBar_ID);




        //Firebase Data Base Store Data.........................


        databaseReference= FirebaseDatabase.getInstance().getReference("Food");
        storageReference = FirebaseStorage.getInstance().getReference("Food");

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Food =FoodName.getText().toString().trim();
                String Price =FoodPrice.getText().toString().trim();
                String Description =FoodDescription.getText().toString().trim();

                if(Food.isEmpty())
                {
                    FoodName.setError("please type food name");
                    FoodName.requestFocus();
                    return;
                }
                if(Price.isEmpty())
                {
                    FoodPrice.setError("please type food price");
                    FoodPrice.requestFocus();
                    return;
                }
                if(Description.isEmpty())
                {
                    FoodDescription.setError("please type food description");
                    FoodDescription.requestFocus();
                    return;
                }else{
                  //  String id = databaseReference.push().getKey();

                  //  ModelClass modelClass = new  ModelClass(FoodName.getText().toString(), FoodPrice.getText().toString(), FoodItem.getSelectedItem().toString(),FoodDescription.getText().toString());
                 //   databaseReference.child(id).setValue(modelClass);
                  //  Toast.makeText(getApplicationContext(),"Data Stored", Toast.LENGTH_SHORT).show();

                    if (uploadTask!=null  && uploadTask.isInProgress() ){
                        Toast.makeText(getApplicationContext(),"Uploading in progress",Toast.LENGTH_SHORT).show();
                    }else{
                        saveData();
                        progressBar.setVisibility(View.VISIBLE);

                    }

                    //FoodName.setText("");
                    //FoodPrice.setText("");
                    //FoodDescription.setText("");



                }




            }
        });

        //File Chooser ...............................................................................
        //   choose image button ...........................................................................................................
        chooseImage=findViewById(R.id.ChooseImageButton_ID);
        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

    }
    public String getFileExxtention(Uri imageUri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
    }
    private void saveData() {
        StorageReference ref = storageReference.child(System.currentTimeMillis()+"."+getFileExxtention(imageUri));
        ref.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content


                        Toast.makeText(getApplicationContext()," Stored successfully  ",Toast.LENGTH_LONG).show();



                        Task<Uri> urlTask=taskSnapshot.getStorage().getDownloadUrl();
                        while (!urlTask.isSuccessful());
                        Uri downloadUrl= urlTask.getResult();



                      //  ModelClass modelClass = new ModelClass(FoodName, downloadUrl.toString());
                        ModelClass modelClass = new  ModelClass(FoodName.getText().toString(), FoodPrice.getText().toString(), FoodItem.getSelectedItem().toString(),FoodDescription.getText().toString(),downloadUrl.toString());


                        String uploadId= databaseReference.push().getKey();
                        databaseReference.child(uploadId).setValue(modelClass);
                        progressBar.setVisibility(View.GONE);

                        FoodName.setText("");
                        FoodPrice.setText("");
                        FoodDescription.setText("");
                        imageView.setImageBitmap(null);

                        backToAdminFoodAddShow();


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        Toast.makeText(getApplicationContext(),"image is not storeg succesfuly",Toast.LENGTH_SHORT).show();

                    }
                });

    }

    private void backToAdminFoodAddShow() {
        Intent intent = new Intent(getApplicationContext(),AdminDataInsertUpdateDelete.class);
        startActivity(intent);
        finish();

    }

    private void openFileChooser() {
        Intent intent =new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==IMAGE_REQUEST && resultCode== RESULT_OK  && data !=null && data.getData() != null){
            imageUri= data.getData();
            Picasso.with(this).load(imageUri).into(imageView);
        }
    }
}
