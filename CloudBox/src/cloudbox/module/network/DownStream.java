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
import java.io.IOException;
import cloudbox.module.Command;
import java.io.DataInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DownStream extends Thread{
    final static private Logger logger = Logger.getLogger(DownStream.class.getName());
    private AModule m_facade;
    private DataInputStream m_streamInput;
    
    public DownStream(AModule f_facade, Socket f_socket) throws IOException{
        m_streamInput = new DataInputStream(f_socket.getInputStream());
        m_facade = f_facade;
    }
    
    public Command getCommand() throws IOException{
        byte[] vecCommand = new byte[m_streamInput.readInt()];
        m_streamInput.readFully(vecCommand,0, vecCommand.length);
        return Serialize.unserializable( vecCommand );
    }
        
    @Override
    public void run() {
        try {
            while(true) {
                Command cmd = getCommand();
                logger.log(Level.INFO, "Receiving : {0}", cmd.getType().toString());
                m_facade.notifyServices( cmd );
            }
        } catch (IOException ex) {
            m_facade.stop();
        }
   }


}
