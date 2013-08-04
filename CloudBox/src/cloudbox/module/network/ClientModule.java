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
import cloudbox.module.AModule;
import cloudbox.module.Message;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientModule extends AModule {
    private String m_host;
    private short m_port;
    private DownStream m_downStream;
    private UpStream m_upStream;
    
    @Override
    public void getNotification(Message f_msg) {
        m_upStream.notify(f_msg);
    }

    public ClientModule(Socket f_client) throws IOException {
        NetHandler netHandler = new NetHandler(f_client);
        m_downStream = new DownStream(this, netHandler);
        m_upStream = new UpStream(netHandler);
    }
    
    public ClientModule( String f_host, short f_port) {
        m_host = f_host;
        m_port = f_port;
    }
    
    @Override
    public void start() {
        try {
            Socket client = new Socket(m_host, m_port);
            NetHandler netHandler = new NetHandler(client);
            m_downStream = new DownStream(this, netHandler);
            m_upStream = new UpStream(netHandler);
            
            m_downStream.start();
            m_upStream.start();
        } catch (UnknownHostException ex) {
            Logger.getLogger(ClientModule.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ClientModule.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void stop() {
        m_downStream.interrupt();
        m_upStream.interrupt();
    }

    @Override
    public Status status() {
        Status status = Status.ERROR;
        if( m_downStream != null & m_upStream != null)
            status = Status.RUNNING;
        if( m_downStream == null & m_upStream == null)
            status = Status.STOPPED;
        return status;
    }

    @Override
    public void loadProperties() {

        
    }

    
}
