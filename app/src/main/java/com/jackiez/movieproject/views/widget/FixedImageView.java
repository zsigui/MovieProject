package com.jackiez.movieproject.views.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jackiez.movieproject.R;
import com.jackiez.movieproject.utils.AppDebugLog;

/**
 * 固定某边大小自动根据图片延伸另一边宽度
 *
 * Created by zsigui on 16-10-25.
 */
// 写的各种坑爹，还不如系统的一个 adjustViewBounds 属性好用
public class FixedImageView extends ImageView {


    private boolean isXFixed = false;
    private int outWidth;
    private int outHeight;
    private boolean paddingExcept = true;
    private int realWidth;
    private int realHeight;

    public FixedImageView(Context context) {
        this(context, null);
    }

    public FixedImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FixedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FixedImageView, defStyleAttr, 0);
        isXFixed = ta.getBoolean(R.styleable.FixedImageView_isXFixed, false);
        outWidth = ta.getDimensionPixelSize(R.styleable.FixedImageView_outWidth, 0);
        outHeight = ta.getDimensionPixelSize(R.styleable.FixedImageView_outHeight, 0);
        paddingExcept = ta.getBoolean(R.styleable.FixedImageView_paddingExcept, true);
        ta.recycle();
    }

    /**
     * 当 paddingExcept 为 true，表示图片宽度; 当 paddingExcept 为 false，表示 View 的宽度(包含padding在内)
     * @param outWidth >0的像素值，表示固定的宽度
     */
    public void setOutWidth(int outWidth) {
        if (outWidth > 0
                && this.outWidth != outWidth) {
            this.outWidth = outWidth;
            requestLayout();
            invalidate();
        }
    }

    /**
     * 当 paddingExcept 为 true，表示图片高度; 当 paddingExcept 为 false，表示 View 的高度(包含padding在内)
     * @param outHeight >0的像素值，表示固定的高度
     */
    public void setOutHeight(int outHeight) {
        if (outHeight > 0
                && this.outHeight != outHeight) {
            this.outHeight = outHeight;
            requestLayout();
            invalidate();
        }
    }

    /**
     * 说明是否需要固定X轴，否则为固定Y轴，需要分别与 outWidth 和 outHeight 搭配使用
     */
    public void setXFixed(boolean XFixed) {
        if (this.isXFixed != XFixed) {
            isXFixed = XFixed;
            requestLayout();
            invalidate();
        }
    }

    /**
     * 计算宽高的是否是否排除
     */
    public void setPaddingExcept(boolean paddingExcept) {
        if (this.paddingExcept != paddingExcept) {
            this.paddingExcept = paddingExcept;
            requestLayout();
            invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (realWidth != 0 && realHeight != 0) {
            AppDebugLog.d(AppDebugLog.TAG_DEBUG_INFO, "realWidth = " + realWidth + ", realHeight = " + realHeight);
            setMeasuredDimension(realWidth, realHeight);
            return;
        }
        if ((isXFixed && outWidth > 0) || (!isXFixed && outHeight > 0)) {

            // 获取图片，先判断前景图，再判断背景图
            Drawable drawable = getDrawable();
            if (drawable == null) {
                drawable = getBackground();
            }
            if (drawable == null) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                return;
            }

            if (getScaleType() != ScaleType.FIT_XY) {
                setScaleType(ScaleType.FIT_XY);
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                return;
            }
            AppDebugLog.d(AppDebugLog.TAG_DEBUG_INFO, "outWidth = " + outWidth + ", outHeight = " + outHeight);
            DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
            int width = (int) (drawable.getIntrinsicWidth() * dm.density);
            int height = (int) (drawable.getIntrinsicHeight() * dm.density);
            AppDebugLog.d(AppDebugLog.TAG_DEBUG_INFO, "d.width = " + width + ", d.height = " + height);
            realWidth = outWidth;
            realHeight = outHeight;
            if (!paddingExcept) {
                // 当高度包含 padding 时，需要先减去以获取实际要求的图片宽高
                if (isXFixed) {
                    realWidth -= (getPaddingLeft() + getPaddingRight());
                } else {
                    realHeight -= (getPaddingTop() + getPaddingBottom());
                }
            }

            float ratio = isXFixed ? (float) realWidth / width : (float) realHeight / height;
            if (isXFixed) {
                height *= ratio;
                width = realWidth;
            } else {
                width *= ratio;
                height = realHeight;
            }

            // 最后的实际大小，添加上 padding 大小
            width += (getPaddingLeft() + getPaddingRight());
            height += (getPaddingTop() + getPaddingBottom());

            realWidth = width & MEASURED_SIZE_MASK;
            realHeight = height  & MEASURED_SIZE_MASK;
            if (getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) getLayoutParams();
                AppDebugLog.d(AppDebugLog.TAG_DEBUG_INFO, "margin = " + lp.leftMargin + ", right_margin = " + lp.rightMargin);
            }
            AppDebugLog.d(AppDebugLog.TAG_DEBUG_INFO, "width = " + width + ", height = " + height + ", padding = " + getPaddingLeft() +", right = " + getPaddingRight());
            setMeasuredDimension(realWidth, realHeight);
            // 已计算好宽高，设置 ScaleType
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
