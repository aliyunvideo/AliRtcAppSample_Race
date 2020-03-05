package com.aliyun.rtcdemo.utils;

import android.content.Context;

import com.aliyun.race.AliyunBeautifyNative;

import org.webrtc.alirtcInterface.ALI_RTC_INTERFACE;

/**
 * race 美颜实现类
 */
public class RaceBeautyImp implements ALI_RTC_INTERFACE.AliTextureObserver {
    private Context mContext;
    /**
     * 美颜开关
     */
    private boolean isOn = true;
    /**
     * race 美颜提供类
     */
    private AliyunBeautifyNative mBeautifyNative;

    /**
     * 磨皮
     */
    private float mSkinBuffing =0.8f;
    /**
     * 美白
     */
    private float mSkinWhitening=0.8f;
    /**
     * 锐化
     */
    private float mSharpen=0.8f;

    public RaceBeautyImp(Context context) {
        mContext = context;
    }

    /**
     * 美颜开关
     *
     * @param filterOn
     */
    public void setFilterOn(boolean filterOn) {
        isOn = filterOn;
    }

    /**
     * 设置美型和参数
     *
     * @param type
     * @param size
     */
    public void setRaceBeautyType(int type, float size) {
        if (mBeautifyNative == null || type < 0) {
            return;
        }
        mBeautifyNative.setFaceShape(type, size);
    }

    /**
     * 磨皮
     *
     * @param size
     */
    public void setSkinBuffing(float size) {
        if (mBeautifyNative == null) {
            return;
        }
        mSkinBuffing =size;
    }

    /**
     * 美白
     *
     * @param size
     */
    public void setSkinWhitening(float size) {
        if (mBeautifyNative == null) {
            return;
        }
        mSkinWhitening =size;
    }

    /**
     * 锐化
     *
     * @param size
     */
    public void setSharpen(float size) {
        if (mBeautifyNative == null) {
            return;
        }
        mSharpen =size;
    }

    @Override
    public void onTextureCreate(String callId, long context) {
        if (mBeautifyNative == null) {
            mBeautifyNative = new AliyunBeautifyNative(mContext);
        }
        mBeautifyNative.initialize();
        mBeautifyNative.setFaceSwitch(true);
    }

    @Override
    public int onTexture(String callId, int inputTexture, int textureWidth, int textureHeight, int stride, int rotate, long extra) {
        int texId = inputTexture;
        if (!isOn) {
            return inputTexture;
        }
        mBeautifyNative.setSkinBuffing(mSkinBuffing);
        mBeautifyNative.setSkinWhitening(mSkinWhitening);
        mBeautifyNative.setSharpen(mSharpen);
        if (mBeautifyNative != null) {
            texId = mBeautifyNative.processTexture(inputTexture, textureWidth, textureHeight, rotate, 0);
            if (texId < 0) {
                texId = inputTexture;
            }
        }
        return texId;
    }

    @Override
    public void onTextureDestroy(String callId) {
        if (mBeautifyNative != null) {
            mBeautifyNative.destroy();
        }
    }
}
