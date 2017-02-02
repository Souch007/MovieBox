package app.moviebox.niku.moviebox;

import android.app.Application;

import com.longtailvideo.jwplayer.cast.CastManager;

/**
 * Created by Bir Al Sabia on 12/23/2016.
 */

public class Myapplication extends Application
{
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize the CastManager.
        // The CastManager must be initialized in the Application's context to prevent
        // issues with garbage collection.
        CastManager.initialize(this);
    }
}
