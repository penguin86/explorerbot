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
import net.java.games.input.Controller;

public class ControllerPollThread extends Thread {
	private Controller controller;
	private OnControllerPolledListener listener;
	private boolean mustStop=false;

	@Override
	public void run() {
		if(controller!=null) {
			while(!mustStop){
				//Poll controller and report to listener
				listener.onControllerPolled(Gamepad.pollController(controller));
				//Wait 100ms between readings
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		else throw new IllegalStateException("Gamepad not set in thread. Call setController before starting thread.");
	}

	/**
	 * Sets the @param controller to be polled
	 */
	public void setController(Controller controller) {
		this.controller = controller;
	}

	/**
	 * Register the @param listener to be notified about controller events
	 */
	public void setOnControllerPolledListener(OnControllerPolledListener listener) {
		this.listener = listener;
	}

	/**
	 * Set @param mustStop to true to make the thread shutdown
	 */
	public void setMustStop(boolean mustStop) {
		this.mustStop = mustStop;
	}

}
