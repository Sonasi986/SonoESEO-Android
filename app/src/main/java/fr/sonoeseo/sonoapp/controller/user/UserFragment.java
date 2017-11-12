package fr.sonoeseo.sonoapp.controller.user;

import android.content.Context;
import android.content.Intent;
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

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import fr.sonoeseo.sonoapp.R;
import fr.sonoeseo.sonoapp.api.APIClient;
import fr.sonoeseo.sonoapp.api.APIConstants;
import fr.sonoeseo.sonoapp.api.APICore;
import fr.sonoeseo.sonoapp.api.APIDecoder;
import fr.sonoeseo.sonoapp.content.Content;
import fr.sonoeseo.sonoapp.controller.common.TeamAdapter;
import fr.sonoeseo.sonoapp.controller.visitor.PortalActivity;

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

public class UserFragment extends Fragment {

    private Context ctx;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View userView = inflater.inflate(R.layout.layout_user, container, false);
        ctx = userView.getContext();

        if (!APIConstants.USER_LOADED) {
            Load load = new Load();
            load.execute();
        }

        TextView txtName = userView.findViewById(R.id.name);
        txtName.setText(getString(R.string.emptyField));
        if (Content.user.getName() != null) {
            txtName.setText(Content.user.getName());
        }

        TextView txtResp = userView.findViewById(R.id.resp);
        txtResp.setText(getString(R.string.emptyField));
        if (Content.user.getResp() != null) {
            txtResp.setText(Content.user.getResp());
        }

        TextView txtLogin = userView.findViewById(R.id.login);
        txtLogin.setText(getString(R.string.emptyField));
        if (Content.user.getLogin() != null) {
            txtLogin.setText(Content.user.getLogin());
        }

        TextView txtPhone = userView.findViewById(R.id.phone);
        txtPhone.setText(getString(R.string.emptyField));
        if (Content.user.getPhone() != null) {
            txtPhone.setText(Content.user.getPhone());
        }

        ImageView imgAvatar = userView.findViewById(R.id.avatar);
        String link = APIConstants.SONO_AVATAR + Content.user.getAvatar();
        Picasso.with(ctx).load(link).error(R.drawable.ic_user).placeholder(R.drawable.ic_user)
                .into(imgAvatar);


        RecyclerView recyclerView = userView.findViewById(R.id.team);
        recyclerView.setLayoutManager(new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL,
                false));
        recyclerView.setAdapter(new TeamAdapter(ctx, Content.user.getTeam()));

        Button disconnect = userView.findViewById(R.id.disconnect);
        disconnect.setOnClickListener(new View.OnClickListener() {

            /**
             * If the user disconnect from the app, we remove all the data from the cache files.
             */
            @Override
            public void onClick(View view) {
                APICore.getInstance().removeAll(getContext());

                FirebaseApp.initializeApp(ctx);
                APIClient.RegisterNotification registerNotification =
                        new APIClient.RegisterNotification(ctx, "");
                registerNotification.execute();

                Intent intent = new Intent(getContext(), PortalActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return userView;
    }

    private class Load extends android.os.AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String args = "login=" + Content.user.getLogin() + "&token=" + Content.user.getToken();
            if (APIClient.getInstance().isOnline(ctx)) {
                return APIClient.getInstance().request(ctx, APIConstants.API_USER, args);
            } else {
                return "";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (!result.isEmpty() && APIDecoder.extractStatus(result) != 2) {
                Content.user = APIDecoder.extractUser(result);
                APICore.getInstance().write(ctx, APIConstants.CORE_USER, result);

                APIConstants.USER_LOADED = true;
            }
        }
    }
}