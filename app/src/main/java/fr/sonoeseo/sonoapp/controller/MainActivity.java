package fr.sonoeseo.sonoapp.controller;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;

import fr.sonoeseo.sonoapp.R;
import fr.sonoeseo.sonoapp.api.APIConstants;
import fr.sonoeseo.sonoapp.content.Content;
import fr.sonoeseo.sonoapp.controller.activities.ActivitiesFragment;
import fr.sonoeseo.sonoapp.controller.articles.ArticlesFragment;
import fr.sonoeseo.sonoapp.controller.calendar.CalendarFragment;
import fr.sonoeseo.sonoapp.controller.directory.DirectoryFragment;
import fr.sonoeseo.sonoapp.controller.services.ServicesFragment;
import fr.sonoeseo.sonoapp.controller.settings.SettingsFragment;
import fr.sonoeseo.sonoapp.controller.user.UserFragment;

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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String ARGUMENT = "FRAGMENT";
    private String currentFragment;
    private HashMap<String, Fragment> fragments = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragments.put(getString(R.string.fragment_news), new ArticlesFragment());
        fragments.put(getString(R.string.fragment_services), new ServicesFragment());
        fragments.put(getString(R.string.fragment_calendar), new CalendarFragment());
        fragments.put(getString(R.string.fragment_activities), new ActivitiesFragment());
        fragments.put(getString(R.string.fragment_settings), new SettingsFragment());
        fragments.put(getString(R.string.fragment_directory), new DirectoryFragment());
        fragments.put(getString(R.string.fragment_user), new UserFragment());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // SET HEADER INFORMATIONS
        View headerView = navigationView.getHeaderView(0);

        TextView txtName = headerView.findViewById(R.id.txtName);
        txtName.setText(getString(R.string.emptyField));
        if (Content.user.getName() != null && txtName != null) {
            txtName.setText(Content.user.getName());
        }

        TextView txtResp = headerView.findViewById(R.id.txtResp);
        txtResp.setText(getString(R.string.emptyField));
        if (Content.user.getResp() != null && txtResp != null) {
            txtResp.setText(Content.user.getResp());
        }

        ImageView imgAvatar = headerView.findViewById(R.id.imgAvatar);
        if (imgAvatar != null) {
            Picasso.with(this).load(APIConstants.SONO_AVATAR +
                    Content.user.getAvatar()).error(R.drawable.ic_user).into(imgAvatar);
        }

        ActionBar actionBar = getSupportActionBar();
        if (savedInstanceState != null) {
            currentFragment = savedInstanceState.getString(ARGUMENT, getString(R.string.fragment_news));
            getSupportFragmentManager().beginTransaction().replace(R.id.content,
                    fragments.get(currentFragment), currentFragment).commit();
            if (actionBar != null) {
                actionBar.setTitle(currentFragment);
            }
        } else {
            currentFragment = getString(R.string.fragment_news);
            getSupportFragmentManager().beginTransaction().replace(R.id.content,
                    fragments.get(currentFragment), currentFragment).commit();

            if (actionBar != null) {
                actionBar.setTitle(currentFragment);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARGUMENT, currentFragment);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (id) {
            case R.id.nav_news:
                currentFragment = getString(R.string.fragment_news);
                fragment = fragments.get(currentFragment);
                fragmentManager.beginTransaction().replace(R.id.content, fragment, currentFragment).commit();
                break;
            case R.id.nav_services:
                currentFragment = getString(R.string.fragment_services);
                fragment = fragments.get(currentFragment);
                fragmentManager.beginTransaction().replace(R.id.content, fragment, currentFragment).commit();
                break;
            case R.id.nav_calendar:
                currentFragment = getString(R.string.fragment_calendar);
                fragment = fragments.get(currentFragment);
                fragmentManager.beginTransaction().replace(R.id.content, fragment, currentFragment).commit();
                break;
            case R.id.nav_activities:
                currentFragment = getString(R.string.fragment_activities);
                fragment = fragments.get(currentFragment);
                fragmentManager.beginTransaction().replace(R.id.content, fragment, currentFragment).commit();
                break;
            case R.id.nav_settings:
                currentFragment = getString(R.string.fragment_settings);
                fragment = fragments.get(currentFragment);
                fragmentManager.beginTransaction().replace(R.id.content, fragment, currentFragment).commit();
                break;
            case R.id.nav_directory:
                currentFragment = getString(R.string.fragment_directory);
                ;
                fragment = fragments.get(currentFragment);
                fragmentManager.beginTransaction().replace(R.id.content, fragment, currentFragment).commit();
                break;
            case R.id.nav_user:
                currentFragment = getString(R.string.fragment_user);
                fragment = fragments.get(currentFragment);
                fragmentManager.beginTransaction().replace(R.id.content, fragment, currentFragment).commit();
                break;
            default:
                break;
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(currentFragment);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
