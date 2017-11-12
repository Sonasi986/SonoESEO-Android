package fr.sonoeseo.sonoapp.controller.dialog;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;

import fr.sonoeseo.sonoapp.R;

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

public class Alert {

    private static Alert instance;

    private Alert() {
    }

    public static Alert getInstance() {
        if (instance == null) {
            instance = new Alert();
        }
        return instance;
    }

    /**
     * This function will open a dialog with a "Damn." answer.
     *
     * @param ctx     : The current context.
     * @param title   : The title of the dialog.
     * @param content : The content of the dialog.
     */
    public void alert(Context ctx, String title, String content) {
        new MaterialDialog.Builder(ctx)
                .title(title)
                .content(content)
                .positiveText(ctx.getString(R.string.damn))
                .titleColor(ctx.getResources().getColor(R.color.darkGray))
                .contentColor(ctx.getResources().getColor(R.color.darkGrayContent))
                .positiveColor(ctx.getResources().getColor(R.color.blue))
                .show();
    }

    /**
     * This function will open a dialog with the given parameters.
     *
     * @param ctx      : The current context.
     * @param title    : The title of the dialog.
     * @param content  : The content of the dialog.
     * @param negative : The text on the button.
     */
    public void alert(Context ctx, String title, String content, String negative) {
        new MaterialDialog.Builder(ctx)
                .title(title)
                .content(content)
                .positiveText(negative)
                .titleColor(ctx.getResources().getColor(R.color.darkGray))
                .contentColor(ctx.getResources().getColor(R.color.darkGrayContent))
                .positiveColor(ctx.getResources().getColor(R.color.blue))
                .show();
    }
}
