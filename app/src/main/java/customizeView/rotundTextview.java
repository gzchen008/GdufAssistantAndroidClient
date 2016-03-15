package customizeView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import vanroid.com.gdufassistant20.R;

/**
 * Created by kami on 15/9/10.
 */
public class rotundTextview extends TextView{

    private int radius;
    private float mTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, getResources().getDisplayMetrics());

    public rotundTextview(Context context) {
        this(context, null);
        initView();
    }

    public rotundTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        setGravity(Gravity.CENTER);
        setTextColor(Color.WHITE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        radius = Math.max(w, h);
        setMeasuredDimension(radius, radius);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(0xff00bfff);

        canvas.drawOval(1, 1, radius - 1, radius - 1, paint);

    }

    private void drawTextOnCenter(Canvas canvas) {
        Paint paint = new Paint(); paint.setColor(0xffffffff);
        paint.setTextSize(mTextSize);
        canvas.drawText("须", 0, 100, paint);
        drawTextOnCenter(canvas);
    }




}
