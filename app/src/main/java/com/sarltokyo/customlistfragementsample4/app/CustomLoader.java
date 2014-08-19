package com.sarltokyo.customlistfragementsample4.app;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by osabe on 14/08/19.
 */
public class CustomLoader extends AsyncTaskLoader<List<Entry>> {

    List<Entry> list;
    static List<Entry> sOldList;
    static List<Entry> sData;
    int mCount;
    int m;

    private static final String TAG = CustomLoader.class.getSimpleName();

    public CustomLoader(Context context, int count) {
        super(context);
        mCount = count;
    }

    /**
     * バックグラウンドでローダ用のデータを読み込む
     */
    @Override
    public List<Entry> loadInBackground() {

        // 2秒止める
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (mCount == 0) {
            sData = new ArrayList<Entry>();
            m = 0;
        } else {
            // それまでのリスト
            sData = sOldList;
            m = sData.size();
        }
        Log.d(TAG, "m = " + m);

        for (int i = 0; i < 30; i++) {
            Entry entry = new Entry();
            entry.setLabel("text" + (i + m));
            sData.add(entry);
        }

       // Entry用のリストを作成
        List<Entry> entries = new ArrayList<Entry>(sData.size());

        for (int i = 0; i < sData.size(); i++) {
            Entry entry = sData.get(i);
            entries.add(entry);
        }

        // このソートは不要。AsyncTaskLoaderを何回も呼ぶ時、逐次的に、前の処理が終わったら
        // 次の処理を呼ぶ場合は、データ順序が正しくなるが、このアプリの実装初期、
        // 築時的に正しく呼ばれていなく、データの順序が正しくならなかったので、
        // リストのソートを実装した。
        // リストをソート
        Collections.sort(entries, CUSTOM_COMPARATOR);

        // entriesの内容をsOldListにコピー
        sOldList = new ArrayList<Entry>();
        for (Entry entry: entries) {
            sOldList.add(entry);
        }

        return entries;
    }

    /**
     * 提供する新しいデータがあるときに呼び出される
     */
    @Override
    public void deliverResult(List<Entry> data) {
        if (isReset()) {
            // リセット時(または最初に読み込みが開始されていない、もしくは
            // reset()が呼び出された後)現在の非同期クエリを解放
            if (data != null) {
                onReleaseResources(data);
            }
            return;
        }

        List<Entry> oldEntries = data;

        sData = data;

        if (isStarted()) {
            // 読み込みが開始されている (startLoading()が呼び出されているが
            // stopLoading()やreset()はまだ呼び出されていない)場合いｎ、その結果を返す
            super.deliverResult(data);
        }

        // この時点で、必要であれば oldEntries に関連するリソースを解放できる
        if (oldEntries != null && oldEntries != data) {
            onReleaseResources(oldEntries);
        }
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    public void reset() {
        super.reset();
        onStopLoading();
    }

    /**
     * 読み込んだデータ・セットに関連するリソースを解放するヘルパーメソッド
     */
    protected void onReleaseResources(List<Entry> apps) {
        // Cursorの場合は閉じる
        // 単純なリストList<>の場合は特に何もしない
    }

    /**
     * Entry用のComparator
     */
    public static final Comparator<Entry> CUSTOM_COMPARATOR =
            new Comparator<Entry>() {
//                private final Collator sCollator = Collator.getInstance();

                @Override
                public int compare(Entry object1, Entry object2) {
// 文字順だと、数字の順にならない
//                    return sCollator.compare(object1.getLabel(), object2.getLabel());
                    String str1 = object1.toString();
                    String str2 = object2.toString();
                    int length1 = str1.length();
                    int length2 = str2.length();
                    int num1 = Integer.valueOf(str1.substring(4, length1));
                    int num2 = Integer.valueOf(str2.substring(4, length2));
// 整数引数をとるCollator#compareはない
//                    return sCollator.compare(num1, num2);
                    if (num1 > num2) {
                        return +1;
                    } else if (num1 == num2) {
                        return 0;
                    } else {
                        return -1;
                    }
                }
            };
}
