package hmf.com.project.onboarding.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by HP-HP on 05-12-2015.
 */
public class TimelineView extends View {

    private Drawable mMarker;
    private Drawable mStartLine;
    private Drawable mEndLine;
    private int mMarkerSize;
    private int mLineSize;
    private String text;

    private Rect mBounds;
    private Context mContext;
    private CharSequence mText = "Time";
    private Paint paint;



    public TimelineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    private void init(AttributeSet attrs) {
        paint  = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setTextSize(15);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, hmf.com.project.onboarding.R.styleable.timeline_style);
        mMarker = typedArray.getDrawable(hmf.com.project.onboarding.R.styleable.timeline_style_marker);
        mStartLine = typedArray.getDrawable(hmf.com.project.onboarding.R.styleable.timeline_style_line);
        mEndLine = typedArray.getDrawable(hmf.com.project.onboarding.R.styleable.timeline_style_line);
        mMarkerSize = typedArray.getDimensionPixelSize(hmf.com.project.onboarding.R.styleable.timeline_style_marker_size, 35);
        mLineSize = typedArray.getDimensionPixelSize(hmf.com.project.onboarding.R.styleable.timeline_style_line_size, 2);
        typedArray.recycle();

        if(mMarker == null) {
            mMarker = mContext.getResources().getDrawable(hmf.com.project.onboarding.R.drawable.marker);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //Width measurements of the width and height and the inside view of child controls
        int w = mMarkerSize + getPaddingLeft() + getPaddingRight();
        int h = mMarkerSize + getPaddingTop() + getPaddingBottom();

        // Width and height to determine the final view through a systematic approach to decision-making
        int widthSize = resolveSizeAndState(w, widthMeasureSpec, 0);
        int heightSize = resolveSizeAndState(h, heightMeasureSpec, 0);

        setMeasuredDimension(widthSize, heightSize);
        initDrawable();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // When the view is displayed when the callback
        // Positioning Drawable coordinates, then draw
        initDrawable();
    }

//    private void initDrawable() {
//        int pLeft = getPaddingLeft();
//        int pRight = getPaddingRight();
//        int pTop = getPaddingTop();
//        int pBottom = getPaddingBottom();
//
//        int width = getWidth();// Width of current custom view
//        int height = getHeight();
//
//        int cWidth = width - pLeft - pRight;// Circle width
//        int cHeight = height - pTop - pBottom;
//
//        int markSize = Math.min(mMarkerSize, Math.min(cWidth, cHeight));
//
//        if(mMarker != null) {
//            mMarker.setBounds(pLeft,pTop,pLeft + markSize,pTop + markSize);
//            mBounds = mMarker.getBounds();
//        }
//
//        int centerX = mBounds.centerY();
//        int lineLeft = centerX - (mLineSize >> 1);
//        if(mStartLine != null) {
//            mStartLine.setBounds(lineLeft, 0, mLineSize + lineLeft, mBounds.top);
//        }
//
//        if(mEndLine != null) {
//            mEndLine.setBounds(lineLeft, mBounds.bottom, mLineSize + lineLeft, height);
//        }
//
//    }
    private void initDrawable() {
        int pLeft = getPaddingLeft();
        int pRight = getPaddingRight();
        int pTop = getPaddingTop();
        int pBottom = getPaddingBottom();

        int width = getWidth();// Width of current custom view
        int height = getHeight();
        int cX = width/2;
        int cY = height/2;
        int cWidth = width - pLeft - pRight;// Circle width
        int cHeight = height - pTop - pBottom;

        int markSize = Math.min(mMarkerSize, Math.min(cWidth, cHeight));

        if(mMarker != null) {
            mMarker.setBounds(cX -(markSize/2),cY-(markSize/2),cX + (markSize/2),cY +(markSize/2));
            mBounds = mMarker.getBounds();
        }

        int centerX = mBounds.centerX();
        int lineLeft = centerX - (mLineSize >> 1);
        if(mStartLine != null) {
            mStartLine.setBounds(0, cY, cX, cY+2);
        }

        if(mEndLine != null) {
            mEndLine.setBounds(cX, cY, cX+width, cY+2);
        }


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        if(mStartLine != null) {
            mStartLine.draw(canvas);
        }
        if(mEndLine != null) {
            mEndLine.draw(canvas);
        }
        if(mMarker != null) {
            mMarker.draw(canvas);

        }
        if(null != text && !text.equals("")) {
            int pLeft = getPaddingLeft();
            int pRight = getPaddingRight();
            int pTop = getPaddingTop();
            int pBottom = getPaddingBottom();

            int width = getWidth();// Width of current custom view
            int height = getHeight();
            int cX = width / 2;
            int cY = height / 2;
            int cWidth = width - pLeft - pRight;// Circle width
            int cHeight = height - pTop - pBottom;

            int markSize = Math.min(mMarkerSize, Math.min(cWidth, cHeight));

//            L.d("TimeLineView::onDraw():: width =  "+ width + " Cx = "+cX +" X ="+ (cX - (markSize / 2)));
//            L.d("TimeLineView::onDraw():: Height =  "+ height + " Cy = "+cY +" X ="+ (cY));
            if (paint != null) {
                canvas.drawText(text ,cX - (15 / 2), cY+5,paint);
            }//if()...
        }//if()....

    }

    public void setMarker(Drawable marker) {
        mMarker = marker;
        initDrawable();
    }

    public void setStartLine(Drawable startline) {
        mStartLine = startline;
        initDrawable();
    }

    public void setEndLine(Drawable endLine) {
        mEndLine = endLine;
        initDrawable();
    }

    public void setMarkerSize(int markerSize) {
        mMarkerSize = markerSize;
        initDrawable();
    }

    public void setLineSize(int lineSize) {
        mLineSize = lineSize;
        initDrawable();
    }

    public void initLine(int viewType) {

        if(viewType == LineType.BEGIN) {
            setStartLine(null);
        } else if(viewType == LineType.END) {
            setEndLine(null);
        } else if(viewType == LineType.ONLYONE) {
            setStartLine(null);
            setEndLine(null);
        }

        initDrawable();
    }
    public void setText(CharSequence text){
        this.mText =  text;
    }//

    public static int getTimeLineViewType(int position, int total_size) {
        if(total_size == 1) {
            return LineType.ONLYONE;
        } else if(position == 0) {
            return LineType.BEGIN;
        } else if(position == total_size - 1) {
            return LineType.END;
        } else {
            return LineType.NORMAL;
        }
    }
}