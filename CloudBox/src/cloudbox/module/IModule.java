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

import java.util.Properties;

/**
 *
 * @author Zirani J.-L.
 */
public interface IModule extends IService{
    
    public static enum Status { RUNNING, STOPPED, ERROR };
    
    /* Services */
    /* attach services */
    public void attachService(IService f_newObs);
    public void dettachService(IService f_newObs);
    
    /* Notify all the services */
    public void notifyServices(Message f_msg);
    public void notifyServices(Command f_cmd);
    
    /* Observers */
    public void attachObs(IObserver f_newObs);
    public void dettachObs(IObserver f_newObs);
    public void notifyObs();
    
    /* starting and stopping the module */
    public void start();
    public void stop();
    public Status status();

    /* properties */
    public void setProperties(Properties f_properties);
    public void loadProperties();

}
