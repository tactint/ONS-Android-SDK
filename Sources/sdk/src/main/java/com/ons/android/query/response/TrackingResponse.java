package com.ons.android.query.response;

import com.ons.android.query.QueryType;
import com.ons.android.query.TrackingQuery;

/**
 * Response for a {@link TrackingQuery}
 */
public class TrackingResponse extends Response {

    /**
     * @param queryID id of the query
     */
    public TrackingResponse(String queryID) {
        super(QueryType.TRACKING, queryID);
    }
}
