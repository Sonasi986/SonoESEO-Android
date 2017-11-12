package fr.sonoeseo.sonoapp.models;

import java.text.SimpleDateFormat;
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

public class Article {

    private String author, authorAvatar, title, type, content;
    private Date date;

    public Article(String author, String authorAvatar, String date, String title, String type,
                   String content) {
        this.author = author;
        this.authorAvatar = authorAvatar;
        try {
            this.date = new SimpleDateFormat("yyyy-M-d").parse(date);
        } catch (Exception e) {
            this.date = null;
        }
        this.title = title;
        this.type = type;
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public String getAuthorAvatar() {
        return authorAvatar;
    }

    public Date getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }
}
