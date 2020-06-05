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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ru.magzyumov.weatherapp.App;
import ru.magzyumov.weatherapp.BaseActivity;
import ru.magzyumov.weatherapp.Constants;
import ru.magzyumov.weatherapp.Database.Location.LocationDao;
import ru.magzyumov.weatherapp.Database.Location.LocationDataSource;
import ru.magzyumov.weatherapp.Database.Location.LocationSource;
import ru.magzyumov.weatherapp.Dialog.AlertDialogWindow;
import ru.magzyumov.weatherapp.R;

import static android.content.Context.LOCATION_SERVICE;

public class GeoMapFragment extends Fragment implements Constants, OnMapReadyCallback {
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
    private AlertDialogWindow alertDialog;
    private LocationDao locationDao;
    private LocationDataSource locationSource;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_geomap, container, false);

        // Деактивируем Drawer
        fragmentChanger.setDrawerIndicatorEnabled(false);

        // Меняем текст в шапке
        fragmentChanger.changeHeader(getResources().getString(R.string.menu_geomap));
        fragmentChanger.changeSubHeader(getResources().getString(R.string.menu_geomap));

        // Показываем кнопку назад
        fragmentChanger.showBackButton(true);

        // Инициализация маркеров
        markers = new ArrayList<Marker>();

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
        positiveButtonListener = null;
        locationListener = null;
        mapLongClickListener = null;
        locationDao = null;
        locationSource = null;
        coordinateFound = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng sydney = new LatLng(-34, 151);
        currentMarker = mMap.addMarker(new MarkerOptions().position(sydney).title("Текущая позиция"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        mMap.setOnMapLongClickListener(mapLongClickListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == GEO_PERMISSION_REQUEST_CODE) {   // Запрошенный нами
            // Permission
            if (grantResults.length == 2 &&
                    (grantResults[0] == PackageManager.PERMISSION_GRANTED ||
                            grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                // Все препоны пройдены и пермиссия дана
                // Запросим координаты
                requestLocation();
            }
        }
    }

    // Инициализация Views
    private void initViews() {
        textLatitude = view.findViewById(R.id.editLat);
        textLongitude = view.findViewById(R.id.editLng);
        textAddress = view.findViewById(R.id.textAddress);
        initSearchByAddress();
    }

    // Добавляем метки на карту
    private void addMarker(LatLng location){
        String title = Integer.toString(markers.size());
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(location)
                .title(title));
        markers.add(marker);
    }

    private void initSearchByAddress() {
        final EditText textSearch = view.findViewById(R.id.searchAddress);
        view.findViewById(R.id.buttonSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Geocoder geocoder = new Geocoder(requireContext());
                final String searchText = textSearch.getText().toString();
                // Операция получения занимает некоторое время и работает по
                // интернету. Поэтому её необходимо запускать в отдельном потоке
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // Получаем координаты по адресу
                            List<Address> addresses = geocoder.getFromLocationName(searchText, 1);
                            if (addresses.size() > 0){
                                final LatLng location = new LatLng(addresses.get(0).getLatitude(),
                                        addresses.get(0).getLongitude());
                                baseActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mMap.addMarker(new MarkerOptions()
                                                .position(location)
                                                .title(searchText));
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, (float)15));
                                    }
                                });
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    // Получаем адрес по координатам
    private void getAddress(final LatLng location){
        final Geocoder geocoder = new Geocoder(requireContext());
        // Поскольку Geocoder работает по интернету, создаём отдельный поток
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final List<Address> addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);
                    textAddress.post(new Runnable() {
                        @Override
                        public void run() {
                            cityFound = addresses.get(0).getAddressLine(0);
                            textAddress.setText(addresses.get(0).getAddressLine(0));
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
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
        // Если Permission’а всё- таки нет, просто выходим: приложение не имеет
        // смысла
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;
        // Получаем менеджер геолокаций
        LocationManager locationManager = (LocationManager) requireContext().getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);

        // Получаем наиболее подходящий провайдер геолокации по критериям.
        // Но определить, какой провайдер использовать, можно и самостоятельно.
        // В основном используются LocationManager.GPS_PROVIDER или
        // LocationManager.NETWORK_PROVIDER, но можно использовать и
        // LocationManager.PASSIVE_PROVIDER - для получения координат в
        // пассивном режиме
        String provider = locationManager.getBestProvider(criteria, true);
        if (provider != null) {
            // Будем получать геоположение через каждые 10 секунд или каждые
            // 10 метров
            locationManager.requestLocationUpdates(provider, 10000, 10, locationListener);
        }
    }

    // Запрашиваем Permission’ы для геолокации
    private void requestLocationPermissions() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.CALL_PHONE)) {
            // Запрашиваем эти два Permission’а у пользователя
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
            double lat = location.getLatitude(); // Широта
            String latitude = Double.toString(lat);
            textLatitude.setText(latitude);

            double lng = location.getLongitude(); // Долгота
            String longitude = Double.toString(lng);
            textLongitude.setText(longitude);

            String accuracy = Float.toString(location.getAccuracy());   // Точность

            LatLng currentPosition = new LatLng(lat, lng);
            currentMarker.setPosition(currentPosition);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, (float)12));
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
            coordinateFound = latLng;
            getAddress(latLng);
            alertDialog.show("Определить погоду в городе " + cityFound + " ?");
            addMarker(latLng);
        }
    };

    private DialogInterface.OnClickListener positiveButtonListener =
            new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Locations location = new Locations(coordinateFound.latitude,
                    coordinateFound.longitude, cityFound, null);
            Locations currentLocation = (Locations) locationSource.getCurrentLocation();
            if (currentLocation != null){
                currentLocation.isCurrent = false;
                locationSource.updateLocation(currentLocation);
            }
            locationSource.addLocation(location);
            fragmentChanger.returnFragment();
        }
    };
}
