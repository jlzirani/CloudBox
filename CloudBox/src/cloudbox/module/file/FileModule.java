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

package cloudbox.module.file;

import cloudbox.module.AModule;
import cloudbox.module.IModule;
import cloudbox.module.IObserver;
import cloudbox.module.Message;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileModule extends AModule implements IObserver {

    static private String ms_strPkgName=FileModule.class.getPackage().getName();
    private SyncFile m_syncFile;

    ExecutorService m_executorProcess = null;
    
    String m_strRootPath;
    
    public FileModule(String string) throws IOException {
        m_syncFile = new SyncFile(this, string);    
    }
    
    public FileModule() throws IOException {
        //m_syncFile = new SyncFile(this);    
    }
    
    public String getDirectoryWatcher() {
        return m_strRootPath;
    }
    
    private void setDirectory() throws IOException {
        m_strRootPath = m_properties.getProperty(ms_strPkgName+".directory");
        if(m_syncFile != null)
            m_syncFile.setDirPath(m_strRootPath);
    }
        
    public SyncFile getSyncFile() {
        return m_syncFile;        
    }

    @Override
    public void getNotification(Message f_msg) {
        if(m_executorProcess != null && !m_executorProcess.isTerminated())
            m_executorProcess.execute(new ProcessCmd(this, m_strRootPath, f_msg));
    }

    @Override
    public void start() {
        try {
            if(m_syncFile == null)
            {
                m_syncFile = new SyncFile(this, m_strRootPath);
                m_syncFile.start();
            }
            if(m_executorProcess == null)
            {   m_executorProcess = Executors.newFixedThreadPool(25);   }
            notifyObs();
        } catch (IOException ex) {
                Logger.getLogger(FileModule.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void stop() {
        if(m_syncFile != null && m_syncFile.isAlive())
        {
            m_syncFile.interrupt();
            try {
                 m_syncFile.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(FileModule.class.getName()).log(Level.SEVERE, null, ex);
            }
            m_syncFile = null;
        
        }

        if(m_executorProcess != null) {
            m_executorProcess.shutdown();
            m_executorProcess = null;
        }
        
        notifyObs();
    }

    @Override
    public Status status() {
        Status status = Status.ERROR; // by default the status is in error
        
        if( m_syncFile == null && 
                (m_executorProcess == null || m_executorProcess.isTerminated()))
        {   status = Status.STOPPED;    }
        else
        {
            if((m_executorProcess != null && !m_executorProcess.isTerminated()) 
                    || (m_syncFile != null && m_syncFile.isAlive() ))
            {   status = Status.RUNNING;    } 
            
        }
        
        return status;
    }

    @Override
    public void loadProperties() {
        if(!m_properties.containsKey(ms_strPkgName+".directory")) {   
            m_properties.setProperty(ms_strPkgName+".directory", 
                    System.getProperty("user.home")+File.separator+"CloudBox"+
                    File.separator);
        }
        try {
            setDirectory();
        } catch (IOException ex) {
            Logger.getLogger(FileModule.class.getName()).log(Level.SEVERE, null, ex);
        }
        notifyObs();
    }

    @Override
    public void update(IModule f_module) {
        if(f_module.status() == Status.STOPPED) {
            dettachService(f_module);
        }
    }

    
}
