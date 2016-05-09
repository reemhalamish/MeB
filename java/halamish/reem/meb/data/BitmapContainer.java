package halamish.reem.meb.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Re'em on 5/9/2016.
 *
 * used to store the bitmaps downloaded from {link: MovieListFragment.GetAllPosterSrcsTask}
 */
public class BitmapContainer {
    private static BitmapContainer instance;
    private Map<MovieEntry, Bitmap> map;
    private BitmapContainer() {map = new HashMap<>();}
    public static synchronized BitmapContainer getInstance() {
        if (instance == null)
            instance = new BitmapContainer();
        return instance;
    }
    public void updateMap(Map<MovieEntry, Bitmap> map) {
        this.map.putAll(map);
    }
    public Map<MovieEntry, Bitmap> getMap() {return map;}

}
