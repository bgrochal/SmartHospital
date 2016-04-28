package pl.edu.agh.iot.hospital.client.view;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.NumberStringConverter;
import pl.edu.agh.iot.hospital.client.model.Patient;

import java.util.Collections;

public class Controller {
    @FXML
    private Button clearHistoryButton;
    @FXML
    private Button deleteProgramButton;
    @FXML
    private Button deleteButton;
    @FXML
    private TextField addField;
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
        deleteButton.disableProperty().bind(Bindings.isEmpty(table.getSelectionModel().getSelectedItems()));
        deleteProgramButton.disableProperty().bind(Bindings.isEmpty(table.getSelectionModel().getSelectedItems()));
        clearHistoryButton.disableProperty().bind(Bindings.isEmpty(table.getSelectionModel().getSelectedItems()));

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

    }

    public void handleAddDevice(ActionEvent actionEvent) {
        table.getItems().add(new Patient(Integer.parseInt(addField.getText())));
    }

    public void handleDeleteAction(ActionEvent actionEvent) {
        table.getItems().remove(table.getSelectionModel().getSelectedItem());
    }

    public void handleDeleteProgramAction(ActionEvent actionEvent) {
        table.getSelectionModel().getSelectedItem().deleteTemperatureProgram();
    }

    public void handleClearHistoryAction(ActionEvent actionEvent) {
        table.getSelectionModel().getSelectedItem().clearHistory();
    }
}
