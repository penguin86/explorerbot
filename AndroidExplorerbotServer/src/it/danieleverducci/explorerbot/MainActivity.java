package it.danieleverducci.explorerbot;

import it.danieleverducci.explorerbot.client.ClientActivity;
import it.danieleverducci.explorerbot.server.ServerActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		findViewById(R.id.startClient).setOnClickListener(this);
		findViewById(R.id.startServer).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent i;
		switch(v.getId()){
		case R.id.startClient:
			String ipAddress = ((EditText)findViewById(R.id.ipAddressEditText)).getText().toString();
			i = new Intent(this, ClientActivity.class);
			i.putExtra(ClientActivity.IP_ADDRESS_INTENT_EXTRA, ipAddress);
			startActivity(i);
			break;
		case R.id.startServer:
			if(android.os.Build.VERSION.SDK_INT>=12) {
			i = new Intent(this, ServerActivity.class);
			startActivity(i);
			} else Toast.makeText(this, R.string.api_requirement_not_satisfied, Toast.LENGTH_SHORT).show();
			break;
		}
	}

}
