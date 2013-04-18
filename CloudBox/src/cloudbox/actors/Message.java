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

package cloudbox.actors;

import tools.Command;


public class Message {
    private Actor m_from;
    private Command m_cmd;
    
    public Message(Actor f_from, Command f_cmd){
        m_from = f_from;
        m_cmd = f_cmd;
    }
    
    public void setCmd(Command f_cmd) { m_cmd = f_cmd; }
    public Command getCmd() { return m_cmd; }
    
    public void set_from(Actor f_from) { m_from = f_from; }
    public Actor get_from() { return m_from; }
    
}
