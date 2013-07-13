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
package tools.network;

import cloudbox.module.network.NetHandler;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import tools.Command;
import tools.Command.eType;

/**
 *
 * @author Zirani J.-L.
 */
public class NetHandlerTest {
    
    static ServerSocket m_srvsockServer;
    static Socket m_sockServer;
    static Socket m_sockClient;
        
    public NetHandlerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() throws IOException {
        m_srvsockServer = new ServerSocket(1337);
        m_srvsockServer.setReuseAddress(true);
        m_sockClient = new Socket("localhost", 1337);
        m_sockServer = m_srvsockServer.accept();
    }
    
    @AfterClass
    public static void tearDownClass() {
        m_srvsockServer = null;
        m_sockClient = null;
        m_sockServer = null;
    }
    
    @Before
    public void setUp() throws UnknownHostException, IOException {
    }
    
    @After
    public void tearDown() {
    }



    /**
     * Test of testSendCommand method, of class NetHandler.
     */
    @Test
    public void testSendCommand() throws Exception {
        System.out.println("send and receive message");
        Command f_cmd1 = new Command(eType.GETFILE), f_cmd2;
        f_cmd1.setPath("/ab");
        NetHandler instance1 = new NetHandler(m_sockClient);
        NetHandler instance2 = new NetHandler(m_sockServer);
        instance1.sendCommand(f_cmd1);
        f_cmd2 = instance2.getCommand();
        
        assertEquals(f_cmd1.getType(), f_cmd2.getType());
        assertEquals(f_cmd1.getPath(), f_cmd2.getPath());
    }


    /**
     * Test of getSocket method, of class NetHandler.
     */
    @Test
    public void testGetSocket() throws IOException {
        System.out.println("getSocket");
        NetHandler instance = new NetHandler(m_sockClient);
        Socket result = instance.getSocket();
        assertEquals(m_sockClient, result);
    }

    /**
     * Test of getOutputStream method, of class NetHandler.
     */
    @Test
    public void testGetOutputStream() throws IOException {
        System.out.println("getOutputStream");
        NetHandler instance = new NetHandler(m_sockClient);
        DataOutputStream result = instance.getOutputStream();
        
        assertNotNull(result);
    }

    /**
     * Test of getInputStream method, of class NetHandler.
     */
    @Test
    public void testGetInputStream() throws IOException {
        System.out.println("getInputStream");
        NetHandler instance = new NetHandler(m_sockClient);
        DataInputStream result = instance.getInputStream();        
        
        assertNotNull(result);
    }
}
