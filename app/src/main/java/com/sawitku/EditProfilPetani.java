package com.sawitku;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ParseException;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import fr.ganfra.materialspinner.MaterialSpinner;

public class EditProfilPetani extends AppCompatActivity  implements View.OnClickListener {

    private FirebaseAuth auth;

    private int PLACE_PICKER_REQUEST = 1;

    String[] SPINNERLIST = {"0,5", "1", "2", "3", "4", "5", "6", "7" , "8", "9", "10"};

      /* deklarasi untuk upload file */

    //constant to track image chooser intent
    private static final int PICK_IMAGE_REQUEST = 234;

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
    String lati = "";
    String logi = "";



    ArrayList<String> worldprovincelist;
    ArrayList<String> idprovincelist;


    ArrayList<String> worldkecamatanlist;
    ArrayList<String> idkecamatanlist;



    ArrayList<String> worldkabupatenlist;
    ArrayList<String> idkabupatenlist;


    String provinsi = "0";
    String nama_provinsi = "0";
    String id_provinsi_db = "0";


    String kabupaten = "0";
    String nama_kabupaten = "0";
    String id_kabupaten_db = "0";




    String kecamatan = "0";
    String nama_kecamatan = "0";
    String id_kecamatan_db = "0";



    /* deklarasi untuk upload file */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil_petani);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        //  Inialisasi Element UI untuk update
        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        imageView = (ImageView) findViewById(R.id.imageView);
        storageReference = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);
        buttonUpload.setOnClickListener(EditProfilPetani.this);
        //  Inialisasi Element UI untuk update




        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, SPINNERLIST);
        MaterialSpinner materialDesignSpinner = (MaterialSpinner)
                findViewById(R.id.android_material_design_spinner);
        materialDesignSpinner.setAdapter(arrayAdapter);



        new ProvinsiAsyncTask().execute("http://188.166.177.2/indonesia/provinsi.php");


        DatabaseReference qry = FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid()).getRef();

        final ProgressDialog Dialog = new ProgressDialog(EditProfilPetani.this);
        Dialog.setMessage("Please Wait...");
        Dialog.show();

        qry.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                EditText input_nama = (EditText)findViewById(R.id.input_nama);
               // EditText input_provinsi = (EditText)findViewById(R.id.input_provinsi);
               // EditText input_kota = (EditText)findViewById(R.id.input_kota);
               // EditText input_kecamatan= (EditText)findViewById(R.id.input_kecamatan);
                EditText input_sumber_benih = (EditText)findViewById(R.id.input_sumber_benih);
                EditText input_tahun_tanam = (EditText)findViewById(R.id.input_tahun_tanam);

                ImageView imageView = (ImageView)findViewById(R.id.imageView);


                if(snapshot.child("foto").exists()){
                    String foto =  snapshot.child("foto").getValue().toString();
                    Glide.with(getApplicationContext()).load(foto)
                            .bitmapTransform(new CenterCrop(EditProfilPetani.this))
                            .into(imageView);

                }

                if(snapshot.child("namaDisplay").exists()){
                    String nama =  snapshot.child("nama").getValue().toString();
                    input_nama.setText(nama);

                }







                if(snapshot.child("luasKebun").exists()){
                    MaterialSpinner materialDesignSpinner = (MaterialSpinner) findViewById(R.id.android_material_design_spinner);
                    String luasKebun =  snapshot.child("luasKebun").getValue().toString();
                    int pakar = Integer.parseInt(luasKebun);
                    materialDesignSpinner.setSelection(pakar);
                }




                if(snapshot.child("sumber_benih").exists()){
                    String kecamatan =  snapshot.child("sumber_benih").getValue().toString();
                    input_sumber_benih.setText(kecamatan);

                }

                if(snapshot.child("tahun_tanam").exists()){
                    String kecamatan =  snapshot.child("tahun_tanam").getValue().toString();
                    input_tahun_tanam.setText(kecamatan);

                }


                if(snapshot.child("luasKebun").exists()){
                    MaterialSpinner materialDesignSpinner = (MaterialSpinner) findViewById(R.id.android_material_design_spinner);
                    String kepakaran =  snapshot.child("luasKebun").getValue().toString();
                    int pakar = Integer.parseInt(kepakaran);
                    materialDesignSpinner.setSelection(pakar);
                }

                if(snapshot.child("id_provinsi_db").exists()){
                    String id_provinsi =  snapshot.child("id_provinsi_db").getValue().toString();
                    id_provinsi_db = id_provinsi;

                }


                if(snapshot.child("id_kabupaten_db").exists()){
                    String id_kabupaten =  snapshot.child("id_kabupaten_db").getValue().toString();
                    id_kabupaten_db = id_kabupaten;

                }



                if(snapshot.child("id_kecamatan_db").exists()){
                    String id_kecamatan =  snapshot.child("id_kecamatan_db").getValue().toString();
                    id_kecamatan_db = id_kecamatan;

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

    public void Simpan(View view) {

        final FirebaseUser user = auth.getCurrentUser();
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("user").getRef();
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                MaterialSpinner materialDesignSpinner = (MaterialSpinner) findViewById(R.id.android_material_design_spinner);
                int posisi = materialDesignSpinner.getSelectedItemPosition();
                EditText input_nama = (EditText)findViewById(R.id.input_nama);
                EditText input_sumber_benih= (EditText)findViewById(R.id.input_sumber_benih);
                EditText input_tahun_tanam= (EditText)findViewById(R.id.input_tahun_tanam);

                Spinner mySpinnerProvinsi = (Spinner) findViewById(R.id.provinsi_spinner);
                Spinner mySpinnerKabupaten = (Spinner) findViewById(R.id.kabupaten_spinner);
                Spinner mySpinnerKecamatan = (Spinner) findViewById(R.id.kecamatan_spinner);
                int posisiProvinsi = mySpinnerProvinsi.getSelectedItemPosition();
                int posisiKabupaten = mySpinnerKabupaten.getSelectedItemPosition();
                int posisiKecamatan = mySpinnerKecamatan.getSelectedItemPosition();

                rootRef.child(user.getUid()).child("namaDisplay").setValue(input_nama.getText().toString());

                rootRef.child(user.getUid()).child("provinsi").setValue(nama_provinsi);
                rootRef.child(user.getUid()).child("kota").setValue(nama_kabupaten);
                rootRef.child(user.getUid()).child("kabupaten").setValue(nama_kabupaten);
                rootRef.child(user.getUid()).child("kecamatan").setValue(nama_kecamatan);


                rootRef.child(user.getUid()).child("id_provinsi").setValue(provinsi);
                rootRef.child(user.getUid()).child("id_kota").setValue(kabupaten);
                rootRef.child(user.getUid()).child("id_kabupaten").setValue(kabupaten);
                rootRef.child(user.getUid()).child("id_kecamatan").setValue(kecamatan);

                rootRef.child(user.getUid()).child("id_provinsi_db").setValue(String.valueOf(posisiProvinsi));
                rootRef.child(user.getUid()).child("id_kabupaten_db").setValue(String.valueOf(posisiKabupaten));
                rootRef.child(user.getUid()).child("id_kecamatan_db").setValue(String.valueOf(posisiKecamatan));


                rootRef.child(user.getUid()).child("sumber_benih").setValue(input_sumber_benih.getText().toString());
                rootRef.child(user.getUid()).child("tahun_tanam").setValue(input_tahun_tanam.getText().toString());
                rootRef.child(user.getUid()).child("lat").setValue(lati);
                rootRef.child(user.getUid()).child("log").setValue(logi);

                rootRef.child(user.getUid()).child("luasKebun").setValue(""+posisi);
                //rootRef.child(user.getUid()).child("foto").setValue(urlFotoFirebase);



                finish();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
//                String toastMsg = String.format(
//                        "Place: %s \n" +
//                                "Alamat: %s \n" +
//                                "Latlng %s \n", place.getName(), place.getAddress(), place.getLatLng().latitude+" "+place.getLatLng().longitude);
//                tvPlaceAPI.setText(toastMsg);

                lati =String.valueOf(place.getLatLng().latitude);
                logi =String.valueOf(place.getLatLng().longitude);

            }
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


    public  void  PickerPlace(View view){

        // membuat Intent untuk Place Picker
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            //menjalankan place picker
            startActivityForResult(builder.build(EditProfilPetani.this), PLACE_PICKER_REQUEST);

            // check apabila <a title="Solusi Tidak Bisa Download Google Play Services di Android" href="http://www.twoh.co/2014/11/solusi-tidak-bisa-download-google-play-services-di-android/" target="_blank">Google Play Services tidak terinstall</a> di HP
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

    }

     /* komponen fungsi untuk upload foto */





     /* Spinner Load Data Provinsi */
     class ProvinsiAsyncTask extends AsyncTask<String, Void, Boolean> {


         ProgressDialog dialog;

         @Override
         protected void onPreExecute() {
             super.onPreExecute();
             dialog = new ProgressDialog(EditProfilPetani.this);
             dialog.setMessage("Sedang Mengambil Data...");
             dialog.setTitle("Connecting server");
             dialog.show();
             dialog.setCancelable(false);
         }

         @Override
         protected Boolean doInBackground(String... urls) {
             try {

                 //------------------>>
                 HttpGet httppost = new HttpGet(urls[0]);
                 HttpClient httpclient = new DefaultHttpClient();
                 HttpResponse response = httpclient.execute(httppost);

                 // StatusLine stat = response.getStatusLine();
                 int status = response.getStatusLine().getStatusCode();

                 worldprovincelist = new ArrayList<String>();
                 idprovincelist = new ArrayList<String>();




                 if (status == 200) {
                     HttpEntity entity = response.getEntity();
                     String data = EntityUtils.toString(entity);




                     JSONObject jsono = new JSONObject(data);
                     JSONArray jarray = jsono.getJSONArray("provinces");



                     for (int i = 0; i < jarray.length(); i++) {
                         JSONObject object = jarray.getJSONObject(i);

                         idprovincelist.add(object.getString("id"));
                         worldprovincelist.add(object.getString("name"));
                         //idlist.add(object.getString("nama"));

                     }
                     return true;
                 }

                 //------------------>>

             } catch (ParseException e1) {
                 e1.printStackTrace();
             } catch (IOException e) {
                 e.printStackTrace();
             } catch (JSONException e) {
                 e.printStackTrace();
             }
             return false;
         }

         protected void onPostExecute(Boolean result) {
             dialog.cancel();
             // adapter.notifyDataSetChanged();

             provinsi = idprovincelist.get(0);
             nama_provinsi = worldprovincelist.get(0);
             Spinner mySpinner = (Spinner) findViewById(R.id.provinsi_spinner);


             // Spinner adapter
             mySpinner
                     .setAdapter(new ArrayAdapter<String>(EditProfilPetani.this,
                             android.R.layout.simple_spinner_dropdown_item,
                             worldprovincelist));


             mySpinner.setSelection(Integer.parseInt(id_provinsi_db));




             // Spinner on item click listener
             mySpinner
                     .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                         @Override
                         public void onItemSelected(AdapterView<?> arg0,
                                                    View arg1, int position, long arg3) {
                             // TODO Auto-generated method stub
                             // Locate the textviews in activity_main.xml
                             System.out.println("ID KATEGORI"+idprovincelist.get(position));

                             provinsi = idprovincelist.get(position);
                             nama_provinsi = worldprovincelist.get(position);


                             new KabupatenAsyncTask().execute("http://188.166.177.2/indonesia/kabupaten.php?provinsi="+provinsi);





                         }

                         @Override
                         public void onNothingSelected(AdapterView<?> arg0) {
                             // TODO Auto-generated method stub
                         }
                     });



             if(result == false)
                 Toast.makeText(EditProfilPetani.this, "Unable to fetch data from server", Toast.LENGTH_LONG).show();

         }
     }

     /* Spinner Load Data Provinsi */



    /* Spinner Load Data Kabupaten */
    class KabupatenAsyncTask extends AsyncTask<String, Void, Boolean> {


        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(EditProfilPetani.this);
            dialog.setMessage("Sedang Mengambil Data...");
            dialog.setTitle("Connecting server");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {

                //------------------>>
                HttpGet httppost = new HttpGet(urls[0]);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);

                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();

                worldkabupatenlist = new ArrayList<String>();
                idkabupatenlist = new ArrayList<String>();




                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);




                    JSONObject jsono = new JSONObject(data);
                    JSONArray jarray = jsono.getJSONArray("kabupaten");



                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject object = jarray.getJSONObject(i);

                        idkabupatenlist.add(object.getString("id"));
                        worldkabupatenlist.add(object.getString("name"));
                        //idlist.add(object.getString("nama"));

                    }
                    return true;
                }

                //------------------>>

            } catch (ParseException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {
            dialog.cancel();
            // adapter.notifyDataSetChanged();

            kabupaten = idkabupatenlist.get(0);
            nama_kabupaten = worldkabupatenlist.get(0);
            Spinner mySpinner = (Spinner) findViewById(R.id.kabupaten_spinner);


            // Spinner adapter
            mySpinner
                    .setAdapter(new ArrayAdapter<String>(EditProfilPetani.this,
                            android.R.layout.simple_spinner_dropdown_item,
                            worldkabupatenlist));

            mySpinner.setSelection(Integer.parseInt(id_kabupaten_db));



            // Spinner on item click listener
            mySpinner
                    .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0,
                                                   View arg1, int position, long arg3) {
                            // TODO Auto-generated method stub
                            // Locate the textviews in activity_main.xml
                            System.out.println("ID KATEGORI"+idkabupatenlist.get(position));

                            kabupaten = idkabupatenlist.get(position);
                            nama_kabupaten = worldkabupatenlist.get(position);

                            new KecamatanAsyncTask().execute("http://188.166.177.2/indonesia/kecamatan.php?kabupaten="+kabupaten);




                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                            // TODO Auto-generated method stub
                        }
                    });



            if(result == false)
                Toast.makeText(EditProfilPetani.this, "Unable to fetch data from server", Toast.LENGTH_LONG).show();

        }
    }

     /* Spinner Load Data Kecamatan */

    class KecamatanAsyncTask extends AsyncTask<String, Void, Boolean> {


        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(EditProfilPetani.this);
            dialog.setMessage("Sedang Mengambil Data...");
            dialog.setTitle("Connecting server");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {

                //------------------>>
                HttpGet httppost = new HttpGet(urls[0]);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);

                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();

                worldkecamatanlist = new ArrayList<String>();
                idkecamatanlist = new ArrayList<String>();




                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);




                    JSONObject jsono = new JSONObject(data);
                    JSONArray jarray = jsono.getJSONArray("kecamatan");



                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject object = jarray.getJSONObject(i);

                        idkecamatanlist.add(object.getString("id"));
                        worldkecamatanlist.add(object.getString("name"));

                    }
                    return true;
                }

                //------------------>>

            } catch (ParseException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {
            dialog.cancel();
            // adapter.notifyDataSetChanged();

            kecamatan = idkecamatanlist.get(0);
            nama_kecamatan = worldkecamatanlist.get(0);
            Spinner mySpinner = (Spinner) findViewById(R.id.kecamatan_spinner);


            // Spinner adapter
            mySpinner
                    .setAdapter(new ArrayAdapter<String>(EditProfilPetani.this,
                            android.R.layout.simple_spinner_dropdown_item,
                            worldkecamatanlist));

            mySpinner.setSelection(Integer.parseInt(id_kecamatan_db));


            // Spinner on item click listener
            mySpinner
                    .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0,
                                                   View arg1, int position, long arg3) {
                            // TODO Auto-generated method stub
                            // Locate the textviews in activity_main.xml
                            System.out.println("ID KATEGORI"+idkecamatanlist.get(position));

                            kecamatan = idkecamatanlist.get(position);
                            nama_kecamatan = worldkecamatanlist.get(position);

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                            // TODO Auto-generated method stub
                        }
                    });



            if(result == false)
                Toast.makeText(EditProfilPetani.this, "Unable to fetch data from server", Toast.LENGTH_LONG).show();

        }
    }


     /* Spinner Load Data Kecamatan */






}
