package gui;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;

import client.ClientUI;
import common.Alerts;
import common.ClientServerMessage;
import common.GetInstance;
import common.Operation;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import common.Order;
import common.OrderChecker;
import common.Park;
import common.Traveler;
import common.Usermanager;
import javafx.fxml.Initializable;
import javafx.scene.layout.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * This Class is the GUI controller of OrderVisit.fxml
 * It handles all the JavaFx nodes events.
 * 
 * In this screen the traveler, group guide can order new visit
 *
 */
public class OrderAVisitController implements Initializable {


    @FXML
    private StackPane HeadPane;

    @FXML
    private Pane PaneOrder;

    @FXML
    private Button btnSubmit;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXTextField txtPhone;

    @FXML
    private JFXTextField txtID;

    @FXML
    private JFXDatePicker txtDate;

    @FXML
    private JFXComboBox<String> OrderComboBox;

    @FXML
    private JFXTextField txtVisitorsNum;

    @FXML
    private JFXComboBox<String> ParkComboBox;

    @FXML
    private JFXComboBox<LocalTime> TimeComboBox;
    
    @FXML
    private JFXRadioButton PayNowBtn;

    @FXML
    private JFXRadioButton PayParkBtn;
    
    @FXML
    private Pane PaneConfirmation;

    @FXML
    private Hyperlink btnHere;

    @FXML
    private Label summaryNum;

    @FXML
    private Label summaryPark;

    @FXML
    private Label summaryDate;

    @FXML
    private Label summaryTime;

    @FXML
    private Label summaryType;

    @FXML
    private Label summaryVisitors;

    @FXML
    private Label summaryStatus;
    
    @FXML
    private Label summaryPrice;

    @FXML
    private Button btnHome;
    
    @FXML
    private Button btnPrice;
    
    @FXML
    private Button Back;


	public static Order order;
	private Usermanager NewTraveler;
	private boolean isNewOrder;
	private boolean isOrderValid;
	private Traveler traveler;
	private URL location;
	private ResourceBundle resources;
	public static boolean isNewTraveler;
	private ToggleGroup paymentToggleGroup;

	/**Pattern to prevent incorrect info when ordering*/
	public static final Pattern mailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
			Pattern.CASE_INSENSITIVE);
	public static final Pattern visitorAmountPattern = Pattern.compile("^[0-9]{0,3}$", Pattern.CASE_INSENSITIVE);
	public static final Pattern phonePattern = Pattern.compile("^[0-9]{10}$", Pattern.CASE_INSENSITIVE);
	public static final Pattern fullNamePattern = Pattern.compile("^[a-zA-Z ]+$", Pattern.CASE_INSENSITIVE);


	
	public Pane getPaneOrder() {
		return PaneOrder;
	}

	public void setPaneOrder(Pane paneOrder) {
		PaneOrder = paneOrder;
	}
	
	public URL getLocation() {
		return location;
	}

	public void setLocation(URL location) {
		this.location = location;
	}

	public ResourceBundle getResources() {
		return resources;
	}

	public void setResources(ResourceBundle resources) {
		this.resources = resources;
	}
	
	public static Order getOrder() {
		return order;
	}

	public static void setOrder(Order order) {
		OrderAVisitController.order = order;
	}
	
	 /**
     * @param location The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
	@SuppressWarnings("static-access")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Locale.setDefault(Locale.ENGLISH); //English-style formatting
		GetInstance.getInstance().setOrderC(this);
		ComboBoxCheck();
		RadioBoxCheck();
		/** Can't order for the past, and can order one year in advance.*/
		txtDate.setDayCellFactory(picker -> new DateCell() { 
			public void updateItem(LocalDate date, boolean empty) {
				super.updateItem(date, empty);
				LocalDate today = LocalDate.now();
				LocalDate nextYear = LocalDate.of(today.getYear() + 1, today.getMonth(), today.getDayOfMonth());
				setDisable(empty || (date.compareTo(nextYear) > 0 || date.compareTo(today) < 0));
			}
		});
		
		
		if (WaitingListController.getSetDateFromWaitList() == 1) {
			txtDate.setValue(LocalDate.parse((String) WaitingListController.getAnotherDates().get(0)));
			TimeComboBox.setValue(LocalTime.parse((String) WaitingListController.getAnotherDates().get(1)));
			WaitingListController.setSetDateFromWaitList(0);
		}
		
		if(!(NewTraveler.GetisNewTraveler())) {
			Traveler traveler = NewTraveler.getCurrentTraveler();
			String fullName = traveler.getFirstName() + " " + traveler.getLastName();
			txtName.setText(fullName);
			txtID.setText(traveler.getId()+"");
			txtEmail.setText(traveler.getEmail());
			txtPhone.setText(traveler.getPhoneNum().replace("-",""));
			txtName.setDisable(true);
			txtID.setDisable(true);
			txtEmail.setDisable(true);
			txtPhone.setDisable(true);
			Back.setVisible(false);
		}
		else {
			Traveler traveler = NewTraveler.getCurrentTraveler();
			txtID.setText(traveler.getId()+"");
			txtID.setDisable(true);

		}
		
	}
	
	/**
     * Handles the action event when the submit order button is clicked.
     *
     * @param event The action event.
     * @throws IOException If an I/O error occurs.
     */
	@SuppressWarnings("static-access")
	@FXML
	private void SubmitOrderButton(ActionEvent event) throws IOException {
		/** We will continue to summary only if the input is valid */
		if (isValidInput()) {
			order = new Order(null, Integer.valueOf(txtID.getText()), getSelectedParkId(), 
					Integer.valueOf(txtVisitorsNum.getText()), null ,txtEmail.getText(),
					LocalDate.parse(txtDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))),
					LocalTime.parse(TimeComboBox.getValue().toString().split("-")[0]), 
					"PENDING", OrderComboBox.getValue() , txtPhone.getText(), ParkComboBox.getValue());
			order.setPrice(CalculatePrice());
			
			
			/** if we pushed Submit order */
			if (btnSubmit == event.getSource()) { 
				
				/**Insert new traveler to DB if needed*/
				if(NewTraveler.GetisNewTraveler()) {
					String[] travelerName = txtName.getText().split(" ");
					String travelerFirstName = travelerName[0];
					String travelerLastName = travelerName.length == 1 ? "" : travelerName[1];
					traveler = new Traveler(order.getVisitorId(), travelerFirstName, travelerLastName, 
						order.getVisitorEmail(), order.getTelephoneNumber(), 0 , 0);
					
					ClientServerMessage<?> AddTraveler = new ClientServerMessage<>(traveler, Operation.POST_NEW_TRAVLER);
					ClientUI.clientControllerInstance.sendMessageToServer(AddTraveler);
				}
				if(CheckIfOrderValid()) {
					/**Set new orderId*/
					order.setOrderId(OrderChecker.getLastNumber());
					
			        if (!OrderChecker.isDateAvailable(order)) { // need to enter waiting list
			        	new Alerts(AlertType.INFORMATION, "Park is full", "Park is full", "Reschedule or enter Waiting list").showAndWait();
			        	PaneOrder.setDisable(true);
			        	Stage newStage = new Stage();
						FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/WaitingList.fxml"));
						WaitingListController controller = new WaitingListController();
						controller.setOrder(order);
						loader.setController(controller);
						loader.load();
						controller.setTimeLabel(order.getDate() + ", " + order.getVisitTime());
						Parent p = loader.getRoot();
						newStage.initModality(Modality.WINDOW_MODAL);
						newStage.getIcons().add(new Image("common/images/Icon.png"));
			
			
						newStage.setTitle("Reschedule");
						newStage.setScene(new Scene(p));
						newStage.setResizable(false);
						newStage.show();
			
						newStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
							@Override
							public void handle(WindowEvent t) {
			
								PaneOrder.setDisable(false);
							}
						});
			        }
				        
			        else {
				    	/**Registering NewOrder to DB*/
						ClientServerMessage<?> OrderAttempt = new ClientServerMessage<>(order, Operation.POST_TRAVLER_ORDER);
						ClientUI.clientControllerInstance.sendMessageToServer(OrderAttempt);
						// Receive the response from the server
					    ClientServerMessage<?> isNewOrderMsg = ClientUI.clientControllerInstance.getData();
				        // Extract the data from the message
				    	isNewOrder = (Boolean) isNewOrderMsg.getFlag();
				        if (!isNewOrder) {
							new Alerts(AlertType.ERROR, "DataBase fail", "DataBase fail", "try restarting the program").showAndWait();
				        }
				        else { /** Order success*/
							this.summaryPark.setText(ParkComboBox.getValue());
							this.summaryDate.setText(order.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
							this.summaryVisitors.setText(order.getAmountOfVisitors() + "");
							this.summaryTime.setText(order.getVisitTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
							this.summaryType.setText(order.getTypeOfOrder());
							this.summaryStatus.setText(order.getOrderStatus());
							this.summaryPrice.setText(String.format("%.1f", order.getPrice()) + " ₪");
							this.summaryNum.setText(String.valueOf(order.getOrderId()));
							order.setStatus("NOTARRIVED");
							PaneOrder.setVisible(false);
							PaneConfirmation.setVisible(true);
						}
			        
				        
				    }
			    }
				else {
					new Alerts(AlertType.ERROR, "Already has order", "Already has order",
							"You already have an order on this date and time").showAndWait();
				}
			}
	    }
		
	}
	
    /**Handles the action event when the home button is clicked.*/
	@FXML
	void HomeButton(ActionEvent event) throws IOException { //return to home
		new Alerts(AlertType.CONFIRMATION, "Confirmation", "Confirmation", "Thank you for ordering from us").showAndWait(); 
		Stage stage = (Stage) btnHome.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource("/gui/HomePageFrame.fxml"));
		stage.setScene(new Scene(root));

	}
	
    /**Handles the action event when the back button is clicked.*/

	@FXML
	void BackButton(ActionEvent event) throws IOException { //return one step
		Stage stage = (Stage) Back.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource("/gui/HomePageFrame.fxml")); // תחליטו אתם לאן
		stage.setScene(new Scene(root));
	}
	
    /**Handles the action event when the price button is clicked.*/
	@FXML 
    private void handlePricingButton(ActionEvent event) {
		String imagePath = "src/common/images/Pricing.png";

        // Load the image and create an ImageView
        Image image = new Image("file:" + imagePath);
        ImageView imageView = new ImageView(image);

        // Create a Stage
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UTILITY);
        stage.setTitle("Pricing Guide");

        // Set the content (ImageView) to the Stage
        stage.setScene(new javafx.scene.Scene(new javafx.scene.layout.StackPane(imageView)));

        // Show the Stage
        stage.show();
    }
	
	/**
    * Checks if the order is valid based on traveler orders for the same date and hour
    * 
    * @return true if the input is valid, false otherwise.
    */
	private Boolean CheckIfOrderValid() {
		//Checking if order is valid
		ClientServerMessage<?> checkOrder = new ClientServerMessage<>(order, Operation.CHECK_IF_ORDER_VALID);
		ClientUI.clientControllerInstance.sendMessageToServer(checkOrder);
		// Receive the response from the server
	    ClientServerMessage<?> isOrderOkayMsg = ClientUI.clientControllerInstance.getData();
	    isOrderValid = (Boolean) isOrderOkayMsg.getFlag();
	    if (!isOrderValid) {
			return false;
        }
	    return true;
	}
	
	/**
    * Adding info to combo box.
    */
	private void ComboBoxCheck() { 

		// Set parks 
		ArrayList<String> parksNames = OrderChecker.getParksNames();
		if (parksNames != null) {
			ParkComboBox.getItems().addAll(parksNames);
		}
		
		// Set up order types
		for (Order.typeOfOrder orderType : Order.typeOfOrder.values()) {
		    OrderComboBox.getItems().add(orderType.toString());
		}
		

		// If it's single traveler he can only order 1 visit
		OrderComboBox.valueProperty().addListener((obs, oldItem, newItem) -> {
			if (newItem == null) {
			} else {
				if (newItem.toString().equals(Order.typeOfOrder.SOLO.toString())) {
					txtVisitorsNum.setText("1");
					txtVisitorsNum.setDisable(true);

				} else {
					txtVisitorsNum.setText("");
					txtVisitorsNum.setPromptText("Visitor's Number");
					txtVisitorsNum.setDisable(false);
				}
			}
		});
		
	//Setting park times
		LocalTime startTime = LocalTime.of(8, 0);
		LocalTime endTime = LocalTime.of(18, 0);

		List<LocalTime> hoursList = new ArrayList<>();
		LocalTime currentTime = startTime;

		while (!currentTime.isAfter(endTime)) {
		    hoursList.add(currentTime);
		    currentTime = currentTime.plusHours(1); // Change the gap to 4 hours
		}

        TimeComboBox.getItems().addAll(hoursList); //adding them to the Time combo box
	}
	
	
	private void RadioBoxCheck() { 
		paymentToggleGroup = new ToggleGroup();
	    PayNowBtn.setToggleGroup(paymentToggleGroup);
	    PayParkBtn.setToggleGroup(paymentToggleGroup);
	}

	
	 /**
	 This function returns the name of the selected park
	 */
	private Integer getSelectedParkId() {
	    Park park = OrderChecker.getParkByName(ParkComboBox.getValue());
		if (park != null)
			return park.getParkNumber();
		else
			return -1;
	}
	

	
	/**
	 This function checks if the given input is valid
	 */
	private boolean isValidInput() {
		String fullName = txtName.getText();
		String visitorsNumber = txtVisitorsNum.getText();
		String email = txtEmail.getText();
		String parkName = new String();
		Integer getMaxVisitors = null;
		Park park = OrderChecker.getParkByName(ParkComboBox.getValue());
		if (ParkComboBox.getValue() != null) {
			parkName = ParkComboBox.getValue().toString();
			getMaxVisitors = park.getMaxVisitors();
		}
		String TravelerId = txtID.getText();
		String Phone = txtPhone.getText();
		String Time = new String();
		if(TimeComboBox.getValue() != null)
			Time = TimeComboBox.getValue().toString();
		if (fullName.isEmpty() || visitorsNumber.isEmpty() || email.isEmpty() || parkName.isEmpty() || TravelerId.isEmpty()
				|| Phone.isEmpty() || txtDate.getValue()==null || Time.isEmpty()) {
			new Alerts(AlertType.ERROR, "Bad Input", "Bad Input", "Please fill all the required fields").showAndWait();
		} 
		
		else if (fullName.split(" ").length != 2) {
			new Alerts(AlertType.ERROR, "Bad Input", "Bad Input",
					"Please enter first name + last name").showAndWait();
		}
		else if (!validInput("AmountVisitor", txtVisitorsNum.getText())) {
			new Alerts(AlertType.ERROR, "Bad Input", "Invalid Visitors amount",
					"Insert a valid visitors amount please").showAndWait();
		}
		else if (getMaxVisitors < Integer.parseInt(visitorsNumber)) {
			String errorMessage = String.format("The park has only %d space, you ordered for too much visitors", getMaxVisitors);
		    new Alerts(AlertType.ERROR, "Place Error", "Park Max Visitors", errorMessage).showAndWait();
		}
		else if (!checkCurrentTime())
			new Alerts(AlertType.ERROR, "Bad Input", "Bad Date Input", "Please select future date").showAndWait();
		else if(TravelerId.length() != 7)
			new Alerts(AlertType.ERROR, "Bad Input", "Bad ID Input", "ID must be 9 digits").showAndWait();
		else if (Integer.parseInt(visitorsNumber) > 15 
				&& Order.typeOfOrder.GUIDEDGROUP.toString().equals(OrderComboBox.getValue())) {
			new Alerts(AlertType.ERROR, "Bad Input", "Invalid Visitor's Number",
					"Group order cannot exceed 15 visitors").showAndWait();
		}
		else if (Order.typeOfOrder.GUIDEDGROUP.toString().equals(OrderComboBox.getValue()) &&
	            Integer.parseInt(visitorsNumber) < 2) {
	        new Alerts(AlertType.ERROR, "Bad Input", "Invalid Visitor's Number",
	                "Group order must have at least 2 visitors").showAndWait();
	    }
		else if(OrderComboBox.getValue()=="GUIDEDGROUP" && Usermanager.getCurrentTraveler().getIsGroupGuide() == 0) {
        	new Alerts(AlertType.ERROR, "Group guide", "Group guide",
        			"You're not group guide, order as SOLO or FAMILY").showAndWait();
		} 
		else if(OrderComboBox.getValue()=="SOLO" && OrderComboBox.getValue()=="FAMILY"
				&& Usermanager.getCurrentTraveler().getIsGroupGuide() == 1) {
        	new Alerts(AlertType.ERROR, "Group guide", "Group guide",
        			"You're a group guide, order as a Group guide").showAndWait();
		} 
		else if (!validInput("fullName", fullName)) {
			new Alerts(AlertType.ERROR, "Bad Input", "Bad Input",
					"Name must contain only letters").showAndWait();
		}
		else if (Integer.parseInt(visitorsNumber) < 1) {
			new Alerts(AlertType.ERROR, "Bad Input", "Invalid Visitor's Number",
					"Visitor's number must be positive number and atleast 1. ").showAndWait();
		}
		else if (Integer.parseInt(visitorsNumber) > 6
				&& Order.typeOfOrder.FAMILY.toString().equals(OrderComboBox.getValue())) {
			new Alerts(AlertType.ERROR, "Bad Input", "Invalid Visitor's Number",
					"Family cannot exceed 6 visitors").showAndWait();
		}
		else if (checkTooLate()) {
			new Alerts(AlertType.ERROR, "Bad Input", "Invalid Date",
					"You can't order for more than a year in advance ").showAndWait();
		}
		else if (!CheckTooEarly()) {
			new Alerts(AlertType.ERROR, "Bad Input", "Invalid Visit Time",
				"Ensure visit time is 24 hours ahead.").showAndWait();
		}
		else if (!validInput("Email", email)) {
			new Alerts(AlertType.ERROR, "Bad Input", "Invalid Email",
					"Insert a valid Email please").showAndWait();
		}
		else if (!validInput("Phone", Phone)) {
			new Alerts(AlertType.ERROR, "Bad Input", "Invalid Phone",
					"Insert a valid Phone number please").showAndWait();
		}
		else if (!(PayParkBtn.isSelected() || PayNowBtn.isSelected())) {
			new Alerts(AlertType.ERROR, "Bad Input", "Invalid Payment Button",
					"Please choose how you want to pay.").showAndWait();
		} else {
			return true;
		}
		
		return false;
	
	}

	/**
	*This function check if the date,time entered is valid
	*@return true if valid, else false
	*/
	public boolean checkCurrentTime() { 
		LocalDate date = txtDate.getValue(); //Date wrote
		String[] arrSplit = TimeComboBox.getValue().toString().split("-");
		String[] hour = arrSplit[0].split(":");
		LocalTime arrivalTime = LocalTime.of(Integer.parseInt(hour[0]), 00, 00);  

		LocalTime now = LocalTime.now();
		if (date.compareTo(LocalDate.now()) <= 0 && now.compareTo(arrivalTime) >= 0) { //check if date and hour is correct
			return false;
		}
		return true;
	}
	
	/**
	 * This function disable ordering to one year + in advance
	 * @return true if the selected date is later than next year, else false
	 */
	public boolean checkTooLate() {
	    LocalDate date = txtDate.getValue();  
	    
	    LocalDate today = LocalDate.now();  // Get the current date
	    LocalDate nextYear = LocalDate.of(today.getYear() + 1, today.getMonth(), today.getDayOfMonth());  // Calculate the date for next year
	    
	    if (date.compareTo(nextYear) > 0) {  // Compare the selected date with next year
	        return true;  
	    }
	    return false;  
	}
	
	/**
	 * This method don't allow to order dates within 24 hours from now
	 * @return false if the selected date is less that 24 hours from now, else true
	 */
	private boolean CheckTooEarly() {
	    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	    LocalDateTime now = LocalDateTime.now();
	    System.out.println(txtDate.getValue());
	    System.out.println(TimeComboBox.getValue());
	    LocalDateTime tomorrow = now.plusDays(1).minusHours(1);
	    
	    LocalDateTime orderDateTime = LocalDateTime.parse(txtDate.getValue() + " " + TimeComboBox.getValue(), dtf);

	    return tomorrow.isBefore(orderDateTime);
	}

	
	/**
	 * This function helps checking if the input is valid
	 */
	public static boolean validInput(String nameMethod, String txt) {
		Matcher matcher = null;
		if (nameMethod.equals("Email")) {
			matcher = mailPattern.matcher(txt);
		} else if (nameMethod.equals("AmountVisitor")) {
			matcher = visitorAmountPattern.matcher(txt);
		} else if (nameMethod.equals("Phone")) {
			matcher = phonePattern.matcher(txt);
		}
		else if (nameMethod.equals("fullName")) {
			matcher = fullNamePattern.matcher(txt);
		}
		return matcher.find();
	} 

	/**
	 * Calculates the cost of the visit
	 * @return cost of the visit
	 */
	private Float CalculatePrice() {
		Float discountprice;
			// Solo/family pre-order 

			if ((OrderComboBox.getValue().equals("SOLO") || OrderComboBox.getValue().equals("FAMILY"))){

				discountprice = (float) (100*Integer.parseInt(txtVisitorsNum.getText())*0.85);
				return discountprice;
			}

			//Group pre-order, pre-payment
			if ((PayNowBtn.isSelected() && (OrderComboBox.getValue().equals("GUIDEDGROUP")))){
				discountprice = (float) (100*(Integer.parseInt(txtVisitorsNum.getText())-1)*0.75*0.88); 
				return discountprice;
			}
			
			//Group pre-order, payment at park
			if ((PayParkBtn.isSelected() && (OrderComboBox.getValue().equals("GUIDEDGROUP")))){
				discountprice = (float) (100*(Integer.parseInt(txtVisitorsNum.getText())-1)*0.75); 
				return discountprice;
			}
			return 0.0f;
			
	}
	


	
}