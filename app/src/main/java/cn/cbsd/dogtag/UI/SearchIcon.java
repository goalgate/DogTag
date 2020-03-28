package cn.cbsd.dogtag.UI;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class SearchIcon extends View {

    private int mWidth, mHeight;//画布的宽高
    private float smallRadii = 25f, bigRadii = 50f, varySet = 0, start, end, length;//大圆的半径，及小圆的半径，变化的参数，和三个变量，下面有说明
    private Path pathSearch, pathBig, pathStart, pathSearching, pathEnd;//绘制的路径
    private Paint paintNormal;//画笔
    private float[] pointXY = new float[2];//获取搜索的把柄的末尾坐标
    private PathMeasure measureSearch = new PathMeasure(), measureBig = new PathMeasure();//测量path的工具
    private TYPE type = TYPE.NONE;//状态
    private ValueAnimator animatorStart, animatorSearching, animatorDone;

    enum TYPE {
        NONE, START, SEARCHING, DONE
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                switch (type) {
                    case NONE://默认状态
                        type = TYPE.START;
                        animatorStart.start();
                        break;
                    case START://开始搜索
                        type = TYPE.SEARCHING;
                        animatorSearching.start();
                        break;
                    case SEARCHING://正在搜索
                        type = TYPE.DONE;
                        animatorDone.start();
                        break;
                    case DONE://搜索完成
//                        type=TYPE.NONE;
                        type = TYPE.START;
                        animatorStart.start();
                        break;
                    default:
                        break;
                }
            }
        }
    };

    public SearchIcon(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        initPath();
        initValueAnimator();
    }

    /**
     * 初始化动画
     */
    private void initValueAnimator() {
        animatorStart = ValueAnimator.ofFloat(0f, 1.000f);
        animatorStart.addUpdateListener(upListener);
        animatorStart.setInterpolator(new LinearInterpolator());
        animatorStart.addListener(listener);
        animatorStart.setRepeatCount(0);
        animatorStart.setDuration(1500);

        animatorSearching = ValueAnimator.ofFloat(0f, 1.000f);
        animatorSearching.addUpdateListener(upListener);
        animatorSearching.setInterpolator(new LinearInterpolator());
        animatorSearching.addListener(listener);
        animatorSearching.setRepeatCount(2);
        animatorSearching.setDuration(2000);


        animatorDone = ValueAnimator.ofFloat(1.000f, 0f);
        animatorDone.addUpdateListener(upListener);
        animatorDone.setInterpolator(new LinearInterpolator());
        animatorDone.addListener(listener);
        animatorDone.setRepeatCount(0);
        animatorDone.setDuration(1500);

    }

    /**
     * 初始化路径
     */
    private void initPath() {
        pathSearch = new Path();
        pathSearch.addArc(new RectF(-smallRadii, -smallRadii, smallRadii, smallRadii), 45, 359.9f);
        pathBig = new Path();
        pathBig.addArc(new RectF(-bigRadii, -bigRadii, bigRadii, bigRadii), 45, 359.9f);
        measureBig = new PathMeasure();
        measureBig.setPath(pathBig, false);
        measureBig.getPosTan(0, pointXY, null);
        pathSearch.lineTo(pointXY[0], pointXY[1]);
        measureSearch = new PathMeasure();
        measureSearch.setPath(pathSearch, false);
        length = (float) (Math.PI * bigRadii * 2 / 4);
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        paintNormal = new Paint();
        paintNormal.setAntiAlias(true);
        paintNormal.setStrokeWidth(5);
        paintNormal.setColor(Color.BLUE);
        paintNormal.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mWidth / 2, mHeight / 2);
        drawSearch(canvas);
    }

    /**
     * 画
     *
     * @param canvas
     */
    private void drawSearch(Canvas canvas) {
        switch (type) {
            case NONE:
                canvas.drawPath(pathSearch, paintNormal);
                break;
            case START:
                pathStart = new Path();
                //这里是画原pathSearch中截取的一段路径
                measureSearch.getSegment(varySet * measureSearch.getLength(), measureSearch.getLength(), pathStart, true);
                canvas.drawPath(pathStart, paintNormal);
                break;
            case SEARCHING:
                end = measureBig.getLength() * varySet;
                start = (float) (end - ((0.5 - Math.abs(varySet - 0.5)) * length));
                pathSearching = new Path();
                //这里是画原pathBig中截取的一段路径
                measureBig.getSegment(start, end, pathSearching, true);
                canvas.drawPath(pathSearching, paintNormal);
                break;
            case DONE:
                pathEnd = new Path();
                //这里是画原pathSearch中截取的一段路径
                measureSearch.getSegment(varySet * measureSearch.getLength(), measureSearch.getLength(), pathEnd, true);
                canvas.drawPath(pathEnd, paintNormal);
                break;
            default:
                break;
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    private ValueAnimator.AnimatorUpdateListener upListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            varySet = (float) valueAnimator.getAnimatedValue();
            invalidate();
        }
    };
    private Animator.AnimatorListener listener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animator) {
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            handler.sendEmptyMessage(0);
        }

        @Override
        public void onAnimationCancel(Animator animator) {
        }

        @Override
        public void onAnimationRepeat(Animator animator) {
        }
    };

    /**
     * 开始搜索
     */
    public void start() {
        if (type == TYPE.NONE) {
            handler.sendEmptyMessage(0);
        }
    }

    public void stop() {
        type = TYPE.NONE;
        this.clearAnimation();
    }
}
