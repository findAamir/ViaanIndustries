package com.example.viaanmovielisting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import Adapter.MoviesListAdapter;
import classes.APIUrl;
import classes.MySingleton;
import classes.RecyclerItemClickListener;
import pojo.MoviesPojo;

public class MainActivity extends AppCompatActivity {

    private ArrayList<MoviesPojo> mRecyclerViewItems = new ArrayList<>();

    private MoviesListAdapter mAdapter;

    int REQUEST_TOTAL_ROW_COUNT = 0;

    ProgressBar progressbar;

    TextView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayoutManager mLayoutManager;
        final RecyclerView mRecyclerView;
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        final SharedPreferences.Editor editor = pref.edit();
        mAdapter = new MoviesListAdapter(getApplicationContext(), mRecyclerViewItems);
        mRecyclerView = findViewById(R.id.moviesRecylerView);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);

        progressbar = findViewById(R.id.progressbar);
        logout = findViewById(R.id.logout);
        if (isNetworkAvailable())
        {
            loadThisPageDataFromApi();
        }
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getApplicationContext(), MoviesDetails.class);
                        intent.putExtra("movieId",mRecyclerViewItems.get(position).getMovieId());
                        intent.putExtra("movieTitle",mRecyclerViewItems.get(position).getMovieTitle());
                        intent.putExtra("movieImage",mRecyclerViewItems.get(position).getMovieThumbnail());
                        intent.putExtra("movieOverView",mRecyclerViewItems.get(position).getMovieOverview());
                        intent.putExtra("movieLanguage",mRecyclerViewItems.get(position).getLanguage());
                        intent.putExtra("movieRelease",mRecyclerViewItems.get(position).getPopularity());
                        startActivity(intent);
                        finish();
                    }
                })
        );
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.remove("user_email"); // will delete key user_email
                editor.commit();
                startActivity(new Intent(MainActivity.this,Login.class));
                finish();
            }
        });

    }

    public void loadThisPageDataFromApi() {

        progressbar.setVisibility(View.VISIBLE);
        final APIUrl serverUrl = new APIUrl();
        serverUrl.url();
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, serverUrl.domain_url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        progressbar.setVisibility(View.GONE);
                        Log.d("data",response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray jsonArray = obj.getJSONArray("results");
                            if(jsonArray.length()==0)
                            {
                                Toast.makeText(MainActivity.this, "No Data Available", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                    REQUEST_TOTAL_ROW_COUNT = obj.getInt("total_results");
                                    MoviesPojo moviesData;
                                    ArrayList<MoviesPojo> newFetchedList = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        moviesData = new MoviesPojo();
                                        JSONObject json_obj = jsonArray.getJSONObject(i);
                                        moviesData.setMovieThumbnail("https://image.tmdb.org/t/p/w500/"+json_obj.getString("poster_path"));
                                        moviesData.setMovieTitle(json_obj.getString("title"));
                                        moviesData.setMovieId(json_obj.getString("id"));
                                        moviesData.setMovieOverview(json_obj.getString("overview"));
                                        moviesData.setLanguage(json_obj.getString("original_language"));
                                        moviesData.setPopularity(json_obj.getString("release_date"));

                                        newFetchedList.add(moviesData);
                                    }
                                    if (newFetchedList.size() > 0) {
                                        mAdapter.insertNewLoadedAdverts(newFetchedList);
                                    }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                alertUserAboutError("Network Error","Can't Connect to network please try again");
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {


            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        return true;
                    }  else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)){
                        return true;
                    }else{
                        alertUserAboutError(getString(R.string.error_title),getString(R.string.network_unavailable));
                    }
                }
            }

            else {

                try {
                    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                    if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                        return true;
                    }
                    else{
                        alertUserAboutError(getString(R.string.error_title),getString(R.string.network_unavailable));
                    }
                } catch (Exception e) {
                    Log.i("update_status", "" + e.getMessage());
                }
            }
        }
        return false;
    }

    private void alertUserAboutError(String title,String titleMessage) {

        AlertDialogFragment alertDialogFragment = new AlertDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("error_title",title);
        bundle.putString("error_message",titleMessage);
        alertDialogFragment.setArguments(bundle);
        alertDialogFragment.show(getSupportFragmentManager(),"error_dialog");
    }
}
