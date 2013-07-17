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
import java.io.IOException;

public class FileFacade extends AModule {

    private ProcessCmd m_processCmd;
    private SyncFile m_syncFile;

    public FileFacade(String string) throws IOException {
        m_processCmd = new ProcessCmd(this, string);
        m_syncFile = new SyncFile(this, string);    
    }
    
    public SyncFile getSyncFile() {
        return m_syncFile;        
    }

    @Override
    public void notify(Message f_msg) {
       m_processCmd.pushMsg(f_msg);
    }

    @Override
    public void start() {
        m_processCmd.start();
        m_syncFile.start();
    }
    
    @Override
    public void stop() {
        m_processCmd.interrupt();
    }

    
}
