package common;

public abstract class OperationsBetweenClientAndServer {
	//Stracture of a command COMMAND nameOfCommand
	
	
	//GET COMMANDS
	public final static String GETALLORDERS = "GETALLORDERS";
	
	public final static String GETTRAVLERINFO = "GETTRAVLERINFO";
	
	public final static String GETTRAVLERLOGINDETAILS = "GETTRAVLERLOGINDETAILS";
	
	public final static String GETALLREPORTS = "GETALLREPORTS";
	
	public final static String GETSPECIFICREPORT = "GETSPECIFICREPORT";
	
	public final static String GETMESSAGES = "GETMESSAGES";

	public final static String GETPROFILE = "GETPROFILE";
	
	public final static String GETAMOUNTOFVISITORS = "GETAMOUNTOFVISITORS";
	
	//POST COMMANDS
	public final static String POSTNEWTRAVLERGUIDER = "POSTNEWTRAVLERGUIDER";
	
	public final static String POSTTRAVLERORDER = "POSTTRAVLERORDER";
	
	public final static String POSTNEWREPORT = "POSTNEWREPORT";
	
	//PATCH COMMANDS
	public final static String PATCHEXISTORDER = "PATCHEXISTORDER";
	
	public final static String PATCHPARKPARAMETERS = "PATCHPARKPARAMETERS";
	
	public final static String PATCHORDERSTATUS = "PATCHORDERSTATUS";
	
	//DELETE COMMANDS
	public final static String DELETEEXISTINGORDER = "DELETEEXISTINGORDER";
	
}
