/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author jlzirani
 */
public class NetHandler {
    private Socket m_Socket = null;
    private DataInputStream m_Input;
    private DataOutputStream m_Output;    
    
    public NetHandler(Socket f_Socket) throws IOException{
        m_Socket = f_Socket;
        m_Input = new DataInputStream(m_Socket.getInputStream());
        m_Output = new DataOutputStream(m_Socket.getOutputStream());
    }

    /**
     * @return the m_Input
     */
    public DataInputStream getInputStream() {
        return m_Input;
    }

    /**
     * @return the m_Output
     */
    public DataOutputStream getOutputStream() {
        return m_Output;
    }
    
    public void sendMessage(Message f_message) throws IOException{
        m_Output.writeInt(f_message.ordinal());
    }
    
    public Message getMessage() throws IOException{
        return Message.values()[ m_Input.readInt() ];
    }
    
    public void sendVecByte( byte[] f_vecByte ) throws IOException{
        m_Output.writeInt(f_vecByte.length);
        m_Output.write(f_vecByte);
    }
    
    public byte[] getVecByte() throws IOException{
        int size = m_Input.readInt();
        byte[] vecResult = new byte[size];
        m_Input.read(vecResult, 0, size);
        return vecResult;
    }
    
    
}
