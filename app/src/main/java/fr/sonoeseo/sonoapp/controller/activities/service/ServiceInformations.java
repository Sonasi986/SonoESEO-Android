package fr.sonoeseo.sonoapp.controller.activities.service;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

import fr.sonoeseo.sonoapp.R;
import fr.sonoeseo.sonoapp.api.APIConstants;
import fr.sonoeseo.sonoapp.content.Content;
import fr.sonoeseo.sonoapp.controller.common.TeamAdapter;

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

public class ServiceInformations extends Fragment {

    /**
     * It will first create the fragment with the corresponding layout.
     * Then it will initialize the recyclerview.
     * <p>
     * It will finally set text on every TextView in the view, according
     * to data in the activity variable in the Content class.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View informationView = inflater.inflate(R.layout.layout_service, container, false);
        final Context ctx = informationView.getContext();

        ImageView imgAvatar = informationView.findViewById(R.id.imgAvatar);
        String link = APIConstants.SONO_AVATAR + Content.activity.getSupervisor().getAvatar();
        Picasso.with(ctx).load(link).placeholder(R.drawable.ic_user)
                .error(R.drawable.ic_user).into(imgAvatar);

        TextView txtSupervisor = informationView.findViewById(R.id.txtSupervisor);
        txtSupervisor.setText(getString(R.string.emptyField));
        if (Content.activity.getSupervisor() != null &&
                Content.activity.getSupervisor().getName() != null) {
            txtSupervisor.setText(Content.activity.getSupervisor().getName());
        }

        TextView txtClient = informationView.findViewById(R.id.txtClient);
        txtClient.setText(getString(R.string.emptyField));
        if (Content.activity.getClient() != null) {
            txtClient.setText(getString(R.string.client) + " " + Content.activity.getClient());
        }

        TextView txtPrice = informationView.findViewById(R.id.txtPrice);
        txtPrice.setText(getString(R.string.emptyField));
        if (Content.activity.getPrice() != null) {
            txtPrice.setText(getString(R.string.price) + " " + Content.activity.getPrice() + " €");
        }

        TextView txtGuarantee = informationView.findViewById(R.id.txtGuarantee);
        txtGuarantee.setText(getString(R.string.emptyField));
        if (Content.activity.getGuarantee() != null) {
            txtGuarantee.setText(getString(R.string.guarantee) + " " + Content.activity.getGuarantee() + " €");
        }

        TextView txtStart = informationView.findViewById(R.id.txtStart);
        txtStart.setText(getString(R.string.emptyField));
        if (Content.activity.getStart() != null) {
            String departString = ctx.getString(R.string.start) + " " + Content.activity.getStart();
            txtStart.setText(departString);
        }

        TextView txtContent = informationView.findViewById(R.id.txtContent);
        txtContent.setText(getString(R.string.emptyField));
        if (Content.activity.getContent() != null) {
            txtContent.setText(ctx.getString(R.string.contents)
                    + "\n" + Content.activity.getContent());
        }

        TextView txtDate = informationView.findViewById(R.id.txtDate);
        txtDate.setText(getString(R.string.emptyField));
        if (Content.activity.getDate() != null) {
            String dateString = new SimpleDateFormat("EEEE d MMM yyyy")
                    .format(Content.activity.getDate());
            txtDate.setText(dateString);
        }

        TextView txtHour = informationView.findViewById(R.id.txtHour);
        txtHour.setText(getString(R.string.emptyField));
        if (Content.activity.getHour() != null) {
            txtHour.setText(Content.activity.getHour());
        }

        TextView txtDateEnd = informationView.findViewById(R.id.txtDateEnd);
        txtDateEnd.setText(getString(R.string.emptyField));
        if (Content.activity.getDateEnd() != null) {
            String dateEndString = new SimpleDateFormat("EEEE d MMM yyyy")
                    .format(Content.activity.getDateEnd());
            txtDateEnd.setText(dateEndString);
        }

        TextView txtHourEnd = informationView.findViewById(R.id.txtHourEnd);
        txtHourEnd.setText(getString(R.string.emptyField));
        if (Content.activity.getHourEnd() != null) {
            txtHourEnd.setText(Content.activity.getHourEnd());
        }


        TextView txtEmptyTeam = informationView.findViewById(R.id.txtEmptyTeam);
        if (!Content.activity.getTeam().isEmpty()) {
            txtEmptyTeam.setVisibility(View.GONE);

            RecyclerView team = informationView.findViewById(R.id.team);
            team.setLayoutManager(new LinearLayoutManager(ctx,
                    LinearLayoutManager.HORIZONTAL, false));
            TeamAdapter teamAdapter = new TeamAdapter(ctx, Content.activity.getTeam());
            team.setAdapter(teamAdapter);
        } else {
            txtEmptyTeam.setVisibility(View.VISIBLE);
        }

        Button btnGps = informationView.findViewById(R.id.btnGps);
        btnGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Content.activity.getPlace().equals("\"Non défini\"") && !Content.activity.getPlace().isEmpty()) {
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse("google.navigation:q=" + Content.activity.getAddress()));
                    startActivity(intent);
                } else {
                    new MaterialDialog.Builder(ctx)
                            .title(getString(R.string.error))
                            .content(getString(R.string.no_place))
                            .negativeText(R.string.dialog_connect_cancel)
                            .titleColor(getResources().getColor(R.color.darkGray))
                            .contentColor(getResources().getColor(R.color.darkGrayContent))
                            .negativeColor(getResources().getColor(R.color.blue))
                            .show();
                }
            }
        });

        return informationView;
    }
}
