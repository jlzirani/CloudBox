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
import cloudbox.module.IModule;
import cloudbox.module.IObserver;
import cloudbox.module.Message;
import cloudbox.module.file.FileActor;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;


public class NetFacade implements IModule {
    final protected ArrayList m_vecActors = new ArrayList();
    DownStream m_downStream;
    UpStream m_upStream;
    
    @Override
    public void attach(IObserver f_newObs) {
        synchronized (m_vecActors) {
            m_vecActors.add(f_newObs);
        }
    }

    @Override
    public void dettach(IObserver f_newObs) {
           synchronized (m_vecActors) {
            m_vecActors.remove(f_newObs);
        }    
    }

    @Override
    public void notifyObs(Message f_msg) {
        synchronized (m_vecActors) {
            for (Object o : m_vecActors) {
                ((IObserver) o).notify(f_msg);
            }
        }
    }

    @Override
    public void notify(Message f_msg) {
        m_upStream.notify(f_msg);
    }

    
    public NetFacade(FileActor f_actorFile, Socket f_sockClient) throws IOException {
        NetHandler netHandler = new NetHandler(f_sockClient);
        m_downStream = new DownStream(this, netHandler);
        m_upStream = new UpStream(netHandler);
    }
    
    public void start() {
        m_downStream.start();
        m_upStream.start();
    }
    
    
    
}
