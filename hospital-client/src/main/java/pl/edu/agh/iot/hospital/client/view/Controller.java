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
import pl.edu.agh.iot.hospital.client.model.Patient;
import pl.edu.agh.iot.hospital.client.model.TempMeasurement;
import pl.edu.agh.iot.hospital.client.model.TempProgramMeasurement;

import java.util.Collections;

public class Controller {
    @FXML
    private Button clearHistoryButton;
    @FXML
    private Button deleteProgramButton;
    @FXML
    private Button viewHistoryButton;
    @FXML
    private Button viewProgramPane;
    @FXML
    private Button addProgramButton;
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
        viewHistoryButton.disableProperty().bind(Bindings.isEmpty(table.getSelectionModel().getSelectedItems()));
        viewProgramPane.disableProperty().bind(Bindings.isEmpty(table.getSelectionModel().getSelectedItems()));
        addProgramButton.disableProperty().bind(Bindings.isEmpty(table.getSelectionModel().getSelectedItems()));

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
        table.getItems().add(new Patient(addField.getText()));
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

    public void handleViewHistoryAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/HistoryViewPane.fxml"));

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setScene(
                    new Scene(
                            (Pane) loader.load()
                    )
            );

            HistoryController controller = loader.<HistoryController>getController();
            stage.show();

            for(TempMeasurement x: table.getSelectionModel().getSelectedItem().getTemperatureHistory()){
                controller.addNew(x);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void handleAddProgramAction(ActionEvent actionEvent){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddProgramPane.fxml"));

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setScene(
                    new Scene(
                            (Pane) loader.load()
                    )
            );

            AddProgramController controller = loader.<AddProgramController>getController();
            controller.setPatient(table.getSelectionModel().getSelectedItem());
            stage.show();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void handleViewProgramAction(ActionEvent actionEvent){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProgramViewPane.fxml"));

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setScene(
                    new Scene(
                            (Pane) loader.load()
                    )
            );

           ProgramController controller = loader.<ProgramController>getController();
            stage.show();

            for(TempProgramMeasurement x: table.getSelectionModel().getSelectedItem().getTemperatureProgram()){
                controller.addNew(x);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}


