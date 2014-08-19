package com.sarltokyo.customlistfragementsample4.app;

/**
 * Created by osabe on 14/08/19.
 */

/**
 * ローダーのアイテム用データクラス
 */
public class Entry {
    private String mLabel;

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String label) {
        mLabel = label;
    }

    @Override
    public String toString() {
        return mLabel;
    }
}
