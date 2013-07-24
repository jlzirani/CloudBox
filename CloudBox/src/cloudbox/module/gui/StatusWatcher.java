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


public class StatusWatcher implements Runnable {
    private Status m_oldState = Status.ERROR;
    private IModule m_fileModule;
    private javax.swing.JButton reloadBtn;
    private javax.swing.JButton startButton;
    private javax.swing.JLabel statusLabel;
    
    public void setReloadButton(javax.swing.JButton f_reloadBtn)
    {   reloadBtn = f_reloadBtn;    }
    
    public void setStartButton(javax.swing.JButton f_startButton) 
    {    startButton = f_startButton;    }    
    
    public void setStatusLabel(javax.swing.JLabel f_statusLabel)
    {   statusLabel = f_statusLabel;    }
    
    public void setFileModule(IModule f_fileModule)
    {   m_fileModule = f_fileModule; }
    
    @Override
    public void run() {
        IModule.Status status = m_fileModule.status();
        switch(status) {
            case RUNNING: if(m_oldState != IModule.Status.RUNNING) {
                            statusLabel.setText("Running");
                            startButton.setText("Stop"); 
                            startButton.setEnabled(true);
                          }
                          break;
            case STOPPED: if(m_oldState != IModule.Status.STOPPED) {
                            reloadBtn.setEnabled(true);
                            statusLabel.setText("Stopped");
                            startButton.setText("Start"); 
                            startButton.setEnabled(true);
                          }
                          break;
            default: statusLabel.setText("Error"); break;
        }
        m_oldState = status;
    }
}
