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
    public final static String GET_MESSAGES = "Get messages";
    public final static String DISCONNECTING = "Disconnecting from server";
    public final static String GET_PROFILE = "Get user profile";
    public final static String GET_AMOUNT_OF_VISITORS = "Get amount of visitors";
    public final static String GET_GENERALPARKWORKER_SIGNED = "Get status of signed in or out of generalparkworker";
    public final static String GET_AMOUNT_OF_VISITORS_FOR_GENERALPARKWORKER = "Get amount of visitors for park worker";
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
    
    public final static String FIND_ORDERS_WITHIN_DATES = "Get all the orders within given dates"; //emanuel

    // PATCH COMMANDS
    public final static String PATCH_EXIST_ORDER = "Patch existing order";
    public final static String PATCH_PARK_PARAMETERS = "Patch park parameters";
    public final static String PATCH_ORDER_STATUS = "Patch order status";
    public final static String PATCH_GENERALPARKWORKER_SIGNEDIN = "Patch generalparkworker to signed in";
    public final static String PATCH_GENERALPARKWORKER_SIGNEDOUT = "Patch generalparkworker to signed out";

    
    // DELETE COMMANDS
    public final static String DELETE_EXISTING_ORDER = "Delete an existing order";
    public final static String RESPONSE_ALL_ORDER = "server send all order";
    
}
