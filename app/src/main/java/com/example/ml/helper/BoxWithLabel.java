package com.example.ml.helper;

import android.graphics.Rect;

public class BoxWithLabel {
    public Rect rect;
    String label;

    public BoxWithLabel(Rect rect, String label) {
        this.rect = rect;
        this.label = label;
    }

}
