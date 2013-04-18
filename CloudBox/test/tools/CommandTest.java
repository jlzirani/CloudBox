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

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import tools.Command.eType;

/**
 *
 * @author Zirani J.-L.
 */
public class CommandTest {
    
    public CommandTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getType method, of class Command.
     */
    @Test
    public void testGetType() {
        System.out.println("getType");
        Command instance = new Command(Command.eType.FILE);
        eType expResult = eType.FILE;
        eType result = instance.getType();
        assertEquals(expResult, result);
    }

    /**
     * Test of getPath method, of class Command.
     */
    @Test
    public void testGetPath() {
        System.out.println("getPath");
        Command instance = new Command(Command.eType.FILE);
        instance.setPath("/");
        assertEquals("/", instance.getPath());
        instance.setPath("a");
        assertEquals("a", instance.getPath());
    }

    /**
     * Test of setIsDir method, of class Command.
     */
    @Test
    public void testSetIsDir() {
        System.out.println("setIsDir");
        boolean m_isDir = false;
        Command instance = new Command(Command.eType.FILE);
        instance.setIsDir(m_isDir);
        
        assert(!instance.getIsDir());
    }

    /**
     * Test of setLength method, of class Command.
     */
    @Test
    public void testSetLength() {
        System.out.println("setLength");
        long m_length = 0L;
        Command instance = new Command(Command.eType.FILE);
        instance.setLength(m_length);
        // TODO review the generated test code and remove the default call to fail.
        assertEquals(m_length, instance.getLength());
    }

    /**
     * Test of setDate method, of class Command.
     */
    @Test
    public void testSetDate() {
        System.out.println("setDate");
        long m_date = 0L;
        Command instance = new Command(Command.eType.FILE);
        instance.setDate(m_date);
        // TODO review the generated test code and remove the default call to fail.
        assertEquals(m_date, instance.getDate());
    }

    public void testSerialGetCommand(Command.eType f_type)
    {
        Command instance = new Command(f_type);
        instance.setPath("/");
        byte[] test = new byte[9]; 
        byte[] command = convert.intToBytes(f_type.ordinal());
        System.arraycopy(command, 0, test, 0, 4);
        System.arraycopy(convert.intToBytes(1), 0, test, 4, 4);
        System.arraycopy("/".getBytes(), 0, test, 8, 1);
        
        assertArrayEquals(test,instance.serializable() );
    }
        
    public void testSerializableIndex()
    {
        Command instance = new Command(Command.eType.INDEX);
        instance.setPath("/");
        instance.AddIndexFile("a", 0L);
        instance.AddIndexFile("b", 0L);
        byte[] test = new byte[39];
        byte[] command = convert.intToBytes(Command.eType.INDEX.ordinal());
        System.arraycopy(command, 0, test, 0, 4);
        System.arraycopy(convert.intToBytes(1), 0, test, 4, 4);
        System.arraycopy("/".getBytes(), 0, test, 8, 1);
        System.arraycopy(convert.intToBytes(2), 0, test, 9, 4);
        System.arraycopy(convert.intToBytes(1), 0, test, 13, 4);
        System.arraycopy("a".getBytes(), 0, test, 17, 1);
        System.arraycopy(convert.longToBytes(0L), 0, test, 18, 8);
        System.arraycopy(convert.intToBytes(1), 0, test, 26, 4);
        System.arraycopy("b".getBytes(), 0, test, 30, 1);
        System.arraycopy(convert.longToBytes(0L), 0, test, 31, 8);       
        
        assertArrayEquals(test,instance.serializable() );        
    }
    
    public void testSerializablePropFile() {
        Command instance = new Command(Command.eType.PROPFILE);
        instance.setPath("/");
        instance.setIsDir(true);
        instance.setDate(59L);
        instance.setLength(28L);
        
        byte[] test = new byte[26];
        byte[] command = convert.intToBytes(Command.eType.PROPFILE.ordinal());
        System.arraycopy(command, 0, test, 0, 4);
        System.arraycopy(convert.intToBytes(1), 0, test, 4, 4);
        System.arraycopy("/".getBytes(), 0, test, 8, 1);
        test[9] = (byte)1;
        System.arraycopy(convert.longToBytes(59L), 0, test, 10, 8);
        System.arraycopy(convert.longToBytes(28L), 0, test, 18, 8);
        
        assertArrayEquals(test,instance.serializable() );
        instance.setIsDir(false);
        test[9] = (byte)0;
        assertArrayEquals(test,instance.serializable() );
    }
    
    
    /**
     * Test of serializable method, of class Command.
     */
    @Test
    public void testSerializable() {
        
        testSerialGetCommand(Command.eType.GETFILE);
        testSerialGetCommand(Command.eType.GETINDEX);
        testSerialGetCommand(Command.eType.GETPROPFILE);
        testSerializableIndex();
        testSerializablePropFile();
    }
    
    public void testUnSerialGetCommand(Command.eType f_type) {
        byte[] test = new byte[9]; 
        byte[] command = convert.intToBytes(f_type.ordinal());
        System.arraycopy(command, 0, test, 0, 4);
        System.arraycopy(convert.intToBytes(1), 0, test, 4, 4);
        System.arraycopy("/".getBytes(), 0, test, 8, 1);
        
        Command instance = Command.unserializable(test);
        
        assertEquals(f_type, instance.getType());
        assertEquals("/", instance.getPath());
    }
    
    
    public void testUnSerialIndex() {
        byte[] test = new byte[39];
        byte[] command = convert.intToBytes(Command.eType.INDEX.ordinal());
        System.arraycopy(command, 0, test, 0, 4);
        System.arraycopy(convert.intToBytes(1), 0, test, 4, 4);
        System.arraycopy("/".getBytes(), 0, test, 8, 1);
        System.arraycopy(convert.intToBytes(2), 0, test, 9, 4);
        System.arraycopy(convert.intToBytes(1), 0, test, 13, 4);
        System.arraycopy("a".getBytes(), 0, test, 17, 1);
        System.arraycopy(convert.longToBytes(0L), 0, test, 18, 8);
        System.arraycopy(convert.intToBytes(1), 0, test, 26, 4);
        System.arraycopy("b".getBytes(), 0, test, 30, 1);
        System.arraycopy(convert.longToBytes(0L), 0, test, 31, 8);    
        Command cmd = Command.unserializable(test);
        
        
        assertEquals(Command.eType.INDEX, cmd.getType());
        assertEquals("/", cmd.getPath());
        assertEquals("a", ((Command.cIndex)cmd.getIndex().get(0)).m_strName);
        assertEquals(0L, ((Command.cIndex)cmd.getIndex().get(0)).m_date);
        assertEquals("b", ((Command.cIndex)cmd.getIndex().get(1)).m_strName);
        assertEquals(0L, ((Command.cIndex)cmd.getIndex().get(1)).m_date);
    }
    
    public void testUnSerialPropFile() {
        byte[] test = new byte[26];
        byte[] command = convert.intToBytes(Command.eType.PROPFILE.ordinal());
        System.arraycopy(command, 0, test, 0, 4);
        System.arraycopy(convert.intToBytes(1), 0, test, 4, 4);
        System.arraycopy("/".getBytes(), 0, test, 8, 1);
        test[9] = (byte)1;
        System.arraycopy(convert.longToBytes(59L), 0, test, 10, 8);
        System.arraycopy(convert.longToBytes(28L), 0, test, 18, 8);   
        Command cmd = Command.unserializable(test);
        
        
        assertEquals(Command.eType.PROPFILE, cmd.getType());
        assertEquals("/", cmd.getPath());
        assertEquals(true, cmd.getIsDir());
        assertEquals(59L, cmd.getDate());
        assertEquals(28L, cmd.getLength());
    }
    
    
    /**
    * Test of unserializable method, of class Command.
    */
    @Test
    public void testUnserializable() {
        testUnSerialGetCommand(Command.eType.GETFILE);
        testUnSerialGetCommand(Command.eType.GETINDEX);
        testUnSerialGetCommand(Command.eType.GETPROPFILE);
        testUnSerialIndex();
        testUnSerialPropFile();
    }
}
