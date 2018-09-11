package com.caojing.kotlinone.fkcamera.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.caojing.kotlinone.fkcamera.bean.ImageRenderer;

import java.io.File;

import cn.co.willow.android.ultimate.gpuimage.core_config.FilterConfig;
import cn.co.willow.android.ultimate.gpuimage.core_render.BaseRenderer;
import cn.co.willow.android.ultimate.gpuimage.core_render_filter.GPUImageFilter;
import cn.co.willow.android.ultimate.gpuimage.manager.image_editor.image_loader.BaseImageLoader;
import cn.co.willow.android.ultimate.gpuimage.manager.image_editor.image_loader.FileImageLoader;
import cn.co.willow.android.ultimate.gpuimage.manager.image_editor.image_loader.UriImageLoader;

/**
 * Created by Caojing on 2018/8/28.
 * 你不是一个人在战斗
 */
public class MyPureImageManager {

    private Context context;
    private ImageRenderer mRenderer;
    private GLSurfaceView mGlSurfaceView;
    private GPUImageFilter mFilter;
    private Bitmap mCurrentBitmap;           // The Image tou want to operate


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private MyPureImageManager(final Context context) {
        this.context = context;
        supportsOpenGLES3(context);
        mFilter = new GPUImageFilter();
        mRenderer = new ImageRenderer(mFilter);
    }

    /**
     * 检测是否支持OpenGl
     */
    private void supportsOpenGLES3(final Context context) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        if (configurationInfo.reqGlEsVersion < 0x30000) {
            throw new IllegalStateException("OpenGL ES 3.0 is not supported on this phone.");
        }
    }

    /**
     * init, and get instance to do next operate
     */
    public static MyPureImageManager init(final Context context) {
        return new MyPureImageManager(context);
    }


    /*关键设置======================================================================================*/

    /**
     * 获取图片
     */
    public void TakePicture() {
        mRenderer.setTakePicture(true);
        requestRender();
    }

    /**
     * 请求刷新渲染器
     */
    public MyPureImageManager requestRender() {
        if (mGlSurfaceView != null) {
            mGlSurfaceView.requestRender();
        }
        return this;
    }

    /**
     * 初始化GLSurfaceView
     */
    public MyPureImageManager setGLSurfaceView(final GLSurfaceView view) {
        mGlSurfaceView = view;
        mGlSurfaceView.setEGLContextClientVersion(2);
//        mGlSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
//        mGlSurfaceView.getHolder().setFormat(PixelFormat.RGBA_8888);
        mGlSurfaceView.setRenderer(mRenderer);
        mGlSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        mGlSurfaceView.requestRender();
        return this;
    }

    /**
     * 设置展示模式
     */
    public MyPureImageManager setScaleType(final FilterConfig.ScaleType scaleType) {
        mRenderer.setScaleType(scaleType);
        return this;
    }

    /**
     * 设置滤镜
     */
    public MyPureImageManager setFilter(final GPUImageFilter filter) {
        mFilter = filter;
        mRenderer.setFilter(mFilter);
        requestRender();
        return this;
    }


    /*图片加载逻辑====================================================================================*/

    /**
     * Sets the image on which the filter should be applied from a Uri.
     */
    public MyPureImageManager setImage(final Uri uri) {
        new UriImageLoader(context, mRenderer, uri)
                .setFinishCallBack(new BaseImageLoader.FinishCallBack() {
                    @Override
                    public void onFinish(Bitmap bitmap) {
                        setImage(bitmap);
                    }
                })
                .execute();
        return this;
    }

    /**
     * Sets the image on which the filter should be applied.
     */
    public MyPureImageManager setImage(final Bitmap bitmap) {
        mCurrentBitmap = bitmap;
        mRenderer.setImageBitmap(bitmap, false);
        requestRender();
        return this;
    }

    /**
     * Sets the image on which the filter should be applied from a File.
     */
    public MyPureImageManager setImage(final File file) {
        new FileImageLoader(context, mRenderer, file)
                .setFinishCallBack(new BaseImageLoader.FinishCallBack() {
                    @Override
                    public void onFinish(Bitmap bitmap) {
                        setImage(bitmap);
                    }
                })
                .execute();
        return this;
    }


    /*渲染流程关联性监听===============================================================================*/
    public void setOnSurfaceSetListener(BaseRenderer.OnSurfaceSetListener mOnSurfaceSetListener) {
        mRenderer.setOnSurfaceSetListener(mOnSurfaceSetListener);
    }

}
