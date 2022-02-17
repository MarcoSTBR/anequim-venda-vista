package com.anequimplus.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

import com.anequimplus.anequimdroid.R;
import com.anequimplus.entity.Modalidade;

import java.util.List;

public abstract class ModalidadeAdapter extends RecyclerView.Adapter<ModalidadeAdapter.ModalidadeAdapterHolder>{

    private Context ctx ;
    private List<Modalidade> list ;

    public ModalidadeAdapter(Context ctx, List<Modalidade> list) {
        this.ctx = ctx;
        this.list = list;
    }

    @NonNull
    @Override
    public ModalidadeAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_grade_modalidade, null);
     return new ModalidadeAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ModalidadeAdapterHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ModalidadeAdapterHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView img ;
        private TextView textModalidade ;
        public ModalidadeAdapterHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imageViewModalidade) ;
            textModalidade =  itemView.findViewById(R.id.textViewModalidade) ;
            itemView.setOnClickListener(this);
        }

        public void bind(Modalidade m){
            textModalidade.setText(m.getDescricao());
            String im = m.getFoto()  ;
            byte[] bt = Base64.decode(im, 0);
            Bitmap myBitmap = BitmapFactory.decodeByteArray(bt, 0, bt.length);
            if (myBitmap != null) {
                img.setImageDrawable(getBitmapArredondado(img, myBitmap, 4));
            }
        }

        private RoundedBitmapDrawable getBitmapArredondado(ImageView img, Bitmap bmp, int cornerScale){
            RoundedBitmapDrawable rdbd = RoundedBitmapDrawableFactory.create(img.getResources(), bmp) ;
            rdbd.setCornerRadius((float)bmp.getWidth() / cornerScale);
            return rdbd ;
        }

        @Override
        public void onClick(View view) {
           selecionado(list.get(getAdapterPosition())) ;
        }
    }

    public abstract void selecionado(Modalidade modalidade);

}

