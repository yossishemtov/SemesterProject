package common;

public abstract class Operation {
    // Structure of a command: COMMAND nameOfCommand
    
    // GET COMMANDS
    public final static String GetAllOrders = "Get all orders";
    public final static String GetTravlerInfo = "Get traveler information";
    public final static String GetTravlerLoginDetails = "Get traveler login details";
    public final static String GetAllReports = "Get all reports";
    public final static String GetSpecificReport = "Get a specific report";
    public final static String GetMessages = "Get messages";
    public final static String Disconnecting = "Disconnecting from server";
    public final static String GetProfile = "Get user profile";
    public final static String GetAmountOfVisitors = "Get amount of visitors";
    
    // POST COMMANDS
    public final static String PostNewTravlerGuider = "Post new traveler guide request";
    public final static String PostTravlerOrder = "Post new traveler order";
    public final static String PostNewReport = "Post a new report";
    
    // PATCH COMMANDS
    public final static String PatchExistOrder = "Patch existing order";
    public final static String PatchParkParameters = "Patch park parameters";
    public final static String PatchOrderStatus = "Patch order status";
    
    // DELETE COMMANDS
    public final static String DeleteExistingOrder = "Delete an existing order";
    
    
    
    public final static String Responseallorder = "server send all order";
    
}
