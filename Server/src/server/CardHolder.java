/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.Serializable;
import javax.swing.JOptionPane;

/**
 *
 * @author PC
 */

//it implements Serializable for future reference - the program could easily be
//modified to store the CardHolder in a random access file as a byte stream
public class CardHolder implements Serializable{
    private String username;
    private String password;
    UserPrivileges userAccess;
    
    public void setUsername(String newUsername){
        if(newUsername != null){
            username = newUsername;
        } else {
            return;
        }
    }
    
    public String getUsername(){
        return username;
    }
    
    public void setPassword(String newPassword){
        if(newPassword != null){
            password = newPassword;
        } else {
            JOptionPane.showMessageDialog(null,
                    "You have entered an invalid password");
        }
    }
    
    public String getPassword(){
        return password;
    }
    
    public void setPrivileges(UserPrivileges newPrivileges){
        if(newPrivileges != null){
            userAccess = newPrivileges;
        } else {
            JOptionPane.showMessageDialog(null,
                    "You have entered an user privilege level");
        }
    }
    
    public UserPrivileges getPrivilege(){
        return this.userAccess;
    }
    
    public CardHolder(String username, String password, UserPrivileges access){
        setUsername(username);
        setPassword(password);
        setPrivileges(access);
    } 
    
    public String toString(){
        return String.format("%s%n%s%n%s%n%s%n", "User Info:", 
                                                username, password, userAccess);
    }
    
    
}
