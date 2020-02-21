package com.example.restadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Intent;
import android.icu.text.Transliterator;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdminShowFood extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private List<ModelClass> modelClassList;

    DatabaseReference databaseReference;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    private static final int IMAGE_REQUEST=1;
    private Uri imageUri;


    private EditText Foodname ;
    private EditText FoodPrice ;
    private EditText FoodDescription ;
    private Spinner FoodSpinner ;
    private Button ChooseImage ;
    private Button Update ;
    private ImageView imageView;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Food List");
        setContentView(R.layout.activity_admin_show_food);


        recyclerView=findViewById(R.id.BurgerRecyclerView_ID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        modelClassList= new ArrayList<>();

        firebaseStorage = FirebaseStorage.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("Food");



        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                modelClassList.clear();

                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    ModelClass modelClass = dataSnapshot1.getValue(ModelClass.class);
                    modelClass.setKey(dataSnapshot1.getKey());

                    modelClassList.add(modelClass);
                }
                myAdapter= new MyAdapter(AdminShowFood.this, modelClassList);
                recyclerView.setAdapter(myAdapter);

                myAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
                    @Override
                    public void OnItemClick(int position) {
                        String text = modelClassList.get(position).getFoodName();
                        Toast.makeText(getApplicationContext(),text+"is selected "+position,Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onDoAnyTask(int position) {
                        ModelClass selectedItem = modelClassList.get(position);

                        showUpdateDialog(selectedItem.getKey(), position);


                    }

                    @Override
                    public void onDelete(int position) {

                        ModelClass selectedItem = modelClassList.get(position);
                        final String key= selectedItem.getKey();

                        StorageReference storageReference = firebaseStorage.getReferenceFromUrl(selectedItem.getImageUrl());
                        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                databaseReference.child(key).removeValue();
                            }
                        });


                    }
                });




                //,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getApplicationContext()," Unsuccessful", Toast.LENGTH_SHORT).show();
            }
        });




    }

    private void showUpdateDialog(final String Key, int position) {
        AlertDialog.Builder alertdailog = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.update_dialog, null, false);
        alertdailog.setView(view);

         final AlertDialog alert = alertdailog.show();

         Foodname =(EditText)view.findViewById(R.id.Update_FoodNameEditText_ID);
         FoodPrice =(EditText)view.findViewById(R.id.Update_FoodPriceEditText_ID);
         FoodDescription =(EditText)view.findViewById(R.id.Update_FoodDescriptionEditText_ID);
         FoodSpinner =(Spinner)view.findViewById(R.id.Update_FoodItemSpinner_ID);
         ChooseImage =(Button)view.findViewById(R.id.Update_ChooseImageButton_ID);
         Update =(Button)view.findViewById(R.id.Update_SavePostButton_ID);
         imageView=(ImageView)view.findViewById(R.id.Update_ImageView_ID);
         progressBar=(ProgressBar)view.findViewById(R.id.Update_ProgressBar_ID);



         //set value...............................................................


        ModelClass modelClass1 = modelClassList.get(position);
        Foodname.setText(modelClass1.getFoodName());
        FoodPrice.setText(modelClass1.getFoodPrice());
        FoodDescription.setText(modelClass1.getFoodDescription());









        String[] fooditem=getResources().getStringArray(R.array.Food_Item);
        ArrayAdapter<String>arrayAdapter= new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,fooditem);
        FoodSpinner.setAdapter(arrayAdapter);

        int x = arrayAdapter.getPosition(modelClass1.getFoodType());
        FoodSpinner.setSelection(x);


//....................................................................................



        ChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        //button update value replacement ...............................
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                String Food =Foodname.getText().toString().trim();
                String Price =FoodPrice.getText().toString().trim();
                String Description =FoodDescription.getText().toString().trim();


                if(Food.isEmpty())
                {
                    Foodname.setError("please type food name");
                    Foodname.requestFocus();
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

                }
               else {
                    progressBar.setVisibility(View.VISIBLE);
                    databaseReference = FirebaseDatabase.getInstance().getReference("Food");
                    storageReference = FirebaseStorage.getInstance().getReference("Food");




                   // Toast.makeText(getApplicationContext()," update successful", Toast.LENGTH_LONG).show();


                    //..................................................................................................
                    StorageReference ref = storageReference.child(Key+"."+getFileExxtention(imageUri));
                    ref.putFile(imageUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // Get a URL to the uploaded content


                                    Toast.makeText(getApplicationContext()," Update successfully  ",Toast.LENGTH_LONG).show();

                                    progressBar.setVisibility(View.GONE);
                                    alert.dismiss();

                                    Task<Uri> urlTask=taskSnapshot.getStorage().getDownloadUrl();
                                    while (!urlTask.isSuccessful());
                                    Uri downloadUrl= urlTask.getResult();
                                    final ModelClass modelClass = new ModelClass(Foodname.getText().toString(), FoodPrice.getText().toString(), FoodSpinner.getSelectedItem().toString(),FoodDescription.getText().toString(),downloadUrl.toString());


                                    databaseReference.child(Key).setValue(modelClass);



                                    databaseReference.child(Key).setValue(modelClass);


                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    Toast.makeText(getApplicationContext(),"image is not storeg succesfuly",Toast.LENGTH_SHORT).show();

                                }
                            });



//..........................................................................................................




                }//else













            }
        });



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
    public String getFileExxtention(Uri imageUri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
    }

}
