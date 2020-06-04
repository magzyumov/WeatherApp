package ru.magzyumov.weatherapp.Fragments;


import android.content.Context;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import okhttp3.ResponseBody;
import retrofit2.Callback;
import ru.magzyumov.weatherapp.BaseActivity;

import ru.magzyumov.weatherapp.CloudMessaging.ApiClient;
import ru.magzyumov.weatherapp.CloudMessaging.ApiInterface;
import ru.magzyumov.weatherapp.CloudMessaging.Model.DataModel;
import ru.magzyumov.weatherapp.CloudMessaging.Model.NotificationModel;
import ru.magzyumov.weatherapp.CloudMessaging.Model.RootModel;
import ru.magzyumov.weatherapp.Constants;
import ru.magzyumov.weatherapp.R;

public class SendPushFragment extends Fragment implements Constants {

    //Объявляем переменные
    private View view;
    private BaseActivity baseActivity;
    private FragmentChanger fragmentChanger;
    private EditText edtTitle;
    private EditText edtMessage;
    private ApiClient apiClient;

    public SendPushFragment() {
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

        apiClient = new ApiClient();

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_send_push, container, false);

        initView();

        initMessaging();

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

        // Освободдаем ресурсы
        view = null;
        fragmentChanger = null;
        baseActivity = null;
        edtTitle = null;
        edtMessage = null;
        apiClient = null;
    }

    private void initView(){
        // Деактивируем Drawer
        fragmentChanger.setDrawerIndicatorEnabled(false);

        //Меняем текст в шапке
        fragmentChanger.changeHeader(getResources().getString(R.string.menu_send_push));
        fragmentChanger.changeSubHeader(getResources().getString(R.string.menu_send_push));

        //Показываем кнопку назад
        fragmentChanger.showBackButton(true);
    }

    private void initMessaging(){
        edtTitle = view.findViewById(R.id.edtTitle);
        edtMessage = view.findViewById(R.id.edtMessage);
        Button btnSend = view.findViewById(R.id.btnSend);

        btnSend.setOnClickListener(sendButtonListener);
    }

    private View.OnClickListener sendButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sendNotificationToUser(TOPIC);
            edtTitle.setText("");
            edtMessage.setText("");
        }
    };

    private void sendNotificationToUser(String token) {
        RootModel rootModel = new RootModel(token,
                new NotificationModel(edtTitle.getText().toString(), edtMessage.getText().toString()),
                new DataModel("Name", "30"));

        ApiInterface apiService =  apiClient.getClient().create(ApiInterface.class);
        retrofit2.Call<ResponseBody> responseBodyCall = apiService.sendNotification(rootModel);

        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Log.e(TAG,"Successfully notification send by using retrofit.");
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
