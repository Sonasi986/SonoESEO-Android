package fr.sonoeseo.sonoapp.controller.common;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import fr.sonoeseo.sonoapp.R;
import fr.sonoeseo.sonoapp.api.APIConstants;
import fr.sonoeseo.sonoapp.models.Mate;

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

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.ViewHolder> {

    private Context ctx;
    private ArrayList<Mate> team;

    /**
     * The constructor of the TeamAdapter.
     *
     * @param ctx  : The current context.
     * @param team : The ArrayList of mates which will be displayed.
     */
    public TeamAdapter(Context ctx, ArrayList<Mate> team) {
        this.ctx = ctx;
        this.team = team;
    }

    /**
     * This function will create a view with the corresponding layout.
     */
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mate,
                parent, false);
        return new ViewHolder(itemView);
    }

    /**
     * This function fill the layout with the correct values.
     *
     * @param holder   : The holder of the elements on the view.
     * @param position : The position in the team array.
     */
    public void onBindViewHolder(ViewHolder holder, int position) {
        String link = APIConstants.SONO_AVATAR + team.get(position).getAvatar();
        Picasso.with(ctx).load(link).placeholder(R.drawable.ic_user).error(R.drawable.ic_user)
                .into(holder.avatar);

        holder.name.setText(ctx.getString(R.string.emptyField));
        if (team.get(position).getName() != null) {
            holder.name.setText(team.get(position).getName());
        }
    }

    @Override
    public int getItemCount() {
        return team.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        ImageView avatar;

        ViewHolder(View view) {
            super(view);

            name = view.findViewById(R.id.name);
            avatar = view.findViewById(R.id.avatar);
        }
    }
}