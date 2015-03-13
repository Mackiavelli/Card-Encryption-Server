/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import view.AddUser;
import view.MainWindow;
import view.SavePanel;

/**
 *
 * @author PC
 */
public class Server implements Runnable {

    private final ExecutorService threads = Executors.newCachedThreadPool();
    private final int port = 15465;
    private ServerSocket server;
    private Socket connection;
    
    //protected
    protected final ServerDatabase database;
    protected AbstractCollection<CryptedCards> crypted;
    protected ObjectOutputStream os;
    protected ObjectInputStream is;

    //visual elements that will be updated
    private static final MainWindow view = new MainWindow();
    private AddUser addUserPanel;
    private SavePanel savePanel;
    private JRadioButton adminRadioButton;
    private ButtonGroup buttonGroup1;
    private JRadioButton clientRadioButton;
    private JPasswordField passwordField;
    private JButton submitButton;
    private JTextField usernameField;
    private JRadioButton visitorRadioButton;
    private JButton saveByCardButton;
    private JButton saveByEncryptedButton;
    
    //protected so it can be accessed in ServerWorker
    protected JTextArea eventLog;

    private void openServerSocket() {
        try {
            this.server = new ServerSocket(port);
        } catch (IOException ioe) {
            eventLog.append("ERROR OPENING NEW SOCKET!\n");
        }
    }

    private void addNewUser() {
        UserPrivileges currentPriv = UserPrivileges.CLIENT;

        if (usernameField.getText().length() == 0
                || passwordField.getPassword().length == 0) {
            usernameField.setText("");
            passwordField.setText("");
            return;
        }

        if (visitorRadioButton.isSelected()) {
            currentPriv = UserPrivileges.CANNOT_ENCRYPT;
        } else if (adminRadioButton.isSelected()) {
            currentPriv = UserPrivileges.ADMINISTRATOR;
        }

        CardHolder temp
                = new CardHolder(usernameField.getText(),
                        new String(passwordField.getPassword()),
                        currentPriv);

        if (database.addUser(temp)) {
            eventLog.append("The user \"" + temp.getUsername()
                    + "\" has been successfully added\n");
            usernameField.requestFocus();
            database.serialize();
        } else {
            JOptionPane.showMessageDialog(null, "User not added!", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        usernameField.setText("");
        passwordField.setText("");
    }

    private void initComponents() {
        
        addUserPanel = view.getAddUserPanel();
        eventLog = view.getEventLog();
        savePanel = view.getSavePanel();

        submitButton = addUserPanel.getSubmitButton();
        submitButton.addActionListener((ActionEvent evt) -> addNewUser());

        usernameField = addUserPanel.getUsernameField();
        usernameField.addActionListener((ActionEvent) -> addNewUser());
        passwordField = addUserPanel.getPasswordField();
        passwordField.addActionListener((ActionEvent evt) -> addNewUser());
        
        buttonGroup1 = addUserPanel.getButtonGroup();
        visitorRadioButton = addUserPanel.getVisitorRadioButton();
        adminRadioButton = addUserPanel.getAdminRadioButton();
        clientRadioButton = addUserPanel.getClientRadioButton();

        saveByCardButton = savePanel.getSaveByCard();
        saveByCardButton.addActionListener(
                (ActionEvent evt) -> saveCardsToCSV(false));
        saveByEncryptedButton = savePanel.getSaveByEncrypted();
        saveByEncryptedButton.addActionListener(
                (ActionEvent evt) -> saveCardsToCSV(true));

        view.setVisible(true);
    }

    //selection sort
    private void sortArray(CryptedCards[] cardArray, boolean sortByCrypted) {
        for (int a = 0; a < cardArray.length - 1; a++) {
            int maxPos = a;
            for (int b = a + 1; b < cardArray.length; b++) {
                if (sortByCrypted) {
                    if (cardArray[maxPos].getEncrypted().
                            compareTo(cardArray[b].getEncrypted()) < 0) {
                        maxPos = b;
                    }
                } else {
                    if (cardArray[maxPos].getRealCardCode().
                            compareTo(cardArray[b].getRealCardCode()) < 0) {
                        maxPos = b;
                    }
                }
            }
            CryptedCards temp = cardArray[a];
            cardArray[a] = cardArray[maxPos];
            cardArray[maxPos] = temp;
            temp = null;
        }
    }

    public Server() {
        database = new ServerDatabase();
        crypted = new ArrayBlockingQueue<>(1000);
        initComponents();
    }

    @Override
    public void run() {
        openServerSocket();
        while (true) {
            Socket clientSocket = null;
            try {
                clientSocket = this.server.accept();
                eventLog.append("A user has connected...\n");
            } catch (IOException ioe) {
                eventLog.append("Server has stopped...\n");
                return;
            }

            threads.execute(new ServerWorker(clientSocket));
        }
    }

    public void saveCardsToCSV(boolean sortedByCrypted) {
        File file = new File("cards.csv");
        try {
            if (crypted.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No encrypted cards to save");
            } else {
                Iterator<CryptedCards> iterator = crypted.iterator();
                CryptedCards[] temp = new CryptedCards[crypted.size()];
                for (int a = 0; a < crypted.size(); a++) {
                    temp[a] = iterator.next();
                }
                
                sortArray(temp, sortedByCrypted);
                BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                bw.write("encrypted card, card number\n");
                
                for (int a = 0; a < temp.length; a++) {
                    bw.write(temp[a].getEncrypted() + ","
                            + temp[a].getRealCardCode() + "\n");
                }
                bw.flush();
                bw.close();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
