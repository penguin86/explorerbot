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

import it.danieleverducci.explorerbot.objects.GamepadPosition;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

public class Gamepad {
	/**
	 * Retrieves the provided @param controller position and @return it as a GamepadPosition object
	 */
	public static GamepadPosition pollController(Controller controller) {
		GamepadPosition pos = new GamepadPosition();
		controller.poll();
		Component[] components = controller.getComponents();

		for(int i=0;i<components.length;i++) {
			if(components[i].isAnalog()){
				if(components[i].getName().equalsIgnoreCase("x")) pos.setX(components[i].getPollData());
				if(components[i].getName().equalsIgnoreCase("y")) pos.setY(components[i].getPollData());
			}
		}
		return pos;
	}

	/**
	 * Iterates the controller searching for an analog joystick and returns the first found. 
	 * Returns null if not found.
	 * @return
	 */
	public static Controller findFirstGamepad() {
		//Retrieve all controllers and use first analog joysitck found
		Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
		for(int i =0;i<controllers.length;i++){
			if(controllers[i].getType()==Controller.Type.GAMEPAD){
				System.out.println("Selected controller "+controllers[i].getName());
				return controllers[i];
			}
		}
		return null;
	}
}
