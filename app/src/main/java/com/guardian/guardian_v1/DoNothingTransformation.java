package com.guardian.guardian_v1;

import android.graphics.Rect;
import android.text.method.TransformationMethod;
import android.view.View;

public class DoNothingTransformation implements TransformationMethod {
    @Override
    public CharSequence getTransformation(CharSequence source, View view) {
        return source;
    }

    @Override
    public void onFocusChanged(View view, CharSequence sourceText, boolean focused, int direction, Rect previouslyFocusedRect) {

    }
}
