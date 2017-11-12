package fr.sonoeseo.sonoapp.models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

public class Activity {

    private int id, state;
    private ActivityType type;
    private Date date, dateEnd;
    private String title, content, client, hour, hourEnd, start, price, guarantee, place, address;
    private Mate supervisor;
    private ArrayList<Material> materials;
    private ArrayList<Mate> team;

    public Activity(int id, int state, String type, String date, String dateEnd, String title,
                    String content, String client, String hour, String hourEnd, String start,
                    String price, String guarantee, String place, String address, Mate supervisor,
                    ArrayList<Material> materials, ArrayList<Mate> team) {
        this.id = id;
        this.state = state;
        this.type = ActivityType.valueOf(type);
        try {
            this.date = new SimpleDateFormat("yyyy-M-d").parse(date);
        } catch (Exception e) {
            this.date = null;
        }

        try {
            this.dateEnd = new SimpleDateFormat("yyyy-M-d").parse(dateEnd);
        } catch (Exception e) {
            this.dateEnd = null;
        }

        this.title = title;
        this.content = content;
        this.client = client;
        this.hour = hour;
        this.hourEnd = hourEnd;
        this.start = start;
        this.price = price;
        this.guarantee = guarantee;
        this.place = place;
        this.address = address;
        this.supervisor = supervisor;
        this.materials = materials;
        this.team = team;
    }

    public int getId() {
        return id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public ActivityType getType() {
        return type;
    }

    public Date getDate() {
        return date;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getClient() {
        return client;
    }

    public String getHour() {
        return hour;
    }

    public String getHourEnd() {
        return hourEnd;
    }

    public String getStart() {
        return start;
    }

    public String getPrice() {
        return price;
    }

    public String getGuarantee() {
        return guarantee;
    }

    public String getPlace() {
        return place;
    }

    public String getAddress() {
        return address;
    }

    public Mate getSupervisor() {
        return supervisor;
    }

    public ArrayList<Material> getMaterials() {
        return materials;
    }

    public ArrayList<Mate> getTeam() {
        return team;
    }
}