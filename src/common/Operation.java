package common;

public abstract class Operation {
	
    // Structure of a command: COMMAND nameOfCommand
    
    // GET COMMANDS
    public final static String GET_ALL_ORDERS = "Get all orders";
    public final static String GET_TRAVLER_INFO = "Get traveler information";
    public final static String GET_TRAVLER_LOGIN_DETAILS = "Get traveler login details";
    public final static String GET_LAST_ORDER_ID = "Get last order id"; //emanuel

    public final static String GET_RECENT_ID_TRAVELER = "Get most recent order of traveler by id"; //emanuel
    //public final static String GET_PARK_BY_ID = "Find specific park based on id"; //emanuel
    public final static String GET_PARKS_INFO = "Get information about all the parks"; //emanuel
    public final static String GET_PARK_BY_NAME = "Get information about a park based on name"; //emanuel
    public final static String GET_GENERAL_PARK_WORKER_DETAILS = "Get GeneralParkWorker details";
    public final static String GET_PARK_DETAILS = "Get Park details based on his number";
    public final static String GET_ALL_REPORTS = "Get all reports";
    public final static String GET_VISITORS_REPORT = "Get visitors report report";
    public final static String GET_MESSAGES = "Get traveler messages from the system";
    public final static String DISCONNECTING = "Disconnecting from server";
    public final static String GET_PROFILE = "Get user profile";
    public final static String GET_AMOUNT_OF_VISITORS = "Get amount of visitors";
    public final static String GET_GENERALPARKWORKER_SIGNED = "Get status of signed in or out of generalparkworker";
    public final static String GET_TRAVELER_SIGNED = "Get status of signed in or out of traveler";
    public final static String GET_AMOUNT_OF_VISITORS_FOR_GENERALPARKWORKER = "Get amount of visitors for park worker";
    public final static String GET_STATUS_PENDING_EMAIL = "Get orders with status PENDING_EMAIL_SENT"; //emanuel
    public final static String GET_ORDER_BY_ID_AND_PARK_NUMBER_DATE = "Get order information by id and park number and date";
    public final static String GET_STATUS_PENDING_NOTIFICATION_BY_TRAVELERID = "Get orders with pending email by travelerID";
    public final static String GET_STATUS_CANCELED_NOTIFICATION_BY_TRAVELERID = "Get orders that were canceled by the server";
    
    public static final String CHECK_SPOT_AVAILABILITY = "CHECK_SPOT_AVAILABILITY"; //emanuel
    public final static String CHECK_IF_ORDER_VALID = "Checking order validity"; //emanuel
    public final static String CHECK_PARK_FULL_DAYS = "Checking when the park is full"; //emanuel

    
    public final static String GET_LAST_WAITINGLIST = "Get last waitingList Id";
    public final static String FIND_PLACE_IN_WAITING_LIST = "Find right placeinList in waitinglist";

    public final static String POST_NEW_TRAVLER_GUIDER = "Post new traveler guide request";
    public final static String POST_NEW_TRAVLER = "Post new traveler";

    public final static String POST_NEW_WAITING_LIST = "Post new Waiting List";

    public final static String POST_EXISTS_TRAVLER_GUIDER = "Post exists traveler guide request";

    public final static String POST_TRAVLER_ORDER = "Post new traveler order";
    public final static String POST_NEW_REPORT = "Post a new report";
    public final static String POST_NEW_VISIT = "Post new visit";
    
    public final static String FIND_ORDERS_WITHIN_DATES = "Get all the orders within given dates"; //emanuel

    // PATCH COMMANDS
    public final static String PATCH_EXIST_ORDER = "Patch existing order";
    public final static String PATCH_PARK_PARAMETERS = "Patch park parameters";
    public final static String PATCH_ORDER_STATUS = "Patch order status";
    public final static String PATCH_GENERALPARKWORKER_SIGNEDIN = "Patch generalparkworker to signed in";
    public final static String PATCH_GENERALPARKWORKER_SIGNEDOUT = "Patch generalparkworker to signed out";
    public final static String PATCH_TRAVELER_SIGNEDIN = "Patch traveler to signed in";
    public final static String PATCH_TRAVELER_SIGNEDOUT = "Patch traveler to signed out";
    public final static String PATCH_ORDER_STATUS_TO_INPARK = "Patch an order to be INPARK status";
    public final static String PATCH_PARK_VISITORS_APPEND = "Patch a park visitors number - append";
    public final static String GET_ALL_WAITING_LISTS = "Get all waiting Lists";
    public final static String DELETE_EXISTING_WAITING_LIST = "Delete an existing waiting List";
    public final static String PATCH_ORDER_STATUS_ARRAYLIST = "Patch order status - arraylist";
    
    // DELETE COMMANDS
    public final static String DELETE_EXISTING_ORDER = "Delete an existing order";
    public final static String RESPONSE_ALL_ORDER = "server send all order";
    
    //report
    public final static String GET_HOURLY_VISIT_DATA_FOR_PARK = "Get hourly visit data for park";
    public final static String POST_VISITORS_REPORT = "post visitor report and general report from park maneger";
    public final static String GET_NEW_VISITORS_REPORT = "Get new visitors report";
    public final static String GET_EXISTS_VISITORS_REPORT = "Get exists visitors  report";
    public final static String GET_GENERAL_REPORT = "Get exists general report ";
    
    //Usage report
    public final static String GET_EXISTS_USAGE_REPORT = "Get exists usage  report";
    public final static String POST_USAGE_REPORT = "post usage report and general report from park maneger";
    public final static String GET_NEW_USAGE_REPORT = "Get new usage report";

    //visit report 
    public final static String GET_NEW_VISIT_REPORT = "Get new visit report for park deparment";

    
    // Operations related to Change Requests
    public final static String GET_MAX_CHANGE_REQUEST_ID = "Get max change request ID";
    public final static String POST_NEW_CHANGE_REQUEST = "Post new change request";
    public final static String GET_CHANGE_REQUESTS = "Get change requests ";
    public final static String PATCH_CHANGE_REQUEST_STATUS = "Patch change request status";
    
    
    
    
    
    public final static String GET_PARK_UNORDEREDVISITS = "Get unordered visits from the park";

  
   
    public final static String PATCH_ORDER_STATUS_TO_COMPLETED = "Patch an order to be COMPLETED status";
    public final static String PATCH_PARK_VISITORS_DEDUCT = "Patch a park visitors number - deduct";
    public final static String PATCH_PARK_UNORDEREDVISITS = "Patch the amount of unordered visits";
    
 

}
