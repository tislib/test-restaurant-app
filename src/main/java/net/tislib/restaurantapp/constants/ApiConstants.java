package net.tislib.restaurantapp.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ApiConstants {

    // api general
    public static final String API_VERSION_V1 = "1.0";
    public static final String API_BASE_PATH = "/api/" + API_VERSION_V1;

    // resource paths
    public static final String API_AUTHENTICATION = API_BASE_PATH + "/authentication";
    public static final String API_USERS = API_BASE_PATH + "/users";
    public static final String API_RESTAURANTS = API_BASE_PATH + "/restaurants";

    // parameters


}
