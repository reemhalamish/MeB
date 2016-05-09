package halamish.reem.meb;

import android.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by Re'em on 5/9/2016.
 */
public class Utils {
    public static void addFragmentToBackStackWithFadeEffect(FragmentManager manager, int resId, Fragment newFragment) {
        android.support.v4.app.FragmentTransaction tr = manager.beginTransaction();
        tr.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
        tr.addToBackStack(null);
        tr.replace(resId, newFragment);
        tr.commit();

    }
}
