package com.cl.slack.lib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description:  自定义progressbar </p>
 * Created by slack on 2016/8/1 10:04 .
 */
public class ProgressbarView extends View {

    private final int DEFAULT_WIDTH = 450;//  长度
    private final int DEFAULT_HEIGTH = 40;// 宽度
    private final int DEFAULT_SHAPE = 0;
    private final int DEFAULT_MAX = 100;
    private final int DEFAULT_PROGRESS = 0;
    private final int DEFAULT_PROGRESS_COLOR = Color.parseColor("#76B034");
    private final int DEFAULT_PROGRESS_BACK_COLOR = Color.parseColor("#EFEFEF");
    private final int DEFAULT_CIRCLE_R = 50;//  半径
    private final int DEFAULT_SQUARE_R = 20;//  圆角矩形半径
    private final int DEFAULT_TEXT_SIZE = 30;//  字体大小

    private enum ShapeType{
        RECTANGLE, // 矩形
        CIRCLE, // 圆形
        SQUARE // 圆角矩形
    }

    private int progress = DEFAULT_PROGRESS;// 队里不为空时为队列最后一个数据
    private int max = DEFAULT_MAX;
    private float mwidth = DEFAULT_WIDTH;
    private float mhight = DEFAULT_HEIGTH;
    private int mShapeType = DEFAULT_SHAPE;
    private int proColor = DEFAULT_PROGRESS_COLOR;
    private int proBackColor = DEFAULT_PROGRESS_BACK_COLOR;
    private int textLowColor = Color.BLACK;
    private int textMiddleColor = Color.BLUE;
    private int textHighColor = Color.RED;
    private int progressSegmentColor = proColor;
    private int progressDoubleSegColor = Color.GRAY;

    private float squareRadius = DEFAULT_SQUARE_R;
    private float textSize = DEFAULT_TEXT_SIZE;

    private boolean showText;
    private float circleX = DEFAULT_CIRCLE_R + 20; // 默认圆心 半径+20
    private float circleR = DEFAULT_CIRCLE_R;

    private float startX;// 矩形开始的点
    private float startY;
    private float startR = -90;//圆弧开始的角度
    private Paint paint;
    RectF circleRectf,squareRectf;
    private List<Integer> progressList;
    
    public ProgressbarView(Context context) {
        this(context,null);
    }

    public ProgressbarView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ProgressbarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        // Load the styled attributes and set their properties
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.ProgressbarView, defStyleAttr, 0);
        mwidth = attributes.getDimension(R.styleable.ProgressbarView_p_width, DEFAULT_WIDTH);
        mhight = attributes.getDimension(R.styleable.ProgressbarView_p_height, DEFAULT_HEIGTH);
        mShapeType = attributes.getInteger(R.styleable.ProgressbarView_p_shapeType, DEFAULT_SHAPE);
        showText = attributes.getBoolean(R.styleable.ProgressbarView_p_showText,false);

        if(mShapeType == 1){
            circleR = attributes.getDimension(R.styleable.ProgressbarView_p_circle_radius, DEFAULT_CIRCLE_R);
//            showText = true;
            circleX = attributes.getDimension(R.styleable.ProgressbarView_p_circle_X_Y,circleR + 20);
        }
        if(mShapeType == 2){
            squareRadius = attributes.getDimension(R.styleable.ProgressbarView_p_square_radius,DEFAULT_SQUARE_R);
        }

        if(showText){
            textSize = attributes.getDimensionPixelSize(R.styleable.ProgressbarView_p_textSize,DEFAULT_TEXT_SIZE);
        }
        max = attributes.getInteger(R.styleable.ProgressbarView_p_maxValue, DEFAULT_MAX);
        progress = attributes.getInteger(R.styleable.ProgressbarView_p_progressValue, DEFAULT_PROGRESS);
        proColor = attributes.getColor(R.styleable.ProgressbarView_p_progressColor,DEFAULT_PROGRESS_COLOR);
        proBackColor = attributes.getColor(R.styleable.ProgressbarView_p_progressBackColor,DEFAULT_PROGRESS_BACK_COLOR);
        textLowColor = attributes.getColor(R.styleable.ProgressbarView_p_textLowColor,Color.BLACK);
        textMiddleColor = attributes.getColor(R.styleable.ProgressbarView_p_textMiddleColor,Color.BLUE);
        textHighColor = attributes.getColor(R.styleable.ProgressbarView_p_textHighColor,Color.RED);
        progressSegmentColor = attributes.getColor(R.styleable.ProgressbarView_p_progressSegmentColor,DEFAULT_PROGRESS_COLOR);
        progressDoubleSegColor = attributes.getColor(R.styleable.ProgressbarView_p_progressDoubleSegColor,Color.GRAY);

        Log.i("slack",mwidth + "," + mhight + "," +  mShapeType + "," +  max + "," +
                progress + "," + showText + "," + squareRadius + "," + textSize);
        paint = new Paint();
        circleRectf = new RectF();
        squareRectf = new RectF();
        progressList = new ArrayList<>();

        paint.setAntiAlias(true);// 设置是否抗锯齿
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);// 帮助消除锯齿
        paint.setColor(Color.parseColor("#EFEFEF"));// 设置画笔灰色
        paint.setStrokeWidth(10);// 设置画笔宽度
//        paint.setStyle(Paint.Style.STROKE);// 设置中空的样式
        paint.setStyle(Paint.Style.FILL);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setColor(proBackColor);// 设置画笔灰色
        paint.setStrokeWidth(10);// 设置画笔宽度
        paint.setTextSize(textSize);
        startX = 0;
        startY = 0;

        switch (mShapeType){
            case 0:
                canvas.drawRect(startX, startY, mwidth, mhight, paint);
                paint.setColor(proColor);
                if(progressList.size() > 0){
                    for(int pro : progressList){
                        canvas.drawRect(startX, startY, ((float) pro / max) * mwidth,
                                mhight, paint);
                        startX = ((float) pro/max) * mwidth;
//                        Log.i("slack","startX:" + startX + "," + pro + "," + max +"," + mwidth);
                        paint.setColor(progressSegmentColor);
                    }
                }else{
                    canvas.drawRect(startX, startY, ((float) progress / max) * mwidth,
                            mhight, paint);
                }


                // 绘制内部线条
//                paint.setColor(Color.RED);
//                canvas.drawLine(startX, startY + (mhight - startY) / 4 * 2,
//                        ((float) progress / max) * mwidth, startY + (mhight - startY)
//                                / 4 * 2, paint);

                paint.setStrokeWidth(1);// 设置画笔宽度
                paint.setColor(progressDoubleSegColor);
                //一条竖线
                if(progressList.size() > 0){
                    for(int i = 0;i<progressList.size() -1;i++){
                        canvas.drawLine(((float) progressList.get(i) / max) * mwidth,startY,((float) progressList.get(i) / max) * mwidth, startY + (mhight - startY),paint);
                    }
                }
                if(showText){
                    // 绘制下标进度数字
                    if(progress<(max/3)){
                        paint.setColor(textLowColor);
                    }else if(progress<(max/3)*2&&progress>(max/3)){
                        paint.setColor(textMiddleColor);
                    }else{
                        paint.setColor(textHighColor);
                    }

                    canvas.drawText(progress + "%", ((float) progress / max) * mwidth - (progress == 0 ? 0 : textSize),
                            mhight + textSize, paint);
                }
                break;
            case 1:
                paint.setStyle(Paint.Style.STROKE);// 设置中空的样式
                paint.setColor(proColor);
                circleRectf.set(circleX,circleX,circleX + circleR * 2,circleX + circleR * 2); // 左下角 ，右下角，这样保证半径正确
                if(progressList.size() > 0){
                    startR = -90;
                    float last = 0;
                    boolean isOu = true;
                    for(int pro : progressList){
                        // 画圆弧，第二个参数为：起始角度，第三个为跨的角度，第四个为true的时候是实心，false的时候为空心
                        canvas.drawArc(circleRectf, startR, ((pro-last) / max) * 360 ,false, paint);
                        startR = ((pro-last) / max) * 360 + startR;
                        last = pro;
//                        Log.i("slack","startR:" + startR);
                        if(isOu){
                            paint.setColor(progressSegmentColor);
                        }else {
                            paint.setColor(proColor);
                        }
                        isOu = !isOu;
                    }
                }else{
                    canvas.drawArc(circleRectf, startR, ((float) progress / max) * 360, false, paint);
                }



                if(showText){
                    paint.reset();// 将画笔重置
                    paint.setStrokeWidth(1);// 再次设置画笔的宽度
                    paint.setTextSize(textSize);
                    if(progress<(max/3)){
                        paint.setColor(textLowColor);
                    }else if(progress<(max/3)*2&&progress>(max/3)){
                        paint.setColor(textMiddleColor);
                    }else{
                        paint.setColor(textHighColor);
                    }
                    if (progress == max) {
                        canvas.drawText("完成", circleX + textSize/2f ,circleX + textSize *2f  , paint);
                    } else {
                        canvas.drawText(progress + "%", circleX + textSize/2f  ,circleX + textSize*2f , paint);
                    }
                }
                break;
            case 2:
                squareRectf.set(startX, startY, mwidth, mhight);
                canvas.drawRoundRect(squareRectf,squareRadius,squareRadius,paint);
                paint.setColor(proColor);
                if(progressList.size() > 0){
                    for(int pro : progressList){
                        squareRectf.set(startX, startY, ((float) pro / max) * mwidth,mhight);
                        canvas.drawRoundRect(squareRectf,squareRadius,squareRadius,paint);
                        startX = ((float) pro/max) * mwidth;
//                        Log.i("slack","startX:" + startX + "," + pro + "," + max +"," + mwidth);
                        paint.setColor(progressSegmentColor);
                    }
                }else{
                    squareRectf.set(startX, startY, ((float) progress / max) * mwidth,mhight);
                    canvas.drawRoundRect(squareRectf,squareRadius,squareRadius,paint);
                }

                // 绘制内部线条
                paint.setColor(progressDoubleSegColor);
                paint.setStrokeWidth(1);// 设置画笔宽度
                //一条竖线
                if(progressList.size() > 0){
                    for(int i = 0;i<progressList.size() -1;i++){

                        canvas.drawLine(((float) progressList.get(i) / max) * mwidth,startY,((float) progressList.get(i) / max) * mwidth, startY + (mhight - startY),paint);
                    }
                }
                if(showText){
                    // 绘制上标进度数字
                    if(progress<(max/3)){
                        paint.setColor(textLowColor);
                    }else if(progress<(max/3)*2&&progress>(max/3)){
                        paint.setColor(textMiddleColor);
                    }else{
                        paint.setColor(textHighColor);
                    }
                    canvas.drawText(progress + "%", ((float) progress / max) * mwidth - (progress == 0 ? 0 : textSize),
                            mhight + textSize, paint);
                }

                break;
            default:break;
        }



    }


    // - - - - - - - - - - - - -  public - - - - - - - - - - - - - - - - -


    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        setProgress(progress,false);
    }

    public void setProgress(int progress,boolean needDel) {
        if(progress <= max){
            this.progress = progress;
            if(needDel){
                progressList.add(progress);
            }
            invalidate();
        }
    }
    public void removeProgress(int progres) {
        if(progressList.size() > 0){
            progressList.remove(progressList.size() - 1 );
            if(progressList.size() == 0){
                progress = DEFAULT_PROGRESS;
            }else {
                progress = progressList.get(progressList.size() - 1);
            }
        }else{
            progress = progres;
        }
        invalidate();
    }


}
