package fr.sonoeseo.sonoapp.controller.calendar;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import fr.sonoeseo.sonoapp.R;
import fr.sonoeseo.sonoapp.controller.common.ItemInterface;
import fr.sonoeseo.sonoapp.models.Activity;

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

class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {

    private Context ctx;
    private ItemInterface itemInterface;
    private ArrayList<Activity> activities = new ArrayList<>();

    /**
     * The constructor of the CalendarAdapter.
     *
     * @param ctx           : The current context.
     * @param itemInterface : The interface which will be used to return the selected item in
     *                      the recyclerview.
     */
    CalendarAdapter(Context ctx, ItemInterface itemInterface) {
        this.ctx = ctx;
        this.itemInterface = itemInterface;
    }

    /**
     * This function will refresh the list of activities in the local variable.
     *
     * @param activities : The list of activities to be shown.
     */
    void setResults(ArrayList<Activity> activities) {
        this.activities = activities;
        notifyDataSetChanged();
    }

    /**
     * This function will create a view with the corresponding layout.
     */
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity,
                parent, false);
        return new ViewHolder(itemView);
    }

    /**
     * This function fill the layout with the correct values.
     *
     * @param holder   : The holder of the elements on the view.
     * @param position : The position in the activities array.
     */
    public void onBindViewHolder(ViewHolder holder, int position) {
        Activity activity = activities.get(position);

        holder.txtTitle.setText(ctx.getString(R.string.emptyField));
        if (activity.getTitle() != null) {
            holder.txtTitle.setText(activity.getTitle());
        }

        String dateString = ctx.getString(R.string.emptyField);
        if (activity.getDate() != null) {
            dateString = new SimpleDateFormat("EEEE d MMMM yyyy")
                    .format(activity.getDate());
        }
        holder.txtDate.setText(dateString);

        holder.txtContent.setText(ctx.getString(R.string.emptyField));
        if (activity.getContent() != null) {
            holder.txtContent.setText(activity.getContent());
        }

        // If the type is a leasing there is little case that there is a comment then
        // we will put the rental return date.
        switch (activity.getType()) {
            case leasing:
                holder.imgGradient.setImageResource(R.drawable.gradient_leasing);
                holder.imgIcon.setImageResource(R.drawable.ic_leasing);
                if (activity.getDateEnd() != null) {
                    String dateEndString = new SimpleDateFormat("EEEE d MMMM yyyy").format(activity.getDateEnd());
                    holder.txtContent.setText(ctx.getString(R.string.back) + " " + dateEndString);
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

    @Override
    public int getItemCount() {
        return activities.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imgGradient, imgIcon;
        TextView txtTitle, txtDate, txtContent;

        ViewHolder(View view) {
            super(view);

            imgGradient = view.findViewById(R.id.imgGradient);
            imgIcon = view.findViewById(R.id.imgIcon);

            txtTitle = view.findViewById(R.id.txtTitle);
            txtDate = view.findViewById(R.id.txtDate);
            txtContent = view.findViewById(R.id.txtContent);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemInterface.onItemClick(getAdapterPosition());
        }
    }
}