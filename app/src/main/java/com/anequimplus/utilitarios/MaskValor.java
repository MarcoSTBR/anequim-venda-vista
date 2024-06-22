package com.anequimplus.utilitarios;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.anequimplus.listeners.ListenerMaskValor;

import java.text.DecimalFormat;

public abstract class MaskValor{


   /* public static final String FORMAT_VALOR = "#0.00";
    public static final String FORMAT_CURRENCY = "R$ #0.00";
    public static final String FORMAT_QUANT_PESO = "#####0.000";*/

    /**
     * Método que deve ser chamado para realizar a formatação
     *
     * @param ediTxt
     * @param mask
     * @return
     */
    public static TextWatcher mask(final EditText ediTxt, final MaskTypes mask, final ListenerMaskValor listenerMaskValor) {
        return new TextWatcher() {
            boolean isUpdating;
            boolean isUpdatingInicial = true;
            String valorOriginal = "";

            @Override
            public void afterTextChanged(final Editable s) {
            }

            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {
            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
                Log.i("maskvalor", "isUpdating " + isUpdating + " CharSequence " + s + " start " + start + " before " + before + " count " + count + " valorOriginal " + String.valueOf(valorOriginal));
                String mascara = "";
                if (isUpdatingInicial) {
                    isUpdatingInicial = false;
                    valorOriginal = String.valueOf(ummask(s.toString(), mask));
                    mascara = display(Integer.valueOf(valorOriginal), mask, listenerMaskValor);
                    isUpdating = true;
                    ediTxt.setText(mascara);
                    ediTxt.setSelection(mascara.length());
                    return;
                }
                if (isUpdating) {
                    isUpdating = false;
                    return;
                }
                Log.i("maskvalor", "valorOriginal " + valorOriginal + " count " + count + " lenght " + valorOriginal.length());
                if ((count == 0) && (valorOriginal.length() > 0)) {
                    valorOriginal = valorOriginal.substring(0, valorOriginal.length() - 1);
                } else {
                    valorOriginal += s.toString().substring(s.length() - 1);
                    Log.i("maskvalor", "Char " + s.toString().substring(s.length() - 1));
                }

                mascara = display(getValorInt(valorOriginal), mask, listenerMaskValor);
                isUpdating = true;
                ediTxt.setText(mascara);
                ediTxt.setSelection(mascara.length());
/*
                if (valorOriginal == 0) {
                   // valorOriginal = Integer.parseInt(s.toString()) ;
                } else {
                   // String v =  Integer.parseInt(s.toString()) ;
                   // valorOriginal += Integer.valueOf(v) ;

                }
               // Toast.makeText(ediTxt.getContext(), s, Toast.LENGTH_SHORT).show();
                String mascara = new DecimalFormat(mask).format(valorOriginal / 100) ;
                ediTxt.setText(mascara);
                ediTxt.setSelection(mascara.length());
*/
/*

                final String str = MaskValor.unmask(s.toString(), mask);
                String mascara = "";
                if (isUpdating) {
                    old = str;
                    isUpdating = false;
                    return;
                }
                int i = 0;
                for (final char m : mask.toCharArray()) {
                    if (m != '#' && str.length() > old.length()) {
                        mascara += m;
                        continue;
                    }
                    try {
                        mascara += str.charAt(i);
                    } catch (final Exception e) {
                        break;
                    }
                    i++;
                }
                isUpdating = true;
                ediTxt.setText(mascara);
                ediTxt.setSelection(mascara.length());
*/
            }
        };
    }

    public static int getValorInt(String v) {
        if (v.length() == 0)
            return 0;
        else return Integer.valueOf(v);
    }



    public static String display(final int v, final MaskTypes mk, final ListenerMaskValor listenerMaskValor) {
        DecimalFormat df = new DecimalFormat(mk.formato);
        Double valorFinal = 0.00 ;
        if (mk == MaskTypes.FORMAT_QUANT_PESO)
            valorFinal = v / 1000.0 ;
        else
            valorFinal = v / 100.0 ;
        listenerMaskValor.valorChange(valorFinal) ;
        return df.format(valorFinal);
    }

    public static int ummask(final String s, final MaskTypes mk) {
        Log.i("maskvalor", "s " + s);
        String ret = s.replaceAll("R", "");
        Log.i("maskvalor", "ret " + ret);
        ret = ret.replace("$", "");
        Log.i("maskvalor", "ret " + ret);
        ret = ret.replaceAll(",", "");
        Log.i("maskvalor", "ret " + ret);
        if (ret.length() == 0)
            return 0;
        else return Integer.valueOf(ret.trim());
    }

    public static Double valorDuble(final String s, final MaskTypes mk) {
        Log.i("maskvalor", "s " + s);
        String ret = s.replaceAll("R", "");
        Log.i("maskvalor", "ret " + ret);
        ret = ret.replace("$", "");
        Log.i("maskvalor", "ret " + ret);
        ret = ret.replaceAll(",", "");
        Log.i("maskvalor", "ret " + ret);
        if (ret.length() == 0)
            return 0.0;
        else {
            if (mk == MaskTypes.FORMAT_QUANT_PESO)
                return Integer.valueOf(ret.trim()) / 1000.0;
            else
                return Integer.valueOf(ret.trim()) / 100.0;
        }
    }


}
