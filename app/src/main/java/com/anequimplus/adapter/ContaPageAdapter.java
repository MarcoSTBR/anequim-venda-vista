package com.anequimplus.adapter;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.anequimplus.anequimdroid.fragment_conta_list.Fragment_conta_aberta;
import com.anequimplus.anequimdroid.fragment_conta_list.Fragment_conta_fechada;

public class ContaPageAdapter extends FragmentPagerAdapter {
    private String[] list ;

    public ContaPageAdapter(FragmentManager fm, String[] list) {
        super(fm);
        this.list = list ;
    }

    @Override
    public Fragment getItem(int position) {
        Log.i("position", "position "+position);
        switch (position) {
            case 0: return new Fragment_conta_aberta() ;
            case 1 : return new Fragment_conta_fechada() ;
            default: return new Fragment_conta_aberta() ;
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
