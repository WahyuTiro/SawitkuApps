package com.sawitku;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfilUser extends AppCompatActivity {

    String idUser =  "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_user);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle bbb = getIntent().getExtras();
        if (bbb != null) {
            idUser = bbb.getString("id");

        }

        DatabaseReference qry = FirebaseDatabase.getInstance().getReference().child("user").child(idUser).getRef();

        final ProgressDialog Dialog = new ProgressDialog(ProfilUser.this);
        Dialog.setMessage("Please Wait...");
        Dialog.show();

        qry.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                TextView input_nama = (TextView)findViewById(R.id.input_nama);
                TextView input_tipe = (TextView)findViewById(R.id.input_tipe);
                TextView input_nama_display = (TextView)findViewById(R.id.input_nama_display);
                TextView input_provinsi = (TextView)findViewById(R.id.input_provinsi);
                TextView input_kota = (TextView)findViewById(R.id.input_kota);
                TextView input_kecamatan= (TextView)findViewById(R.id.input_kecamatan);
                ImageView imageView = (ImageView)findViewById(R.id.imageView);


                if(snapshot.child("tipe").exists()){
                    String TipeTeks = "";
                    String idTeks = snapshot.child("tipe").getValue().toString();
                    int idTipe = Integer.parseInt(idTeks);

                    if(idTipe==1){
                        TipeTeks = "Petani";
                    }
                    else if(idTipe==2){
                        TipeTeks = "Peneliti";
                    }
                    else if(idTipe==3){
                        TipeTeks = "Praktisi";
                    }

                    else if(idTipe==4){
                        TipeTeks = "Distributor";
                    }

                    input_tipe.setText("("+TipeTeks+")");

                }


                if(snapshot.child("foto").exists()){
                    String foto =  snapshot.child("foto").getValue().toString();
                    Glide.with(ProfilUser.this).load(foto)
                            .bitmapTransform(new CenterCrop(ProfilUser.this))
                            .into(imageView);

                }

                if(snapshot.child("namaDisplay").exists()){
                    String nama =  snapshot.child("namaDisplay").getValue().toString();
                    input_nama_display.setText(nama);

                }else{
                    input_nama_display.setVisibility(View.GONE);
                }


                if(snapshot.child("nama").exists()){
                    String nama =  snapshot.child("nama").getValue().toString();
                    input_nama.setText(nama);

                }




                if(snapshot.child("provinsi").exists()){
                    String provinsi =  snapshot.child("provinsi").getValue().toString();
                    input_provinsi.setText(provinsi);

                }


                if(snapshot.child("kota").exists()){
                    String kota =  snapshot.child("kota").getValue().toString();
                    input_kota.setText(kota);

                }


                if(snapshot.child("kecamatan").exists()){
                    String kecamatan =  snapshot.child("kecamatan").getValue().toString();
                    input_kecamatan.setText(kecamatan);

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
}
