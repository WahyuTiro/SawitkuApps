package com.sawitku;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.sawitku.vfragment.ItemDownloadFragment;
import com.sawitku.vfragment.ItemHomeFragment;
import com.sawitku.vfragment.ItemQAFragment;
import com.sawitku.vfragment.ItemSearchFragment;

public class MainActivity extends AppCompatActivity {


    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();

        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String token = FirebaseInstanceId.getInstance().getToken();

        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid()).getRef();

        ref2.child("token").setValue(token);


        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.action_item1:
                                selectedFragment = ItemHomeFragment.newInstance();
                                break;
                            case R.id.action_item2:
                                selectedFragment = ItemSearchFragment.newInstance();
                                break;
                            case R.id.action_item3:
                                selectedFragment = ItemQAFragment.newInstance();
                                break;
                            case R.id.action_item4:
                                selectedFragment = ItemDownloadFragment.newInstance();
                                break;
                            case R.id.action_item5:
                                selectedFragment = ProfilFragment.newInstance();
                                break;
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, ItemHomeFragment.newInstance());
        transaction.commit();





        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("qna").child("1").getRef();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {


                if(snapshot.child("nama").exists()){
                    String nama = snapshot.child("nama").getValue().toString();
                    System.out.println("Can Get Database"+ nama);
                }



            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Log.e("Chat", "The read failed: " + error.getText());
            }
        });

    }

    public void Keluar(View view){


        Toast.makeText(MainActivity.this, "Keluar", Toast.LENGTH_LONG).show();
        signOut();
        finish();



    }


    public void editProfile(View view){


        Intent editP = new Intent(MainActivity.this, EditProfileActivity.class);
        startActivity(editP);



    }

    public void editPassword(View view){


        Intent editP = new Intent(MainActivity.this, ResetPasswordActivity.class);
        startActivity(editP);



    }


    public void termsOfCondition(View view){


        Intent editP = new Intent(MainActivity.this, TermOfService.class);
        startActivity(editP);



    }


    public void contactUS(View view){


        Intent editP = new Intent(MainActivity.this, ContactUs.class);
        startActivity(editP);



    }

    //sign out method
    public void signOut() {
        auth.signOut();
    }


}
