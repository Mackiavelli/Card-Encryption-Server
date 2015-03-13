/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encryption;

import encryption.Encryptable;

/**
 *
 * @author Martin Petkov
 */
public class RailFenceCipher implements Encryptable{
    private char[] encrypted;
    
    @Override
    public String encrypt(String plainText){
        plainText = plainText.replaceAll("\\s", "");
        encrypted = new char[plainText.length()];
        
        int currentIndex = 0;
                
        for(int step = 0; step <= 2; step++){
            for(int a = step; a < plainText.length(); a += 4 + (-2)*(step%2)){
                encrypted[currentIndex++] = plainText.charAt(a);
            }
        }
        
        return new String(encrypted);
        
    }
    
    @Override
    public String decrypt(String ciphertext){
        char[] result = new char[ciphertext.length()];
        int start = 0;
        int middle = ciphertext.length()/4 + (ciphertext.length()%4==0?0:1);
        int end = 3 * ciphertext.length() / 4 + 
                ((ciphertext.length()%4==2 || ciphertext.length()%4==1)?1:0);
        int edgeOffset = 0;
        int midOffset = 0;
        int currentPosition = 0;
        
        while(currentPosition < result.length){
            if(start + edgeOffset < middle){
                result[currentPosition++] = 
                                    ciphertext.charAt(start + edgeOffset);
            }
            if(middle + midOffset < end){
                result[currentPosition++] =
                                    ciphertext.charAt(middle + midOffset++);
            }
            if(end + edgeOffset < ciphertext.length()){
                result[currentPosition++] = 
                                    ciphertext.charAt(end + edgeOffset++);
            }
            if(middle + midOffset < end){
                result[currentPosition++] = 
                                    ciphertext.charAt(middle + midOffset++);
            }
        }
        
        return new String(result);
    }
}
