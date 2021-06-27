package com.sommelier.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sommelier.fragment.GalleryFragment;
import com.sommelier.fragment.GrapesFragment;
import com.sommelier.R;
import com.sommelier.fragment.RegionFragment;
import com.sommelier.fragment.SettingsFragment;
import com.sommelier.fragment.SortableFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView navigation;
    private Object savedInstanceState;
    int onStartCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            showFragment(GrapesFragment.TAG);
        }

        animationStartActivity();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_grapes:
                showFragment(GrapesFragment.TAG);
                return true;
            case R.id.navigation_sortable:
                showFragment(SortableFragment.TAG);
                return true;
            case R.id.navigation_region:
                showFragment(RegionFragment.TAG);
                return true;
            case R.id.navigation_gallery:
                showFragment(GalleryFragment.TAG);
                return true;
            case R.id.navigation_settings:
                showFragment(SettingsFragment.TAG);
                return true;
        }

        return false;
    }

    private boolean showFragment(@NonNull String tag) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            switch (tag) {
                case GrapesFragment.TAG: {
                    fragment = new GrapesFragment();
                    break;
                }
                case SortableFragment.TAG: {
                    fragment = new SortableFragment();
                    break;
                }
                case RegionFragment.TAG: {
                    fragment = new RegionFragment();
                    break;
                }
                case GalleryFragment.TAG: {
                    fragment = new GalleryFragment();
                    break;
                }
                case SettingsFragment.TAG: {
                    fragment = new SettingsFragment();
                    break;
                }
                default: {
                    fragment = new GrapesFragment();
                    break;
                }
            }
        }

        return loadFragment(fragment);
    }

    /**
     * загрузка фрагмента в FrameLayout
     *
     * @param fragment
     */
    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                    .replace(R.id.frame_container, fragment)
                    .commit();

            return true;
        }

        return false;
    }

    private void animationStartActivity() {
        onStartCount = 1;
        if (savedInstanceState == null) {  // 1st time
            this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } else { // уже создана, поэтому обратная анимация
            onStartCount = 2;
        }
    }
}
