package halamish.reem.meb.data;

import android.os.Parcel;
import android.os.Parcelable;

import org.apache.commons.lang3.StringEscapeUtils;

/**
 * Created by Re'em on 5/7/2016.
 *
 * contains a lot of generated code in order to be parcelled and so being kept safe on screen rotation
 */
public class MovieEntry implements Parcelable {
    String title, tt;
    public MovieEntry(String title, String tt) {
        this.title = StringEscapeUtils.unescapeHtml3(title);
        this.tt = tt;
    }
    public MovieEntry(Parcel in) {
        title = in.readString();
        tt = in.readString();
    }

    public String getTitle() {
        return title;
    }

    public String getTt() {
        return tt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(tt);
    }

    public static final Creator<MovieEntry> CREATOR = new Creator<MovieEntry>() {
        @Override
        public MovieEntry createFromParcel(Parcel in) {
            return new MovieEntry(in);
        }

        @Override
        public MovieEntry[] newArray(int size) {
            return new MovieEntry[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (o.getClass() != MovieEntry.class)
            return false;
        MovieEntry other = (MovieEntry) o;
        return other.title.equals(title) && other.tt.equals(tt);
    }
}
