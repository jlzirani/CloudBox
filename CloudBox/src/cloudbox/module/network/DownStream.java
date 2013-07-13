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
import cloudbox.module.Message;
import java.io.IOException;
import tools.Command;


public class DownStream extends Thread{
    private NetHandler m_netHandler;
    private IModule m_facade;

    public DownStream(IModule f_facade, NetHandler f_netHandler){
        m_netHandler = f_netHandler;
        m_facade = f_facade;
    }
    
    @Override
    public void run() {
        try {
      
            while(true) {
                Command cmd = m_netHandler.getCommand();
                // @TODO modify the null !
                m_facade.notifyObs( new Message( m_facade, cmd) );
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
    }


}
