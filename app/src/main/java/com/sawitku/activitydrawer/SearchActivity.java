package com.sawitku.activitydrawer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import com.sawitku.ProfilUser;
import com.sawitku.R;
import com.sawitku.fadapter.MemberAdapter;
import com.sawitku.fmodels.SearchUser;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private ListView mainListView ;
    private ArrayAdapter<String> listAdapter ;

    ArrayList<SearchUser> memberList;

    MemberAdapter adapter;

    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        memberList = new ArrayList<SearchUser>();
        adapter = new MemberAdapter(SearchActivity.this, R.layout.kebun_search_list_item, memberList);
        mainListView = (ListView) findViewById( R.id.mainListView );

        DatabaseReference qry = FirebaseDatabase.getInstance().getReference().child("user").getRef();

        final ProgressDialog Dialog = new ProgressDialog(SearchActivity.this);
        Dialog.setMessage("Please Wait...");
        Dialog.show();

        qry.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                // Find the ListView resource.



                for (DataSnapshot singleSnapshot : snapshot.getChildren()){
                    SearchUser member = new SearchUser();
                    String nama = (String) singleSnapshot.child("nama").getValue();
                    String id = (String) singleSnapshot.child("id").getValue();

                    String provinsi = "---";
                    if(singleSnapshot.child("provinsi").exists()){
                        provinsi = (String) singleSnapshot.child("provinsi").getValue();
                    }

                    String kabupaten = "---";
                    if(singleSnapshot.child("kecamatan").exists() && singleSnapshot.child("kabupaten").exists()){
                        kabupaten = singleSnapshot.child("kecamatan").getValue().toString()+"/"+singleSnapshot.child("kabupaten").getValue().toString();

                    }

                    String foto = "";
                    if(singleSnapshot.child("provinsi").exists()){
                        foto = (String) singleSnapshot.child("foto").getValue();

                    }


                    member.setId(id);
                    member.setNama(nama);
                    member.setProvinsi(provinsi);
                    member.setKabupaten(kabupaten);
                    member.setFoto(foto);

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


        searchView=(SearchView)findViewById(R.id.searchVieww);
        searchView.setQueryHint("Pencarian...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(SearchActivity.this, query, Toast.LENGTH_LONG).show();
                memberList.clear();
                adapter.notifyDataSetChanged();



                final ProgressDialog Dialog = new ProgressDialog(SearchActivity.this);
                Dialog.setMessage("Please Wait...");
                Dialog.show();


                DatabaseReference qry = FirebaseDatabase.getInstance().getReference().child("user").getRef();

                Query queryRef = qry.orderByChild("nama").startAt(query).endAt(query+ "\uf8ff");

                queryRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        // Find the ListView resource.



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
                // Toast.makeText(SearchActivity.this, newText, Toast.LENGTH_LONG).show();
                return false;
            }
        });


        Button btnCari = (Button)findViewById(R.id.btnCari);

        btnCari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                memberList.clear();
                adapter.notifyDataSetChanged();

                EditText teksSearch = (EditText)findViewById(R.id.searchView);
                String teksPencarian = teksSearch.getText().toString();

                final ProgressDialog Dialog = new ProgressDialog(SearchActivity.this);
                Dialog.setMessage("Please Wait...");
                Dialog.show();


                DatabaseReference qry = FirebaseDatabase.getInstance().getReference().child("user").getRef();

                Query queryRef = qry.orderByChild("nama").startAt(teksPencarian).endAt(teksPencarian+ "\uf8ff");

                queryRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        // Find the ListView resource.


                        for (DataSnapshot singleSnapshot : snapshot.getChildren()){
                            SearchUser member = new SearchUser();
                            String nama = (String) singleSnapshot.child("nama").getValue();
                            String id = (String) singleSnapshot.child("id").getValue();

                            String provinsi = "---";
                            if(singleSnapshot.child("provinsi").exists()){
                                provinsi = (String) singleSnapshot.child("provinsi").getValue();
                            }

                            String kabupaten = "---";
                            if(singleSnapshot.child("kecamatan").exists() && singleSnapshot.child("kabupaten").exists()){
                                kabupaten = singleSnapshot.child("kecamatan").getValue().toString()+"/"+singleSnapshot.child("kabupaten").getValue().toString();

                            }

                            String foto = (String) singleSnapshot.child("foto").getValue();

                            member.setId(id);
                            member.setNama(nama);
                            member.setProvinsi(provinsi);
                            member.setKabupaten(kabupaten);
                            member.setFoto(foto);

                            if(singleSnapshot.child("nama").exists()){
                                memberList.add(member);
                            }
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


        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long id) {
                // TODO Auto-generated method stub

                String  idMember = String.valueOf(memberList.get(position).getId());
                Intent pindah = new Intent(SearchActivity.this, ProfilUser.class);
                pindah.putExtra("id",idMember);
                startActivity(pindah);

            }
        });
        
        
    }
    
    
    

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
