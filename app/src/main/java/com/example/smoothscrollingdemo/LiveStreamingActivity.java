package com.example.smoothscrollingdemo;

import static io.agora.rtc2.Constants.CLIENT_ROLE_AUDIENCE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;

import io.agora.rtc2.RtcEngine;


public class LiveStreamingActivity extends AppCompatActivity {

    public static String[] mChannels = new String[] {
            "TravelDiaries",
            "TechTalks",
            "FitnessFreaks",
            "CulinaryDelights",
            "MovieMania",
            "GamingGalaxy",
            "HistoryHub",
            "InspireInnovate"
    };
    private ViewPager2 mViewPager;
    private LiveStreamingPagerAdapter mViewPagerAdapter;
    private RtcEngineEventCallback mCurrentFragmentCallback;
    private String lastChannel = null;
    private RtcEngine mRtcEngine;
    private Fragment currentFragment;
    private Fragment lastFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_streaming);

        mRtcEngine = ((MyApplication) getApplication()).getRtcEngine();
        mRtcEngine.setClientRole(CLIENT_ROLE_AUDIENCE);
        mRtcEngine.enableVideo();
        mRtcEngine.enableInstantMediaRendering();

        // Initialize the ViewPager2 component
        mViewPager = findViewById(R.id.viewpager);

        // Initialize the custom adapter and set it to the ViewPager2 component
        mViewPagerAdapter = new LiveStreamingPagerAdapter(this);
        mViewPager.setAdapter(mViewPagerAdapter);

        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setOffscreenPageLimit(1);

        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                handleChannelChange(mChannels[position]);
                handleFragmentRegistration(position);
                signalLastFragmentCleanup();

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });


    }

    private void handleChannelChange(String newChannel) {
        // Leave the last channel
        if (lastChannel != null) {
            mRtcEngine.leaveChannel();
        }

        // Join the new channel
        int a = mRtcEngine.joinChannel(null, newChannel, "Extra Optional Data", 0);
        lastChannel = newChannel;
    }

    private void handleFragmentRegistration(int position) {
        // Unregister the previous fragment
        if (mCurrentFragmentCallback != null) {
            ((MyApplication) getApplication()).unregisterCurrentFragment();
        }

        // Register the current fragment
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("f" + position);
        if (fragment instanceof RtcEngineEventCallback) {
            mCurrentFragmentCallback = (RtcEngineEventCallback) fragment;
            ((MyApplication) getApplication()).registerCurrentFragment(mCurrentFragmentCallback);
        }
    }

    private void signalLastFragmentCleanup() {
        lastFragment = currentFragment;
        // Retrieve the new current fragment using its position.
        // You might need to adapt the method to get the fragment depending on your specific setup.
        currentFragment = (Fragment) getSupportFragmentManager().findFragmentByTag("f" + mViewPager.getCurrentItem());

        // Signal the last fragment to perform cleanup if it's not null.
        if (lastFragment != null && lastFragment instanceof YourFragmentInterface) {
            ((YourFragmentInterface) lastFragment).onBecameInactive();
        }
    }

    private class LiveStreamingPagerAdapter extends FragmentStateAdapter {

        public LiveStreamingPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @Override
        public int getItemCount() {
            return mChannels.length;
        }

        public String getChannelName(int position) {
            return mChannels[position];
        }

        @Override
        public long getItemId(int position) {
            // This will set the item ID to the fragment's position, helping in setting the tag.
            return position;
        }

        @Override
        public boolean containsItem(long itemId) {
            // Return true if the current view pager contains the item with the given ID
            return itemId < mChannels.length && itemId >= 0;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return LiveDetailFragment.newInstance(getChannelName(position),"");
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            if(mRtcEngine != null){
                mRtcEngine.leaveChannel();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        if (mCurrentFragmentCallback != null) {
            ((MyApplication) getApplication()).unregisterCurrentFragment();
        }

    }


}