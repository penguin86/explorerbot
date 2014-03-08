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

package it.danieleverducci.explorerbot.client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import it.danieleverducci.explorerbot.AppConfiguration;
import it.danieleverducci.explorerbot.objects.GamepadPosition;

public class ClientNetworkCommunicationThread extends Thread {
	
	private String ipAddress;
	private boolean mustExit=false;
	private GamepadPosition lastKnownPosition;

	public ClientNetworkCommunicationThread(String ipAddress) {
		super();
		this.ipAddress = ipAddress;
	}

	@Override
	public void run() {
		try {
			//Setup connection
			Socket socket = new Socket(ipAddress, AppConfiguration.PORT);
			OutputStream os = socket.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);
			
			//Send gamepad position every 100msec
			while(!mustExit){
				if(lastKnownPosition!=null){
					//Send to network
					try{
						oos.writeObject(lastKnownPosition);
						lastKnownPosition=null;
					}catch(SocketException e){
						System.out.println("Connection interrupted by server. Exiting.");
						System.exit(0);
					}
				}
				//Wait 100ms
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			socket.close();
			
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Set @param mustExit to true to make the thread shutdown
	 */
	public void setMustExit(boolean mustExit) {
		this.mustExit = mustExit;
	}

	/**
	 * @return the last gamepad's known position
	 */
	public GamepadPosition getLastKnownPosition() {
		return lastKnownPosition;
	}

	/**
	 * Sets the @param lastKnownPosition to be sent to the server
	 */
	public void setLastKnownPosition(GamepadPosition lastKnownPosition) {
		this.lastKnownPosition = lastKnownPosition;
	}

}
