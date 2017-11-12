package fr.sonoeseo.sonoapp.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.sonoeseo.sonoapp.models.Activity;
import fr.sonoeseo.sonoapp.models.Article;
import fr.sonoeseo.sonoapp.models.Mate;
import fr.sonoeseo.sonoapp.models.Material;
import fr.sonoeseo.sonoapp.models.User;

/**
 * Created by sonasi on 01/11/2017.
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

public class APIDecoder {

    private static final String JSON_STATUS = "status";
    private static final String JSON_DATA = "data";

    /**
     * Extract the status from the data.
     *
     * @param data : The JSON String.
     * @return the status as an int.
     */
    public static int extractStatus(String data) {
        int status = 2;

        try {
            JSONObject jsonObject = new JSONObject(data);
            status = jsonObject.getInt(JSON_STATUS);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return status;
    }


    /**
     * Extract the list of Mate from the JSONArray.
     *
     * @param data : The JSONArray which is the list of Mate.
     * @return the ArrayList of mate.
     */
    private static ArrayList<Mate> extractTeam(JSONArray data) {
        ArrayList<Mate> results = new ArrayList<>();

        if (data != null) {
            int id;
            String name, avatar, phone;
            JSONObject jsonObject;
            for (int i = 0; i < data.length(); i++) {
                jsonObject = data.optJSONObject(i);
                if (jsonObject != null) {
                    id = jsonObject.optInt("id");
                    name = optString(jsonObject, "name");
                    avatar = optString(jsonObject, "avatar");
                    phone = optString(jsonObject, "phone");
                    results.add(new Mate(id, name, avatar, phone));
                }
            }
        }
        return results;
    }

    /**
     * Extract the user from the JSON as String.
     *
     * @param data : The JSON String.
     * @return User the instance of the user.
     */
    public static User extractUser(String data) {
        User result;

        int id, admin;
        String name, login, resp, avatar, phone, token;
        ArrayList<Mate> team;
        try {
            JSONObject object = new JSONObject(data);
            JSONObject userInfo = object.getJSONObject(JSON_DATA);

            id = userInfo.getInt("id");
            name = optString(userInfo, "name");
            login = optString(userInfo, "login");
            resp = optString(userInfo, "resp");
            admin = userInfo.getInt("admin");
            avatar = optString(userInfo, "avatar");
            phone = optString(userInfo, "phone");
            token = optString(userInfo, "token");
            team = extractTeam(userInfo.getJSONArray("team"));

            result = new User(id, name, login, resp, avatar, admin, phone, token, team);
        } catch (JSONException e) {
            e.printStackTrace();
            result = null;
        }
        return result;
    }

    /**
     * Extract the list of Articles.
     *
     * @param data : The JSON String.
     * @return the ArrayList of articles.
     */
    public static ArrayList<Article> extractArticles(String data) {
        ArrayList<Article> results = new ArrayList<>();

        String author, authorAvatar, date, title, type, content;
        try {
            JSONObject object = new JSONObject(data);
            JSONArray articles = object.getJSONArray(JSON_DATA);

            for (int i = 0; i < articles.length(); i++) {
                JSONObject article = articles.getJSONObject(i);


                author = optString(article, "author");
                authorAvatar = optString(article, "authorAvatar");
                date = optString(article, "date");
                title = optString(article, "title");
                type = optString(article, "type");
                content = optString(article, "content");
                results.add(new Article(author, authorAvatar, date, title, type, content));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            results.clear();
        }
        return results;
    }

    /**
     * Extract the address book from the JSON.
     *
     * @param data : The JSON String.
     * @return the ArrayList of Mate which is the directory.
     */
    public static ArrayList<Mate> extractDirectory(String data) {
        ArrayList<Mate> results = new ArrayList<>();

        int id;
        String name, avatar, phone;
        try {
            JSONObject object = new JSONObject(data);
            JSONArray directory = object.getJSONArray(JSON_DATA);
            JSONObject mate;
            for (int i = 0; i < directory.length(); i++) {
                mate = directory.getJSONObject(i);

                id = mate.getInt("id");
                name = optString(mate, "name");
                avatar = optString(mate, "avatar");
                phone = optString(mate, "phone");
                results.add(new Mate(id, name, avatar, phone));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            results.clear();
        }
        return results;
    }

    /**
     * Extract the list of materials.
     *
     * @param material : The JSONArray which contains the list of materials.
     * @return The ArrayList of materials.
     */
    private static ArrayList<Material> extractMaterials(JSONArray material) {
        ArrayList<Material> results = new ArrayList<>();

        if (material != null) {
            String name;
            int quantity;
            JSONObject jsonObject;
            for (int i = 0; i < material.length(); i++) {
                jsonObject = material.optJSONObject(i);

                name = optString(jsonObject, "name");
                quantity = jsonObject.optInt("quantity");
                results.add(new Material(name, quantity));
            }
        }
        return results;
    }

    /**
     * Extract the list of activities from the JSON.
     *
     * @param data : The JSON String.
     * @return the ArrayList of activity.
     */
    public static ArrayList<Activity> extractActivities(String data) {
        ArrayList<Activity> results = new ArrayList<>();

        int id, state;
        Mate supervisor;
        String type;
        ArrayList<Mate> team;
        ArrayList<Material> material;
        String title, content, client, date, dateEnd, hour, hourEnd, start, price, guarantee,
                place, address;

        int supervisorId;
        String supervisorName, supervisorAvatar;
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray activities = jsonObject.getJSONArray(JSON_DATA);

            JSONObject jsonActivity, jsonSupervisor;
            for (int i = 0; i < activities.length(); i++) {
                jsonActivity = activities.getJSONObject(i);

                id = jsonActivity.optInt("id");
                state = jsonActivity.optInt("state");

                type = optString(jsonActivity, "type");
                title = optString(jsonActivity, "title");
                content = optString(jsonActivity, "content");
                client = optString(jsonActivity, "client");
                date = optString(jsonActivity, "date");
                dateEnd = optString(jsonActivity, "dateEnd");
                hour = optString(jsonActivity, "hour");
                hourEnd = optString(jsonActivity, "hourEnd");
                start = optString(jsonActivity, "start");
                price = optString(jsonActivity, "price");
                guarantee = optString(jsonActivity, "guarantee");
                place = optString(jsonActivity, "place");
                address = optString(jsonActivity, "address");

                team = extractTeam(jsonActivity.optJSONArray("team"));
                material = extractMaterials(jsonActivity.optJSONArray("material"));

                supervisor = null;
                jsonSupervisor = jsonActivity.optJSONObject("supervisor");
                if (jsonSupervisor != null) {
                    supervisorId = jsonSupervisor.optInt("id");
                    supervisorName = optString(jsonSupervisor, "name");
                    supervisorAvatar = optString(jsonSupervisor, "avatar");
                    supervisor = new Mate(supervisorId, supervisorName, supervisorAvatar);
                }

                results.add(new Activity(id, state, type, date, dateEnd, title, content, client,
                        hour, hourEnd, start, price, guarantee, place, address, supervisor,
                        material, team));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            results.clear();
        }
        return results;
    }

    /**
     * This function simply return the String from the JSONObject and according to the key.
     * If there is no value, it will return a null.
     *
     * @param jsonObject : The JSON Object from which we want to extract the value.
     * @param key        : The key where we want to extract the value.
     * @return String : The value extracted from the JSON Object.
     */
    private static String optString(JSONObject jsonObject, String key) {
        if (jsonObject.isNull(key)) {
            return null;
        } else {
            return jsonObject.optString(key, null);
        }
    }

}
