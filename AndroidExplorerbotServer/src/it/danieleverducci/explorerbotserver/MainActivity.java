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

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity implements OnControllerPolledListener {
	private SerialCommunication serial;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//Initialize serial communication
		serial = new SerialCommunication(this);

		//Start network communication server
		ServerNetworkCommunicationThread sct = new ServerNetworkCommunicationThread();
		sct.setOnControllerPolledListener(this);
		sct.start();

	}

	@Override
	public void onControllerPolled(GamepadPosition position) {
		if(serial.isConnected()){
			try {
				serial.sendToArduino(position);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onDestroy() {
		if(serial!=null){
			try {
				serial.closeConnection();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		super.onDestroy();
	}

}
