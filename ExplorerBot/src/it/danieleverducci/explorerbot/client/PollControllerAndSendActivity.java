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

import it.danieleverducci.explorerbot.interfaces.OnControllerPolledListener;
import it.danieleverducci.explorerbot.objects.GamepadPosition;
import net.java.games.input.Controller;

public class PollControllerAndSendActivity implements OnControllerPolledListener {
	private ClientNetworkCommunicationThread net;

	public PollControllerAndSendActivity(String ipAddress) {
		//Find gamepad
		Controller controller = Gamepad.findFirstGamepad();
		if(controller==null) {
			System.err.println("No gamepad controller found. Exiting.");
			System.exit(1);
		}
		
		//Initialize network communication thread
		net = new ClientNetworkCommunicationThread(ipAddress);
		net.start();
		
		//Initialize gamepad poll thread
		ControllerPollThread poll = new ControllerPollThread();
		poll.setController(controller);
		poll.setOnControllerPolledListener(this);
		poll.start();
	}

	@Override
	public void onControllerPolled(GamepadPosition position) {
		// Notify network thread
		net.setLastKnownPosition(position);
	}

}
