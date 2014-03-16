package it.danieleverducci.explorerbot.client;



import it.danieleverducci.explorerbot.R;
import it.danieleverducci.explorerbot.interfaces.OnControllerPolledListener;
import it.danieleverducci.explorerbot.objects.GamepadPosition;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

public class ClientActivity extends Activity implements OnControllerPolledListener, OnCheckedChangeListener, OnClickListener {
	public static final String IP_ADDRESS_INTENT_EXTRA = "ipAddress";
	private AccelerometerPoller accelPoller;
	private ClientNetworkCommunicationThread net;
	private ToggleButton drive;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Draw view and set listeners
		setContentView(R.layout.activity_client);
		drive = (ToggleButton)findViewById(R.id.drive);
		drive.setOnCheckedChangeListener(this);
		findViewById(R.id.exit).setOnClickListener(this);
		
		//Retrieve user-provided IP address
		String ipAddress = getIntent().getStringExtra(IP_ADDRESS_INTENT_EXTRA);

		//Initialize accelerometer poller
		accelPoller = new AccelerometerPoller(this);
		accelPoller.setOnControllerPolledListener(this);

		//Initialize network communication thread
		net = new ClientNetworkCommunicationThread(ipAddress);
		net.start();
	}

	@Override
	protected void onResume() {
		super.onResume();
		ToggleButton transmitting = (ToggleButton)findViewById(R.id.drive);
		if(accelPoller!=null && transmitting.isChecked()) accelPoller.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if(accelPoller!=null) accelPoller.onPause();
	}

	@Override
	public void onControllerPolled(GamepadPosition position) {
		// Notify network thread
		net.setLastKnownPosition(position);
	}

	@Override
	protected void onDestroy() {
		if(net!=null) net.setMustExit(true);
		super.onDestroy();
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		 
			if(isChecked) {
				if(accelPoller!=null) accelPoller.onResume();
				drive.setBackgroundColor(Color.parseColor("#FFFF0000"));
			}
			else {
				if(accelPoller!=null) accelPoller.onPause();
				drive.setBackgroundColor(Color.parseColor("#FFC0C0C0"));
			}
		
	}

	@Override
	public void onClick(View v) {
		accelPoller.onPause();
		net.setMustExit(true);
		finish();
	}



}
