package org.olf

import com.k_int.okapi.OkapiTenantAwareController;
import com.k_int.web.toolkit.SimpleLookupService;

/**
 * Overrides doTheLookup so that we can change the maximum number of records that can be returned per page
 */
class OkapiTenantAwareExtendedController<T> extends OkapiTenantAwareController<T>  {

    // Required as we override the maximum number of records that can be returned in index
    SimpleLookupService simpleLookupService

    /** Specifies the maximum number of records to return for a page */
    private int maxRecordsPerPage = 1000;

    OkapiTenantAwareExtendedController(Class<T> resource, int maxRecordsPerPage = 1000) {
        this(resource, false, maxRecordsPerPage);
    }

    OkapiTenantAwareExtendedController(Class<T> resource, boolean readOnly, int maxRecordsPerPage = 1000) {
        super(resource, readOnly);
        this.maxRecordsPerPage = maxRecordsPerPage;
    }

    @Override
    /**
     * We override this method so we can vary what the maximum number of items that get returned is.
     * So for a large record we can restrict it to the default of 100 that is in the library
     * and for smaller records we can make it a larger number.
     * The method was copied from com.k_int.web.toolkit.rest.RestfulController.groovy with maxRecordsPerPage replacing 100
     * Note: simpleLookupService still caps it at 1000 regardless
     *
     * @param res The class that we are returning records for
     * @param baseQuery A closure that contains any extra queries beyond what has been asked for in the parameters
     * @return The results of the query
     */
    protected def doTheLookup (Class<T> res = this.resource, Closure baseQuery) {
        final int offset = params.int("offset") ?: 0;
        final int perPage = Math.min(params.int('perPage') ?: params.int('max') ?: 10, maxRecordsPerPage);
        final int page = params.int("page") ?: (offset ? (offset / perPage) + 1 : 1);
        final List<String> filters = getParamList("filters");
        final List<String> match_in = getParamList("match");
        final List<String> sorts = getParamList("sort");

        if (params.boolean('stats')) {
            return(simpleLookupService.lookupWithStats(res, params.term, perPage, page, filters, match_in, sorts, null, baseQuery));
        } else {
            return(simpleLookupService.lookup(res, params.term, perPage, page, filters, match_in, sorts, baseQuery));
        }
    }
}
