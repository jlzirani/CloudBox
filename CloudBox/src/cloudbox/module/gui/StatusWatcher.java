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
import javax.swing.JLabel;


public class StatusWatcher implements Runnable {
    private Status m_fileOldState = Status.ERROR;
    private FileModule m_fileModule;
    private javax.swing.JButton reloadBtn;
    private javax.swing.JButton startButton;
    private javax.swing.JLabel statusLabel;
    private NetModule m_netModule;
    private JLabel netStatusLabel;
    private JLabel modeLabel;
    private JLabel serverLabel;
    private JLabel dirWatcherLabel;
    private Status m_netOldState;
    
    public void setReloadButton(javax.swing.JButton f_reloadBtn)
    {   reloadBtn = f_reloadBtn;    }
    
    public void setStartButton(javax.swing.JButton f_startButton) 
    {    startButton = f_startButton;    }    
    
    public void setFileStatusLabel(javax.swing.JLabel f_statusLabel)
    {   statusLabel = f_statusLabel;    }
    
    public void setFileModule(FileModule f_fileModule)
    {   m_fileModule = f_fileModule; }
    
    void setNetModule(NetModule f_netModule) 
    {   m_netModule = f_netModule;  }

    void setNetStatusLabel(JLabel f_netStatusLabel) 
    {   netStatusLabel = f_netStatusLabel;  }

    void setModeLabel(JLabel f_modeLabel) 
    {   modeLabel = f_modeLabel;    }

    void setServerLabel(JLabel f_serverLabel) 
    {   serverLabel = f_serverLabel;    }
    
    void setDirLabel(JLabel f_dirWatcherLabel) 
    {   dirWatcherLabel = f_dirWatcherLabel;    }
    
   
    public void setFileStatus() {
        IModule.Status status = m_fileModule.status();
        switch(status) {
            case RUNNING: if(m_fileOldState != IModule.Status.RUNNING) {
                            statusLabel.setText("Running");
                            startButton.setText("Stop"); 
                            startButton.setEnabled(true);
                            
                          }
                          break;
            case STOPPED: if(m_fileOldState != IModule.Status.STOPPED) {
                            reloadBtn.setEnabled(true);
                            statusLabel.setText("Stopped");
                            startButton.setText("Start"); 
                            startButton.setEnabled(true);
                          }
                          break;
            default: statusLabel.setText("Error"); break;
        }
        dirWatcherLabel.setText(m_fileModule.getDirectoryWatcher());
        m_fileOldState = status;
    }
    
    public void setNetStatus() {
        IModule.Status status = m_netModule.status();
        switch(status) {
            case RUNNING: if(m_netOldState != IModule.Status.RUNNING) {
                            netStatusLabel.setText("Running");
                          }
                          break;
            case STOPPED: if(m_netOldState != IModule.Status.STOPPED) {
                            netStatusLabel.setText("STOPPED");
                          }
                          break;
            default: statusLabel.setText("Error"); break;
        }
        modeLabel.setText(m_netModule.getMode());
        serverLabel.setText(m_netModule.getHost()+":"+m_netModule.getPort());
        m_netOldState = status;
    }
        
    
    @Override
    public void run() {
        setFileStatus();
        setNetStatus();
    }

 

}
