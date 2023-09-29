
# AgoraSmoothScrollingViewPager
Building a Smooth Vertical Scrolling Viewpager2 for Live Video Streams using Agora RTC SDK
**Complete guide:** https://medium.com/@arundroid94/building-smooth-scrolling-with-agora-rtc-sdk-in-less-than-90-minutes-6f214ef28447

**Prerequisites to run the demo:**

- Create a new project in the Agora console and select Testing Mode: App ID (do not enable token) when creating the project.
- Once the project is created, navigate to the project page and copy the App ID.
- Paste the App ID into the agora_app_id field in strings.xml.
- No changes are needed for the token, since we are using App ID directly.

**To run the demo:**

- Build and run the app on a device or emulator after adding your Agora App ID.
- The app will connect to Agora using the App ID directly without enabling token authentication.
- Swipe between fragments to preview different video channels.

**Notes:**

- Using App ID directly is only recommended for development/testing purposes. Use token authentication in production apps.
The app demonstrates ViewPager2 with FragmentStateAdapter for smooth channel switching.

- This project demonstrates how to implement smooth scrolling between fragments using ViewPager2 and FragmentStateAdapter.

**Overview**

The app consists of a ViewPager2 that pages between fragments, each containing a video stream for a different channel. When swiping between pages, the video for the new channel is loaded smoothly.

**Some key implementations:**

- Each page is represented by a LiveStreamFragment that handles loading the video stream.
- A custom LiveStreamAdapter extends FragmentStateAdapter to manage page fragments.
- The ViewPager2 uses the adapter to page between fragments.
- Channel switching logic occurs in LiveStreamingActivity based on position.
- Fragments handle lifecycle to clean up resources.

**Setup**
- The main activity LiveStreamingActivity initializes the ViewPager2 and adapter.
- An array of channel names is passed to adapter.
- LiveStreamFragment takes care of UI and video streaming via platform SDK.
- Channel name is passed to fragments via a bundle.

**Usage**
- Swiping between pages will smoothly transition between video streams.
- The streaming relies on callbacks from the platform SDK to handle events.
- Resources are cleaned up in fragment lifecycle methods.

**Notes**
- Used FragmentStateAdapter over RecyclerView adapter for fragment lifecycle management.
- Made sure to cleanup SurfaceViews in onDestroyView() to avoid crashes.
- Handled orientation change to retain fragments and channel state.

