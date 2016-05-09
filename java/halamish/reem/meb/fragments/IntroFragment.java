package halamish.reem.meb.fragments;

import android.content.Context;
import android.graphics.Interpolator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import halamish.reem.meb.MainActivity;
import halamish.reem.meb.R;

/**
 * Created by Re'em on 5/7/2016.
 * the first thing to welcome the user
 */
public class IntroFragment extends Fragment {
    private static final long ANIM_DUR = 750;
    ImageButton mBtnCalc;
    EditText mEdtHobby;
    TextView mTvButtonTitle;
    public IntroFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View retval = inflater.inflate(R.layout.activity_main_fragment_intro, container, false);
        mBtnCalc = (ImageButton) retval.findViewById(R.id.btn_intro_calculate);
        mEdtHobby = (EditText) retval.findViewById(R.id.edt_intro_insert_name);
        mTvButtonTitle = (TextView) retval.findViewById(R.id.tv_intro_button_title);

        mEdtHobby.setOnKeyListener(new EditText.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.d("abd", "key pressed");
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    smoothMove();
                    return true;
                }
                return false;
            }
        });


        mBtnCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                smoothMove();
            }
        });

        return retval;
    }

    /**
     * close the leyboard
     * spin the button
     * search imdb (will close the fragment when found)
     */
    private void smoothMove() {
        String workAt = mEdtHobby.getText().toString();
        if (!validate(workAt))
            return;
        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEdtHobby.getWindowToken(), 0);

        RotateAnimation spin = new RotateAnimation(
                0,
                360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        spin.setDuration(ANIM_DUR);
        spin.setRepeatCount(Animation.INFINITE);
        spin.setInterpolator(new LinearInterpolator());
        mBtnCalc.startAnimation(spin);

        AlphaAnimation disappear = new AlphaAnimation(1f, 0f);
        disappear.setDuration(ANIM_DUR);
        disappear.setFillAfter(true);
        mTvButtonTitle.startAnimation(disappear);

        startSearchingTheInternet(workAt);
    }

    private boolean validate(String workAt) {
        if (workAt.length() == 0) {
            Toast.makeText(getContext(), "Please insert a hobby.\n(e.g. ''baking cakes'')", Toast.LENGTH_SHORT).show();
            return false;
        }

        String regx = "^[\\p{L}' ]+$";
        Pattern pattern = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(workAt);
        if (!matcher.find()) {
            Toast.makeText(getContext(), "Please use letters and spaces solely", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void startSearchingTheInternet(String workAt) {
        ((MainActivity) getActivity()).findMovies(workAt);
    }

    public void makeButtonVisibleAgain() {
        mBtnCalc.setAnimation(null);
        mTvButtonTitle.setAnimation(null);
    }

}
