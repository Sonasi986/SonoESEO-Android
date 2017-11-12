package fr.sonoeseo.sonoapp.controller.activities.material;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.sonoeseo.sonoapp.R;
import fr.sonoeseo.sonoapp.content.Content;

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

public class FragmentMaterials extends Fragment {

    /**
     * This function will create the view with the corresponding layout.
     * Then it will initialize the recyclerView with the material list from
     * the activity variable in the Content.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View materielView = inflater.inflate(R.layout.layout_materials, container, false);
        Context ctx = materielView.getContext();

        RecyclerView material = materielView.findViewById(R.id.materiel);
        material.setLayoutManager(new LinearLayoutManager(ctx));
        MaterialAdapter materialAdapter = new MaterialAdapter(ctx, Content.activity.getMaterials());
        material.setAdapter(materialAdapter);

        return materielView;
    }
}
