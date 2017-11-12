package fr.sonoeseo.sonoapp.controller.activities.material;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import fr.sonoeseo.sonoapp.R;
import fr.sonoeseo.sonoapp.models.Material;

/**
 * Created by sonasi on 05/07/2017.
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

public class MaterialAdapter extends RecyclerView.Adapter<MaterialAdapter.ViewHolder> {

    private ArrayList<Material> material;
    private Context ctx;

    /**
     * The constructor of the ArticlesAdapter.
     *
     * @param ctx      : The current context.
     * @param material : The ArrayList of materials which will be displayed.
     */
    MaterialAdapter(Context ctx, ArrayList<Material> material) {
        this.ctx = ctx;
        this.material = material;
    }

    /**
     * This function will create a view with the corresponding layout.
     */
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_material,
                parent, false);

        return new ViewHolder(itemView);
    }

    /**
     * This function fill the layout with the correct values.
     *
     * @param holder   : The holder of the elements on the view.
     * @param position : The position in the sections array.
     */
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtName.setText(ctx.getString(R.string.emptyField));
        if (material.get(position).getName() != null) {
            holder.txtName.setText(material.get(position).getName());
        }

        String quantity = "x " + material.get(position).getQuantity();
        holder.txtQuantity.setText(quantity);

        if (material.get(position).getSelected() == 0) {
            holder.content.setBackgroundColor(ContextCompat.getColor(ctx, R.color.white));
        } else if (material.get(position).getSelected() == 1) {
            holder.content.setBackgroundColor(ContextCompat.getColor(ctx, R.color.green));
        } else {
            holder.content.setBackgroundColor(ContextCompat.getColor(ctx, R.color.red));
        }
    }

    @Override
    public int getItemCount() {
        return material.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView txtName, txtQuantity;
        private CardView content;

        ViewHolder(View view) {
            super(view);

            txtName = view.findViewById(R.id.txtName);
            txtQuantity = view.findViewById(R.id.txtQuantity);

            content = view.findViewById(R.id.content);
            content.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Material materialItem = material.get(getAdapterPosition());
            if (materialItem.getSelected() == 0) {
                view.setBackgroundColor(ContextCompat.getColor(ctx, R.color.blue));
                materialItem.setSelected(1);
            } else if (materialItem.getSelected() == 1) {
                view.setBackgroundColor(ContextCompat.getColor(ctx, R.color.red));
                materialItem.setSelected(2);
            } else {
                view.setBackgroundColor(ContextCompat.getColor(ctx, R.color.white));
                materialItem.setSelected(0);
            }
        }
    }
}
