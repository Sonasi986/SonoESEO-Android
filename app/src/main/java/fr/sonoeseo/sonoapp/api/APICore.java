package fr.sonoeseo.sonoapp.api;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by sonasi on 31/10/2017.
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

public class APICore {

    private static APICore instance;

    private APICore() {
    }

    public static synchronized APICore getInstance() {
        if (instance == null) {
            instance = new APICore();
        }
        return instance;
    }

    /**
     * This function remove all the content of the cache files.
     *
     * @param cxt : The current context.
     */
    public void removeAll(Context cxt) {
        for (String entity : APIConstants.CORE_ENTITIES) {
            write(cxt, entity, "");
        }
    }

    /**
     * This function extract the string contained in the cache file.
     *
     * @param cxt        : The current context.
     * @param coreEntity : The name of the cache file where the data are going to be extracted.
     * @return
     */
    public String read(Context cxt, String coreEntity) {
        String result = "";
        File cacheFile = new File(cxt.getCacheDir() + "/" + coreEntity);
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(cacheFile);
            result = convertStreamToString(fileInputStream);
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * This function will write the data passed in arguments in the cache file.
     *
     * @param cxt        : The current context.
     * @param coreEntity : The name of the file where the data are going to be written.
     * @param data       : The json string received.
     */
    public void write(Context cxt, String coreEntity, String data) {
        File cacheFile = new File(cxt.getCacheDir() + "/" + coreEntity);
        try {
            try {
                FileOutputStream stream = new FileOutputStream(cacheFile);
                try {
                    try {
                        stream.write(data.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } finally {
                    stream.close();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * To convert the InputStream to String we use the BufferedReader.readLine()
     * method. We iterate until the BufferedReader return null which means
     * there's no more data to read. Each line will appended to a StringBuilder
     * and returned as String.
     */
    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
