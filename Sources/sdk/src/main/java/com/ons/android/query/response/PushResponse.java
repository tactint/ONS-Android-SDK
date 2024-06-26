package com.ons.android.query.response;

import com.ons.android.query.PushQuery;
import com.ons.android.query.QueryType;

/**
 * Response for {@link PushQuery}
 */
public class PushResponse extends Response {

    /**
     * @param queryID
     */
    public PushResponse(String queryID) {
        super(QueryType.PUSH, queryID);
    }
}
