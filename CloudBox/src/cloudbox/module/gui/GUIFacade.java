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

import cloudbox.module.AModule;
import cloudbox.module.Message;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;

public class GUIFacade extends AModule {
    private MainFrame m_mainFrame;
    static private String ms_strPkgName =GUIFacade.class.getPackage().getName();
    
    @Override
    public void start() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    m_mainFrame = new MainFrame(m_properties);
                    m_mainFrame.setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(GUIFacade.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
    }

    @Override
    public void stop() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void getNotification(Message f_msg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Status status() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadProperties() {
        if(!m_properties.containsKey("interface")) {
            m_properties.setProperty("interface", this.getClass().getName());            
        }
        
        if(!m_properties.containsKey(ms_strPkgName+".look")) {
            m_properties.setProperty(ms_strPkgName+".look", UIManager.getSystemLookAndFeelClassName());
        }

    }

}
