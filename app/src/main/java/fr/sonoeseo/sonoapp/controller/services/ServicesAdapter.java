package fr.sonoeseo.sonoapp.controller.services;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.sectionedrecyclerview.ItemCoord;
import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.afollestad.sectionedrecyclerview.SectionedViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fr.sonoeseo.sonoapp.R;
import fr.sonoeseo.sonoapp.controller.activities.common.ActivityInterface;
import fr.sonoeseo.sonoapp.models.Activity;

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

public class ServicesAdapter extends SectionedRecyclerViewAdapter<ServicesAdapter.ViewHolder> {

    private Context ctx;
    private ActivityInterface activityInterface;
    private List<String> sections = new ArrayList<>();
    private HashMap<String, ArrayList<Activity>> activities = new HashMap<>();

    /**
     * The constructor of the CalendarAdapter.
     *
     * @param ctx               : The current context.
     * @param activityInterface : The interface which will be used to return the selected item in
     *                          the recyclerview.
     */
    public ServicesAdapter(Context ctx, ActivityInterface activityInterface) {
        this.ctx = ctx;
        this.activityInterface = activityInterface;
    }

    /**
     * This function will refresh the list of sections and activities in the local variable.
     *
     * @param sections   : The list of sections for the recyclerView.
     * @param activities : The hashmap of activities according to sections.
     */
    public void setActivities(List<String> sections,
                              HashMap<String, ArrayList<Activity>> activities) {
        this.sections = sections;
        this.activities = activities;

        notifyDataSetChanged();
    }

    @Override
    public int getSectionCount() {
        return sections.size();
    }

    @Override
    public int getItemCount(int sectionIndex) {
        return activities.get(sections.get(sectionIndex)).size();
    }

    /**
     * This function fill the layout with the correct values.
     */
    @Override
    public void onBindHeaderViewHolder(ViewHolder holder, int section, boolean expanded) {
        holder.txtSection.setText(sections.get(section).toUpperCase());
    }

    @Override
    public void onBindFooterViewHolder(ViewHolder holder, int section) {
    }

    /**
     * This function fill the layout with the correct values.
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int section, int relativePosition, int absolutePosition) {
        Activity activity = activities.get(sections.get(section)).get(relativePosition);

        holder.txtTitle.setText(ctx.getString(R.string.emptyField));
        if (activity.getTitle() != null) {
            holder.txtTitle.setText(activity.getTitle());
        }

        holder.txtDate.setText(ctx.getString(R.string.emptyField));
        if (activity.getDate() != null) {
            String dateString = new SimpleDateFormat("dd").format(activity.getDate());
            holder.txtDate.setText(dateString);
        }

        holder.txtDay.setText(ctx.getString(R.string.emptyField));
        if (activity.getContent() != null) {
            String dayString = new SimpleDateFormat("EEE").format(activity.getDate());
            holder.txtDay.setText(dayString.toUpperCase());
        }

        holder.txtPlace.setText(ctx.getString(R.string.emptyField));
        if (activity.getPlace() != null) {
            holder.txtPlace.setText(activity.getPlace());
        }

        switch (activity.getState()) {
            case 1:
                holder.imgStatus.setImageResource(R.drawable.ic_valide_1);
                break;
            case 2:
                holder.imgStatus.setImageResource(R.drawable.ic_valide_2);
                break;
            case 3:
                holder.imgStatus.setImageResource(R.drawable.ic_valide_3);
                break;
            default:
                holder.imgStatus.setImageResource(R.drawable.ic_valide_0);
                break;
        }
    }

    /**
     * This function will create a view with the corresponding layout.
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Change inflated layout based on type
        int layoutRes;
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                layoutRes = R.layout.item_section;
                break;
            default:
                layoutRes = R.layout.item_service;
                break;
        }
        View v = LayoutInflater.from(parent.getContext()).inflate(layoutRes,
                parent, false);
        return new ViewHolder(v, viewType);
    }

    class ViewHolder extends SectionedViewHolder implements View.OnClickListener {

        ImageView imgStatus;
        TextView txtTitle, txtDay, txtDate, txtPlace, txtSection;

        ViewHolder(View view, int viewType) {
            super(view);
            if (viewType == VIEW_TYPE_HEADER) {
                txtSection = view.findViewById(R.id.txtSection);
            } else {
                imgStatus = view.findViewById(R.id.imgStatus);

                txtTitle = view.findViewById(R.id.txtTitle);
                txtDay = view.findViewById(R.id.txtDay);
                txtDate = view.findViewById(R.id.txtDate);
                txtPlace = view.findViewById(R.id.txtPlace);

                view.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View view) {
            if (!isHeader() && !isFooter()) {
                ItemCoord position = getRelativePosition();
                activityInterface.onActivityClick(sections.get(position.section()),
                        getRelativePosition().relativePos());
            }
        }
    }
}