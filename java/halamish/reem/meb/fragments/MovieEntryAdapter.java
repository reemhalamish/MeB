package halamish.reem.meb.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import halamish.reem.meb.Utils;
import halamish.reem.meb.data.BitmapContainer;
import halamish.reem.meb.data.MovieEntry;
import halamish.reem.meb.R;

/**
 * Created by Re'em on 5/7/2016.
 *
 * adapter for the MovieEntries
 */
public class MovieEntryAdapter extends ArrayAdapter<MovieEntry> {
    private static final String TAG = "MovieAdapter";
    List<MovieEntry> mEntries;
    LayoutInflater mInflater;
    Map<View, MovieEntry> mCurActiveViews;
    Map<MovieEntry, View> mCurActiveEntries;
    FragmentActivity mActivity;


    public MovieEntryAdapter(FragmentActivity context, int resource, List<MovieEntry> objects) {
        super(context, resource, objects);
        mEntries = objects;
        mInflater = LayoutInflater.from(getContext());
        mCurActiveViews = new HashMap<>();
        mCurActiveEntries = new HashMap<>();
        mActivity = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view  = mInflater.inflate(R.layout.row_movie_entry, parent, false);
        } else {
            view = convertView;
        }
        MovieEntry entry = mEntries.get(position);
        final String url = "http://www.imdb.com/title/" + entry.getTt() + "/";

        TextView tvTitle = (TextView) view.findViewById(R.id.tv_rowmovie_title);
        tvTitle.setPaintFlags(tvTitle.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvTitle.setText(entry.getTitle());
        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                getContext().startActivity(i);
            }
        });

        // take care of the stars
        if (position % 2 > 0) {
            tvTitle.setTextColor(ContextCompat.getColor(getContext(), R.color.hyperlink1));
        } else {
            tvTitle.setTextColor(ContextCompat.getColor(getContext(), R.color.hyperlink2));
        }

        // handle the poster
        ImageView ivPoster = (ImageView) view.findViewById(R.id.iv_rowmovie_poster);
        if (!BitmapContainer.getInstance().getMap().containsKey(entry)) { // still loading
            ivPoster.setImageResource(android.R.drawable.alert_light_frame);
            ivPoster.setOnClickListener(null);
            ivPoster.setAlpha(1f);
        }
        Bitmap poster = BitmapContainer.getInstance().getMap().get(entry);
        if (poster != null) {
            ivPoster.setImageBitmap(poster);
            setListenerOnPoster(ivPoster, entry);
            ivPoster.setAlpha(1f);
        } else {
            ivPoster.setImageResource(R.drawable.no_img_avail);
            ivPoster.setOnClickListener(null);
            ivPoster.setAlpha(0.5f);
        }


        mCurActiveViews.put(view, entry);
        mCurActiveEntries.put(entry, view);
        return view;
    }

    public void updateView(final MovieEntry entry, final Bitmap bitmap) {
        Log.d(TAG, "updating entry: " + entry.getTt());
        View curView = mCurActiveEntries.get(entry);
        if (curView == null || ! mCurActiveViews.get(curView).equals(entry))
            return;

        final ImageView ivPoster = ((ImageView) curView.findViewById(R.id.iv_rowmovie_poster));
        final Animation anim_out = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out);
        final Animation anim_in  = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in);
        anim_out.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                ivPoster.startAnimation(anim_in);
                ivPoster.setImageBitmap(bitmap);
                ivPoster.setAlpha(1f);
                setListenerOnPoster(ivPoster, entry);
            }
        });
        ivPoster.startAnimation(anim_out);


    }

    private void setListenerOnPoster(ImageView ivPoster, final MovieEntry entry) {
        final Bitmap sentBitmap;
        ivPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageFullScreenFragment frag = new ImageFullScreenFragment();
                Bundle args = new Bundle();
                args.putParcelable(ImageFullScreenFragment.FULLSCREEN_IMAGE_TAG, entry);
                frag.setArguments(args);

                Utils.addFragmentToBackStackWithFadeEffect(
                        mActivity.getSupportFragmentManager(),
                        R.id.frm_fragment,
                        frag
                );
            }
        });
    }

}
