package com.caojing.kotlinone.fkcamera.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.caojing.kotlinone.fkcamera.utils.BaseUtils;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import cn.co.willow.android.ultimate.gpuimage.ui.FilterRecoderView;

/**
 * Created by Caojing on 2018/8/28.
 * 你不是一个人在战斗
 */
public class MyFilterRecoderView extends FilterRecoderView implements GLSurfaceView.Renderer {

    int width_surface, height_surface;
    private boolean isTakePicture;

    public MyFilterRecoderView(Context context) {
        super(context);
        setRenderer(this);
    }

    public MyFilterRecoderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setRenderer(this);
    }

    public MyFilterRecoderView setTakePicture(boolean takePicture) {
        isTakePicture = takePicture;
        return this;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        // Adjust the viewport based on geometry changes,
        // such as screen rotation
        width_surface = width;
        height_surface = height;
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        // 获取GLSurfaceView的图片并保存
        if (isTakePicture) {
            ToastUtils.showShort("拍照");
            Bitmap bitmap = BaseUtils.createBitmapFromGLSurface(0, 0, width_surface,
                    height_surface, gl10);
            ImageUtils.save(bitmap, BaseUtils.getFilePath(), Bitmap.CompressFormat.JPEG);
            isTakePicture = false;
        }
    }
}
