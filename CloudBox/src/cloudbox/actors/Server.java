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

package cloudbox.actors;

import cloudbox.actors.file.FileActor;
import cloudbox.actors.network.Peer;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Actor{
    FileActor m_fileActor;
    int m_iPort;
    
    public Server(FileActor f_fileActor, int f_iPort) {
        m_fileActor= f_fileActor;
        m_iPort = f_iPort;
    }
    
    public Server(FileActor f_fileActor) {
        m_fileActor= f_fileActor;
        m_iPort = 1337;
    }   
    
    @Override
    public void run() {
        try {
            ServerSocket socket = new ServerSocket(m_iPort);
            socket.getReuseAddress();
            while(true) {
                Socket client = socket.accept();
                Peer peer = new Peer(m_fileActor, client);
                peer.attach(m_fileActor);
                peer.start();                
            }
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    
    
    }

    @Override
    public void start() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void join() throws InterruptedException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void interrupt() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}
