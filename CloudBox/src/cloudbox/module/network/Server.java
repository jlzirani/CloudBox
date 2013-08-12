/*
 * Copyright (C) 2013 Zirani J.-L.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as 
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cloudbox.module.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jlzirani
 */

public class Server extends Thread {
    private static final Logger logger =  Logger.getLogger(Server.class.getName());
    private NetModule m_netModule;
    private int m_iPort;
    ServerSocket socket;
    
    public Server(NetModule f_netModule, int f_iPort) {
        m_netModule = f_netModule;
        m_iPort = f_iPort;
    }
    
    public void stopAccept()
    {
        if(socket != null)
        {
            try {
                logger.log(Level.INFO,"Shutting down the listen of the socket");
                socket.close();
            } catch (IOException ex) {
               logger.log(Level.SEVERE, null, ex);
            }
        }
    }
    
     
    public void run() {
        boolean bContinue = true;
        try {
            socket = new ServerSocket(m_iPort);
            socket.getReuseAddress();
            while(bContinue) {
                try {
                    Socket client = socket.accept();
                    m_netModule.addClient(new ClientModule(client));
                } 
                catch (SocketException ex) {
                    bContinue = false;
                }
                catch (IOException ex)
                {   logger.log(Level.SEVERE, null, ex); }
            }
        } catch (IOException ex) 
        {   logger.log(Level.SEVERE, null, ex); }
    }

}