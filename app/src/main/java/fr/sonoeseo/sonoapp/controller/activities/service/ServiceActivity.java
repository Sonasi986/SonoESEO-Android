package fr.sonoeseo.sonoapp.controller.activities.service;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;

import java.util.ArrayList;

import fr.sonoeseo.sonoapp.R;
import fr.sonoeseo.sonoapp.content.Content;
import fr.sonoeseo.sonoapp.controller.activities.common.ViewPagerAdapter;
import fr.sonoeseo.sonoapp.controller.activities.material.FragmentMaterials;

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

public class ServiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);

        setContentView(R.layout.activity_activities);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

            actionBar.setTitle(getString(R.string.emptyField));
            if (Content.activity.getTitle() != null) {
                actionBar.setTitle(Content.activity.getTitle());
            }
        }

        TextView place = (TextView) findViewById(R.id.place);
        place.setText(getString(R.string.emptyField));
        if (Content.activity.getPlace() != null) {
            place.setText(Content.activity.getPlace());
        }

        //Initializing the tablayout
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tabInformations)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tabMaterial)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //Fragments
        ServiceInformations serviceInformations = new ServiceInformations();
        FragmentMaterials fragmentMaterials = new FragmentMaterials();

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(serviceInformations);
        fragments.add(fragmentMaterials);

        //Initializing the viewpager
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragments));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    /**
     * This function finish the current activity when the user tap on the back button
     * in the support action bar.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
