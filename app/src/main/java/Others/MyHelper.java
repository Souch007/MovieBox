package Others;

import android.content.Context;

/**
 * Created by developer on 1/27/2017.
 */

public class MyHelper {
    public static   MyHelper instance;
    public static Context context;
    public String currentFragmentTag="1";

    public MyHelper(Context mContext)
    {
        context = mContext;
    }

    public static MyHelper getInstance(Context mContext) {
        if (instance == null) {
            instance = new MyHelper(mContext);
        }
        context = mContext;
        return instance;
    }
}
