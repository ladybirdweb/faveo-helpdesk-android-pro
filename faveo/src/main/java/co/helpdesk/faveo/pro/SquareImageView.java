package co.helpdesk.faveo.pro;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

public class SquareImageView extends android.support.v7.widget.AppCompatImageView {
    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int size;

        //If the layout_width or layout_width of this view is set as match_parent or any exact dimension, then this view will use that dimension
        if(MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY ^ MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
            if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY)
                size = width;
            else
                size = height;
        }
        //If the layout_width and layout_width of this view are not set or both set as match_parent or any exact dimension,
        // then this view will use the minimum dimension
        else
            size = Math.min(width, height);
        setMeasuredDimension(size, size);
    }
}
