/*
 * Copyright (c) 2017. Truiton (http://www.truiton.com/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 * Mohit Gupt (https://github.com/mohitgupt)
 *
 */

package com.sawitku.vfragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sawitku.DownloadFileActivity;
import com.sawitku.R;
import com.sawitku.fmodels.Files;

public class ItemDownloadFragment extends Fragment {

    //private SectionsPagerAdapter mSectionsPagerAdapter;

    ScaleAnimation shrinkAnim;
    //Getting reference to Firebase Database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseReference = database.getReference();
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mLayoutManager;
    private TextView tvNoMovies;


    // button to show progress dialog
    Button btnShowProgress;

    // Progress Dialog
    private ProgressDialog pDialog;
    ImageView my_image;
    // Progress dialog type (0 - for Horizontal progress bar)
    public static final int progress_bar_type = 0;

    public static ItemDownloadFragment newInstance() {
        ItemDownloadFragment fragment = new ItemDownloadFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_item_qa, container, false);

        //Initializing our Recyclerview
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        tvNoMovies = (TextView) view.findViewById(R.id.tv_no_movies);

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
        final FirebaseRecyclerAdapter<Files, MovieViewHolder> adapter = new FirebaseRecyclerAdapter<Files, MovieViewHolder>(
                Files.class,
                R.layout.download_list_item,
                MovieViewHolder.class,
                //referencing the node where we want the database to store the data from our Object
                mDatabaseReference.child("file").getRef()
        ) {
            @Override
            protected void populateViewHolder(final MovieViewHolder viewHolder, Files kat, final int position) {
                if (tvNoMovies.getVisibility() == View.VISIBLE) {
                    tvNoMovies.setVisibility(View.GONE);
                }
                viewHolder.tvMovieName.setText(kat.getNama());
                viewHolder.iv_id.setText(String.valueOf(kat.getId()));
                viewHolder.iv_url.setText(String.valueOf(kat.getFile_url()));
                viewHolder.iv_caption.setText(String.valueOf(kat.getCaption()));




            }


            @Override
            public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                final MovieViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                viewHolder.setOnClickListener(new MovieViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String namafile =  viewHolder.tvMovieName.getText().toString();
                        String caption = viewHolder.iv_caption.getText().toString();
                        String url = viewHolder.iv_url.getText().toString();

                        Intent pindah = new Intent(getActivity(), DownloadFileActivity.class);
                        pindah.putExtra("nama", namafile);
                        pindah.putExtra("caption",caption);
                        pindah.putExtra("url", url);
                        startActivity(pindah);






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



        return view;

    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {

        TextView tvMovieName;
        TextView iv_id;
        TextView iv_caption;
        TextView iv_url;
        private MovieViewHolder.ClickListener mClickListener;

        public MovieViewHolder(View v) {
            super(v);
            tvMovieName = (TextView) v.findViewById(R.id.tv_name);
            iv_id = (TextView) v.findViewById(R.id.iv_id);
            iv_caption = (TextView) v.findViewById(R.id.iv_caption);
            iv_url = (TextView) v.findViewById(R.id.iv_url);


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

        public void setOnClickListener(MovieViewHolder.ClickListener clickListener) {
            mClickListener = clickListener;
        }

        //Interface to send callbacks...
        public interface ClickListener {
            public void onItemClick(View view, int position);

            public void onItemLongClick(View view, int position);
        }


    }









}
