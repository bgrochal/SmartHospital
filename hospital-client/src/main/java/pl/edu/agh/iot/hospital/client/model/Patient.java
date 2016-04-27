package pl.edu.agh.iot.hospital.client.model;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Patient {
    String baseForUri;

    private String name;
    private int temperature;
    private int bedAngle;
    private String alert;

    public Patient(String baseForUri) throws URISyntaxException {
        this.baseForUri = baseForUri + ":";
        refresh();
    }

    public int getTemp(){
        return temperature;
    }

    public int getBedAngle(){
        return bedAngle;
    }

    public String getName(){
        return name;
    }

    public String getAlert(){
        return alert;
    }

    public void setObserveTemp(CoapHandler handler) throws URISyntaxException {
        URI uri = new URI(baseForUri + "/Temperature");
        new CoapClient(uri).observe(handler);
    }

    public void setObserveAlert(CoapHandler handler) throws URISyntaxException {
        URI uri = new URI(baseForUri + "/Alert");
        new CoapClient(uri).observe(handler);
    }

    public List<TempMeasurement> getTemperatureHistory() throws IOException, URISyntaxException {
        URI uri = new URI(baseForUri + "/TemperatureHistory");
        CoapResponse response = new CoapClient(uri).get();

        List<TempMeasurement> result = new ArrayList<>();

        String[] splitted = response.getResponseText().split("\\n");
        for(String s: splitted){
            result.add(new TempMeasurement(s));
        }

        return result;
    }

    public boolean clearHistory() throws URISyntaxException {
        URI uri = new URI(baseForUri + "/TemperatureHistory");
        CoapResponse response = new CoapClient(uri).delete();

        return (response.getResponseText().equals("History of temperatures deleted successfully"));
    }

    public boolean setTemperatureProgram(TempProgram program) throws URISyntaxException {
        URI uri = new URI(baseForUri + "/TemperatureProgram");
        CoapResponse response =  new CoapClient(uri).put(program.toString(), 0);

        return (response.getResponseText().equals("Program registered and started successfully"));
    }

    public List<TempProgramMeasurement> getTemperatureProgram() throws URISyntaxException, IOException {
        URI uri = new URI(baseForUri + "/TemperatureProgram");
        CoapResponse response = new CoapClient(uri).get();

        List<TempProgramMeasurement> result = new ArrayList<>();

        String[] splitted = response.getResponseText().split("\\n");
        for(String s: splitted){
            result.add(new TempProgramMeasurement(s));
        }

        return result;
    }

    public boolean deleteTemperatureProgram() throws URISyntaxException {
        URI uri = new URI(baseForUri + "/TemperatureProgram");
        CoapResponse response = new CoapClient(uri).delete();

        return (response.getResponseText().equals("Program's output file deleted successfully"));
    }

    public void setName(String newName) throws URISyntaxException {
        URI uri = new URI(baseForUri + "/Patient");
        CoapResponse response = new CoapClient(uri).put(newName, 0);

        refreshName();
    }

    public void setBedAngle(int newAngle) throws URISyntaxException {
        URI uri = new URI(baseForUri + "/Bed");
        CoapResponse response = new CoapClient(uri).put(String.valueOf(newAngle), 0);

        refreshName();
    }

    public void refresh() throws URISyntaxException {
        refreshName();
        refresBed();
        refreshAlert();
        refreshTemperature();
    }

    private void refreshName() throws URISyntaxException {
        URI uri = new URI(baseForUri + "/Patient");
        CoapResponse response = new CoapClient(uri).get();

        name = response.getResponseText();
    }

    private void refresBed() throws URISyntaxException {
        URI uri = new URI(baseForUri + "/Bed");
        CoapResponse response = new CoapClient(uri).get();

        bedAngle = Integer.parseInt(response.getResponseText());
    }

    private void refreshTemperature() throws URISyntaxException {
        URI uri = new URI(baseForUri + "/Temperature");
        CoapResponse response = new CoapClient(uri).get();

        temperature = Integer.parseInt(response.getResponseText());
    }

    private void refreshAlert() throws URISyntaxException {
        URI uri = new URI(baseForUri + "/Patient");
        CoapResponse response = new CoapClient(uri).get();

        alert = response.getResponseText();
    }

}
