package pl.edu.agh.iot.hospital.client.model;

import java.io.IOException;

/**
 * Created by wgrabis on 27.04.2016.
 */
public class TempMeasurement {
    private String date;
    private String hour;
    private String temp;

    public TempMeasurement(String data) throws IOException {
        String[] splitted = data.split(",?\\s+");
        if(splitted.length != 3)throw new IOException();

        date = splitted[0];
        hour = splitted[1];
        temp = splitted[2];
    }
}
