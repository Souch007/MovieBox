package app.moviebox.niku.moviebox;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import category.FragmentTvshows;
import category.Fragmentcategory;

public class MovieParentTab extends Fragment {

    private ViewPager mViewPager;
    int cat_id;
    private ImageView img;
    TabLayout tabLayout;
    private int[] tabIcons = {
            R.drawable.movietab,
            R.drawable.moviewhitetab,
            R.drawable.iptvicon,


    };
    public MovieParentTab() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            Activity a = getActivity();
            if(a != null) a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        }
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {




        View view=inflater.inflate(R.layout.activity_movie_parent_tab, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
      //  Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbar);
       // View incHeader = view.findViewById(R.id.relativeLayout);
        mViewPager = (ViewPager)view. findViewById(R.id.container);
        setupViewPager(mViewPager);


        tabLayout = (TabLayout)view. findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        setupTabIcons();

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

       // View incHeader = getActivity().findViewById(R.id.relativeLayout);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

             //   selectedPagePosition = position;
                switch (position) {
                    case 0:
                        DrawerActivity.instance.myHelper.currentFragmentTag ="1";
                        DrawerActivity.instance.searchViewLayout.setHint("Search Movie");
                        break;
                    case 1:
                        DrawerActivity.instance.myHelper.currentFragmentTag ="2";
                        DrawerActivity.instance.searchViewLayout.setHint("Search Tv shows");
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


//        getActivity().setSupportActionBar(toolbar);
        return view;
    }



       /* mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                selectedPagePosition = position;
                etSearch.getText().clear();

                switch (position) {
                    case 0:
                        etSearch.setHint("Search Chat");
                        etSearch.setEnabled(true);
                        fabCreate.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        etSearch.setHint("Search Groups");
                        etSearch.setEnabled(true);
                        fabCreate.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        etSearch.setHint("Search Friends");
                        etSearch.setEnabled(true);
                        fabCreate.setVisibility(View.GONE);
                        break;
                    *//*case 3:
                        etSearch.setHint("Bizknights");
                        etSearch.setEnabled(false);
                        fabCreate.setVisibility((post_on_wall.equalsIgnoreCase("1")) ? View.VISIBLE : View.GONE);
                        break;*//*
                    default:
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/


        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }*/

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(new Fragmentcategory(), "");
        adapter.addFragment(new FragmentTvshows(), " ");
       // adapter.addFragment(new List_for_all_friends(), " ");
        // adapter.addFragment(new List_For_Wall(), " ");
        viewPager.setAdapter(adapter);

    }


    private void setupTabIcons() {
        tabLayout.getTabAt(0).setText("Movie");
        tabLayout.getTabAt(1).setText("Tv Show");
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
       // tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        // tabLayout.getTabAt(3).setIcon(tabIcons[3]);
    }

    public class ViewPagerAdapter extends FragmentStatePagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
    }

   /* public static  class Fragmentcategory extends Fragment {
        private static final String URL_GETMOVIES = "getmovie";
        Categoriesimages obj;
        Adapter_for_catgories CategoryAdapter;

        ArrayList<Categoriesimages> arrayprofessionals = new ArrayList<>();
        JsonParser jsonParser = new JsonParser();
        public static int LIMIT_START_KEY = 0;
        GridLayoutManager gridLayoutManager;
        Context context;
        category.Fragmentcategory myfragment;
        private static final String SUCCESS = "response";
        int
                success,
                pastVisiblesItems,
                visibleItemCount,
                totalItemCount,
                previousTotal = 0;

        String BASE_URL;
        RecyclerView recyclerView;

        boolean
                load = true,
                isViewShown = true,  isVisibleToUser = false,  isFragmentLoaded = false;
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


            View view=inflater.inflate(R.layout.fragment_fragmentcategory, container, false);

            recyclerView = (RecyclerView) view.findViewById(R.id.rvcategory);
            BASE_URL = getActivity().getString(R.string.base_url);
            gridLayoutManager =  new GridLayoutManager(getActivity(), 2);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);
            context=getActivity();
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
            return view;
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

                // progressBar.setVisibility(View.VISIBLE);
//            loading1 = ProgressDialog.show(List_Taxi_For_Hire.this, "Loading Cities...", null, true, true);

                super.onPreExecute();
            }

            @Override
            protected JSONObject doInBackground(String... params) {

                List<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("start",  String.valueOf(LIMIT_START_KEY)));
               // nameValuePairs.add(new BasicNameValuePair("category_id",  String.valueOf(cat_id)));
                JSONObject jsonObject = jsonParser.makeHttpRequest(BASE_URL + URL_GETMOVIES, "POST", nameValuePairs);
                return jsonObject;

            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {


//            loading1.dismiss();
                // progressBar.setVisibility(View.GONE);
                if( jsonObject.isNull("edges") == false) {
                    Toast.makeText(context, "Someting went wrong please check your internet connection", Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        success = jsonObject.getInt(SUCCESS);
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
                                obj  = new Categoriesimages();
                                JSONObject jop = jarray.getJSONObject(n);
                                obj.setCatimgae(jop.getString(("avatar")));
                                obj.setMovieid(jop.getString(("id")));
                                arrayprofessionals.add(obj);

                            }

                            CategoryAdapter = new Adapter_for_catgories(getActivity(),arrayprofessionals);
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
//                ima.setVisibility(View.VISIBLE);
//
//                ima.setBackgroundResource(R.drawable.nored);
//                Toast.makeText(getApplicationContext(), "No Data To Load", Toast.LENGTH_SHORT).show();

//                zerom = String.valueOf(model.getSelectedItem());
//                zeroc = String.valueOf(color.getSelectedItem());
                    }

                }}

        }

    }*/

}
