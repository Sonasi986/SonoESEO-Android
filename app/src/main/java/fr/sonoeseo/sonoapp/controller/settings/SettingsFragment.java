package fr.sonoeseo.sonoapp.controller.settings;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.Calendar;

import fr.sonoeseo.sonoapp.R;
import fr.sonoeseo.sonoapp.api.APIConstants;

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

public class SettingsFragment extends Fragment {
    public static final String APP_VERSION = "2.5";

    View settingsView;
    TextView facebook, twitter, sonoeseo, sylapse, contact, legal, about;
    Context ctx;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        settingsView = inflater.inflate(R.layout.layout_settings, container, false);

        ctx = settingsView.getContext();

        facebook = settingsView.findViewById(R.id.facebook);
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentToBrowser("https://www.facebook.com/sonoeseo/");
            }
        });

        twitter = settingsView.findViewById(R.id.twitter);
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentToBrowser("https://twitter.com/SonoESEO");
            }
        });

        sonoeseo = settingsView.findViewById(R.id.sonoeseo);
        sonoeseo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentToBrowser("http://www.sonoeseo.com");
            }
        });

        sylapse = settingsView.findViewById(R.id.sylapse);
        sylapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentToBrowser("http://sylapse.sonoeseo.com/interLogin.php");
            }
        });

        contact = settingsView.findViewById(R.id.contact);
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(ctx)
                        .title("Contact")
                        .content(Html.fromHtml("<body><b>Développeur :</b> KATOA Sonasi<br/><br/>" +
                                "<b>Mail :</b> s.katoa@sonoeseo.com</body>"))
                        .negativeText(R.string.dialog_connect_cancel)
                        .titleColor(getResources().getColor(R.color.darkGray))
                        .contentColor(getResources().getColor(R.color.darkGrayContent))
                        .negativeColor(getResources().getColor(R.color.blue))
                        .show();
            }
        });

        legal = settingsView.findViewById(R.id.legal);
        legal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentToBrowser(APIConstants.SONO_CONDITIONS);
            }
        });

        about = settingsView.findViewById(R.id.about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = Calendar.getInstance().get(Calendar.YEAR);
                new MaterialDialog.Builder(ctx)
                        .title("About")
                        .content(Html.fromHtml("<body><b>Version " + APP_VERSION + "</b><br/>" +
                                "<br/>" +
                                "Application : SONO ESEO<br/>" +
                                "<i>Merci à Papy pour sa coopération sur Sylapse</i><br/>" +
                                "<br/>" +
                                "© KATOA Sonasi " + year + "</body>"))
                        .negativeText(R.string.dialog_connect_cancel)
                        .titleColor(getResources().getColor(R.color.darkGray))
                        .contentColor(getResources().getColor(R.color.darkGrayContent))
                        .negativeColor(getResources().getColor(R.color.blue))
                        .show();
            }
        });

        return settingsView;
    }

    /**
     * When the user tap on an item where we have to open a website.
     * It will call this function.
     *
     * @param url : The url which will be opened in the navigator.
     */
    public void intentToBrowser(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

}
