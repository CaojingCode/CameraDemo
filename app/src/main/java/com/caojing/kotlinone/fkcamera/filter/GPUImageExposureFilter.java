package com.caojing.kotlinone.fkcamera.filter;

import android.opengl.GLES30;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;


/**
 * Created by Caojing on 2018/9/12.
 * 基础滤镜：曝光率滤镜，值范围 -10.0~10.0，0.0为正常值
 * 你不是一个人在战斗
 */
public class GPUImageExposureFilter extends GPUImageFilter {

    public static final String EXPOSURE_FRAGMENT_SHADER = "" +
            " varying highp vec2 textureCoordinate;\n" +
            " \n" +
            " uniform sampler2D inputImageTexture;\n" +
            " uniform highp float exposure;\n" +
            " \n" +
            " void main()\n" +
            " {\n" +
            "     highp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n" +
            "     \n" +
            "     gl_FragColor = vec4(textureColor.rgb * pow(2.0, exposure), textureColor.w);\n" +
            " } ";

    private int mExposureLocation;
    private float mExposure;

    public GPUImageExposureFilter() {
        this(1.0f);
    }

    public GPUImageExposureFilter(final float exposure) {
        super(NO_FILTER_VERTEX_SHADER, EXPOSURE_FRAGMENT_SHADER);
        mExposure = exposure;
    }

    @Override
    public void onInit() {
        super.onInit();
        mExposureLocation = GLES30.glGetUniformLocation(getProgram(), "exposure");
    }

    @Override
    public void onInitialized() {
        super.onInitialized();
        setExposure(mExposure);
    }

    public void setExposure(final float exposure) {
        mExposure = exposure;
        setFloat(mExposureLocation, mExposure);
    }

}
