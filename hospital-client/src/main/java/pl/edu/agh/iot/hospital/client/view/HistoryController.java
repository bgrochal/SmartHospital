package pl.edu.agh.iot.hospital.client.view;

import com.sun.javafx.collections.ObservableListWrapper;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import pl.edu.agh.iot.hospital.client.model.Patient;
import pl.edu.agh.iot.hospital.client.model.TempMeasurement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2016-04-28.
 */
public class HistoryController {
    @FXML
    private TableView<TempMeasurement> table;
    @FXML
    private TableColumn<TempMeasurement, String> date;
    @FXML
    private TableColumn<TempMeasurement, String> hour;
    @FXML
    private TableColumn<TempMeasurement, String> temp;

    private ObservableList<TempMeasurement> list= new ObservableListWrapper<>(new ArrayList<>());;

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
    }

    void addNew(TempMeasurement measurement){
        table.getItems().add(measurement);
    }
}
