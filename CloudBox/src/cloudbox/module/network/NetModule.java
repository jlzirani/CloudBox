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

package cloudbox.module.network;

import cloudbox.module.AModule;
import cloudbox.module.IModule;
import cloudbox.module.Message;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 *
 * @author jlzirani
 */

public class NetModule extends AModule {
    private static String ms_strPkgName = NetModule.class.getPackage().getName();
    private static final Logger logger =  Logger.getLogger(NetModule.class.getName());
    private final ArrayList m_vecClient = new ArrayList();
    private Server m_Server = null;
    private short m_port;
    private String m_host;
    private boolean m_bServer;

    @Override
    public void getNotification(Message f_msg) {
        synchronized(m_vecClient) {
            for(Object o: m_vecClient)
            {   ((IModule)o).getNotification(f_msg); }
        }
    }

    private void startServer() {
        // @TODO
    }
    
    @Override
    public void start() {
        if(m_bServer)   
        {   startServer();  }
        else
        {   addClient(new ClientModule(m_host, m_port));  }
    }

    @Override
    public void stop() {
        synchronized(m_vecClient) {
            for( Object o: m_vecClient )
            {   ((IModule)o).stop();    }
            m_vecClient.clear();
        }
        
        if(m_Server != null) 
        {   m_Server.stop();  }
    }

    @Override
    public Status status() {
        Status status;
        if(m_bServer) {            
            if(m_Server != null)
                status = Status.RUNNING;
            else
                status = Status.STOPPED;
        }
        else {
            synchronized(m_vecClient) {
                if(m_vecClient.isEmpty())
                    status = Status.STOPPED;
                else
                    status = ((IModule)m_vecClient.get(0)).status();
            }
        }
        return status;
    }

    @Override
    public void loadProperties() {
        if(!m_properties.containsKey(ms_strPkgName+".mode")) {   
            m_properties.setProperty(ms_strPkgName+".mode", 
                    "client");
        }
    
        if(!m_properties.containsKey(ms_strPkgName+".port")) {   
            m_properties.setProperty(ms_strPkgName+".port", 
                    "1337");
        }
    
        if("client".equals(m_properties.getProperty(ms_strPkgName+".mode")) ) {
            if( !m_properties.containsKey(ms_strPkgName+".server")) {
                m_properties.setProperty(ms_strPkgName+".server", "localhost");
            }
            m_host = m_properties.getProperty(ms_strPkgName+".server");
        }
        m_port = Short.parseShort(m_properties.getProperty(ms_strPkgName+".port"));
        
        m_bServer = "server".equals(m_properties.getProperty(ms_strPkgName+".mode"));
    }
    
    public String getMode() {
        if(m_bServer)
            return "Server";
        return "Client";
    }
    
    public int getPort() {
        return m_port;
    }

    public String getHost() {
        return m_host;
    }

    void addClient(ClientModule clientModule) {
        
        synchronized(m_vecServices){
            for( Object o: m_vecServices )
            {   clientModule.attachService((IModule)o);    }
        }
        
        synchronized(m_vecClient) {
                m_vecClient.add(new ClientModule(m_host, m_port));
        }
        
        clientModule.start();
    }
}
