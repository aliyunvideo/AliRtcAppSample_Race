package com.aliyun.rtcdemo.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alivc.rtc.AliRtcAuthInfo;
import com.alivc.rtc.AliRtcEngine;
import com.alivc.rtc.AliRtcEngineEventListener;
import com.alivc.rtc.AliRtcEngineNotify;
import com.alivc.rtc.AliRtcRemoteUserInfo;
import com.aliyun.race.AliyunBeautifyNative;
import com.aliyun.rtcdemo.R;
import com.aliyun.rtcdemo.base.BaseActivity;
import com.aliyun.rtcdemo.bean.ChartUserBean;
import com.aliyun.rtcdemo.bean.RTCAuthInfo;
import com.aliyun.rtcdemo.service.ForegroundService;
import com.aliyun.rtcdemo.utils.AliRtcConstants;
import com.aliyun.rtcdemo.utils.AppUtils;
import com.aliyun.rtcdemo.utils.DensityUtils;
import com.aliyun.rtcdemo.utils.RaceBeautyImp;
import com.aliyun.rtcdemo.widget.seekbar.DiscreteSeekBar;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.ali.ThreadUtils;
import org.webrtc.alirtcInterface.ALI_RTC_INTERFACE;
import org.webrtc.alirtcInterface.AliParticipantInfo;
import org.webrtc.alirtcInterface.AliStatusInfo;
import org.webrtc.alirtcInterface.AliSubscriberInfo;
import org.webrtc.sdk.SophonSurfaceView;

import static com.alivc.rtc.AliRtcEngine.AliRtcAudioTrack.AliRtcAudioTrackNo;
import static com.alivc.rtc.AliRtcEngine.AliRtcRenderMode.AliRtcRenderModeAuto;
import static com.alivc.rtc.AliRtcEngine.AliRtcVideoTrack.AliRtcVideoTrackBoth;
import static com.alivc.rtc.AliRtcEngine.AliRtcVideoTrack.AliRtcVideoTrackCamera;
import static com.alivc.rtc.AliRtcEngine.AliRtcVideoTrack.AliRtcVideoTrackNo;
import static com.alivc.rtc.AliRtcEngine.AliRtcVideoTrack.AliRtcVideoTrackScreen;
import static com.aliyun.rtcdemo.utils.AliRtcConstants.SOPHON_RESULT_SIGNAL_HEARTBEAT_TIMEOUT;
import static com.aliyun.rtcdemo.utils.AliRtcConstants.SOPHON_SERVER_ERROR_POLLING;
import static com.aliyun.rtcdemo.utils.AliRtcConstants.VIDEO_INFO_KEYS;

/**
 * 音视频通话的activity
 */
public class AliRtcChatActivity extends BaseActivity implements View.OnClickListener, DiscreteSeekBar.OnProgressChangeListener {
    private static final String TAG = AliRtcChatActivity.class.getName();

    /**
     * 用户名
     */
    String mUsername;
    /**
     * 频道名
     */
    String mChannel;
    /**
     * rtcAuthInfo，本地用户加入房间的时候返回的json
     */
    RTCAuthInfo mRtcAuthInfo;
    /**
     * 本地流播放view
     */
    private SophonSurfaceView mLocalView;
    /**
     * SDK提供的对音视频通话处理的引擎类
     */
    private AliRtcEngine mAliRtcEngine;
    /**
     * 前台服务的Intent
     */
    private Intent mForeServiceIntent;
    /**
     * 权限判断
     */
    private boolean mGrantPermission;
    /**
     * 数据集
     */
    private Bundle mBundle;
    /**
     * 承载远程User的Adapter
     */
    private ChartUserAdapter mUserListAdapter;
    private boolean mIsAudioCapture;
    private boolean mIsAudioPlay;
    private TextView mTvShowBeauty, mTvJoinChannel, mTvSure;
    private RelativeLayout mRlRaceContent;
    private DiscreteSeekBar beauty_whitening, beauty_buffing, beauty_sharp, beauty_cut_cheek, beauty_cut_face, beauty_thin_face, beauty_long_face, beauty_lower_jaw, beauty_higher_jaw, beauty_thin_jaw, beauty_mouth_high, beauty_philtrum;
    private DiscreteSeekBar beauty_thin_mandible, beauty_big_eye, beauty_canthus, beauty_canthus1, beauty_eye_angle1, beauty_eye_angle2, beauty_eye_tdangle, beauty_thin_nose, beauty_nose_wing, beauty_nasal_height, beauty_nose_tip_height, beauty_mouth_width, beauty_mouth_size;
    private RaceBeautyImp mRaceBeautyImp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alirtc_activity_chat);
        // 获取上个页面附带的参
        getIntentData();
        // 初始化界面上的view
        initView();
        // 初始化引擎以及打开预览界面
        initRTCEngineAndStartPreview();
        // 打开加入房间前需要的参数
        openJoinChannelBeforeNeedParams();
    }


    private void getIntentData() {
        mBundle = getIntent().getExtras();
        if (null != mBundle) {
            //用户名
            mUsername = mBundle.getString("username");
            //频道
            mChannel = mBundle.getString("channel");
            //音频采集
            mIsAudioCapture = mBundle.getBoolean("audioCapture");
            //音频播放
            mIsAudioPlay = mBundle.getBoolean("audioPlay");
            //rtcAuthInfo
            mRtcAuthInfo = (RTCAuthInfo) mBundle.getSerializable("rtcAuthInfo");
        }
    }

    private void initView() {
        beauty_whitening = findViewById(R.id.beauty_whitening);
        beauty_buffing = findViewById(R.id.beauty_buffing);
        beauty_sharp = findViewById(R.id.beauty_sharp);
        beauty_eye_angle1 = findViewById(R.id.beauty_eye_angle1);
        beauty_cut_cheek = findViewById(R.id.beauty_cut_cheek);
        beauty_cut_face = findViewById(R.id.beauty_cut_face);
        beauty_thin_face = findViewById(R.id.beauty_thin_face);
        beauty_long_face = findViewById(R.id.beauty_long_face);
        beauty_lower_jaw = findViewById(R.id.beauty_lower_jaw);
        beauty_higher_jaw = findViewById(R.id.beauty_higher_jaw);
        beauty_thin_jaw = findViewById(R.id.beauty_thin_jaw);
        beauty_mouth_high = findViewById(R.id.beauty_mouth_high);
        beauty_philtrum = findViewById(R.id.beauty_philtrum);
        beauty_thin_mandible = findViewById(R.id.beauty_thin_mandible);
        beauty_big_eye = findViewById(R.id.beauty_big_eye);
        beauty_canthus = findViewById(R.id.beauty_canthus);
        beauty_canthus1 = findViewById(R.id.beauty_canthus1);
        beauty_eye_angle2 = findViewById(R.id.beauty_eye_angle2);
        beauty_eye_tdangle = findViewById(R.id.beauty_eye_tdangle);
        beauty_thin_nose = findViewById(R.id.beauty_thin_nose);
        beauty_nose_wing = findViewById(R.id.beauty_nose_wing);
        beauty_nasal_height = findViewById(R.id.beauty_nasal_height);
        beauty_nose_tip_height = findViewById(R.id.beauty_nose_tip_height);
        beauty_mouth_width = findViewById(R.id.beauty_mouth_width);
        beauty_mouth_size = findViewById(R.id.beauty_mouth_size);

        beauty_whitening.setOnProgressChangeListener(this);
        beauty_buffing.setOnProgressChangeListener(this);
        beauty_sharp.setOnProgressChangeListener(this);
        beauty_eye_angle1.setOnProgressChangeListener(this);
        beauty_cut_cheek.setOnProgressChangeListener(this);
        beauty_cut_face.setOnProgressChangeListener(this);
        beauty_thin_face.setOnProgressChangeListener(this);
        beauty_long_face.setOnProgressChangeListener(this);
        beauty_lower_jaw.setOnProgressChangeListener(this);
        beauty_higher_jaw.setOnProgressChangeListener(this);
        beauty_thin_jaw.setOnProgressChangeListener(this);
        beauty_mouth_high.setOnProgressChangeListener(this);
        beauty_thin_mandible.setOnProgressChangeListener(this);
        beauty_big_eye.setOnProgressChangeListener(this);
        beauty_canthus.setOnProgressChangeListener(this);
        beauty_canthus1.setOnProgressChangeListener(this);
        beauty_eye_angle2.setOnProgressChangeListener(this);
        beauty_eye_tdangle.setOnProgressChangeListener(this);
        beauty_thin_nose.setOnProgressChangeListener(this);
        beauty_nose_wing.setOnProgressChangeListener(this);
        beauty_nasal_height.setOnProgressChangeListener(this);
        beauty_nose_tip_height.setOnProgressChangeListener(this);
        beauty_mouth_width.setOnProgressChangeListener(this);
        beauty_mouth_size.setOnProgressChangeListener(this);
        beauty_philtrum.setOnProgressChangeListener(this);

        //默认磨皮 锐化，美白 百分80
        beauty_whitening.setProgress(80);
        beauty_buffing.setProgress(80);
        beauty_sharp.setProgress(80);

        mTvShowBeauty = findViewById(R.id.tv_beauty);
        mRlRaceContent = findViewById(R.id.race_content);
        TextView finish = findViewById(R.id.tv_finish);
        mTvJoinChannel = findViewById(R.id.tv_join_channel);
        mTvSure = findViewById(R.id.tv_sure);
        mTvSure.setOnClickListener(this);
        mLocalView = findViewById(R.id.sf_local_view);
        View parentView = findViewById(R.id.chart_parent);
        SwitchCompat btn_switch_beauty = findViewById(R.id.btn_switch_beauty);
        //默认开启美颜
        btn_switch_beauty.setChecked(true);
        btn_switch_beauty.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mRaceBeautyImp == null) {
                    return;
                }
                if (isChecked) {
                    mRaceBeautyImp.setFilterOn(true);
                } else {
                    mRaceBeautyImp.setFilterOn(false);
                }
            }
        });

        mRaceBeautyImp = new RaceBeautyImp(this);


        if (AliRtcConstants.BRAND_OPPO.equalsIgnoreCase(Build.BRAND) && AliRtcConstants.MODEL_OPPO_R17.equalsIgnoreCase(
                Build.MODEL)) {
            parentView.setPadding(0, DensityUtils.dip2px(this, 20), 0, 0);
        }
        finish.setOnClickListener(this);
        mTvJoinChannel.setOnClickListener(this);
        // 承载远程User的Adapter
        mUserListAdapter = new ChartUserAdapter();
        RecyclerView chartUserListView = findViewById(R.id.chart_content_userlist);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        chartUserListView.setLayoutManager(layoutManager);
        chartUserListView.addItemDecoration(new BaseRecyclerViewAdapter.DividerGridItemDecoration(
                getResources().getDrawable(R.drawable.chart_content_userlist_item_divider)));
        DefaultItemAnimator anim = new DefaultItemAnimator();
        anim.setSupportsChangeAnimations(false);
        chartUserListView.setItemAnimator(anim);
        chartUserListView.setAdapter(mUserListAdapter);
        mUserListAdapter.setOnSubConfigChangeListener(mOnSubConfigChangeListener);
        chartUserListView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {
                Log.i(TAG, "onChildViewAttachedToWindow : " + view);

            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                Log.i(TAG, "onChildViewDetachedFromWindow : " + view);
            }
        });
        mTvShowBeauty.setOnClickListener(this);
    }

    private void initRTCEngineAndStartPreview() {
        if (this.checkPermission(Manifest.permission.CAMERA) || checkPermission(
                Manifest.permission.MODIFY_AUDIO_SETTINGS)) {
            Toast.makeText(this.getApplicationContext(), "需要开启权限才可进行观看", Toast.LENGTH_SHORT).show();
            mGrantPermission = false;
            return;
        }
        mGrantPermission = true;
        // 防止初始化过多
        if (mAliRtcEngine == null) {
            JSONObject jsonObject = new JSONObject();
            //初始化支持美颜的sdk
            try {
                jsonObject.put("user_specified_video_preprocess", "TRUE");
                //实例化,必须在主线程进行。
                mAliRtcEngine = AliRtcEngine.getInstance(getApplicationContext(), jsonObject.toString());
                //设置事件的回调监听
                mAliRtcEngine.setRtcEngineEventListener(mEventListener);
                //设置接受通知事件的回调
                mAliRtcEngine.setRtcEngineNotify(mEngineNotify);
                // 初始化本地视图
                initLocalView();
                //开启预览
                startPreview();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
//        mRaceBeautyImp.setSkinWhitening(0.8f);
//        mRaceBeautyImp.setSkinBuffing(0.8f);
//        mRaceBeautyImp.setSharpen(0.8f);

        //注册美颜
        mAliRtcEngine.RegisterTexturePreObserver("", mRaceBeautyImp);


    }

    private void openJoinChannelBeforeNeedParams() {
        //开启音频采集
        if (mIsAudioCapture) {
            mAliRtcEngine.startAudioCapture();
        } else {
            mAliRtcEngine.stopAudioCapture();
        }
        //开启音频播放
        if (mIsAudioPlay) {
            mAliRtcEngine.startAudioPlayer();
        } else {
            mAliRtcEngine.stopAudioPlayer();
        }
    }

    private boolean checkPermission(String permission) {
        try {
            int i = ActivityCompat.checkSelfPermission(this, permission);
            if (i != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        } catch (RuntimeException e) {
            return true;
        }
        return false;
    }

    private void startPreview() {
        if (mAliRtcEngine == null) {
            return;
        }
        try {
            mAliRtcEngine.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置本地的预览视图的view
     */
    private void initLocalView() {
        mLocalView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        mLocalView.setZOrderOnTop(false);
        mLocalView.setZOrderMediaOverlay(false);
        AliRtcEngine.AliVideoCanvas aliVideoCanvas = new AliRtcEngine.AliVideoCanvas();
        aliVideoCanvas.view = mLocalView;
        aliVideoCanvas.renderMode = AliRtcRenderModeAuto;
        if (mAliRtcEngine != null) {
            mAliRtcEngine.setLocalViewConfig(aliVideoCanvas, AliRtcVideoTrackCamera);
        }
    }

    private void joinChannel() {
        if (mAliRtcEngine == null) {
            return;
        }
        AliRtcAuthInfo userInfo = new AliRtcAuthInfo();
        userInfo.setAppid(mRtcAuthInfo.data.appid);
        userInfo.setNonce(mRtcAuthInfo.data.nonce);
        userInfo.setTimestamp(mRtcAuthInfo.data.timestamp);
        userInfo.setUserId(mRtcAuthInfo.data.userid);
        userInfo.setGslb(mRtcAuthInfo.data.gslb);
        userInfo.setToken(mRtcAuthInfo.data.token);
        userInfo.setConferenceId(mChannel);
        /*
         *设置自动发布和订阅，只能在joinChannel之前设置
         *参数1    true表示自动发布；false表示手动发布
         *参数2    true表示自动订阅；false表示手动订阅
         */
        mAliRtcEngine.setAutoPublish(true, true);
        // 加入频道
        mAliRtcEngine.joinChannel(userInfo, mUsername);

    }

    private void addRemoteUser(String uid) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //判断用户是否在线
                if (null == mAliRtcEngine) {
                    return;
                }
                AliRtcRemoteUserInfo remoteUserInfo = mAliRtcEngine.getUserInfo(uid);
                if (remoteUserInfo != null) {
                    mUserListAdapter.updateData(convertRemoteUserToUserData(remoteUserInfo), true);
                }
            }
        });
    }

    private void removeRemoteUser(String uid) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mUserListAdapter.removeData(uid, true);
            }
        });
    }

    private void updateRemoteDisplay(String uid, AliRtcEngine.AliRtcAudioTrack at, AliRtcEngine.AliRtcVideoTrack vt) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (null == mAliRtcEngine) {
                    return;
                }
                AliRtcRemoteUserInfo remoteUserInfo = mAliRtcEngine.getUserInfo(uid);
                // 如果没有，说明已经退出了或者不存在。则不需要添加，并且删除
                if (remoteUserInfo == null) {
                    // remote user exit room
                    Log.e(TAG, "updateRemoteDisplay remoteUserInfo = null, uid = " + uid);
                    return;
                }
                //change
                AliRtcEngine.AliVideoCanvas cameraCanvas = remoteUserInfo.getCameraCanvas();
                AliRtcEngine.AliVideoCanvas screenCanvas = remoteUserInfo.getScreenCanvas();
                //视频情况
                if (vt == AliRtcVideoTrackNo) {
                    //没有视频流
                    cameraCanvas = null;
                    screenCanvas = null;
                } else if (vt == AliRtcVideoTrackCamera) {
                    //相机流
                    screenCanvas = null;
                    cameraCanvas = createCanvasIfNull(cameraCanvas);
                    //SDK内部提供进行播放的view
                    mAliRtcEngine.setRemoteViewConfig(cameraCanvas, uid, AliRtcVideoTrackCamera);
                } else if (vt == AliRtcVideoTrackScreen) {
                    //屏幕流
                    cameraCanvas = null;
                    screenCanvas = createCanvasIfNull(screenCanvas);
                    //SDK内部提供进行播放的view
                    mAliRtcEngine.setRemoteViewConfig(screenCanvas, uid, AliRtcVideoTrackScreen);
                } else if (vt == AliRtcVideoTrackBoth) {
                    //多流
                    cameraCanvas = createCanvasIfNull(cameraCanvas);
                    //SDK内部提供进行播放的view
                    mAliRtcEngine.setRemoteViewConfig(cameraCanvas, uid, AliRtcVideoTrackCamera);
                    screenCanvas = createCanvasIfNull(screenCanvas);
                    //SDK内部提供进行播放的view
                    mAliRtcEngine.setRemoteViewConfig(screenCanvas, uid, AliRtcVideoTrackScreen);
                } else {
                    return;
                }
                ChartUserBean chartUserBean = convertRemoteUserInfo(remoteUserInfo, cameraCanvas, screenCanvas);
                mUserListAdapter.updateData(chartUserBean, true);

            }
        });
    }

    private ChartUserBean convertRemoteUserToUserData(AliRtcRemoteUserInfo remoteUserInfo) {
        String uid = remoteUserInfo.getUserID();
        ChartUserBean ret = mUserListAdapter.createDataIfNull(uid);
        ret.mUserId = uid;
        ret.mUserName = remoteUserInfo.getDisplayName();
        ret.mIsCameraFlip = false;
        ret.mIsScreenFlip = false;
        return ret;
    }

    private AliRtcEngine.AliVideoCanvas createCanvasIfNull(AliRtcEngine.AliVideoCanvas canvas) {
        if (canvas == null || canvas.view == null) {
            //创建canvas，Canvas为SophonSurfaceView或者它的子类
            canvas = new AliRtcEngine.AliVideoCanvas();
            SophonSurfaceView surfaceView = new SophonSurfaceView(this);
            surfaceView.setZOrderOnTop(true);
            surfaceView.setZOrderMediaOverlay(true);
            canvas.view = surfaceView;
            //renderMode提供四种模式：Auto、Stretch、Fill、Crop，建议使用Auto模式。
            canvas.renderMode = AliRtcRenderModeAuto;
        }
        return canvas;
    }

    private ChartUserBean convertRemoteUserInfo(AliRtcRemoteUserInfo remoteUserInfo,
                                                AliRtcEngine.AliVideoCanvas cameraCanvas,
                                                AliRtcEngine.AliVideoCanvas screenCanvas) {
        String uid = remoteUserInfo.getUserID();
        ChartUserBean ret = mUserListAdapter.createDataIfNull(uid);
        ret.mUserId = remoteUserInfo.getUserID();

        ret.mUserName = remoteUserInfo.getDisplayName();

        ret.mCameraSurface = cameraCanvas != null ? cameraCanvas.view : null;
        ret.mIsCameraFlip = cameraCanvas != null && cameraCanvas.mirrorMode == AliRtcEngine.AliRtcRenderMirrorMode.AliRtcRenderMirrorModeAllEnabled;

        ret.mScreenSurface = screenCanvas != null ? screenCanvas.view : null;
        ret.mIsScreenFlip = screenCanvas != null && screenCanvas.mirrorMode == AliRtcEngine.AliRtcRenderMirrorMode.AliRtcRenderMirrorModeAllEnabled;

        return ret;
    }

    /**
     * 特殊错误码回调的处理方法
     *
     * @param error 错误码
     */
    private void processOccurError(int error) {
        switch (error) {
            case SOPHON_SERVER_ERROR_POLLING:
            case SOPHON_RESULT_SIGNAL_HEARTBEAT_TIMEOUT:
                noSessionExit(error);
                break;
            default:
                break;
        }
    }

    /**
     * 错误处理
     *
     * @param error 错误码
     */
    private void noSessionExit(int error) {
        runOnUiThread(() -> new AlertDialog.Builder(AliRtcChatActivity.this)
                .setTitle("ErrorCode : " + error)
                .setMessage("网络超时，请退出房间")
                .setPositiveButton("确定", (dialog, which) -> {
                    dialog.dismiss();
                    onBackPressed();
                })
                .create()
                .show());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //销毁服务
        if (null != mForeServiceIntent && AppUtils.isServiceRunning(this.getApplicationContext(),
                ForegroundService.class.getName())) {
            stopService(mForeServiceIntent);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_finish:
                //销毁服务
                if (null != mForeServiceIntent && AppUtils.isServiceRunning(this.getApplicationContext(),
                        ForegroundService.class.getName())) {
                    stopService(mForeServiceIntent);
                }
                //离会
                if (mAliRtcEngine != null) {
                    mAliRtcEngine.setRtcEngineNotify(null);
                    mAliRtcEngine.setRtcEngineEventListener(null);
                    mAliRtcEngine.stopPreview();
                    mAliRtcEngine.leaveChannel();
                }
                finish();
                break;
            case R.id.tv_join_channel:
                if (mGrantPermission) {
                    joinChannel();
                } else {
                    setUpSplash();
                }
                break;
            case R.id.tv_beauty:
            case R.id.tv_sure:
                if (mRlRaceContent.getVisibility() == View.VISIBLE) {
                    mRlRaceContent.setVisibility(View.GONE);
                } else if (mRlRaceContent.getVisibility() == View.GONE) {
                    mRlRaceContent.setVisibility(View.VISIBLE);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAliRtcEngine != null) {
            mAliRtcEngine.UnRegisterTexturePreObserver("");
            mAliRtcEngine.destroy();
        }
    }


    /**
     * 用户操作回调监听(回调接口都在子线程)
     */
    private AliRtcEngineEventListener mEventListener = new AliRtcEngineEventListener() {

        /**
         * 加入房间的回调
         * @param i 结果码
         */
        @Override
        public void onJoinChannelResult(int i) {
            runOnUiThread(() -> {
                if (i == 0) {
                    mTvJoinChannel.setVisibility(View.GONE);
                    //开启前台服务
                    if (null == mForeServiceIntent) {
                        mForeServiceIntent = new Intent(AliRtcChatActivity.this.getApplicationContext(),
                                ForegroundService.class);
                        mForeServiceIntent.putExtras(mBundle);
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(mForeServiceIntent);
                    } else {
                        startService(mForeServiceIntent);
                    }
                }
            });
        }

        /**
         * 离开房间的回调
         * @param i 结果码
         */
        @Override
        public void onLeaveChannelResult(int i) {

        }

        /**
         * 推流的回调
         * @param i 结果码
         * @param s publishId
         */
        @Override
        public void onPublishResult(int i, String s) {

        }

        /**
         * 取消发布本地流回调
         * @param i 结果码
         */
        @Override
        public void onUnpublishResult(int i) {

        }

        /**
         * 订阅成功的回调
         * @param s userid
         * @param i 结果码
         * @param aliRtcVideoTrack 视频的track
         * @param aliRtcAudioTrack 音频的track
         */
        @Override
        public void onSubscribeResult(String s, int i, AliRtcEngine.AliRtcVideoTrack aliRtcVideoTrack,
                                      AliRtcEngine.AliRtcAudioTrack aliRtcAudioTrack) {
            if (i == 0) {
                updateRemoteDisplay(s, aliRtcAudioTrack, aliRtcVideoTrack);
            }
        }

        /**
         * 取消的回调
         * @param i 结果码
         * @param s userid
         */
        @Override
        public void onUnsubscribeResult(int i, String s) {
            updateRemoteDisplay(s, AliRtcAudioTrackNo, AliRtcVideoTrackNo);
        }

        /**
         * 网络状态变化的回调
         * @param aliRtcNetworkQuality
         */
        @Override
        public void onNetworkQualityChanged(String s, AliRtcEngine.AliRtcNetworkQuality aliRtcNetworkQuality, AliRtcEngine.AliRtcNetworkQuality aliRtcNetworkQuality1) {

        }

        /**
         * 出现警告的回调
         * @param i
         */
        @Override
        public void onOccurWarning(int i) {

        }

        /**
         * 出现错误的回调
         * @param error 错误码
         */
        @Override
        public void onOccurError(int error) {
            //错误处理
            processOccurError(error);
        }

        /**
         * 当前设备性能不足
         */
        @Override
        public void onPerformanceLow() {

        }

        /**
         * 当前设备性能恢复
         */
        @Override
        public void onPermormanceRecovery() {

        }

        /**
         * 连接丢失
         */
        @Override
        public void onConnectionLost() {

        }

        /**
         * 尝试恢复连接
         */
        @Override
        public void onTryToReconnect() {

        }

        /**
         * 连接已恢复
         */
        @Override
        public void onConnectionRecovery() {

        }

        /**
         * @param aliRTCSDK_client_role
         * @param aliRTCSDK_client_role1
         * 用户角色更新
         */
        @Override
        public void onUpdateRoleNotify(ALI_RTC_INTERFACE.AliRTCSDK_Client_Role aliRTCSDK_client_role, ALI_RTC_INTERFACE.AliRTCSDK_Client_Role aliRTCSDK_client_role1) {

        }
    };

    /**
     * SDK事件通知(回调接口都在子线程)
     */
    private AliRtcEngineNotify mEngineNotify = new AliRtcEngineNotify() {
        /**
         * 远端用户停止发布通知，处于OB（observer）状态
         * @param aliRtcEngine 核心引擎对象
         * @param s userid
         */
        @Override
        public void onRemoteUserUnPublish(AliRtcEngine aliRtcEngine, String s) {
            updateRemoteDisplay(s, AliRtcAudioTrackNo, AliRtcVideoTrackNo);
        }

        /**
         * 远端用户上线通知
         * @param s userid
         */
        @Override
        public void onRemoteUserOnLineNotify(String s) {
            addRemoteUser(s);
        }

        /**
         * 远端用户下线通知
         * @param s userid
         */
        @Override
        public void onRemoteUserOffLineNotify(String s) {
            removeRemoteUser(s);
        }

        /**
         * 远端用户发布音视频流变化通知
         * @param s userid
         * @param aliRtcAudioTrack 音频流
         * @param aliRtcVideoTrack 相机流
         */
        @Override
        public void onRemoteTrackAvailableNotify(String s, AliRtcEngine.AliRtcAudioTrack aliRtcAudioTrack,
                                                 AliRtcEngine.AliRtcVideoTrack aliRtcVideoTrack) {
            updateRemoteDisplay(s, aliRtcAudioTrack, aliRtcVideoTrack);
        }

        /**
         * 订阅流回调，可以做UI及数据的更新
         * @param s userid
         * @param aliRtcAudioTrack 音频流
         * @param aliRtcVideoTrack 相机流
         */
        @Override
        public void onSubscribeChangedNotify(String s, AliRtcEngine.AliRtcAudioTrack aliRtcAudioTrack,
                                             AliRtcEngine.AliRtcVideoTrack aliRtcVideoTrack) {

        }

        /**
         * 订阅信息
         * @param aliSubscriberInfos 订阅自己这边流的user信息
         * @param i 当前订阅人数
         */
        @Override
        public void onParticipantSubscribeNotify(AliSubscriberInfo[] aliSubscriberInfos, int i) {

        }

        /**
         * 首帧的接收回调
         * @param s callId
         * @param s1 stream_label
         * @param s2 track_label 分为video和audio
         * @param i 时间
         */
        @Override
        public void onFirstFramereceived(String s, String s1, String s2, int i) {

            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(AliRtcChatActivity.this, "首帧接受成功", Toast.LENGTH_SHORT).show();

                }
            });


        }

        /**
         * 首包的发送回调
         * @param s callId
         * @param s1 stream_label
         * @param s2 track_label 分为video和audio
         * @param i 时间
         */
        @Override
        public void onFirstPacketSent(String s, String s1, String s2, int i) {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(AliRtcChatActivity.this, "首包发送成功", Toast.LENGTH_SHORT).show();

                }
            });
        }

        /**
         *首包数据接收成功
         * @param callId 远端用户callId
         * @param streamLabel 远端用户的流标识
         * @param trackLabel 远端用户的媒体标识
         * @param timeCost 耗时
         */
        @Override
        public void onFirstPacketReceived(String callId, String streamLabel, String trackLabel, int timeCost) {

        }

        /**
         * 取消订阅信息回调
         * @param aliParticipantInfos 订阅自己这边流的user信息
         * @param i 当前订阅人数
         */
        @Override
        public void onParticipantUnsubscribeNotify(AliParticipantInfo[] aliParticipantInfos, int i) {

        }

        /**
         * 被服务器踢出或者频道关闭时回调
         * @param i
         */
        @Override
        public void onBye(int i) {

        }

        @Override
        public void onParticipantStatusNotify(AliStatusInfo[] aliStatusInfos, int i) {

        }

        /**
         * @param aliRtcStats
         * 实时数据回调(2s触发一次)
         */
        @Override
        public void onAliRtcStats(ALI_RTC_INTERFACE.AliRtcStats aliRtcStats) {

        }
    };


    private ChartUserAdapter.OnSubConfigChangeListener mOnSubConfigChangeListener = new ChartUserAdapter.OnSubConfigChangeListener() {
        @Override
        public void onFlipView(String uid, int flag, boolean flip) {
            AliRtcRemoteUserInfo userInfo = mAliRtcEngine.getUserInfo(uid);
            switch (flag) {
                case AliRtcConstants.CAMERA:
                    if (userInfo != null) {
                        AliRtcEngine.AliVideoCanvas cameraCanvas = userInfo.getCameraCanvas();
                        if (cameraCanvas != null) {
                            cameraCanvas.mirrorMode = flip ? AliRtcEngine.AliRtcRenderMirrorMode.AliRtcRenderMirrorModeAllEnabled : AliRtcEngine.AliRtcRenderMirrorMode.AliRtcRenderMirrorModeAllDisable;
                            mAliRtcEngine.setRemoteViewConfig(cameraCanvas, uid, AliRtcVideoTrackCamera);
                        }
                    }
                    break;
                case AliRtcConstants.SCREEN:
                    if (userInfo != null) {
                        AliRtcEngine.AliVideoCanvas screenCanvas = userInfo.getScreenCanvas();
                        if (screenCanvas != null) {
                            screenCanvas.mirrorMode = flip ? AliRtcEngine.AliRtcRenderMirrorMode.AliRtcRenderMirrorModeAllEnabled : AliRtcEngine.AliRtcRenderMirrorMode.AliRtcRenderMirrorModeAllDisable;
                            mAliRtcEngine.setRemoteViewConfig(screenCanvas, uid, AliRtcVideoTrackScreen);
                        }
                    }
                    break;
            }
        }

        @Override
        public void onShowVideoInfo(String uid, int flag) {
            AliRtcEngine.AliRtcVideoTrack track = AliRtcVideoTrackNo;
            switch (flag) {
                case AliRtcConstants.CAMERA:
                    track = AliRtcVideoTrackCamera;
                    break;
                case AliRtcConstants.SCREEN:
                    track = AliRtcVideoTrackScreen;
                    break;
            }
            if (mAliRtcEngine != null) {
                String result = mAliRtcEngine.getMediaInfoWithUserId(uid, track, VIDEO_INFO_KEYS);

                Toast.makeText(AliRtcChatActivity.this, result, Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * 美颜参数选项
     *
     * @param seekBar  The DiscreteSeekBar
     * @param value    the new value
     * @param fromUser if the change was made from the user or not (i.e. the developer calling {@link #setProgress(int)}
     */
    @Override
    public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

    }

    /***
     * 滑动停止之后才开始赋值
     * @param seekBar
     */
    @Override
    public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
        if (mRaceBeautyImp == null) {
            return;
        }
        int progress = seekBar.getProgress();
//
        switch (seekBar.getId()) {
            case R.id.beauty_whitening:
                mRaceBeautyImp.setSkinWhitening(progress / 100f);
                break;
            case R.id.beauty_buffing:
                mRaceBeautyImp.setSkinBuffing(progress / 100f);
                break;
            case R.id.beauty_sharp:
                mRaceBeautyImp.setSharpen(progress / 100f);
                break;
            case R.id.beauty_cut_cheek:
                //颧骨[0,1]
                mRaceBeautyImp.setRaceBeautyType(AliyunBeautifyNative.ALR_FACE_SHAPE_TYPE_CUT_CHEEK, progress / 100f);
                break;
            case R.id.beauty_cut_face:
                //削脸[0,1]
                mRaceBeautyImp.setRaceBeautyType(AliyunBeautifyNative.ALR_FACE_SHAPE_TYPE_CUT_FACE, progress / 100f);
                break;
            case R.id.beauty_thin_face:
                //瘦脸       [0,1]
                mRaceBeautyImp.setRaceBeautyType(AliyunBeautifyNative.ALR_FACE_SHAPE_TYPE_THIN_FACE, progress / 100f);
                break;
            case R.id.beauty_long_face:
                //脸长       [0,1]
                mRaceBeautyImp.setRaceBeautyType(AliyunBeautifyNative.ALR_FACE_SHAPE_TYPE_LONG_FACE, progress / 100f);
                break;
            case R.id.beauty_lower_jaw:
                //下巴缩短   [-1,1]
                mRaceBeautyImp.setRaceBeautyType(AliyunBeautifyNative.ALR_FACE_SHAPE_TYPE_LOWER_JAW, (progress - 50) * 2 / 100f);
                break;
            case R.id.beauty_higher_jaw:
                //下巴拉长   [-1,1]
                mRaceBeautyImp.setRaceBeautyType(AliyunBeautifyNative.ALR_FACE_SHAPE_TYPE_HIGHER_JAW, (progress - 50) * 2 / 100f);
                break;
            case R.id.beauty_thin_jaw:
                //瘦下巴   [-1,1]
                mRaceBeautyImp.setRaceBeautyType(AliyunBeautifyNative.ALR_FACE_TYPE_THIN_JAW, (progress - 50) * 2 / 100f);
                break;
            case R.id.beauty_thin_mandible:
                //瘦下颌    [0,1]
                mRaceBeautyImp.setRaceBeautyType(AliyunBeautifyNative.ALR_FACE_TYPE_THIN_MANDIBLE, progress / 100f);
                break;
            case R.id.beauty_big_eye:
                //        //大眼        [0,1]
                mRaceBeautyImp.setRaceBeautyType(AliyunBeautifyNative.ALR_FACE_SHAPE_TYPE_BIG_EYE, progress / 100f);
                break;
            case R.id.beauty_eye_angle1:
                //眼角1   [0,1]
                mRaceBeautyImp.setRaceBeautyType(AliyunBeautifyNative.ALR_FACE_SHAPE_TYPE_EYE_ANGLE1, progress / 100f);
                break;
            case R.id.beauty_canthus:
                //眼距   [-1,1]
                mRaceBeautyImp.setRaceBeautyType(AliyunBeautifyNative.ALR_FACE_SHAPE_TYPE_CANTHUS, (progress - 50) * 2 / 100f);
                break;
            case R.id.beauty_canthus1:

                //拉宽眼距   [-1,1]
                mRaceBeautyImp.setRaceBeautyType(AliyunBeautifyNative.ALR_FACE_SHAPE_TYPE_CANTHUS1, (progress - 50) * 2 / 100f);
                break;
            case R.id.beauty_eye_angle2:
                //眼角2   [-1,1]
                mRaceBeautyImp.setRaceBeautyType(AliyunBeautifyNative.ALR_FACE_SHAPE_TYPE_EYE_ANGLE2, (progress - 50) * 2 / 100f);
                break;
            case R.id.beauty_eye_tdangle:
                //眼睛高度   [-1,1]
                mRaceBeautyImp.setRaceBeautyType(AliyunBeautifyNative.ALR_FACE_TYPE_EYE_TDANGLE, (progress - 50) * 2 / 100f);
                break;
            case R.id.beauty_thin_nose:
                //瘦鼻   [0,1]
                mRaceBeautyImp.setRaceBeautyType(AliyunBeautifyNative.ALR_FACE_SHAPE_TYPE_THIN_NOSE, progress / 100f);
                break;
            case R.id.beauty_nose_wing:
                //鼻翼   [0,1]
                mRaceBeautyImp.setRaceBeautyType(AliyunBeautifyNative.ALR_FACE_SHAPE_TYPE_NOSEWING, progress / 100f);
                break;
            case R.id.beauty_nasal_height:
                //鼻长   [-1,1]
                mRaceBeautyImp.setRaceBeautyType(AliyunBeautifyNative.ALR_FACE_SHAPE_TYPE_NASAL_HEIGHT, (progress - 50) * 2 / 100f);
                break;
            case R.id.beauty_nose_tip_height:
                //鼻头长   [-1,1]
                mRaceBeautyImp.setRaceBeautyType(AliyunBeautifyNative.ALR_FACE_TYPE_NOSE_TIP_HEIGHT, (progress - 50) * 2 / 100f);
                break;
            case R.id.beauty_mouth_width:
                //唇宽   [-1,1]
                mRaceBeautyImp.setRaceBeautyType(AliyunBeautifyNative.ALR_FACE_SHAPE_TYPE_MOUTH_WIDTH, (progress - 50) * 2 / 100f);
                break;
            case R.id.beauty_mouth_size:
                //嘴唇大小   [-1,1]
                mRaceBeautyImp.setRaceBeautyType(AliyunBeautifyNative.ALR_FACE_TYPE_MOUTH_SIZE, (progress - 50) * 2 / 100f);
                break;
            case R.id.beauty_mouth_high:
                //唇高   [-1,1]
                mRaceBeautyImp.setRaceBeautyType(AliyunBeautifyNative.ALR_FACE_SHAPE_TYPE_MOUTH_HIGH, (progress - 50) * 2 / 100f);
                break;
            case R.id.beauty_philtrum:
                //人中   [-1,1]
                mRaceBeautyImp.setRaceBeautyType(AliyunBeautifyNative.ALR_FACE_SHAPE_TYPE_PHILTRUM, (progress - 50) * 2 / 100f);
                break;
            default:
                break;
        }
    }
}
