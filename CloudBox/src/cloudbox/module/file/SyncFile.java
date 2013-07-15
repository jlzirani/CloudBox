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

import cloudbox.module.IModule;
import static java.nio.file.StandardWatchEventKinds.*;
import static java.nio.file.LinkOption.*;
import java.nio.file.attribute.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import tools.Command;
/**
 *
 * @author jlzirani
 */


public class SyncFile extends Thread{
    private IModule m_facade;
    String m_strRootPath;
    private boolean trace = false;

    private final Map<WatchKey,Path> keys = new HashMap<>();
    private final WatchService watcher;
        
       
    public SyncFile(IModule f_facade, String f_strRootPath) throws IOException{
       watcher = FileSystems.getDefault().newWatchService();
       m_facade = f_facade;       
       m_strRootPath = f_strRootPath;
       registerAll(Paths.get(f_strRootPath));
   }
           
    /**
     * Register the given directory with the WatchService
     */
    private void register(Path dir) throws IOException {
        WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        if (trace) {
            Path prev = keys.get(key);
            if (prev == null) {
                System.out.format("register: %s\n", dir);
            } else {
                if (!dir.equals(prev)) {
                    System.out.format("update: %s -> %s\n", prev, dir);
                }
            }
        }
        keys.put(key, dir);
    }
    
    /**
     * Register the given directory, and all its sub-directories, with the
     * WatchService.
     */
    private void registerAll(final Path start) throws IOException {
        // register directory and sub-directories
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
                        @Override
                        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                throws IOException
            {
                register(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }
    
        /**
     * Process all events for keys queued to the watcher
     */
    @Override
    public void run() {
        while (true) {
 
            // wait for key to be signalled
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException x) {
                return;
            }
 
            Path dir = keys.get(key);
            if (dir == null) {
                System.err.println("WatchKey not recognized!!");
                continue;
            }
 
            for (WatchEvent<?> event: key.pollEvents()) {
                WatchEvent.Kind kind = event.kind();
 
                // TBD - provide example of how OVERFLOW event is handled
                if (kind == OVERFLOW) {
                    continue;
                }
 
                // Context for directory entry event is the file name of entry
                WatchEvent<Path> ev = (WatchEvent<Path>)event;
                Path name = ev.context();
                Path child = dir.resolve(name);
 
                // print out event
                System.out.format("%s child: %s\n", event.kind().name(), child );
 
                // if directory is created, and watching recursively, then
                // register it and its sub-directories
                
                Path relativePath =  Paths.get(m_strRootPath).relativize(child);
                
                if (kind == ENTRY_DELETE) {
                    Command cmd = new Command(Command.eType.DELETE);
                    cmd.setPath(relativePath.toString());
                    m_facade.notifyObs( cmd );
                }
         
                if (kind == ENTRY_CREATE) {
                    try {
                        m_facade.notifyObs( Tools.constructPropFile(m_strRootPath, 
                                relativePath.toString()  ) );
                        
                        if (Files.isDirectory(child, NOFOLLOW_LINKS)) {
                            registerAll(child);
                        }
                    } catch (IOException x) {
                        // ignore to keep sample readbale
                    }
                }
            }
 
            // reset key and remove from set if directory no longer accessible
            boolean valid = key.reset();
            if (!valid) {
                keys.remove(key);
 
                // all directories are inaccessible
                if (keys.isEmpty()) {
                    break;
                }
            }
        }
    }
    
    
    
}
