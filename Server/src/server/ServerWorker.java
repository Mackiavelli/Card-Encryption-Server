/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import encryption.Crypt;
import encryption.Encryptable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author PC
 */
//extend the server to get access to the fields without declaring them again
public class ServerWorker extends Server implements Runnable {

    private final Socket clientSocket;
    private final Encryptable encryption;
    private String currentUsername;
    private UserPrivileges currentPrivilege;

    private boolean verifyCredentials(String username, String password) {
        //only one object of the class can access the method at any given time
        synchronized (ServerWorker.class) {
            if (database.verifyUser(username, password)) {
                currentUsername = username;
                try {
                    currentPrivilege = database.getUserPrivilege(currentUsername);
                    os.writeObject(true);
                    os.flush();
                    eventLog.append("User " + currentUsername + " has logged in\n");
                } catch (IOException ioe) {
                    eventLog.append("Problem returning result to client "
                            + currentUsername + "\n");
                }
                return true;
            } else {
                try {
                    eventLog.append("Could not verify credentials!!!\n");
                    os.writeObject(false);
                    os.flush();
                    clientSocket.close();
                } catch (IOException ioe) {
                    eventLog.append("Problem with closing the socket for user"
                            + currentUsername + "!!!\n");
                }
            }
            return false;
        }
    }

    private void getStreams() throws IOException {
        try {
            os = new ObjectOutputStream(clientSocket.getOutputStream());
            os.flush();
            is = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException ioe) {
            eventLog.append("Problem with getting access of streams!!!\n");
        }
    }

    private void processConnection() throws IOException {
        String message;
        do {
            try {
                message = (String) is.readObject();
                if (message.equals("___ACC___")) {
                    String username = (String) is.readObject();
                    String password = (String) is.readObject();
                    verifyCredentials(username, password);
                } else {
                    readCommand(message);
                }
                System.out.println(message);
            } catch (ClassNotFoundException cnfe) {
                eventLog.append("Unknown object received from "
                        + currentUsername);
            }
        } while (true);
    }

    private void processEncryption() throws IOException, ClassNotFoundException {
        if (currentPrivilege == UserPrivileges.CANNOT_ENCRYPT) {
            eventLog.append("User: " + currentUsername
                    + " doesn't have the necessary privileges to encrypt...'\n");
        } else {
            String cardNumber = (String) is.readObject();
            String result = encryption.encrypt(cardNumber);
            if (result == null) {
                return;
            }
            crypted.add(new CryptedCards(result, cardNumber));
            os.writeObject(result);
            os.flush();
            eventLog.append("User: " + currentUsername
                    + " has requested successful ENCRYPTION\n");
        }
    }

    private void processDecryption() throws IOException, ClassNotFoundException {
        if (currentPrivilege == UserPrivileges.CANNOT_ENCRYPT) {
            eventLog.append("User: " + currentUsername
                    + " doesn't have the necessary privileges to decrypt...'\n");
        } else {
            String cryptedNumber = (String) is.readObject();
            String result = encryption.decrypt(cryptedNumber);
            if (result == null) {
                return;
            }
            os.writeObject(result);
            os.flush();
            eventLog.append("User: " + currentUsername
                    + " has requested successful DECRYPTION\n");
        }
    }

    private void readCommand(String command) {
        try {
            if (command.equals("___ACC___")) {
                //processLogin();
            } else if (command.equals("___CRY___")) {
                processEncryption();
            } else if (command.equals("___DEC___")) {
                processDecryption();
            }
        } catch (IOException ioe) {
            System.out.println("problem");
        } catch (ClassNotFoundException cnfe) {
            System.out.println("problem");
        }
    }

    public ServerWorker(Socket newClientSocket) {
        clientSocket = newClientSocket;
        encryption = new Crypt();
    }

    public void run() {
        try {
            getStreams();
            processConnection();
            System.out.println("does this count?");
        } catch (IOException ioe) {
            System.out.println("Problem with getting streams");
        }
    }
}
