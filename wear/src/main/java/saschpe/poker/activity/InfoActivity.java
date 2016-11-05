package saschpe.poker.activity;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.CardFragment;

import saschpe.poker.BuildConfig;
import saschpe.poker.R;
import saschpe.versioninfo.VersionInfoUtils;

public class InfoActivity extends WearableActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        String description = VersionInfoUtils.getFormattedVersion(this, getPackageName(), BuildConfig.VERSION_NAME) + "\n" + VersionInfoUtils.getFormattedCopyright(this, getPackageName(), "Sascha Peilicke");

        CardFragment cardFragment = CardFragment.create(
                getString(R.string.app_name), description,
                R.mipmap.ic_launcher);

        getFragmentManager().beginTransaction()
                .add(R.id.frame_layout, cardFragment)
                .commit();
    }
}