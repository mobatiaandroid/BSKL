package com.mobatia.bskl.manager;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;


/**
 * Created by gayatri on 30/3/17.
 */
public class TextViewFontSansProRegular extends TextView {
    public TextViewFontSansProRegular(Context context) {
        super(context);
        Typeface type = Typeface.createFromAsset(context.getAssets(),"fonts/SourceSansPro-Regular.otf" );
        this.setTypeface(type);
        //this.setTextColor(context.getResources().getColor(R.color.white));
    }

    public TextViewFontSansProRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface type = Typeface.createFromAsset(context.getAssets(),"fonts/SourceSansPro-Regular.otf");
        this.setTypeface(type);
        //this.setTextColor(context.getResources().getColor(R.color.white));

    }

    public TextViewFontSansProRegular(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Typeface type = Typeface.createFromAsset(context.getAssets(),"fonts/SourceSansPro-Regular.otf" );
        this.setTypeface(type);
        //this.setTextColor(context.getResources().getColor(R.color.white));

    }


}