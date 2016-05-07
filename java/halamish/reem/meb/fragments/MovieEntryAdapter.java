package halamish.reem.meb.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import halamish.reem.meb.data.MovieEntry;
import halamish.reem.meb.R;

/**
 * Created by Re'em on 5/7/2016.
 *
 * adapter for the MovieEntries
 */
public class MovieEntryAdapter extends ArrayAdapter<MovieEntry> {
    List<MovieEntry> mEntries;
    LayoutInflater mInflater;
    public MovieEntryAdapter(Context context, int resource, List<MovieEntry> objects) {
        super(context, resource, objects);
        mEntries = objects;
        mInflater = LayoutInflater.from(getContext());
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
//            view.findViewById(R.id.iv_rowmovie_poster_left).setVisibility(View.GONE);
//            view.findViewById(R.id.iv_rowmovie_poster_right).setVisibility(View.VISIBLE);
            // TODO delete here and at xml if not needed
        } else {
//            view.findViewById(R.id.iv_rowmovie_poster_left).setVisibility(View.VISIBLE);
//            view.findViewById(R.id.iv_rowmovie_poster_right).setVisibility(View.GONE);
            tvTitle.setTextColor(ContextCompat.getColor(getContext(), R.color.hyperlink2));
        }

        return view;
    }
}
