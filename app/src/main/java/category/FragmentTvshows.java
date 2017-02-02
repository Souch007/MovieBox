package category;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bhargavms.dotloader.DotLoader;
import com.github.se_bastiaan.torrentstream.StreamStatus;
import com.github.se_bastiaan.torrentstream.Torrent;
import com.github.se_bastiaan.torrentstream.TorrentStream;
import com.github.se_bastiaan.torrentstream.listeners.TorrentListener;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Adapters.Adapter_for_catgories;
import Adapters.Adapters_tvshows;
import Others.JsonParser;
import Others.Tvshows;
import app.moviebox.niku.moviebox.R;


public class FragmentTvshows extends Fragment implements TorrentListener {
    private ProgressBar mProgressBar;
    private TorrentStream torrentStream;
    private static final String URL_GETMOVIES = "getserial";
    private static final String TORRENT = "Torrent";
    private ProgressBar progressBar;
    DotLoader dotLoader;
    ArrayList<Tvshows> arraytvshows = new ArrayList<>();
    private String streamUrl = "magnet:?xt=urn:btih:D60795899F8488E7E489BA642DEFBCE1B23C9DA0&dn=Kingsman%3A+The+Secret+Service+%282014%29+%5B720p%5D&tr=http%3A%2F%2Ftracker.yify-torrents.com%2Fannounce&tr=udp%3A%2F%2Ftracker.openbittorrent.com%3A80&tr=udp%3A%2F%2Ftracker.publicbt.org%3A80&tr=udp%3A%2F%2Ftracker.coppersurfer.tk%3A6969&tr=udp%3A%2F%2Ftracker.leechers-paradise.org%3A6969&tr=udp%3A%2F%2Fopen.demonii.com%3A1337&tr=udp%3A%2F%2Fp4p.arenabg.ch%3A1337&tr=udp%3A%2F%2Fp4p.arenabg.com%3A1337";
    RecyclerView recyclerViewtv;
    Tvshows obj;
    EditText etSearch;
    String message;
    Adapters_tvshows tvAdapter;
    JsonParser jsonParser = new JsonParser();
    public static int LIMIT_START_KEY = 0;
    LinearLayoutManager Linearlayoutmanager;
    Context context;
    CoordinatorLayout clMain;


    private static final String SUCCESS = "response";
    private SearchView.OnQueryTextListener queryTextListener;
    int
            success,
            pastVisiblesItems,
            visibleItemCount,
            totalItemCount,
            previousTotal = 0;

    String BASE_URL,str_etSearch;
    TextView norecshow;
    boolean
            load = true,
            isViewShown = true, isVisibleToUser = false, isFragmentLoaded = false,IS_SEARCHING = false;;

    public FragmentTvshows() {
        // Required empty public constructor
    }

    /*View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            progressBar.setProgress(0);
            if (torrentStream.isStreaming()) {
                torrentStream.stopStream();
                button.setText("Start stream");
                return;
            }
            torrentStream.startStream(streamUrl);
            button.setText("Stop stream");
        }
    };*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);


        this.isVisibleToUser = isVisibleToUser;

        if (isVisibleToUser && !isFragmentLoaded) {

            if (getView() != null) {
                IS_SEARCHING = false;
                refresh();

                new gettvshows().execute();
            } else {

                isViewShown = false;
            }
        }
    }

    /* @Override public void setUserVisibleHint(boolean isVisibleToUser) {
         super.setUserVisibleHint(isVisibleToUser);
         if (isVisibleToUser && !isFragmentLoaded ) {
             // Load your data here or do network operations here
             isFragmentLoaded = true;
         }
     }*/



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.tvshowsdetail, container, false);
        BASE_URL = getActivity().getString(R.string.base_url);
        recyclerViewtv = (RecyclerView) rootview.findViewById(R.id.recycletv);
        norecshow = (TextView) rootview.findViewById(R.id.idshow);

        Linearlayoutmanager = new LinearLayoutManager(getActivity());
        recyclerViewtv.setLayoutManager(Linearlayoutmanager);
        recyclerViewtv.getRecycledViewPool().setMaxRecycledViews(0, 0);
        dotLoader = (DotLoader)rootview.findViewById(R.id.dot_loader);
        clMain = (CoordinatorLayout)rootview. findViewById(R.id.main_layout);
        context = getActivity();

        /*DrawerActivity.instance.searchViewLayout.setSearchListener(new SearchViewLayout.SearchListener() {
            @Override
            public void onFinished(String searchKeyword) {
                str_etSearch=searchKeyword;
                IS_SEARCHING = true;
                refresh();
                new gettvshows().execute();
            }
        });*/
       // View incHeader = getActivity().findViewById(R.id.relativeLayout);
        //incHeader.setVisibility(View.VISIBLE);


       /* String action = getActivity().getIntent().getAction();
        Uri data = getActivity().getIntent().getData();*/
       /* if (action != null && action.equals(Intent.ACTION_VIEW) && data != null) {
            try {
                streamUrl = URLDecoder.decode(data.toString(), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
*/

        if (!isViewShown) {
            refresh();
            new gettvshows().execute();
        }
        /*if (getView() != null) {
            refresh();

            new gettvshows().execute();
        } else {

            isViewShown = false;
        }*/

        recyclerViewtv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    visibleItemCount = Linearlayoutmanager.getChildCount();
                    totalItemCount = Linearlayoutmanager.getItemCount();
                    pastVisiblesItems = Linearlayoutmanager.findFirstVisibleItemPosition();

                    if (load) {
                        if (totalItemCount > previousTotal) {
                            load = false;
                            previousTotal = totalItemCount;
                        }
                    }
                    if (!load && (totalItemCount - visibleItemCount)
                            <= (pastVisiblesItems + visibleItemCount)) {
                        new gettvshows().execute();
                        load = true;
                    }
                }
            }
        });


       /* TorrentOptions torrentOptions = new TorrentOptions.Builder()
                .saveLocation(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS))
                .removeFilesAfterStop(true)
                .build();

        torrentStream = TorrentStream.init(torrentOptions);
        torrentStream.addListener(this);

        button = (Button)rootview. findViewById(R.id.button);
        button.setOnClickListener(onClickListener);
        progressBar = (ProgressBar)rootview. findViewById(R.id.progress);

        progressBar.setMax(100);*/
        return rootview;
    }


    public  void doing()
    {

    }

    @Override
    public void onStreamProgress(Torrent torrent, StreamStatus status) {
        if (status.bufferProgress <= 100 && progressBar.getProgress() < 100 && progressBar.getProgress() != status.bufferProgress) {
            Log.d(TORRENT, "Progress: " + status.bufferProgress);
            progressBar.setProgress(status.bufferProgress);
        }
    }

    @Override
    public void onStreamPrepared(Torrent torrent) {
        torrent.startDownload();
    }

    @Override
    public void onStreamStarted(Torrent torrent) {

    }

    @Override
    public void onStreamError(Torrent torrent, Exception e) {

    }

    @Override
    public void onStreamReady(Torrent torrent) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(torrent.getVideoFile().toString()));
        intent.setDataAndType(Uri.parse(torrent.getVideoFile().toString()), "video/mp4");
        startActivity(intent);
    }


    @Override
    public void onStreamStopped() {

    }

    public void startSearching(String query) {
        str_etSearch = query;
        IS_SEARCHING = true;
        refresh();
        new gettvshows().execute();

    }

    public void refresh() {
//        IS_SEARCHING = false;
        isViewShown = true;
        LIMIT_START_KEY = 0;
//        isVisibleToUser = false;

        isViewShown = true;
        load = true;
        previousTotal = 0;
        pastVisiblesItems = 0;
        visibleItemCount = 0;
        totalItemCount = 0;


//        start_for_local = 0;
//        end_for_local = start_for_local + limit_for_local;

    }

    private class gettvshows extends AsyncTask<String, String, JSONObject>

    {
        protected void onPreExecute() {

            dotLoader.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dotLoader.setNumberOfDots(4);
                }
            }, 4000);
            if(IS_SEARCHING){
                Toast.makeText(context, "Searching" +etSearch +"Please wait", Toast.LENGTH_SHORT).show();

            }
//            str_etSearch = etSearch.getText().toString();
            // progressBar.setVisibility(View.VISIBLE);
//            loading1 = ProgressDialog.show(List_Taxi_For_Hire.this, "Loading Cities...", null, true, true);

            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... params) {

            List<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("start", String.valueOf(LIMIT_START_KEY)));
            if (IS_SEARCHING==true)
            {  nameValuePairs.add(new BasicNameValuePair(" title",  str_etSearch));

            }
            JSONObject jsonObject = jsonParser.makeHttpRequest(BASE_URL + URL_GETMOVIES, "POST", nameValuePairs);
            return jsonObject;

        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {

            dotLoader.setVisibility(View.GONE);
//            loading1.dismiss();
            // progressBar.setVisibility(View.GONE);
            if (jsonObject.isNull("edges") == false) {
                Toast.makeText(context, "Someting went wrong please check your internet connection", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    success = jsonObject.getInt(SUCCESS);
                    message = jsonObject.getString("description");
                   // Toast.makeText(context, message, Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (success == 1) {
                    try {
                        if (LIMIT_START_KEY == 0) {
                            arraytvshows.clear();
//                        arrayLastName.clear();
//                        arrayFriendId.clear();
//                        arrayStatus.clear();
                        }

                        LIMIT_START_KEY = Integer.valueOf(jsonObject.getString("start"));
                        JSONArray jarray = jsonObject.getJSONArray("data");

                        for (int n = 0; n <= jarray.length() - 1; n++) {
                            obj = new Tvshows();
                            JSONObject jop = jarray.getJSONObject(n);
                            obj.setCattvimage(jop.getString("avatar"));
                            obj.setCattvtitle(jop.getString(("title")));
                            obj.setCattvrating(jop.getString(("release_date")));
                            obj.setCattvdate(jop.getString(("created_date")));
                            obj.setCattvnext(jop.getString(("modified_date")));
                            obj.setSerialid(jop.getString("id"));
                            obj.setDESC(jop.getString("description"));
                           obj.setrating(jop.getString("imdb_rat"));

                            arraytvshows.add(obj);

                        }

                        tvAdapter = new Adapters_tvshows(getActivity(), arraytvshows);
                        int currentPosition = Linearlayoutmanager.findLastVisibleItemPosition();
                        tvAdapter.notifyDataSetChanged();
                        Log.i("arraytvshows",String.valueOf(arraytvshows.size()));

                        recyclerViewtv.setAdapter(tvAdapter);




                        Linearlayoutmanager.scrollToPositionWithOffset(currentPosition - 3, 0);

                        tvAdapter.SetOnItemClickListener(new Adapter_for_catgories.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {


                                Bundle bundle = new Bundle();
                                bundle.putString("id", arraytvshows.get(position).Seriaid());
                                bundle.putString("avatar", arraytvshows.get(position).Cattvimage());
                                bundle.putString("title", arraytvshows.get(position).Cattvtitle());
                                bundle.putString("release_date", arraytvshows.get(position).Cattvrating());
                                bundle.putString("created_date", arraytvshows.get(position).Cattvdate());
                                bundle.putString("description", arraytvshows.get(position).Desc());
                                bundle.putString("imdb_rat", arraytvshows.get(position).Rating());

                                Fragmenttvshowsdetail fragobj = new Fragmenttvshowsdetail();
                                fragobj.setArguments(bundle);
                                FragmentTransaction tr = getFragmentManager().beginTransaction();
                                tr.replace(R.id.containerView, fragobj);
                                tr.addToBackStack(null);
                                tr.commit();

                            }
                        });


                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                } else {
                    norecshow.setVisibility(View.VISIBLE);

                }

            }
        }

    }
   /* @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menufragment, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.i("onQueryTextChange", newText);

                    return true;
                }
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i("onQueryTextSubmit", query);

                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }*/
    @Override
    public void onStart() {
        super.onStart();
    }



    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}