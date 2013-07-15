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

import java.io.File;
import tools.Command;


public class Tools {
    
   static public File getFile(String strRootPath, String strRelativePath){
       return new File( strRootPath +"\\"+ strRelativePath);
   }
    
   static public Command constructPropFile( String strRootPath, String strRelativePath ) {
        Command result = new Command(Command.eType.PROPFILE);
        result.setPath( strRelativePath );
        File file = getFile( strRootPath, strRelativePath);
        
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
}
