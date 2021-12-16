package com.anequimplus.utilitarios;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

public class DisplaySet {

    private static int value = 1 ;

    public static int getNumeroDeColunasGrade(Context ctx){
        int v = 0 ;

        switch(ctx.getResources().getConfiguration().orientation){
            case Configuration.ORIENTATION_PORTRAIT : v = 1 ;
            break;
            case Configuration.ORIENTATION_LANDSCAPE: v = 2 ;
            break;
        }
        Log.i("getNumeroDeColunasGrade", "ORIENTATION "+ctx.getResources().getConfiguration().orientation) ;
        Log.i("getNumeroDeColunasGrade", "v "+v) ;
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        Log.i("getNumeroDeColunasGrade", " x "+ size.x) ;
        Log.i("getNumeroDeColunasGrade", " y "+ size.y) ;



        return (int) size.x / 600  ;
    }
}
