package fr.sonoeseo.sonoapp.controller.calendar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fr.sonoeseo.sonoapp.R;
import fr.sonoeseo.sonoapp.api.APIClient;
import fr.sonoeseo.sonoapp.api.APIConstants;
import fr.sonoeseo.sonoapp.api.APICore;
import fr.sonoeseo.sonoapp.api.APIDecoder;
import fr.sonoeseo.sonoapp.content.Content;
import fr.sonoeseo.sonoapp.controller.activities.leasing.LeasingActivity;
import fr.sonoeseo.sonoapp.controller.activities.meeting.MeetingActivity;
import fr.sonoeseo.sonoapp.controller.activities.service.ServiceActivity;
import fr.sonoeseo.sonoapp.controller.common.ItemInterface;
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

public class CalendarFragment extends Fragment implements ItemInterface, OnDateSelectedListener {

    ArrayList<Activity> results = new ArrayList<>();
    MaterialCalendarView calendar;
    CalendarAdapter calendarAdapter;
    TextView txtResult;
    Context ctx;

    /**
     * It will first create the fragment with the corresponding layout.
     * Then it will initialize the recyclerview and calendar.
     * <p>
     * Finally it will search the activities from the cache file and if the activities were not
     * loaded from the API since the launch of the application it then call the API.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View calendarView = inflater.inflate(R.layout.layout_calendar,
                container, false);
        ctx = calendarView.getContext();

        txtResult = calendarView.findViewById(R.id.txtResult);

        calendar = calendarView.findViewById(R.id.calendar);
        calendar.setOnDateChangedListener(this);

        if (Content.activities.isEmpty()) {
            loadCore();
        } else {
            calendar.setSelectedDate(new Date());
            refreshCalendarColors();
        }
        if (!APIConstants.ACTIVITIES_LOADED) {
            Load load = new Load();
            load.execute();
        }

        RecyclerView results = calendarView.findViewById(R.id.results);
        results.setLayoutManager(new LinearLayoutManager(ctx));
        calendarAdapter = new CalendarAdapter(ctx, this);
        results.setAdapter(calendarAdapter);

        onDateSelected(calendar, calendar.getSelectedDate(), true);

        return calendarView;
    }

    /**
     * This function is override from the ItemInterface.
     *
     * @param position : The position of the selected activity from the list.
     */
    @Override
    public void onItemClick(int position) {
        Content.activity = results.get(position);

        Intent intent;
        switch (results.get(position).getType()) {
            case leasing:
                intent = new Intent(ctx, LeasingActivity.class);
                break;
            case service:
                intent = new Intent(ctx, ServiceActivity.class);
                break;
            default:
                intent = new Intent(ctx, MeetingActivity.class);
                break;
        }
        startActivity(intent);
    }

    /**
     * When a date is selected it will refresh the results list.
     */
    @Override
    public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView,
                               @Nullable CalendarDay date, boolean selected) {
        results.clear();

        for (Activity activity : Content.activities) {
            if (date != null) {
                if (activity.getType() != ActivityType.leasing && (activity.getDate().equals(date.getDate())
                        || activity.getDateEnd().equals(date.getDate()) || (activity.getDate().before(date.getDate())
                        && activity.getDateEnd().after(date.getDate())))) {
                    results.add(activity);
                }
                if (activity.getType() == ActivityType.leasing && (activity.getDate().equals(date.getDate())
                        || activity.getDateEnd().equals(date.getDate()))) {
                    results.add(activity);
                }
            }
        }

        // Update result answer
        String resultString = ctx.getString(R.string.noResults);
        if (results.size() > 0) {
            if (results.size() == 1) {
                resultString = results.size() + " " + ctx.getString(R.string.result);
            } else {
                resultString = results.size() + " " + ctx.getString(R.string.results);
            }
        }
        if (txtResult != null) {
            txtResult.setText(resultString);
        }

        if (calendarAdapter != null) {
            calendarAdapter.setResults(results);
        }
    }

    /**
     * This function will return a list of dates which are between the start and end dates given.
     *
     * @param start : The start date.
     * @param end   : The end date.
     * @return The list of dates.
     */
    private List<Date> getDatesBetween(Date start, Date end) {
        Date cursor = start;

        Calendar cal = Calendar.getInstance();
        List<Date> results = new ArrayList<>();
        while (!cursor.equals(end)) {
            results.add(cursor);

            cal.setTime(cursor);
            cal.add(Calendar.DATE, 1);
            cursor = cal.getTime();
        }
        if (!results.contains(end)) {
            results.add(end);
        }
        return results;
    }

    /**
     * This function will put colors on the calendar for each date when there are rentals or return
     * rentals and all the intervals for which there is a meeting or a service.
     */
    private void refreshCalendarColors() {
        ArrayList<Date> dates = new ArrayList<>();
        for (Activity activity : Content.activities) {
            if (activity.getType() == ActivityType.service || activity.getType() == ActivityType.meeting
                    && (activity.getDate() != null && activity.getDateEnd() != null)) {
                List<Date> list = getDatesBetween(activity.getDate(), activity.getDateEnd());
                for (Date date : list) {
                    dates.add(date);
                }
            } else {
                if (activity.getDate() != null) {
                    dates.add(activity.getDate());
                }
                if (activity.getDateEnd() != null) {
                    dates.add(activity.getDateEnd());
                }
            }
        }
        calendar.addDecorator(new CalendarDecorator(dates));
    }

    /**
     * Refresh the list of activities from the cache file.
     * <p>
     * Then it will refresh the results and the colors on the calendar.
     */
    private void loadCore() {
        String activitiesResult = APICore.getInstance().read(ctx, APIConstants.CORE_ACTIVITIES);
        if (!activitiesResult.isEmpty()) {
            Content.activities = APIDecoder.extractActivities(activitiesResult);

            calendar.setSelectedDate(new Date());
            refreshCalendarColors();
        }
    }

    /**
     * This AsyncTask will refresh the activities data by calling the API.
     * <p>
     * It will then refresh the colors on the calendars.
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

                calendar.setSelectedDate(new Date());
                refreshCalendarColors();
                APIConstants.ACTIVITIES_LOADED = true;
            }
        }
    }
}