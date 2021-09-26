package com.sawitku;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sawitku.fmodels.Questions;

import java.io.IOException;

import fr.ganfra.materialspinner.MaterialSpinner;

public class CreateQuestion extends AppCompatActivity implements View.OnClickListener  {
    /* deklarasi untuk upload file */

    //constant to track image chooser intent
    private static final int PICK_IMAGE_REQUEST = 234;

    private FirebaseAuth auth;


    //view objects
    private Button buttonChoose;
    private Button buttonUpload;
    private EditText editTextName;
    private TextView textViewShow;
    private ImageView imageView;

    //uri to store file
    private Uri filePath;

    //firebase objects
    private StorageReference storageReference;
    private DatabaseReference mDatabase;

    String urlFotoFirebase = "";

    /* deklarasi untuk upload file */

    EditText userInput;

    String NamaUser = "";
    String profil_picture = "";
    String provinsi = "";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);





        //get firebase auth instance
        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();

        userInput = (EditText)findViewById(R.id.input_pertanyaan);



        //  Inialisasi Element UI untuk update
        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        imageView = (ImageView) findViewById(R.id.imageView);
        storageReference = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);
        buttonUpload.setOnClickListener(CreateQuestion.this);
        //  Inialisasi Element UI untuk update




        DatabaseReference qry = FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid()).getRef();

        final ProgressDialog Dialog = new ProgressDialog(CreateQuestion.this);
        Dialog.setMessage("Please Wait...");
        Dialog.show();

        qry.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {



                if(snapshot.child("nama").exists()){
                   NamaUser =  snapshot.child("nama").getValue().toString();
                }else{
                    NamaUser = user.getEmail();
                }


                if(snapshot.child("provinsi").exists()){
                    provinsi =  snapshot.child("provinsi").getValue().toString();
                }else{
                    provinsi = "NOT DEFINED";
                }



                if(snapshot.child("foto").exists()){
                    profil_picture =  snapshot.child("foto").getValue().toString();
                }else{
                    profil_picture = "http://icons.iconarchive.com/icons/webalys/kameleon.pics/512/Farmer-icon.png";
                }







                Dialog.hide();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Log.e("Chat", "The read failed: " + error.getText());
            }
        });







    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

     /* komponen fungsi untuk upload foto */

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

            uploadFile();
        }
    }

    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        //checking if file is available
        if (filePath != null) {
            //displaying progress dialog while image is uploading
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            //getting the storage reference
            StorageReference sRef = storageReference.child(Constants.STORAGE_PATH_UPLOADS + System.currentTimeMillis() + "." + getFileExtension(filePath));

            //adding the file to reference
            sRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //dismissing the progress dialog
                            progressDialog.dismiss();

                            //displaying success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                            FirebaseUser user = auth.getCurrentUser();

                            //creating the upload object to store uploaded image details
                            Upload upload = new Upload(user.getUid(), taskSnapshot.getDownloadUrl().toString());


                            urlFotoFirebase = taskSnapshot.getDownloadUrl().toString();
                            //adding an upload to firebase database
                            String uploadId = mDatabase.push().getKey();
                            mDatabase.child(uploadId).setValue(upload);


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //displaying the upload progress
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        } else {
            //display an error if no file is selected
        }
    }







    @Override
    public void onClick(View view) {
        if (view == buttonChoose) {
            showFileChooser();
        } else if (view == buttonUpload) {
            showFileChooser();
        }
    }

         /* komponen fungsi untuk upload foto */



    public void Simpan(View view) {




        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("qna").child("1").child("question").getRef();
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                Long tsLong = System.currentTimeMillis();
                String ts = tsLong.toString();
                FirebaseUser user = auth.getCurrentUser();

                Questions msg = new Questions(Long.parseLong(ts), NamaUser, user.getUid(), userInput.getText().toString());
                rootRef.child(ts).setValue(msg);
                rootRef.child(ts).child("question").setValue(userInput.getText().toString());
                rootRef.child(ts).child("idUser").setValue(user.getUid());
                rootRef.child(ts).child("gambar").setValue(urlFotoFirebase);
                rootRef.child(ts).child("profil_picture").setValue(profil_picture);
                rootRef.child(ts).child("provinsi").setValue(provinsi);

                finish();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }



}
