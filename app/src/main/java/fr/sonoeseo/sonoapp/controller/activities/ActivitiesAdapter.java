package fr.sonoeseo.sonoapp.controller.activities;

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

public class ActivitiesAdapter extends SectionedRecyclerViewAdapter<ActivitiesAdapter.ViewHolder> {

    public Context ctx;
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
    ActivitiesAdapter(Context ctx, ActivityInterface activityInterface) {
        this.ctx = ctx;
        this.activityInterface = activityInterface;
    }

    /**
     * This function will refresh the list of sections and activities in the local variable.
     *
     * @param activities : The list of activities to be shown.
     */
    void setActivities(List<String> sections,
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
     *
     * @param holder           : The holder of the elements on the view.
     * @param section          : The position in the sections array.
     * @param relativePosition : The position in the section.
     * @param absolutePosition : The absolute position in the recyclerView.
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
            String dateString = new SimpleDateFormat("EEEE d MMMM yyyy").format(activity.getDate());
            holder.txtDate.setText(dateString);
        }

        holder.txtContent.setText(ctx.getString(R.string.emptyField));
        if (activity.getContent() != null) {
            holder.txtContent.setText(activity.getContent());
        }

        switch (activity.getType()) {
            case leasing:
                holder.imgGradient.setImageResource(R.drawable.gradient_leasing);
                holder.imgIcon.setImageResource(R.drawable.ic_leasing);
                if (activity.getDateEnd() != null) {
                    String dateString = new SimpleDateFormat("EEEE d MMMM yyyy").format(activity.getDateEnd());
                    holder.txtContent.setText(ctx.getString(R.string.back) + " " + dateString);
                }
                break;
            case service:
                holder.imgGradient.setImageResource(R.drawable.gradient_service);
                holder.imgIcon.setImageResource(R.drawable.ic_services);
                break;
            default:
                holder.imgGradient.setImageResource(R.drawable.gradient_meeting);
                holder.imgIcon.setImageResource(R.drawable.ic_meeting);
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
                layoutRes = R.layout.item_activity;
                break;
        }
        View v = LayoutInflater.from(parent.getContext()).inflate(layoutRes,
                parent, false);
        return new ViewHolder(v, viewType);
    }

    class ViewHolder extends SectionedViewHolder implements View.OnClickListener {

        ImageView imgGradient, imgIcon;
        TextView txtTitle, txtDate, txtContent, txtSection;

        ViewHolder(View view, int viewType) {
            super(view);
            if (viewType == VIEW_TYPE_HEADER) {
                txtSection = view.findViewById(R.id.txtSection);
            } else {
                imgGradient = view.findViewById(R.id.imgGradient);
                imgIcon = view.findViewById(R.id.imgIcon);

                txtTitle = view.findViewById(R.id.txtTitle);
                txtDate = view.findViewById(R.id.txtDate);
                txtContent = view.findViewById(R.id.txtContent);

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