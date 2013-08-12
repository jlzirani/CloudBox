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

package cloudbox.module.user;

import cloudbox.module.AModule;
import cloudbox.module.Command;
import cloudbox.module.Command.eType;
import cloudbox.module.IModule;
import cloudbox.module.IObserver;
import cloudbox.module.Message;
import java.util.logging.Level;
import java.util.logging.Logger;


public class UserClientModule extends AModule {
    private static final Logger logger =  Logger.getLogger(UserClientModule.class.getName());
    String m_strUser = "User", m_strPassword = "Password";
    private IModule m_fileModule;
    //ExecutorService m_executorProcess = null;

    public void setFileModule(IModule f_fileModule) {
        m_fileModule = f_fileModule;
    }
        
    public UserClientModule(IModule f_fileModule) {
        m_fileModule = f_fileModule;
    }
    
    @Override
    public void start() 
    {   /* Do nothing */    }

    @Override
    public void stop() 
    {   /* DO nothing */    }

    @Override
    public Status status() {
        return Status.RUNNING; // Always running
    }

    @Override
    public void loadProperties() 
    {   /* DO nothing */    }

    @Override
    public void getNotification(Message f_msg) {
        Command cmd = f_msg.getCmd();
        switch(cmd.getType())
        {
            case ASKLOGIN: sendLogin(f_msg); break;
            case LOGINSUCCESSFULL: loginSuccessFull(f_msg); break;
            default: logger.log(Level.WARNING, "Type {0} not recognized", cmd.getType());
        }
    }

    private void sendLogin(Message f_msg) {
        Command cmd = new Command(eType.LOGIN);
        cmd.setUser(m_strUser);
        cmd.setPassword(m_strPassword);
        
        Message msg = new Message(this, cmd);
        f_msg.get_from().getNotification(msg);
    }

    private void loginSuccessFull(Message f_msg) {
        IModule src = f_msg.get_from();
        src.dettachService(this);
        this.dettachService(src);
        src.attachService(m_fileModule); //replacing the service
        src.attachObs((IObserver) m_fileModule); //Dirty cast
        m_fileModule.attachService(src);
    }

}
