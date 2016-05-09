package halamish.reem.meb.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import halamish.reem.meb.R;
import halamish.reem.meb.data.BitmapContainer;
import halamish.reem.meb.data.MovieEntry;

/**
 * Created by Re'em on 5/9/2016.
 */
public class ImageFullScreenFragment extends Fragment {
    public static final String FULLSCREEN_IMAGE_TAG = "fullscreen_bitmap";
    MovieEntry mEntry;
    Bitmap mFullscreenImage;
    public ImageFullScreenFragment() {}

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
        mEntry = args.getParcelable(FULLSCREEN_IMAGE_TAG);
        mFullscreenImage = BitmapContainer.getInstance().getMap().get(mEntry);
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null (which
     * is the default implementation).  This will be called between
     * {@link #onCreate(Bundle)} and {@link #onActivityCreated(Bundle)}.
     * <p/>
     * <p>If you return a View from here, you will later be called in
     * {@link #onDestroyView} when the view is being released.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mEntry = savedInstanceState.getParcelable(FULLSCREEN_IMAGE_TAG);
            mFullscreenImage = BitmapContainer.getInstance().getMap().get(mEntry);
        }
        if (mFullscreenImage == null) {
            mFullscreenImage = BitmapFactory.decodeResource(getResources(), R.drawable.no_img_avail);
        }

        View retval = inflater.inflate(R.layout.activity_main_fragment_fullscreen_image, container, false);
        ImageView ivMainImage = (ImageView) retval.findViewById(R.id.iv_fullscreen_main_image);
        ivMainImage.setImageBitmap(mFullscreenImage);
        ImageButton ibExit = (ImageButton) retval.findViewById(R.id.ib_fullscreen_exit);
        ibExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });

        return retval;
    }

    /**
     * Called to ask the fragment to save its current dynamic state, so it
     * can later be reconstructed in a new instance of its process is
     * restarted.  If a new instance of the fragment later needs to be
     * created, the data you place in the Bundle here will be available
     * in the Bundle given to {@link #onCreate(Bundle)},
     * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}, and
     * {@link #onActivityCreated(Bundle)}.
     * <p/>
     * <p>This corresponds to {@link Activity#onSaveInstanceState(Bundle)
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
        outState.putParcelable(FULLSCREEN_IMAGE_TAG, mEntry);
    }
}
