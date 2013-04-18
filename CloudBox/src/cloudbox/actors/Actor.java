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

package cloudbox.actors;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Actor implements Runnable{
    final static private Logger logger = Logger.getLogger(Actor.class.getName());
    final protected ArrayList m_vecActors = new ArrayList();
    final private Queue m_vecMessage = new LinkedList();
    
    public void attach(Actor f_newActor) {
        synchronized(m_vecActors) {
            m_vecActors.add(f_newActor);
        }
    }
    public void dettach(Actor f_Actor) { 
        synchronized(m_vecActors){
            m_vecActors.remove(f_Actor);
        }
    }
    
    public void push_message(Message o) {
        synchronized(m_vecMessage) {
            m_vecMessage.add(o); 
            m_vecMessage.notify();
        }
    }
    
    protected Message get_first_message() {
        Message msg;
        synchronized(m_vecMessage) {
            msg = (Message) m_vecMessage.poll();
        }
        return msg;
    }
    
    protected Message top_message() {
        Message msg;
        synchronized(m_vecMessage) {
            msg = (Message) m_vecMessage.peek();
        }
        return msg;
    }
    
    protected void wait_message() {
        if(m_vecMessage.isEmpty())
        {
            synchronized(m_vecMessage){
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
