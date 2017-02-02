package category;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.se_bastiaan.torrentstream.TorrentStream;

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
import app.moviebox.niku.moviebox.Playseries;
import app.moviebox.niku.moviebox.R;

/**
 * Created by Bir Al Sabia on 1/9/2017.
 */

public class Fragmenttvshowsdetail extends Fragment {
    private ProgressBar mProgressBar;
    private TorrentStream torrentStream;
    private static final String URL_GETMOVIES = "getepisode";

    ArrayList<Tvshows> arraytvshows = new ArrayList<>();
    private String streamUrl = "magnet:?xt=urn:btih:D60795899F8488E7E489BA642DEFBCE1B23C9DA0&dn=Kingsman%3A+The+Secret+Service+%282014%29+%5B720p%5D&tr=http%3A%2F%2Ftracker.yify-torrents.com%2Fannounce&tr=udp%3A%2F%2Ftracker.openbittorrent.com%3A80&tr=udp%3A%2F%2Ftracker.publicbt.org%3A80&tr=udp%3A%2F%2Ftracker.coppersurfer.tk%3A6969&tr=udp%3A%2F%2Ftracker.leechers-paradise.org%3A6969&tr=udp%3A%2F%2Fopen.demonii.com%3A1337&tr=udp%3A%2F%2Fp4p.arenabg.ch%3A1337&tr=udp%3A%2F%2Fp4p.arenabg.com%3A1337";
    RecyclerView recyclerViewtv;
    Tvshows obj;
    Adapters_tvshows tvAdapter;
    TextView Movietilte, action, releasedate, description, rating;
    JsonParser jsonParser = new JsonParser();
    public static int LIMIT_START_KEY = 0;
    LinearLayoutManager Linearlayoutmanager;
    Context context;
    String message;

    private static final String SUCCESS = "response";
    int
            success,
            pastVisiblesItems,
            visibleItemCount,
            totalItemCount,
            previousTotal = 0;

    String BASE_URL, serialid, avatar, titlee, release_date, createdate, desc, ratringdetail, episodelink;
    boolean
            load = true,
            isViewShown = true, isVisibleToUser = false, isFragmentLoaded = false;

    public Fragmenttvshowsdetail() {

    }

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
                refresh();

                new gettvshows().execute();
            } else {

                isViewShown = false;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_fragment_tvshows, container, false);

        BASE_URL = getActivity().getString(R.string.base_url);

        serialid = this.getArguments().getString("id");
        avatar = this.getArguments().getString("avatar");
        titlee = this.getArguments().getString("title");
        release_date = this.getArguments().getString("release_date");
        createdate = this.getArguments().getString("created_date");
        ratringdetail = this.getArguments().getString("imdb_rat");
        desc = this.getArguments().getString("description");
        // episodelink = this.getArguments().getString("link_url");


        recyclerViewtv = (RecyclerView) rootview.findViewById(R.id.recycletv);
        Movietilte = (TextView) rootview.findViewById(R.id.output);
        action = (TextView) rootview.findViewById(R.id.Action);
        releasedate = (TextView) rootview.findViewById(R.id.relesedate);
        description = (TextView) rootview.findViewById(R.id.description);
        rating = (TextView) rootview.findViewById(R.id.rating);
        Linearlayoutmanager = new LinearLayoutManager(getActivity());

        Movietilte.setText(titlee);
        releasedate.setText(release_date);
        description.setText(desc);
        rating.setText(ratringdetail);


        recyclerViewtv.setLayoutManager(Linearlayoutmanager);
        recyclerViewtv.getRecycledViewPool().setMaxRecycledViews(0, 0);
     //   View incHeader = getActivity().findViewById(R.id.relativeLayout);
       // incHeader.setVisibility(View.GONE);
        context = getActivity();

        new gettvshows().execute();

        if (!isViewShown) {
            refresh();
            new gettvshows().execute();
        }

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


        return rootview;
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

            // progressBar.setVisibility(View.VISIBLE);
//            loading1 = ProgressDialog.show(List_Taxi_For_Hire.this, "Loading Cities...", null, true, true);

            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... params) {

            List<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("sid", serialid));
            nameValuePairs.add(new BasicNameValuePair("start", String.valueOf(LIMIT_START_KEY)));
            JSONObject jsonObject = jsonParser.makeHttpRequest(BASE_URL + URL_GETMOVIES, "POST", nameValuePairs);
            return jsonObject;

        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {


//            loading1.dismiss();
            // progressBar.setVisibility(View.GONE);
            if (jsonObject.isNull("edges") == false) {
                Toast.makeText(context, "Someting went wrong please check your internet connection", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    success = jsonObject.getInt(SUCCESS);
                    message = jsonObject.getString("description");
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (success == 1) {
                    try {
                        if (LIMIT_START_KEY == 0) {
                            arraytvshows.clear();
//
//

                        }

                        LIMIT_START_KEY = Integer.valueOf(jsonObject.getString("start"));
                        JSONArray jarray = jsonObject.getJSONArray("data");
                        //JSONArray jarray1 = jsonObject.getJSONArray("links");

                        final int numberOfItemsInResp1 = jarray.length();
                        for (int n = 0; n < numberOfItemsInResp1; n++) {
                            obj = new Tvshows();
                            try {
                                JSONObject jop = jarray.getJSONObject(n);
                                obj.setCattvimage(jop.getString("avatar"));
                                obj.setCattvtitle(jop.getString(("episode_number")));
                                obj.setCattvrating(jop.getString(("release_date")));
                                obj.setCattvdate(jop.getString(("created_date")));
                                obj.setCattvnext(jop.getString(("modified_date")));
                                JSONArray jarray1 = jop.getJSONArray("links");
                                final int numberOfItemsInResp = jarray1.length();
                                for (int r = 0; r <= numberOfItemsInResp-1; r++) {
                                    JSONObject jop1 = jarray1.getJSONObject(r);
                                    obj.setepilink(jop1.getString(("link_url")));
                                }

                                arraytvshows.add(obj);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        tvAdapter = new Adapters_tvshows(getActivity(), arraytvshows);
                        int currentPosition = Linearlayoutmanager.findLastVisibleItemPosition();
                        tvAdapter.notifyDataSetChanged();

                        recyclerViewtv.setAdapter(tvAdapter);

                        Linearlayoutmanager.scrollToPositionWithOffset(currentPosition - 3, 0);

                        tvAdapter.SetOnItemClickListener(new Adapter_for_catgories.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {


                               /* Intent i = new Intent(context, Playseries.class);
                                i.putExtra("avatar", arraytvshows.get(position).Cattvimage());
                                i.putExtra("linkurl", arraytvshows.get(position).Epilink());
                                context.startActivity(i);*/


                                 String aa=arraytvshows.get(position).Epilink();
                                Intent intent = new Intent(getActivity(), Playseries.class);
                                intent.putExtra("linkurl",aa);
                                startActivity(intent);

                            }
                        });


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

    @Override
    public void onResume() {
        super.onResume();

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
        LIMIT_START_KEY = 0;
    }
}
