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

import cloudbox.module.Message;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import tools.Command;
import static tools.Command.eType.*;


public class ProcessCmd extends Thread {
    final static private Logger logger = Logger.getLogger(ProcessCmd.class.getName());
    final private Queue m_vecMessage = new LinkedList();
    private FileFacade m_facade;
    private String m_strRootPath;
    
    protected void pushMsg(Message f_msg) {
        synchronized (m_vecMessage) {
            m_vecMessage.add(f_msg);
            m_vecMessage.notify();
        }        
    }
    
    public void setDirPath(String f_strDirPath) {
        m_strRootPath = f_strDirPath;
    }
    
    
    protected Message getFirstMsg() {
        Message msg;
        synchronized (m_vecMessage) {
            msg = (Message) m_vecMessage.poll();
        }
        return msg;
    }

    protected Message topMsg() {
        Message msg;
        synchronized (m_vecMessage) {
            msg = (Message) m_vecMessage.peek();
        }
        return msg;
    }

    protected void wait_message() {
        if (m_vecMessage.isEmpty()) {
            synchronized (m_vecMessage) {
                try {
                    m_vecMessage.wait();
                } catch (InterruptedException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    public void run() {
        while(true){
            wait_message();
            Message msg = getFirstMsg();

            logger.log(Level.INFO, "processing message : {0} Path: {1}", 
                  new Object[]{msg.getCmd().getType(), msg.getCmd().getPath()});

            switch(msg.getCmd().getType()){
                case GETINDEX: getIndex(msg); break;
                case GETPROPFILE: getPropFile(msg); break;
                case GETFILE: getFile(msg); break;
                case INDEX:  index(msg); break;
                case PROPFILE: PropFile(msg); break;
                case FILE: File(msg); break;
                case DELETE: Delete(msg); break;
                default: 
                    logger.log(Level.WARNING, "Message with type :{0} not recognized",
                            msg.getCmd().getType() );
            }

        }
    }
 
    private void setLastDate(File f_file, long l_newDate) {
        m_facade.getSyncFile().inebitorFile( f_file.toPath() ); // unregister a file
        f_file.setLastModified(l_newDate);
    }
    
    public ProcessCmd( FileFacade f_facade) {
       m_facade = f_facade;
    }
    
   public ProcessCmd( FileFacade f_facade, String f_strRootPath) {
       m_facade = f_facade;       
       m_strRootPath = f_strRootPath;
   }
    
   private void getIndex(Message f_msg) {
        Command query = f_msg.getCmd();
        Command result = new Command(Command.eType.INDEX);
        
        File root = Tools.getFile( m_strRootPath, query.getPath());
        result.setPath( query.getPath() );
        
        for(File file : root.listFiles()){
            result.AddIndexFile( file.getName(), file.lastModified() );            
        }
        
        f_msg.get_from().notify(new Message(m_facade, result));
   }
    
   private void index(Message f_msg){
        Command query = f_msg.getCmd();
        String strPath = query.getPath();
        File[] vecFiles = Tools.getFile(m_strRootPath, query.getPath()).listFiles();

        if(vecFiles.length > query.getIndex().size() )
        {
            for(File file: vecFiles) {
                Command.cIndex distantFile = query.IndexGetFileName(file.getName());
                if( distantFile == null) {
                    file.delete();
                }
                
            }
        }
        
        for( Object o : query.getIndex() ){
            Command.cIndex it = (Command.cIndex)o; //Casting
            String strPathFile = strPath + File.separator + it.m_strName;
            File file = Tools.getFile(m_strRootPath, strPathFile);
            
            if(!file.exists() || file.lastModified() < it.m_date)
            {
                Command requestFile = new Command(Command.eType.GETPROPFILE);
                requestFile.setPath(strPathFile);
                f_msg.get_from().notify(new Message(m_facade, requestFile));
            }
            
            if(file.exists() && file.lastModified() > it.m_date) {
                f_msg.get_from().notify(new Message(m_facade, 
                           Tools.constructPropFile(m_strRootPath,strPathFile)));
            }
       }
   }
    
    private void getPropFile(Message f_msg) {
        Command query = f_msg.getCmd();
        Message msg = new Message(m_facade,  
                        Tools.constructPropFile(m_strRootPath,query.getPath()));
        
        f_msg.get_from().notify(msg);
    }
    
    private void PropFile(Message f_msg) {
        Command query = f_msg.getCmd();
        File file = Tools.getFile(m_strRootPath, query.getPath());
        Command reponse = null; 
       
        if(!file.exists())
        {
            if(query.getIsDir()) {
                file.mkdir();
                reponse = new Command(Command.eType.GETINDEX);
                setLastDate(file, query.getDate());
            }
            else 
            {   reponse = new Command(Command.eType.GETFILE);   }
        }
        else {
            if(file.lastModified() < query.getDate()) {
                if(query.getIsDir())
                {   reponse = new Command(Command.eType.GETINDEX);  }
                else
                {   reponse = new Command(Command.eType.GETFILE);   }
            }
        }
        
        if(reponse != null) {
            reponse.setPath(query.getPath());
            f_msg.get_from().notify(new Message(m_facade,reponse));
        }
    }
    
    private void getFile(Message f_msg) {
        FileInputStream inputStream = null;
        
        Command query = f_msg.getCmd();
        Command answer = new Command(Command.eType.FILE);
      
        try {
            answer.setPath(query.getPath());
            
            File file = Tools.getFile(m_strRootPath, query.getPath() );
            File parent = file.getParentFile();
            
            long parentDate = parent.lastModified() > query.getDate() ? 
                                parent.lastModified() : query.getDate();
            
            byte[] contentFile = new byte[(int)file.length()];
            inputStream = new FileInputStream(file);
            inputStream.read(contentFile);
            
            setLastDate(file, query.getDate());
            
            answer.setLength(file.length());
            answer.setDate(file.lastModified());
            answer.setData(contentFile);
            
            setLastDate(parent, parentDate);

            f_msg.get_from().notify(new Message(m_facade,answer));
        } catch (FileNotFoundException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }  finally {
            try {
                inputStream.close();
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
    
    }
    
    private void File( Message f_msg ) {

        OutputStream outputStream = null;
        
        Command query = f_msg.getCmd();
        File file = Tools.getFile(m_strRootPath, query.getPath() );
            
        m_facade.getSyncFile().inebitorFile( file.toPath() ); // unregister a file
        
        try {
            // opening the file and write on it
            outputStream = new FileOutputStream(file); 

            outputStream.write(query.getData());
            outputStream.flush();
            
            // modify the date
            file.setLastModified(query.getDate());
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }  finally {
            try {
                outputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }
    
    
    private void Delete(File f_file) {
        
        if(f_file.exists()) {
            if(f_file.isDirectory() && f_file.list().length>=0) {
                for(File file2Del : f_file.listFiles())
                    Delete(file2Del);
            }
            
            m_facade.getSyncFile().inebitorFile( f_file.toPath() );
            f_file.delete();
        }        
    }
    
    private void Delete(Message f_msg) {
        Command query = f_msg.getCmd();
        Delete(Tools.getFile(m_strRootPath, query.getPath()));
    }
   
}
