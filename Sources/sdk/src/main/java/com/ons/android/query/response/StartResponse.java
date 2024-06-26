package com.ons.android.query.response;

import com.ons.android.query.QueryType;
import com.ons.android.query.StartQuery;

/**
 * Response for a {@link StartQuery}
 */
public final class StartResponse extends Response {

    /**
     * @param queryID
     */
    public StartResponse(String queryID) {
        super(QueryType.START, queryID);
    }
}
