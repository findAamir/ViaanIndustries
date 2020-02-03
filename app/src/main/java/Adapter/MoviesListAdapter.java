package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.viaanmovielisting.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import pojo.MoviesPojo;

public class MoviesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;

    private ArrayList<MoviesPojo> mItemsArrayList;

    public MoviesListAdapter(Context context, ArrayList<MoviesPojo> itemsList) {

            mContext = context;
            mItemsArrayList = itemsList;

    }
    @Override
    public int getItemCount() {
        return mItemsArrayList.size();
    }

    public int getAdapterStandartItemsCount() {
        int currenBildirisCount = 0;
        for (int i = 0; i < mItemsArrayList.size(); i++) {
                currenBildirisCount++;
        }
        return currenBildirisCount;
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_movies_list, parent, false);
            return new StandartAdvertHolder(view);

    }
    private class StandartAdvertHolder extends RecyclerView.ViewHolder {

        private TextView movieTitle,movieId,movieOverview,movieLanguage,movieRelease;
        private ImageView moviePoster;


        StandartAdvertHolder(View itemView) {
            super(itemView);
            movieTitle=itemView.findViewById(R.id.movieTitle);
            moviePoster=itemView.findViewById(R.id.moviePoster);
            movieId=itemView.findViewById(R.id.moviesId);
            movieOverview = itemView.findViewById(R.id.movieOverview);
            movieLanguage = itemView.findViewById(R.id.language);
            movieRelease = itemView.findViewById(R.id.releasedate);
        }

        void bind(MoviesPojo rowMainCategory) {
            RequestOptions options = new RequestOptions();
            options.centerCrop();
            options.dontAnimate();
            Glide.with(mContext)
                    .load(rowMainCategory.getMovieThumbnail())
                    .thumbnail(0.1f)
                    .apply(options)
                    .into(moviePoster);

            movieTitle.setText(rowMainCategory.getMovieTitle());
            movieId.setText(rowMainCategory.getMovieId());
            movieOverview.setText(rowMainCategory.getMovieOverview());
            movieLanguage.setText(rowMainCategory.getLanguage());
            movieRelease.setText(rowMainCategory.getPopularity());
        }
    }

    @Override
    public void onBindViewHolder(@NotNull RecyclerView.ViewHolder holder, int position) {
        MoviesPojo mainCategoryModel = mItemsArrayList.get(position);

        ((StandartAdvertHolder) holder).bind(mainCategoryModel);
    }
    public void insertNewLoadedAdverts(ArrayList<MoviesPojo> newMainCategoryList) {
        int newListCount = newMainCategoryList.size();

        int nobatdakyFromNewList = 0;
        int startOfNextIndex = mItemsArrayList.size();


        int loopSize = newListCount + mItemsArrayList.size();

        for (int i = startOfNextIndex; i < loopSize; i++) {

            if (nobatdakyFromNewList < newMainCategoryList.size()) {
                mItemsArrayList.add(i, newMainCategoryList.get(nobatdakyFromNewList));
                nobatdakyFromNewList++;
            }
        }
        notifyDataSetChanged();
    }
}
