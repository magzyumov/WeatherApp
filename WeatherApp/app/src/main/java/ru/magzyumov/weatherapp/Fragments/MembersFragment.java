package ru.magzyumov.weatherapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ru.magzyumov.weatherapp.BaseActivity;
import ru.magzyumov.weatherapp.Constants;
import ru.magzyumov.weatherapp.Database.Firebase.Installation;
import ru.magzyumov.weatherapp.Database.Firebase.Position;
import ru.magzyumov.weatherapp.R;


public class MembersFragment extends Fragment implements Constants, OnMapReadyCallback {
    private DatabaseReference firebaseDB;
    private GoogleMap mMap;
    private View view;
    private BaseActivity baseActivity;
    private FragmentChanger fragmentChanger;
    private List<LatLng> members;

    public MembersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof FragmentChanger) fragmentChanger = (FragmentChanger) context;
        if(context instanceof BaseActivity) baseActivity = (BaseActivity) context;
    }

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);

        firebaseDB = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_members, container, false);

        // Деактивируем Drawer
        fragmentChanger.setDrawerIndicatorEnabled(false);

        // Меняем текст в шапке
        fragmentChanger.changeHeader(getResources().getString(R.string.menu_members));

        // Показываем кнопку назад
        fragmentChanger.showBackButton(true);

        // Инициализация маркеров
         members = new ArrayList<LatLng>();

        // Инициализируем карту
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.geomap);
        mapFragment.getMapAsync(this);

        getMembers();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Скрываем кнопку назад
        fragmentChanger.showBackButton(false);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Освобождаем ресурсы
        mMap = null;
        view = null;
        fragmentChanger = null;
        baseActivity = null;
        firebaseDB = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    private void addMarker(LatLng location, String name){
        if(name == null) name = String.valueOf(members.size());
        mMap.addMarker(new MarkerOptions()
                .position(location)
                .title(name));
        members.add(location);
    }

    private void getMembers(){
        firebaseDB.child(PHONES).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    Position position = null;
                    Installation installation = null;
                    LatLng location = null;
                    String name = null;


                    if(singleSnapshot.child(POSITION).getKey().equals(POSITION)){
                        position = singleSnapshot.child(POSITION).getValue(Position.class);
                        if(position!= null) {
                            location = new LatLng(Double.valueOf(position.getLatitude()),
                                    Double.valueOf(position.getLongitude()));
                        }
                    }
                    if(singleSnapshot.child(INSTALLATION).getKey().equals(INSTALLATION)){
                        installation = singleSnapshot.child(INSTALLATION).getValue(Installation.class);
                        if(installation!= null) {
                            name = installation.getName();
                        }
                    }
                    if(location != null && name != null) addMarker(location, name);
                    if(location != null && name == null) addMarker(location, null);
                }
                if(members != null){
                    if(members.size()>0) mMap.moveCamera(CameraUpdateFactory
                            .newLatLngZoom(members.get(0),(float)4));
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}
