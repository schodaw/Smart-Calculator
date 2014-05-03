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
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Zak
 */
public class DCPDynamicDiscoveryComputeEngine {

    static final Logger LOG = Logger.getLogger(DCPDynamicDiscoveryComputeEngine.class.getName());
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int port = 5050;
        DatagramSocket socket;
        try {
            socket = new DatagramSocket();
            socket.setBroadcast(true);
            byte[] message = ("HERE_I_AM:6060").getBytes();
            byte[] buffer = new byte[2048];
            DatagramPacket datagramReceived = new DatagramPacket(buffer, buffer.length);
            
            try {
                DatagramPacket datagramSend = new DatagramPacket(message, message.length, InetAddress.getByName("255.255.255.255"), port);
                socket.send(datagramSend);
                while (true) {
                    socket.receive(datagramReceived);
                    if (datagramReceived.getData().equals("HELLO")) {
                        
                    }
                                
                }
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, ex.getMessage(), ex);
            }
        } catch (SocketException ex) {
            Logger.getLogger(DCPDynamicDiscoveryComputeEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
