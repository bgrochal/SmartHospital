package pl.edu.agh.iot.hospital.client.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Patient {

//    private static final String BASE = "coap://192.168.17.9";
//    private static final String BASE = "coap://127.0.0.1:";
    private static int deviceNum = 1;

    private StringProperty baseForUri;
    private StringProperty name;
    private IntegerProperty temperature;
    private IntegerProperty deviceNo;
    private IntegerProperty bedAngle;
    private StringProperty alert;

    private String url;

    private final CoapClient tempClient;
    private final CoapClient alertClient;

    public Patient(String url) {
        baseForUri = new SimpleStringProperty();
        deviceNo = new SimpleIntegerProperty();
        name = new SimpleStringProperty();
        alert = new SimpleStringProperty();
        temperature = new SimpleIntegerProperty();
        bedAngle = new SimpleIntegerProperty();

        tempClient = new CoapClient();
        alertClient = new CoapClient();
        this.url = url;

        setDeviceNo(deviceNum);
        deviceNum++;
        baseForUri.addListener((observable, oldValue, newValue) -> refresh());
        addNameEditionListener();
        addBedPositionEditionListener();
        observeTemp();
        observeAlert();

        refresh();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public IntegerProperty temperatureProperty() {
        return temperature;
    }

    public IntegerProperty bedAngleProperty() {
        return bedAngle;
    }

    public IntegerProperty deviceNoProperty() {
        return deviceNo;
    }

    public void setDeviceNo(int deviceNo) {
        this.deviceNo.set(deviceNo);
//        baseForUri.set(BASE + (deviceNo - 1) + ":/Hospital");
        baseForUri.set(url + "/Hospital");
        tempClient.setURI(baseForUri.get() + "/Temperature");
        alertClient.setURI(baseForUri.get() + "/Alert");
    }


    public StringProperty alertProperty() {
        return alert;
    }

    private void observeTemp() {
        tempClient.observe(new AbstractCoapHandler() {
            @Override
            public void onLoad(CoapResponse coapResponse) {
                temperature.set(Integer.parseInt(coapResponse.getResponseText()));
            }
        });
    }

    private void observeAlert() {
        alertClient.observe(new AbstractCoapHandler() {
            @Override
            public void onLoad(CoapResponse coapResponse) {
                alert.set(coapResponse.getResponseText());
            }
        });
    }

    private void addNameEditionListener() {
        addPropertyListener(name, baseForUri.get() + "/Patient", this::refreshName);
    }

    private void addBedPositionEditionListener() {
        addPropertyListener(bedAngle, baseForUri.get() + "/Bed", this::refreshBed);
    }

    private <T> void addPropertyListener(ObservableValue<T> property, String uriString, Supplier<Void> refreshOperation) {
        property.addListener((observable, oldValue, newValue) -> {
            if (!(newValue == null) && !newValue.equals(oldValue)) {
                try {
                    URI uri = new URI(uriString);
                    new CoapClient(uri).put(newValue.toString(), 0);
                    refreshOperation.get();
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }



    public List<TempMeasurement> getTemperatureHistory() throws URISyntaxException {
        URI uri = new URI(baseForUri.get() + "/TemperaturesHistory");
        CoapResponse response = new CoapClient(uri).get();

        List<TempMeasurement> result = new ArrayList<>();

        String[] splitted = response.getResponseText().split("\\n");
        for(String s: splitted){
            try {
                TempMeasurement temp = new TempMeasurement(s);
                result.add(temp);
            } catch (IOException e) {
                System.out.println("Could not parse line: " + s);
            }
        }

        return result;
    }

    public boolean clearHistory() {
        try {
            URI uri = new URI(baseForUri.get() + "/TemperaturesHistory");
            CoapResponse response = new CoapClient(uri).delete();

            return (response.getResponseText().equals("History of temperatures deleted successfully"));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean setTemperatureProgram(TempProgram program) throws URISyntaxException {
        URI uri = new URI(baseForUri.get() + "/TemperatureProgram");
        CoapResponse response =  new CoapClient(uri).put(program.toString(), 0);

        return (response.getResponseText().equals("Program registered and started successfully"));
    }

    public List<TempProgramMeasurement> getTemperatureProgram() throws URISyntaxException, IOException {
        URI uri = new URI(baseForUri.get() + "/TemperatureProgram");
        CoapResponse response = new CoapClient(uri).get();

        List<TempProgramMeasurement> result = new ArrayList<>();

        String[] splitted = response.getResponseText().split("\\n");
        for(String s: splitted){
            result.add(new TempProgramMeasurement(s));
        }

        return result;
    }

    public boolean deleteTemperatureProgram() {
        try {
            URI uri = new URI(baseForUri.get() + "/TemperatureProgram");
            CoapResponse response = new CoapClient(uri).delete();

            return (response.getResponseText().equals("Program's output file deleted successfully"));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public void refresh() {
        refreshName();
        refreshBed();
        refreshAlert();
        refreshTemperature();
    }

    private Void refreshName() {
        try {
            URI uri = new URI(baseForUri.get() + "/Patient");
            CoapResponse response = new CoapClient(uri).get();
            name.set(response.getResponseText());
            return null;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private Void refreshBed() {
        try {
            URI uri = new URI(baseForUri.get() + "/Bed");
            CoapResponse response = new CoapClient(uri).get();
            bedAngle.set(Integer.parseInt(response.getResponseText()));
            return null;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private void refreshTemperature() {
        try {
            URI uri = new URI(baseForUri.get() + "/Temperature");
            CoapResponse response = new CoapClient(uri).get();

            temperature.set(Integer.parseInt(response.getResponseText()));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private void refreshAlert() {
        try {
            URI uri = new URI(baseForUri.get() + "/Alert");
            CoapResponse response = new CoapClient(uri).get();

            alert.set(response.getResponseText());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static abstract class AbstractCoapHandler implements CoapHandler {
        @Override
        public void onError() {}
    }

}
