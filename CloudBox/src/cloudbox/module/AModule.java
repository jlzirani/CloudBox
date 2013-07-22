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
import tools.Command;

public abstract class AModule implements IModule{
    final protected ArrayList m_vecActors = new ArrayList();
    protected Properties m_properties;
    
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
    public void notifyObs(Command f_cmd) {
        notifyObs(new Message(this, f_cmd));
    }
    
    @Override
    public void setProperties(Properties f_properties) {
        m_properties = f_properties;
        loadProperties();
    }
    
}
