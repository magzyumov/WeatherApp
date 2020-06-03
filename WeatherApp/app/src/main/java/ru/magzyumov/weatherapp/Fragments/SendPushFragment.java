package ru.magzyumov.weatherapp.Fragments;


import android.content.Context;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.HashMap;
import java.util.Map;

import ru.magzyumov.weatherapp.BaseActivity;

import ru.magzyumov.weatherapp.CloudMessaging.RequestSingleton;
import ru.magzyumov.weatherapp.Constants;
import ru.magzyumov.weatherapp.R;

public class SendPushFragment extends Fragment implements Constants {

    //Объявляем переменные
    private View view;
    private BaseActivity baseActivity;
    private FragmentChanger fragmentChanger;
    private EditText edtTitle;
    private EditText edtMessage;

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
        fragmentChanger = null;
        baseActivity = null;
        edtTitle = null;
        edtMessage = null;
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

    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API_URL, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());
                        edtTitle.setText("");
                        edtMessage.setText("");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(requireContext(), "Request error", Toast.LENGTH_LONG).show();
                        Log.e(TAG, "onErrorResponse: Didn't work");
                        Log.e(TAG, serverKey);
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        RequestSingleton.getInstance(requireContext()).addToRequestQueue(jsonObjectRequest);
    }

    private View.OnClickListener sendButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            JSONObject notification = new JSONObject();
            JSONObject notifcationBody = new JSONObject();
            try {
                notifcationBody.put("title", edtTitle.getText().toString());
                notifcationBody.put("body", edtMessage.getText().toString());

                notification.put("to", TOPIC);
                notification.put("notification", notifcationBody);
            } catch (JSONException e) {
                Log.e(TAG, "onCreate: " + e.getMessage() );
            }
            sendNotification(notification);
        }
    };
}
