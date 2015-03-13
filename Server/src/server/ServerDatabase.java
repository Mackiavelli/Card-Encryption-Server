/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import javax.swing.JOptionPane;

/**
 *
 * @author PC
 */
public class ServerDatabase implements Serializable {

    private final int MAX_USERS = 1000;
    private final File database = new File("database.xml");
    private AbstractCollection<CardHolder> users;
    private final String xml = "<?xml version=\"1.0\" ?>";
    private final String usr = "<users>";
    private final String usrClose = "</users>";

    public ServerDatabase() {
        users = new ArrayBlockingQueue<>(MAX_USERS);
        deserialize();
    }
    
    public UserPrivileges getUserPrivilege(String username){
        Iterator<CardHolder> iterator = users.iterator();
        while(iterator.hasNext()){
            CardHolder temp = iterator.next();
            if(temp.getUsername().equals(username)){
                return temp.getPrivilege();
            }
        }
        return null;
    }
    
    private void addFromDatabase(List<String> users) {
        if (!users.isEmpty()) {
            Iterator<String> iterator = users.iterator();
            XStream xstream = new XStream(new StaxDriver());
            xstream.alias("user", CardHolder.class);

            while (iterator.hasNext()) {
                String current = iterator.next();
                if (current.contains(usr)) {
                    current = String.format("%s%s",
                            current.substring(0, xml.length()),
                            current.substring(xml.length() + usr.length()));

                }
                if (!current.contains(usrClose)) {
                    CardHolder temp = (CardHolder) xstream.fromXML(current);
                    addUser(temp);

                }
            }
        } else {
            return;
        }
    }

    //this method reads from the XML file and puts it into the collection
    private void deserialize() {
        Reader reading = null;

        try {
            reading = new BufferedReader(new FileReader(database));
            List<String> lines
                    = Files.readAllLines(Paths.get(database.getPath()));
            addFromDatabase(lines);
        } catch (IOException ioe) {
            System.out.println("A problem with deserializing (input from db)");
        } finally {
            try {
                reading.close();
            } catch (Exception e) {
                System.out.println("There was an unexpected problem");
            }
        }
    }

    //this method outputs into an xml file
    public void serialize() {
        BufferedWriter writer = null;
        try {
            XStream xstream = new XStream(new StaxDriver());
            xstream.alias("user", CardHolder.class);
            writer = new BufferedWriter(new FileWriter(database));
            Iterator<CardHolder> iterator = users.iterator();
            writer.write(xml + usr);
            while (iterator.hasNext()) {
                String output = xstream.toXML(iterator.next());
                output = output.substring(xml.length());
                writer.write(output + "\n");
            }
            writer.write(usrClose);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (Exception closing) {
                System.out.println("Couldn't close");
            }
        }
    }
    
    private boolean userExists(CardHolder temp){
        Iterator<CardHolder> iterator = users.iterator();
        while(iterator.hasNext()){
            CardHolder loopTemp = iterator.next();
            if(loopTemp.getUsername().equals(temp.getUsername())){
                return true;
            }
        }
        return false;
    }

    public boolean addUser(CardHolder newUser) {
        if (users.size() < MAX_USERS && !userExists(newUser)) {
            users.add(newUser);
            return true;
        } else {
            JOptionPane.showMessageDialog(null,
                    "User already exists or the database is full!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public boolean verifyUser(String username, String password){
        Iterator<CardHolder> iterator = users.iterator();
        System.out.println(users.size());
        while(iterator.hasNext()){
            CardHolder temp = iterator.next();
            if(temp.getUsername().equalsIgnoreCase(username)){
                if(temp.getPassword().equalsIgnoreCase(password)){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void finalize() {
        serialize();
    }
}
