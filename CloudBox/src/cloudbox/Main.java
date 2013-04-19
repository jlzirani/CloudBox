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

import cloudbox.actors.Server;
import cloudbox.actors.file.FileActor;
import cloudbox.actors.network.Peer;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    final static private Logger logger = Logger.getLogger(Main.class.getName());
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        FileActor file = new FileActor(System.getProperty("user.home")+"/CloudBox/"+args[0]);
        file.start();

        if("server".equals(args[0]))
        {
            logger.log(Level.INFO, "Starting server");
            Server server = new Server(file);
            server.run();
        }

        if("client".equals(args[0]))
        {
            
            logger.log(Level.INFO, "Starting client");
            try {
                Peer client = new Peer(file, "localhost", (short)1337);
                client.attach(file);
                client.run();
            } catch (java.net.ConnectException ex) {
                logger.log(Level.SEVERE, "Connection refused");
            } catch (IOException ex) {
                logger.log(Level.SEVERE,null, ex);
            }
        }
        file.interrupt(); // we kill the file thread
    }
}
