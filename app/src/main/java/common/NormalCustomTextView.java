package common;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by Ashish.Kumar on 17-01-2018.
 */

public class NormalCustomTextView extends android.support.v7.widget.AppCompatTextView {

    public NormalCustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public NormalCustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NormalCustomTextView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "font.ttf");
        setTypeface(tf);
    }

}