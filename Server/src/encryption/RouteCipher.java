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
public class RouteCipher implements Encryptable{
    private final int key;
    
    private int generateKey(){
        Random rand = new Random();
        int temp = rand.nextInt(7) + 2;
        return temp;
    }
    
    public RouteCipher(){
        key = 5;
    }
    
    public RouteCipher(int key){
        this.key = key;
    }
    
    public int getKey(){
        return key;
    }
    
    @Override
    public String encrypt(String plaintext){
        int currentOffest = 0;
        int currentPosition = 0;
        char[] result = new char[plaintext.length()];
        
        for(int a = 0; a < key; a++){
            while(plaintext.length() - currentOffest - 1 - a >= 0){
                result[currentPosition++] = 
                        plaintext.charAt(plaintext.length() - currentOffest - 1 - a);
                currentOffest += key;
            }
            currentOffest = 0;
        }
        
        return new String(result);
    }
    
    @Override
    public String decrypt(String ciphertext){
        int remainder = ciphertext.length() % key;
        int passedChars = 0;
        char[] result = new char[ciphertext.length()];
        
        for(int a = 0; a < key; a++){
            int loopLength = ciphertext.length() / key ;
            
            if(remainder-- > 0){
                loopLength++;
            }
            
            for(int b = 0; b < loopLength; b++){
                result[result.length - 1 - a - b * key] = 
                        ciphertext.charAt(passedChars++);
            }
            
        }
        return new String(result);
    }
}
