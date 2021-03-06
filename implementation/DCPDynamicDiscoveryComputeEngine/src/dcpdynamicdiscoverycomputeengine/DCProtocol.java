/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dcpdynamicdiscoverycomputeengine;

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
        public final static int DEFAULT_SERVER_DISCOVERY_PORT = 5050;
        public final static int DEFAULT_CLIENT_DISCOVERY_PORT = 4040;
        
        //structure d'un message HELLO : "HELLO"
        public final static String CMD_HELLO = "HELLO";
        //structure d'un message HERE_I_AM : "HERE_I_AM":TCP port number
	public final static String CMD_HERE_I_AM = "HERE_I_AM";
}
