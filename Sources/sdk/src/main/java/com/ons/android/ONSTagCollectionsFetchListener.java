package com.ons.android;

import android.content.Context;
import androidx.annotation.NonNull;
import com.ons.android.annotation.PublicSDK;
import java.util.Map;
import java.util.Set;

/**
 * Listener used when fetching tag collections using {@link ONS.User#fetchTagCollections(Context, ONSTagCollectionsFetchListener)}.
 */
@PublicSDK
public interface ONSTagCollectionsFetchListener {
    /**
     * @param tagCollections A map of set of tag collections. The keys are the ones used when setting the tag collections.
     */
    void onSuccess(@NonNull Map<String, Set<String>> tagCollections);

    void onError();
}
