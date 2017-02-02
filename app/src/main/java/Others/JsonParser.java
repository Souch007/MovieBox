package Others;

import android.view.Window;
import android.view.WindowManager;

import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.core.PlayerState;
import com.longtailvideo.jwplayer.events.listeners.AdvertisingEvents;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Bir Al Sabia on 5/16/2016.
 */
public class JsonParser {
    static InputStream inputStream;
    JSONObject jsonObject;
    static String json="";
//    public static String BASE_URL = ((MyApplication) getApplication()).getBaseUrl();

    public JsonParser(){}
    public JSONObject makeHttpRequest(String URl_METHOD, String method, List<NameValuePair> nameValuePairs) {

        String URI = URl_METHOD+"?";
        try {
            if (method == "POST") {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(URI);
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                inputStream = httpEntity.getContent();

            } else if (method == "GET")
            {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                String paramStrng = URLEncodedUtils.format(nameValuePairs, "utf-8");
                URI += "?" + paramStrng;
                HttpGet httpGet = new HttpGet(URI);
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                inputStream = httpEntity.getContent();
                httpResponse.getStatusLine().getStatusCode();

            }
        }
        catch (UnsupportedEncodingException ue)
        {

            ue.printStackTrace();
        }
        catch (IOException io){

            io.printStackTrace();
        }

        try{

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            inputStream.close();
            json = stringBuilder.toString();


        }
        catch (UnsupportedEncodingException ue){

            ue.printStackTrace();}
        catch (IOException io){

            io.printStackTrace();
        }
        try {
            jsonObject = new JSONObject(json);
        }
        catch (JSONException je){

            je.printStackTrace();
        }
        return jsonObject;
    }

    public static class KeepScreenOnHandler implements VideoPlayerEvents.OnPlayListener,
            VideoPlayerEvents.OnPauseListener,
            VideoPlayerEvents.OnCompleteListener,
            VideoPlayerEvents.OnErrorListener,
            AdvertisingEvents.OnAdPlayListener,
            AdvertisingEvents.OnAdPauseListener,
            AdvertisingEvents.OnAdCompleteListener,
            AdvertisingEvents.OnAdSkippedListener,
            AdvertisingEvents.OnAdErrorListener {

        /**
         * The application window
         */
        private Window mWindow;

        public KeepScreenOnHandler(JWPlayerView jwPlayerView, Window window) {
            jwPlayerView.addOnPlayListener(this);
            jwPlayerView.addOnPauseListener(this);
            jwPlayerView.addOnCompleteListener(this);
            jwPlayerView.addOnErrorListener(this);
            jwPlayerView.addOnAdPlayListener(this);
            jwPlayerView.addOnAdPauseListener(this);
            jwPlayerView.addOnAdCompleteListener(this);
            jwPlayerView.addOnAdErrorListener(this);
            mWindow = window;
        }

        private void updateWakeLock(boolean enable) {
            if(enable) {
                mWindow.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            } else {
                mWindow.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        }

        @Override
        public void onPlay(PlayerState oldState) {
            updateWakeLock(true);
        }

        @Override
        public void onPause(PlayerState oldState) {
            updateWakeLock(false);
        }

        @Override
        public void onComplete() {
            updateWakeLock(false);
        }

        @Override
        public void onError(String message) {
            updateWakeLock(false);
        }

        @Override
        public void onAdPlay(String tag, PlayerState oldState) {
            updateWakeLock(true);
        }

        @Override
        public void onAdPause(String tag, PlayerState oldState) {
            updateWakeLock(false);
        }

        @Override
        public void onAdComplete(String tag) {
            updateWakeLock(false);
        }

        @Override
        public void onAdSkipped(String tag) {
            updateWakeLock(false);
        }

        @Override
        public void onAdError(String tag, String message) {
            updateWakeLock(false);
        }
    }
}
