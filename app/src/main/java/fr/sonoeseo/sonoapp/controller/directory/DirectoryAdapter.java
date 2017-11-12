package fr.sonoeseo.sonoapp.controller.directory;

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
import fr.sonoeseo.sonoapp.controller.common.ItemInterface;
import fr.sonoeseo.sonoapp.models.Mate;

/**
 * Created by sonasi on 03/07/2017.
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

class DirectoryAdapter extends RecyclerView.Adapter<DirectoryAdapter.ViewHolder> {

    private ArrayList<Mate> results;
    private ItemInterface itemInterface;
    private Context ctx;

    /**
     * The constructor of the DirectoryAdapter.
     *
     * @param ctx           : The current context.
     * @param itemInterface : The interface which will be used to return the selected item in
     *                      the recyclerview.
     */
    DirectoryAdapter(Context ctx, ItemInterface itemInterface) {
        this.ctx = ctx;
        this.itemInterface = itemInterface;
    }

    /**
     * This function will refresh the list of mates in the local variable.
     *
     * @param results : The list of mates to be shown.
     */
    public void setResults(ArrayList<Mate> results) {
        this.results = results;
        notifyDataSetChanged();
    }

    /**
     * This function will create a view with the corresponding layout.
     */
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_directory,
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
        Mate mate = results.get(position);

        String link = APIConstants.SONO_AVATAR + mate.getAvatar();
        Picasso.with(ctx).load(link).placeholder(R.drawable.ic_user).error(R.drawable.ic_user)
                .into(holder.imgAvatar);

        holder.txtName.setText(ctx.getString(R.string.emptyField));
        if (mate.getName() != null) {
            holder.txtName.setText(mate.getName());
        }

        holder.txtPhone.setText(ctx.getString(R.string.emptyField));
        if (mate.getPhone() != null) {
            holder.txtPhone.setText(mate.getPhone());
        }
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imgAvatar;
        TextView txtName, txtPhone;

        ViewHolder(View view) {
            super(view);

            imgAvatar = view.findViewById(R.id.imgAvatar);
            txtName = view.findViewById(R.id.txtName);
            txtPhone = view.findViewById(R.id.txtPhone);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemInterface.onItemClick(getAdapterPosition());
        }
    }
}