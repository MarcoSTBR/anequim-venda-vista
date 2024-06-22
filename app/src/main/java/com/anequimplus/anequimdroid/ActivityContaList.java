package com.anequimplus.anequimdroid;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.anequimplus.adapter.ContaPageAdapter;
import com.google.android.material.tabs.TabLayout;

public class ActivityContaList extends AppCompatActivity {

    private TabLayout tabLayout ;
    private ViewPager viewPager ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conta_list);
        Toolbar toolbar = findViewById(R.id.toolbarConta);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tabLayout = findViewById(R.id.tabConta) ;
        viewPager = findViewById(R.id.viewpager_conta) ;
        viewPager.setAdapter(new ContaPageAdapter(getSupportFragmentManager(), getResources().getStringArray(R.array.conta_list)));
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