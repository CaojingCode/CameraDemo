package com.caojing.kotlinone.fkcamera.bean;

import android.graphics.Bitmap;

import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.caojing.kotlinone.fkcamera.utils.BaseUtils;

import javax.microedition.khronos.opengles.GL10;

import cn.co.willow.android.ultimate.gpuimage.core_render.PureImageRenderer;
import cn.co.willow.android.ultimate.gpuimage.core_render_filter.GPUImageFilter;

/**
 * Created by Caojing on 2018/8/28.
 * 你不是一个人在战斗
 */

public class ImageRenderer extends PureImageRenderer {

    private boolean isTakePicture;

    public ImageRenderer setTakePicture(boolean takePicture) {
        isTakePicture = takePicture;
        return this;
    }

    public ImageRenderer(GPUImageFilter filter) {
        super(filter);
    }



    @Override
    public void onDrawFrame(GL10 gl) {
        super.onDrawFrame(gl);

        if (isTakePicture) {
            ToastUtils.showShort("拍照");
            Bitmap bitmap = BaseUtils.createBitmapFromGLSurface(0, 0, mImageWidth,
                    mImageHeight, gl);
            ImageUtils.save(bitmap, BaseUtils.getFilePath(), Bitmap.CompressFormat.JPEG);
            isTakePicture = false;
        }
    }
}
