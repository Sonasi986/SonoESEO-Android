package fr.sonoeseo.sonoapp.controller.articles;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

import fr.sonoeseo.sonoapp.R;
import fr.sonoeseo.sonoapp.api.APIConstants;
import fr.sonoeseo.sonoapp.content.Content;

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

public class ArticleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_article);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

            actionBar.setTitle(getString(R.string.emptyField));
            if (Content.article.getTitle() != null) {
                actionBar.setTitle(Content.article.getTitle());
            }
        }

        ImageView imgAvatar = (ImageView) findViewById(R.id.imgAvatar);
        String link = APIConstants.SONO_AVATAR + Content.article.getAuthorAvatar();
        Picasso.with(this).load(link).placeholder(R.drawable.ic_user).error(R.drawable.ic_user)
                .into(imgAvatar);

        TextView txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText(getString(R.string.emptyField));
        if (Content.article.getTitle() != null) {
            txtTitle.setText(Content.article.getTitle());
        }

        TextView txtAuthor = (TextView) findViewById(R.id.txtAuthor);
        txtAuthor.setText(getString(R.string.emptyField));
        if (Content.article.getAuthor() != null) {
            txtAuthor.setText(Content.article.getAuthor());
        }

        TextView txtDate = (TextView) findViewById(R.id.txtDate);
        String dateString = getString(R.string.emptyField);
        if (Content.article.getDate() != null) {
            dateString = new SimpleDateFormat("EEEE d MMMM yyyy")
                    .format(Content.article.getDate());
        }
        txtDate.setText(dateString);

        WebView wvContent = (WebView) findViewById(R.id.wvContent);
        wvContent.setBackgroundColor(Color.TRANSPARENT);
        wvContent.setHapticFeedbackEnabled(false);
        wvContent.getSettings().setDefaultFontSize(15);
        wvContent.setClickable(false);
        wvContent.setLongClickable(false);
        wvContent.getSettings().setJavaScriptEnabled(false);
        wvContent.loadData("" +
                        "<body style=\"text-align: justify;\">" + Content.article.getContent()
                        .replace("\n", "<br/>") + "</body>",
                "text/html; charset=UTF-8", null);
    }

    /**
     * This function is simply allowing us to go back to the previous activity when the user
     * tap on the back button in the supportActionBar.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
