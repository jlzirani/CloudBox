package server;

import cloudBox.CloudFile;
import cloudBox.FileInterfaceForResource;
import cloudBox.User;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class Server extends UnicastRemoteObject implements FileInterfaceForResource {

    private List<User> listUser;
    
    public Server() throws RemoteException {
        this.listUser = new ArrayList<>();
    }
    
    @Override
    public void addUser(User user) throws RemoteException {
        listUser.add(user);
    }

    @Override
    public List<User> getAllUser() throws RemoteException {
        return listUser;
    }

    @Override
    public List<CloudFile> getFileFromUser(String userName) throws RemoteException {
        User userFound = getUser(userName);
        if(userFound == null) {
            return null;
        }
        return userFound.getListFile();
    }

    @Override
    public User getUser(String userName) throws RemoteException {
        User userFound = null;
        for(User user: listUser)
        {
            if(user.getName() == null ? userName == null : user.getName().equals(userName)) {
                userFound = user;
            }   
        }
        return userFound;
    }

    @Override
    public void addFile(String userName, CloudFile newFile) throws RemoteException {
        getUser(userName).addFile(newFile);
    }
}
