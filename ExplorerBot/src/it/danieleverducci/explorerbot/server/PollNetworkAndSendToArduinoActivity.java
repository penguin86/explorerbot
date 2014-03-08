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

package it.danieleverducci.explorerbot.server;

import java.net.InetAddress;
import java.net.UnknownHostException;

import jssc.SerialPortException;
import it.danieleverducci.explorerbot.interfaces.OnControllerPolledListener;
import it.danieleverducci.explorerbot.objects.GamepadPosition;

public class PollNetworkAndSendToArduinoActivity implements OnControllerPolledListener {
	private SerialCommunication serial;

	public PollNetworkAndSendToArduinoActivity(){
		//Initialize serial communication
		serial = new SerialCommunication();

		//Print server address
		try {
			InetAddress thisMachine = InetAddress.getLocalHost();
			System.out.println("This server address is: "+thisMachine.getHostAddress());
		} catch (UnknownHostException e) {
			System.out.println("Unable to retrieve this machine IP address.");
		}

		//Start network communication server
		ServerNetworkCommunicationThread sct = new ServerNetworkCommunicationThread();
		sct.setOnControllerPolledListener(this);
		sct.start();
	}

	@Override
	public void onControllerPolled(GamepadPosition position) {
		System.out.println("Received "+position.getX()+" "+position.getY());
		if(serial.isConnected()){
			try {
				serial.sendToArduino(position);
			} catch (SerialPortException e) {
				System.err.println("Data not sent to Arduino!");
				e.printStackTrace();
			}
		}
	}

}
