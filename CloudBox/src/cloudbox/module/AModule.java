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
import java.util.Properties;

public abstract class AModule {
    
    public static enum Status { RUNNING, STOPPED, ERROR };
    
    final protected ArrayList m_vecServices = new ArrayList();
    final protected ArrayList m_vecObservers = new ArrayList();
    protected Properties m_properties;
    
    /* Observers */
        public void attachObs(AModule f_newObs) {
        synchronized (m_vecObservers) {
            m_vecObservers.add(f_newObs);
        }
    }
        public void dettachObs(AModule f_newObs){
        synchronized (m_vecObservers) {
            m_vecObservers.remove(f_newObs);
        }   
    }
    
    
        public void notifyObs() {
        synchronized (m_vecObservers) {
            for (Object o : m_vecObservers) {
                ((AModule) o).update(this);
            }
        }
    }
    
        public void attachService(AModule f_newService) {
        synchronized (m_vecServices) {
            m_vecServices.add(f_newService);
        }
    }

        public void dettachService(AModule f_newService) {
        synchronized (m_vecServices) {
            m_vecServices.remove(f_newService);
        }    
    }

        public void notifyServices(Message f_msg) {
        synchronized (m_vecServices) {
            for (Object o : m_vecServices) {
                ((AModule) o).getNotification(f_msg);
            }
        }
    }

    public void notifyServices(Command f_cmd) {
        notifyServices(new Message(this, f_cmd));
    }
    
        public void setProperties(Properties f_properties) {
        m_properties = f_properties;
        loadProperties();
    }
          
    /* starting and stopping the module */
    abstract public void start();
    abstract public void stop();
    abstract public Status status();

    /* properties */
    abstract public void loadProperties();
  
    
    abstract public void getNotification(Message f_msg);
    
    // Update an observer
    public void update( AModule f_subject )
    {
        // Do nothing
    }
    
}
