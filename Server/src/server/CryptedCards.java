/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.Serializable;

/**
 *
 * @author PC
 */
//serializable for future reference and update purposes
public class CryptedCards implements Serializable{
    private final String encrypted;
    private final String realCardCode;
    
    public String getEncrypted(){
        return encrypted;
    }
    
    public String getRealCardCode(){
        return realCardCode;
    }
    
    public CryptedCards(String enc, String realCardCode){
        encrypted = enc;
        this.realCardCode = realCardCode;
    }
}
