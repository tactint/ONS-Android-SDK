package com.ons.android.messaging.view.helper;

import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.ons.android.core.Logger;
import com.ons.android.messaging.AsyncImageDownloadTask;
import com.ons.android.messaging.gif.GifHelper;
import com.ons.android.module.MessagingModule;

public class ImageHelper {

    public interface Cache {
        void put(@NonNull AsyncImageDownloadTask.Result result);

        @Nullable
        AsyncImageDownloadTask.Result get(@NonNull String key);
    }

    public static void setDownloadResultInImage(
        @NonNull ImageView targetImage,
        @NonNull AsyncImageDownloadTask.Result result
    ) {
        if (result instanceof AsyncImageDownloadTask.BitmapResult) {
            targetImage.setImageBitmap(((AsyncImageDownloadTask.BitmapResult) result).get());
        } else if (result instanceof AsyncImageDownloadTask.GIFResult) {
            try {
                byte[] gifData = ((AsyncImageDownloadTask.GIFResult) result).get();
                targetImage.setImageDrawable(GifHelper.getDrawableForBytes(targetImage.getContext(), gifData, true));
            } catch (Exception e) {
                Logger.internal(MessagingModule.TAG, "Could not start GIF", e);
            }
        } else {
            Logger.internal(MessagingModule.TAG, "Could not display AsyncImageDownloadTask.Result: unknown type");
        }
    }
}
