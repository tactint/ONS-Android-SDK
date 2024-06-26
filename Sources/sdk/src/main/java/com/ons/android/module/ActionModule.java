package com.ons.android.module;

import android.content.Context;
import android.text.TextUtils;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.ons.android.ONS;
import com.ons.android.ONSDeeplinkInterceptor;
import com.ons.android.UserAction;
import com.ons.android.UserActionSource;
import com.ons.android.actions.ClipboardActionRunnable;
import com.ons.android.actions.DeeplinkActionRunnable;
import com.ons.android.actions.GroupActionRunnable;
import com.ons.android.actions.LocalCampaignsRefreshActionRunnable;
import com.ons.android.actions.NotificationPermissionActionRunnable;
import com.ons.android.actions.RatingActionRunnable;
import com.ons.android.actions.RedirectNotificationSettingsAction;
import com.ons.android.actions.SmartReOptInAction;
import com.ons.android.actions.UserDataBuiltinActionRunnable;
import com.ons.android.actions.UserEventBuiltinActionRunnable;
import com.ons.android.core.Logger;
import com.ons.android.json.JSONObject;
import com.ons.android.processor.Module;
import com.ons.android.processor.Singleton;
import java.util.HashMap;
import java.util.Locale;

/**
 * ONS's Action Module.
 *
 */
@Module
@Singleton
public class ActionModule extends ONSModule {

    public static final String TAG = "Action";

    public static final String RESERVED_ACTION_IDENTIFIER_PREFIX = "ons.";

    private HashMap<String, UserAction> registeredActions;

    private HashMap<String, Integer> drawableAliases;

    /**
     * Deeplink interceptor
     */
    private ONSDeeplinkInterceptor deeplinkInterceptor = null;

    public ActionModule() {
        registeredActions = new HashMap<>();
        drawableAliases = new HashMap<>();

        //TODO: Try to load them lazily on the first "performAction"
        registerBuiltinActions();
    }

    /**
     * See {@link ONS.Actions#register(UserAction)}
     */
    public void registerAction(@NonNull UserAction userAction) {
        //noinspection ConstantConditions
        if (userAction == null) {
            throw new IllegalArgumentException("action cannot be null");
        }

        final String identifier = userAction.getIdentifier();
        if (identifier.startsWith(RESERVED_ACTION_IDENTIFIER_PREFIX)) {
            throw new IllegalArgumentException(
                "The action identifier ('" +
                identifier +
                "') is using a reserved prefix (" +
                RESERVED_ACTION_IDENTIFIER_PREFIX +
                ")"
            );
        }

        if (registeredActions.put(identifier.toLowerCase(Locale.US), userAction) != null) {
            Logger.warning(TAG, "Action '" + identifier + "' was already registered, and will be replaced.");
        }
    }

    /**
     * See {@link ONS.Actions#unregister(String)}
     */
    public void unregisterAction(@NonNull String identifier) {
        //noinspection ConstantConditions
        if (identifier == null) {
            throw new IllegalArgumentException("identifier cannot be null");
        }

        if ("".equals(identifier.trim())) {
            throw new IllegalArgumentException("identifier cannot be empty");
        }

        if (identifier.startsWith(RESERVED_ACTION_IDENTIFIER_PREFIX)) {
            throw new IllegalArgumentException(
                "The action identifier ('" +
                identifier +
                "') is using a reserved prefix (" +
                RESERVED_ACTION_IDENTIFIER_PREFIX +
                ")"
            );
        }

        registeredActions.remove(identifier.toLowerCase(Locale.US));
    }

    /**
     * See {@link ONS.Actions#addDrawableAlias(String, int)}
     */
    public void addDrawableAlias(@NonNull String alias, @DrawableRes int drawableResID) {
        //noinspection ConstantConditions
        if (alias == null) {
            throw new IllegalArgumentException("identifier cannot be null");
        }

        if ("".equals(alias.trim())) {
            throw new IllegalArgumentException("identifier cannot be empty");
        }

        if (drawableResID == 0) {
            throw new IllegalArgumentException("drawable ressource ID must be different than 0");
        }

        drawableAliases.put(alias.toLowerCase(Locale.US), drawableResID);
    }

    @DrawableRes
    public int getAliasedDrawableID(@NonNull String alias) {
        //noinspection ConstantConditions
        if (alias == null) {
            return 0;
        }

        if ("".equals(alias.trim())) {
            return 0;
        }

        Integer resId = drawableAliases.get(alias.toLowerCase(Locale.US));

        return resId != null ? resId : 0;
    }

    /**
     * Perform the specified action.
     * Meant for the public SDK
     *
     * @param context    Context, if any
     * @param identifier Action identifier
     * @param args       Action arguments in JSON form
     * @return true if an action was registered for this identifier and performed, false otherwise.
     */
    public boolean performUserAction(@Nullable Context context, @NonNull String identifier, @Nullable JSONObject args) {
        if (
            TextUtils.isEmpty(identifier) ||
            identifier.toLowerCase(Locale.US).startsWith(RESERVED_ACTION_IDENTIFIER_PREFIX)
        ) {
            Logger.internal(
                TAG,
                "Identifier is null, empty, or starts with " + RESERVED_ACTION_IDENTIFIER_PREFIX + " . Aborting."
            );
            return false;
        }

        if (args == null) {
            args = new JSONObject();
        }

        return performAction(context, identifier, args, null);
    }

    /**
     * Perform the specified action.
     * Meant for the private SDK
     *
     * @param context    Context, if any
     * @param identifier Action identifier
     * @param args       Action arguments in JSON form
     * @param source     The action source
     * @return true if an action was registered for this identifier and performed, false otherwise.
     */
    public boolean performAction(
        @Nullable Context context,
        @NonNull String identifier,
        @NonNull JSONObject args,
        @Nullable UserActionSource source
    ) {
        final UserAction userAction = registeredActions.get(identifier.toLowerCase(Locale.US));

        if (userAction != null) {
            userAction.getRunnable().performAction(context, userAction.getIdentifier(), args, source);
            return true;
        }

        Logger.error(
            TAG,
            "ONS attempted to perform the action named '" +
            identifier +
            "' but couldn't find any action registered to handle this."
        );

        return false;
    }

    /**
     * Get the developer registered deeplink interceptor
     */
    public void setDeeplinkInterceptor(@Nullable ONSDeeplinkInterceptor interceptor) {
        this.deeplinkInterceptor = interceptor;
    }

    /**
     * Set the developer registered deeplink interceptor
     */
    @Nullable
    public ONSDeeplinkInterceptor getDeeplinkInterceptor() {
        return this.deeplinkInterceptor;
    }

    private void registerBuiltinActions() {
        registeredActions.put(
            GroupActionRunnable.IDENTIFIER,
            new UserAction(GroupActionRunnable.IDENTIFIER, new GroupActionRunnable(this))
        );

        registeredActions.put(
            DeeplinkActionRunnable.IDENTIFIER,
            new UserAction(DeeplinkActionRunnable.IDENTIFIER, new DeeplinkActionRunnable(this))
        );

        registeredActions.put(
            LocalCampaignsRefreshActionRunnable.IDENTIFIER,
            new UserAction(LocalCampaignsRefreshActionRunnable.IDENTIFIER, new LocalCampaignsRefreshActionRunnable())
        );

        registeredActions.put(
            UserDataBuiltinActionRunnable.IDENTIFIER,
            new UserAction(UserDataBuiltinActionRunnable.IDENTIFIER, new UserDataBuiltinActionRunnable())
        );

        registeredActions.put(
            UserEventBuiltinActionRunnable.IDENTIFIER,
            new UserAction(UserEventBuiltinActionRunnable.IDENTIFIER, new UserEventBuiltinActionRunnable())
        );

        registeredActions.put(
            ClipboardActionRunnable.IDENTIFIER,
            new UserAction(ClipboardActionRunnable.IDENTIFIER, new ClipboardActionRunnable())
        );

        registeredActions.put(
            RatingActionRunnable.IDENTIFIER,
            new UserAction(RatingActionRunnable.IDENTIFIER, new RatingActionRunnable())
        );

        registeredActions.put(
            NotificationPermissionActionRunnable.IDENTIFIER,
            new UserAction(NotificationPermissionActionRunnable.IDENTIFIER, new NotificationPermissionActionRunnable())
        );

        registeredActions.put(
            SmartReOptInAction.IDENTIFIER,
            new UserAction(SmartReOptInAction.IDENTIFIER, new SmartReOptInAction())
        );

        registeredActions.put(
            RedirectNotificationSettingsAction.IDENTIFIER,
            new UserAction(RedirectNotificationSettingsAction.IDENTIFIER, new RedirectNotificationSettingsAction())
        );
    }

    public int getDrawableIdForNameOrAlias(@NonNull Context context, @Nullable String drawableName) {
        if (TextUtils.isEmpty(drawableName)) {
            return 0;
        }

        int resId = getAliasedDrawableID(drawableName);

        if (resId == 0) {
            // Try to search in the current package's resources
            resId = context.getResources().getIdentifier(drawableName, "drawable", context.getPackageName());
        }

        return resId;
    }

    //region: ONSModule

    @Override
    public String getId() {
        return "action";
    }

    @Override
    public int getState() {
        return 1;
    }
    //endregion
}
