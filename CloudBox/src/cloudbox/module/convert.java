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

import java.nio.ByteBuffer;


public class convert {

    public static long bytesToLong(byte[] f_vecByte) {
        return ByteBuffer.wrap(f_vecByte).getLong();
    }

    public static int bytesToInt(byte[] f_vecByte) {
        return ByteBuffer.wrap(f_vecByte).getInt();
    }

    public static byte[] intToBytes(int f_iVal) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(f_iVal);
        return buffer.array();
    }

    public static byte[] longToBytes(long f_lVal) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(f_lVal);
        return buffer.array();
    }
    
    public static long bytesToLong(byte[] f_vecByte, int pos) {
        byte[] temp = new byte[8];
        System.arraycopy(f_vecByte, pos, temp, 0, 8);
        return ByteBuffer.wrap(temp).getLong();
    }

    public static int bytesToInt(byte[] f_vecByte, int pos) {
        byte[] temp = new byte[4];
        System.arraycopy(f_vecByte, pos, temp, 0, 4);
        return ByteBuffer.wrap(temp).getInt();
    }
    
    public static String bytesToString(byte[] f_vecByte, int pos, int lentgh)
    {
        byte[] temp = new byte[lentgh];
        System.arraycopy(f_vecByte, pos, temp, 0, lentgh);
        return new String(temp);
    }

    
}
