package ru.magzyumov.weatherapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

import ru.magzyumov.weatherapp.BaseActivity;
import ru.magzyumov.weatherapp.Constants;
import ru.magzyumov.weatherapp.Dialog.AlertDialogWindow;
import ru.magzyumov.weatherapp.R;


public class MembersFragment extends Fragment implements Constants, OnMapReadyCallback {
    private DatabaseReference firebaseDB;
    private String id;
    private int size;
    private EditText textLatitude;
    private EditText textLongitude;
    private TextView textAddress;
    private Marker currentMarker;
    private GoogleMap mMap;
    private View view;
    private String cityFound;
    private LatLng coordinateFound;
    private BaseActivity baseActivity;
    private FragmentChanger fragmentChanger;
    private List<Marker> markers;
    private List<LatLng> members;
    private AlertDialogWindow alertDialog;

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

        // Инициализируем Alert
        alertDialog = new AlertDialogWindow(requireContext(), getString(R.string.menu_geomap),
                getString(R.string.ok));

        firebaseDB = FirebaseDatabase.getInstance().getReference();
        id = Settings.Secure.getString(requireContext().getContentResolver(), Settings.Secure.ANDROID_ID);
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
        markers = new ArrayList<Marker>();

        // Инициализируем карту
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.geomap);
        mapFragment.getMapAsync(this);

        initViews();

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
        textLatitude = null;
        textLongitude = null;
        currentMarker = null;
        markers =  null;
        alertDialog = null;
        textAddress = null;
        coordinateFound = null;
        firebaseDB = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        try{
            for(LatLng member : members){
                addMarker(member);
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLng(members.get(0)));
        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    // Инициализация Views
    private void initViews() {
        textLatitude = view.findViewById(R.id.editLat);
        textLongitude = view.findViewById(R.id.editLng);
        getMembers();
    }

    // Добавляем метки на карту
    private void addMarker(LatLng location){
        String title = Integer.toString(markers.size());
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(location)
                .title(title));
        markers.add(marker);
    }

    private void getMembers(){
        firebaseDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                size = (int) dataSnapshot.getChildrenCount();
                Log.e(TAG, dataSnapshot.getKey());

                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){

                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        firebaseDB.child(PHONES).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                size = (int) dataSnapshot.getChildrenCount();

                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}
