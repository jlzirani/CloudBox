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
import cloudbox.module.IObserver;
import cloudbox.module.file.FileModule;
import cloudbox.module.network.NetModule;
import cloudbox.module.user.UserModule;
import java.awt.Component;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Zirani J.-L.
 */
public class MainFrame extends javax.swing.JFrame implements IObserver {
    static private String ms_strPkgName =MainFrame.class.getPackage().getName();
    private static final Logger logger = Logger.getLogger(MainFrame.class.getName());
    
    private OptionFrame optionFrame;
    private Properties m_properties;
    private FileModule m_fileModule;
    private UserModule m_userModule;
    private NetModule m_netModule;
    private boolean m_setUpdate;
    private final ExecutorService executor = Executors.newFixedThreadPool(1);
    final StatusWatcher statusWatcher = new StatusWatcher();
   
    
   
    /**
     * Creates new form MainFrame
     */
    public MainFrame(Properties f_prop) throws IOException {
        initComponents();
        setTitle("CloudBox");
        
        m_properties = f_prop;
        
        m_fileModule = new FileModule();
        m_netModule = new NetModule();
        
        m_userModule = new UserModule();
        m_userModule.setFileModule(m_fileModule);
        m_userModule.setNetModule(m_netModule);
        
        m_netModule.attachService(m_userModule);
        
        m_userModule.setProperties(m_properties);
        m_fileModule.setProperties(m_properties);
        m_netModule.setProperties(m_properties);
        optionFrame = new OptionFrame(this, m_properties);
       
        for (final javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
            final JRadioButtonMenuItem button = new JRadioButtonMenuItem();
            button.setText(info.getName());
            lookMenu.add(button);

            button.addActionListener(new java.awt.event.ActionListener() {
               @Override
               public void actionPerformed(java.awt.event.ActionEvent evt) {
                   setLook(info.getClassName());
                   button.setSelected(true);
               }
            });
             
            if( m_properties.getProperty(ms_strPkgName+".look").equals(info.getClassName()) ) {
                setLook(info.getClassName());   
                button.setSelected(true);
            }
        }
        setStatusLabel();

        m_fileModule.attachObs(this);
        m_netModule.attachObs(this);
    
    }

    private void setStatusLabel()
    {
        statusWatcher.setFileModule(m_fileModule);
        statusWatcher.setNetModule(m_netModule);
        
        statusWatcher.setMainFrame(this);
        
        statusWatcher.run();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        filePanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        statusMsgLabel = new javax.swing.JLabel();
        fileStatusLabel = new javax.swing.JLabel();
        dirWatcherMsgLabel = new javax.swing.JLabel();
        dirWatcherLabel = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        msgModeLabel = new javax.swing.JLabel();
        msgServerLabel = new javax.swing.JLabel();
        msgStatusLabel = new javax.swing.JLabel();
        netStatusLabel = new javax.swing.JLabel();
        modeLabel = new javax.swing.JLabel();
        serverLabel = new javax.swing.JLabel();
        allStartButton = new javax.swing.JToggleButton();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        saveCfgFile = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        exitButton = new javax.swing.JMenuItem();
        toolsMenu = new javax.swing.JMenu();
        lookMenu = new javax.swing.JMenu();
        optionsButton = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Directory watcher"));

        statusMsgLabel.setText("Status :");

        fileStatusLabel.setText("jLabel");

        dirWatcherMsgLabel.setText("Directory watcher :");

        dirWatcherLabel.setText("jLabel");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(statusMsgLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fileStatusLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 379, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(dirWatcherMsgLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dirWatcherLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMsgLabel)
                    .addComponent(fileStatusLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dirWatcherMsgLabel)
                    .addComponent(dirWatcherLabel)))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Network"));

        msgModeLabel.setText("Mode :");

        msgServerLabel.setText("Server :");

        msgStatusLabel.setText("Status :");

        netStatusLabel.setText("jLabel");

        modeLabel.setText("jLabel");

        serverLabel.setText("jLabel");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(msgStatusLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(netStatusLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(msgServerLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(serverLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(msgModeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(modeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(msgModeLabel)
                    .addComponent(modeLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(msgServerLabel)
                    .addComponent(serverLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(msgStatusLabel)
                    .addComponent(netStatusLabel)))
        );

        allStartButton.setText("Start/Stop");
        allStartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                allStartButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout filePanelLayout = new javax.swing.GroupLayout(filePanel);
        filePanel.setLayout(filePanelLayout);
        filePanelLayout.setHorizontalGroup(
            filePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, filePanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(allStartButton)
                .addContainerGap())
        );
        filePanelLayout.setVerticalGroup(
            filePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(filePanelLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(allStartButton)
                .addContainerGap(128, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("General", filePanel);

        fileMenu.setText("File");

        saveCfgFile.setText("Save config");
        saveCfgFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveCfgFileActionPerformed(evt);
            }
        });
        fileMenu.add(saveCfgFile);
        fileMenu.add(jSeparator1);

        exitButton.setText("Exit");
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });
        fileMenu.add(exitButton);

        menuBar.add(fileMenu);

        toolsMenu.setText("Tools");
        toolsMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toolsMenuActionPerformed(evt);
            }
        });

        lookMenu.setText("Look");
        toolsMenu.add(lookMenu);

        optionsButton.setText("Options");
        optionsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optionsButtonActionPerformed(evt);
            }
        });
        toolsMenu.add(optionsButton);

        menuBar.add(toolsMenu);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void optionsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optionsButtonActionPerformed
        optionFrame.setProperties();
        optionFrame.setVisible(true);
    }//GEN-LAST:event_optionsButtonActionPerformed

    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitButtonActionPerformed

    private void toolsMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toolsMenuActionPerformed

    }//GEN-LAST:event_toolsMenuActionPerformed

    private void saveCfgFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveCfgFileActionPerformed
        try {
            m_properties.store(new FileOutputStream("cloudbox.cfg"), "");
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_saveCfgFileActionPerformed

    private void allStartButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_allStartButtonActionPerformed

        allStartButton.setEnabled(false);
        if("Start".equals(allStartButton.getText()))
        {   
            logger.log(Level.INFO, "Starting modules");
            m_fileModule.start();
            m_userModule.start();
            m_netModule.start();
        }
        else
        {
            logger.log(Level.INFO, "Stoping modules");
            m_fileModule.stop();
            m_userModule.stop();
            m_netModule.stop();
        }
    }//GEN-LAST:event_allStartButtonActionPerformed

    public final void setLook(String f_strLook) {
        try {
            for( Component o: lookMenu.getMenuComponents() )
            {
                ((JRadioButtonMenuItem)o).setSelected(false);
            }
             
            UIManager.setLookAndFeel(f_strLook);
            SwingUtilities.updateComponentTreeUI(this);
            this.pack(); 
            
            SwingUtilities.updateComponentTreeUI(optionFrame);
            optionFrame.pack();
            
            m_properties.setProperty(ms_strPkgName+".look", f_strLook);
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JToggleButton allStartButton;
    public javax.swing.JLabel dirWatcherLabel;
    private javax.swing.JLabel dirWatcherMsgLabel;
    private javax.swing.JMenuItem exitButton;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JPanel filePanel;
    public javax.swing.JLabel fileStatusLabel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JMenu lookMenu;
    private javax.swing.JMenuBar menuBar;
    public javax.swing.JLabel modeLabel;
    private javax.swing.JLabel msgModeLabel;
    private javax.swing.JLabel msgServerLabel;
    private javax.swing.JLabel msgStatusLabel;
    public javax.swing.JLabel netStatusLabel;
    private javax.swing.JMenuItem optionsButton;
    private javax.swing.JMenuItem saveCfgFile;
    public javax.swing.JLabel serverLabel;
    private javax.swing.JLabel statusMsgLabel;
    private javax.swing.JMenu toolsMenu;
    // End of variables declaration//GEN-END:variables

    @Override
    public void update(IModule f_module) {
        logger.log(Level.INFO, "Update module");
        executor.execute(statusWatcher);
    }

    void updateServices() {
        if(m_netModule.status() == Status.RUNNING || m_fileModule.status() == Status.RUNNING) {
            m_setUpdate = true;
        }
        else {
            m_setUpdate = false;
            m_netModule.loadProperties();
            m_fileModule.loadProperties();
        }
    }
    
    public boolean mustUpdateServices()
    {   return m_setUpdate; }
    
}
