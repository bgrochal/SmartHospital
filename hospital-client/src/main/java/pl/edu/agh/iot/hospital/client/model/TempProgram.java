package pl.edu.agh.iot.hospital.client.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wgrabis on 27.04.2016.
 */
public class TempProgram {
    private IntegerProperty time;
    private List<IntegerSomething> temperatures;

    public List<IntegerSomething> getTemperatures() {
        return temperatures;
    }

    public int getTime() {
        return time.get();
    }

    public IntegerProperty timeProperty() {
        return time;
    }

    public TempProgram(int time){

        this.time = new SimpleIntegerProperty(time);
        temperatures = new ArrayList<>();
    }

    public void put(int temperature){
        temperatures.add(new IntegerSomething(temperature));
    }

    @Override
    public String toString(){
        String result = String.valueOf(time);

        for(IntegerProperty temp: temperatures){
            result += " " + String.valueOf(temp.get());
        }

        return result;
    }
}
