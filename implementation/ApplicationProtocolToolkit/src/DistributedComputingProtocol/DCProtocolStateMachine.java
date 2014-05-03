//package DistributedComputingProtocol;
//
//import ch.heigvd.res.toolkit.interfaces.IState;
//import ch.heigvd.res.toolkit.interfaces.IStateMachine;
//import ch.heigvd.res.toolkit.impl.Event;
//import ch.heigvd.res.toolkit.impl.Message;
//import ch.heigvd.res.toolkit.impl.AbstractStateMachine;
//
///**
// * This class implements the state machine for the Ping Pong protocol.
// *
// * @author Olivier Liechti
// */
//public class DCProtocolStateMachine extends AbstractStateMachine implements IStateMachine {
//
//	public DCProtocolStateMachine(IContext context) {
//		super(context);
//	}
//
//	@Override
//	public void destroy() {
//		
//	}
//
//	@Override
//	public IState getInitialState() {
//		return DCProtocol.State.STATE_START;
//	}
//
//	@Override
//	public void onStateEntered(IState state) {
//		super.onStateEntered(state);
//
//		/*
//		 When we enter STATE_START, we send a notification to the client to say hello. We
//		 then immediately transition to STATE_PING, as per protocol specification.
//		 */
//		if (state == DCProtocol.State.STATE_START) {
//			Message msg = new Message(DCProtocol.MessageType.MESSAGE_TYPE_NOTIFICATION);
//			msg.setAttribute("payload", DCProtocol.NOTIFICATION_WELCOME_TEXT);
//			getContext().sendMessage(msg);
//                        if(computeEngineIsPrivate) {
//                           triggerTransitionToState(DCProtocol.State.);
//                           inactivityGuard.notifyClientActivity();
//                        }
//		}
//
//		/*
//		 When we enter STATE_END, we send a notification to inform the client that we are
//		 closing the connection. We then close the connection.
//		 */
//		if (state == DCProtocol.State.STATE_END) {
//			Message msg = new Message(DCProtocol.MessageType.MESSAGE_TYPE_NOTIFICATION);
//			msg.setAttribute("payload", DCProtocol.NOTIFICATION_CLOSING_TEXT);
//			getContext().sendMessage(msg);
//			getContext().closeSession();
//		}
//	}
//
//	@Override
//	public void onEvent(Event e) {
//
//		// If we have received an invalid message from the client we return an error message
//		if (e.getType() == Event.EventType.EVENT_TYPE_INVALID_MESSAGE_ARRIVED) {
//			Message reply = new Message(DCProtocol.MessageType.MESSAGE_TYPE_RESULT);
//			reply.setAttribute("statusCode", 400);
//			reply.setAttribute("payload", DCProtocol.RESULT_INVALID_CMD_TEXT);
//			getContext().sendMessage(reply);
//			return;
//		}
//
//		// Whatever the state, if we receive a BYE command, we know what to do
//		if (e.getType() == Event.EventType.EVENT_TYPE_MESSAGE_ARRIVED) {
//			Message incomingMessage = (Message) (e.getAttribute("message"));
//			String command = (String) incomingMessage.getAttribute("command");
//			if (DCProtocol.Command.CMD_BYE.getKeyword().equals(command)) {
//				Message lastMessage = new Message(DCProtocol.MessageType.MESSAGE_TYPE_RESULT);
//				lastMessage.setAttribute("payload", DCProtocol.RESULT_BYE_TEXT);
//				getContext().sendMessage(lastMessage);
//				triggerTransitionToState(DCProtocol.State.STATE_END);
//				return;
//			}
//		}
//
//		// Whatever the state, if we receive a SCORE command, we know what to do
//		if (e.getType() == Event.EventType.EVENT_TYPE_MESSAGE_ARRIVED) {
//			Message incomingMessage = (Message) (e.getAttribute("message"));
//			String command = (String) incomingMessage.getAttribute("command");
//			if (PingPongProtocol.Command.CMD_SCORE.getKeyword().equals(command)) {
//				Message scoreMessage = new Message(PingPongProtocol.MessageType.MESSAGE_TYPE_RESULT);
//				StringBuilder scoreMessagePayload = new StringBuilder();
//				scoreMessagePayload.append("Missed: ")
//								.append(missedCount)
//								.append(" Successful: ")
//								.append(successCount)
//								.append(" Late: ")
//								.append(lateCount);
//
//				scoreMessage.setAttribute("statusCode", "200");
//				scoreMessage.setAttribute("payload", scoreMessagePayload.toString());
//				getContext().sendMessage(scoreMessage);
//				return;
//			}
//		}
//
//		// Whatever the state, if we receive a HELP command, we know what to do
//		if (e.getType() == Event.EventType.EVENT_TYPE_MESSAGE_ARRIVED) {
//			Message incomingMessage = (Message) (e.getAttribute("message"));
//			String command = (String) incomingMessage.getAttribute("command");
//			if (PingPongProtocol.Command.CMD_HELP.getKeyword().equals(command)) {
//				Message message = new Message(PingPongProtocol.MessageType.MESSAGE_TYPE_RESULT);
//
//				StringBuilder helpMessagePayload = new StringBuilder("You can use the following commands: ");
//				for (PingPongProtocol.Command cmd : PingPongProtocol.Command.values()) {
//					helpMessagePayload.append("['")
//									.append(cmd.getKeyword())
//									.append("': ")
//									.append(cmd.getHelp())
//									.append("] ");
//				}
//				message.setAttribute("statusCode", "200");
//				message.setAttribute("payload", helpMessagePayload.toString());
//				getContext().sendMessage(message);
//				return;
//			}
//		}
//
//		switch ((PingPongProtocol.State) getCurrentState()) {
//			case STATE_PING:
//				onEventInStatePingOrPong(e);
//				break;
//			case STATE_PONG:
//				onEventInStatePingOrPong(e);
//				break;
//		}
//
//	}
//
//	/**
//	 * This method handles events when the state machine is either in STATE_PING or STATE_PONG
//	 * @param e the event
//	 */
//	private void onEventInStatePingOrPong(Event e) {
//		switch ((Event.EventType) e.getType()) {
//			case EVENT_TYPE_MESSAGE_ARRIVED:
//				Message reply = new Message(PingPongProtocol.MessageType.MESSAGE_TYPE_RESULT);
//				Message request = (Message) e.getAttribute("message");
//				String command = (String) request.getAttribute("command");
//
//				boolean successful;
//				IState targetState;
//				
//				if (getCurrentState() == PingPongProtocol.State.STATE_PING) {
//					successful = (command.equals(PingPongProtocol.Command.CMD_PING.getKeyword()));
//					targetState = PingPongProtocol.State.STATE_PONG;
//				} else {
//					successful = (command.equals(PingPongProtocol.Command.CMD_PONG.getKeyword()));
//					targetState = PingPongProtocol.State.STATE_PING;
//				}
//
//				if (successful) {
//					successCount++;
//					reply.setAttribute("statusCode", "200");
//					reply.setAttribute("payload", PingPongProtocol.RESULT_SUCCESS_TEXT);
//					getContext().sendMessage(reply);
//					triggerTransitionToState(targetState);
//				} else {
//					missedCount++;
//					reply.setAttribute("statusCode", "200");
//					reply.setAttribute("payload", PingPongProtocol.RESULT_FAIL_TEXT);
//					getContext().sendMessage(reply);
//				}
//				break;
//		}
//	}
//
//}
