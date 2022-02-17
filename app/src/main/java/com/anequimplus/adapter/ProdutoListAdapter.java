package com.anequimplus.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

import com.anequimplus.anequimdroid.R;
import com.anequimplus.entity.Produto;

import java.text.DecimalFormat;
import java.util.List;

public abstract class ProdutoListAdapter extends RecyclerView.Adapter<ProdutoListAdapter.ProdutoListHolder>{

    private Context ctx ;
    private List<Produto> list ;

    public ProdutoListAdapter(Context ctx, List<Produto> list) {
        this.ctx = ctx;
        this.list = list;
    }

    @NonNull
    @Override
    public ProdutoListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_grade_produto_list, null);
        return new ProdutoListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdutoListHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ProdutoListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView img ;
        TextView codigo ;
        TextView descricao ;
        TextView preco ;

        public ProdutoListHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imageViewProdutoList) ;
            codigo =  itemView.findViewById(R.id.textProdutoCodigo) ;
            descricao = itemView.findViewById(R.id.textProdutoDescricao) ;
            preco = itemView.findViewById(R.id.textProdutoPreco) ;
            itemView.setOnClickListener(this);
        }

        public void bind(Produto it){
            DecimalFormat frmV = new DecimalFormat("R$ #0.00");
            codigo.setText(it.getCodBarra()) ;
            descricao.setText(it.getDescricao());
            preco.setText(frmV.format(it.getPreco()));

            String im = it.getImagem() ;
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

        private ShapeDrawable getAlinharFoto(@ColorInt int color, View v) {
            ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());
            shapeDrawable.setIntrinsicHeight(v.getHeight());
            shapeDrawable.setIntrinsicWidth(v.getWidth());
            shapeDrawable.getPaint().setColor(color);
            return shapeDrawable;
        }

        @Override
        public void onClick(View v) {
           // Toast.makeText(ctx, list.get(getAdapterPosition()).getDescricao(), Toast.LENGTH_SHORT).show();
            selecionado(list.get(getAdapterPosition())) ;
        }
    }

    public abstract void selecionado(Produto p) ;
}
