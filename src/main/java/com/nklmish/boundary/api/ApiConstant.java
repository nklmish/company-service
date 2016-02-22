package com.nklmish.boundary.api;

public class ApiConstant {

    public static class HalResourceNames {
        public static final String COMPANIES = "companies";
    }

    public static class Urls {
        public static final String API_ROOT = "/api";
        public static final String COMPANIES = API_ROOT + "/" + "companies";
    }

    public static class TemplatePlaceHolders {
        public static final String PAGE = "page";
        public static final String SIZE = "size";
        public static final String SORT = "sort";
    }

    public static class RequestParamsNames {
        public static class Company {
            public static final String BENEFICIAL_OWNERS = "beneficialOwners";
            public static final String BENEFICIAL_OWNERS_FIELD_NAME = "name";
        }
    }

    public static class Format {
        public static final String APPLICATION_HAL_JSON = "application/hal+json";
    }

    public static class Documentation {
        public static final String PAGINATED_RESULTS = "Please note results are paginated. Pagination starts from 0 and number of results on a specific page can be " +
                "altered via size parameter(default 25)";
    }

    public static class Pagination {
        public static final int DEFAULT_RETURN_RECORD_COUNT = 1;
    }
}
