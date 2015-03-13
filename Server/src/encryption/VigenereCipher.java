package encryption;
import encryption.Encryptable;
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author PC
 */
public class VigenereCipher implements Encryptable{

    private final String cipher;
    private static final char[][] encryptionTable = 
        {
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'},
            {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'},
            {'2', '3', '4', '5', '6', '7', '8', '9', '0', '1'},
            {'3', '4', '5', '6', '7', '8', '9', '0', '1', '2'},
            {'4', '5', '6', '7', '8', '9', '0', '1', '2', '3'},
            {'5', '6', '7', '8', '9', '0', '1', '2', '3', '4'},
            {'6', '7', '8', '9', '0', '1', '2', '3', '4', '5'},
            {'7', '8', '9', '0', '1', '2', '3', '4', '5', '6'},
            {'8', '9', '0', '1', '2', '3', '4', '5', '6', '7'},
            {'9', '0', '1', '2', '3', '4', '5', '6', '7', '8'}
        };
    
    //this function can be used to achieve a more random ciphering
    //it should be noted that a string should be used to keep the data
    //about the cipher, since it's different every time the program fires up
    private String generateCiper(){
        Random rand = new Random();
        long temp = Math.abs(rand.nextInt()); 
        return String.valueOf(temp);      
    }
    
    public VigenereCipher(){
        cipher = "9407309121";
    }

    @Override
    public String encrypt(String plaintext){
        char[] plainTemp = plaintext.toCharArray();
        char[] result = new char[plainTemp.length];
        
        for(int a = 0; a < plainTemp.length; a++){
            result[a] = 
                    encryptionTable
                    [cipher.charAt(a%cipher.length()) - '0']
                                            [(int)(plainTemp[a] - '0')];
        }
        
        return new String(result);
    }
    
    @Override
    public String decrypt(String ciphertext){
        char[] decryptTemp = ciphertext.toCharArray();
        char[] result = new char[ciphertext.length()];
        for(int a = 0; a < decryptTemp.length; a++){
            for(int b = 0; b < 10; b++){
                if(decryptTemp[a] == 
                        encryptionTable[cipher.charAt(a%cipher.length()) - '0'][b]){
                    result[a] = (char)(b + '0');
                }
            }
        }
        
        return new String(result);
    }
    
}

