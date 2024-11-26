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
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

import com.anequimplus.DaoClass.DaoDbTabela;
import com.anequimplus.anequimdroid.R;
import com.anequimplus.entity.Acompanhamento_produto;
import com.anequimplus.entity.FilterTable;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.entity.ItemSelect;
import com.anequimplus.entity.PedidoItem;
import com.anequimplus.entity.PedidoItemAcomp;
import com.anequimplus.entity.Produto;
import com.anequimplus.listeners.ListenerAcompanhamentoSelect;
import com.anequimplus.valores.ControlePeso;

import java.text.DecimalFormat;
import java.util.List;

public class GradeVendasItensAdapter extends RecyclerView.Adapter<GradeVendasItensAdapter.GradeVendasItensHolder> {

    private Context ctx ;
    private List<PedidoItem> list ;
    private ListenerAcompanhamentoSelect listenerAcompanhamentoSelect ;

    public GradeVendasItensAdapter(Context ctx, List<PedidoItem> list, ListenerAcompanhamentoSelect listenerAcompanhamentoSelect) {
        this.ctx = ctx ;
        this.list = list;
        this.listenerAcompanhamentoSelect = listenerAcompanhamentoSelect ;
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
        TextView preco ;
        ImageButton mais ;
        ImageButton menos ;
        ImageView image ;
        TextView acomp_itens ;

        public GradeVendasItensHolder(@NonNull View itemView) {
            super(itemView);
            descricao = itemView.findViewById(R.id.textProdutoDescricao);
            obs       = itemView.findViewById(R.id.textProdutoObs);
            quantidade = itemView.findViewById(R.id.textQPrPedidoItemS);
            preco      = itemView.findViewById(R.id.textProdutoPreco) ;
            mais      = itemView.findViewById(R.id.ib_maisS);
            menos     = itemView.findViewById(R.id.ib_menosS);
            image     = itemView.findViewById(R.id.imageViewProdutoIt);
            acomp_itens= itemView.findViewById(R.id.acomp_itens);
        }

        public void bind(final PedidoItem it){
            DecimalFormat frmV = new DecimalFormat("R$ #0.00");
            setValores(it);
            preco.setText(frmV.format(it.getItemSelect().getProduto().getPreco()));

            mais.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ifKilo(it)){
                        setPeso(it) ;
                    } else {
                        if (it.getItemSelect().getQuantidade() == 0) {
                            if (getAcompanhamentos(it.getItemSelect().getProduto()).size() > 0)
                                listenerAcompanhamentoSelect.setAcompanhamento(it);
                            else {
                                it.getItemSelect().setQuantidade(it.getItemSelect().getQuantidade() + 1);
                                setValores(it);
                            }
                        } else {
                            it.getItemSelect().setQuantidade(it.getItemSelect().getQuantidade() + 1);
                            setValores(it);
                        }
                    }
                }
            });

            menos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    double q = it.getItemSelect().getQuantidade() - 1 ;
                    if ( q <= 0) {
                        q = 0 ;
                        it.getAcompanhamentos().clear();
                    }
                    it.getItemSelect().setQuantidade(q);
                    setValores(it) ;
                }
            });

            String im = it.getItemSelect().getProduto().getImagem() ;
            byte[] bt = Base64.decode(im, 0);
            Bitmap myBitmap = BitmapFactory.decodeByteArray(bt, 0, bt.length);
            if (myBitmap != null) {
                image.setImageDrawable(getBitmapArredondado(image, myBitmap, 4));
            }
        }

        private void setPeso(final PedidoItem it){
           // Log.i("kil", it.getItemSelect().getProduto().getUnidade().toUpperCase().substring(0,1) ) ;
            new ControlePeso(ctx) {
                @Override
                public void setQuantidade(Double q) {
                    it.getItemSelect().setQuantidade(q);
                    setValores(it);
                }

                @Override
                public void setErro(String msg) {
                    Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
                }
            }.getPeso();
        }

        private boolean ifKilo(PedidoItem item){
            return item.getItemSelect().getProduto().getUnidade().toUpperCase().substring(0,1).equals("K") ;
            //return (prd.getUnidade().toUpperCase().substring(0,1).equals("K")) ;
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

        public void setValores(PedidoItem item){
            ItemSelect it = item.getItemSelect() ;
            item.getItemSelect().setValor(it.getQuantidade() * item.getItemSelect().getPreco());
            item.getItemSelect().setComissao(it.getProduto().getComissao() / 100 * it.getValor());
            DecimalFormat frm = new DecimalFormat("R$ #0.00");
            DecimalFormat qrm = new DecimalFormat("#0.###");
            descricao.setText(it.getProduto().getDescricao());
            obs.setText(it.getObs());
            quantidade.setText(qrm.format(it.getQuantidade()));
            String ac = getAcomp(item) ;
            if (ac.length() == 0)
                acomp_itens.setVisibility(View.GONE);
              else {
                acomp_itens.setVisibility(View.VISIBLE);
                acomp_itens.setText(ac);
            }
        }

        private String getAcomp(PedidoItem it){
            DecimalFormat frm = new DecimalFormat("R$ #0.00");
            String aux = "" ;
            for (PedidoItemAcomp item : it.getAcompanhamentos()){
                String auxi = item.getItemSelect().getProduto().getDescricao() ;
                if (it.getItemSelect().getValor() > 0)
                    auxi  = auxi + " " + frm.format(item.getItemSelect().getValor()) ;
                if (aux.length() == 0)
                    aux = auxi ;
                  else aux = aux +"\n" + auxi ;
            }
            return aux ;
        }


    }

    private List<Acompanhamento_produto> getAcompanhamentos(Produto p) {
        FilterTables filters = new FilterTables() ;
        filters.add(new FilterTable("AFOOD_PRODUTO_ID", "=", String.valueOf(p.getId())));
        return DaoDbTabela.getAcompanhamanto_ProdutoADO(ctx).getList(filters, "");
    }
}
