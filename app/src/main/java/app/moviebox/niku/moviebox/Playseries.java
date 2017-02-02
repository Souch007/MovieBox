package app.moviebox.niku.moviebox;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import Others.JsonParser;

public class Playseries extends AppCompatActivity implements VideoPlayerEvents.OnFullscreenListener {
    private JWPlayerView mPlayerView;
    String link,avatar;

    private CoordinatorLayout mCoordinatorLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playseries);
        Intent getter=new Intent();
        if (!getter.equals(null))
        {

            link=getIntent().getExtras().getString("avatar");
            avatar=getIntent().getExtras().getString("linkurl");
        }
        mPlayerView = (JWPlayerView) findViewById(R.id.jwplayer);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.activity_jwplayerview);
        mPlayerView.addOnFullscreenListener(this);
        new JsonParser.KeepScreenOnHandler(mPlayerView, getWindow());
        PlaylistItem pi = new PlaylistItem.Builder()
                .file(avatar)
                .image(link)
                .build();
        mPlayerView.load(pi);
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // Set fullscreen when the device is rotated to landscape
        mPlayerView.setFullscreen(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE, true);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onResume() {
        // Let JW Player know that the app has returned from the background
        mPlayerView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        // Let JW Player know that the app is going to the background
        mPlayerView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        // Let JW Player know that the app is being destroyed
        mPlayerView.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onFullscreen(boolean fullscreen) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if (fullscreen) {
                actionBar.hide();
            } else {
                actionBar.show();
            }
        }

        // When going to Fullscreen we want to set fitsSystemWindows="false"
        mCoordinatorLayout.setFitsSystemWindows(!fullscreen);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Exit fullscreen when the user pressed the Back button
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mPlayerView.getFullscreen()) {
                mPlayerView.setFullscreen(false, true);
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
