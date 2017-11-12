package fr.sonoeseo.sonoapp.controller.directory;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import fr.sonoeseo.sonoapp.R;
import fr.sonoeseo.sonoapp.api.APIClient;
import fr.sonoeseo.sonoapp.api.APIConstants;
import fr.sonoeseo.sonoapp.api.APICore;
import fr.sonoeseo.sonoapp.api.APIDecoder;
import fr.sonoeseo.sonoapp.content.Content;
import fr.sonoeseo.sonoapp.controller.common.ItemInterface;
import fr.sonoeseo.sonoapp.models.Mate;

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

public class DirectoryFragment extends Fragment implements ItemInterface {

    private ArrayList<Mate> results = new ArrayList<>();
    private DirectoryAdapter directoryAdapter;
    private TextInputEditText searchInput;
    private Context ctx;

    /**
     * It will first create the fragment with the corresponding layout.
     * Then it will initialize the recyclerview and calendar.
     * <p>
     * Finally it will search the directory from the cache file and if the directory were not
     * loaded from the API since the launch of the application it then call the API.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View directoryView = inflater.inflate(R.layout.layout_directory,
                container, false);
        ctx = directoryView.getContext();

        RecyclerView recyclerView = directoryView.findViewById(R.id.directory);
        recyclerView.setLayoutManager(new LinearLayoutManager(ctx));
        directoryAdapter = new DirectoryAdapter(ctx, this);
        recyclerView.setAdapter(directoryAdapter);

        if (!Content.directory.isEmpty()) {
            loadCore();
        }
        if (!APIConstants.DIRECTORY_LOADED) {
            Load load = new Load();
            load.execute();
        }

        searchInput = directoryView.findViewById(R.id.searchInput);
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filter(searchInput.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        searchInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        return directoryView;
    }

    /**
     * If the user quit the directory and return to it.
     * He must find the full list of mates in the directory.
     */
    @Override
    public void onResume() {
        super.onResume();

        filter("");
        if (searchInput != null) {
            searchInput.setText("");
        }
    }

    /**
     * This function will simply hide the keyboard when the user tap anywhere on the screen
     * but outside the InputTextEdit.
     */
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager)
                ctx.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * This function is override from the ItemInterface.
     * It will open a dialog for the user, where he will be able to choose to call the mate or
     * send a message or add the mate to contacts.
     *
     * @param position : The position of the selected activity from the list.
     */
    @Override
    public void onItemClick(int position) {
        final Dialog dialog = new Dialog(ctx);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_directory);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        dialog.setCanceledOnTouchOutside(false);


        final Mate mate = results.get(position);

        TextView title = dialog.findViewById(R.id.title);
        title.setText(ctx.getString(R.string.emptyField));
        if (mate.getName() != null) {
            title.setText(mate.getName());
        }

        TextView txtPhone = dialog.findViewById(R.id.txtPhone);
        txtPhone.setText(ctx.getString(R.string.emptyField));
        if (mate.getPhone() != null) {
            txtPhone.setText(mate.getPhone());
        }

        ImageView imageView = dialog.findViewById(R.id.avatar);
        Picasso.with(ctx).load(APIConstants.SONO_AVATAR + mate.getAvatar()).error(R.drawable.ic_user).into(imageView);

        Button btnAdd = dialog.findViewById(R.id.btnAdd);
        Button btnMsg = dialog.findViewById(R.id.btnMsg);
        Button btnCall = dialog.findViewById(R.id.btnCall);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent contactIntent = new Intent(Intent.ACTION_INSERT);
                contactIntent.setType(ContactsContract.Contacts.CONTENT_TYPE);

                contactIntent.putExtra(ContactsContract.Intents.Insert.NAME, mate.getName());
                contactIntent.putExtra(ContactsContract.Intents.Insert.PHONE, mate.getPhone());
                startActivity(contactIntent);
            }
        });

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + mate.getPhone()));
                if (ActivityCompat.checkSelfPermission(ctx,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(callIntent);
            }
        });

        btnMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("sms:" + mate.getPhone()));

                if (ActivityCompat.checkSelfPermission(ctx,
                        Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(sendIntent);
            }
        });

        Button btnClose = dialog.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    /**
     * This function will filter the list of results by showing only mates which
     * are corresponding to the research.
     * <p>
     * Then it will sort the results by ascending names.
     *
     * @param search : The search.
     */
    public void filter(String search) {
        results.clear();

        if (!Content.directory.isEmpty()) {
            if (search.isEmpty()) {
                results = new ArrayList<>(Content.directory);
            } else {
                for (Mate mate : Content.directory) {
                    if (mate.getName().toLowerCase().contains(search.toLowerCase())) {
                        results.add(mate);
                    }
                }
            }
        }

        Collections.sort(results, new Comparator<Mate>() {
            @Override
            public int compare(Mate m1, Mate m2) {
                return m1.getName().compareToIgnoreCase(m2.getName());
            }
        });
        if (directoryAdapter != null) {
            directoryAdapter.setResults(results);
        }
    }

    /**
     * Refresh the list of mates from the cache file.
     * <p>
     * It will then refresh the list of the recyclerView.
     */
    private void loadCore() {
        String directoryResult = APICore.getInstance().read(ctx, APIConstants.CORE_DIRECTORY);
        if (!directoryResult.isEmpty()) {
            Content.directory = APIDecoder.extractDirectory(directoryResult);

            filter("");
        }
    }

    /**
     * This AsyncTask will refresh the directory data by calling the API.
     * <p>
     * It will then refresh the list of the recyclerView.
     */
    private class Load extends android.os.AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String args = "login=" + Content.user.getLogin() + "&token=" + Content.user.getToken();
            if (APIClient.getInstance().isOnline(ctx)) {
                return APIClient.getInstance().request(ctx, APIConstants.API_DIRECTORY, args);
            } else {
                return "";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (!result.isEmpty() && APIDecoder.extractStatus(result) != 2) {
                Content.directory = APIDecoder.extractDirectory(result);
                APICore.getInstance().write(ctx, APIConstants.CORE_DIRECTORY, result);

                filter("");
                APIConstants.DIRECTORY_LOADED = true;
            }
        }
    }
}