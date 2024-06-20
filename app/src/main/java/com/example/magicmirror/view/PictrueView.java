package com.example.magicmirror.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

@SuppressLint("AppCompatCustomView")
public class PictrueView extends ImageView {
    public PictrueView (Context context){
        this(context,null);
    }
    public PictrueView(Context context, AttributeSet attrs) {
        super(context,attrs,0);
    }
    public PictrueView(Context context, AttributeSet attrs,int defStyleAttr) {
        super(context,attrs,defStyleAttr);

    }



}
