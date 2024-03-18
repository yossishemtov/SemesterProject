package common;

public abstract class Operation {
	
    // Structure of a command: COMMAND nameOfCommand
    
    // GET COMMANDS
    public final static String GET_ALL_ORDERS = "Get all orders";
    public final static String GET_TRAVLER_INFO = "Get traveler information";
    public final static String GET_TRAVLER_LOGIN_DETAILS = "Get traveler login details";
    
    public final static String GET_GENERAL_PARK_WORKER_DETAILS = "Get GeneralParkWorker details";
    public final static String GET_PARK_DETAILS = "Get Park details";
    public final static String GET_ALL_REPORTS = "Get all reports";
    public final static String GET_VISITORS_REPORT = "Get visitors report report";
    public final static String GET_MESSAGES = "Get traveler messages from the system";
    public final static String DISCONNECTING = "Disconnecting from server";
    public final static String GET_PROFILE = "Get user profile";
    public final static String GET_AMOUNT_OF_VISITORS = "Get amount of visitors";
    public final static String GET_GENERALPARKWORKER_SIGNED = "Get status of signed in or out of generalparkworker";
    public final static String GET_TRAVELER_SIGNED = "Get status of signed in or out of traveler";
    public final static String GET_AMOUNT_OF_VISITORS_FOR_GENERALPARKWORKER = "Get amount of visitors for park worker";
    public final static String GET_ORDER_BY_ID_AND_PARK_NUMBER_DATE = "Get order information by id and park number and date";
    

    public final static String POST_NEW_TRAVLER_GUIDER = "Post new traveler guide request";
    public final static String POST_EXISTS_TRAVLER_GUIDER = "Post exists traveler guide request";

    public final static String POST_TRAVLER_ORDER = "Post new traveler order";
    public final static String POST_NEW_REPORT = "Post a new report";
    public final static String POST_NEW_VISIT = "Post new visit";
    
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

    
    // Operations related to Change Requests
    public final static String GET_MAX_CHANGE_REQUEST_ID = "Get max change request ID";
    public final static String POST_NEW_CHANGE_REQUEST = "Post new change request";
    public final static String GET_CHANGE_REQUESTS = "Get change requests ";
    public final static String PATCH_CHANGE_REQUEST_STATUS = "Patch change request status";
    
    
}
