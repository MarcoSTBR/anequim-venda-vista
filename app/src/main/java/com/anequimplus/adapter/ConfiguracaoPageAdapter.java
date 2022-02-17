package com.anequimplus.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.anequimplus.anequimdroid.fragment_config.FragmentConfigConexao;
import com.anequimplus.anequimdroid.fragment_config.FragmentConfigImpressoras;
import com.anequimplus.anequimdroid.fragment_config.FragmentConfigOperacional;

public class ConfiguracaoPageAdapter extends FragmentPagerAdapter {

    private String[] list ;


    public ConfiguracaoPageAdapter(FragmentManager fm, String[] list) {
        super(fm);
        this.list = list ;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 1 : return new FragmentConfigConexao() ;
            case 2 : return new FragmentConfigImpressoras();
            default: return new FragmentConfigOperacional() ;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return this.list[position];
    }

    @Override
    public int getCount() {
        return this.list.length;
    }
}
