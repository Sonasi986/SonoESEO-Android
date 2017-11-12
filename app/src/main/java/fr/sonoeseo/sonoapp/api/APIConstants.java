package fr.sonoeseo.sonoapp.api;

/**
 * Created by sonasi on 31/10/2017.
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see http://www.gnu.org/licenses/
 */

public class APIConstants {
    // The string for user
    public static final String CORE_USER = "sonoeseo_user.json";
    // The string for directory
    public static final String CORE_DIRECTORY = "sonoeseo_directory.json";
    // The string for Activites
    public static final String CORE_ACTIVITIES = "sonoeseo_activities.json";
    // The string for layout_articles
    public static final String CORE_ARTICLES = "sonoeseo_articles.json";
    // The list of entities registered in the core data
    public static final String[] CORE_ENTITIES = {CORE_ACTIVITIES, CORE_ARTICLES, CORE_DIRECTORY,
            CORE_USER};
    // Root of API link.
    public static final String API_ROOT = "https://api.sonoeseo.com";
    // Link to login.
    public static final String API_LOGIN = API_ROOT + "/connect/";
    // Link to get news & notifications.
    public static final String API_ARTICLES = API_ROOT + "/articles/";
    // Link to get the annuaire.
    public static final String API_DIRECTORY = API_ROOT + "/directory/";
    // Link to get the list of activities including Prestations.
    public static final String API_ACTIVITIES = API_ROOT + "/activities/";
    // Link to register the push token.
    public static final String API_NOTIFICATION = API_ROOT + "/notification/";
    // Link to get the user datas.
    public static final String API_USER = API_ROOT + "/user/";
    // Link to set the user state for a service.
    public static final String API_SERVICE = API_ROOT + "/service/";
    // Link to sonoeseo.com
    public static final String SONO_ROOT = "http://www.sonoeseo.com";
    // Link to user's avatar
    public static final String SONO_AVATAR = SONO_ROOT + "/img/team/";
    public static final String SONO_CONDITIONS = SONO_ROOT + "/conditions_application.php";
    public static final String EMAIL = "sonoeseo@gmail.com";
    public static final String EMAIL_TOPIC = "[APP] J'ai oublié mon mot de passe.";
    public static Boolean ACTIVITIES_LOADED = false;
    public static Boolean ARTICLES_LOADED = false;
    public static Boolean DIRECTORY_LOADED = false;
    public static Boolean USER_LOADED = false;
}
