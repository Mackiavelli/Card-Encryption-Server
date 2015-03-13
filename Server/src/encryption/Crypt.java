/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encryption;

/**
 *
 * @author PC
 */
public class Crypt implements Encryptable {
    //we are using a triple encryption system
    private final Encryptable[] encryptions = {new VigenereCipher(), 
                        new RailFenceCipher(), new RouteCipher()}; 
    

    public Crypt() {
    }

    @Override
    public String encrypt(String plaintext) {
        plaintext = plaintext.replaceAll("\\s+", "");
        String result
                = encryptions[0].encrypt(plaintext);
        
        if (encryptions.length > 1) {
            for (int a = 1; a < encryptions.length; a++) {
                result = encryptions[a].encrypt(result);
            }
        }
        
        result = result.substring(0, 4) + " " + result.substring(4, 8) + " " + 
                result.substring(8, 12) + " " + result.substring(12,
                                                            plaintext.length());
        return result;
    }   

    @Override
    public String decrypt(String ciphertext) {
        ciphertext = ciphertext.replaceAll("\\s+", "");
        String result
                = encryptions[encryptions.length - 1].decrypt(ciphertext);
        
        if (encryptions.length > 1) {
            for (int a = encryptions.length - 2; a >= 0; a--) {
                result = encryptions[a].decrypt(result);
            }
        }
        result = result.substring(0, 4) + " " + result.substring(4, 8) + " " + 
                result.substring(8, 12) + " " + result.substring(12,
                                                        ciphertext.length());
        System.out.println("we in here?");
        return result;
    }
    
    
}
