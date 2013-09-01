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
import cloudbox.module.Message;
import cloudbox.module.network.NetModule;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class UserModule extends AModule {
    private static String ms_strPkgName = UserModule.class.getPackage().getName();
    ExecutorService m_executorProcess = null;
    private boolean m_bServer;
    private boolean m_bEnabled;
    private String m_strUser;
    private String m_strPassword;
    private AModule m_fileModule;
    private NetModule m_netModule;     

    public void setFileModule(AModule f_module)
    {   m_fileModule = f_module;    }
    
    public void setNetModule(NetModule f_module)
    {
        if(m_netModule != null)
        {   m_netModule.dettachObs(this);   }
        
        m_netModule = f_module;   
        m_netModule.attachObs(this);
    }
    
    @Override
    public void attachService(AModule f_newService) {
        super.attachService(f_newService);
        if(m_bEnabled && m_bServer && status() == Status.RUNNING)
            f_newService.getNotification(new Message(this, new Command(Command.eType.ASKLOGIN)));
    }
        
    @Override
    public void start() {
        if(m_bEnabled) {
            int poolsize = m_bServer? 25:1;
            m_executorProcess = Executors.newFixedThreadPool(poolsize);
        }
        else {
            m_netModule.attachService(m_fileModule);
            m_fileModule.attachService(m_netModule);
        }
    }

    @Override
    public void stop() {
        if(m_bEnabled) {
           m_executorProcess.shutdown();
           m_executorProcess = null;
        }
    }

    @Override
    public Status status() {
        if(m_bEnabled && m_executorProcess != null)
        {   return Status.RUNNING;  }
        return Status.STOPPED;
   }

    @Override
    public void loadProperties() {
        if(!m_properties.contains(ms_strPkgName+".enabled")) {
            m_properties.put(ms_strPkgName+".enabled", "true");
        }
        
        m_bEnabled = "true".equals(m_properties.getProperty(ms_strPkgName+".enabled"));
        if(m_bEnabled){
            if(!m_properties.contains(ms_strPkgName+".user")) {
                m_properties.put(ms_strPkgName+".user", "user");
            }
            if(!m_properties.contains(ms_strPkgName+".password")) {
                m_properties.put(ms_strPkgName+".password", "");
            }
            m_strUser = m_properties.getProperty(ms_strPkgName+".user");
            m_strPassword = m_properties.getProperty(ms_strPkgName+".password");
        }
    }

    @Override
    public void getNotification(Message f_msg) {
       if(m_executorProcess != null && !m_executorProcess.isTerminated())
       {
           UserValidator userValidator;
           if(m_bServer) {
                userValidator = new UserServerValidator(this, m_fileModule, m_strUser, m_strPassword, f_msg);
           }
           else {
                userValidator = new UserClientValidator(this, m_fileModule, m_strUser, m_strPassword, f_msg);
           }
           
           m_executorProcess.execute(userValidator);
       }
    }

    @Override
    public void update(AModule f_module) {
        m_bServer = m_netModule.isServer();
    }
    
}
