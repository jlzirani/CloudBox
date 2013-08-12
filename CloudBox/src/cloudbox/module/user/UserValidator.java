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

import cloudbox.module.Command;
import cloudbox.module.Command.eType;
import cloudbox.module.IModule;
import cloudbox.module.Message;

public class UserValidator implements Runnable {
    private IModule m_fileModule;
    private String m_strUser, m_strPassword;
    private Message m_msg;
    private IModule m_parent;
    
    public UserValidator() {} // cstr with no paramater
    public UserValidator(IModule f_parent, IModule f_fileModule, String f_strUser, String f_password, Message f_msg) {
        m_fileModule = f_fileModule;
        m_parent = f_parent;
        m_strUser = f_strUser;
        m_strPassword = f_password;
        m_msg = f_msg;
    }
    
    
    @Override
    public void run() {
        Command cmd = m_msg.getCmd();
        Command cmdAnswer = null;
        if(cmd.getType() == eType.LOGIN && m_strUser == cmd.getUser() && m_strPassword == cmd.getPassword())
        {
            IModule src = m_msg.get_from();
            src.dettachService(m_parent);
            src.attachService(m_fileModule); //replacing the service
            cmdAnswer = new Command(eType.LOGINSUCCESSFULL);
        }
        else 
        {   cmdAnswer = new Command(eType.ASKLOGIN);    }
        m_msg.get_from().getNotification(new Message(m_parent, cmdAnswer));
    }
    
}
