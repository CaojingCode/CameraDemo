package com.caojing.kotlinone.fkcamera.utils;

import android.graphics.Bitmap;
import android.opengl.GLException;
import android.os.Environment;
import android.widget.ImageView;

import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import static java.util.Collections.sort;

/**
 * Created by Caojing on 2018/8/27.
 * 你不是一个人在战斗
 */
public class BaseUtils {


    public static String filedir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/吉家";

    /**
     * 从sd卡获取图片资源
     *
     * @return
     */
    public static List<String> getImagePathFromSD() {
        // 图片列表
        List<String> imagePathList = new ArrayList<>();
        // 得到该路径文件夹下所有的文件
        File fileAll = new File(filedir);
        File[] files = fileAll.listFiles();
        if (files == null)
            return new ArrayList<>();
        // 将所有的文件存入ArrayList中,并过滤所有图片格式的文件
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (checkIsImageFile(file.getPath())) {
                imagePathList.add(file.getPath());
            }
        }
        sort(imagePathList);
        // 返回得到的图片列表
        return imagePathList;
    }


    /**
     * 检查扩展名，得到图片格式的文件
     *
     * @param fName 文件名
     * @return
     */
    private static boolean checkIsImageFile(String fName) {
        boolean isImageFile = false;
        // 获取扩展名
        String FileEnd = fName.substring(fName.lastIndexOf(".") + 1,
                fName.length()).toLowerCase();
        if (FileEnd.equals("jpg")
                || FileEnd.equals("jpeg")) {
            isImageFile = true;
        } else {
            isImageFile = false;
        }
        return isImageFile;
    }

    //带角度旋转的
    public static void showNormalImageNoCache(Object url, final ImageView imageView, int empty) {
        RequestOptions options = new RequestOptions()
                .error(empty)
                .placeholder(empty)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(Utils.getApp()).asBitmap().load(url).apply(options).into(imageView);
    }

    /**
     * 相机照片保存路径
     */
    public static String getFilePath() {
        return BaseUtils.filedir + "/" + TimeUtils.getNowMills() + ".jpg";
    }

    public static Bitmap createBitmapFromGLSurface(int x, int y, int w, int h,GL10 gl)
            throws OutOfMemoryError {
        int bitmapBuffer[] = new int[w * h];
        int bitmapSource[] = new int[w * h];
        IntBuffer intBuffer = IntBuffer.wrap(bitmapBuffer);
        intBuffer.position(0);
        try {
            gl.glReadPixels(x, y, w, h, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, intBuffer);
            int offset1, offset2;
            for (int i = 0; i < h; i++) {
                offset1 = i * w;
                offset2 = (h - i - 1) * w;
                for (int j = 0; j < w; j++) {
                    int texturePixel = bitmapBuffer[offset1 + j];
                    int blue = (texturePixel >> 16) & 0xff;
                    int red = (texturePixel << 16) & 0x00ff0000;
                    int pixel = (texturePixel & 0xff00ff00) | red | blue;
                    bitmapSource[offset2 + j] = pixel;
                }
            }
        } catch (GLException e) {
            return null;
        }

        return Bitmap.createBitmap(bitmapSource, w, h, Bitmap.Config.ARGB_8888);
    }
}
