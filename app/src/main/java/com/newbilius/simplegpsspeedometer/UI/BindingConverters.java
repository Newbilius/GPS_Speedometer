package com.newbilius.simplegpsspeedometer.UI;

import android.databinding.BindingConversion;
import android.view.View;

public class BindingConverters {
    @BindingConversion
    public static int boolToIsVisibleConverter(boolean value) {
        return value
                ? View.VISIBLE
                : View.GONE;
    }
}
