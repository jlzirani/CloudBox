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

package tools;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Command {
    static final private Logger logger = Logger.getLogger(Command.class.getName());

    static public enum eType { GETINDEX, INDEX, GETFILE, FILE, GETPROPFILE, 
        PROPFILE, DELETE };
    
    static public class cIndex {
        public String m_strName;
        public long m_date;
        
        public cIndex( String f_strName, long f_date ){
            m_strName = f_strName;
            m_date = f_date;
        }
    }
    
    private eType m_Type;
    private String m_Path = null; // used by all
    private boolean m_isDir = false; // used by PROPFILE
    private long m_length = 0; // used by PROPFILE
    private long m_date = 0; // used by PROPFILE and FILE
    private ArrayList Index = null; // used by INDEX
    private int m_sizeIndex = 4;
    private byte[] m_data = null;
    
    
    public Command( eType f_Type ) {
        m_Type = f_Type;
        if(m_Type == eType.INDEX) {
            Index = new ArrayList();
        }
    }
    
    public eType getType() { return m_Type; }
    
    public void setPath(String f_strName) {
        m_Path = f_strName;
    }
    
    public String getPath() {
        return m_Path;
    }

    public boolean getIsDir() {
        return m_isDir;
    }

    public void setIsDir(boolean m_isDir) {
        this.m_isDir = m_isDir;
    }

    public long getLength() {
        return m_length;
    }

    public void setLength(long m_length) {
        this.m_length = m_length;
    }

    public long getDate() {
        return m_date;
    }

    public void setDate(long m_date) {
        this.m_date = m_date;
    }
    
    public byte[] getData() {
        return m_data;
    }

    public void setData(byte[] m_data) {
        this.m_data = m_data;
    }
    
    public ArrayList getIndex() {
        return Index;
    }
    
    public void AddIndexFile( cIndex f_cIndex) {
        Index.add(f_cIndex);
        m_sizeIndex += 12 + f_cIndex.m_strName.length();
    }
    
    public void AddIndexFile( String f_strName, long f_data ) {
        AddIndexFile(new cIndex(f_strName, f_data));
    }
    
    public cIndex IndexGetFileName( String f_strName ){
        cIndex searchedItem = null;
        for( Object o: Index){
            cIndex it = (cIndex) o;
            if(it.m_strName.equals(f_strName)) {
                searchedItem =  it;
            }
        }
        return searchedItem;
    }
    
    
    private byte[] serializePath(){
        byte[] str = m_Path.getBytes();
        byte[] length = convert.intToBytes(str.length);
        byte[] result = new byte[str.length+4];
        
        System.arraycopy(length, 0, result, 0, 4);
        System.arraycopy(str, 0, result, 4, str.length);
        return result;
    }
    private byte[] serializeCmd(){
        return convert.intToBytes(m_Type.ordinal());
    }
    
    private byte[] serializeGet()
    {
        byte[] vecPath = serializePath();
        byte[] result = new byte[vecPath.length+4];
        
        System.arraycopy(serializeCmd(), 0, result, 0, 4);
        System.arraycopy(vecPath, 0, result, 4, vecPath.length);
        
        return result;
    }
    
    private byte[] serializeIndex()
    {
        byte[] vecPath = serializePath();
        byte[] result = new byte[vecPath.length+4+m_sizeIndex];
        
        System.arraycopy(serializeCmd(), 0, result, 0, 4);
        System.arraycopy(vecPath, 0, result, 4, vecPath.length);
        int index = vecPath.length + 4;
        System.arraycopy(convert.intToBytes(Index.size()), 0, result, index, 4);
        index += 4;
        
        for(Object it:  Index) {
            cIndex iit = (cIndex) it;
            byte[] str = iit.m_strName.getBytes();
            System.arraycopy(convert.intToBytes(str.length),0,result, index, 4);
            index += 4;
            System.arraycopy(str, 0, result, index, str.length);
            index += str.length;
            System.arraycopy(convert.longToBytes(iit.m_date),0,result,index, 8);
            index += 8;
        }
               
        return result;
    }
    
    private byte[] serualizablePropFile( ) {
        // {PROPFILE, PATH, ISDIR, DATE, LENGTH}
        byte[] vecPath = serializePath();
        byte[] result = new byte[vecPath.length+4+17];
        
        System.arraycopy(serializeCmd(), 0, result, 0, 4);
        System.arraycopy(vecPath, 0, result, 4, vecPath.length);
        int index = vecPath.length + 4;
        result[index++] = (m_isDir) ? (byte)1 : (byte) 0;
        System.arraycopy(convert.longToBytes(m_date),0,result,index,8);
        index += 8 ;
        System.arraycopy(convert.longToBytes(m_length),0,result,index,8);
    
        return result;
    }
    
    private byte[] serializeFile() {
        // {FILE, PATH, DATE, LENGTH, FILECONTENT}
        byte[] vecPath = serializePath();
        byte[] result = new byte[vecPath.length+4+16+m_data.length];
        System.arraycopy(serializeCmd(), 0, result, 0, 4);
        System.arraycopy(vecPath, 0, result, 4, vecPath.length);        
        int index = vecPath.length + 4;
        System.arraycopy(convert.longToBytes(m_date),0,result,index,8);
        index += 8 ;
        System.arraycopy(convert.longToBytes(m_data.length),0,result,index,8);
        index += 8;
        System.arraycopy(m_data,0,result,index,m_data.length);
        return result;
    }
    
    public byte[] serializable()
    {
        byte[] result = null;
        
        switch(m_Type){
            case GETINDEX:
            case GETFILE :
            case DELETE :
            case GETPROPFILE: result = serializeGet(); break;
            case INDEX: result = serializeIndex(); break;
            case PROPFILE: result = serualizablePropFile(); break;
            case FILE: result = serializeFile(); break;
            default: logger.log(Level.WARNING, "Type {0} non recognized", m_Type.toString());
        }
        
        return result;       
    }
    
    private int unserializablePath( byte[] f_data ) {
        byte[] vecSizePath = new byte[4];
        System.arraycopy(f_data,4,vecSizePath,0,4);
        int iSizePath = convert.bytesToInt(vecSizePath);
        byte[] vecPath = new byte[iSizePath];
        System.arraycopy(f_data,8,vecPath,0,iSizePath);
        setPath(new String(vecPath));
        return iSizePath+8;
    }
    
    private void unserializeIndex( byte[] f_data ){
        int index = unserializablePath(f_data);
        int nbrIndex = convert.bytesToInt(f_data, index);
        index += 4;
        
        for(int i =0; i < nbrIndex; ++i){
            int strSize = convert.bytesToInt(f_data, index);
            index += 4;
            String str = convert.bytesToString(f_data, index, strSize);
            index += strSize;
                      
            AddIndexFile(str, convert.bytesToLong(f_data, index));
            index += 8;
        }
    }
    
    private void unserualizablePropFile( byte[] f_data ) {
        // {PROPFILE, PATH, ISDIR, DATE, LENGTH}
        int index = unserializablePath(f_data);
        m_isDir = f_data[index++] == 1;
        m_date = convert.bytesToLong(f_data, index);
        index +=8;
        m_length = convert.bytesToLong(f_data, index);
    }

    private void unserializeFile(byte[] f_data) {
        // {FILE, PATH, DATE, LENGTH, FILECONTENT, DELETE}
        int index = unserializablePath(f_data);
        m_date = convert.bytesToLong(f_data, index);
        index += 8;
        m_length = convert.bytesToLong(f_data, index);
        index +=8;
        
        m_data = new byte[(int)m_length];
        System.arraycopy(f_data, index, m_data, 0, (int)m_length);
    }
    
    static public Command unserializable( byte[] f_data ) {
        byte[] vecType = new byte[4];
        System.arraycopy(f_data, 0, vecType, 0, 4);
        eType type = eType.values()[convert.bytesToInt(vecType)];
        Command cmd = new Command(type);
                
       switch(type){
            case GETINDEX:
            case GETFILE :
            case DELETE :
            case GETPROPFILE: cmd.unserializablePath( f_data ); break;
            case INDEX: cmd.unserializeIndex(f_data); break;
            case PROPFILE: cmd.unserualizablePropFile( f_data ); break;
            case FILE: cmd.unserializeFile(f_data); break;
            default: logger.log(Level.WARNING, "Type {0} non recognized", type.toString());
        }
              
        
        return cmd;
    }
    
    
    
}
