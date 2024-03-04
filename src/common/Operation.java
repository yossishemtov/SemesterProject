package common;

public abstract class Operation {
    // Structure of a command: COMMAND nameOfCommand
    
    // GET COMMANDS
    public final static String GETALLORDERS = "Get all orders";
    public final static String GETTRAVLERINFO = "Get traveler information";
    public final static String GETTRAVLERLOGINDETAILS = "Get traveler login details";
    public final static String GETALLREPORTS = "Get all reports";
    public final static String GETSPECIFICREPORT = "Get a specific report";
    public final static String GETMESSAGES = "Get messages";
    public final static String Disconnecting = "Disconnecting from server";
    public final static String GETPROFILE = "Get user profile";
    public final static String GETAMOUNTOFVISITORS = "Get amount of visitors";
    
    // POST COMMANDS
    public final static String POSTNEWTRAVLERGUIDER = "Post new traveler guide request";
    public final static String POSTTRAVLERORDER = "Post new traveler order";
    public final static String POSTNEWREPORT = "Post a new report";
    
    // PATCH COMMANDS
    public final static String PATCHEXISTORDER = "Patch existing order";
    public final static String PATCHPARKPARAMETERS = "Patch park parameters";
    public final static String PATCHORDERSTATUS = "Patch order status";
    
    // DELETE COMMANDS
    public final static String DELETEEXISTINGORDER = "Delete an existing order";
}
