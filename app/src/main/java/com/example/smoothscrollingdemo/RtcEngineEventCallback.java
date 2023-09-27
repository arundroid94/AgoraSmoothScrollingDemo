package com.example.smoothscrollingdemo;

public interface RtcEngineEventCallback {

    void onJoinChannelSuccess(String channel, int uid, int elapsed);
    void onUserJoined(int uid, int elapsed);
    void onRemoteVideoStateChanged(int uid, int state, int reason, int elapsed);
    void onError(int err);
    void onUserOffline(int uid, int reason);

}
