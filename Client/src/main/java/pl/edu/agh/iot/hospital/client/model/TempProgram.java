package pl.edu.agh.iot.hospital.client.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;
import java.util.List;

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
        String result = String.valueOf(time.get());

        for(IntegerProperty temp: temperatures){
            result += " " + String.valueOf(temp.get());
        }

        return result;
    }
}
