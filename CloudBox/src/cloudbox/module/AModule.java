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
package cloudbox.module;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import tools.Command;

public abstract class AModule implements Runnable, IModule {

    final static private Logger logger = Logger.getLogger(AModule.class.getName());
    final protected ArrayList m_vecActors = new ArrayList();
    final private Queue m_vecMessage = new LinkedList();

    public void attach(AModule f_newActor) {
        synchronized (m_vecActors) {
            m_vecActors.add(f_newActor);
        }
    }

    public void dettach(AModule f_Actor) {
        synchronized (m_vecActors) {
            m_vecActors.remove(f_Actor);
        }
    }

    public void notifyMsg(Message f_msg) {
        synchronized (m_vecActors) {
            for (Object o : m_vecActors) {
                ((AModule) o).pushMsg(f_msg);
            }
        }
    }

    public void notifyCmd(Command f_cmd) {
        notifyMsg(new Message(this, f_cmd));
    }

    public void pushMsg(Message f_msg) {
        synchronized (m_vecMessage) {
            m_vecMessage.add(f_msg);
            m_vecMessage.notify();
        }
    }

    public void pushCmd(AModule f_from, Command f_cmd) {
        pushMsg( new Message(f_from, f_cmd) ); 
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

    abstract public void start();

    abstract public void join() throws InterruptedException;

    abstract public void interrupt();
}
