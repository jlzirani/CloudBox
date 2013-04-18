package server;

import GUI.FrameSrv;
import cloudBox.CloudFile;
import cloudBox.FileInterfaceForResource;
import cloudBox.User;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class Main {

    public static void main(String[] args) {
        System.setSecurityManager(new RMISecurityManager());
        
        try {
            FileInterfaceForResource srv = new Server();
            Registry registry = LocateRegistry.createRegistry( 1099 );
            
            srv.addUser(new User("Jimmy", new Date()));
            srv.addUser(new User("Pfff", new Date()));
            srv.addFile("Jimmy", new CloudFile("Jimmy", "pompom"));
            srv.addFile("Jimmy", new CloudFile("Jimmy", "pompom2"));
            srv.addFile("Pfff", new CloudFile("Pfff", "pompom3"));
            
            Naming.rebind("cloudBox", srv);
            
            FrameSrv frame = new FrameSrv(srv);
            frame.setVisible(true);
            
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, ex.getMessage());
        }
    }
}

