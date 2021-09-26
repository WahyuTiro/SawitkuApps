package com.sawitku.vfragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sawitku.ProfilUser;
import com.sawitku.R;
import com.sawitku.fmodels.MapMember;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class MapDistributorFragment extends Fragment implements
        GoogleMap.OnMarkerClickListener {

    MapView mMapView;
    private GoogleMap googleMap;
    ArrayList<MapMember> memberList;



    public static MapDistributorFragment newInstance() {
        MapDistributorFragment fragment = new MapDistributorFragment();
        return fragment;
    }





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_map_petani, container, false);

        memberList = new ArrayList<MapMember>();



        CardView cardMember = (CardView)rootView.findViewById(R.id.card_view);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }


        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                DatabaseReference qry = FirebaseDatabase.getInstance().getReference().child("user").getRef();

                qry.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        for (DataSnapshot singleSnapshot : snapshot.getChildren()) {

                            //jika memiliki koordinat dan tipe maka tampilkan di peta

                            if (singleSnapshot.child("lat").exists() && singleSnapshot.child("tipe").exists()) {

                                //

                                if(singleSnapshot.child("tipe").getValue().toString().contains("4")) {

                                    String str = singleSnapshot.child("lat").getValue().toString();

                                    if (str != null && !str.isEmpty()) {

                                        MapMember member = new MapMember();

                                        String id = (String) singleSnapshot.child("id").getValue();
                                        String lat = (String) singleSnapshot.child("lat").getValue();
                                        String log = (String) singleSnapshot.child("log").getValue();
                                        String nama = (String) singleSnapshot.child("nama").getValue();
                                        String tipe = (String) singleSnapshot.child("tipe").getValue();
                                        String gambar = (String) singleSnapshot.child("foto").getValue();
                                        String provinsi = (String) singleSnapshot.child("provinsi").getValue();
                                        String kecamatan = (String) singleSnapshot.child("kecamatan").getValue();

                                        member.setId(id);
                                        member.setNama(nama);
                                        member.setTipe(tipe);
                                        member.setGambar(gambar);
                                        member.setProvinsi(provinsi);
                                        member.setKecamatan(kecamatan);

                                        memberList.add(member);


                                        double latitude = Double.parseDouble(lat);
                                        double longitude = Double.parseDouble(log);

                                        // For dropping a marker at a point on the Map
                                        LatLng sydney = new LatLng(latitude, longitude);


                                        googleMap.addMarker(new MarkerOptions().position(sydney).icon(getMarkerIcon("#26A69A")));


                                    }

                                }
                                //
                            }

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Log.e("Chat", "The read failed: " + error.getText());
                    }
                });



                // For dropping a marker at a point on the Map
           //     LatLng medan = new LatLng(3.5773702, 98.6583127);
                // googleMap.addMarker(new MarkerOptions().position(sydney).title("Kebun 1").snippet("Keterangan Kebun1"));

//                // For zooming automatically to the location of the marker
//                CameraPosition cameraPosition = new CameraPosition.Builder().target(medan).zoom(5).build();
//                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {

                        Toast.makeText(getActivity(), "Pavorito", Toast.LENGTH_SHORT).show();

                        CardView cardMember = (CardView)rootView.findViewById(R.id.card_view);
                        TextView info_text = (TextView) rootView.findViewById(R.id.info_text);
                        TextView iv_id = (TextView) rootView.findViewById(R.id.iv_id);
                        TextView tipe_text = (TextView) rootView.findViewById(R.id.tipe_text);
                        TextView alamat_text = (TextView) rootView.findViewById(R.id.alamat_text);
                        ImageView imageProfil = (ImageView)rootView.findViewById(R.id.imageProfil);

                        cardMember.setVisibility(View.VISIBLE);

                        int idMap = Integer.parseInt(removeFirstChar(marker.getId()));
                        int idTipe = Integer.parseInt(memberList.get(idMap).getTipe());



                        String Nama = memberList.get(idMap).getNama();
                        String Gambar = memberList.get(idMap).getGambar();
                        String kecamatan = memberList.get(idMap).getKecamatan();
                        String provinsi = memberList.get(idMap).getProvinsi();

                        String TipeTeks = "";

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

                        iv_id.setText(memberList.get(idMap).getId());
                        info_text.setText(Nama);
                        tipe_text.setText(TipeTeks);
                        alamat_text.setText(kecamatan + "," +provinsi);

                        Glide.with(getActivity()).load(Gambar)
                                .bitmapTransform(new CropCircleTransformation(getActivity()))
                                .into(imageProfil);

                        System.out.println("ID PETA " + marker.getId());

                        return true;

                    }
                });

                CameraUpdate center=
                        CameraUpdateFactory.newLatLng(new LatLng(3.5776539,98.6668215));
                CameraUpdate zoom= CameraUpdateFactory.zoomTo(4);

                googleMap.moveCamera(center);
                googleMap.animateCamera(zoom);

            }
        });






        cardMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView iv_id = (TextView) rootView.findViewById(R.id.iv_id);

                Intent pindah = new Intent(getActivity(), ProfilUser.class);
                pindah.putExtra("id",iv_id.getText().toString());
                startActivity(pindah);
            }
        });


        return rootView;

    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }




    // method definition
    public BitmapDescriptor getMarkerIcon(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }

    public String removeFirstChar(String s){
        return s.substring(1);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        Toast.makeText(getActivity(), "msg msg", Toast.LENGTH_SHORT).show();


        return false;
    }
}
