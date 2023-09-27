package com.example.smoothscrollingdemo;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.agora.rtc2.RtcEngine;
import io.agora.rtc2.video.VideoCanvas;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LiveDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


public class LiveDetailFragment extends Fragment implements RtcEngineEventCallback, YourFragmentInterface {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_CHANNEL_NAME = "channelName";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mChannelName;
    private String mParam2;

    private RtcEngine mRtcEngine;
    private GridLayout mHostVideoContainer;
    private RelativeLayout mPlaceHolderLayout;
    // List to store the UIDs of the users who have joined the channel
    private List<Integer> mUserList = new ArrayList<>();
    // Map to store the last known state for each UID
    // Variables to store the last UID and its state
    private int mLastUid = -1;
    private int mLastState = -1;

    public LiveDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LiveDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LiveDetailFragment newInstance(String param1, String param2) {
        LiveDetailFragment fragment = new LiveDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CHANNEL_NAME, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mChannelName = getArguments().getString(ARG_CHANNEL_NAME);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // You can now use mRtcEngine as needed
        MyApplication myApplication = (MyApplication) getActivity().getApplication();
        mRtcEngine = myApplication.getRtcEngine();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_live_detail, container, false);

        // Initialize the View and set resources
        TextView tvChannelName = view.findViewById(R.id.channelName);
        ImageView imageView = view.findViewById(R.id.user_icon);

        mHostVideoContainer = view.findViewById(R.id.hostVideoContainer);
        mPlaceHolderLayout = view.findViewById(R.id.hostImagePlaceholder);

        tvChannelName.setText(mChannelName);
        imageView.setImageResource(R.mipmap.portrait07);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("VSS", "onStop -- ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onJoinChannelSuccess(String channel, int uid, int elapsed) {

    }

    @Override
    public void onUserJoined(int uid, int elapsed) {
        Log.i("VSS", "onUserJoined -" + uid);
        // Add the UID to the list
        mUserList.add(uid);


    }

    @Override
    public void onRemoteVideoStateChanged(int uid, int state, int reason, int elapsed) {
        Log.i("VSS", "onRemoteVideoStateChanged -" + reason);

        if (reason == 6 && (mLastUid != uid || mLastState != 6)) {

            Log.i("VSS", "onRemoteVideoStateChanged if-" + reason);

            setUpRemoteVideo(uid);
            mLastUid = uid; // Update the last UID
            mLastState = 6; // Update the last state
        } else if ((reason == 5 || reason == 3 || reason == 7) && (mLastUid != uid || mLastState != reason)) {

            Log.i("VSS", "onRemoteVideoStateChanged else if-" + reason);

            removeRemoteVideo(uid);

            mLastUid = uid; // Update the last UID
            mLastState = reason; // Update the last state
        }

    }

    @Override
    public void onError(int err) {

    }

    @Override
    public void onUserOffline(int uid, int reason) {
        Log.i("VSS", "onUserOffline -" + uid);
        // Remove the UID from the list
        mUserList.remove(Integer.valueOf(uid));

    }

    private void setUpRemoteVideo(int uid) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                // Create a new SurfaceView for the user
                SurfaceView surfaceView = new SurfaceView(getActivity());
                surfaceView.setZOrderMediaOverlay(true);
                surfaceView.setId(uid); // Set the ID of the SurfaceView to the UID of the user

                // Add the SurfaceView to the mHostVideoContainer
                mHostVideoContainer.addView(surfaceView);

                // Setup the remote video
                VideoCanvas videoCanvas = new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_HIDDEN, uid);
                mRtcEngine.setupRemoteVideo(videoCanvas);

            }
        });
    }


    private void removeRemoteVideo(int uid) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                try {

                    // Find the SurfaceView with the UID of the user and remove it from the mHostVideoContainer
                    View surfaceView = mHostVideoContainer.findViewById(uid);
                    if (surfaceView != null) {
                        mHostVideoContainer.removeView(surfaceView);
                    }

                } catch (Exception e) {
                    StringWriter sw = new StringWriter();
                    e.printStackTrace(new PrintWriter(sw));
                    String exceptionAsString = sw.toString();
                    Log.e("VSS", "Exception - " + exceptionAsString);
                }

            }
        });
    }

    @Override
    public void onBecameInactive() {

        Log.i("VSS", "removeAllSurfaceView -");
        removeAllUserViews();

    }

    private void removeAllUserViews() {

        //Reset the local initialization
        mLastUid = -1;
        mLastState = -1;

        for (Integer uid : mUserList) {
            View surfaceView = mHostVideoContainer.findViewById(uid);
            if (surfaceView != null) {
                mHostVideoContainer.removeView(surfaceView);
            }
        }
        // Optionally, clear the user list after removing all views
        mUserList.clear();
    }


}