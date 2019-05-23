package ru.mail.krivonos.al.controller.constant;

public class URLConstants {

    public static final String REDIRECT_WITH_PARAMETER_TEMPLATE = "redirect:%s?%s";
    public static final String REDIRECT_WITH_DECIMAL_PARAMETER_TEMPLATE = "redirect:%s?%s%d";
    public static final String REDIRECT_WITH_TWO_PARAMETERS_TEMPLATE = "redirect:%s?%s%d&%s";
    public static final String REDIRECT_TEMPLATE = "redirect:%s";
    public static final String HOMEPAGE_URL = "/";
    public static final String LOGIN_PAGE_URL = "/login";
    public static final String FORBIDDEN_PAGE_URL = "/403";
    public static final String INTERNAL_ERROR_PAGE_URL = "/500";
    public static final String USERS_PAGE_URL = "/private/users";
    public static final String USERS_ADD_PAGE_URL = "/private/users/add";
    public static final String USERS_UPDATE_URL = "/private/users/{id}/update";
    public static final String USERS_DELETE_URL = "/private/users/delete";
    public static final String USERS_PASSWORD_CHANGE_URL = "/private/users/{id}/password";
    public static final String REVIEWS_PAGE_URL = "/reviews";
    public static final String REVIEWS_DELETE_URL = "/reviews/{id}/delete";
    public static final String REVIEWS_UPDATE_URL = "/reviews/update";
    public static final String BOOTSTRAP_CONTENT_URL = "/webjars/**";
    public static final String ARTICLES_PAGE_URL = "/articles";
    public static final String ARTICLE_PAGE_URL = "/article";
    public static final String ADD_ARTICLE_PAGE_URL = "/private/articles/add";
    public static final String DELETE_ARTICLE_URL = "/private/articles/delete";
    public static final String EDIT_ARTICLE_URL = "/private/articles/edit";
    public static final String ARTICLE_ADD_COMMENT_URL = "/article/comment";
    public static final String ARTICLE_DELETE_COMMENT_URL = "/article/comment/delete";
    public static final String PROFILE_PAGE_URL = "/private/profile";
    public static final String PROFILE_UPDATE_URL = "/private/profile/{id}/update";
    public static final String PROFILE_PASSWORD_UPDATE_URL = "/private/profile/{id}/password";
    public static final String FAVORITE_ARTICLES_URL = "/private/articles/favorites";
    public static final String ITEMS_PAGE_URL = "/items";

    private URLConstants() {
    }
}
