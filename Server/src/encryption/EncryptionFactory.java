/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encryption;

import encryption.Encryptable;
import java.util.Random;

/**
 *
 * @author PC
 */
public class EncryptionFactory {
    
    public static Encryptable getRandomEncryption(){
        Random rand = new Random();
        Encryptable temp;
        switch(rand.nextInt(3)){
            case 0: temp = new RailFenceCipher(); break;
            case 1: temp = new RouteCipher();  break;
            case 2: temp = new VigenereCipher();break;
            default: temp = new RailFenceCipher(); break;
        }
        
        return temp;
    }
}
