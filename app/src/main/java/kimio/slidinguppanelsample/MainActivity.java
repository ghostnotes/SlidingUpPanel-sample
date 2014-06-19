package kimio.slidinguppanelsample;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.nineoldandroids.view.animation.AnimatorProxy;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;


public class MainActivity extends Activity {
    private static final String TAG = "TAG_MAIN_ACTIVITY";
    private static final String KEY_IS_SLIDING_PANEL_EXPANDED = "KEY_IS_SLIDING_PANEL_EXPANDED";

    private SlidingUpPanelLayout mSlidingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_main);

        mSlidingLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mSlidingLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View view, float slideOffset) {
                Log.d(TAG, "slide offset: " + slideOffset);
                setActionBarTranslation(mSlidingLayout.getCurrentParalaxOffset());
            }

            @Override
            public void onPanelCollapsed(View view) {
            }

            @Override
            public void onPanelExpanded(View view) {
            }

            @Override
            public void onPanelAnchored(View view) {
            }
        });


        boolean actionBarHidden =
            savedInstanceState != null && savedInstanceState.getBoolean(KEY_IS_SLIDING_PANEL_EXPANDED, false);
        if (actionBarHidden) {
            int actionBarHeight = getActionBarHeight();
            setActionBarTranslation(-actionBarHeight);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_IS_SLIDING_PANEL_EXPANDED, mSlidingLayout.isExpanded());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mSlidingLayout != null
            && mSlidingLayout.isExpanded() || mSlidingLayout.isAnchored()
        ) {
            mSlidingLayout.collapsePane();
        } else {
            super.onBackPressed();
        }
    }

    private int getActionBarHeight(){
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();

        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

    public void setActionBarTranslation(float y) {
        int actionBarHeight = getActionBarHeight();

        ViewGroup content = ((ViewGroup) findViewById(android.R.id.content).getParent());

        int children = content.getChildCount();
        for (int i = 0; i < children; i++) {
            View child = content.getChildAt(i);
            if (child.getId() != android.R.id.content) {
                if (y <= -actionBarHeight) {
                    child.setVisibility(View.GONE);
                } else {
                    child.setVisibility(View.VISIBLE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        child.setTranslationY(y);
                    } else {
                        AnimatorProxy.wrap(child).setTranslationY(y);
                    }
                }
            }
        }
    }
}
