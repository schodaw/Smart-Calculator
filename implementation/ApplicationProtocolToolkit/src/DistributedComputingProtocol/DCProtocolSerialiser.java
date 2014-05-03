/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DistributedComputingProtocol;

import DistributedComputingProtocol.DCProtocol.Command;
import ch.heigvd.res.toolkit.impl.InvalidMessageException;
import ch.heigvd.res.toolkit.impl.Message;
import ch.heigvd.res.toolkit.interfaces.IProtocolSerializer;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import javax.json.*;

/**
 *
 * @author admin
 */
public class DCProtocolSerialiser implements IProtocolSerializer {

   @Override
   public Message deserialize(byte[] data) throws InvalidMessageException {      
      
      String sData = new String(data);
      String[] tokens = sData.split(" : ");
      List<String> arguments = new LinkedList<>();
      for (int i=1; i<tokens.length; i++) {
              arguments.add(tokens[i]);
      }
      
      Message message = new Message(DCProtocol.MessageType.MESSAGE_TYPE_COMMAND);
      message.setAttribute("command", jsonObject.getString("MESSAGE_TYPE"));
      message.setAttribute("arguments", arguments);				
      message.setAttribute("payload", sData);

      boolean validCommand = false;
      for (Command protocolCommand: DCProtocol.Command.values()) {
              if (protocolCommand.getKeyword().equals(jsonObject.getString("MESSAGE_TYPE"))) {
                      validCommand = true;
                      break;
              }
      }
      if (!validCommand) {
              throw new InvalidMessageException();
      }

      return message;
   }

   @Override
   public byte[] serialize(Message message) {

//      StringBuilder sb = new StringBuilder();
//
//      switch ((DCProtocol.MessageType)message.getType()) {
//              case MESSAGE_TYPE_RESULT:
//                      sb.append("[RESULT]: ");
//                      sb.append(message.getAttribute("statusCode"));
//                      sb.append(" ");
//                      sb.append(message.getAttribute("payload"));
//                      break;
//              case MESSAGE_TYPE_NOTIFICATION:
//                      sb.append("[NOTIFY]: ");
//                      sb.append(message.getAttribute("payload"));
//                      break;
//      }
//      byte[] data = sb.toString().getBytes(); 
//      return data;
   }
   
}
