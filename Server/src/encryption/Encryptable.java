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
public interface Encryptable {
    public String encrypt(String plaintext);
    public String decrypt(String ciphertext);
}
