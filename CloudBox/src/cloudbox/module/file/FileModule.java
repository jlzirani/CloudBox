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
import cloudbox.module.Message;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileModule extends AModule {

    static private String ms_strPkgName=FileModule.class.getPackage().getName();
    private SyncFile m_syncFile;
    
    private Thread m_threadFile;
    ExecutorService m_executorProcess = null;
    
    String m_strRootPath;
    
    public FileModule(String string) throws IOException {
        m_syncFile = new SyncFile(this, string);    
    }
    
    public FileModule() throws IOException {
        m_syncFile = new SyncFile(this);    
    }
    
    public String getDirectoryWatcher() {
        return m_strRootPath;
    }
    
    private void setDirectory() throws IOException {
        m_strRootPath = m_properties.getProperty(ms_strPkgName+".directory");
        m_syncFile.setDirPath(m_strRootPath);
    }
        
    public SyncFile getSyncFile() {
        return m_syncFile;        
    }

    @Override
    public void notify(Message f_msg) {
        if(m_executorProcess != null && !m_executorProcess.isTerminated())
            m_executorProcess.execute(new ProcessCmd(this, m_strRootPath, f_msg));
    }

    @Override
    public void start() {
        m_threadFile = new Thread(m_syncFile);
        m_executorProcess = Executors.newFixedThreadPool(25);
        m_threadFile.start();
    }
    
    @Override
    public void stop() {
        m_threadFile.interrupt();
        
        try {
             m_threadFile.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(FileModule.class.getName()).log(Level.SEVERE, null, ex);
        }
        m_executorProcess.shutdown();
        m_threadFile = null;
    }

    @Override
    public Status status() {
        Status status = Status.ERROR; // by default the status is in error
        
        if( m_threadFile == null && 
                (m_executorProcess == null || m_executorProcess.isTerminated()))
        {   status = Status.STOPPED;    }
        else
        {
            if((m_executorProcess != null && !m_executorProcess.isTerminated()) 
                    || (m_threadFile != null && m_threadFile.isAlive() ))
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
    }

    
}
