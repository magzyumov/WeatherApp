package ru.magzyumov.weatherapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements Constants{
    private View view;
    final MainPresenter presenter = MainPresenter.getInstance();
    final Logic logic = Logic.getInstance();

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Меняем текст в шапке
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.app_name);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(R.string.app_name);

        view = inflater.inflate(R.layout.fragment_main, container, false);

        //Иницилизируем кнопку-ссылку
        initBottomLink();

        //Обновляем данные
        logic.refreshData();
        makeLine();
        makeHeaderTable();
        return view;
    }

    private void makeHeaderTable(){
        String tempEU;
        TextView textViewCurrent = view.findViewById(R.id.textViewCurrent);
        textViewCurrent.setText(((Integer)presenter.getCurrentTemp()).toString());

        tempEU = (presenter.getSwitch(MainPresenter.Field.SETTING_TEMP_EU)) ? (getString(R.string.celsius)) : (getString(R.string.fahrenheit));
        TextView textViewCurrentEU = view.findViewById(R.id.textViewCurrentEU);
        textViewCurrentEU.setText(tempEU);

        ImageView imageViewCurrent = view.findViewById(R.id.imageViewCurrent);
        imageViewCurrent.setImageResource(R.drawable.bkn_d_line_light);

        getCurrCity(presenter.getCurrentCity());

        //Ставим background картинку
        LinearLayout linearLayout = view.findViewById(R.id.linearLayoutPic);
        linearLayout.setBackgroundResource(getResources().
                getIdentifier(logic.getBackgroundPicName(),"drawable", getActivity().
                        getApplicationContext().getPackageName()));
    }

    private void getCurrCity(String city){
        String cityStr = (city==null)?(getResources().getString(R.string.currentCityName)):city;
        TextView currCity = view.findViewById(R.id.textViewCurrentCity);
        currCity.setText(cityStr);
    }

    private void initBottomLink(){
        TextView textView = view.findViewById(R.id.textViewProvider);
        textView.setText(Html.fromHtml("<a href=" + PROVIDER_URL + "><font color=#AAA>" + getString(R.string.provider) + "</font></a>"));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(PROVIDER_URL));
                startActivity(browser);
            }
        });
    }

    private void makeLine(){
        String string = "textView1H";
        String image = "imageView1H";

        ImageView imageView;
        TextView textView;

        imageView = view.findViewById(R.id.imageViewNow);
        imageView.setImageResource(getResources().getIdentifier(logic.getLinePic(Logic.Field.values()[0]),"drawable", getActivity().getApplicationContext().getPackageName()));

        int cnt = 0;
        for (int i = 1; i <= 24 ; i++) {
            if((logic.getCurrentHour() + i) == 23) cnt = i;
            textView = view.findViewById(getResources().getIdentifier(string.replace("1", String.valueOf(i)),"id", getActivity().getApplicationContext().getPackageName()));
            textView.setText(String.format("%02d:00", (((logic.getCurrentHour()+i) >= 24) ? (i-cnt-1) : (logic.getCurrentHour()+i))));

            imageView = view.findViewById(getResources().getIdentifier(image.replace("1", String.valueOf(i)),"id", getActivity().getApplicationContext().getPackageName()));
            imageView.setImageResource(getResources().getIdentifier(logic.getLinePic(Logic.Field.values()[i]),"drawable", getActivity().getApplicationContext().getPackageName()));
        }
    }
}
