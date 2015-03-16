# Card-Encryption-Server
This is the server-side of a project whose idea is to provide a server-client interface for crypting credit cards.

### ATTENTION! ###
Once you pull the project, you need to compile it before you open anything from the "view" package in your IDE. Not 
doing so will cause an error. The project needs to have the Java Panels compiled, before they are added to a Frame.
Thus, if you open MainWindow without first compiling the panels within it, you will get an error.

The accepted cards are: MasterCard, Visa, AmericanExpress, Discovery. 
All of them, consist of 16 digits, besides AmEx, which has only 15. They must also be correct card numbers, which is
checked with the Luhn formula.

1. Idea of the project.
The project is a realization of a client-server interface which provides credit card encryption services. The 
accepted cards are listed in the above paragraph. The server is also the admin of the "system", which is the 
client-server program. He can add new users, give them different privilege levels, and export the performed (by the
users) encryptions and decryptions in a sorted CSV file. (Take note that these are the features included in this ver.
For current information and version changelogs, please scroll down).

2. Realization
The server is based on a socket connection. It waits for a user to connect, then gives the job to a worker-thread. The
server continues listening for new connections, doing the same thing every time a user connects.
The user database is stored in an XML file. The serialization is done via XStream (http://xstream.codehaus.org). The
.jar file has been included as a dependency, and can be found in the nbproject/dependencies folder.

### CHANGELOG ###
ver. 1.0 - This is the first version of the project
- Allows the creation of users
- Allows the export of encrypted/decrypted card numbers in a CSV file
- Shows a changelog of all user interactions with the server
- Uses a single cipher algorithm for encryption
- There are three user privilege levels
