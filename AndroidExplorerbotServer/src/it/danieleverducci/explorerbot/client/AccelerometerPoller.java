package it.danieleverducci.explorerbot.client;

import it.danieleverducci.explorerbot.interfaces.OnControllerPolledListener;
import it.danieleverducci.explorerbot.objects.GamepadPosition;
import android.app.Service;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class AccelerometerPoller implements SensorEventListener {
	private Context context;
	private SensorManager sensorManager;
	private Sensor accelerometer;
	private OnControllerPolledListener onControllerPolledListener;
	private GamepadPosition pos;

	public AccelerometerPoller(Context context) {
		this.context = context;
		this.pos = new GamepadPosition();

		sensorManager = (SensorManager)this.context.getSystemService(Service.SENSOR_SERVICE);
		accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//		sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		pos.setX(normalize(event.values[1]));
		pos.setY(normalize(event.values[0]));
		onControllerPolledListener.onControllerPolled(pos);
	}

	/**
	 * Takes an accelerometer @param f (force) in m/s2 and converts to a float between 1 and -1
	 * @return
	 */
	private float normalize(float f) {
		//The acceleration on earth is 9,8m/s, but we want the two edges to be easily reachable, so divide for 9,0
		f = f/9f;
		if(f<=1.0f && f>=-1.0f) return f;
		else if(f>1.0f) return 1.0f;
		else return -1.0f;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {}

	public void onResume() {
		sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
	}

	public void onPause() {
		sensorManager.unregisterListener(this);
	}

	public void setOnControllerPolledListener(OnControllerPolledListener onControllerPolledListener) {
		this.onControllerPolledListener = onControllerPolledListener;
	}
}
