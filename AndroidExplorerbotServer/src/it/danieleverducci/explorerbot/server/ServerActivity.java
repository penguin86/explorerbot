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
import it.danieleverducci.explorerbot.R;
import it.danieleverducci.explorerbot.interfaces.OnControllerPolledListener;
import it.danieleverducci.explorerbot.objects.GamepadPosition;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ServerActivity extends Activity implements OnControllerPolledListener, OnClickListener {
	private SerialCommunication serial;
	private ServerNetworkCommunicationThread sct;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_server);
		((TextView)findViewById(R.id.port)).setText(AndroidAppConfiguration.PORT+"");

		//Initialize serial communication
		serial = new SerialCommunication(this);

		//Start network communication server
		sct = new ServerNetworkCommunicationThread();
		sct.setOnControllerPolledListener(this);
		sct.start();

		//Register interface buttons listener
		findViewById(R.id.killserverbutton).setOnClickListener(this);
		findViewById(R.id.startvideo).setOnClickListener(this);
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
		//Terminate network thread
		if(sct!=null && sct.isAlive()){
			sct.setMustExit(true);
		}
		//Terminate serial connection to Arduino
		if(serial!=null){
			try {
				serial.closeConnection();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.killserverbutton:
			finish();	//Finishing the activity, onDestroy will be called
			break;
		case R.id.startvideo:
			launchStreamingApp();
			break;
		}
	}

	/**
	 * If the app is installed, start it. Otherwise, open the Android Market for download
	 */
	private void launchStreamingApp() {
		Intent launchIntent = getPackageManager().getLaunchIntentForPackage(AndroidAppConfiguration.DEFAULT_IPCAMERA_APP_PACKAGENAME);
		if(launchIntent==null) launchIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + AndroidAppConfiguration.DEFAULT_IPCAMERA_APP_PACKAGENAME));
		startActivity( launchIntent );
	}

}
