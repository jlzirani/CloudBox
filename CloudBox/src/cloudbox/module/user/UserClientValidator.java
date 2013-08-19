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
import static cloudbox.module.Command.eType.ASKLOGIN;
import static cloudbox.module.Command.eType.LOGINSUCCESSFULL;
import cloudbox.module.IModule;
import cloudbox.module.IObserver;
import cloudbox.module.Message;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserClientValidator extends UserValidator {
    private static final Logger logger =  Logger.getLogger(UserClientValidator.class.getName());
 
    public UserClientValidator(IModule f_parent, IModule f_fileModule, String f_strUser, String f_password, Message f_msg) {
        super(f_parent, f_fileModule, f_strUser, f_password, f_msg);
    }
    
    
    @Override
    public void run() {
        
        Command cmd = m_msg.getCmd();
        switch(cmd.getType())
        {
            case ASKLOGIN: sendLogin(m_msg); break;
            case LOGINSUCCESSFULL: loginSuccessFull(m_msg); break;
            default: logger.log(Level.WARNING, "Type {0} not recognized", cmd.getType());
        }
        
    }    
    
    private void sendLogin(Message f_msg) {
        Command cmd = new Command(eType.LOGIN);
        cmd.setUser(m_strUser);
        cmd.setPassword(m_strPassword);
        
        Message msg = new Message(m_parent, cmd);
        f_msg.get_from().getNotification(msg);
    }

    private void loginSuccessFull(Message f_msg) {
        IModule src = f_msg.get_from();
        src.dettachService(m_parent);
        src.attachService(m_fileModule); //replacing the service
        
        m_parent.dettachService(src);
        src.attachObs((IObserver) m_fileModule); //Dirty cast
        m_fileModule.attachService(src);
    }
    
}
