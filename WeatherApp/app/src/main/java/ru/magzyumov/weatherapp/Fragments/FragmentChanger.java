package ru.magzyumov.weatherapp.Fragments;

import android.os.Bundle;

public interface FragmentChanger {
    void changeFragment(String tag, Bundle args, boolean addToBackStack);
}
