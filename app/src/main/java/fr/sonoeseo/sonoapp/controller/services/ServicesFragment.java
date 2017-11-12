package fr.sonoeseo.sonoapp.controller.services;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import fr.sonoeseo.sonoapp.R;
import fr.sonoeseo.sonoapp.api.APIClient;
import fr.sonoeseo.sonoapp.api.APIConstants;
import fr.sonoeseo.sonoapp.api.APICore;
import fr.sonoeseo.sonoapp.api.APIDecoder;
import fr.sonoeseo.sonoapp.content.Content;
import fr.sonoeseo.sonoapp.controller.activities.common.ActivityInterface;
import fr.sonoeseo.sonoapp.models.Activity;
import fr.sonoeseo.sonoapp.models.ActivityType;

/**
 * Created by sonasi on 01/07/2017.
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

public class ServicesFragment extends Fragment implements ActivityInterface, ServiceInterface {

    List<String> sections = new ArrayList<>();
    ServicesAdapter servicesAdapter;
    Context ctx;
    private HashMap<String, ArrayList<Activity>> services = new HashMap<>();

    /**
     * It will first create the fragment with the corresponding layout.
     * Then it will initialize the recyclerview.
     * <p>
     * Finally it will search the activities from the cache file and if the activities were not
     * loaded from the API since the launch of the application it then call the API.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View servicesView = inflater.inflate(R.layout.layout_services, container, false);
        ctx = servicesView.getContext();

        RecyclerView recyclerView = servicesView.findViewById(R.id.services);
        recyclerView.setLayoutManager(new LinearLayoutManager(ctx));
        servicesAdapter = new ServicesAdapter(ctx, this);
        recyclerView.setAdapter(servicesAdapter);

        if (Content.activities.isEmpty()) {
            loadCore();
        } else {
            getSections();
        }
        if (!APIConstants.ACTIVITIES_LOADED) {
            Load load = new Load();
            load.execute();
        }

        return servicesView;
    }

    /*
     * This function generate the sections and set activities in the corresponding sections.
     */
    private void getSections() {
        sections.clear();
        services.clear();

        // Then we generate our array with the section as key and services as values.
        String section;
        for (Activity activity : Content.activities) {
            if (activity.getType() == ActivityType.service && (activity.getDate() != null)
                    && (activity.getDate().equals(new Date()) ||
                    activity.getDate().after(new Date()))) {
                section = new SimpleDateFormat("MMMM yyyy").format(activity.getDate())
                        .toUpperCase();
                if (!services.containsKey(section)) {
                    services.put(section, new ArrayList<Activity>());
                }
                services.get(section).add(activity);
            }
        }

        for (ArrayList<Activity> list : services.values()) {
            // Sort by ascending date.
            Collections.sort(list, new Comparator<Activity>() {
                public int compare(Activity a1, Activity a2) {
                    return a1.getDate().compareTo(a2.getDate());
                }
            });
        }

        sections = new ArrayList<>(services.keySet());
        Collections.sort(sections, new Comparator<String>() {
            public int compare(String section1, String section2) {
                Date date1 = new Date(), date2 = new Date();
                try {
                    date1 = new SimpleDateFormat("MMMM yyyy").parse(section1);
                    date2 = new SimpleDateFormat("MMMM yyyy").parse(section2);
                } catch (Exception e) {
                }
                return date1.compareTo(date2);
            }
        });

        servicesAdapter.setActivities(sections, services);
    }

    /**
     * This function override the ActivityInterace.
     *
     * @param section          : The section of the selected item.
     * @param relativePosition : The position in the section list.
     */
    @Override
    public void onActivityClick(String section, int relativePosition) {
        Content.activity = services.get(section).get(relativePosition);
        ServicePopup.getInstance().setServiceInterface(this);
        ServicePopup.getInstance().modal(ctx);
    }

    /**
     * This function override the ServiceInterface.
     * When the user choose unavailable or available on the popup,
     * this function will be executed.
     */
    @Override
    public void refresh() {
        servicesAdapter.setActivities(sections, services);
    }

    /**
     * Refresh the list of activities from the cache file.
     * <p>
     * Then it will refresh the recyclerView by getting the sections.
     */
    private void loadCore() {
        String activitiesResult = APICore.getInstance().read(ctx, APIConstants.CORE_ACTIVITIES);
        if (!activitiesResult.isEmpty()) {
            Content.activities = APIDecoder.extractActivities(activitiesResult);

            // Refresh local data.
            getSections();
        }
    }

    /**
     * This AsyncTask will refresh the activities data by calling the API.
     * <p>
     * It will then refresh the recyclerView by getting the sections.
     */
    private class Load extends android.os.AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String args = "login=" + Content.user.getLogin() + "&token=" + Content.user.getToken();
            if (APIClient.getInstance().isOnline(ctx)) {
                return APIClient.getInstance().request(ctx, APIConstants.API_ACTIVITIES, args);
            } else {
                return "";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (!result.isEmpty() && APIDecoder.extractStatus(result) != 2) {
                Content.activities = APIDecoder.extractActivities(result);
                APICore.getInstance().write(ctx, APIConstants.CORE_ACTIVITIES, result);

                // Refresh local data.
                getSections();
                APIConstants.ACTIVITIES_LOADED = true;
            }
        }
    }
}