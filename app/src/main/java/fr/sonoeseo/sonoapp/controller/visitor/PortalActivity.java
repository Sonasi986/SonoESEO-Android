package fr.sonoeseo.sonoapp.controller.visitor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import fr.sonoeseo.sonoapp.R;
import fr.sonoeseo.sonoapp.controller.login.LoginActivity;

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

public class PortalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_portal);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        final Context ctx = this;

        Button btnManage = (Button) findViewById(R.id.btn_gestion);
        btnManage.setOnClickListener(new View.OnClickListener() {

            /**
             * On click on the button we launch the LoginActivity.
             */
            public void onClick(View v) {
                Intent intent = new Intent(ctx, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button btnPublic = (Button) findViewById(R.id.btn_public);
        btnPublic.setOnClickListener(new View.OnClickListener() {

            /**
             * On click on the button we launch the WebviewActivity.
             */
            public void onClick(View v) {
                Intent intent = new Intent(ctx, WebviewActivity.class);
                startActivity(intent);
            }
        });
    }
}
