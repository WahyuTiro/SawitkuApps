package com.sawitku;

import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sawitku.activitydrawer.DownloadFilesActivity;
import com.sawitku.activitydrawer.SearchActivity;
import com.sawitku.fmodels.Pertanyaan;

import java.text.SimpleDateFormat;
import java.util.Date;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    ScaleAnimation shrinkAnim;
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mLayoutManager;
    private TextView tvNoMovies;

    //Getting reference to Firebase Database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseReference = database.getReference();

    String idTopik = "1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FirebaseUser user = mAuth.getCurrentUser();

        DatabaseReference qry = FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid()).getRef();


        qry.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                View hView =  navigationView.getHeaderView(0);

                TextView nav_user  = (TextView)hView.findViewById(R.id.textViewNama);
                TextView nav_email  = (TextView)hView.findViewById(R.id.textViewEmail);
                ImageView nav_photo = (ImageView)hView.findViewById(R.id.imageViewGambar);



                if(snapshot.child("nama").exists()){
                    String nama =  snapshot.child("nama").getValue().toString();
                    nav_user.setText(nama);

                }

                if(snapshot.child("foto").exists()){
                    String foto =  snapshot.child("foto").getValue().toString();
                    Glide.with(getApplicationContext()).load(foto)
                            .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                            .into(nav_photo);

                }

                if(snapshot.child("namaDisplay").exists()){
                    String nama =  snapshot.child("namaDisplay").getValue().toString();
                    nav_email.setText("("+nama+")");

                }else{
                    nav_email.setVisibility(View.GONE);
                }




            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Log.e("Chat", "The read failed: " + error.getText());
            }
        });
        
        // List Pertanyaan

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);



        //Initializing our Recyclerview
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        tvNoMovies = (TextView) findViewById(R.id.tv_no_movies);

        //scale animation to shrink floating actionbar
        shrinkAnim = new ScaleAnimation(1.15f, 0f, 1.15f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        if (mRecyclerView != null) {
            //to enable optimization of recyclerview
            mRecyclerView.setHasFixedSize(true);
        }
        //using staggered grid pattern in recyclerview
        mLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);


        //Say Hello to our new Firebase UI Element, i.e., FirebaseRecyclerAdapter
        final FirebaseRecyclerAdapter<Pertanyaan,  MovieViewHolder> adapter = new FirebaseRecyclerAdapter<Pertanyaan, MovieViewHolder>(
                Pertanyaan.class,
                R.layout.questions_list_items,
                MovieViewHolder.class,
                //referencing the node where we want the database to store the data from our Object
                mDatabaseReference.child("qna").child(idTopik).child("question").getRef()
        ) {
            @Override
            protected void populateViewHolder(final MovieViewHolder viewHolder, Pertanyaan kat, final int position) {
                if (tvNoMovies.getVisibility() == View.VISIBLE) {
                    tvNoMovies.setVisibility(View.GONE);
                }
                viewHolder.tvMovieName.setText(kat.getNama());
                viewHolder.tvQuestion.setText(kat.getQuestion());
                viewHolder.iv_id.setText(String.valueOf(kat.getId()));
                viewHolder.tvProvinsi.setText(String.valueOf(kat.getProvinsi()));
                Glide.with(getApplicationContext()).load(kat.getProfil_picture())
                        .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                        .into(viewHolder.iv_movie_poster);

                SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                String tanggal =  sfd.format(new Date(kat.getId()));

                viewHolder.tv_tanggal.setText(tanggal);

            }


            @Override
            public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                final MovieViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                viewHolder.setOnClickListener(new  MovieViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {


                        if (!(viewHolder.iv_id.getText().toString().isEmpty())) {

                            Intent k = new Intent(HomeActivity.this, ListJawaban.class);
                            k.putExtra("id", idTopik);
                            k.putExtra("idQuestion", viewHolder.iv_id.getText().toString());
                            k.putExtra("question", viewHolder.tvQuestion.getText().toString());
                            startActivity(k);

                        }


                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        //  Toast.makeText(getActivity(), "Item long clicked at " + position, Toast.LENGTH_SHORT).show();
                    }
                });
                return viewHolder;
            }



        };


        mRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(getActivity(), "CAKRA MAMAH MERTUAH.....", Toast.LENGTH_LONG).show();
            }
        });



        mRecyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// get prompts.xml view
//                LayoutInflater li = LayoutInflater.from(HomeActivity.this);
//                View promptsView = li.inflate(R.layout.prompts, null);
//
//                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
//                        HomeActivity.this);
//
//                // set prompts.xml to alertdialog builder
//                alertDialogBuilder.setView(promptsView);
//
//                final EditText userInput = (EditText) promptsView
//                        .findViewById(R.id.editTextDialogUserInput);
//
//                // set dialog message
//                alertDialogBuilder
//                        .setCancelable(false)
//                        .setPositiveButton("OK",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog,int id) {
//                                        // get user input and set it to result
//                                        // edit text
//                                        //result.setText(userInput.getText());
//
//                                        Bundle bbb = getIntent().getExtras();
//                                        if (bbb != null) {
//                                            idTopik = bbb.getString("id");
//                                        }
//
//                                        Toast.makeText(getApplicationContext(), "input message" + userInput.getText(), Toast.LENGTH_SHORT).show();
//
//                                        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("qna").child("1").child("question").getRef();
//                                        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                                            @Override
//                                            public void onDataChange(DataSnapshot snapshot) {
//
//                                                Long tsLong = System.currentTimeMillis();
//                                                String ts = tsLong.toString();
//
//                                                FirebaseUser user = mAuth.getCurrentUser();
//
//                                                Pertanyaan msg = new Pertanyaan(Long.parseLong(ts), user.getEmail(), user.getUid(), userInput.getText().toString());
//                                                rootRef.child(ts).setValue(msg);
//                                                rootRef.child(ts).child("question").setValue(userInput.getText().toString());
//                                                rootRef.child(ts).child("idUser").setValue(user.getUid());
//
//                                            }
//
//                                            @Override
//                                            public void onCancelled(DatabaseError databaseError) {
//
//                                            }
//                                        });
//
//
//
//
//
//                                    }
//                                })
//                        .setNegativeButton("Cancel",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog,int id) {
//                                        dialog.cancel();
//                                    }
//                                });
//
//                // create alert dialog
//                AlertDialog alertDialog = alertDialogBuilder.create();
//
//                // show it
//                alertDialog.show();


                Intent question = new Intent(HomeActivity.this, CreateQuestion.class);
                startActivity(question);


            }
        });










    }



    public static class MovieViewHolder extends RecyclerView.ViewHolder {

        TextView tvMovieName;
        TextView tvQuestion;
        TextView tvProvinsi;
        TextView iv_id;
        TextView tv_tanggal;
        ImageView iv_movie_poster;

        public MovieViewHolder(View v) {
            super(v);
            tvMovieName = (TextView) v.findViewById(R.id.tv_name);
            tvQuestion = (TextView) v.findViewById(R.id.tv_question);
            iv_id = (TextView) v.findViewById(R.id.iv_id);
            iv_movie_poster = (ImageView)v.findViewById(R.id.iv_movie_poster);
            tvProvinsi = (TextView) v.findViewById(R.id.tv_province);
            tv_tanggal = (TextView) v.findViewById(R.id.tv_tanggal);


            //listener set on ENTIRE ROW, you may set on individual components within a row.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onItemClick(v, getAdapterPosition());

                }
            });
            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mClickListener.onItemLongClick(v, getAdapterPosition());
                    return true;
                }
            });
        }

        private  MovieViewHolder.ClickListener mClickListener;

        //Interface to send callbacks...
        public interface ClickListener {
            public void onItemClick(View view, int position);

            public void onItemLongClick(View view, int position);
        }

        public void setOnClickListener( MovieViewHolder.ClickListener clickListener) {
            mClickListener = clickListener;
        }
    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.home, menu);
//        return true;
//    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.navigation_map) {
            Intent pindah = new Intent(HomeActivity.this, PeopleMap.class);
            startActivity(pindah);
            // Handle the camera action
        } else if (id == R.id.navigation_search) {
            Intent pindah = new Intent(HomeActivity.this, SearchActivity.class);
            startActivity(pindah);

        } else if (id == R.id.navigation_download) {
            Intent pindah = new Intent(HomeActivity.this, DownloadFilesActivity.class);
            startActivity(pindah);

        } else if (id == R.id.navigation_profile) {

            Intent pindah = new Intent(HomeActivity.this, EditProfileActivity.class);
            startActivity(pindah);

        }  else if (id == R.id.nav_send) {
            signOut();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void signOut() {
        mAuth.signOut();
        Intent pindah = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(pindah);
    }
}
