package fr.sonoeseo.sonoapp.controller.articles;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import fr.sonoeseo.sonoapp.R;
import fr.sonoeseo.sonoapp.api.APIConstants;
import fr.sonoeseo.sonoapp.content.Content;
import fr.sonoeseo.sonoapp.controller.common.ItemInterface;
import fr.sonoeseo.sonoapp.models.Article;

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

class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ViewHolder> {

    private ItemInterface itemInterface;
    private Context ctx;

    /**
     * The constructor of the ArticlesAdapter.
     *
     * @param ctx           : The current context.
     * @param itemInterface : The interface which will be used to return the selected item in
     *                      the recyclerview.
     */
    ArticlesAdapter(Context ctx, ItemInterface itemInterface) {
        this.ctx = ctx;
        this.itemInterface = itemInterface;
    }

    /**
     * This function will create a view with the corresponding layout.
     */
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article,
                parent, false);
        return new ViewHolder(itemView);
    }

    /**
     * This function fill the layout with the correct values.
     *
     * @param holder : The holder of the elements on the view.
     */
    public void onBindViewHolder(ViewHolder holder, int position) {
        Article article = Content.articles.get(position);

        String link = APIConstants.SONO_AVATAR + article.getAuthorAvatar();
        Picasso.with(ctx).load(link).placeholder(R.drawable.ic_user)
                .error(R.drawable.ic_user).into(holder.imgAvatar);

        holder.txtName.setText(ctx.getString(R.string.emptyField));
        if (article.getAuthor() != null) {
            holder.txtName.setText(article.getAuthor());
        }

        holder.txtType.setText(ctx.getString(R.string.emptyField));
        if (article.getType() != null) {
            holder.txtType.setText(article.getType());
        }

        holder.txtTitle.setText(ctx.getString(R.string.emptyField));
        if (article.getTitle() != null) {
            holder.txtTitle.setText(article.getTitle());
        }

        holder.txtContent.setText(ctx.getString(R.string.emptyField));
        if (article.getContent() != null) {
            holder.txtContent.setText(article.getContent());
        }
    }

    @Override
    public int getItemCount() {
        return Content.articles.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imgAvatar;
        TextView txtName, txtType, txtTitle, txtContent;

        ViewHolder(View view) {
            super(view);

            imgAvatar = view.findViewById(R.id.imgAvatar);

            txtName = view.findViewById(R.id.txtName);
            txtType = view.findViewById(R.id.txtType);
            txtTitle = view.findViewById(R.id.txtTitle);
            txtContent = view.findViewById(R.id.txtContent);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemInterface.onItemClick(getAdapterPosition());
        }
    }
}
