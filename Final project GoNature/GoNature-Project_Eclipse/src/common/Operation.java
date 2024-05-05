package common;


/**
 * The Operation class contains constants representing various operations
 * That are passing between the client and the server.
 */
public abstract class Operation {
	
    // Structure of a command: COMMAND nameOfCommand
    
    // GET COMMANDS
	/**
     * Retrieves all orders.
     */
    public final static String GET_ALL_ORDERS = "Get all orders";
    
    /**
     * Retrieves traveler information.
     */
    public final static String GET_TRAVLER_INFO = "Get traveler information";
    
    /**
     * Retrieves traveler login details.
     */
    public final static String GET_TRAVLER_LOGIN_DETAILS = "Get traveler login details";
    
    /**
     * Retrieves the last order ID.
     */
    public final static String GET_LAST_ORDER_ID = "Get last order id";

    /**
     * Retrieves the most recent order of a traveler by ID.
     */
    public final static String GET_RECENT_ID_TRAVELER = "Get most recent order of traveler by id";
    
    /**
     * Retrieves information about all the parks.
     */
    public final static String GET_PARKS_INFO = "Get information about all the parks";
    
    /**
     * Retrieves information about a park based on its name.
     */
    public final static String GET_PARK_BY_NAME = "Get information about a park based on name";
    
    /**
     * Retrieves details of a general park worker.
     */
    public final static String GET_GENERAL_PARK_WORKER_DETAILS = "Get GeneralParkWorker details";
    
    /**
     * Retrieves park details based on its number.
     */
    public final static String GET_PARK_DETAILS = "Get Park details based on his number";
    
    /**
     * Retrieves all reports.
     */
    public final static String GET_ALL_REPORTS = "Get all reports";
    
    /**
     * Retrieves a visitors report.
     */
    public final static String GET_VISITORS_REPORT = "Get visitors report report";
    
    /**
     * Get traveler messages from the system
     */
    public final static String GET_MESSAGES = "Get traveler messages from the system";
    
    /**
     * Disconnecting client from server
     */
    public final static String DISCONNECTING = "Disconnecting from server";
    
    /**
     * Get user profile information
     */
    public final static String GET_PROFILE = "Get user profile";
    
    /**
     * Get amount of visitors in the park
     */
    public final static String GET_AMOUNT_OF_VISITORS = "Get amount of visitors";
    
    /**
     * Get status of generalparkworker, if signed in or out
     */
    public final static String GET_GENERALPARKWORKER_SIGNED = "Get status of signed in or out of generalparkworker";
    
    /**
     * Get status of traveler if signed in or out
     */
    public final static String GET_TRAVELER_SIGNED = "Get status of signed in or out of traveler";
    
    /**
     * Get amount of visitors for where the park worker works
     */
    public final static String GET_AMOUNT_OF_VISITORS_FOR_GENERALPARKWORKER = "Get amount of visitors for park worker";
    
    /**
     * Get all waiting Lists
     */
    public final static String GET_ALL_WAITING_LISTS = "Get all waiting Lists";

    /**
     * Get order information by id and park number and date
     */
    public final static String GET_ORDER_BY_ID_AND_PARK_NUMBER_DATE = "Get order information by id and park number and date";
    
    /**
     * Get last waitingList Id
     */
    public final static String GET_LAST_WAITINGLIST = "Get last waitingList Id";

    /**
     * Find right placeinList in waitinglist
     */
    public final static String FIND_PLACE_IN_WAITING_LIST = "Find right placeinList in waitinglist";

    /**
     * Post new traveler guide request
     */
    public final static String POST_NEW_TRAVLER_GUIDER = "Post new traveler guide request";
    
    /**
     * Post new traveler to system
     */
    public final static String POST_NEW_TRAVLER = "Post new traveler";

    /**
     * Post new Waiting List to system
     */
    public final static String POST_NEW_WAITING_LIST = "Post new Waiting List";

    /**
     * Post existing traveler guide request
     */
    public final static String POST_EXISTS_TRAVLER_GUIDER = "Post exists traveler guide request";

    /**
     * Post new traveler order to system
     */
    public final static String POST_TRAVLER_ORDER = "Post new traveler order";
    
    /**
     * Post a new report to system
     */
    public final static String POST_NEW_REPORT = "Post a new report";
    
    /**
     * Post a new visit to system
     */
    public final static String POST_NEW_VISIT = "Post new visit";
    
    /**
     * Get all the orders within given dates
     */
    public final static String FIND_ORDERS_WITHIN_DATES = "Get all the orders within given dates";
    
    /**
     * Finding available dates to reschedule a visit
     */
    public final static String FIND_AVAILABLE_DATES = "Finding available dates to reschedule";

    /**
     * Receive all orders
     */
    public final static String RESPONSE_ALL_ORDER = "server send all order";
    
    // PATCH COMMANDS
    
    /**
     * Patch an existing order
     */
    public final static String PATCH_EXIST_ORDER = "Patch existing order";
    
    /**
     * Patch park parameters
     */
    public final static String PATCH_PARK_PARAMETERS = "Patch park parameters";
    
    /**
     * Patch an order status
     */
    public final static String PATCH_ORDER_STATUS = "Patch order status";
    
    /**
     * Patch generalparkworker to signedin status
     */
    public final static String PATCH_GENERALPARKWORKER_SIGNEDIN = "Patch generalparkworker to signed in";
    
    /**
     * Patch generalparkworker to signedout status
     */
    public final static String PATCH_GENERALPARKWORKER_SIGNEDOUT = "Patch generalparkworker to signed out";
    
    /**
     * Patch traveler to signedin status
     */
    public final static String PATCH_TRAVELER_SIGNEDIN = "Patch traveler to signed in";
    
    /**
     * Patch traveler to signedout status
     */
    public final static String PATCH_TRAVELER_SIGNEDOUT = "Patch traveler to signed out";
    
    /**
     * Patch an order to be INPARK status
     */
    public final static String PATCH_ORDER_STATUS_TO_INPARK = "Patch an order to be INPARK status";
    
    /**
     * Patch a park visitors number
     */
    public final static String PATCH_PARK_VISITORS = "Patch a park visitors number";
    
    
    // DELETE COMMANDS
    
    /**
     * Delete an existing waiting List
     */
    public static final String DELETE_EXISTING_WAITING_LIST = "Delete an existing waiting List";
    
    /**
     * Delete an existing order
     */
    public final static String DELETE_EXISTING_ORDER = "Delete an existing order";
    
    
    //report
    
    /**
     * Get hourly visit data for park
     */
    public final static String GET_HOURLY_VISIT_DATA_FOR_PARK = "Get hourly visit data for park";
    
    /**
     * Post visitor report and general report from park manager
     */
    public final static String POST_VISITORS_REPORT = "post visitor report and general report from park maneger";
    
    /**
     * Get new visitors report
     */
    public final static String GET_NEW_VISITORS_REPORT = "Get new visitors report";
    
    /**
     * Get exists visitors report
     */
    public final static String GET_EXISTS_VISITORS_REPORT = "Get exists visitors report";
    
    /**
     * Get exists general report
     */
    public final static String GET_GENERAL_REPORT = "Get exists general report";
    
    /**
     * Get Cancellation Report report
     */
    public final static String GET_CANCELLATION_REPORT = "Get Cancellation Report";

    
    //Usage report
    
    /**
     * Get exists usage report
     */
    public final static String GET_EXISTS_USAGE_REPORT = "Get exists usage report";
    
    /**
     * Post usage report and general report from park manager
     */
    public final static String POST_USAGE_REPORT = "post usage report and general report from park maneger";
    
    /**
     * Get new usage report
     */
    public final static String GET_NEW_USAGE_REPORT = "Get new usage report";

    //visit report 
    
    /**
     * Get new visit report for park deparment
     */
    public final static String GET_NEW_VISIT_REPORT = "Get new visit report for park deparment";

    
    // Operations related to Change Requests
    
    /**
     * Get max change request ID
     */
    public final static String GET_MAX_CHANGE_REQUEST_ID = "Get max change request ID";
    
    /**
     * Post new change request
     */
    public final static String POST_NEW_CHANGE_REQUEST = "Post new change request";
    
    /**
     * Get change requests
     */
    public final static String GET_CHANGE_REQUESTS = "Get change requests";
    
    /**
     * Patch change request status
     */
    public final static String PATCH_CHANGE_REQUEST_STATUS = "Patch change request status";

    /**
     * Get unordered visits from the park
     */
    public final static String GET_PARK_UNORDEREDVISITS = "Get unordered visits from the park";

  
    /**
     * Patch an order to be COMPLETED status
     */
    public final static String PATCH_ORDER_STATUS_TO_COMPLETED = "Patch an order to be COMPLETED status";
    
    /**
     * Patch a park visitors number - deduct
     */
    public final static String PATCH_PARK_VISITORS_DEDUCT = "Patch a park visitors number - deduct";
    
    /**
     * Patch the amount of unordered visits
     */
    public final static String PATCH_PARK_UNORDEREDVISITS = "Patch the amount of unordered visits";
    
    
    /**
     * Get orders with status PENDING_EMAIL_SENT
     */
    public final static String GET_STATUS_PENDING_NOTIFICATION_BY_TRAVELERID = "Get orders with status PENDING_EMAIL_SENT";
    
    /**
     * Get orders with status CANCELEDBYSERVER
     */
    public final static String GET_STATUS_CANCELED_NOTIFICATION_BY_TRAVELERID = "Get orders with status CANCELEDBYSERVER";

    
    /**
     * Get orders with status HAS_SPOT
     */
    public final static String GET_STATUS_HAS_SPOT = "Get orders with status HAS_SPOT"; 
    
    /**
     * Check spot availability in park
     */
    public static final String CHECK_SPOT_AVAILABILITY = "CHECK_SPOT_AVAILABILITY"; 
    
    /**
     * Checking order validity
     */
    public final static String CHECK_IF_ORDER_VALID = "Checking order validity"; 
    
    /**
     * Checking when the park is full
     */
    public final static String CHECK_PARK_FULL_DAYS = "Checking when the park is full"; 
    
    /**
     * Post new order from waitingList
     */
    public final static String POST_ORDER_FROM_WAITING = "Post new order from waitingList";
    
    /**
     * Patch order status with arraylist
     */
    public final static String PATCH_ORDER_STATUS_ARRAYLIST = "Patch order status with arraylist";
    
    /**
     * Patch an waitinglist status
     */
    public final static String PATCH_WAITING_STATUS = "Patch an waitinglist status";

    
 

}
