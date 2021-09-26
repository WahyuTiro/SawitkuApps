package com.sawitku;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.sawitku.fmodels.SearchUser;

import fr.ganfra.materialspinner.MaterialSpinner;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class EditProfileActivity extends AppCompatActivity {

    String[] SPINNERLIST = {"Petani", "Peneliti", "Praktisi", "Perusahaan"};

    int posisi = 0;

    private FirebaseAuth auth;

    TextView txtEditFoto;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        //get firebase auth instance
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        EditText input_email = (EditText) findViewById(R.id.input_email);

        input_email.setText(user.getEmail());


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, SPINNERLIST);
        MaterialSpinner materialDesignSpinner = (MaterialSpinner)
                findViewById(R.id.android_material_design_spinner);
        materialDesignSpinner.setAdapter(arrayAdapter);

        txtEditFoto = (TextView)findViewById(R.id.txtEditFoto);

        DatabaseReference qry = FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid()).getRef();

        qry.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                EditText input_nama = (EditText) findViewById(R.id.input_nama);
                ImageView fotoProfil = (ImageView)findViewById(R.id.fotoProfil);

                MaterialSpinner materialDesignSpinner = (MaterialSpinner)
                        findViewById(R.id.android_material_design_spinner);

                if(snapshot.child("nama").exists()){
                    String nama =  snapshot.child("nama").getValue().toString();
                    input_nama.setText(nama);

                }

                if(snapshot.child("tipe").exists()) {
                    String Stipe =  snapshot.child("tipe").getValue().toString();
                    int tipe = Integer.parseInt(Stipe);
                    materialDesignSpinner.setSelection(tipe);


                }

                if(snapshot.child("foto").exists()){
                    String foto =  snapshot.child("foto").getValue().toString();
                    Glide.with(EditProfileActivity.this).load(foto)
                            .bitmapTransform(new CropCircleTransformation(EditProfileActivity.this))
                            .into(fotoProfil);

                }

               // Dialog.hide();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Log.e("Chat", "The read failed: " + error.getText());
            }
        });

        txtEditFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pindah = new Intent(EditProfileActivity.this, EditFotoProfil.class);
                startActivity(pindah);
            }
        });





    }

    public void Lanjutkan(View view) {

        final FirebaseUser user = auth.getCurrentUser();
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("user").getRef();
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                MaterialSpinner materialDesignSpinner = (MaterialSpinner) findViewById(R.id.android_material_design_spinner);
                int posisi = materialDesignSpinner.getSelectedItemPosition();

                EditText input_nama = (EditText) findViewById(R.id.input_nama);
                String nama = input_nama.getText().toString();

                if (snapshot.hasChild(user.getUid())) {

                    if (TextUtils.isEmpty(nama)) {
                        Toast.makeText(getApplicationContext(), "Silahkan Masukkan Nama", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (posisi == 0) {
                        Toast.makeText(getApplicationContext(), "Silahkan Pilih Tipe Professi", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    rootRef.child(user.getUid()).child("id").setValue(user.getUid());
                    rootRef.child(user.getUid()).child("nama").setValue(nama);
                    rootRef.child(user.getUid()).child("tipe").setValue(String.valueOf(posisi));

                    if (posisi == 1) {

                        Intent k = new Intent(EditProfileActivity.this, EditProfilPetani.class);
                        startActivity(k);
                    }

                    if (posisi == 2) {

                        Intent k = new Intent(EditProfileActivity.this, EditProfilPeneliti.class);
                        startActivity(k);

                    }


                    if (posisi == 3) {

                        Intent k = new Intent(EditProfileActivity.this, EditProfilPraktisi.class);
                        startActivity(k);

                    }

                    if (posisi == 4) {

                        Intent k = new Intent(EditProfileActivity.this, EditProfilPerusahaan.class);
                        startActivity(k);

                    }


                } else {

                    if (TextUtils.isEmpty(nama)) {
                        Toast.makeText(getApplicationContext(), "Silahkan Masukkan Nama", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (posisi == 0) {
                        Toast.makeText(getApplicationContext(), "Silahkan Pilih Tipe Professi", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    Toast.makeText(getApplicationContext(), "TIDAK ADA", Toast.LENGTH_SHORT).show();
                    rootRef.child(user.getUid()).child("id").setValue(user.getUid());
                    rootRef.child(user.getUid()).child("nama").setValue(nama);
                    rootRef.child(user.getUid()).child("lat").setValue("3.5755365");
                    rootRef.child(user.getUid()).child("log").setValue("98.6655594");
                    rootRef.child(user.getUid()).child("tipe").setValue(String.valueOf(posisi));

                    if (posisi == 1) {

                        Intent k = new Intent(EditProfileActivity.this, EditProfilPetani.class);
                        startActivity(k);
                    }

                    if (posisi == 2) {

                        Intent k = new Intent(EditProfileActivity.this, EditProfilPeneliti.class);
                        startActivity(k);

                    }


                    if (posisi == 3) {

                        Intent k = new Intent(EditProfileActivity.this, EditProfilPraktisi.class);
                        startActivity(k);

                    }

                    if (posisi == 4) {

                        Intent k = new Intent(EditProfileActivity.this, EditProfilPerusahaan.class);
                        startActivity(k);

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }


}
