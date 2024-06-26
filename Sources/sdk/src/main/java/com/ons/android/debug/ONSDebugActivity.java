package com.ons.android.debug;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.ons.android.ONS;
import com.ons.android.R;
import com.ons.android.debug.fragment.IdentifierDebugFragment;
import com.ons.android.debug.fragment.LocalCampaignDebugFragment;
import com.ons.android.debug.fragment.LocalCampaignsDebugFragment;
import com.ons.android.debug.fragment.MainDebugFragment;
import com.ons.android.debug.fragment.UserDataDebugFragment;
import com.ons.android.di.providers.CampaignManagerProvider;

/**
 * Debug activity that display info from ONS SDK
 *
 */
public class ONSDebugActivity extends FragmentActivity implements OnMenuSelectedListener {

    public static final int MAIN_DEBUG_FRAGMENT = 0;
    public static final int IDENTIFIER_DEBUG_FRAGMENT = 1;
    public static final int USER_DATA_DEBUG_FRAGMENT = 2;
    public static final int LOCAL_CAMPAIGNS_DEBUG_FRAGMENT = 3;
    public static final int LOCAL_CAMPAIGN_DEBUG_FRAGMENT = 4;

    private Fragment[] fragments = new Fragment[5];

    private void switchFragment(int newIndex, boolean first, String campaignToken) {
        if (newIndex >= 0 && newIndex < fragments.length) {
            if (fragments[newIndex] == null) {
                switch (newIndex) {
                    case MAIN_DEBUG_FRAGMENT:
                        fragments[newIndex] = MainDebugFragment.newInstance();
                        break;
                    case IDENTIFIER_DEBUG_FRAGMENT:
                        fragments[newIndex] = IdentifierDebugFragment.newInstance();
                        break;
                    case USER_DATA_DEBUG_FRAGMENT:
                        fragments[newIndex] = UserDataDebugFragment.newInstance();
                        break;
                    case LOCAL_CAMPAIGNS_DEBUG_FRAGMENT:
                        fragments[newIndex] = LocalCampaignsDebugFragment.newInstance(CampaignManagerProvider.get());
                        break;
                    case LOCAL_CAMPAIGN_DEBUG_FRAGMENT:
                        fragments[newIndex] =
                            LocalCampaignDebugFragment.newInstance(campaignToken, CampaignManagerProvider.get());
                        break;
                }
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (first) {
                fragmentTransaction
                    .replace(R.id.com_onssdk_debug_fragment_container, fragments[newIndex])
                    .commitNow();
            } else {
                fragmentTransaction
                    .replace(R.id.com_onssdk_debug_fragment_container, fragments[newIndex])
                    .addToBackStack(null)
                    .commit();
            }
        }
    }

    private void switchFragment(int newIndex, boolean first) {
        switchFragment(newIndex, first, null);
    }

    @Override
    public void onMenuSelected(int menu) {
        switchFragment(menu, false);
    }

    @Override
    public void onCampaignMenuSelected(String campaignToken) {
        switchFragment(LOCAL_CAMPAIGN_DEBUG_FRAGMENT, false, campaignToken);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.com_onssdk_debug_view);
        if (savedInstanceState == null) {
            switchFragment(MAIN_DEBUG_FRAGMENT, true);
        }

        getActionBar().setTitle(R.string.com_onssdk_debug_view_title);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ONS.onStart(this);
    }

    @Override
    protected void onStop() {
        ONS.onStop(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        ONS.onDestroy(this);
        super.onDestroy();
    }
}
