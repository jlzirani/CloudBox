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

/**
 *
 * @author Zirani J.-L.
 */
public class convertTest {
    
    public convertTest() {
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
     * Test of intToBytes method, of class NetHandler.
     */
    @Test
    public void testConvertIntToBytes() {
        System.out.println("convertIntToBytes");
        int f_iVal = 0;
        byte[] expResult = {0,0,0,0};
        byte[] result = convert.intToBytes(f_iVal);
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of longToBytes method, of class NetHandler.
     */
    @Test
    public void testConvertLongToBytes() {
        System.out.println("convertLongToBytes");
        long f_lVal = 0L;
        byte[] expResult = {0,0,0,0,0,0,0,0};
        byte[] result = convert.longToBytes(f_lVal);
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of bytesToInt method, of class NetHandler.
     */
    @Test
    public void testConvertBytesToInt() {
        System.out.println("convertBytesToInt");
        byte[] f_vecByte = {0,0,0,0};
        int expResult = 0;
        int result = convert.bytesToInt(f_vecByte);
        assertEquals(expResult, result);
        assertEquals(expResult, convert.bytesToInt(f_vecByte, 0));
    }

    /**
     * Test of bytesToLong method, of class NetHandler.
     */
    @Test
    public void testConvertBytesToLong() {
        System.out.println("convertBytesToLong");
        byte[] f_vecByte = {0,0,0,0,0,0,0,0};
        long expResult = 0L;
        long result = convert.bytesToLong(f_vecByte);
        assertEquals(expResult, result);
        assertEquals(expResult, convert.bytesToLong(f_vecByte, 0));
    }
    
    /**
     * Test of bytesToString method, of class NetHandler.
     */
    @Test
    public void testConvertBytesToString() {
        System.out.println("convertBytesToString");
        String str = "convertBytesToString";
        byte[] vecStr = str.getBytes();
        
        assertEquals(str, convert.bytesToString(vecStr, 0, vecStr.length));
    }
    
    
    
}
