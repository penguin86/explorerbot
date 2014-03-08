/* 
    Copyright 2014 Daniele Verducci
    This file is part of ExplorerBot.

    ExplorerBot is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    ExplorerBot is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with ExplorerBot.  If not, see <http://www.gnu.org/licenses/>.
*/

package it.danieleverducci.explorerbot;


import it.danieleverducci.explorerbot.client.PollControllerAndSendActivity;
import it.danieleverducci.explorerbot.server.PollNetworkAndSendToArduinoActivity;

public class Main {

	private static final String USAGE_EXPLAINED = "\nUsage: explorerbot [role]\nRoles: server or client.\n ES: explorerbot server\nES: explorerbot client 192.168.0.13";

	public static void main(String[] args) {
		//Verify arguments supplied
		if(args.length==0) {
			System.err.println("No role specified. "+USAGE_EXPLAINED);
			return;
		}

		if(args[0].equalsIgnoreCase("client")){
			if(args.length<2) System.err.println("You must specify the server's IP address ad parameter. "+USAGE_EXPLAINED);
			else new PollControllerAndSendActivity(args[1]);
		} else if(args[0].equalsIgnoreCase("server")){
			new PollNetworkAndSendToArduinoActivity();
		} else System.err.println(args[0]+": no role with this name. "+USAGE_EXPLAINED);
		
	}

	

}
