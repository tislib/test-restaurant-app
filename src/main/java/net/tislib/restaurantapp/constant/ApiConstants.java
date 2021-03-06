package net.tislib.restaurantapp.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ApiConstants {

    // api general
    public static final String API_VERSION_V1 = "1.0";
    public static final String API_BASE_PATH = "/api/" + API_VERSION_V1;

    // resource paths
    public static final String API_SWAGGER_UI = "/swagger-ui";
    public static final String API_H2_CONSOLE = "/h2-console";
    public static final String API_SWAGGER_API_DOCS = "/v3/api-docs";
    public static final String API_AUTHENTICATION = API_BASE_PATH + "/authentication";
    public static final String API_RESTAURANTS = API_BASE_PATH + "/restaurants";
    public static final String API_USERS = API_BASE_PATH + "/users";
    public static final String API_REVIEWS = API_RESTAURANTS + "/{restaurantId}/reviews";

    // paths
    public static final String PATH_ID = "/{id}";
    public static final String PATH_TOKEN = "/token";
    public static final String PATH_REGISTER = "/register";
    public static final String PATH_ANT_MATCH_ALL = "/**";

    public static final String PATH_OWNER_REPLY = PATH_ID + "/owner-reply";

}
