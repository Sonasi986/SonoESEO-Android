package fr.sonoeseo.sonoapp.controller.articles;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Collections;
import java.util.Comparator;

import fr.sonoeseo.sonoapp.R;
import fr.sonoeseo.sonoapp.api.APIClient;
import fr.sonoeseo.sonoapp.api.APIConstants;
import fr.sonoeseo.sonoapp.api.APICore;
import fr.sonoeseo.sonoapp.api.APIDecoder;
import fr.sonoeseo.sonoapp.content.Content;
import fr.sonoeseo.sonoapp.controller.common.ItemInterface;
import fr.sonoeseo.sonoapp.models.Article;

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

public class ArticlesFragment extends Fragment implements ItemInterface {

    private ArticlesAdapter articlesAdapter;
    private Context ctx;

    /**
     * It will first create the fragment with the corresponding layout.
     * Then it will initialize the recyclerview.
     * <p>
     * Finally it will search the articles from the cache file and if the articles were not
     * loaded from the API since the launch of the application it then call the API.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View articlesView = inflater.inflate(R.layout.layout_articles, container, false);
        ctx = articlesView.getContext();

        RecyclerView recyclerView = articlesView.findViewById(R.id.articles);
        recyclerView.setLayoutManager(new LinearLayoutManager(ctx));
        articlesAdapter = new ArticlesAdapter(ctx, this);
        recyclerView.setAdapter(articlesAdapter);

        loadCore();
        if (!APIConstants.ARTICLES_LOADED) {
            Load load = new Load();
            load.execute();
        }

        return articlesView;
    }

    /**
     * This function is override from the ItemInterface.
     *
     * @param position : The position of the selected article from the list.
     */
    @Override
    public void onItemClick(int position) {
        Content.article = Content.articles.get(position);

        Intent intent = new Intent(getContext(), ArticleActivity.class);
        startActivity(intent);
    }

    /**
     * Refresh the list of articles from the cache file.
     * <p>
     * Then it will sort the articles list and refresh the recyclerView.
     */
    public void loadCore() {
        String articlesData = APICore.getInstance().read(ctx, APIConstants.CORE_ARTICLES);
        if (!articlesData.isEmpty()) {
            Content.articles = APIDecoder.extractArticles(articlesData);

            Collections.sort(Content.articles, new Comparator<Article>() {
                public int compare(Article a1, Article a2) {
                    return a2.getDate().compareTo(a1.getDate());
                }
            });
            articlesAdapter.notifyDataSetChanged();
        }
    }

    /**
     * This AsyncTask will refresh the articles data by calling the API.
     * <p>
     * It will then sort the articles list, refresh the recyclerView and
     * register the user to notifications.
     */
    public class Load extends android.os.AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String args = "login=" + Content.user.getLogin() + "&token=" + Content.user.getToken();
            if (APIClient.getInstance().isOnline(ctx)) {
                return APIClient.getInstance().request(ctx, APIConstants.API_ARTICLES, args);
            } else {
                return "";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (!result.isEmpty() && APIDecoder.extractStatus(result) != 2) {
                Content.articles = APIDecoder.extractArticles(result);
                APICore.getInstance().write(ctx, APIConstants.CORE_ARTICLES, result);

                Collections.sort(Content.articles, new Comparator<Article>() {
                    public int compare(Article a1, Article a2) {
                        return a2.getDate().compareTo(a1.getDate());
                    }
                });
                articlesAdapter.notifyDataSetChanged();
                APIConstants.ARTICLES_LOADED = true;

                FirebaseApp.initializeApp(ctx);
                APIClient.RegisterNotification registerNotification = new APIClient.RegisterNotification(ctx,
                        FirebaseInstanceId.getInstance().getToken());
                registerNotification.execute();
            }
        }
    }
}