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

public class NetFacade extends AModule {
    DownStream m_downStream;
    UpStream m_upStream;
    
    @Override
    public void notify(Message f_msg) {
        m_upStream.notify(f_msg);
    }

    public NetFacade(Socket f_sockClient) throws IOException {
        NetHandler netHandler = new NetHandler(f_sockClient);
        m_downStream = new DownStream(this, netHandler);
        m_upStream = new UpStream(netHandler);
    }
    
    public NetFacade( String localhost, short s) throws IOException {
        Socket client = new Socket(localhost, s);
        NetHandler netHandler = new NetHandler(client);
        m_downStream = new DownStream(this, netHandler);
        m_upStream = new UpStream(netHandler);
    }
    
    @Override
    public void start() {
        m_downStream.start();
        m_upStream.start();
    }

    public void join() throws InterruptedException {
        m_downStream.join();
        
    }
    
    @Override
    public void stop() {
        m_downStream.interrupt();
        m_upStream.interrupt();
    }

    
}
