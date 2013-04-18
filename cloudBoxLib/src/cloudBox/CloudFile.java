/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudBox;

import java.io.Serializable;

/**
 *
 * @author jlzirani
 */
public class CloudFile implements Serializable {
    
    private String owner;
    private String nameFile;

    public CloudFile(String author, String nameFile) {
        this.owner = author;
        this.nameFile = nameFile;
    }
    
    public void setOwner(String owner) {
        this.owner = owner;
    }
    
    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }
    
    public String getOwner() {
        return this.owner;
    }    
    
    public String getNameFile() {
        return this.nameFile;
    }
}
