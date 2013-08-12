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

    private MainFrame mainFrame;
       
    public void setFileStatus() {
        IModule.Status status = m_fileModule.status();
        switch(status) {
            case RUNNING:   mainFrame.fileStatusLabel.setText("Running");
                            mainFrame.fileStartButton.setText("Stop"); 
                          break;
            case STOPPED:   mainFrame.fileReloadBtn.setEnabled(true);
                            mainFrame.fileStatusLabel.setText("Stopped");
                            mainFrame.fileStartButton.setText("Start"); 
                          break;
            default: mainFrame.fileStatusLabel.setText("Error"); break;
        }
        mainFrame.dirWatcherLabel.setText(m_fileModule.getDirectoryWatcher());
        mainFrame.fileStartButton.setEnabled(true); 

    }
    
    public void setNetStatus() {
        IModule.Status status = m_netModule.status();
        switch(status) {
            case RUNNING: mainFrame.netStatusLabel.setText("Running");
                          mainFrame.netStartButton.setText("Stop");
                          break;
            case STOPPED: 
                            mainFrame.netStatusLabel.setText("Stopped");
                            mainFrame.netStartButton.setText("Start");
                            mainFrame.netStatusLabel.setEnabled(true);
                          break;
            default: mainFrame.netStatusLabel.setText("Error"); break;
        }
        mainFrame.netStartButton.setEnabled(true);
        updateNetModule();
    }
        
    
    @Override
    public void run() {
        setFileStatus();
        setNetStatus();
        if(m_fileModule.status() == Status.STOPPED && m_netModule.status() == 
                Status.STOPPED)
            mainFrame.reloadBtn.setEnabled(true);
    }

    void setMainFrame(MainFrame aThis) {
        mainFrame = aThis;
    }

    void setFileModule(FileModule f_fileModule) {
        m_fileModule = f_fileModule;
    }

    void setNetModule(NetModule f_netModule) {
        m_netModule = f_netModule;
    }

    
    private void updateNetModule() {
        mainFrame.modeLabel.setText(m_netModule.getMode());

        if("Server".equals(m_netModule.getMode())) {
            mainFrame.serverLabel.setEnabled(false);
            mainFrame.serverLabel.setText("localhost"+":"+m_netModule.getPort());
        }
        else {
            mainFrame.serverLabel.setEnabled(true);
            mainFrame.serverLabel.setText(m_netModule.getHost()+":"+m_netModule.getPort());
        }
    }
    

}
