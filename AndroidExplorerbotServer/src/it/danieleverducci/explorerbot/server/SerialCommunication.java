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

package it.danieleverducci.explorerbot.server;

import it.danieleverducci.explorerbot.AndroidAppConfiguration;
import it.danieleverducci.explorerbot.objects.GamepadPosition;

import java.io.IOException;

import android.content.Context;
import android.hardware.usb.UsbManager;
import android.util.Log;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialProber;


public class SerialCommunication {
	private boolean connected=false;
	private UsbSerialDriver driver;

	public SerialCommunication(Context context) {
		if(AndroidAppConfiguration.LOG_ENABLED)  Log.d(this.getClass().toString(),"Searcing for Arduino: ");

		//Find first port
		UsbManager manager = (UsbManager)context.getSystemService(Context.USB_SERVICE);
		driver = UsbSerialProber.acquire(manager);

		if(driver!=null){
			if(AndroidAppConfiguration.LOG_ENABLED)  Log.d(this.getClass().toString(),"Found arduino on port"+driver.getDevice().getDeviceName());
			connected=true;
			try {
				driver.setBaudRate(9600);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			if(AndroidAppConfiguration.LOG_ENABLED)  Log.d(this.getClass().toString(),"Arduino not found!");
			connected=false;
		}
	}

	/**
	 * Sends the provided @param gamepad position to Arduino via serial communication
	 * @throws SerialPortException
	 */
	public void sendToArduino(GamepadPosition pos) throws IOException {
		if(!connected) throw new IllegalStateException("Connection not extabilished!");
		//Converts the two floats ranging -1.0f -> 1.0f to two bytes ranging 0 -> 127
		byte[] data = new byte[2];
		data[0] = (byte)((pos.getX()+1)*63);//x
		data[1] = (byte)((pos.getY()+1)*63);//y
		driver.write(data, 80);
		if(AndroidAppConfiguration.LOG_ENABLED)  Log.e("DANY", "Written "+pos.getX()+" "+pos.getY());
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
	 * @throws IOException
	 */
	public void closeConnection() throws IOException{
		if(driver!=null){
			driver.close();
			driver=null;
		}
		connected=false;
	}

}
