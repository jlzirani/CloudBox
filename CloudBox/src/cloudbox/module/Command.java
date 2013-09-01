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

package cloudbox.module;

import java.util.ArrayList;


public class Command {
    // TODO Modify Command to a dictionnary instead of this

    static public enum eType { GETINDEX, INDEX, GETFILE, FILE, GETPROPFILE, 
        PROPFILE, DELETE, LOGIN, ASKLOGIN, LOGINSUCCESSFULL };
    
    static public class cIndex {
        public String m_strName;
        public long m_date;
        
        public cIndex( String f_strName, long f_date ){
            m_strName = f_strName;
            m_date = f_date;
        }
    }
    
    private eType m_Type;
    private String m_Path = null; // used by all w/out LOGIN
    private boolean m_isDir = false; // used by PROPFILE
    private long m_length = 0; // used by PROPFILE
    private long m_date = 0; // used by PROPFILE and FILE
    private ArrayList Index = null; // used by INDEX
    private int m_sizeIndex = 4;
    private byte[] m_data = null;
    private String m_strUser = null, m_strPassword = null; // used by login
    
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
    
    public void setUser(String f_user) 
    {   this.m_strUser = f_user;    }
    
    public void setPassword(String f_password)
    {   this.m_strPassword = f_password; }
    
    public String getUser() 
    {   return this.m_strUser;    }
    
    public String getPassword()
    {   return this.m_strPassword; }
    
    public ArrayList getIndex() {
        return Index;
    }
    
    public int sizeOfIndex() {
        return m_sizeIndex;
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
    
}
