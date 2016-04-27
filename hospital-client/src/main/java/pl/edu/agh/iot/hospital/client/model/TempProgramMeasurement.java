package pl.edu.agh.iot.hospital.client.model;

import java.io.IOException;

/**
 * Created by wgrabis on 27.04.2016.
 */
public class TempProgramMeasurement {
    private String date;
    private String hour;
    private String expected;
    private String temp;

    public String getDate() {
        return date;
    }

    public String getHour() {
        return hour;
    }

    public String getExpected() {
        return expected;
    }

    public String getTemp() {
        return temp;
    }

    public TempProgramMeasurement(String data) throws IOException {
        String[] splitted = data.split(",?\\s+");
        if(splitted.length != 6)throw new IOException();

        date = splitted[0];
        hour = splitted[1];
        temp = splitted[4];
        expected = splitted[5];

    }


}
