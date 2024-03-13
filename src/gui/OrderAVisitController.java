//package gui;
//
//import java.io.IOException;
//import java.net.URL;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.Arrays;
//
//import java.util.List;
//import java.util.Locale;
//import java.util.ResourceBundle;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import com.jfoenix.controls.JFXComboBox;
//import com.jfoenix.controls.JFXDatePicker;
//import com.jfoenix.controls.JFXTextField;
//
//import common.Alerts;
//import javafx.event.ActionEvent;
//import javafx.event.EventHandler;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.control.Alert.AlertType;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.stage.Modality;
//import javafx.stage.Stage;
//import javafx.stage.StageStyle;
//import javafx.stage.WindowEvent;
//import common.Order;
//import javafx.fxml.Initializable;
//import javafx.scene.layout.*;
//import java.time.LocalDate;
//import java.time.LocalTime;
//
///**
// * This Class is the GUI controller of OrderVisit.fxml
// * It handles all the JavaFx nodes events.
// * 
// * In this screen the traveler, group guide can order new visit
// *
// */
//public class OrderAVisitController implements Initializable {
//
//
//    @FXML
//    private StackPane HeadPane;
//
//    @FXML
//    private Pane PaneOrder;
//
//    @FXML
//    private Button btnSubmit;
//
//    @FXML
//    private JFXTextField txtName;
//
//    @FXML
//    private JFXTextField txtEmail;
//
//    @FXML
//    private JFXTextField txtPhone;
//
//    @FXML
//    private JFXTextField txtID;
//
//    @FXML
//    private JFXDatePicker txtDate;
//
//    @FXML
//    private JFXComboBox<Order.OrderType> OrderComboBox;
//
//    @FXML
//    private JFXTextField txtVisitorsNum;
//
//    @FXML
//    private JFXComboBox<String> ParkComboBox;
//
//    @FXML
//    private JFXComboBox<LocalTime> TimeComboBox;
//
//    @FXML
//    private Pane PaneConfirmation;
//
//    @FXML
//    private Hyperlink btnHere;
//
//    @FXML
//    private Label summaryNum;
//
//    @FXML
//    private Label summaryPark;
//
//    @FXML
//    private Label summaryDate;
//
//    @FXML
//    private Label summaryTime;
//
//    @FXML
//    private Label summaryType;
//
//    @FXML
//    private Label summaryVisitors;
//
//    @FXML
//    private Label summaryStatus;
//    
//    @FXML
//    private Label summaryPrice;
//
//    @FXML
//    private Button btnHome;
//    
//    @FXML
//    private Button btnPrice;
//    
//    @FXML
//    private Button Back;
//
//
//	private Order order;
//	private static boolean DBFail = true;
//	
//	//Pattern to prevent incorrect info when ordering
//	public static final Pattern MailCheck = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
//			Pattern.CASE_INSENSITIVE);
//	public static final Pattern VisitorsAmountCheck = Pattern.compile("^[0-9]{0,3}$", Pattern.CASE_INSENSITIVE);
//	public static final Pattern PhoneCheck = Pattern.compile("^[0-9]{10}$", Pattern.CASE_INSENSITIVE);
//
//	@Override
//	public void initialize(URL location, ResourceBundle resources) {
//		Locale.setDefault(Locale.ENGLISH); //English-style formatting
//	    PaneConfirmation.setVisible(false); // Set PaneConfirmation initially invisible
//		ComboBoxCheck();
//		// Can't order for the past, and can order one year in advance.
//		txtDate.setDayCellFactory(picker -> new DateCell() { 
//			public void updateItem(LocalDate date, boolean empty) {
//				super.updateItem(date, empty);
//				LocalDate today = LocalDate.now();
//				LocalDate nextYear = LocalDate.of(today.getYear() + 1, today.getMonth(), today.getDayOfMonth());
//				setDisable(empty || (date.compareTo(nextYear) > 0 || date.compareTo(today) < 0));
//			}
//		});
//	}
//
//	@FXML
//	private void SubmitOrderButton(ActionEvent event) throws IOException {
//		// We will continue to summary only if the input is valid
//		if (isValidInput()) {
//			order = new Order(txtID.getText(), ParkComboBox.getValue().toString(), 
//					Integer.parseInt(txtVisitorsNum.getText()), txtEmail.getText(),
//					txtDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
//					TimeComboBox.getValue().toString().split("-")[0], Order.OrderStatus.PENDING, (Order.OrderType) OrderComboBox.getValue() );
//					
//
//			if (btnSubmit == event.getSource()) { //if we pushed Submit order
//				//ClientServerMessage OrderAttempt = new ClientServerMessage(NewOrderMsg, Operation.)
//				//SystemClient.handleMessageFromClient(NewOrderMsg); //צריך לברר
//
//				if (order.getOrderStatus().equals("WAITING")) { // need to enter waiting list
//					PaneOrder.setDisable(true);
//					Stage stage = new Stage();
//					Pane root = FXMLLoader.load(getClass().getResource("/gui/WaitingList.fxml"));
//					Scene scene = new Scene(root);
//					stage.setResizable(false);
//					stage.setScene(scene);
//					stage.show();
//
//					stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
//						@Override
//						public void handle(WindowEvent t) {
//
//							PaneOrder.setDisable(false);
//						}
//					});
//				
//				} else if (order.getOrderStatus().equals("EXISTING")) { 
//					new Alerts(AlertType.ERROR, "Has Order", "Has Order ", "You already have an order on this day").showAndWait();
//				} else if (!DBFail) { // the Order details didn't enter to DB
//					new Alerts(AlertType.ERROR, "Something went wrong", "Something went wrong", "Restart the program").showAndWait();
//				} else { // Order success
//					this.summaryPark.setText(order.getParkId());
//					this.summaryDate.setText(order.getDate());
//					this.summaryVisitors.setText(order.getAmountOfVisitors() + "");
//					this.summaryTime.setText(order.getVisitTime());
//					this.summaryType.setText(order.getOrdertype() + "");
//					this.summaryStatus.setText(order.getOrderStatus() + "");
//					Double value=CalculatePrice();
//					this.summaryPrice.setText(String.format("%.1f", value) + " ₪");
//					PaneConfirmation.setVisible(true);
//				}
//			
//			}
//		}
//	}
//
//	
//	@FXML
//	void HomeButton(ActionEvent event) throws IOException { //return to home
//		new Alerts(AlertType.CONFIRMATION, "Confirmation", "Confirmation", "Thank you for ordering from us").showAndWait(); 
//		Stage stage = (Stage) btnHome.getScene().getWindow();
//		Parent root = FXMLLoader.load(getClass().getResource("/gui/HomePageFrame.fxml"));
//		stage.setScene(new Scene(root));
//	}
//	
//	@FXML
//	void BackButton(ActionEvent event) throws IOException { //return one step
//		Stage stage = (Stage) Back.getScene().getWindow();
//		Parent root = FXMLLoader.load(getClass().getResource("/gui/HomePageFrame.fxml")); // תחליטו אתם לאן
//		stage.setScene(new Scene(root));
//	}
//	
//	@FXML //price button
//    private void handlePricingButton(ActionEvent event) {
//		String imagePath = "src/common/images/Pricing.jpg";
//
//        // Load the image and create an ImageView
//        Image image = new Image("file:" + imagePath);
//        ImageView imageView = new ImageView(image);
//
//        // Create a Stage
//        Stage stage = new Stage();
//        stage.initModality(Modality.APPLICATION_MODAL);
//        stage.initStyle(StageStyle.UTILITY);
//        stage.setTitle("Pricing Guide");
//
//        // Set the content (ImageView) to the Stage
//        stage.setScene(new javafx.scene.Scene(new javafx.scene.layout.StackPane(imageView)));
//
//        // Show the Stage
//        stage.show();
//    }
//	
//	private void ComboBoxCheck() { //adding info to combo box
//		ParkComboBox.getItems().clear();
//		OrderComboBox.getItems().clear();
//		TimeComboBox.getItems().clear();
//
//
//		// Set parks 
//		ArrayList<String> parksNames = new ArrayList<>(Arrays.asList("A", "B", "C")); //צריך להכניס מהדטא בייס תשמות
//		if (parksNames != null) {
//			ParkComboBox.getItems().addAll(parksNames);
//		}
//		
//		// Set up order types
//		OrderComboBox.getItems().addAll(Arrays.asList(Order.OrderType.values()));
//		
//
//		// If it's single traveler he can only order 1 visit
//		OrderComboBox.valueProperty().addListener((obs, oldItem, newItem) -> {
//			if (newItem == null) {
//			} else {
//				if (newItem.toString().equals(Order.OrderType.SINGLE.toString())) {
//					txtVisitorsNum.setText("1");
//					txtVisitorsNum.setDisable(true);
//
//				} else {
//					txtVisitorsNum.setText("");
//					txtVisitorsNum.setPromptText("Visitor's Number");
//					txtVisitorsNum.setDisable(false);
//				}
//			}
//		});
//		
//		//Setting park times
//		LocalTime startTime = LocalTime.of(8, 0);
//        LocalTime endTime = LocalTime.of(18, 0);
//
//        List<LocalTime> hoursList = new ArrayList<>();
//        LocalTime currentTime = startTime;
//
//        while (!currentTime.isAfter(endTime)) {
//            hoursList.add(currentTime);
//            currentTime = currentTime.plusHours(1);
//        }
//
//        TimeComboBox.getItems().addAll(hoursList); //adding them to the Time combo box
//	}
//	
//	
//	// This function checks if the given input is valid
//	private boolean isValidInput() {
//		String visitorsNumber = txtVisitorsNum.getText();
//		String email = txtEmail.getText();
//		String parkName = new String();
//		if (ParkComboBox.getValue() != null)
//			parkName = ParkComboBox.getValue().toString();
//		String TravelerId = txtID.getText();
//		String Phone = txtPhone.getText();
//		String Time = new String();
//			if(TimeComboBox.getValue() != null)
//				Time = TimeComboBox.getValue().toString();
//
//		if (visitorsNumber.isEmpty() || email.isEmpty() || parkName.isEmpty() || TravelerId.isEmpty()
//				|| Phone.isEmpty() || txtDate.getValue()==null || Time.isEmpty()) {
//			new Alerts(AlertType.ERROR, "Bad Input", "Bad Input", "Please all the required fields").showAndWait();
//		} 
//		else if (!checkCurrentTime())
//			new Alerts(AlertType.ERROR, "Bad Input", "Bad Date Input", "Please select future date").showAndWait();
//		else if(TravelerId.length() != 9)
//			new Alerts(AlertType.ERROR, "Bad Input", "Bad ID Input", "ID must be 9 digits").showAndWait();
//		else if (Integer.parseInt(visitorsNumber) > 15
//				&& Order.OrderType.GROUP.toString().equals(OrderComboBox.getValue().toString())) {
//			new Alerts(AlertType.ERROR, "Bad Input", "Invalid Visitor's Number",
//					"Group order cannot exceed 15 visitors").showAndWait();
//		}
//		else if (Integer.parseInt(visitorsNumber) < 1) {
//			new Alerts(AlertType.ERROR, "Bad Input", "Invalid Visitor's Number",
//					"Visitor's number must be positive number and atleast 1. ").showAndWait();
//		}
//		else if (checkTooLate()) {
//			new Alerts(AlertType.ERROR, "Bad Input", "Invalid Date",
//					"You can't order for more than a year in advance ").showAndWait();
//		}
//		else if (!validInput("Email", email)) {
//			new Alerts(AlertType.ERROR, "Bad Input", "Invalid Email",
//					"Insert a valid Email please").showAndWait();
//		}
//		else if (!validInput("Phone", Phone)) {
//			new Alerts(AlertType.ERROR, "Bad Input", "Invalid Phone",
//					"Insert a valid Phone number please").showAndWait();
//		}
//		else if (!validInput("AmountVisitor", txtVisitorsNum.getText())) {
//			new Alerts(AlertType.ERROR, "Bad Input", "Invalid Visitors amount",
//					"Insert a valid visitors amount please").showAndWait();
//		} else {
//			return true;
//		}
//		
//		return false;
//	}
//
//	//This function check if the date,time entered is valid
//	public boolean checkCurrentTime() { 
//		LocalDate date = txtDate.getValue(); //Date wrote
//		String[] arrSplit = TimeComboBox.getValue().toString().split("-");
//		String[] hour = arrSplit[0].split(":");
//		LocalTime arrivalTime = LocalTime.of(Integer.parseInt(hour[0]), 00, 00);  
//
//		LocalTime now = LocalTime.now();
//		if (date.compareTo(LocalDate.now()) <= 0 && now.compareTo(arrivalTime) >= 0) { //check if date and hour is correct
//			return false;
//		}
//		return true;
//	}
//	
//	//This function disable ordering to one year + in advance
//	private boolean checkTooLate() {
//	    LocalDate date = txtDate.getValue();  
//	    
//	    LocalDate today = LocalDate.now();  // Get the current date
//	    LocalDate nextYear = LocalDate.of(today.getYear() + 1, today.getMonth(), today.getDayOfMonth());  // Calculate the date for next year
//	    
//	    if (date.compareTo(nextYear) > 0) {  // Compare the selected date with next year
//	        return true;  // Return true if the selected date is later than next year
//	    }
//	    return false;  
//	}
//	
//	//This function helps checking if the input is valid
//	public static boolean validInput(String nameMethod, String txt) {
//		Matcher matcher = null;
//		if (nameMethod.equals("Email")) {
//			matcher = MailCheck.matcher(txt);
//		} else if (nameMethod.equals("AmountVisitor")) {
//			matcher = VisitorsAmountCheck.matcher(txt);
//		} else if (nameMethod.equals("Phone")) {
//			matcher = PhoneCheck.matcher(txt);
//		}
//		return matcher.find();
//	} 
//
//	//This function calculates the price of order
//	private Double CalculatePrice() {
//			double discountprice;
//			// Solo/family pre-order 
//
//			if ((order.getOrdertype().equals(Order.OrderType.SINGLE) || order.getOrdertype().equals(Order.OrderType.FAMILY))){
//
//				discountprice = 100*Integer.parseInt(txtVisitorsNum.getText())*0.85;
//				return discountprice;
//			}
//
//			//Group pre-order
//			if ((order.getOrdertype().equals(Order.OrderType.GROUP))){
//				discountprice = 100*(Integer.parseInt(txtVisitorsNum.getText())-1)*0.75*0.88; 
//				//אמרו שהתשלום מתבצע מחוץ למערכת אז אין לי מושג איך להתייחס לקטע של ה12 אחוז הנחה לתשלום מראש
//				return discountprice;
//			}
//			return 0.0;
//			
//	}
//	
//}