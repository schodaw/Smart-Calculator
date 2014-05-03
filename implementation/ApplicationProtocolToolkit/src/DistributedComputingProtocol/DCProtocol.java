/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DistributedComputingProtocol;

import ch.heigvd.res.toolkit.interfaces.IEventType;
import ch.heigvd.res.toolkit.interfaces.IMessageType;
import ch.heigvd.res.toolkit.interfaces.IState;

/**
 * This class is used to define constants and enum types specific to the
 * Distributed Computing protocol.
 *
 * @author Dominique Jollien
 */
public class DCProtocol {
   /**
	 * The default TCP port on which the server is accepting connection requests
	 */
	public final static int DEFAULT_PORT = 6060;

	/**
	 * This enum type defines the possible states for the Dynamic Computing prococol
	 * state machine
	 */
	public enum State implements IState {
		STATE_START,
                STATE_AUTHENTICATION,
                STATE_REGISTRATION,
		STATE_WAITING_COMPUTING_REQUEST, 
		STATE_COMPUTE_REQUEST, 
		STATE_WAITING_LIST_INPUTS,
                STATE_WAITING_INPUTS,
                STATE_END;
	}
	
	/**
	 * This enum type defines the specific event types understood by the Dynamic Computing
	 * protocol state machine. Note that there is another, generic enum type defined
	 * in ch.heigvd.res.toolkit.impl.EventType
	 */
	public enum EventType implements IEventType {
		EVENT_TYPE_MAX_IDLE_TIME_REACHED;
	}
	
	/**
	 * This enum type defines the protocol commands defined by the Dynamic Computing protocol
	 * The commands are encapsulated in messages
	 */
	public enum Command {
		CMD_CONNECTION("CONNECTION_REQUEST", "Ask for connection to the Compute Engine."),
                CMD_LOGIN("USER_CREDENTIALS","Ask for login to the Compute Engine."),
		CMD_REGISTER("REGISTRATION_REQUEST", "Ask for registration of a new user to the Compute Engine."),
                CMD_REGISTER_VALUE("NEW_USER_CREDENTIALS", "Give the credentials for a new user to be registered to the Compute Engine."),
		CMD_COMPUTING_REQUEST("COMPUTING_REQUEST", "Ask for the specified computing request."),
                CMD_INPUT_REPLY("INPUT_REPLY", "Give an input value."),
                CMD_LIST_INPUT_REPLY("LIST_INPUT_REPLY", "Give an input value."),
                CMD_LIST_INPUT_ENDOFREPLY("LIST_INPUT_ENDOFREPLY", "Terminate the list of input values."),
		CMD_BYE("bye", "Close the session."),
		CMD_HELP("help", "Display help message.");
		
		private final String keyword;
		private final String help;
		
		Command(String keyword, String help) {
			this.keyword = keyword;
			this.help = help;
		}

		public String getKeyword() {
			return keyword;
		}

		public String getHelp() {
			return help;
		}
		
	}

	/**
	 * This enum type defines the types of messages that can be exchanged by
	 * clients and servers using the Dynamic Computing protocol. A "command" is a message
	 * sent by a client to a server. A "result" is a message sent by a server to
	 * a client, in response to a previous "command". A "notification" is a message
	 * sent by a server to a client (without a previous "command")
	 */
	public enum MessageType implements IMessageType {
		MESSAGE_TYPE_COMMAND,
		MESSAGE_TYPE_RESULT,
		MESSAGE_TYPE_NOTIFICATION
	}
	
	public static String NOTIFICATION_WELCOME_TEXT = "Welcome to the Compute Engine. Here are my available compute functions.";
	public static String NOTIFICATION_BYE_TEXT = "bye bye";
	public static String NOTIFICATION_CLOSING_TEXT = "closing the connection...";
	
        public static String RESULT_LOGIN_REQUIRED = "You have to log in.";
        public static String RESULT_LOGIN_FAILURE = "Login failure.";
        public static String RESULT_FUNCTIONS_LIST = "Available compute functions :";
        public static String RESULT_REGISTRATION_ASKED = "You have to registrer a new account";
        public static String RESULT_REGISTRATION_FAILURE = "Registration failure.";
        public static String RESULT_REGISTRATION_SUCCESS = "Registration success.";
	public static String RESULT_COMPUTE_FUNCTION = "We are going to compute the function.";
        public static String RESULT_UNKOWN_FUNCTION = "Wrong compute function name.";
        public static String RESULT_INPUTS_REQUEST = "X Input values are required.";
        public static String RESULT_LIST_INPUTS_REQUEST = "Input values are required.";
        public static String RESULT_COMPUTE_RESULT = "Here is the result :";
        public static String RESULT_COMPUTE_FAILURE = "Compute failure";
	public static String RESULT_BYE_TEXT = "bye bye";
	public static String RESULT_INVALID_CMD_TEXT = "Invalid command";
	
	public static String MESSAGE_ATTRIBUTE_PAYLOAD = "payload";
	public static String MESSAGE_ATTRIBUTE_STATUS_CODE = "statusCode";
}
