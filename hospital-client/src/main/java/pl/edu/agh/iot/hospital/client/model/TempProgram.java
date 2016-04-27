package pl.edu.agh.iot.hospital.client.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wgrabis on 27.04.2016.
 */
public class TempProgram {
    private int time;
    private List<Integer> temperatures;

    public TempProgram(int time){
        this.time = time;
        temperatures = new ArrayList<>();
    }

    public void put(int temperature){
        temperatures.add(temperature);
    }

    @Override
    public String toString(){
        String result = String.valueOf(time);

        for(int temp: temperatures){
            result += " " + String.valueOf(temp);
        }

        return result;
    }
}
