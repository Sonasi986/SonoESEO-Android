package fr.sonoeseo.sonoapp.controller.calendar;

import android.graphics.Color;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sonasi on 02/11/2017.
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

public class CalendarDecorator implements DayViewDecorator {

    private ArrayList<CalendarDay> dates = new ArrayList<>();

    /**
     * The constructor of the CalendarDecorator.
     *
     * @param dates : The list of dates.
     */
    public CalendarDecorator(ArrayList<Date> dates) {
        for (Date date : dates) {
            this.dates.add(CalendarDay.from(date));
        }
    }

    /**
     * This function will decorate the date according the given parameter.
     *
     * @param day : The day to decorate.
     * @return If the day in parameter is in the array.
     */
    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    /**
     * This function decorate the view.
     *
     * @param view : The view to decorate.
     */
    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan(7, Color.rgb(253, 160, 96)));
    }
}