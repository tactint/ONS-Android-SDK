package com.ons.android.module;

import android.content.Context;
import androidx.annotation.NonNull;
import com.ons.android.di.providers.ActionModuleProvider;
import com.ons.android.di.providers.DataCollectionModuleProvider;
import com.ons.android.di.providers.DisplayReceiptModuleProvider;
import com.ons.android.di.providers.EventDispatcherModuleProvider;
import com.ons.android.di.providers.LocalCampaignsModuleProvider;
import com.ons.android.di.providers.MessagingModuleProvider;
import com.ons.android.di.providers.ProfileModuleProvider;
import com.ons.android.di.providers.PushModuleProvider;
import com.ons.android.di.providers.TrackerModuleProvider;
import com.ons.android.di.providers.UserModuleProvider;
import com.ons.android.processor.Module;
import com.ons.android.processor.Provide;
import com.ons.android.processor.Singleton;
import java.util.ArrayList;
import java.util.List;

/**
 * Module master that dispatch to subscribed modules
 *
 */
@Module
@Singleton
public class ONSModuleMaster extends ONSModule {

    /**
     * Subscribed modules
     */
    private final List<ONSModule> modules;

    private ONSModuleMaster(List<ONSModule> modules) {
        this.modules = modules;
    }

    @Provide
    public static ONSModuleMaster provide() {
        List<ONSModule> modules = new ArrayList<>(8);

        modules.add(ActionModuleProvider.get());
        modules.add(DisplayReceiptModuleProvider.get());
        modules.add(EventDispatcherModuleProvider.get());
        modules.add(LocalCampaignsModuleProvider.get());
        modules.add(MessagingModuleProvider.get());
        modules.add(PushModuleProvider.get());
        modules.add(TrackerModuleProvider.get());
        modules.add(UserModuleProvider.get());
        modules.add(ProfileModuleProvider.get());
        modules.add(DataCollectionModuleProvider.get());
        return new ONSModuleMaster(modules);
    }

    // ------------------------------------------->

    @Override
    public String getId() {
        return "master";
    }

    @Override
    public int getState() {
        return 1;
    }

    @Override
    public void onsContextBecameAvailable(@NonNull Context applicationContext) {
        for (ONSModule module : modules) {
            module.onsContextBecameAvailable(applicationContext);
        }
    }

    @Override
    public void onsWillStart() {
        for (ONSModule module : modules) {
            module.onsWillStart();
        }
    }

    @Override
    public void onsDidStart() {
        for (ONSModule module : modules) {
            module.onsDidStart();
        }
    }

    @Override
    public void onsIsFinishing() {
        for (ONSModule module : modules) {
            module.onsIsFinishing();
        }
    }

    @Override
    public void onsWillStop() {
        for (ONSModule module : modules) {
            module.onsWillStop();
        }
    }

    @Override
    public void onsDidStop() {
        for (ONSModule module : modules) {
            module.onsDidStop();
        }
    }
}
