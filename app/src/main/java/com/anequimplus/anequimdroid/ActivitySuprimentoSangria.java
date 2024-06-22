package com.anequimplus.anequimdroid;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.anequimplus.adapter.ContaPageSuprimentoSangriaAdapter;
import com.google.android.material.tabs.TabLayout;

public class ActivitySuprimentoSangria extends AppCompatActivity {

    private TabLayout tabLayout ;
    private ViewPager viewPager ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suprimento_sangria);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tabLayout = findViewById(R.id.tabsupsan) ;
        viewPager = findViewById(R.id.viewpager_supsan) ;
        viewPager.setAdapter(new ContaPageSuprimentoSangriaAdapter(getSupportFragmentManager(), getResources().getStringArray(R.array.sup_san_list)));
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return true ;//super.onOptionsItemSelected(item);
    }
}