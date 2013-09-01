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
import cloudbox.module.AModule;
import cloudbox.module.Message;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserServerValidator extends UserValidator {
    private static final Logger logger =  Logger.getLogger(UserServerValidator.class.getName());

    public UserServerValidator(AModule f_parent, AModule f_fileModule, String f_strUser, String f_password, Message f_msg) {
        super(f_parent, f_fileModule, f_strUser, f_password, f_msg);
    }
    
    @Override
    public void run() {
        Command cmd = m_msg.getCmd();
        
        if(cmd.getType() == eType.LOGIN && m_strUser.equals(cmd.getUser()) 
                && m_strPassword.equals(cmd.getPassword()))
        {
            logger.log(Level.INFO, "Login accepted" );
            AModule src = m_msg.get_from();

            Command cmdAnswer = new Command(eType.LOGINSUCCESSFULL);
            m_msg.get_from().getNotification(new Message(m_parent, cmdAnswer));

            src.dettachService(m_parent);
            m_parent.dettachService(src);
            
            src.attachService(m_fileModule); //replacing the service
            m_fileModule.attachService(src);
            
            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            
            cmdAnswer = new Command(eType.GETPROPFILE);
            cmdAnswer.setPath("/");
            m_msg.get_from().getNotification(new Message(m_parent, cmdAnswer));

        }
        else 
        {   
            Command cmdAnswer = new Command(eType.ASKLOGIN);
            m_msg.get_from().getNotification(new Message(m_parent, cmdAnswer));
        }
        
    }
    
}
