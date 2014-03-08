/* 
    Copyright 2014 Daniele Verducci
    This file is part of AndroidExplorerbotServer.

    AndroidExplorerbotServer is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    AndroidExplorerbotServer is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with AndroidExplorerbotServer.  If not, see <http://www.gnu.org/licenses/>.
*/

package it.danieleverducci.explorerbotserver;

import it.danieleverducci.explorerbot.objects.GamepadPosition;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerNetworkCommunicationThread extends Thread {
	private OnControllerPolledListener listener;
	private boolean mustExit=false;

	@Override
	public void run() {
		try {
			ServerSocket server = new ServerSocket(AppConfiguration.PORT);
			System.out.println("Server started on port "+AppConfiguration.PORT);
			Socket client = server.accept();
			System.out.println("Client connected");
			server.close();		//Once the client is connected, closing the server denies other clients connections
			InputStream is = client.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(is);
			while(!mustExit){
				//Read object and notify new controller position (readObject will return only when there is something to read)
				GamepadPosition pos = (GamepadPosition)ois.readObject();
				listener.onControllerPolled(pos);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	public void setOnControllerPolledListener(OnControllerPolledListener listener) {
		this.listener = listener;
	}

	public void setMustExit(boolean mustExit) {
		this.mustExit = mustExit;
	}

}
