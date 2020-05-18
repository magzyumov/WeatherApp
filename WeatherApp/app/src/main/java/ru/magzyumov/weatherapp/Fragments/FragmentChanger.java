package ru.magzyumov.weatherapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

public interface FragmentChanger {
    void changeFragment(Fragment newFragment, String newFragmentTag, boolean addToBackStack, Bundle args);
    void changeHeader(String text);
    void changeSubHeader(String text);
    void showBackButton(boolean show);
    void returnFragment();
    void setDrawerIndicatorEnabled(boolean enabled);
}
