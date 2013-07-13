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

import cloudbox.module.Message;
import cloudbox.module.file.FileActor;
import java.io.IOException;
import tools.Command;


public class NetProcess extends Thread{
    private Peer m_parent;
    private NetHandler m_netHandler;
    private FileActor m_actorFile;
    
    public NetProcess( Peer f_parent, FileActor f_actorFile, NetHandler f_netHandler ) {
        m_netHandler = f_netHandler;
        m_actorFile = f_actorFile;
        m_parent = f_parent;
    }
    
    private void AskPropFile(String f_strPath) throws IOException{
        Command cmd = new Command(Command.eType.GETPROPFILE);
        cmd.setPath(f_strPath);
        m_netHandler.sendCommand(cmd);        
    }
    
    
    @Override
    public void run() {
        try {
            AskPropFile("/");
      
            while(true) {
                Command cmd = m_netHandler.getCommand();
                m_parent.notifyCmd( cmd );
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
    }
    
    
}
