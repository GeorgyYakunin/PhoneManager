package com.fara.phonemanager.utils;

import android.content.Context;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

public class SpannableStringUtil {
    private static SpannableStringUtil spannableStringUtil;
    private Context context;

    private SpannableStringBuilder builder;
    private SpannableString redSpannable;

    private SpannableStringUtil(Context context) {
        this.context = context;
        this.builder = new SpannableStringBuilder();
    }

    public static SpannableStringUtil getInstance(Context context) {
        return new SpannableStringUtil(context);
    }

    public SpannableStringBuilder build(String text, int color, int start, int end) {
        redSpannable = new SpannableString(text);
        redSpannable.setSpan(new ForegroundColorSpan(color), start, end, 0);
        builder.append(redSpannable);

        return builder;
    }
}