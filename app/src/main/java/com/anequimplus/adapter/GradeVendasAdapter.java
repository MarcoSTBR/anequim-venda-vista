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
import com.anequimplus.entity.GradeVendas;

import java.util.List;

public abstract class GradeVendasAdapter extends RecyclerView.Adapter<GradeVendasAdapter.GradeVendasAdapterHolder> {

    private Context ctx ;
    private List<GradeVendas> list ;

    public GradeVendasAdapter(Context ctx, List<GradeVendas> list){
        this.ctx = ctx ;
        this.list = list ;
    }

    @NonNull
    @Override
    public GradeVendasAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_grade_grade_vendas, null);
        return new GradeVendasAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GradeVendasAdapterHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class GradeVendasAdapterHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txt ;
        ImageView img ;

        public GradeVendasAdapterHolder(@NonNull View itemView) {
            super(itemView);
            txt = (TextView) itemView.findViewById(R.id.descricaoGradeVendas);
            img =  (ImageView) itemView.findViewById(R.id.fotoGradeVendas);
            itemView.setOnClickListener(this);
        }

        public void bind(GradeVendas g){
            txt.setText(g.getDescricao()) ;
            String im = g.getImagem() ;
            //Base64.Decoder base64 = Base64.getDecoder();
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
        public void onClick(View v) {
            selecionado(list.get(getAdapterPosition())) ;
        }
    }

    public abstract void selecionado(GradeVendas g) ;

}
