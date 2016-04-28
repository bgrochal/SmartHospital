package pl.edu.agh.iot.hospital.client.view;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.NumberStringConverter;
import pl.edu.agh.iot.hospital.client.model.*;


import java.net.URISyntaxException;

/**
 * Created by Admin on 2016-04-28.
 */
public class AddProgramController {
    private int time = 0;
    private Patient patient;

    @FXML
    private TableView<IntegerSomething> table;
    @FXML
    private TableColumn<IntegerSomething, Integer>  temp;
    @FXML
    private Button acceptButton;
    @FXML
    private Button addButton;
    @FXML
    private TextField addField;
    @FXML
    private Button setButton;
    @FXML
    void initialize(){
        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        table.setEditable(true);

        temp.setCellValueFactory(data -> data.getValue().asObject());
        temp.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
    }

    public void setPatient(Patient patient){
        this.patient = patient;
    }

    public void handleAcceptAction(ActionEvent actionEvent) {
        Stage stage = (Stage) acceptButton.getScene().getWindow();
        try {
            TempProgram program = new TempProgram(time);
            for(IntegerSomething s: table.getItems()){
                program.put(s.get());
            }
            patient.setTemperatureProgram(program);

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        stage.close();
    }

    public void handleAddAction(ActionEvent actionEvent) {
        table.getItems().add(new IntegerSomething(0));
    }

    public void handleSetTime(ActionEvent actionEvent){
        time = Integer.parseInt(addField.textProperty().get());
    }
}

