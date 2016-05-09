package halamish.reem.meb.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import halamish.reem.meb.R;
import halamish.reem.meb.data.BitmapContainer;
import halamish.reem.meb.data.MovieEntry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Created by Re'em on 5/7/2016.
 * the second fragment
 */
public class MovieListFragment extends ListFragment {
    public static final String MOVIES_SAVED_TAG = "all_saved_movies";
    public static final String DOWNLOAD_POSTER_STARTED_TAG = "download_started";
    private static final String TAG = "movie fragment";
    ArrayList<MovieEntry> mAllMovies;
    MovieEntryAdapter mAdapter;



    public MovieListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View retval = inflater.inflate(R.layout.activity_main_fragment_list, container, false);
        return retval;
    }


    /**
     * Supply the construction arguments for this fragment.  This can only
     * be called before the fragment has been attached to its activity; that
     * is, you should call it immediately after constructing the fragment.  The
     * arguments supplied here will be retained across fragment destroy and
     * creation.
     *
     * @param args
     */
    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        mAllMovies = args.getParcelableArrayList(MOVIES_SAVED_TAG);
    }

    /**
     * Called to do initial creation of a fragment.  This is called after
     * onAttach(Activity) and before
     * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * <p>
     * <p>Note that this can be called while the fragment's activity is
     * still in the process of being created.  As such, you can not rely
     * on things like the activity's content view hierarchy being initialized
     * at this point.  If you want to do work once the activity itself is
     * created, see {@link #onActivityCreated(Bundle)}.
     * <p>
     * <p>Any restored child fragments will be created before the base
     * <code>Fragment.onCreate</code> method returns.</p>
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(MOVIES_SAVED_TAG))
                mAllMovies = savedInstanceState.getParcelableArrayList(MOVIES_SAVED_TAG);
            if (!savedInstanceState.getBoolean(DOWNLOAD_POSTER_STARTED_TAG, false))
                new GetAllPosterSrcsTask().execute(mAllMovies);
        } else { // first time, mAllMovies already initiated from setArguments()
            new GetAllPosterSrcsTask().execute(mAllMovies);
        }

        mAdapter = new MovieEntryAdapter(
                getActivity(),
                R.layout.activity_main_fragment_list,
                mAllMovies
                );
        setListAdapter(mAdapter);

    }

    /**
     * Called to ask the fragment to save its current dynamic state, so it
     * can later be reconstructed in a new instance of its process is
     * restarted.  If a new instance of the fragment later needs to be
     * created, the data you place in the Bundle here will be available
     * in the Bundle given to {@link #onCreate(Bundle)},
     * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}, and
     * {@link #onActivityCreated(Bundle)}.
     * <p>
     * <p>This corresponds to Activity#onSaveInstanceState(Bundle)
     * Activity.onSaveInstanceState(Bundle)} and most of the discussion there
     * applies here as well.  Note however: <em>this method may be called
     * at any time before {@link #onDestroy()}</em>.  There are many situations
     * where a fragment may be mostly torn down (such as when placed on the
     * back stack with no UI showing), but its state will not be saved until
     * its owning activity actually needs to save its state.
     *
     * @param outState Bundle in which to place your saved state.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MOVIES_SAVED_TAG, mAllMovies);
        outState.putBoolean(DOWNLOAD_POSTER_STARTED_TAG, true); // should download only once
    }

    /**
     * Called when the Fragment is no longer started.  This is generally
     * tied to Activity.onStop of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()");
        mAdapter.notifyDataSetInvalidated();
    }

    class GetAllPosterSrcsTask extends AsyncTask<List<MovieEntry>, MovieEntryWithBitmap, Map<MovieEntry, Bitmap>> {

        @Override
        protected Map<MovieEntry, Bitmap> doInBackground(List<MovieEntry>... lists) {
            Map<MovieEntry, Bitmap> retval = new HashMap<>();
            for (MovieEntry entry : lists[0])
                try {
                    String url = "http://www.imdb.com/title/" + entry.getTt() + "/";
                    String query = "div.poster a img";
                    Document doc = Jsoup.connect(url).get();
                    Element image = doc.select(query).first();
                    if (image == null) {
                        retval.put(entry, null);
                        continue;
                    }
                    String src = image.attr("src");
                    Bitmap newPoster = BitmapFactory.decodeStream(new URL(src).openStream());
                    retval.put(entry, newPoster);
                    publishProgress(new MovieEntryWithBitmap(entry, newPoster));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            return retval;
        }

        @Override
        protected void onProgressUpdate(MovieEntryWithBitmap... values) {
            Log.d(TAG, "updating Bitmap for entry " + values[0].movieEntry.getTt());
            mAdapter.updateView(values[0].movieEntry, values[0].bitmap);
//            mAdapter.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(Map<MovieEntry, Bitmap> movieEntryBitmapMap) {
            BitmapContainer.getInstance().updateMap(movieEntryBitmapMap);
            mAdapter.notifyDataSetChanged();
        }

        @Override
        protected void onCancelled() {
        }

        @Override
        protected void onCancelled(Map<MovieEntry, Bitmap> movieEntryBitmapMap) {
        }
    }


    private class MovieEntryWithBitmap {
        MovieEntry movieEntry;
        Bitmap bitmap;
        MovieEntryWithBitmap(MovieEntry m, Bitmap b) {
            movieEntry = m;
            bitmap = b;
        }
    }
}
