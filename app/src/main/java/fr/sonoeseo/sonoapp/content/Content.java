package fr.sonoeseo.sonoapp.content;

import java.util.ArrayList;

import fr.sonoeseo.sonoapp.models.Activity;
import fr.sonoeseo.sonoapp.models.Article;
import fr.sonoeseo.sonoapp.models.Mate;
import fr.sonoeseo.sonoapp.models.User;

/**
 * Created by sonasi on 02/07/2017.
 * <p>
 * This class contain every data used in the application.
 * When the data is loaded, every Controller will look for this variables.
 * <p>
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

public class Content {

    // Sort variables.
    public static Boolean categorized = false;
    public static Boolean ascendingSort = true;

    // The user object.
    public static User user;

    // The list of articles (Could be notification or news).
    public static ArrayList<Article> articles = new ArrayList<>();

    // The list of mates in the directory.
    public static ArrayList<Mate> directory = new ArrayList<>();

    // The list of activities.
    public static ArrayList<Activity> activities = new ArrayList<>();

    // The selected activity.
    public static Activity activity;

    // The selected article.
    public static Article article;

}
