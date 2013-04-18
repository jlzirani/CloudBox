/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudBox;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author user
 */
public interface FileInterfaceForResource extends Remote {
    
    public void addUser(User user) throws RemoteException;
    public List<User> getAllUser() throws RemoteException;
    public User getUser( String userName ) throws RemoteException;
    public List<CloudFile> getFileFromUser( String userName ) throws RemoteException;

    public void addFile(String userName, CloudFile newFile)  throws RemoteException;
}
