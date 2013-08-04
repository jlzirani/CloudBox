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

public abstract class AModule implements IModule{
    final protected ArrayList m_vecServices = new ArrayList();
    protected Properties m_properties;
    
    @Override
    public void attachService(IService f_newObs) {
        synchronized (m_vecServices) {
            m_vecServices.add(f_newObs);
        }
    }

    @Override
    public void dettachService(IService f_newObs) {
           synchronized (m_vecServices) {
            m_vecServices.remove(f_newObs);
        }    
    }

    @Override
    public void notifyServices(Message f_msg) {
        synchronized (m_vecServices) {
            for (Object o : m_vecServices) {
                ((IService) o).getNotification(f_msg);
            }
        }
    }

    @Override
    public void notifyServices(Command f_cmd) {
        notifyServices(new Message(this, f_cmd));
    }
    
    @Override
    public void setProperties(Properties f_properties) {
        m_properties = f_properties;
        loadProperties();
    }
    
}
