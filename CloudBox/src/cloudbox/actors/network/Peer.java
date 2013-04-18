/*  CloudBox: A student project that synchronize files between computers.
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
package cloudbox.actors.network;

import cloudbox.actors.Actor;
import cloudbox.actors.Message;
import cloudbox.actors.file.FileActor;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import tools.network.NetHandler;

public class Peer extends Actor {
    FileActor m_actorFile;
    NetHandler m_netHandler;
    NetProcess m_thdNet;
    Thread m_thread;
    
    public Peer(FileActor f_actorFile, String server, short port) throws IOException {
        m_actorFile = f_actorFile;
        Socket sockClient = new Socket(server,port);
        m_netHandler = new NetHandler(sockClient);
    }
    
    public Peer(FileActor f_actorFile, Socket f_sockClient) throws IOException {
        m_actorFile = f_actorFile;
        m_netHandler = new NetHandler(f_sockClient);
    }

    
    @Override
    public void run() {
        m_thdNet = new NetProcess(this, m_actorFile, m_netHandler);
        m_thdNet.start();
        try {
            while(true) {
                    wait_message();
                    Message msg = get_first_message();
                    m_netHandler.sendCommand(msg.getCmd());
            }
        } catch (IOException ex) {
                Logger.getLogger(Peer.class.getName()).log(Level.SEVERE, null, ex);
        }
        m_thdNet.interrupt();
    }

    @Override
    public void start() {
        m_thread = new Thread(this);
        m_thread.start();
    }

    @Override
    public void join() throws InterruptedException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void interrupt() {
    }


}


