package fr.sonoeseo.sonoapp.controller.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Switch;

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
import fr.sonoeseo.sonoapp.controller.activities.leasing.LeasingActivity;
import fr.sonoeseo.sonoapp.controller.activities.meeting.MeetingActivity;
import fr.sonoeseo.sonoapp.controller.activities.service.ServiceActivity;
import fr.sonoeseo.sonoapp.models.Activity;

import static fr.sonoeseo.sonoapp.models.ActivityType.leasing;
import static fr.sonoeseo.sonoapp.models.ActivityType.meeting;
import static fr.sonoeseo.sonoapp.models.ActivityType.service;

/**
 * Created by sonasi on 02/07/2017.
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

public class ActivitiesFragment extends Fragment implements ActivityInterface, View.OnClickListener {

    private HashMap<String, ArrayList<Activity>> activities = new HashMap<>();
    private List<String> sections = new ArrayList<>();
    private ActivitiesAdapter activitiesAdapter;
    private Context ctx;

    /**
     * It will first create the fragment with the corresponding layout.
     * Then it will initialize the recyclerview and calendar.
     * <p>
     * Finally it will search the activities from the cache file and if the activities were not
     * loaded from the API since the launch of the application it then call the API.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View activitiesView = inflater.inflate(R.layout.layout_activities,
                container, false);
        ctx = activitiesView.getContext();

        FloatingActionButton fab = activitiesView.findViewById(R.id.filter);
        fab.setOnClickListener(this);

        RecyclerView recyclerView = activitiesView.findViewById(R.id.activities);
        recyclerView.setLayoutManager(new LinearLayoutManager(ctx));
        activitiesAdapter = new ActivitiesAdapter(ctx, this);
        activitiesAdapter.shouldShowFooters(false);
        recyclerView.setAdapter(activitiesAdapter);

        if (Content.activities.isEmpty()) {
            loadCore();
        } else {
            sort();
        }
        if (!APIConstants.ACTIVITIES_LOADED) {
            Load load = new Load();
            load.execute();
        }

        return activitiesView;
    }

    /**
     * This function override the ActivityInterace.
     *
     * @param section          : The section of the selected item.
     * @param relativePosition : The position in the section list.
     */
    @Override
    public void onActivityClick(String section, int relativePosition) {
        Content.activity = activities.get(section).get(relativePosition);
        Intent intent;
        switch (Content.activity.getType()) {
            case service:
                intent = new Intent(ctx, ServiceActivity.class);
                break;
            case leasing:
                intent = new Intent(ctx, LeasingActivity.class);
                break;
            default:
                intent = new Intent(ctx, MeetingActivity.class);
                break;
        }
        startActivity(intent);
    }

    /**
     * This function will simply sort the activities :
     * <p>
     * If the results are categorized, it will first get sections according to categories
     * and then it will sort activities in sections?
     * <p>
     * If the results are uncategorized, it will create sections according to MONTH YEAR
     * and then it will sort sections by ascending/descending dates and the same for
     * activities for sections.
     */
    public void sort() {
        sections.clear();
        activities.clear();

        String section = "";
        if (Content.categorized) {
            for (Activity activity : Content.activities) {
                if (activity.getType() == service && !activities.containsKey(service.name())) {
                    section = service.name();
                    activities.put(section, new ArrayList<Activity>());
                }
                if (activity.getType() == leasing && !activities.containsKey(leasing.name())) {
                    section = leasing.name();
                    activities.put(section, new ArrayList<Activity>());
                }
                if (activity.getType() == meeting && !activities.containsKey(meeting.name())) {
                    section = meeting.name();
                    activities.put(section, new ArrayList<Activity>());
                }
                section = activity.getType().name();
                activities.get(section).add(activity);
            }
            sections = new ArrayList<>(activities.keySet());
        } else {
            for (Activity activity : Content.activities) {
                if (activity.getDate() != null) {
                    section = new SimpleDateFormat("MMMM yyyy").format(activity.getDate());
                    if (!activities.containsKey(section)) {
                        activities.put(section, new ArrayList<Activity>());
                    }
                    activities.get(section).add(activity);
                }
            }

            sections = new ArrayList<>(activities.keySet());
            if (Content.ascendingSort) {
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
            } else {
                Collections.sort(sections, new Comparator<String>() {
                    public int compare(String section1, String section2) {
                        Date date1 = new Date(), date2 = new Date();
                        try {
                            date1 = new SimpleDateFormat("MMMM yyyy").parse(section1);
                            date2 = new SimpleDateFormat("MMMM yyyy").parse(section2);
                        } catch (Exception e) {
                        }
                        return date2.compareTo(date1);
                    }
                });
            }
        }

        if (Content.ascendingSort) {
            for (ArrayList<Activity> list : activities.values()) {
                Collections.sort(list, new Comparator<Activity>() {
                    public int compare(Activity a1, Activity a2) {
                        return a1.getDate().compareTo(a2.getDate());
                    }
                });
            }
        } else {
            for (ArrayList<Activity> list : activities.values()) {
                Collections.sort(list, new Comparator<Activity>() {
                    public int compare(Activity a1, Activity a2) {
                        return a2.getDate().compareTo(a1.getDate());
                    }
                });
            }
        }

        activitiesAdapter.setActivities(sections, activities);
    }

    /**
     * On click on the sort button.
     * It will open a dialog with two switch to sort the list of categories or not and by ascending
     * dates or not.
     */
    @Override
    public void onClick(View v) {
        final Dialog dialog = new Dialog(ctx);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_sort);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        final Switch switchCategorized = dialog.findViewById(R.id.switchCategorized);
        final Switch switchAscending = dialog.findViewById(R.id.switchAscending);

        switchCategorized.setChecked(Content.categorized);
        switchAscending.setChecked(Content.ascendingSort);

        Button validate = dialog.findViewById(R.id.validate);
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Content.categorized = switchCategorized.isChecked();
                Content.ascendingSort = switchAscending.isChecked();
                sort();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    /**
     * Refresh the list of activities from the cache file.
     * <p>
     * Then it will refresh the activities and sort the list to generate sections.
     */
    private void loadCore() {
        String directoryResult = APICore.getInstance().read(ctx, APIConstants.CORE_ACTIVITIES);
        if (!directoryResult.isEmpty()) {
            Content.activities = APIDecoder.extractActivities(directoryResult);

            // Refresh local data.
            sort();
        }
    }

    /**
     * This AsyncTask will refresh the activities data by calling the API.
     * <p>
     * It will then refresh the sections and activities in the recyclerView..
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
                sort();
                APIConstants.ACTIVITIES_LOADED = true;
            }
        }
    }
}