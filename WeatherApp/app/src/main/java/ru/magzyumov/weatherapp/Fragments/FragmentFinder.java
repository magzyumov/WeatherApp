package ru.magzyumov.weatherapp.Fragments;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class FragmentFinder {

    //Поле менеджера фрагментов получаемое от активити
    private FragmentManager fragmentManager;

    //Контруктор класса
    public FragmentFinder(FragmentManager fragmentManager){
        this.fragmentManager = fragmentManager;
    }

    //Метод поиска фрагмента
    public Fragment findFragment (String tag){
        return fragmentManager.findFragmentByTag(tag);
    }
}
