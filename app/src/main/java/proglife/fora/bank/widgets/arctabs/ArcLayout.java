package proglife.fora.bank.widgets.arctabs;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;

import proglife.fora.bank.R;

public class ArcLayout extends FrameLayout {

    protected ArcLayoutSettings settings;

    private int height = 0;

    private int width = 0;

    private Path clipPath;

    public ArcLayout(Context context) {
        super(context);
        init(context, null);
    }

    public ArcLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        settings = new ArcLayoutSettings(context, attrs);
        settings.setElevation(ViewCompat.getElevation(this));
    }

    private Path createClipPath() {
        final Path path = new Path();

        float arcHeight = settings.getArcHeight();

        switch (settings.getPosition()){
            case ArcLayoutSettings.POSITION_BOTTOM:{
                if (settings.isCropInside()) {
                    path.moveTo(0, 0);
                    path.lineTo(0, height);
                    path.quadTo(width / 2, height - 2 * arcHeight, width, height);
                    path.lineTo(width, 0);
                    path.close();
                } else {
                    path.moveTo(0, 0);
                    path.lineTo(0, height - arcHeight);
                    path.quadTo(width / 2, height + arcHeight, width, height - arcHeight);
                    path.lineTo(width, 0);
                    path.close();
                }
                break;
            }
            case ArcLayoutSettings.POSITION_TOP:
                if (settings.isCropInside()) {
                    path.moveTo(0, height);
                    path.lineTo(0, 0);
                    path.quadTo(width / 2, 2 * arcHeight, width, 0);
                    path.lineTo(width, height);
                    path.close();
                } else {
                    path.moveTo(0, arcHeight);
                    path.quadTo(width / 2, -arcHeight, width, arcHeight);
                    path.lineTo(width, height);
                    path.lineTo(0, height);
                    path.close();
                }
                break;
            case ArcLayoutSettings.POSITION_LEFT:
                if (settings.isCropInside()) {
                    path.moveTo(width, 0);
                    path.lineTo(0, 0);
                    path.quadTo(arcHeight * 2, height / 2, 0, height);
                    path.lineTo(width, height);
                    path.close();
                } else {
                    path.moveTo(width, 0);
                    path.lineTo(arcHeight, 0);
                    path.quadTo(-arcHeight, height / 2, arcHeight, height);
                    path.lineTo(width, height);
                    path.close();
                }
                break;
            case ArcLayoutSettings.POSITION_RIGHT:
                if (settings.isCropInside()) {
                    path.moveTo(0, 0);
                    path.lineTo(width, 0);
                    path.quadTo(width - arcHeight * 2, height / 2, width, height);
                    path.lineTo(0, height);
                    path.close();
                } else {
                    path.moveTo(0, 0);
                    path.lineTo(width - arcHeight, 0);
                    path.quadTo(width + arcHeight, height / 2, width - arcHeight, height);
                    path.lineTo(0, height);
                    path.close();
                }
                break;
        }

        return path;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            calculateLayout();
        }
    }

    private void calculateLayout() {
        if (settings == null) {
            return;
        }
        height = getMeasuredHeight();
        width = getMeasuredWidth();
        if (width > 0 && height > 0) {

            clipPath = createClipPath();
            ViewCompat.setElevation(this, settings.getElevation());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !settings.isCropInside()) {
                ViewCompat.setElevation(this, settings.getElevation());
                setOutlineProvider(new ViewOutlineProvider() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void getOutline(View view, Outline outline) {
                        outline.setConvexPath(clipPath);
                    }
                });
            }
        }
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        int saveCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        super.dispatchDraw(canvas);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));
        canvas.drawPath(clipPath, paint);
        canvas.restoreToCount(saveCount);
        paint.setXfermode(null);
    }

    public class ArcLayoutSettings {
        public final static int CROP_INSIDE = 0;
        public final static int CROP_OUTSIDE = 1;

        public final static int POSITION_BOTTOM = 0;
        public final static int POSITION_TOP = 1;
        public final static int POSITION_LEFT = 2;
        public final static int POSITION_RIGHT = 3;

        private boolean cropInside = true;
        private float arcHeight;
        private float elevation;

        private int position;

        private float dpToPx(Context context, int dp) {
            Resources r = context.getResources();
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
        }

        ArcLayoutSettings(Context context, AttributeSet attrs) {
            TypedArray styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.ArcLayout, 0, 0);
            arcHeight = styledAttributes.getDimension(R.styleable.ArcLayout_arc_height, dpToPx(context, 10));

            final int cropDirection = styledAttributes.getInt(R.styleable.ArcLayout_arc_cropDirection, CROP_INSIDE);
            cropInside = (cropDirection == CROP_INSIDE);

            position = styledAttributes.getInt(R.styleable.ArcLayout_arc_position, POSITION_BOTTOM);

            styledAttributes.recycle();
        }

        public float getElevation() {
            return elevation;
        }

        public void setElevation(float elevation) {
            this.elevation = elevation;
        }

        public boolean isCropInside() {
            return cropInside;
        }

        public float getArcHeight() {
            return arcHeight;
        }

        public int getPosition() {
            return position;
        }
    }


}