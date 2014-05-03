/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dcpdynamicdiscoverycomputeengine;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
    
/**
 *
 * @author Zak
 */
public class DCPDynamicDiscoveryComputeEngine implements Runnable {

   
   private DatagramSocket socket;

   public DCPDynamicDiscoveryComputeEngine() {
      try {
         try {
            socket = new DatagramSocket(DCProtocol.DEFAULT_SERVER_DISCOVERY_PORT, InetAddress.getByName("0.0.0.0"));
            socket.setBroadcast(true);
         } catch (SocketException ex) {
            Logger.getLogger(DCPDynamicDiscoveryComputeEngine.class.getName()).log(Level.SEVERE, null, ex);
         }
      } catch (UnknownHostException ex) {
         Logger.getLogger(DCPDynamicDiscoveryComputeEngine.class.getName()).log(Level.SEVERE, null, ex);
      }
   }
    
    public void sendHEREIAM(InetAddress adresse, int port) {
      try {
         byte[] message = (DCProtocol.CMD_HERE_I_AM + ":" + DCProtocol.DEFAULT_PORT).getBytes();
         DatagramPacket datagram = new DatagramPacket(message, message.length, adresse, port);
         socket.send(datagram);
      } catch (IOException ex) {
         Logger.getLogger(DCPDynamicDiscoveryComputeEngine.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
   
    public void startUp() {
      try {
         /*
          * Announcing the compute engine with a broadcasted HERE_I_AM
          */
         sendHEREIAM(InetAddress.getByName("255.255.255.255"), DCProtocol.DEFAULT_CLIENT_DISCOVERY_PORT);

         /*
          * Launching the listening thread
          */
         new Thread(this).start();
      } catch (UnknownHostException ex) {
         Logger.getLogger(DCPDynamicDiscoveryComputeEngine.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   @Override
   public void run() {
      while (true) {
              byte[] buffer = new byte[2048];
              DatagramPacket datagram = new DatagramPacket(buffer, buffer.length);
              try {
                  socket.receive(datagram);
                  String msg = new String(datagram.getData(), datagram.getOffset(), datagram.getLength());

                  //si le message est un HELLO on répond avec HERE_I_AM
                  if(msg.equals(DCProtocol.CMD_HELLO)) {
                     sendHEREIAM(datagram.getAddress(), datagram.getPort());
                  }

                  System.out.println("HELLO recu depuis : " + datagram.getAddress() + ":" + datagram.getPort());

              } catch (IOException ex) {
                      Logger.getLogger(DCPDynamicDiscoveryComputeEngine.class.getName()).log(Level.SEVERE, null, ex);
              }
      }
   }
   
   public static void main(String[] args) {
     DCPDynamicDiscoveryComputeEngine computeEngine = new DCPDynamicDiscoveryComputeEngine();
     computeEngine.startUp();
   }        
}
