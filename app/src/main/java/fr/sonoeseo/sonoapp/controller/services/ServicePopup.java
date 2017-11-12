package fr.sonoeseo.sonoapp.controller.services;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import fr.sonoeseo.sonoapp.R;
import fr.sonoeseo.sonoapp.api.APIClient;
import fr.sonoeseo.sonoapp.api.APIConstants;
import fr.sonoeseo.sonoapp.api.APIDecoder;
import fr.sonoeseo.sonoapp.content.Content;
import fr.sonoeseo.sonoapp.controller.dialog.Alert;

/**
 * Created by sonasi on 02/11/2017.
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

public class ServicePopup {

    private static ServicePopup instance;
    private ServiceInterface serviceInterface;

    private ServicePopup() {
    }

    public static ServicePopup getInstance() {
        if (instance == null) {
            instance = new ServicePopup();
        }
        return instance;
    }

    /**
     * Set the serviceInterface, then when the user has responded to the dialog,
     * we will ask the single function of serviceInterface to reload the recyclerView content.
     *
     * @param serviceInterface : The ServiceInterface which contain only one function.
     */
    public void setServiceInterface(ServiceInterface serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

    /**
     * This function will simply open a dialog with every data about the service,
     * then the user will be able to indicate if if he will be available or unavailable.
     *
     * @param ctx : The current context.
     */
    public void modal(final Context ctx) {
        final Dialog dialog = new Dialog(ctx);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_service);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        dialog.setCanceledOnTouchOutside(false);

        TextView txtTitle = dialog.findViewById(R.id.txtTitle);
        txtTitle.setText(ctx.getString(R.string.emptyField));
        if (Content.activity.getTitle() != null) {
            txtTitle.setText(Content.activity.getTitle());
        }

        TextView txtPlace = dialog.findViewById(R.id.txtPlace);
        txtPlace.setText(ctx.getString(R.string.emptyField));
        if (Content.activity.getPlace() != null) {
            txtPlace.setText(Content.activity.getPlace());
        }

        TextView txtDate = dialog.findViewById(R.id.txtDate);
        txtDate.setText(ctx.getString(R.string.emptyField));
        if (Content.activity.getDate() != null) {
            String dateString = new SimpleDateFormat("EEEE d MMMM yyyy")
                    .format(Content.activity.getDate());
            txtDate.setText(dateString);
        }

        TextView txtDateEnd = dialog.findViewById(R.id.txtDateEnd);
        txtDateEnd.setText(ctx.getString(R.string.emptyField));
        if (Content.activity.getDateEnd() != null) {
            String dateString = new SimpleDateFormat("EEEE d MMMM yyyy")
                    .format(Content.activity.getDateEnd());
            txtDateEnd.setText(dateString);
        }

        TextView txtHour = dialog.findViewById(R.id.txtHour);
        txtHour.setText(ctx.getString(R.string.emptyField));
        if (Content.activity.getHour() != null) {
            txtHour.setText(Content.activity.getHour());
        }

        TextView txtHourEnd = dialog.findViewById(R.id.txtHourEnd);
        txtHourEnd.setText(ctx.getString(R.string.emptyField));
        if (Content.activity.getHourEnd() != null) {
            txtHourEnd.setText(Content.activity.getHourEnd());
        }

        final TextView txtState = dialog.findViewById(R.id.txtState);
        switch (Content.activity.getState()) {
            case 0:
                txtState.setText("Non inscrit.");
                break;
            case 1:
                txtState.setText("Validé.");
                break;
            case 2:
                txtState.setText("Inscrit, en attente de validation.");
                break;
            default:
                txtState.setText("Vous êtes indisponible.");
                break;
        }

        Button btnUnavailable = dialog.findViewById(R.id.btnUnavailable);
        btnUnavailable.setOnClickListener(new View.OnClickListener() {

            /**
             * This function will verify if the user is not already unavailable for this
             * service.
             * If not it will send a request to the API to change the status of the user for a
             * given service.
             */
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (Content.activity.getState() == 3) {
                    Alert.getInstance().alert(ctx, ctx.getString(R.string.error), ctx.getString(R.string.alreadyUnavailable));
                } else {
                    String args = "login=" + Content.user.getLogin() + "&token=" + Content.user.getToken()
                            + "&service=" + Content.activity.getId() + "&state=3";
                    Request request = new Request(ctx, args, serviceInterface);
                    request.execute();

                    Content.activity.setState(3);
                }
            }
        });

        Button btnAvailable = dialog.findViewById(R.id.btnAvailable);
        btnAvailable.setOnClickListener(new View.OnClickListener() {

            /**
             * This function will verify if the user is not already registered or validate for this
             * service.
             * If not it will send a request to the API to change the status of the user for a
             * given service.
             */
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (Content.activity.getState() == 2 && Content.activity.getState() != 1) {
                    Alert.getInstance().alert(ctx, ctx.getString(R.string.error), ctx.getString(R.string.alreadyAvailable));
                } else {
                    String args = "login=" + Content.user.getLogin() + "&token=" + Content.user.getToken()
                            + "&service=" + Content.activity.getId() + "&state=2";
                    Request request = new Request(ctx, args, serviceInterface);
                    request.execute();

                    Content.activity.setState(2);
                }
            }
        });

        RelativeLayout close = dialog.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    /**
     * This AsyncTask will send a request to the API to update the status of the user
     * for a given service.
     */
    private class Request extends android.os.AsyncTask<String, Void, String> {
        private Context ctx;
        private String args;
        private ServiceInterface serviceInterface;

        public Request(Context ctx, String args, ServiceInterface serviceInterface) {
            this.ctx = ctx;
            this.args = args;
            this.serviceInterface = serviceInterface;
        }

        @Override
        protected String doInBackground(String... urls) {
            if (APIClient.getInstance().isOnline(ctx)) {
                return APIClient.getInstance().request(ctx, APIConstants.API_SERVICE, args);
            } else {
                return "";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (!result.isEmpty() && APIDecoder.extractStatus(result) != 2) {
                serviceInterface.refresh();
            }
        }
    }
}
