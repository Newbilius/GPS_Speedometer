package com.newbilius.simplegpsspeedometer.Utilities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class NavigationHelpers {
    public static void goToUrl(Activity activity, String url) {
        activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    public static void rateApplicationInGooglePlay(Activity activity) {
        activity.startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://play.google.com/store/apps/details?id=" + activity.getPackageName())));
    }
}
