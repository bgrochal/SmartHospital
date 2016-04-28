package pl.edu.agh.iot.hospital.client.view;

import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.TextFieldTableCell;
import pl.edu.agh.iot.hospital.client.model.TempProgram;
import pl.edu.agh.iot.hospital.client.model.TempProgramMeasurement;


/**
 * Created by Admin on 2016-04-28.
 */
public class ProgramController {
    @FXML
    private TableView<TempProgramMeasurement> table;
    @FXML
    private TableColumn<TempProgramMeasurement, String> date;
    @FXML
    private TableColumn<TempProgramMeasurement, String> hour;
    @FXML
    private TableColumn<TempProgramMeasurement, String> temp;
    @FXML
    private TableColumn<TempProgramMeasurement, String> expected;

    @FXML
    void initialize(){
        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        table.setEditable(true);

        date.setCellValueFactory(data -> data.getValue().dateProperty());
        date.setCellFactory(TextFieldTableCell.forTableColumn());

        hour.setCellValueFactory(data -> data.getValue().hourProperty());
        hour.setCellFactory(TextFieldTableCell.forTableColumn());

        temp.setCellValueFactory(data -> data.getValue().tempProperty());
        temp.setCellFactory(TextFieldTableCell.forTableColumn());

        expected.setCellValueFactory(data -> data.getValue().expectedProperty());
        expected.setCellFactory(TextFieldTableCell.forTableColumn());
    }

    void addNew(TempProgramMeasurement measurement){
        table.getItems().add(measurement);
    }
}
