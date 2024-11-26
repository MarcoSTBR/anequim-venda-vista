package com.anequimplus.anequimdroid.fragment_config;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.adapter.ConfigImpressoraAdapter;
import com.anequimplus.anequimdroid.R;
import com.anequimplus.utilitarios.DisplaySet;

public class FragmentConfigImpressoras extends Fragment {


    private RecyclerView grade ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_config_impressoras, container, false);
        grade = view.findViewById(R.id.grade_config_impressoras_list) ;
        GridLayoutManager layoutManager=new GridLayoutManager(getContext(), DisplaySet.getNumeroDeColunasGrade(getContext()));
        grade.setLayoutManager(layoutManager);
        grade.setAdapter(new ConfigImpressoraAdapter(getContext(), DaoDbTabela.getImpressoraADO(getContext()).getList()));
        return view ;
    }
}