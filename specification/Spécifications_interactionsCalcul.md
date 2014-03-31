# Distributed Computing Protocol V1.0 Specification : interactions after dynamic discovery

    Author              : Dominique Jollien and Frédéric Saam, HEIG-VD
    Last revision date  : 30.03.2014

    Revision history
             30.03.2014 : Finalized the v1.0 specification

## 1. Introduction

The Distributed Computing Protocol (DCP) is an application-level
protocol that describes the operation of a distributed system used to
perform distributed computing.

The goal of such a distributed system is to allow clients to access
computing servers and ask them for computation results.

This document will describe the interactions between the servers able to
provide computation and the clients once they have established a
connection.

## 2. Terminology

**Smart Calculator**: a client in our DCP specification. This client
connects to Compute Engine to ask them to perform computation.

**Compute Engine**: a server in our DCP specification. This server
recieve computation requests from clients and reply them with the
result. A compute Engine can be public or private, if he is private the
Smart Calculator connecting to him must possess a recognized account by
him.

**Compute Function**: a computation of which a Compute Engine is capable
of. A Compute Function may require data input to be performed.

**Compute Result**: a result of a Compute Function as computed by a
Compute Engine.

## 3. Protocol Overview

### 3.1. System Architecture

Just an overview of the system architecture and the messages between the
components.

### 3.2. System Components

![][]

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

The overall interactions between components are shown on the diagram in
chapter 3.1.

## 4. Protocol Details

### 4.1. Transport Protocols and Connections

The dynamic service discovery used by this protocol is described in the
"Dynamic Discovery" specification document.

Interactions between the Smart Calculator and the Compute Engine after
the dynamic service discovery are done via TCP. The default listening
port for Compute Engines is 6060.

To establish a connection with a Compute Engine, after having
dynamically discovered it, the Smart Calculator send him a
CONNECTION\_REQUEST message.

If the Compute Engine is private he follows the connection process with
a LOGIN\_REQUIRED message.

The Smart Calculator can then reply with a USER\_CREDENTIALS message and
be logged in if the given credentials are recognize by the compute
engine or he can ask for registration in the Compute Engine's user
database with a REGISTRATION\_REQUEST message which will be replied by a
REGISTRATION\_REPLY.

He must then reply with a NEW\_USER\_CREDENTIALS to register himself
which will be replied either by a REGISTRATION\_FAILURE or
REGISTRATION\_SUCCESS.

Once his account is created he can go back to logging in.

### 4.2. State Management

This protocol is stageful.

![][1]

#### 4.2.1. Initial state (listening for connection request)

In this state the Compute Engine is listening for CONNECTION\_REQUEST
from the Smart Calculator.

When he receives one, he reply with LOGIN\_REQUIRED and goes in the
state Authentication if he is private but he replies with
FUNCTIONS\_LIST and goes it the state Waiting\_computing\_request if he
is public.

#### 4.2.2. State Authentication

Used if the Compute Engine is private and the user has to log in.

In this state the Compute Engine can receive from the Smart Calculator :

-   USER\_CREDENTIALS in which case he check the credentials and replies
    with LOGIN\_FAILURE or send FUNCTIONS\_LIST and goes in the state
    Waiting\_computing\_request.

-   REGISTRATION\_REQUEST in which case he reply with
    REGISTRATION\_REPLY and goes in the state REGISTRATION.

#### 4.2.3. State Registration

Used if the Compute Engine is private and the user wants to create a new
account.

In this state the Compute Engine can receive from the Smart Calculator :

-   NEW\_USER\_CREDENTIALS in which case he tries to create a new
    account with the credentials and replies with REGISTRATION\_FAILURE
    or REGISTRATION\_SUCCESS.

-   CONNECTION\_REQUEST in which case he reply with LOGIN\_REQUIRED and
    goes in the state Authentication.

#### 4.2.4. State Waiting\_computing\_request

Used if a Smart Calculator is connected of the Compute Engine and no
task is currently performing.

In this state the Compute Engine can receive from the Smart Calculator :

-   COMPUTING\_REQUEST in which case he replies with UNKOWN\_FUNCTION if
    the name of the computing function asked is wrong otherwise he goes
    in the state compute\_request.

-   BYE in which case he closes the connection.

#### 4.2.5. State compute\_request

Used if a Compute Engine is processing inputs for a requested compute
function.

In this state the Compute Engine can send to the Smart Calculator :

-   INPUTS\_REQUEST in which case he goes in the state Waiting\_inputs.

-   LIST\_INPUTS\_REQUEST in which case he goes in the state
    Waiting\_list\_inputs.

-   COMPUTING\_RESULT in which case he goes in the state
    Waiting\_computing\_request.

-   COMPUTING\_FAILURE in which case he goes in the state
    Waiting\_computing\_request.

#### 4.2.6. State Waiting\_inputs

Used if a Compute Enging is asking for a certain number of inputs for a
requested compute function.

In this state the Compute Engine can receive from the Smart Calculator :

-   INPUTS\_REPLY in which case he goes in the state compute\_request.

#### 4.2.7. State Waiting\_list\_inputs

Used if a Compute Enging is asking for an unknown number of inputs for a
requested compute function.

In this state the Compute Engine can receive from the Smart Calculator :

-   LIST\_INPUTS\_REPLY in which case he store an additional input.

-   LIST\_INPUTS\_ENDOFREPLY in which case he goes in the state
    compute\_result.

### 4.3. Message Types, Syntax and Semantics

All the messages are encoded in JSON.

#### 4.3.1 Connection

Connection between a Smart Calculator and a Compute Engine.

##### 4.3.1.1 Message CONNECTION\_REQUEST

Sent by a Smart Calculator to a Compute Engine on it's listening port.
Asks for a connection with the Compute Engine. If he is public the
message is simply replied by FUNCTIONS\_LIST otherwise it's replied by
LOGIN\_REQUIRED.

Payload : {"MESSAGE\_TYPE" : "CONNECTION\_REQUEST"}

##### 4.3.1.2 Message LOGIN\_REQUIRED

Sent by a private Compute Engine to a Smart Calculator that sent
CONNECTION\_REQUEST. Asks the Smart Calculator for a user id and
password.

Payload : {"MESSAGE\_TYPE" : "LOGIN\_REQUIRED"}

##### 4.3.1.3 Message USER\_CREDENTIALS

Sent by a Smart Calculator to a private Compute Engine that sent
LOGIN\_REQUIRED. Provide user credentials that must be known by the
Compute Engine to successfully log in the user. The user password is
hashed in sha-256.

Payload : {"MESSAGE\_TYPE" : "USER\_CREDENTIALS", "USER\_ID" : "the user
id provided", "USER\_PASSWORD" : "the user password provided"}

#### 4.3.1.4 Message LOGIN\_FAILURE

Sent by a private Compute Engine to a Smart Calculator that sent
USER\_CREDENTIALS.

Inform the Smart Calculator he is not logged in. The reasons are given
in the EXPLANATIONS field.

Payload : {"MESSAGE\_TYPE" : "LOGIN\_FAILURE", "EXPLANATIONS" :
"explanations of the reasons of the failure"}

##### 4.3.1.5 Message REGISTRATION\_REQUEST

Sent by a Smart Calculatorto a private Compute Engine that sent
LOGIN\_REQUIRED. Ask the Compute Engine for registration of a new user
in his user database.

Payload : {"MESSAGE\_TYPE" : "REGISTRATION\_REQUEST"}

##### 4.3.1.6 Message REGISTRATION\_REPLY

Sent by a private Compute Engine to a Smart Calculator that sent
REGISTRATION\_REQUEST. Asks the Smart Calculator for a user id, a
password and an email.

Payload : {"MESSAGE\_TYPE" : "REGISTRATION\_REPLY"}

##### 4.3.1.7 Message NEW\_USER\_CREDENTIALS

Sent by a Smart Calculator to a private Compute Engine that sent
REGISTRATION\_REPLY. Provide new user credentials to let the Compute
Engine create a new user account. The user password is hashed in
sha-256.

Payload : {"MESSAGE\_TYPE" : "NEW\_USER\_CREDENTIALS", "USER\_ID" : "the
user id provided", "USER\_PASSWORD" : "the user password provided",
"USER\_EMAIL" : "the user email provided"}

##### 4.3.1.8 Message REGISTRATION\_SUCCESS

Sent by a private Compute Engine to a Smart Calculator that sent
NEW\_USER\_CREDENTIALS. Inform the Smart Calculator that his new account
has been created and he can use it to log himself in this Compute
Engine.

Payload : {"MESSAGE\_TYPE" : "REGISTRATION\_SUCCESS"}

##### 4.3.1.9 Message REGISTRATION\_FAILURE

Sent by a private Compute Engine to a Smart Calculator that sent
NEW\_USER\_CREDENTIALS. Inform the Smart Calculator that his new account
has not been created. The reasons are given in the EXPLANATIONS field.

Payload : {"MESSAGE\_TYPE" : "REGISTRATION\_FAILURE", "EXPLANATIONS" :
"explanations of the reasons of the failure"}

#### 4.3.1.10 Message BYE

Sent by a Smart Calculator to a private Compute Engine after a
successfull authentication. Log out of the Smart Calculator on the
Compute Engine. Can also be send by the Compute Engine to the Smart
Calculator to terminate the session.

Payload : {"MESSAGE\_TYPE" : "BYE"}

#### 4.3.2 Computing

Computing requests and replies between a Smart Calculator and a Compute
Engine after a successful connection.

#### 4.3.2.1 FUNCTIONS\_LIST

Sent by a Compute Engine to a Smart Calculator that successfully
connected (by simply sending a CONNECTION\_REQUEST if the Compute Engine
is public or by going through the logging process). Inform the Smart
Calculator of the computing functions the Compute Engine is capable.

Payload : {"MESSAGE\_TYPE" : "FUNCTIONS\_LIST",

"FUNCTIONS" : {

"the name of a computing function" : "description of the computing
function",

"the name of another computing function" : "description of the computing
function",

...

}}

#### 4.3.2.2 COMPUTING\_REQUEST

Sent by a Smart Calculato to a Compute Engine that sent FUNCTIONS\_LIST.
Ask the Compute Engine to perform a computing function he is capable of.

Payload : {"MESSAGE\_TYPE" : "COMPUTING\_REQUEST","FUNCTIONS" : "the
name of a computing function"}

#### 4.3.2.3 UNKOWN\_FUNCTION

Sent by a Compute Engine to a Smart Calculator that sent
COMPUTING\_REQUEST with an unrecognised computing function name.

Payload : {"MESSAGE\_TYPE" : "COMPUTING\_REQUEST","FUNCTIONS" : "the
name of a computing function"}

#### 4.3.2.4 INPUTS\_REQUEST

Sent by a Compute Engine to a Smart Calculator that sent
COMPUTING\_REQUEST if the computing function that is requested requires
a certain number of input.

Payload : {"MESSAGE\_TYPE" : "INPUTS\_REQUEST",

"INPUTS" : {"the name of a required input", "the name of another
required input", ...}

}

#### 4.3.2.5 INPUTS\_REPLY

Sent by a Smart Calculator to a Compute Engine to that sent
INPUTS\_REQUEST. Provide a value for each required input.

Payload : {"MESSAGE\_TYPE" : "INPUTS\_REPLY",

"INPUTS" : {

"the name of a required input" : "the value of the input",

"the name of another required input" : "the value of the input",

...

}}

#### 4.3.2.6 LIST\_INPUTS\_REQUEST

Sent by a Compute Engine to a Smart Calculator that sent
COMPUTING\_REQUEST if the computing function that is requested requires
an unkown number of input.

Payload : {"MESSAGE\_TYPE" : "LIST\_INPUTS\_REQUEST"}

#### 4.3.2.7 LIST\_INPUTS\_REPLY

Sent by a Smart Calculator to a Compute Engine to that sent
LIST\_INPUTS\_REQUEST. Provide a value an input.

Payload : {"MESSAGE\_TYPE" : "LIST\_INPUTS\_REPLY", "INPUT" : "the value
of the input"}

#### 4.3.2.8 LIST\_INPUTS\_ENDOFREPLY

Sent by a Smart Calculator to a Compute Engine to that sent
LIST\_INPUTS\_REQUEST. Inform the Compute Engine that all the inputs
have been sent.

Payload : {"MESSAGE\_TYPE" : "LIST\_INPUTS\_ENDOFREPLY"}

#### 4.3.2.9 COMPUTING\_RESULT

Sent by Compute Engine to a Smart Calculator that sent
COMPUTING\_REQUEST and all the required inputs. Provide the result of
the computing functions applied to the inputs provided by the Smart
Calculator.

Payload : {"MESSAGE\_TYPE" : "COMPUTING\_RESULT", "COMPUTING\_RESULT" :
"the result of the computing function"}

#### 4.3.2.10 COMPUTING\_FAILURE

Sent by Compute Engine to a Smart Calculator that sent
COMPUTING\_REQUEST and all the required inputs.

Inform the Smart Calculator that the computing of the inputs with the
requested computing function failed. The reasons are given in the
EXPLANATIONS field.

Payload : {"MESSAGE\_TYPE" : "COMPUTING\_RESULT", "EXPLANATIONS" :
"explanations of the reasons of the failure"}

### 4.4. Security Considerations

#### 4.4.1. Cryptographic hash function

We have chosen to use the cryptographic hash function sha-256 to stock
and verify the passwords.

#### 4.4.2. Connexion to private compute engine

There is no real "session" in this protocol, when the client tries to
connect to a private engine, we hash the password and then we compare to
the user\_id/hash in our database. If the comparison is correct, the server
send the LOGIN\_SUCCESS message and then the functions. If the comparison
is wrong, the server send a LOGIN\_FAIL message and ask again for the user's
credentials.

## 5. Examples

![][2]

## 6. References

-   "Distributed Computing Protocol V1.0 Specification : Dynamic
    dicovery" by Simone Righitto and Anthony Roubaty

  []: img/componentDiagram.png
  [1]: img/stateMachineDiagram.png
  [2]: img/sequeceDiagram.png
