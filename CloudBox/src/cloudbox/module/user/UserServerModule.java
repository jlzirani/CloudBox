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
import cloudbox.module.IService;
import cloudbox.module.Message;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserServerModule extends AModule {

    String m_strUser = "User", m_strPassword = "Password";
    
    ExecutorService m_executorProcess = null;
    UserValidator m_userValidator = new UserValidator();
    
    public void attachService(IService f_newObs) {
        super.attachService(f_newObs);
        f_newObs.getNotification(new Message(this, new Command(eType.ASKLOGIN)));
    }
    
    @Override
    public void start() {
        m_executorProcess = Executors.newFixedThreadPool(25);
        
        notifyObs();
    }

    @Override
    public void stop() {
        m_executorProcess.shutdown();
        
        notifyObs();
    }

    @Override
    public Status status() {
        if(m_executorProcess != null)
            return Status.RUNNING;
        return Status.STOPPED;
    
    }

    @Override
    public void loadProperties() {
        // #TODO ! momentally deactivate    
    }

    @Override
    public void getNotification(Message f_msg) {
                if(m_executorProcess != null && !m_executorProcess.isTerminated())
            m_executorProcess.execute(new UserValidator(this, null, m_strUser, m_strPassword, f_msg));
    }
    
}
