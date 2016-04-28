package pl.edu.agh.iot.hospital.client.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.IOException;

public class TempMeasurement {
    private StringProperty date;
    private StringProperty hour;

    public StringProperty tempProperty() {
        return temp;
    }

    public StringProperty dateProperty() {
        return date;
    }

    public StringProperty hourProperty() {
        return hour;
    }

    private StringProperty temp;

    public String getDate() {
        return date.get();
    }

    public String getHour() {
        return hour.get();
    }

    public String getTemp() {
        return temp.get();
    }

    public TempMeasurement(String data) throws IOException {
        String[] splitted = data.split(",?\\s+");
        date = new SimpleStringProperty();
        hour = new SimpleStringProperty();
        temp = new SimpleStringProperty();

        if(splitted == null || splitted.length != 3)throw new IOException();

        date.set(splitted[0]);
        hour.set(splitted[1]);
        temp.set(splitted[2]);
    }
}
