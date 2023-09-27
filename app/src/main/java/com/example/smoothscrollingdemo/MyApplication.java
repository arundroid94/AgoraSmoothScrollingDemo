package com.example.smoothscrollingdemo;

import android.app.Application;
import android.util.Log;

import io.agora.rtc2.ChannelMediaOptions;
import io.agora.rtc2.Constants;
import io.agora.rtc2.IRtcEngineEventHandler;
import io.agora.rtc2.RtcEngine;
import io.agora.rtc2.RtcEngineConfig;

public class MyApplication extends Application {

    private RtcEngine mRtcEngine;
    private ChannelMediaOptions option;
    private RtcEngineEventCallback mCurrentFragmentCallback;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeRtcEngine();
    }

    private void initializeRtcEngine() {
        // Your existing code to initialize the RtcEngine
        RtcEngineConfig rtcEngineConfig = new RtcEngineConfig();
        rtcEngineConfig.mAppId = getString(R.string.agora_app_id);
        rtcEngineConfig.mContext = getApplicationContext();
        rtcEngineConfig.mEventHandler = iRtcEngineEventHandler; // Make sure this is accessible here
        rtcEngineConfig.mChannelProfile = Constants.CHANNEL_PROFILE_LIVE_BROADCASTING;
        rtcEngineConfig.mAudioScenario = Constants.AudioScenario.getValue(Constants.AudioScenario.DEFAULT);

        try {
            mRtcEngine = RtcEngine.create(rtcEngineConfig);
            option = new ChannelMediaOptions();
            option.channelProfile = Constants.CHANNEL_PROFILE_LIVE_BROADCASTING;
            option.clientRoleType = Constants.CLIENT_ROLE_AUDIENCE;
            option.autoSubscribeAudio = true;
            option.autoSubscribeVideo = true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public RtcEngine getRtcEngine() {
        return mRtcEngine;
    }

    public void registerCurrentFragment(RtcEngineEventCallback callback) {
        this.mCurrentFragmentCallback = callback;
        Log.i("VSS","registerCurrentFragment");

    }

    public void unregisterCurrentFragment() {
        this.mCurrentFragmentCallback = null;
        Log.i("VSS","unregisterCurrentFragment");
    }

    private final IRtcEngineEventHandler iRtcEngineEventHandler = new IRtcEngineEventHandler() {
        @Override
        public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
            super.onJoinChannelSuccess(channel, uid, elapsed);
            if (mCurrentFragmentCallback != null) {
                mCurrentFragmentCallback.onJoinChannelSuccess(channel, uid, elapsed);
            }
        }

        @Override
        public void onUserJoined(int uid, int elapsed) {
            super.onUserJoined(uid, elapsed);
            if (mCurrentFragmentCallback != null) {
                mCurrentFragmentCallback.onUserJoined(uid, elapsed);
            }
        }

        @Override
        public void onRemoteVideoStateChanged(int uid, int state, int reason, int elapsed) {
            super.onRemoteVideoStateChanged(uid, state, reason, elapsed);
            if (mCurrentFragmentCallback != null) {
                mCurrentFragmentCallback.onRemoteVideoStateChanged(uid, state, reason, elapsed);
            }
        }

        @Override
        public void onError(int err) {
            super.onError(err);
            if (mCurrentFragmentCallback != null) {
                mCurrentFragmentCallback.onError(err);
            }
        }

        @Override
        public void onUserOffline(int uid, int reason) {
            super.onUserOffline(uid, reason);
            if (mCurrentFragmentCallback != null) {
                mCurrentFragmentCallback.onUserOffline(uid, reason);
            }
        }
    };
}

