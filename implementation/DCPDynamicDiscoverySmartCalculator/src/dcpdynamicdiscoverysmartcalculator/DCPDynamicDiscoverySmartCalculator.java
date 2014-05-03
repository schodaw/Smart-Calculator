package dcpdynamicdiscoverysmartcalculator;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implémentation de la partie Dynamic Discovery des Smart Calculator du protocol Distributed Computing
 * @author Jollien Dominique, Saam Frédéric
 */
public class DCPDynamicDiscoverySmartCalculator implements Runnable {

   //we store the list of available compute engines with their listening TCP port for client connection
   private LinkedList<InetAddress> computeEnginesAdresses;
   private LinkedList<Integer> computeEnginesListeningPorts;
   
   public DCPDynamicDiscoverySmartCalculator() {
      computeEnginesAdresses = new LinkedList<InetAddress>();
      computeEnginesListeningPorts = new LinkedList<Integer>();
   }
   
   public void start() {
      /*
       * Announcing the Smart Calculator with a broadcasted HELLO
       */
       try {
         DatagramSocket announcementSocket = new DatagramSocket();
         announcementSocket.setBroadcast(true);
         
         byte[] payload = (DCProtocol.CMD_HELLO).getBytes();
         DatagramPacket datagram = new DatagramPacket(payload, payload.length, InetAddress.getByName("255.255.255.255"), DCProtocol.DEFAULT_LISTENING_PORT);
         announcementSocket.send(datagram);
      } catch (IOException ex) {
         Logger.getLogger(DCPDynamicDiscoverySmartCalculator.class.getName()).log(Level.SEVERE, null, ex);
      }
      
      /*
       * Launching the listening thread
       */
      new Thread(this);
   }
   
   /*
    * Tâche qui écoute sur le port UDP de dynamic Discovery pour recevoir les annonces de Compute Engine
    */
   @Override
   public void run() {
      try {
         DatagramSocket listeningSocket = new DatagramSocket(DCProtocol.DEFAULT_LISTENING_PORT);
         while (true) {
                 byte[] buffer = new byte[2048];
                 DatagramPacket datagram = new DatagramPacket(buffer, buffer.length);
                 try {
                         listeningSocket.receive(datagram);
                         String msg = new String(datagram.getData(), datagram.getOffset(), datagram.getLength());
                         
                         //si le message est un HERE_I_AM on stoque l'addresse envoyée
                         if(msg.startsWith(DCProtocol.CMD_HERE_I_AM)) {
                            String[] chunks = msg.split(":");
                            computeEnginesAdresses.push(datagram.getAddress());
                            computeEnginesListeningPorts.push(Integer.valueOf(chunks[1]));
                         }
                         
                         
                         System.out.println("Liste des adresses stockées : ");
                         for (int i = 0; i < computeEnginesAdresses.size(); i++) {
                              System.out.println(computeEnginesAdresses.get(i) + ":" + computeEnginesListeningPorts.get(i));
                         }
                         
                         
                 } catch (IOException ex) {
                         Logger.getLogger(DCPDynamicDiscoverySmartCalculator.class.getName()).log(Level.SEVERE, null, ex);
                 }
         }
      } catch (SocketException ex) {
         Logger.getLogger(DCPDynamicDiscoverySmartCalculator.class.getName()).log(Level.SEVERE, null, ex);
      }
   }
   
   public static void main(String[] args) {
      DCPDynamicDiscoverySmartCalculator smartCalculator = new DCPDynamicDiscoverySmartCalculator();
      smartCalculator.start();
   }
}
