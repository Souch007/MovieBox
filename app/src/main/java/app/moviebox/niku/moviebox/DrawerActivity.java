package app.moviebox.niku.moviebox;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Others.Drawermenu;
import Others.JsonParser;
import Others.MyHelper;
import Others.SingletonModel;
import xyz.sahildave.widget.SearchViewLayout;

public class DrawerActivity extends AppCompatActivity {

    Drawermenu obj;
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FragmentManager mFragmentManager;
    android.support.v4.app.FragmentTransaction mFragmentTransaction;
    CoordinatorLayout clMain;
    int success, menuItemID, currentpostition;
    Menu menu;
    Toolbar toolbar;
    String BASE_URL;
    ArrayList<Drawermenu> arrayid = new ArrayList<>();
    private static final String URL_GETMENUITEMS = "getcategories";
    JsonParser jsonParser = new JsonParser();
    private static final String SUCCESS = "response";
    boolean doubleBackToExitPressedOnce = false;
    public MyHelper myHelper;
    public static DrawerActivity instance;
    public SearchViewLayout searchViewLayout;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        instance=this;
        myHelper=MyHelper.getInstance(DrawerActivity.this);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        BASE_URL = getString(R.string.base_url);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.shitstuff);
        clMain = (CoordinatorLayout) findViewById(R.id.clMain);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                // Do whatever you want here

                mNavigationView.getMenu().findItem(menuItemID).setChecked(true);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // Do whatever you want here
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

// Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();


        mFragmentTransaction.replace(R.id.containerView, new MovieParentTab()).commit();
        //tvHeader.setText("Bkmalls");
        // mNavigationView.getMenu().getItem(0).setChecked(true);

        menu = mNavigationView.getMenu();
        new getDrawerCategories().execute();

        searchViewLayout = (SearchViewLayout) findViewById(R.id.search_view_container);
        searchViewLayout.setHint("Search Movie");
        searchViewLayout.setExpandedContentSupportFragment(this, new MovieParentTab());
     /*   searchViewLayout.setSearchBoxListener(new SearchViewLayout.SearchBoxListener() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(DrawerActivity.instance.myHelper.currentFragmentTag.matches("1")){
                    Fragmentcategory fragmentcategory =new Fragmentcategory();
                    fragmentcategory.startSearching(s.toString());
                } else{
                    FragmentTvshows fragmentTvshows =new FragmentTvshows();
                    fragmentTvshows.startSearching(s.toString());

                }

            }
        });*/
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                mDrawerLayout.closeDrawers();
                menuItemID = menuItem.getItemId();

                for (int i = 0; i < mNavigationView.getMenu().size(); i++) {
                    if (menuItem == mNavigationView.getMenu().getItem(i)) {
                        currentpostition = i;
                        break;
                    }
                }
               /* Bundle bundle = new Bundle();
                String data = arrayid.get(currentpostition).Movieid();
                bundle.putString("id", arrayid.get(currentpostition).Movieid());
                MovieParentTab fragobj = new MovieParentTab();
                fragobj.setArguments(bundle);

                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.containerView, fragobj);
                mFragmentTransaction.commit();
*/
                MovieParentTab fragment = new MovieParentTab();
                final Bundle bundle = new Bundle();
                bundle.putString("id",arrayid.get(currentpostition).Catid());
                Log.i("BUNDLE", bundle.toString());
                fragment.setArguments(bundle);

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.containerView, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
                SingletonModel.getSingletonModel().drawercatid=arrayid.get(currentpostition).Catid();

                return true;
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menufragment, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }*/
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Drawer Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    private class getDrawerCategories extends AsyncTask<String, String, JSONObject>

    {
        protected void onPreExecute() {

            // progressBar.setVisibility(View.VISIBLE);
//            loading1 = ProgressDialog.show(List_Taxi_For_Hire.this, "Loading Cities...", null, true, true);

            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... params) {

            List<NameValuePair> nameValuePairs = new ArrayList<>();
            JSONObject jsonObject = jsonParser.makeHttpRequest(BASE_URL + URL_GETMENUITEMS, "POST", nameValuePairs);
            return jsonObject;

        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {


//            loading1.dismiss();
            // progressBar.setVisibility(View.GONE);
            if (jsonObject.isNull("edges") == false) {
                Toast.makeText(DrawerActivity.this, "Someting went wrong please check your internet connection", Toast.LENGTH_SHORT).show();
            } else {

                try {
                    success = jsonObject.getInt(SUCCESS);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (success == 1) {
                    try {

                        JSONArray jarray = jsonObject.getJSONArray("data");

                        for (int n = 0; n <= jarray.length()-1; n++) {
                            obj = new Drawermenu();
                            JSONObject jop = jarray.getJSONObject(n);
                            menu.add((jop.getString(("name"))));
                            obj.setCatid((jop.getString(("id"))));
                            arrayid.add(obj);
                           /* obj.setCatimgae(jop.getString(("avatar")));
                            obj.setMovieid(jop.getString(("id")));
                            arrayprofessionals.add(obj);*/


                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }

        }
    }

    @Override
    public void onBackPressed() {

       super.onBackPressed();

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finish();
            System.exit(0);
            return;
        }
        else
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press once again to exit", Toast.LENGTH_SHORT).show();
        MovieParentTab fragment = new MovieParentTab();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.containerView, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
        SingletonModel.getSingletonModel().drawercatid=null;

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

   /* @Override
    public void onBackPressed()
    {
        if(getSupportFragmentManager().getBackStackEntryCount() > 0)
            getSupportFragmentManager().popBackStack();
        else
            super.onBackPressed();
    }*/
}
