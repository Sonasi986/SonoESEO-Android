package fr.sonoeseo.sonoapp.controller.splashscreen;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import fr.sonoeseo.sonoapp.R;
import fr.sonoeseo.sonoapp.api.APIConstants;
import fr.sonoeseo.sonoapp.api.APICore;
import fr.sonoeseo.sonoapp.api.APIDecoder;
import fr.sonoeseo.sonoapp.content.Content;
import fr.sonoeseo.sonoapp.controller.MainActivity;
import fr.sonoeseo.sonoapp.controller.visitor.PortalActivity;

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

public class SplashscreenActivity extends Activity {

    private Context ctx;

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        ctx = this;

        if (Build.VERSION.SDK_INT >= 23) {
            List<String> listPermissionsNeeded = new ArrayList<>();
            if (checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
            }
            if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
            }
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 1);
            } else {
                StartAnimations();
            }
        } else {
            StartAnimations();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permission, @NonNull int[] grantResult) {
        super.onRequestPermissionsResult(requestCode, permission, grantResult);
        if (grantResult[0] == PackageManager.PERMISSION_GRANTED) {
            StartAnimations();
        } else {
            StartAnimations();
        }
    }

    private void StartAnimations() {
        View lineLeft = findViewById(R.id.lineLeft);
        View lineRight = findViewById(R.id.lineRight);
        lineLeft.clearAnimation();
        lineRight.clearAnimation();

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.translation_left);
        anim.reset();
        lineLeft.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translation_right);
        anim.reset();
        lineRight.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();

        ImageView logoSono = findViewById(R.id.logo);
        logoSono.clearAnimation();
        logoSono.startAnimation(anim);

        Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    // Splash screen pause time
                    while (waited < 1500) {
                        sleep(100);
                        waited += 100;
                    }

                    if (wasConnected()) {
                        String userResult = APICore.getInstance().read(ctx, APIConstants.CORE_USER);
                        Content.user = APIDecoder.extractUser(userResult);
                        if (Content.user != null) {
                            Intent intent = new Intent(ctx, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(ctx, PortalActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Intent intent = new Intent(ctx, PortalActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        finish();
                    }
                } catch (InterruptedException e) {
                    Intent intent = new Intent(ctx, PortalActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    finish();
                } finally {
                }
            }
        };
        splashTread.start();
    }

    private boolean wasConnected() {
        String userResult = APICore.getInstance().read(ctx, APIConstants.CORE_USER);
        if (userResult.equals("")) {
            return false;
        } else {
            Content.user = APIDecoder.extractUser(userResult);
            if (Content.user == null) {
                APICore.getInstance().removeAll(ctx);
                return false;
            }
            return true;
        }
    }
}