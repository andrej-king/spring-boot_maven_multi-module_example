package com.example.common.domain.util;

import lombok.experimental.UtilityClass;

/**
 * General utils
 */
@UtilityClass
public class AppUtil {
    /**
     * Date time format with TZ<br/>
     * Example result: <i>2022-12-02T10:17:38Z<i/>
     */
    public final String ISO8601_DATE_TIME_TZ = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public final String APP_TIMEZONE = "UTC";

    /* ENV path */
    public final String ENV_PATH_PROFILE = "${spring.profiles.active:Unknown}";
    public final String ENV_PATH_DEBUG = "${app-config.debug}";

    /* Profile possible values */
    public final String PROFILE_DEV = "dev";
    public final String PROFILE_PROD = "prod";
    public final String PROFILE_TEST = "test";

}
