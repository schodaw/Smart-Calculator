package dcpdynamicdiscoverysmartcalculator;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jollien Dominique, Saam Frédéric
 */
public class DCPDynamicDiscoverySmartCalculator {
   
   private DatagramSocket announcementSocket;

   public DCPDynamicDiscoverySmartCalculator() {
      try {
         announcementSocket = new DatagramSocket();
         announcementSocket.setBroadcast(true);
      } catch (SocketException ex) {
         Logger.getLogger(DCPDynamicDiscoverySmartCalculator.class.getName()).log(Level.SEVERE, null, ex);
      }
      
   }
   public static void main(String[] args) {
      DCPDynamicDiscoverySmartCalculator smartCalculator = new DCPDynamicDiscoverySmartCalculator();
      smartCalculator.start();
      
      		while (true) {
			try {
				byte[] payload = ("HELLO").getBytes();
				DatagramPacket datagram = new DatagramPacket(payload, payload.length, InetAddress.getByName("255.255.255.255"), DCProtocol.DEFAULT_LISTENING_PORT);
				socket.send(datagram);
				LOG.log(Level.INFO, "Datagram broadcasted to local network nodes.");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ex) {
					LOG.log(Level.SEVERE, ex.getMessage(), ex);
				}
			} catch (IOException ex) {
				LOG.log(Level.SEVERE, ex.getMessage(), ex);
			}
		}
   }
}
