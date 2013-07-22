/*  Main: A student project that synchronize files between computers.
    Copyright (C) 2013  Zirani Jean-Luc

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package cloudbox;

import cloudbox.module.IModule;
import cloudbox.module.gui.GUIFacade;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    final static private Logger logger = Logger.getLogger(Main.class.getName());
    
    /*private static void server() {
        try {
            FileFacade file = new FileFacade(System.getProperty("user.home")+"/CloudBox/server");
            file.start();
            logger.log(Level.INFO, "Starting server");
            Server server = new Server(file);
            server.run();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
    
    private static void client() {
        try {
            FileFacade file = new FileFacade(System.getProperty("user.home")+"/CloudBox/client");
            file.start();
            logger.log(Level.INFO, "Starting client");

            NetFacade client = new NetFacade( "localhost", (short)1337);

            client.attach(file);
            file.attach(client);

            client.start();

            client.join();
        }  catch (IOException ex) {
            logger.log(Level.SEVERE,null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException, IOException {
        
        final Properties prop = new Properties();
        
        if(args.length == 0) {            
            
            try {
                prop.load(new FileInputStream("cloudbox.cfg"));
            }
            catch (FileNotFoundException ex) {
                logger.log(Level.WARNING, "The config file \"cloudbox.cfg\" was not found");
            }            
        }
        else {
            switch(args[0]) {
                default: logger.log(Level.WARNING, "Parameter {0} was not recognized", args[0]);
            }
        }
        
        
        try {
            IModule gui;
            gui = (IModule)Class.forName( prop.getProperty("interface", GUIFacade.class.getName()) ).newInstance();
            gui.setProperties(prop);
            gui.start();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        

        
        
        
    }
}
