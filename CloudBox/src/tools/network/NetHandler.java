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

package tools.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import tools.Command;

public class NetHandler {
    private Socket m_socket = null;
    private DataOutputStream m_streamOutput = null;
    private DataInputStream m_streamInput = null;

    public NetHandler(Socket f_newSocket) throws IOException
    {
        m_socket = f_newSocket;
        m_streamOutput = new DataOutputStream(m_socket.getOutputStream());
        m_streamInput = new DataInputStream(m_socket.getInputStream());
    }
    
    
    public void sendCommand(Command f_cmd) throws IOException{
        byte[] vecCommand = f_cmd.serializable();
        
        m_streamOutput.writeInt(vecCommand.length);
        m_streamOutput.write(vecCommand);
    }
    
    public Command getCommand() throws IOException{
        byte[] vecCommand = new byte[m_streamInput.readInt()];
        m_streamInput.readFully(vecCommand,0, vecCommand.length);
        return Command.unserializable(vecCommand);
    }
    
    /* @return Return the socket */
    public Socket getSocket() { return m_socket; }
    /* @return Return the data output handler */
    public DataOutputStream getOutputStream() { return m_streamOutput; }
    /* @return Return the data input handler */
    public DataInputStream getInputStream() { return m_streamInput; }    

}
