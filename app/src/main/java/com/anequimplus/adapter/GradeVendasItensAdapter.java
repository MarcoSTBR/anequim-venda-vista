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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

import com.anequimplus.anequimdroid.R;
import com.anequimplus.entity.ItenSelect;

import java.text.DecimalFormat;
import java.util.List;

public class GradeVendasItensAdapter extends RecyclerView.Adapter<GradeVendasItensAdapter.GradeVendasItensHolder> {

    private Context ctx ;
    private List<ItenSelect> list ;

    public GradeVendasItensAdapter(Context ctx, List<ItenSelect> list) {
        this.ctx = ctx ;
        this.list = list;
    }

    @NonNull
    @Override
    public GradeVendasItensHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_grade_grade_venda_item, null);
        return new GradeVendasItensHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GradeVendasItensHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class GradeVendasItensHolder extends RecyclerView.ViewHolder {

        TextView descricao ;
        TextView obs ;
        TextView quantidade ;
        ImageButton mais ;
        ImageButton menos ;
        ImageView image ;


        public GradeVendasItensHolder(@NonNull View itemView) {
            super(itemView);
            descricao = itemView.findViewById(R.id.textProdutoDescricao);
            obs       = itemView.findViewById(R.id.textProdutoObs);
            quantidade = itemView.findViewById(R.id.textQPrPedidoItemS);
            mais      = itemView.findViewById(R.id.ib_maisS);
            menos     = itemView.findViewById(R.id.ib_menosS);
            image     = itemView.findViewById(R.id.imageViewProdutoIt);

        }


        public void bind(ItenSelect it){
            final ItenSelect pit = it;
            setValores(pit);

            mais.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pit.setQuantidade(pit.getQuantidade() + 1);
                    setValores(pit) ;
                }
            });

            menos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    double q = pit.getQuantidade() - 1 ;
                    if ( q < 0) q = 0 ;
                    pit.setQuantidade(q);
                    setValores(pit) ;
                }
            });

            String im = pit.getProduto().getImagem() ;
            byte[] bt = Base64.decode(im, 0);
            Bitmap myBitmap = BitmapFactory.decodeByteArray(bt, 0, bt.length);
            if (myBitmap != null) {
                image.setImageDrawable(getBitmapArredondado(image, myBitmap, 4));
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

        public void setValores(ItenSelect it){
            it.setValor(it.getQuantidade() * it.getPreco());
            it.setComissao(it.getProduto().getComissao() / 100 * it.getValor());
            DecimalFormat frm = new DecimalFormat("R$ #0.00");
            DecimalFormat qrm = new DecimalFormat("#0.###");
            descricao.setText(it.getProduto().getDescricao());
            obs.setText(it.getObs());
            quantidade.setText(qrm.format(it.getQuantidade()));
        }

    }
}
