package com.sawitku;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sawitku.fmodels.Questions;
import com.sawitku.vfragment.ItemQAFragment;

public class ListPertanyaan extends AppCompatActivity {

    ScaleAnimation shrinkAnim;
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mLayoutManager;
    private TextView tvNoMovies;

    //Getting reference to Firebase Database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseReference = database.getReference();

    String idTopik = "1";

    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pertanyaan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Bundle bbb = getIntent().getExtras();
        if (bbb != null) {
            idTopik = bbb.getString("id");
        }


        //get firebase auth instance
        auth = FirebaseAuth.getInstance();


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
        final FirebaseRecyclerAdapter<Questions,  MovieViewHolder> adapter = new FirebaseRecyclerAdapter<Questions,  MovieViewHolder>(
                Questions.class,
                R.layout.questions_list_items,
                 MovieViewHolder.class,
                //referencing the node where we want the database to store the data from our Object
                mDatabaseReference.child("qna").child(idTopik).child("question").getRef()
        ) {
            @Override
            protected void populateViewHolder(final  MovieViewHolder viewHolder, Questions kat, final int position) {
                if (tvNoMovies.getVisibility() == View.VISIBLE) {
                    tvNoMovies.setVisibility(View.GONE);
                }
                viewHolder.tvMovieName.setText(kat.getQuestion());
                viewHolder.iv_id.setText(String.valueOf(kat.getId()));


            }


            @Override
            public  MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                final  MovieViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                viewHolder.setOnClickListener(new  MovieViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {


                        if (!(viewHolder.iv_id.getText().toString().isEmpty())) {

                            Bundle bbb = getIntent().getExtras();
                            if (bbb != null) {
                                idTopik = bbb.getString("id");
                            }

                            Intent k = new Intent(ListPertanyaan.this, ListJawaban.class);
                            k.putExtra("id", idTopik);
                            k.putExtra("idQuestion", viewHolder.iv_id.getText().toString());
                            k.putExtra("question", viewHolder.tvMovieName.getText().toString());
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


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



        /*
                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(ListPertanyaan.this);
                View promptsView = li.inflate(R.layout.prompts, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        ListPertanyaan.this);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // get user input and set it to result
                                        // edit text
                                        //result.setText(userInput.getText());

                                        Bundle bbb = getIntent().getExtras();
                                        if (bbb != null) {
                                            idTopik = bbb.getString("id");
                                        }

                                        Toast.makeText(getApplicationContext(), "input message" + userInput.getText(), Toast.LENGTH_SHORT).show();

                                        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("qna").child(idTopik).child("question").getRef();
                                        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot snapshot) {

                                                Long tsLong = System.currentTimeMillis();
                                                String ts = tsLong.toString();

                                                FirebaseUser user = auth.getCurrentUser();

                                                Questions msg = new Questions(Long.parseLong(ts), user.getEmail(), user.getUid(), userInput.getText().toString());
                                                rootRef.child(ts).setValue(msg);
                                                rootRef.child(ts).child("question").setValue(userInput.getText().toString());
                                                rootRef.child(ts).child("idUser").setValue(user.getUid());

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });





                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

       */

        Intent question = new Intent(ListPertanyaan.this, CreateQuestion.class);
        startActivity(question);


            }
        });









    }


    public static class MovieViewHolder extends RecyclerView.ViewHolder {

        TextView tvMovieName;
        TextView iv_id;

        public MovieViewHolder(View v) {
            super(v);
            tvMovieName = (TextView) v.findViewById(R.id.tv_name);
            iv_id = (TextView) v.findViewById(R.id.iv_id);


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
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
