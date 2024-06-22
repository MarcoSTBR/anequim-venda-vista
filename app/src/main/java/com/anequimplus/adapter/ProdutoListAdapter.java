package com.anequimplus.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.anequimplus.valores.ControlePeso;
import com.anequimplus.entity.Acompanhamento_produto;
import com.anequimplus.entity.FilterTable;
import com.anequimplus.entity.FilterTables;
import com.anequimplus.entity.ItemSelect;
import com.anequimplus.entity.PedidoItem;
import com.anequimplus.entity.PedidoItemAcomp;
import com.anequimplus.entity.Produto;
import com.anequimplus.listeners.ListenerAcompanhamentoSelect;

import java.text.DecimalFormat;
import java.util.List;

public class ProdutoListAdapter extends RecyclerView.Adapter<ProdutoListAdapter.ProdutoListHolder>{

    private Context ctx ;
    private ListenerAcompanhamentoSelect listenerAcompanhamentoSelect ;
    private List<PedidoItem> list ;

    public ProdutoListAdapter(Context ctx, List<PedidoItem> list, ListenerAcompanhamentoSelect listenerAcompanhamentoSelect) {
        this.ctx = ctx;
        this.list = list ;
        this.listenerAcompanhamentoSelect = listenerAcompanhamentoSelect ;
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

    class ProdutoListHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView codigo;
        TextView descricao;
        TextView preco;

        TextView quantidade;
        ImageView mais;
        ImageView menos;

        TextView acomp ;

        public ProdutoListHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imageViewProdutoList);
            codigo = itemView.findViewById(R.id.textProdutoCodigo);
            descricao = itemView.findViewById(R.id.textProdutoDescricao);
            preco = itemView.findViewById(R.id.textProdutoPreco);
            quantidade = itemView.findViewById(R.id.qPrdPedidoItemS);
            mais = itemView.findViewById(R.id.ib_prdmais);
            menos = itemView.findViewById(R.id.ib_prdmenos);
            acomp = itemView.findViewById(R.id.acomp);

          //  itemView.setOnClickListener(this);
        }

        public void bind(final PedidoItem item) {
            DecimalFormat frmV = new DecimalFormat("R$ #0.00");
            final Produto prd = item.getItemSelect().getProduto() ;
            codigo.setText(item.getItemSelect().getProduto().getCodBarra());
            descricao.setText(item.getItemSelect().getProduto().getDescricao());
            preco.setText(frmV.format(item.getItemSelect().getPreco()));
            setValores(item);
            mais.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ifKilo(item)){
                        setPeso(item) ;
                    } else {
                        if (item.getItemSelect().getQuantidade() == 0) {
                            if (getAcompanhamentos(item.getItemSelect().getProduto()).size() > 0)
                                listenerAcompanhamentoSelect.setAcompanhamento(item);
                            else {
                                item.getItemSelect().setQuantidade(item.getItemSelect().getQuantidade() + 1);
                                listenerAcompanhamentoSelect.ok(item);
                                setValores(item);
                            }
                        } else {
                            item.getItemSelect().setQuantidade(item.getItemSelect().getQuantidade() + 1);
                            listenerAcompanhamentoSelect.ok(item);
                            setValores(item);
                        }
                    }
                }
            });

            menos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    item.getItemSelect().setQuantidade(item.getItemSelect().getQuantidade() - 1);
                    if (item.getItemSelect().getQuantidade() <= 0) {
                        item.getItemSelect().setQuantidade(0);
                        item.getAcompanhamentos().clear();
                    }
                    setValores(item);
                }
            });

            String im = prd.getImagem();
            Log.i("tamanho_foto", "id "+prd.getId()+" "+prd.getDescricao()+" tamanho im "+" "+im.length()) ;
            byte[] bt = Base64.decode(im, 0);
            Bitmap myBitmap = BitmapFactory.decodeByteArray(bt, 0, bt.length);
            if (myBitmap != null) {
                    img.setImageDrawable(getBitmapArredondado(img, myBitmap, 4));
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
                    //setQuantidade(0.0);

                }
            }.getPeso();
        }

        private boolean ifKilo(PedidoItem item){
           return item.getItemSelect().getProduto().getUnidade().toUpperCase().substring(0,1).equals("K") ;
           //return (prd.getUnidade().toUpperCase().substring(0,1).equals("K")) ;
        }

        private void setValores(PedidoItem item) {
            ItemSelect it = item .getItemSelect() ;
            DecimalFormat frmQ = new DecimalFormat("#0.###");
            if (ifKilo(item)) frmQ = new DecimalFormat("0.000"); //Muda a mascara
            it.setPreco(it.getProduto().getPreco());
            it.setValor(it.getQuantidade() * it.getPreco());
            it.setComissao(it.getValor() *  it.getProduto().getComissao() / 100);
            quantidade.setText(frmQ.format(it.getQuantidade()));
            String qd = ""  ;
            for (PedidoItemAcomp ac : item.getAcompanhamentos()){
                if (qd.length() == 0)
                    qd = getAcomp(ac) ;
                 else
                    qd = qd + "\n" + getAcomp(ac) ;
            }
            if (qd.length() == 0)
                acomp.setVisibility(View.GONE);
              else {
                acomp.setVisibility(View.VISIBLE);
                acomp.setText(qd);
            }
        }

        private String getAcomp(PedidoItemAcomp ac){
            DecimalFormat frmV = new DecimalFormat("R$ #0.00");
            String aux = ac.getItemSelect().getProduto().getDescricao() ;
            if (ac.getItemSelect().getValor() > 0){
                aux =  aux + " - " + frmV.format(ac.getItemSelect().getValor()) ;
            }
            return aux ;
        }

        private RoundedBitmapDrawable getBitmapArredondado(ImageView img, Bitmap bmp, int cornerScale) {
            RoundedBitmapDrawable rdbd = RoundedBitmapDrawableFactory.create(img.getResources(), bmp);
            rdbd.setCornerRadius((float) bmp.getWidth() / cornerScale);
            return rdbd;
        }

        private ShapeDrawable getAlinharFoto(@ColorInt int color, View v) {
            ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());
            shapeDrawable.setIntrinsicHeight(v.getHeight());
            shapeDrawable.setIntrinsicWidth(v.getWidth());
            shapeDrawable.getPaint().setColor(color);
            return shapeDrawable;
        }
    }

    private List<Acompanhamento_produto> getAcompanhamentos(Produto p) {
        FilterTables filters = new FilterTables() ;
        filters.add(new FilterTable("AFOOD_PRODUTO_ID", "=", String.valueOf(p.getId())));
        return DaoDbTabela.getAcompanhamanto_ProdutoADO(ctx).getList(filters, "");
    }


}
