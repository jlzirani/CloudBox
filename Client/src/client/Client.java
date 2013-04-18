package client;

import GUI.ConnectionDialog;
import GUI.FrameAdmFile;
import cloudBox.FileInterfaceForResource;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author user
 */
public class Client {

    private static String propFilePath = "my_config.properties";
    private FileInterfaceForResource ressource = null;
    private Properties Prop = new Properties();
    private static final Logger log = Logger.getLogger(Client.class.getName());

    public Client() throws IOException {

        try {
            Prop.load(new FileInputStream(propFilePath));
            setRessources();
        } catch (NotBoundException | MalformedURLException | RemoteException ex) {
            showConnectionDialog(ex.getLocalizedMessage());
        } catch (FileNotFoundException ex) {
            Prop.setProperty("server", "localhost");
            Prop.setProperty("port", "1099");
            Prop.setProperty("user", "Jimmy");
            showConnectionDialog("");
        }

        if (ressource != null) {
            try {
                FrameAdmFile frame = new FrameAdmFile(ressource,Prop.getProperty("user"));
                frame.setVisible(true);
                frame.setTitle("Administrate your file.");
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    private void setRessources() throws NotBoundException, MalformedURLException, RemoteException {
        log.log(Level.INFO, "connection to {0}", Prop.getProperty("user") + "@" + Prop.getProperty("server") + ":" + Prop.getProperty("port"));
        ressource = (FileInterfaceForResource) Naming.lookup("//" + Prop.getProperty("server") + ":" + Prop.getProperty("port") + "/" + "cloudBox");
    }

    private void showConnectionDialog(String errorMsg) {

        final ConnectionDialog dialog = new ConnectionDialog(new JFrame(), true, Prop);
        dialog.setErrorMessages(errorMsg);
        dialog.addPropertyChangeListener("Connexion", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                try {
                    setRessources();
                    dialog.dispose();
                    Prop.store(new FileOutputStream(propFilePath), "");
                } catch (FileNotFoundException ex) {
                    log.log(Level.SEVERE, null, ex);
                } catch (NotBoundException | MalformedURLException | RemoteException ex) {
                    dialog.setErrorMessages(ex.getMessage());
                } catch (IOException ex) {
                    log.log(Level.SEVERE, null, ex);
                }
            }
        });
        dialog.setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
    }
}
