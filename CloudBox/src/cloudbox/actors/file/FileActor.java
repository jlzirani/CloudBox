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

package cloudbox.actors.file;

import cloudbox.actors.Actor;
import cloudbox.actors.Message;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import tools.Command;
import tools.Command.cIndex;
import tools.Command.eType;

public class FileActor extends Actor {
    private String m_strRootPath;
    private Thread m_thread;
    final static private Logger logger = Logger.getLogger(FileActor.class.getName());
    
    public FileActor(String f_strRootPath) {
        m_strRootPath = f_strRootPath;
        File root = new File(m_strRootPath);
        if(!root.exists())
            root.mkdirs();
    }
    
    private File getFile(String strPath){
        return new File( m_strRootPath + strPath);
   }
    
    
    private void getIndex(Message f_msg) {
        Command query = f_msg.getCmd();
        Command result = new Command(Command.eType.INDEX);
        File root = getFile( query.getPath());
        result.setPath( query.getPath() );
        
        for(File file : root.listFiles()){
            result.AddIndexFile( file.getName(), file.lastModified() );            
        }
        
        f_msg.get_from().push_message(new Message(this, result));
    }
    
    public Command constructPropFile( String strFilePath ) {
        
        Command result = new Command(Command.eType.PROPFILE);
        result.setPath( strFilePath );
        File file = getFile( strFilePath);
        
        if(file.isDirectory()) {
            result.setIsDir(true);
            result.setLength( (long)file.list().length );
        }
        else{
            result.setIsDir(false);
            result.setLength( file.length() );
        }
        
        result.setDate(file.lastModified());
        return result;
    }
    
    
    public void index(Message f_msg){
        Command query = f_msg.getCmd();
        String strPath = query.getPath();
        //File path = getFile( query.getPath() );
        File[] vecFiles = getFile(query.getPath()).listFiles();
        if(vecFiles.length > query.getIndex().size() )
        {
            for(File file: vecFiles) {
                cIndex distantFile = query.IndexGetFileName(file.getName());
                if( distantFile == null) {
                    file.delete();
                }
                
            }
        }
        
        for( Object o : query.getIndex() ){
            Command.cIndex it = (Command.cIndex)o; //Casting
            String strPathFile = strPath + File.separator + it.m_strName;
            File file = getFile( strPathFile);
            if(!file.exists() || file.lastModified() < it.m_date)
            {
                Command requestFile = new Command(eType.GETPROPFILE);
                requestFile.setPath(strPathFile);
                f_msg.get_from().push_message(new Message(this, requestFile));
            }
            
            if(file.exists() && file.lastModified() > it.m_date) {
                Message msg = new Message(this, constructPropFile(strPathFile));
                f_msg.get_from().push_message(msg);
            }
       }
    }
    
    public void getPropFile(Message f_msg) {
        Command query = f_msg.getCmd();
        Message msg = new Message(this, constructPropFile(query.getPath()));
        
        f_msg.get_from().push_message(msg);
    }
    
    public void PropFile(Message f_msg) {
        Command query = f_msg.getCmd();
        File file = getFile( query.getPath());
        
        if(file.lastModified() < query.getDate()) {
            if(query.getIsDir()){
                Command cmd = new Command(Command.eType.GETINDEX);
                cmd.setPath(query.getPath());
                f_msg.get_from().push_message(new Message(this, cmd));
            }
            else{
                Command cmd = new Command(Command.eType.GETFILE);
                cmd.setPath(query.getPath());
                f_msg.get_from().push_message(new Message(this, cmd));
            }
        }
    }
    
    
    public void getFile(Message f_msg) {
        
        
    }   
        
    @Override
    public void run() {
        while(true){
            wait_message();
            Message msg = get_first_message();
            
            logger.log(Level.INFO, "processing message : {0} Path: {1}", 
                  new Object[]{msg.getCmd().getType(), msg.getCmd().getPath()});
            switch(msg.getCmd().getType()){
                case GETINDEX: getIndex(msg); break;
                case GETPROPFILE: getPropFile(msg); break;
                case GETFILE: getFile(msg); break;
                case INDEX: index(msg); break;
                case PROPFILE: PropFile(msg); break;
                default: 
                    logger.log(Level.WARNING, "Message {0} not recognized",
                            msg.getCmd().getType() );
            }
            
        }

    }

    @Override
    public void start() {
        m_thread = new Thread(this);
        m_thread.start();
    }

    @Override
    public void join() throws InterruptedException {
        try{
            if(m_thread != null) {
                m_thread.join();
            }
        }
        finally {
            m_thread = null;
        }
    }

    @Override
    public void interrupt() {
        if(m_thread != null)
            m_thread.interrupt();
    }

}
