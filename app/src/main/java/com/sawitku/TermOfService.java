package com.sawitku;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TermOfService extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("app").getRef();




        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                TextView about = (TextView)findViewById(R.id.about);

                about.setText(Html.fromHtml(snapshot.child("terms").getValue().toString()));


              //  about.setText(snapshot.child("terms").getValue().toString());


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Log.e("Chat", "The read failed: " + error.getText());
            }
        });
    }



    //jika back button di klik
    public boolean onOptionsItemSelected(MenuItem item) {


        onBackPressed();

        return true;
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
