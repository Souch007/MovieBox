package category;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bhargavms.dotloader.DotLoader;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Adapters.Adapter_for_catgories;
import Others.Categoriesimages;
import Others.JsonParser;
import Others.SingletonModel;
import app.moviebox.niku.moviebox.MainActivity;
import app.moviebox.niku.moviebox.R;


public class Fragmentcategory extends Fragment {
    private static final String URL_GETMOVIES = "getmovie";
    Categoriesimages obj;
    Adapter_for_catgories CategoryAdapter;
    String cat_id, message;
    ArrayList<Categoriesimages> arrayprofessionals = new ArrayList<>();
    JsonParser jsonParser = new JsonParser();
    public static int LIMIT_START_KEY = 0;
    GridLayoutManager gridLayoutManager;
    Context context;
    public Fragmentcategory instance;
    CoordinatorLayout clMain;
    DotLoader dotLoader;
    TextView norecshow;
    private static final String SUCCESS = "response";
    int
            success,
            pastVisiblesItems,
            visibleItemCount,
            totalItemCount,
            previousTotal = 0;

    String BASE_URL, str_etSearch;
    RecyclerView recyclerView;
    public boolean IS_SEARCHING = false;
    boolean
            load = true,
            isViewShown = true, isVisibleToUser = false, isFragmentLoaded = false;
    ImageView img;


    public Fragmentcategory() {

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
                IS_SEARCHING = false;
                refresh();
                new getCategories().execute();
            } else {

                isViewShown = false;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragmentcategory, container, false);
        instance = this;
        cat_id = SingletonModel.getSingletonModel().drawercatid;

        recyclerView = (RecyclerView) view.findViewById(R.id.rvcategory);
        norecshow = (TextView) view.findViewById(R.id.idshow);
        norecshow.setVisibility(View.GONE);
        BASE_URL = getActivity().getString(R.string.base_url);
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);
        clMain = (CoordinatorLayout) view.findViewById(R.id.main_layout);
        dotLoader = (DotLoader) view.findViewById(R.id.dot_loader);
        context = getActivity();
        //View incHeader = getActivity().findViewById(R.id.relativeLayout);

        /*DrawerActivity.instance.searchViewLayout.setSearchListener(new SearchViewLayout.SearchListener() {
            @Override
            public void onFinished(String searchKeyword) {
                str_etSearch = searchKeyword.toString();
                IS_SEARCHING = false;
                refresh();
                new getCategories().execute();

            }
        });*/

        /*DrawerActivity.instance.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                str_etSearch=query;
                IS_SEARCHING = true;
                refresh();
                new getCategories().execute();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String i=String.valueOf(newText.length());
                if (i==String.valueOf(0))
                {
                    IS_SEARCHING = false;
                    refresh();
                    new getCategories().execute();
                }
                //  Toast.makeText(getActivity(), newText, Toast.LENGTH_LONG).show();
                return false;
            }
        });*/
        // etSearch = (EditText) incHeader.findViewById(R.id.etSearch);


       /*
*/

        if (!isViewShown) {
            refresh();
            new getCategories().execute();
        }

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    visibleItemCount = gridLayoutManager.getChildCount();
                    totalItemCount = gridLayoutManager.getItemCount();
                    pastVisiblesItems = gridLayoutManager.findFirstVisibleItemPosition();

                    if (load) {
                        if (totalItemCount > previousTotal) {
                            load = false;
                            previousTotal = totalItemCount;
                        }
                    }
                    if (!load && (totalItemCount - visibleItemCount)
                            <= (pastVisiblesItems + visibleItemCount)) {
                        new getCategories().execute();
                        load = true;
                    }
                }
            }
        });



       /* etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int count) {

                if (isVisibleToUser) {

                    if (count >= 3) {
                        IS_SEARCHING = true;
                        refresh();
                        new getCategories().execute();
                    } else if (count == 0) {
                        IS_SEARCHING = false;
                        norecshow.setVisibility(View.GONE);
                        refresh();
                        new getCategories().execute();
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
*/
        return view;
    }

    public void startSearching(String query) {
        str_etSearch = query;
        IS_SEARCHING = true;
        refresh();
        new getCategories().execute();

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


    private class getCategories extends AsyncTask<String, String, JSONObject>

    {
        protected void onPreExecute() {
           /* dotLoader.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dotLoader.setNumberOfDots(4);
                }
            }, 4000);
*/
//            str_etSearch = etSearch.getText().toString();

            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... params) {

            List<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("start", String.valueOf(LIMIT_START_KEY)));
            if (IS_SEARCHING == true) {
                nameValuePairs.add(new BasicNameValuePair(" title", str_etSearch));

            }
            if (cat_id != "-1") {

                nameValuePairs.add(new BasicNameValuePair("category_id", cat_id));

            }


            JSONObject jsonObject = jsonParser.makeHttpRequest(BASE_URL + URL_GETMOVIES, "POST", nameValuePairs);
            return jsonObject;

        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {

            dotLoader.resetColors();
            dotLoader.setVisibility(View.GONE);
//            loading1.dismiss();
            // progressBar.setVisibility(View.GONE);
            if (jsonObject.isNull("edges") == false) {
                Toast.makeText(context, "Someting went wrong please check your internet connection", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    success = jsonObject.getInt(SUCCESS);
                    message = jsonObject.getString("description");
                    //  Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (success == 1) {
                    try {
                        if (LIMIT_START_KEY == 0) {
                            arrayprofessionals.clear();
//                        arrayLastName.clear();
//                        arrayFriendId.clear();
//                        arrayStatus.clear();
                        }

                        LIMIT_START_KEY = Integer.valueOf(jsonObject.getString("start"));
                        JSONArray jarray = jsonObject.getJSONArray("data");

                        for (int n = 0; n <= jarray.length() - 1; n++) {
                            obj = new Categoriesimages();
                            JSONObject jop = jarray.getJSONObject(n);
                            obj.setCatimgae(jop.getString(("avatar")));
                            obj.setMovieid(jop.getString(("id")));
                            arrayprofessionals.add(obj);

                        }

                        CategoryAdapter = new Adapter_for_catgories(getActivity(), arrayprofessionals);
                        int currentPosition = gridLayoutManager.findLastVisibleItemPosition();
                        CategoryAdapter.notifyDataSetChanged();

                        recyclerView.setAdapter(CategoryAdapter);

                        gridLayoutManager.scrollToPositionWithOffset(currentPosition - 3, 0);

                        CategoryAdapter.SetOnItemClickListener(new Adapter_for_catgories.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {


                                Intent i = new Intent(context, MainActivity.class);
                                i.putExtra("Movieid", arrayprofessionals.get(position).Movieid());
                                context.startActivity(i);

                            }
                        });


                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                } else {


                    Snackbar snackbar = Snackbar.make(clMain, message, Snackbar.LENGTH_LONG).setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            v.setVisibility(View.GONE);
                        }
                    });
                    snackbar.show();
                }

            }
        }

    }

}
