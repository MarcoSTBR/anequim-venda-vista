package com.anequimplus.adapter;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.anequimplus.anequimdroid.fragment_suprimento_sangria.Fragment_sangria;
import com.anequimplus.anequimdroid.fragment_suprimento_sangria.Fragment_suprimento;

public class ContaPageSuprimentoSangriaAdapter extends FragmentPagerAdapter {

    private String[] list ;

    public ContaPageSuprimentoSangriaAdapter(FragmentManager fm, String[] list) {
        super(fm);
        this.list = list ;
    }

    @Override
    public Fragment getItem(int position) {
        Log.i("position", "position "+position);
        switch (position) {
            case 0: return new Fragment_suprimento() ;
            case 1: return new Fragment_sangria() ;
            default: return new Fragment_suprimento() ;
        }
    }

    @Override
    public int getCount() {
        return list.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return this.list[position];
    }
}
