package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import client.NavigationManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DepartmentManagerReportsController implements Initializable {

	ObservableList<?> observable = FXCollections.observableArrayList(); 




	    @FXML
	    private Label headerLabel;

	    @FXML
	    private TableView<?> ReportsTableView;

	    @FXML
	    private TableColumn<?, ?> reportIDCol;

	    @FXML
	    private TableColumn<?, ?> reportTypeCol;

	    @FXML
	    private TableColumn<?, ?> parkIDCol;

	    @FXML
	    private TableColumn<?, ?> monthCol;

	    @FXML
	    private TableColumn<?, ?> commentCol;

	    @FXML
	    private JFXButton visitReportBtn;

	    @FXML
	    private JFXButton CancelsReportBtn;

	    @FXML
	    private JFXComboBox<?> monthCB;

	    @FXML
	    void cancelReportBtn(MouseEvent event) {

	    }

	    @FXML
	    void visitReportBtn(MouseEvent event)  {
			
	    	//NavigationManager.openPage("DeparmentManegerVisitsReport.fxml",event , "Home Page", true);
	    	   try {
	    		   System.out.println("Loading FXML from path: " + NavigationManager.class.getResource("/gui/" + "test.fxml"));

	   	    	NavigationManager.openPage("test.fxml",event , "", true);
	    	    } catch (Exception ex) {
	    	        System.out.println(ex.toString()); // Log the exception to understand what went wrong
	    	    }

	    }

	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
//		loadTabelView();
//		initComboBox();
//		initTabelView();
	}

	

}
