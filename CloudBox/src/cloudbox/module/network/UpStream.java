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
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;


public class UpStream extends Thread {
    final static private Logger logger = Logger.getLogger(UpStream.class.getName());
    final private Queue m_vecMessage = new LinkedList();
    private NetHandler m_netHandler;
    
    public UpStream(NetHandler f_netHandler){
        m_netHandler = f_netHandler;
    }
    
    protected Message getFirstMsg() {
        Message msg;
        synchronized (m_vecMessage) {
            msg = (Message) m_vecMessage.poll();
        }
        return msg;
    }

    protected Message topMsg() {
        Message msg;
        synchronized (m_vecMessage) {
            msg = (Message) m_vecMessage.peek();
        }
        return msg;
    }

    protected void wait_message() {
        if (m_vecMessage.isEmpty()) {
            synchronized (m_vecMessage) {
                try {
                    m_vecMessage.wait();
                } catch (InterruptedException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public void notify(Message f_msg) {
        synchronized (m_vecMessage) {
            m_vecMessage.add(f_msg);
            m_vecMessage.notify();
        }
    }
    
    @Override
    public void run() {
        try {
            while(true) {
                    wait_message();
                    Message msg = getFirstMsg();
                    m_netHandler.sendCommand(msg.getCmd());
            }
        } catch (IOException ex) {
                Logger.getLogger(Peer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
 

}
