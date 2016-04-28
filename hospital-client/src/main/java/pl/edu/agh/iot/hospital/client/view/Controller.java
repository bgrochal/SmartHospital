package pl.edu.agh.iot.hospital.client.view;

import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.NumberStringConverter;
import pl.edu.agh.iot.hospital.client.model.Patient;

import java.util.Collections;

public class Controller {
    @FXML
    private TableView<Patient> table;

    @FXML
    private TableColumn<Patient, Integer> deviceNo;
    @FXML
    private TableColumn<Patient, String> patient;
    @FXML
    private TableColumn<Patient, Integer> temp;
    @FXML
    private TableColumn<Patient, Integer> bedPosition;
    @FXML
    private TableColumn<Patient, String> alert;

    @FXML
    private void initialize() {
        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        table.setEditable(true);

        deviceNo.setCellValueFactory(data -> data.getValue().deviceNoProperty().asObject());
        deviceNo.setCellFactory(TextFieldTableCell.<Patient, Integer>forTableColumn(new IntegerStringConverter()));
        deviceNo.setOnEditCommit((event) -> event.getTableView().getItems().get(event.getTablePosition().getRow()).setDeviceNo(event.getNewValue()));

        bedPosition.setCellValueFactory(data -> data.getValue().bedAngleProperty().asObject());
        bedPosition.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        temp.setCellValueFactory(data -> data.getValue().temperatureProperty().asObject());

        patient.setCellValueFactory(data -> data.getValue().nameProperty());
        patient.setCellFactory(TextFieldTableCell.forTableColumn());

        alert.setCellValueFactory(data -> data.getValue().alertProperty());

        table.getItems().setAll(new Patient(0));
    }
}
