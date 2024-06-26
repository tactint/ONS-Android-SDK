package com.ons.android;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.TaskStackBuilder;
import com.ons.android.annotation.PublicSDK;
import com.ons.android.core.DeeplinkHelper;
import com.ons.android.core.Logger;
import com.ons.android.di.providers.ActionModuleProvider;

/**
 * Dummy activity that starts {@link ONSActionService} or opens a deeplink
 */
@PublicSDK
public class ONSActionActivity extends Activity {

    private static final String TAG = "ONSActionActivity";
    public static final String EXTRA_DEEPLINK_KEY = "deeplink";

    @NonNull
    private Intent addPayloadToIntent(@NonNull Intent intent, @Nullable Bundle payload) {
        if (payload != null) {
            intent.putExtra(ONS.Push.PAYLOAD_KEY, payload);
        }
        return intent;
    }

    @NonNull
    private TaskStackBuilder addPayloadToTaskStackBuilder(
        @NonNull TaskStackBuilder stackBuilder,
        @Nullable Bundle payload
    ) {
        if (payload != null && stackBuilder.getIntentCount() > 0) {
            Intent firstIntent = stackBuilder.editIntentAt(0);
            if (firstIntent != null) {
                addPayloadToIntent(firstIntent, payload);
            }
        }
        return stackBuilder;
    }

    /**
     * Launch the deeplink by using ONSDeeplinkInterceptor if available
     *
     * @param activityIntent
     * @param deeplink
     * @throws DeeplinkInterceptorRuntimeException
     */
    private void launchDeeplink(final Intent activityIntent, final String deeplink)
        throws DeeplinkInterceptorRuntimeException {
        Bundle payload = activityIntent.getBundleExtra(ONS.Push.PAYLOAD_KEY);
        ONSDeeplinkInterceptor interceptor = ActionModuleProvider.get().getDeeplinkInterceptor();
        if (interceptor != null) {
            try {
                TaskStackBuilder stackBuilder;
                try {
                    stackBuilder = interceptor.getTaskStackBuilder(this, deeplink);
                } catch (RuntimeException re) {
                    Logger.error(
                        TAG,
                        "Interceptor has thrown a runtime exception. Aborting deeplink opens by rethrowing",
                        re
                    );
                    throw new DeeplinkInterceptorRuntimeException(re);
                }

                if (stackBuilder != null) {
                    addPayloadToTaskStackBuilder(stackBuilder, payload).startActivities();
                    return;
                }

                Intent i;
                try {
                    i = interceptor.getIntent(this, deeplink);
                } catch (RuntimeException re) {
                    Logger.error(
                        TAG,
                        "Interceptor has thrown a runtime exception. Aborting deeplink opens by rethrowing",
                        re
                    );
                    throw new DeeplinkInterceptorRuntimeException(re);
                }

                if (i != null) {
                    startActivity(addPayloadToIntent(i, payload));
                    return;
                }
            } catch (DeeplinkInterceptorRuntimeException die) {
                throw die;
            } catch (Exception e) {
                Logger.error(TAG, "Error when trying to open deeplink from interceptor. Using fallback intent.", e);

                Intent fallback;
                try {
                    fallback = interceptor.getFallbackIntent(this);
                } catch (RuntimeException re) {
                    Logger.error(
                        TAG,
                        "Interceptor has thrown a runtime exception. Aborting deeplink opens by rethrowing",
                        re
                    );
                    throw new DeeplinkInterceptorRuntimeException(re);
                }

                if (fallback != null) {
                    startActivity(addPayloadToIntent(fallback, payload));
                }
                return;
            }
        }

        // Interceptor not set or return null, using default behaviour
        Intent i = DeeplinkHelper.getIntent(deeplink, false, true);
        startActivity(addPayloadToIntent(i, payload));
    }

    @Override
    protected void onStart() {
        super.onStart();
        ONS.onStart(this);

        final Intent activityIntent = getIntent();
        if (activityIntent != null) {
            final String deeplink = activityIntent.getStringExtra(EXTRA_DEEPLINK_KEY);
            if (!TextUtils.isEmpty(deeplink)) {
                try {
                    launchDeeplink(activityIntent, deeplink);
                } catch (DeeplinkInterceptorRuntimeException die) {
                    throw die.getWrappedRuntimeException();
                } catch (ActivityNotFoundException e) {
                    Logger.error(
                        TAG,
                        "Could not open deeplink: no activity found to handle Intent. Is it valid and your manifest well-formed? URL: " +
                        deeplink
                    );
                } catch (Exception e) {
                    Logger.error(TAG, "Error while trying to open a deeplink", e);
                }
            } else {
                // If not started for a deeplink, forward to the service
                final Intent i = new Intent(this, ONSActionService.class);
                i.setAction(activityIntent.getAction());
                i.putExtras(activityIntent);
                startService(i);
            }
        }
        finish();
    }

    @Override
    protected void onStop() {
        ONS.onStop(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        ONS.onDestroy(this);
        super.onDestroy();
    }
}
