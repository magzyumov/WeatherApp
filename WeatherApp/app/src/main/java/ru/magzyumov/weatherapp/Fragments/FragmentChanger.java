package ru.magzyumov.weatherapp.Fragments;

import android.os.Bundle;

public interface FragmentChanger {
    void changeFragment(String tag, Bundle args, boolean addToBackStack);
    void changeHeader(String text);
    void changeSubHeader(String text);
    void showBackButton(boolean show);
    void returnFragment();
}
