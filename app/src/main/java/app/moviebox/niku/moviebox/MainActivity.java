package app.moviebox.niku.moviebox;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.longtailvideo.jwplayer.JWPlayerSupportFragment;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.core.PlayerState;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.captions.Caption;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Others.JWEventHandler;
import Others.JsonParser;

public class MainActivity extends AppCompatActivity implements VideoPlayerEvents.OnFullscreenListener {
    private static final String URL_GETMOVIES = "getmovielinks";
    private static final String SUCCESS = "response";
    private JWPlayerSupportFragment mPlayerFragment;
    private JWPlayerView mPlayerView;
    private CoordinatorLayout mCoordinatorLayout;
    JsonParser jsonParser = new JsonParser();
    int success;
    WebView wecview;
    String BASE_URL, movieid, avatar,message;
    private JWEventHandler mEventHandler;
    private PlayerState mPlayerState;
    List<Caption> aa;
    List<Caption> captionTracks = new ArrayList<>();
    TextView Movietilte, action, releasedate, description, rating;
    ArrayList<String> movielinks_array = new ArrayList<String>();
    String html = "<iframe src=\"https://r6---sn-2uja-3ipd.googlevideo.com/videoplayback?requiressl=yes&id=80e35592c1cf84d5&itag=18&source=webdrive&ttl=transient&app=explorer&ip=182.180.129.253&ipbits=0&expire=1484142606&sparams=expire,id,ip,ipbits,itag,mm,mn,ms,mv,pl,requiressl,source,ttl&signature=31762A6E39EE4769D9523F29EB399DC3383BD60A.5D0F1D561AC9641E0D5CDFADED0BCF164A3A3688&key=cms1&pl=19&cms_redirect=yes&mm=31&mn=sn-2uja-3ipd&ms=au&mt=1484135457&mv=m\"></iframe>";
String html1 ="<track src=\"captions_output_1484141619.vtt\" kind=\"subtitles\" srclang=\"en\" label=\"English\">";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_example);

        BASE_URL = getString(R.string.base_url);
        Intent getchatintent = getIntent();

        if (getchatintent != null) {
            movieid = getchatintent.getStringExtra("Movieid");

        }
        mPlayerView = (JWPlayerView) findViewById(R.id.jwplayer);
        //wecview = (WebView) findViewById(R.id.webview);

        Movietilte = (TextView) findViewById(R.id.output);
        action = (TextView) findViewById(R.id.Action);
        releasedate = (TextView) findViewById(R.id.relesedate);
        description = (TextView) findViewById(R.id.description);
        rating = (TextView) findViewById(R.id.rating);

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.activity_jwplayerview);
        mPlayerView.addOnFullscreenListener(this);
        new JsonParser.KeepScreenOnHandler(mPlayerView, getWindow());



      /*  WebSettings webViewSettings = wecview.getSettings();
        webViewSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webViewSettings.setJavaScriptEnabled(true);
        wecview.loadData(html, "text/html", null);*/
        // mEventHandler = new JWEventHandler(mPlayerView, outputTextView);

        new getCategorieslinks().execute();

        mPlayerView.addOnErrorListener(new VideoPlayerEvents.OnErrorListener() {
            @Override
            public void onError(String s) {

                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();

               /* if (mPlayerState != PlayerState.PLAYING && mPlayerState != PlayerState.BUFFERING) {
                    // Start playback in case the user hasn't done this yet, since we don't want to have
                    // a movable player that does not play anything...


                for (int i=0;i<movielinks_array.size();i++) {

                    PlaylistItem pi = new PlaylistItem.Builder()
                            .file("https://1fiagea.oloadcdn.net/dl/l/vgnB2pimPI709kLv/Z_k6MXcsdEg/13+Hours.mp4?mime=true")
                            .image(movielinks_array.get(i))
                            .build();
                    mPlayerView.load(pi);
                    if (s.equals(null)) {
                        break;
                    }
                }
                }

*/
            }
        });


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

    /**
     * Handles JW Player going to and returning from fullscreen by hiding the ActionBar
     *
     * @param fullscreen true if the player is fullscreen
     */
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

    private class getCategorieslinks extends AsyncTask<String, String, JSONObject>

    {
        protected void onPreExecute() {

            // progressBar.setVisibility(View.VISIBLE);
//            loading1 = ProgressDialog.show(List_Taxi_For_Hire.this, "Loading Cities...", null, true, true);

            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... params) {

            List<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("movieid", movieid));
            JSONObject jsonObject = jsonParser.makeHttpRequest(BASE_URL + URL_GETMOVIES, "POST", nameValuePairs);
            return jsonObject;

        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {


//            loading1.dismiss();
            // progressBar.setVisibility(View.GONE);

            try {
                success = jsonObject.getInt(SUCCESS);
                message = jsonObject.getString("message");
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (success == 1) {
                try {

                    JSONArray jarray = jsonObject.getJSONArray("data");
                    JSONObject jmoviedetail = jsonObject.getJSONObject("moviedetail");
                    avatar = jmoviedetail.getString("avatar");
                    Movietilte.setText(jmoviedetail.getString("title"));
                    description.setText(jmoviedetail.getString("description"));
                    releasedate.setText(jmoviedetail.getString("release_date"));
                    action.setText(jmoviedetail.getString("movie_type"));
                    rating.setText(jmoviedetail.getString("imdb_rat"));


                    for (int n = 0; n <= jarray.length() - 1; n++) {

                        JSONObject jop = jarray.getJSONObject(n);
                        movielinks_array.add(jop.getString(("link_url")));




// Create a Caption pointing to English subtitles and add it to the list
                     /*   Caption captionEn = new Caption("file_en.srt").label("helooo");
                        captionTracks.add(captionEn)*/;

// Create a Caption pointing to Spanish subtitles, this time using the Builder
                        Caption captionEn = new Caption("http://dev.nsol.sg/projects/moviebox/player/Dunkirk.srt");
                        captionTracks.add(captionEn);
                        Caption captionSp = new Caption.Builder().file("http://dev.nsol.sg/projects/moviebox/player/Dunkirk.srt").label("English").build();
                        captionTracks.add(captionSp);
// Add the Caption tracks to the PlaylistItem

                        PlaylistItem pi = new PlaylistItem.Builder()
                                .file(movielinks_array.get(n))
                                .image(avatar)
                                .build();
                        pi.setCaptions(captionTracks);
                        mPlayerView.load(pi);
                        mPlayerView.addOnCaptionsChangedListener(new VideoPlayerEvents.OnCaptionsChangedListener() {
                            @Override
                            public void onCaptionsChanged(int i, List<Caption> list) {


                            }
                        });
                    }


                } catch (Exception e) {
                    e.printStackTrace();

                }
            } else {
//                ima.setVisibility(View.VISIBLE);
//
//                ima.setBackgroundResource(R.drawable.nored);
//                Toast.makeText(getApplicationContext(), "No Data To Load", Toast.LENGTH_SHORT).show();

//                zerom = String.valueOf(model.getSelectedItem());
//                zeroc = String.valueOf(color.getSelectedItem());
            }

        }

    }

}