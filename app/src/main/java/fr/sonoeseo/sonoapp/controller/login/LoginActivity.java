package fr.sonoeseo.sonoapp.controller.login;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;

import fr.sonoeseo.sonoapp.R;
import fr.sonoeseo.sonoapp.api.APIClient;
import fr.sonoeseo.sonoapp.api.APIConstants;
import fr.sonoeseo.sonoapp.api.APICore;
import fr.sonoeseo.sonoapp.api.APIDecoder;
import fr.sonoeseo.sonoapp.content.Content;
import fr.sonoeseo.sonoapp.controller.MainActivity;
import fr.sonoeseo.sonoapp.controller.dialog.Alert;
import fr.sonoeseo.sonoapp.models.User;

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

public class LoginActivity extends AppCompatActivity {

    private EditText loginEditText, passwordEditText;
    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        ctx = this;

        loginEditText = (EditText) findViewById(R.id.username);
        passwordEditText = (EditText) findViewById(R.id.password);

        Button connect = (Button) findViewById(R.id.connect);
        if (connect != null) {
            connect.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String login = loginEditText.getText().toString();
                    String password = passwordEditText.getText().toString();

                    if (APIClient.getInstance().isOnline(ctx)) {
                        if (!login.isEmpty() && !password.isEmpty()) {
                            ConnectUser connectUser = new ConnectUser(ctx, login, password);
                            connectUser.execute();
                        } else {
                            Alert.getInstance().alert(ctx, getString(R.string.error),
                                    getString(R.string.empty_login),
                                    getString(R.string.dialog_connect_cancel));
                        }
                    } else {

                        Alert.getInstance().alert(ctx, getString(R.string.error),
                                getString(R.string.empty_login),
                                getString(R.string.dialog_connect_cancel));
                    }
                }
            });
        }

        Button legal = (Button) findViewById(R.id.legal);
        if (legal != null) {
            legal.setOnClickListener(new View.OnClickListener() {

                /**
                 * This function will open the legal notices in the Android navigator
                 * when the user ask for legal.
                 */
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(APIConstants.SONO_CONDITIONS));
                    startActivity(intent);
                }
            });
        }

        Button forgot = (Button) findViewById(R.id.forgot);
        if (forgot != null) {
            forgot.setOnClickListener(new View.OnClickListener() {

                /**
                 * This function will ask user for an email client.
                 */
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + APIConstants.EMAIL));
                    intent.putExtra(Intent.EXTRA_SUBJECT, APIConstants.EMAIL_TOPIC);
                    intent.putExtra(Intent.EXTRA_TEXT, "");
                    startActivity(Intent.createChooser(intent, "Send Email"));
                }
            });
        }
    }

    /**
     * This AsyncTask will try to connect the user.
     * It will first open a Dialog during the connection time.
     * When a answer is received it will close the dialog.
     */
    class ConnectUser extends android.os.AsyncTask<String, Void, String> {

        MaterialDialog mdProgress;
        private Context context;
        private String login, password;

        ConnectUser(Context context, String login, String password) {
            this.context = context;
            this.login = login;
            this.password = password;
        }

        @Override
        protected void onPreExecute() {
            mdProgress = new MaterialDialog.Builder(context)
                    .title(getString(R.string.dialog_connect_title))
                    .content(getString(R.string.dialog_connect_content))
                    .negativeText(R.string.dialog_connect_cancel)
                    .cancelable(false)
                    .progress(true, 4, false)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onNegative(MaterialDialog dialog) {
                            ConnectUser.this.cancel(true);
                        }
                    })
                    .titleColor(getResources().getColor(R.color.darkGray))
                    .contentColor(getResources().getColor(R.color.darkGrayContent))
                    .negativeColor(getResources().getColor(R.color.blue))
                    .show();
        }

        @Override
        protected String doInBackground(String... urls) {
            if (APIClient.getInstance().isOnline(context)) {
                String args = "login=" + login + "&password=" + password;
                return APIClient.getInstance().request(ctx, APIConstants.API_LOGIN, args);
            } else {
                return "";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            final String error = "{\"status\":1,\"data\":\"\"}";
            if (result.isEmpty() || APIDecoder.extractStatus(result) != 1 ||
                    result.equals(error)) {

                String title, msg;
                if (APIDecoder.extractStatus(result) == 1) {
                    title = getString(R.string.error_mdp_title);
                    msg = getString(R.string.error_mdp);
                } else {
                    title = getString(R.string.dialog_connect_title);
                    msg = getString(R.string.error);
                }
                mdProgress.dismiss();
                mdProgress = new MaterialDialog.Builder(context)
                        .title(title)
                        .content(msg)
                        .negativeText(getString(R.string.close))
                        .titleColor(getResources().getColor(R.color.darkGray))
                        .contentColor(getResources().getColor(R.color.darkGrayContent))
                        .negativeColor(getResources().getColor(R.color.blue))
                        .show();
            } else {
                mdProgress.dismiss();
                User user = APIDecoder.extractUser(result);

                if (user == null) {
                    Content.user = null;
                    String title = getString(R.string.dialog_connect_title);
                    String msg = getString(R.string.error);

                    mdProgress.dismiss();
                    mdProgress = new MaterialDialog.Builder(context)
                            .title(title)
                            .content(msg)
                            .negativeText(getString(R.string.close))
                            .titleColor(getResources().getColor(R.color.darkGray))
                            .contentColor(getResources().getColor(R.color.darkGrayContent))
                            .negativeColor(getResources().getColor(R.color.blue))
                            .show();
                } else {
                    Content.user = user;
                    APICore.getInstance().write(ctx, APIConstants.CORE_USER, result);

                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }
    }
}