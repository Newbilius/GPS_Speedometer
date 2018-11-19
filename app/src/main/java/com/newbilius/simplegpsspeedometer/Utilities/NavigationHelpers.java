package com.newbilius.simplegpsspeedometer.Utilities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class NavigationHelpers {
    public static void goToUrl(Context context, String url) {
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    public static void rateApplicationInGooglePlay(Context context) {
        context.startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
    }
}
