# Distributed Computing Protocol V1.0 Specification

\`\`\` Author : Jollien Dominique, Saam Frédéric, HEIG-VD Last revision
date : 27.03.2014

Revision history 22.03.2014 : did this 23.03.2014 : added that
24.03.2014 : modified this

\`\`\`

## 1. Introduction

The Distributed Computing Protocol (DCP) is an application-level
protocol that describes the operation of a distributed system used to
perform distributed computing.

The goal of such a distributed system is to allow clients to access
computing servers and ask them for computation results.

## 2. Terminology

**Smart Calculator**: a client in our DCP specification. This client
connects to Compute Engine to ask them to perform computation.

**Compute Engine**: a server in our DCP specification. This server
recieve computation requests from clients and reply them with the
result.

**Compute Function**: a computation of which a Compute Engine is capable
of. A Compute Function may require data input to be performed.

**Compute Result**: a result of a Compute Function as computed by a
Compute Engine.

## 3. Protocol Overview

### 3.1. System Architecture

Just an overview of the system architecture and the messages.

[centerimg width=520 src="img/componentDiagram.png"/center][]

### 3.2. System Components

#### 3.2.1. Smart Calculator

A client in our DCP specification. This client connects to Compute
Engine which announce themself on the network to ask them to perform
computation.

#### 3.2.2. Compute Engine

A service provider in our DCP specification. This component must be able
to listen for client connection request and send broadcast datagrams to
announce themself to the network.

This server recieve computation requests from clients and reply them
with the result.

If the Compute Engine is private it must have access to a user database
to check the credentials given by clients.

### 3.3. Interactions Between Components

\>Insert one or more **sequence diagrams** here, to show the high-level
interactions between the components (request-replies, discovery, etc.).
This is not the place to give all the details, but it should give the
reader a general understanding of what is happening at runtime. For each
sequence diagram, explain the reason for which the components are
interacting with each other (i.e. state the purpose of the interaction).

[centerimg width=520 src="img/sequenceDiagram.png"/center][]

## 4. Protocol Details

\>In this section, you should provide **all the information that is
required for the readers to do a detailed design and an implementation
of the software components** that will use your protocol to interact
with each other. The readers should find a clear description of the
communication patterns, the syntax and semantics of messages and of
other rules defined by the protocol.

### 4.1. Transport Protocols and Connections

\>In this paragraph, you should explain which protocols are used to
transport application-level messages. You should define the standard
port(s) (in the same way that HTTP defines 80 as the standard TCP port).
You should also explain how the components start to interact with each
other. If a connection is established, then how is it established and
what is the procedure that needs to be implemented?

### 4.2. State Management

\>In this paragraph, you should explain whether your protocol is
stageful or stateless. If it is stageful, then you should describe all
the states in which an application session can be, what events can
happen in each of these states and what are the possible transitions
between the states and what happens during these transitions.

\>Start by showing a state machine diagram, which will give an overview
to the readers. Then provide a detailed description of each state.

[centerimg width=320 src="images/04/stateMachineDiagram.png"/center][]

#### 4.2.1. State NAME\_OF\_THE\_STATE\_1

> In this paragraph, you should describe one particular state that
> appears on the state machine diagram. You should explain what it means
> for the conversation to be in this state. You should also explain what
> events are expected to happen while the conversation is in this state
> and what messages can be accepted from the other component. You should
> describe what should happen when these events happen (transition to
> another state, emission of a message, etc). Sometimes, you will
> specify timing constraints in this part of the document. For example,
> you could state that when the conversation enters a particular state,
> then a timer is started and emits a signal after a given time. This
> signal would be considered as a specific event, that might trigger a
> transition to another state and other side effects (closing of a
> connection, emission of a message, etc.).

### 4.3. Message Types, Syntax and Semantics

\>In this section, you should describe in details what messages are
exchanged by system components, how they are structured and encoded,
when they should be sent and what should happen when they are received.

> It is a good idea to write one paragraph for each type of message.
> Very often, you will also want to document some decisions that are
> valid for all messages. For example, you could state that all messages
> are encoded in JSON or in XML. Or, if you are using a grammar in your
> specification, you could state what kind of grammar it is and document
> the high-level production rules. Also, many protocol define a common
> structure for several message types, often involving the separation
> between an envelope (with headers) and a payload. This structure and
> the role of the headers may then be specified in an additional section
> that would be added here.

#### 4.3.1. Message MESSAGE\_TYPE\_1

\>In this paragraph, you should provide all the details for one
particular type of messages. You will therefore have one such paragraph
for every type defined in your protocol.

### 4.4. Miscellaneous Considerations

> Depending on the actual system and protocol, you will need to address
> additional issues and specify various elements. Typical topics that
> are covered in protocol specifications include reliability, caching,
> scalability, content negotiation, encoding formats and others. You
> will have to adapt this part of the template to describe what is
> relevant to your own situation. If you have many questions to answer,
> then you will most likely split this section into multiple sections.

### 4.5. Security Considerations

\>In many protocol specifications, security aspects of the protocol are
treated in a dedicated section. Sometimes, the authentication,
authorization, confidentiality rules are specified in the main protocol
specification. Very often, however, the main protocol specification
defines a generic mechanism (e.g. it states that requests should contain
an authentication header, without fully specifying what should be
transmitted in the header). Other specification documents are then
written to use this extension point and to specify one or more different
ways to actually handle the authentication in a system implementation.
The HTTP specification follows this approach, with authentication
mechanisms specified in [RFC
2617][]<https://tools.ietf.org/html/rfc2617>.

## 5. Examples

\>In this section, you should **give examples of interactions between
the components** of the distributed system. In previous sections, you
have specified detailed rules (messaging patterns, session states,
message syntax and semantics, etc.). Developers will use these detailed
specifications when they implement your protocol.

\>But by providing examples in the document, **you will help them in two
ways**. Firstly, it will be **easier for them to understand what your
protocol does and how**. Analyzing at a concrete dialog between a client
and a server is often easier than directly digging into a more abstract
definition. Secondly, it will allow them to **confirm their
understanding of the protocol specification**. After reading the
different rules specified in the previous sections, they will have a way
to check that they have understood them correctly.

## 6. References

\>Very often, you will provide references to other protocol
specifications and/or to other documents. This is something that you
should do in a specific section of your document.

  [centerimg width=520 src="img/componentDiagram.png"/center]: centerimg width=520 src="img/componentDiagram.png"/center
  [centerimg width=520 src="img/sequenceDiagram.png"/center]: centerimg width=520 src="img/sequenceDiagram.png"/center
  [centerimg width=320 src="images/04/stateMachineDiagram.png"/center]: centerimg width=320 src="images/04/stateMachineDiagram.png"/center
  [RFC 2617]: #
