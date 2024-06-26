package com.ons.android.lint;

import com.android.tools.lint.client.api.IssueRegistry;
import com.android.tools.lint.client.api.Vendor;
import com.android.tools.lint.detector.api.ApiKt;
import com.android.tools.lint.detector.api.Issue;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

@SuppressWarnings("unused")
public class ONSIssueRegistry extends IssueRegistry
{

    @Override
    public int getApi()
    {
        return ApiKt.CURRENT_API;
    }

    @Override
    public int getMinApi()
    {
        return 6;
    }

    @Nullable
    @Override
    public Vendor getVendor()
    {
        return new Vendor("ONS.com", "com.ons.android", "https://pfs.gdn", "ONS SDK Team via email/slack");
    }

    @NotNull
    @Override
    public List<Issue> getIssues()
    {
        return Collections.singletonList(DIProvideMethodDetector.ISSUE);
    }

}
