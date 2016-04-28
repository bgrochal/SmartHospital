package pl.edu.agh.iot.hospital.client.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.IOException;

/**
 * Created by wgrabis on 27.04.2016.
 */
public class TempProgramMeasurement {
    private StringProperty date;
    private StringProperty hour;
    private StringProperty expected;
    private StringProperty temp;

    public String getDate() {
        return date.get();
    }

    public String getHour() {
        return hour.get();
    }

    public String getExpected() {
        return expected.get();
    }

    public String getTemp() {
        return temp.get();
    }

    public StringProperty tempProperty() {
        return temp;
    }

    public StringProperty dateProperty() {
        return date;
    }

    public StringProperty hourProperty() {
        return hour;
    }

    public StringProperty expectedProperty() {
        return expected;
    }

    public TempProgramMeasurement(String data) throws IOException {
        String[] splitted = data.split(",?\\s+");
        if(splitted.length != 6)throw new IOException();

        date = new SimpleStringProperty();
        hour = new SimpleStringProperty();
        expected = new SimpleStringProperty();
        temp = new SimpleStringProperty();

        date.set(splitted[0]);
        hour.set(splitted[1]);
        temp.set(splitted[4]);
        expected.set(splitted[5]);

    }


}
