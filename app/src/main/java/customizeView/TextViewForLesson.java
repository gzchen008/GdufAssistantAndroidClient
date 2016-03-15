package customizeView;

import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import vanroid.com.gdufassistant20.R;


public class TextViewForLesson extends TextView {

	private int mColor[] = new int[] { R.color.a, R.color.b, R.color.c,
			R.color.d, R.color.e, R.color.f, R.color.g, R.color.h, R.color.i,
			R.color.j };

	public TextViewForLesson(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public TextViewForLesson(Context context) {
		super(context);
		initView();
	}

	@Override
	protected void onDraw(Canvas canvas) {

		Random ran = new Random();

		Paint p = new Paint();
		p.setAntiAlias(true);
		p.setColor(getResources().getColor(mColor[ran.nextInt(mColor.length)]));

		int width = getWidth();
		int height = getHeight();

		RectF rf = new RectF(1, 1, width - 1, height - 1);

		canvas.drawRoundRect(rf, 15, 15, p);

		super.onDraw(canvas);
	}

	private void initView() {
		setGravity(Gravity.CENTER);
		setTextColor(Color.WHITE);
	}

}
