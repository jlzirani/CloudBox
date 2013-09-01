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

package cloudbox.module.network;

import cloudbox.module.Command;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Serialize {
    static final private Logger logger = Logger.getLogger(Serialize.class.getName());

    
    static private byte[] serializePath(Command cmd){
        byte[] str = cmd.getPath().getBytes();
        byte[] length = intToBytes(str.length);
        byte[] result = new byte[str.length+4];
        
        System.arraycopy(length, 0, result, 0, 4);
        System.arraycopy(str, 0, result, 4, str.length);
        return result;
    }
    
    static private byte[] serializeCmd(Command cmd){
        return intToBytes(cmd.getType().ordinal());
    }
    
    static private byte[] serializeGet(Command cmd)
    {
        byte[] vecPath = serializePath(cmd);
        byte[] result = new byte[vecPath.length+4];
        
        System.arraycopy(serializeCmd(cmd), 0, result, 0, 4);
        System.arraycopy(vecPath, 0, result, 4, vecPath.length);
        
        return result;
    }
    
    static private byte[] serializeIndex(Command cmd)
    {
        byte[] vecPath = serializePath(cmd);
        byte[] result = new byte[vecPath.length+4+cmd.sizeOfIndex()];
        
        ArrayList Index = cmd.getIndex();
        
        System.arraycopy(serializeCmd(cmd), 0, result, 0, 4);
        System.arraycopy(vecPath, 0, result, 4, vecPath.length);
        int index = vecPath.length + 4;
        System.arraycopy(intToBytes(Index.size()), 0, result, index, 4);
        index += 4;
        
        for(Object it:  Index) {
            Command.cIndex iit = (Command.cIndex) it;
            byte[] str = iit.m_strName.getBytes();
            System.arraycopy(intToBytes(str.length),0,result, index, 4);
            index += 4;
            System.arraycopy(str, 0, result, index, str.length);
            index += str.length;
            System.arraycopy(longToBytes(iit.m_date),0,result,index, 8);
            index += 8;
        }
               
        return result;
    }
    
    static private byte[] serualizablePropFile( Command cmd ) {
        // {PROPFILE, PATH, ISDIR, DATE, LENGTH}
        byte[] vecPath = serializePath(cmd);
        byte[] result = new byte[vecPath.length+4+17];
        
        System.arraycopy(serializeCmd(cmd), 0, result, 0, 4);
        System.arraycopy(vecPath, 0, result, 4, vecPath.length);
        int index = vecPath.length + 4;
        result[index++] = cmd.getIsDir() ? (byte)1 : (byte) 0;
        System.arraycopy(longToBytes(cmd.getDate()),0,result,index,8);
        index += 8 ;
        System.arraycopy(longToBytes(cmd.getLength()),0,result,index,8);
    
        return result;
    }
    
    static private byte[] serializeFile( Command cmd ) {
        // {FILE, PATH, DATE, LENGTH, FILECONTENT}
        byte[] vecPath = serializePath(cmd);
        byte[] file = cmd.getData();
        byte[] result = new byte[vecPath.length+4+16+file.length];
        
        System.arraycopy(serializeCmd(cmd), 0, result, 0, 4);
        System.arraycopy(vecPath, 0, result, 4, vecPath.length);        
        int index = vecPath.length + 4;
        System.arraycopy(longToBytes(cmd.getDate()),0,result,index,8);
        index += 8 ;
        System.arraycopy(longToBytes(file.length),0,result,index,8);
        index += 8;
        System.arraycopy(file,0,result,index,file.length);
        return result;
    }
    
    
    static private byte[] serializeLogin(Command cmd) {
        // {LOGIN, USER, PASSWORD}

        byte[] vecUser = cmd.getUser().getBytes();
        byte[] vecPassword = cmd.getPassword().getBytes();
        byte[] lengthUser = intToBytes(vecUser.length);
        byte[] lengthPass = intToBytes(vecPassword.length);
        byte[] result = new byte[vecUser.length+vecPassword.length+12];
        
        System.arraycopy(serializeCmd(cmd), 0, result, 0, 4);

        System.arraycopy(lengthUser, 0, result, 4, 4);
        System.arraycopy(vecUser, 0, result, 8, vecUser.length);
        System.arraycopy(lengthPass, 0, result, 8+vecUser.length, 4);
        System.arraycopy(vecPassword, 0, result,12+vecUser.length , vecPassword.length);
        
        return result;
    }
    
    static public byte[] serializable(Command cmd)
    {
        byte[] result = null;
        
        switch(cmd.getType()){
            case GETINDEX:
            case GETFILE :
            case DELETE :
            case GETPROPFILE: result = serializeGet(cmd); break;
            case LOGIN: result = serializeLogin(cmd); break;
            case LOGINSUCCESSFULL: // Do nothing, same things as ASKLOGIN
            case ASKLOGIN: result = serializeCmd(cmd); break;
            case INDEX: result = serializeIndex(cmd); break;
            case PROPFILE: result = serualizablePropFile(cmd); break;
            case FILE: result = serializeFile(cmd); break;
            default: logger.log(Level.WARNING, "Type {0} non recognized", cmd.getType().toString());
        }
        
        return result;       
    }
    
    static private int unserializablePath( Command cmd, byte[] f_data ) {
        byte[] vecSizePath = new byte[4];
        System.arraycopy(f_data,4,vecSizePath,0,4);
        int iSizePath = bytesToInt(vecSizePath);
        byte[] vecPath = new byte[iSizePath];
        System.arraycopy(f_data,8,vecPath,0,iSizePath);
        cmd.setPath(new String(vecPath));
        return iSizePath+8;
    }
    
    static private void unserializeIndex( Command cmd, byte[] f_data ){
        int index = unserializablePath(cmd, f_data);
        int nbrIndex = bytesToInt(f_data, index);
        index += 4;
        
        for(int i =0; i < nbrIndex; ++i){
            int strSize = bytesToInt(f_data, index);
            index += 4;
            String str = bytesToString(f_data, index, strSize);
            index += strSize;
                      
            cmd.AddIndexFile(str, bytesToLong(f_data, index));
            index += 8;
        }
    }
    
    static private void unserualizablePropFile( Command cmd, byte[] f_data ) {
        // {PROPFILE, PATH, ISDIR, DATE, LENGTH}
        int index = unserializablePath(cmd, f_data);
        cmd.setIsDir(f_data[index++] == 1 );
        cmd.setDate( bytesToLong(f_data, index) );
        index +=8;
        cmd.setLength( bytesToLong(f_data, index) );
    }

    static private void unserializeFile(Command cmd, byte[] f_data) {
        // {FILE, PATH, DATE, LENGTH, FILECONTENT, DELETE}
        int index = unserializablePath(cmd, f_data);
        cmd.setDate( bytesToLong(f_data, index) );
        index += 8;
        cmd.setLength( bytesToLong(f_data, index) );
        index +=8;
        
        byte[] m_data = new byte[(int)cmd.getLength()];
        System.arraycopy(f_data, index, m_data, 0, (int)cmd.getLength());
    
        cmd.setData(m_data);
    }
    
    static private void unserializableLogin(Command cmd, byte[] f_data) {
        byte[] vecSizeStr = new byte[4];
        System.arraycopy(f_data,4,vecSizeStr,0,4);
        int iSizeStr = bytesToInt(vecSizeStr);
        cmd.setUser(bytesToString(f_data, 8,iSizeStr));
        
        int currentIndex = iSizeStr + 8;
        System.arraycopy(f_data,currentIndex,vecSizeStr,0,4);
        iSizeStr = bytesToInt(vecSizeStr);
        currentIndex += 4;
        cmd.setPassword(bytesToString(f_data, currentIndex, iSizeStr));
    }
 
    static public Command unserializable( byte[] f_data ) {
        byte[] vecType = new byte[4];
        System.arraycopy(f_data, 0, vecType, 0, 4);
        Command.eType type = Command.eType.values()[bytesToInt(vecType)];
        Command cmd = new Command(type);
                
       switch(type){
            case GETINDEX:
            case GETFILE :
            case DELETE :
            case GETPROPFILE: unserializablePath( cmd, f_data ); break;
            case LOGINSUCCESSFULL: // Do nothing, same things as ASKLOGIN
            case ASKLOGIN: break; // It is already unserializable :)
            case LOGIN: unserializableLogin( cmd, f_data ); break;
            case INDEX: unserializeIndex( cmd,f_data); break;
            case PROPFILE: unserualizablePropFile( cmd, f_data ); break;
            case FILE: unserializeFile(cmd, f_data); break;
            default: logger.log(Level.WARNING, "Type {0} non recognized", type.toString());
        }
              
        
        return cmd;
    }

    public static long bytesToLong(byte[] f_vecByte, int pos) {
        byte[] temp = new byte[8];
        System.arraycopy(f_vecByte, pos, temp, 0, 8);
        return ByteBuffer.wrap(temp).getLong();
    }

    public static byte[] longToBytes(long f_lVal) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(f_lVal);
        return buffer.array();
    }

    public static long bytesToLong(byte[] f_vecByte) {
        return ByteBuffer.wrap(f_vecByte).getLong();
    }

    public static int bytesToInt(byte[] f_vecByte, int pos) {
        byte[] temp = new byte[4];
        System.arraycopy(f_vecByte, pos, temp, 0, 4);
        return ByteBuffer.wrap(temp).getInt();
    }

    public static int bytesToInt(byte[] f_vecByte) {
        return ByteBuffer.wrap(f_vecByte).getInt();
    }

    public static byte[] intToBytes(int f_iVal) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(f_iVal);
        return buffer.array();
    }

    public static String bytesToString(byte[] f_vecByte, int pos, int lentgh) {
        byte[] temp = new byte[lentgh];
        System.arraycopy(f_vecByte, pos, temp, 0, lentgh);
        return new String(temp);
    }
}
