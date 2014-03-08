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

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;
import it.danieleverducci.explorerbot.objects.GamepadPosition;

public class SerialCommunication {
	private boolean connected=false;
	private SerialPort port;

	public SerialCommunication() {
		System.out.print("\nSearcing for Arduino: ");
		String[] portNames = SerialPortList.getPortNames();
		for(int i = 0; i < portNames.length; i++){
			System.out.print(portNames[i]+"... ");
			try {
				if(tryConnecting(portNames[i])){
					port.setParams(SerialPort.BAUDRATE_9600, 
                            SerialPort.DATABITS_8,
                            SerialPort.STOPBITS_1,
                            SerialPort.PARITY_NONE);
					connected=true;
					System.out.println("Arduino found on "+portNames[i]);
					break;
				}
			} catch (SerialPortException e) {}
		}

		if(!connected){
			System.err.println("ERROR: Arduino not found. Exiting.");
			System.exit(1);
		}
	}

	/**
	 * Tries to connect to the specified port.
	 * @param string The name of the port
	 * @throws SerialPortException 
	 */
	private boolean tryConnecting(String portName) throws SerialPortException {
		port = new SerialPort(portName);
		return port.openPort();
	}

	/**
	 * Sends the provided @param gamepad position to Arduino via serial communication
	 * @throws SerialPortException
	 */
	public void sendToArduino(GamepadPosition pos) throws SerialPortException{
		if(!connected) throw new IllegalStateException("Connection not extabilished!");
		//Converts the two floats ranging -1.0f -> 1.0f to two bytes ranging 0 -> 127
		byte[] data = new byte[2];
		data[0] = (byte)((pos.getX()+1)*63);//x
		data[1] = (byte)((pos.getY()+1)*63);//y
		port.writeBytes(data);
	}
	
	/**
	 * @return true if an Arduino was found
	 */
	public boolean isConnected(){
		return connected;
	}
	
	/**
	 * Terminates the connection to the Arduino
	 * @return true if succeed
	 * @throws SerialPortException
	 */
	public boolean closeConnection() throws SerialPortException{
		return port.closePort();
	}
}
