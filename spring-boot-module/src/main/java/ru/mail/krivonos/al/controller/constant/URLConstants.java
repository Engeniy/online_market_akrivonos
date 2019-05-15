package ru.mail.krivonos.al.controller.constant;

public class URLConstants {

    public static final String REDIRECT_WITH_PARAMETER_TEMPLATE = "redirect:%s?%s";
    public static final String HOMEPAGE_URL = "/";
    public static final String LOGIN_PAGE_URL = "/login";
    public static final String FORBIDDEN_PAGE_URL = "/403";
    public static final String INTERNAL_ERROR_PAGE_URL = "/500";
    public static final String USERS_PAGE_URL = "/private/users";
    public static final String USERS_PAGE_WITH_PAGE_NUMBER_URL = "/private/users/{page}";
    public static final String USERS_ADD_PAGE_URL = "/private/users/add";
    public static final String USERS_UPDATE_URL = "/private/users/{id}/update";
    public static final String USERS_DELETE_URL = "/private/users/delete";
    public static final String USERS_PASSWORD_CHANGE_URL = "/private/users/{id}/password";
    public static final String REVIEWS_PAGE_URL = "/reviews";
    public static final String REVIEWS_PAGE_WITH_PAGE_NUMBER_URL = "/reviews/{page}";
    public static final String REVIEWS_DELETE_URL = "/reviews/{id}/delete";
    public static final String REVIEWS_UPDATE_URL = "/reviews/update";
    public static final String BOOTSTRAP_CONTENT_URL = "/webjars/**";

    private URLConstants() {
    }
}
