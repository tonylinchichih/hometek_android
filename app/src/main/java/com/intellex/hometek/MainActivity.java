package com.intellex.hometek;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.intellex.hometek.sip.R;
import com.intellex.hometek.ui.MainFragment;
import com.intellex.hometek.ui.MyBaseFragment;
import com.intellex.hometek.ui.RegisterFragment;
import com.intellex.hometek.ui.WaitingDlgFragment;
import me.leolin.shortcutbadger.ShortcutBadger;
import org.linphone.LinphoneContext;
import org.linphone.LinphoneManager;

public class MainActivity extends AppCompatActivity
        implements MainFragment.OnFragmentInteractionListener,
                RegisterFragment.OnFragmentInteractionListener {
    public static final String TAG = MainActivity.class.getSimpleName();
    private View mProgressBar;

    private static final int MSG_GET_EHOME_STATUS_1 = 1;
    private static final int MSG_GET_EHOME_STATUS_2 = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = findViewById(R.id.marker_progress);
        mProgressBar.setAlpha(0.0f);

        handleIntentFromNotification(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntentFromNotification(intent);
    }

    private void handleIntentFromNotification(Intent intent) {
        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras. In this sample the launcher
        // intent is fired when the notification is tapped, so any accompanying data would
        // be handled here. If you want a different intent fired, set the click_action
        // field of the notification message to the desired intent. The launcher intent
        // is used when no click_action is specified.
        //
        // Handle possible data accompanying notification message.
        // [START handle_data_extras]
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                // Log.d(TAG, "Key: " + key + " Value: " + value);
            }

            if (getIntent().getExtras() != null
                    && getIntent().getExtras().containsKey("notification_id")
                    && getIntent().getExtras().containsKey("message")
                    && getIntent().getExtras().containsKey("badge")) {
                //                insertData((String) getIntent().getExtras().get("message"));
                int badge = Integer.parseInt(getIntent().getExtras().getString("badge"));
                ShortcutBadger.applyCount(this.getApplicationContext(), badge);

                getIntent().getExtras().remove("notification_id");
                getIntent().getExtras().remove("message");
                getIntent().getExtras().remove("badge");
            } else {
                Log.e(TAG, "推播資料格式不對");
            }
        }
        // [END handle_data_extras]
    }

    WaitingDlgFragment dlg = null;

    @Override
    public void onFragmentInteraction(Uri uri) {}

    public void setting(View v) {
        showRegister();
    }

    public void notification(View v) {}

    private void showRegister() {
        showPage(new RegisterFragment(), RegisterFragment.class.getSimpleName());
    }

    public void clickSecurity(View v) {
        ShowSecurity();
    }

    public void ShowSecurity() {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, Main2Activity.class);
        startActivity(intent);
    }

    private void showDeviceTypePage() {}

    MyBaseFragment currentFragment;

    private void showPage(Fragment fragment, String tag) {
        currentFragment = (MyBaseFragment) fragment;
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.setCustomAnimations(
                android.R.anim.fade_in,
                android.R.anim.fade_out,
                android.R.anim.fade_in,
                android.R.anim.slide_out_right);
        transaction.replace(R.id.container2, fragment, tag).addToBackStack(tag);
        transaction.commit();
    }

    public void clickSmartRemote(View v) {
        showDeviceTypePage();
    }

    public void setActionBarTitle(int str_id) {
        ((TextView) findViewById(R.id.page_title)).setText(str_id);
    }

    public void clearActionBarTitle() {
        ((TextView) findViewById(R.id.page_title)).setText("");
    }

    @Override
    protected void onResume() {
        super.onResume();

        // reset miss call
        LinphoneManager.getCore().resetMissedCallsCount();
        LinphoneContext.instance().getNotificationManager().dismissMissedCallNotification();
    }
}
