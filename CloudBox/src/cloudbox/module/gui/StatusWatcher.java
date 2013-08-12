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

package cloudbox.module.gui;

import cloudbox.module.IModule;
import cloudbox.module.IModule.Status;
import static cloudbox.module.IModule.Status.RUNNING;
import static cloudbox.module.IModule.Status.STOPPED;
import cloudbox.module.file.FileModule;
import cloudbox.module.network.NetModule;


public class StatusWatcher implements Runnable {

    private FileModule m_fileModule;
    private NetModule m_netModule;

    private MainFrame m_mainFrame;
       
    public void setFileStatus() {
        IModule.Status status = m_fileModule.status();
        switch(status) {
            case RUNNING:   m_mainFrame.fileStatusLabel.setText("Running");
                          break;
            case STOPPED:   m_mainFrame.fileStatusLabel.setText("Stopped");
                          break;
            default: m_mainFrame.fileStatusLabel.setText("Error"); break;
        }
        m_mainFrame.dirWatcherLabel.setText(m_fileModule.getDirectoryWatcher());
//        mainFrame.fileStartButton.setEnabled(true); 

    }
    
    public void setNetStatus() {
        IModule.Status status = m_netModule.status();
        switch(status) {
            case RUNNING: m_mainFrame.netStatusLabel.setText("Running");
                          break;
            case STOPPED: 
                            m_mainFrame.netStatusLabel.setText("Stopped");
                           break;
            default: m_mainFrame.netStatusLabel.setText("Error"); break;
        }
 
        updateNetModule();
    }
        
    
    @Override
    public void run() {
        setFileStatus();
        setNetStatus();
        if(m_fileModule.status() == Status.STOPPED && m_netModule.status() == 
                Status.STOPPED)
        {
            if(m_mainFrame.mustUpdateServices())
                m_mainFrame.updateServices();
            m_mainFrame.allStartButton.setEnabled(true);
            m_mainFrame.allStartButton.setText("Start");
        }
        else {
            if(!m_mainFrame.allStartButton.isEnabled())
            {
                m_mainFrame.allStartButton.setText("Stop");
                m_mainFrame.allStartButton.setEnabled(true);
            }
        }

    }

    void setMainFrame(MainFrame aThis) {
        m_mainFrame = aThis;
    }

    void setFileModule(FileModule f_fileModule) {
        m_fileModule = f_fileModule;
    }

    void setNetModule(NetModule f_netModule) {
        m_netModule = f_netModule;
    }
    
    private void updateNetModule() {
        m_mainFrame.modeLabel.setText(m_netModule.getMode());

        if("Server".equals(m_netModule.getMode())) {
            m_mainFrame.serverLabel.setEnabled(false);
            m_mainFrame.serverLabel.setText("localhost"+":"+m_netModule.getPort());
        }
        else {
            m_mainFrame.serverLabel.setEnabled(true);
            m_mainFrame.serverLabel.setText(m_netModule.getHost()+":"+m_netModule.getPort());
        }
    }
    

}
