package ru.magzyumov.weatherapp.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ru.magzyumov.weatherapp.App;
import ru.magzyumov.weatherapp.BaseActivity;
import ru.magzyumov.weatherapp.Constants;
import ru.magzyumov.weatherapp.Database.Firebase.PhoneClass;
import ru.magzyumov.weatherapp.Database.Location.LocationDao;
import ru.magzyumov.weatherapp.Database.Location.LocationDataSource;
import ru.magzyumov.weatherapp.Database.Location.LocationSource;
import ru.magzyumov.weatherapp.Database.Location.Locations;
import ru.magzyumov.weatherapp.Dialog.AlertDialogWindow;
import ru.magzyumov.weatherapp.GeoMapThreads;
import ru.magzyumov.weatherapp.R;

import static android.content.Context.LOCATION_SERVICE;

public class GeoMapFragment extends Fragment implements Constants, OnMapReadyCallback {
    private EditText textLatitude;
    private EditText textLongitude;
    private TextView textAddress;
    private Marker currentMarker;
    private GoogleMap mMap;
    private View view;
    private BaseActivity baseActivity;
    private FragmentChanger fragmentChanger;
    private AlertDialogWindow alertDialog;
    private LocationDao locationDao;
    private LocationDataSource locationSource;
    private Handler handler;
    private GeoMapThreads geoMapThreads;

    public GeoMapFragment() {
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

        // Инициализируем объект для обращения к базе
        locationDao = App.getInstance().getLocationDao();
        locationSource = new LocationSource(locationDao);

        // Инициализируем поток для определения координат
        geoMapThreads = new GeoMapThreads(GEOMAP);
        handler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_geomap, container, false);

        // Деактивируем Drawer
        fragmentChanger.setDrawerIndicatorEnabled(false);

        // Меняем текст в шапке
        fragmentChanger.changeHeader(getResources().getString(R.string.menu_geomap));

        // Показываем кнопку назад
        fragmentChanger.showBackButton(true);

        // Инициализируем Alert
        alertDialog = new AlertDialogWindow(requireContext(), getString(R.string.menu_geomap),
                getString(R.string.no), getString(R.string.ok), positiveButtonListener);

        // Инициализируем карту
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.geomap);
        mapFragment.getMapAsync(this);

        requestPermissions();

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
        view = null;
        fragmentChanger = null;
        baseActivity = null;
        alertDialog = null;
        textAddress = null;
        positiveButtonListener = null;
        locationListener = null;
        mapLongClickListener = null;
        locationDao = null;
        locationSource = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng moscow = new LatLng(55.753960, 37.620393);
        currentMarker = mMap.addMarker(new MarkerOptions().position(moscow));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(moscow,(float)5));

        mMap.setOnMapLongClickListener(mapLongClickListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == GEO_PERMISSION_REQUEST_CODE) {
            if (grantResults.length == 2 &&
                    (grantResults[0] == PackageManager.PERMISSION_GRANTED ||
                            grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                requestLocation();
            }
        }
    }

    // Инициализация Views
    private void initViews() {
        textLatitude = view.findViewById(R.id.editLat);
        textLongitude = view.findViewById(R.id.editLng);
        textAddress = view.findViewById(R.id.textAddress);
        view.findViewById(R.id.buttonDetect).setOnClickListener(v -> requestLocation());
    }

    private void getNameCity(LatLng location) {
        if (Geocoder.isPresent()) {
            try {
                Geocoder geocoder = new Geocoder(requireContext());
                List<Address> addresses = geocoder.getFromLocation (location.latitude,
                        location.longitude, 1);
                handler.post(() -> writePositionToDB(location.latitude,
                        location.longitude, addresses.get(0).getAddressLine(0)));

                handler.post(() -> alertDialog.show(String.format(getString(R.string.geoSearch),
                        addresses.get(0).getAddressLine(0), String.valueOf(location.latitude),
                        String.valueOf(location.longitude))));

                handler.post(() -> textLatitude.setText(Double.toString(location.latitude)));
                handler.post(() -> textLongitude.setText(Double.toString(location.longitude)));
                handler.post(() -> textAddress.setText(addresses.get(0).getAddressLine(0)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Запрашиваем Permission’ы
    private void requestPermissions() {
        // Проверим, есть ли Permission’ы, и если их нет, запрашиваем их у
        // пользователя
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Запрашиваем координаты
            requestLocation();
        } else {
            // Permission’ов нет, запрашиваем их у пользователя
            requestLocationPermissions();
        }
    }

    // Запрашиваем координаты
    private void requestLocation() {
        // Если Permission’а всё- таки нет, просто выходим: приложение не имеет смысла
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;
        // Получаем менеджер геолокаций
        LocationManager locationManager = (LocationManager) requireContext().getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);

        String provider = locationManager.getBestProvider(criteria, true);
        if (provider != null) {
            locationManager.requestLocationUpdates(provider, 10000, 10, locationListener);
        }
    }

    private void requestLocationPermissions() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) ||
                !ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    GEO_PERMISSION_REQUEST_CODE);
        }
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            double lat = location.getLatitude();
            String latitude = Double.toString(lat);
            textLatitude.setText(latitude);

            double lng = location.getLongitude();
            String longitude = Double.toString(lng);
            textLongitude.setText(longitude);

            LatLng currentPosition = new LatLng(lat, lng);
            currentMarker.setPosition(currentPosition);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, (float)12));

            geoMapThreads.postTask(GEOMAP, () -> getNameCity(currentPosition));

            writePositionToFirebase(latitude, longitude);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private GoogleMap.OnMapLongClickListener mapLongClickListener =
            new GoogleMap.OnMapLongClickListener() {
        @Override
        public void onMapLongClick(LatLng latLng) {
            geoMapThreads.postTask(GEOMAP, () -> getNameCity(latLng));
        }
    };

    private DialogInterface.OnClickListener positiveButtonListener =
            new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Locations currentLocation = locationSource.getLocationById(0);
            locationSource.setLocationSearched(currentLocation, true);
            locationSource.setLocationCurrent(currentLocation, true);
            fragmentChanger.returnFragment();
        }
    };

    private void writePositionToFirebase(String latitude, String longitude){
        SimpleDateFormat dateFormat = new SimpleDateFormat(TIMESTAMP_PATTERN, Locale.getDefault());
        Date date = new Date();

        String id = Settings.Secure.getString(requireContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        String timeStamp = dateFormat.format(date);

        DatabaseReference firebaseDB = FirebaseDatabase.getInstance().getReference();
        PhoneClass phonePosition = new PhoneClass(timeStamp, latitude, longitude);
        firebaseDB.child(PHONES).child(id).child(POSITION).setValue(phonePosition);
    }

    private void writePositionToDB(double latitude, double longitude, String location){
        Locations currentLocation = locationSource.getLocationById(0);
        currentLocation.region = location;
        currentLocation.latitude = latitude;
        currentLocation.longitude = longitude;
        locationSource.updateLocation(currentLocation);
    }
}
