package halamish.reem.meb;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import halamish.reem.meb.data.MovieEntry;
import halamish.reem.meb.fragments.IntroFragment;
import halamish.reem.meb.fragments.MovieListFragment;

public class MainActivity extends FragmentActivity {
    private static final String INTRO_FRAGMENT_TAG = "intro_fragment";

    IntroFragment mIntroFrag;
    MovieListFragment mListFrag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mIntroFrag = new IntroFragment();
        if (savedInstanceState == null) { // first time here
            getSupportFragmentManager().beginTransaction().add(R.id.frm_fragment, mIntroFrag).commit();
        }
    }


    public void findMovies(String workAt) {
        new FindAllMoviesTask().execute(workAt);
    }

    public class FindAllMoviesTask extends AsyncTask<String, Void, ArrayList<MovieEntry>> {
        String error_msg; // will be initated if there is a need

        ArrayList<MovieEntry> getAllEntriesFromJson(JSONArray items) {
            ArrayList<MovieEntry> retval = new ArrayList<>();
            for (int i = 0; i < items.size(); i++) {
                JSONObject object = (JSONObject) items.get(i);
                retval.add(new MovieEntry(object.get("title").toString(),
                                          object.get("id").toString())
                          );
            }
            return retval;
        }

        /**
         * assuming the input is validated
         */
        @Override
        protected ArrayList<MovieEntry> doInBackground(String... strings) {
            String workAt = strings[0];
            workAt = workAt.replace(' ', '+');
            workAt = "http://www.imdb.com/xml/find?json=1&tt=1&nm=on&q=" + workAt;
            URLConnection connection;
            try {
                connection = new URL(workAt).openConnection();
                connection.setRequestProperty("Accept-Charset", "UTF-8");
                InputStream response = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(response));
                Log.d(INTRO_FRAGMENT_TAG, workAt);
                JSONObject object = (JSONObject) new JSONParser().parse(reader);
                reader.close();
                if (object.containsKey("title_exact"))
                    return getAllEntriesFromJson((JSONArray) object.get("title_exact"));
                if (object.containsKey("title_substring"))
                    return getAllEntriesFromJson((JSONArray) object.get("title_substring"));
                if (object.containsKey("title_approx"))
                    return getAllEntriesFromJson((JSONArray) object.get("title_approx"));

                error_msg = "Couldn't find a relevant movie, Would you try another hobby?";
                cancel(true);
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                error_msg = "Connection failed. Please check your internet connection and try again";
                cancel(true);
                return null;
            } catch (org.json.simple.parser.ParseException e) {
                e.printStackTrace();
                error_msg = "Error connecting to IMDB server. Please check your internet connection and try again";
                cancel(true);
                return null;
            }

        }

        @Override
        protected void onCancelled(ArrayList<MovieEntry> movieEntries) {
            super.onCancelled(movieEntries);
            mIntroFrag.makeButtonVisibleAgain();
            Toast.makeText(MainActivity.this, error_msg, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(ArrayList<MovieEntry> movieEntries) {
            super.onPostExecute(movieEntries);
            mListFrag = new MovieListFragment();
            Bundle intoFrag = new Bundle();
            intoFrag.putParcelableArrayList(MovieListFragment.MOVIES_SAVED_TAG, movieEntries);
            mListFrag.setArguments(intoFrag);
            FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
            trans.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            trans.addToBackStack(null);
            trans.replace(R.id.frm_fragment, mListFrag);
            trans.commit();
        }
    }

}
