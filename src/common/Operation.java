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
    public final static String GET_MESSAGES = "Get messages";
    public final static String DISCONNECTING = "Disconnecting from server";
    public final static String GET_PROFILE = "Get user profile";
    public final static String GET_AMOUNT_OF_VISITORS = "Get amount of visitors";
    public final static String GET_GENERALPARKWORKER_SIGNED = "Get status of signed in or out of generalparkworker";
    public final static String GET_AMOUNT_OF_VISITORS_FOR_GENERALPARKWORKER = "Get amount of visitors for park worker";
    

    public final static String POST_NEW_TRAVLER_GUIDER = "Post new traveler guide request";
    public final static String POST_EXISTS_TRAVLER_GUIDER = "Post exists traveler guide request";

    public final static String POST_TRAVLER_ORDER = "Post new traveler order";
    public final static String POST_NEW_REPORT = "Post a new report";
    
    // PATCH COMMANDS
    public final static String PATCH_EXIST_ORDER = "Patch existing order";
    public final static String PATCH_PARK_PARAMETERS = "Patch park parameters";
    public final static String PATCH_ORDER_STATUS = "Patch order status";
    public final static String PATCH_GENERALPARKWORKER_SIGNEDIN = "Patch generalparkworker to signed in";
    public final static String PATCH_GENERALPARKWORKER_SIGNEDOUT = "Patch generalparkworker to signed out";

    
    // DELETE COMMANDS
    public final static String DELETE_EXISTING_ORDER = "Delete an existing order";
    public final static String RESPONSE_ALL_ORDER = "server send all order";
    
    
    // Operations related to Change Requests
    public final static String GET_MAX_CHANGE_REQUEST_ID = "Get max change request ID";
    public final static String POST_NEW_CHANGE_REQUEST = "Post new change request";
    public final static String GET_CHANGE_REQUESTS_WAITING_FOR_APPROVAL = "Get change requests waiting for approval";
    public final static String PATCH_CHANGE_REQUEST_STATUS = "Patch change request status";
    
    
}
