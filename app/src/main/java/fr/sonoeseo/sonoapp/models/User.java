package fr.sonoeseo.sonoapp.models;

import java.util.ArrayList;

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

public class User {

    private int id, admin;
    private String name, login, resp, avatar, phone, token;
    private ArrayList<Mate> team;

    public User(int id, String name, String login, String resp, String avatar, int admin,
                String phone, String token, ArrayList<Mate> team) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.resp = resp;
        this.avatar = avatar;
        this.admin = admin;
        this.phone = phone;
        this.token = token;
        this.team = new ArrayList<>(team);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLogin() {
        return login;
    }

    public String getResp() {
        return resp;
    }

    public String getAvatar() {
        return avatar;
    }

    public int getAdmin() {
        return admin;
    }

    public String getPhone() {
        return phone;
    }

    public String getToken() {
        return token;
    }

    public ArrayList<Mate> getTeam() {
        return team;
    }
}
