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
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sawitku.R;
import com.sawitku.fadapter.MemberAdapter;
import com.sawitku.fmodels.SearchUser;

import java.util.ArrayList;

public class ItemSearchFragment extends Fragment {

    private ListView mainListView ;
    private ArrayAdapter<String> listAdapter ;

    ArrayList<SearchUser> memberList;

    MemberAdapter adapter;

    private SearchView searchView;

    public static ItemSearchFragment newInstance() {
        ItemSearchFragment fragment = new ItemSearchFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_search, container, false);

        memberList = new ArrayList<SearchUser>();
        adapter = new MemberAdapter(getActivity(), R.layout.kebun_search_list_item, memberList);

        DatabaseReference qry = FirebaseDatabase.getInstance().getReference().child("user").getRef();

        final ProgressDialog Dialog = new ProgressDialog(getActivity());
        Dialog.setMessage("Please Wait...");
        Dialog.show();

        qry.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                // Find the ListView resource.
                mainListView = (ListView) view.findViewById( R.id.mainListView );


                for (DataSnapshot singleSnapshot : snapshot.getChildren()){
                    SearchUser member = new SearchUser();
                    String nama = (String) singleSnapshot.child("nama").getValue();
                    String id = (String) singleSnapshot.child("id").getValue();
                    member.setId(id);
                    member.setNama(nama);
   //                 memberList.add(member);

                    if(singleSnapshot.child("nama").exists()){
                        memberList.add(member);
                    }

                }

                mainListView.setAdapter(adapter);

                Dialog.hide();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Log.e("Chat", "The read failed: " + error.getText());
            }
        });


        searchView=(SearchView)view.findViewById(R.id.searchVieww);
        searchView.setQueryHint("Pencarian...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getActivity(), query, Toast.LENGTH_LONG).show();
                memberList.clear();
                adapter.notifyDataSetChanged();



                final ProgressDialog Dialog = new ProgressDialog(getActivity());
                Dialog.setMessage("Please Wait...");
                Dialog.show();


                DatabaseReference qry = FirebaseDatabase.getInstance().getReference().child("user").getRef();

                Query queryRef = qry.orderByChild("nama").startAt(query).endAt(query+ "\uf8ff");

                queryRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        // Find the ListView resource.
                        mainListView = (ListView) view.findViewById( R.id.mainListView );


                        for (DataSnapshot singleSnapshot : snapshot.getChildren()){
                            SearchUser member = new SearchUser();
                            String nama = (String) singleSnapshot.child("nama").getValue();
                            String id = (String) singleSnapshot.child("id").getValue();
                            member.setId(id);
                            member.setNama(nama);
                            memberList.add(member);
                        }


                        mainListView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        Dialog.hide();


                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Log.e("Chat", "The read failed: " + error.getText());
                    }
                });

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
               // Toast.makeText(getActivity(), newText, Toast.LENGTH_LONG).show();
                return false;
            }
        });


        Button btnCari = (Button)view.findViewById(R.id.btnCari);

        btnCari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               memberList.clear();
                adapter.notifyDataSetChanged();

                EditText teksSearch = (EditText)view.findViewById(R.id.searchView);
                String teksPencarian = teksSearch.getText().toString();

                final ProgressDialog Dialog = new ProgressDialog(getActivity());
                Dialog.setMessage("Please Wait...");
                Dialog.show();


                DatabaseReference qry = FirebaseDatabase.getInstance().getReference().child("user").getRef();

                Query queryRef = qry.orderByChild("nama").startAt(teksPencarian).endAt(teksPencarian+ "\uf8ff");

                queryRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        // Find the ListView resource.
                        mainListView = (ListView) view.findViewById( R.id.mainListView );


                        for (DataSnapshot singleSnapshot : snapshot.getChildren()){
                            SearchUser member = new SearchUser();
                            String nama = (String) singleSnapshot.child("nama").getValue();
                            String id = (String) singleSnapshot.child("id").getValue();
                            member.setId(id);
                            member.setNama(nama);
                            memberList.add(member);
                        }


                        mainListView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        Dialog.hide();


                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Log.e("Chat", "The read failed: " + error.getText());
                    }
                });

            }
        });




        return view;

    }
}
