/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudBox;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author jlzirani
 */
public class User implements Serializable
{
    private String name;
    private Date creationDate;

    private List<CloudFile> listFile;

    public List<CloudFile> getListFile() {
        return listFile;
    }
    
    public User(String name, Date creationDate) {
        this.name = name;
        this.creationDate = creationDate;
        this.listFile = new ArrayList<CloudFile>();
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public int getNbrFile(){
        return this.listFile.size();
    }
    
    public void addFile(CloudFile file) {
        listFile.add(file);
    }
    
}
