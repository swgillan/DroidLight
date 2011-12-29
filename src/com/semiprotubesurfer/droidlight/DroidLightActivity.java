package com.semiprotubesurfer.droidlight;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class DroidLightActivity extends Activity {
	private TextView mStatusText;
	private ImageView mLight;
	Camera mCamera;
	private Camera.Parameters mCameraParameters;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mStatusText = (TextView)findViewById(R.id.statusText);
        mLight = (ImageView)findViewById(R.id.light);
        
        mLight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if ( v.getTag().equals(getResources().getString(R.string.off_tag)) ) {
					v.setTag(getResources().getString(R.string.on_tag));
					mLight.setImageResource(R.drawable.lightbulb_on);
					mStatusText.setText(R.string.off_text);
					mCameraParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
					mCamera.setParameters(mCameraParameters);
				} else {
					v.setTag(getResources().getString(R.string.off_tag));
					mLight.setImageResource(R.drawable.lightbulb_off);
					mStatusText.setText(R.string.on_text);
					mCameraParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
					mCamera.setParameters(mCameraParameters);
				}
			}
		});
        /*
         * Check the configuration state. This is done if the configuration
         * of the device was changed (ie orientation change). In this event
         * restart the application where it was left, if the light was on,
         * turn it back on. The light asset and status text object will also need
         * to be put to the proper state as well.
         */
        final Object mTag = (Object) getLastNonConfigurationInstance();
        if (mTag != null) {
        	mCamera = Camera.open();
        	mCamera.startPreview();
        	mCameraParameters = mCamera.getParameters();
        	mLight.setTag(mTag);
        	if (mTag.equals(getResources().getString(R.string.off_tag))) {
        		mLight.setImageResource(R.drawable.lightbulb_off);
				mStatusText.setText(R.string.on_text);
				mCameraParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        	} else {
        		mLight.setImageResource(R.drawable.lightbulb_on);
				mStatusText.setText(R.string.off_text);
				mCameraParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        	}
        	mCamera.setParameters(mCameraParameters);
        }
        
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        /*
         *  Open the default i.e. the first rear facing camera.
         *  Do this if the application is starting up or coming back
         *  from the background.
         */
        if (mCamera == null) {
        	mCamera = Camera.open();
        	mCamera.startPreview();
        	mCameraParameters = mCamera.getParameters();
        	mCameraParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        	mCamera.setParameters(mCameraParameters);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        /* 
         * Because the Camera object is a shared resource, it's very
         * important to release it when the activity is paused.
         * 
         * When the application becomes paused, set the camera to the off
         * state. If a configuration change is being done, it will be
         * reset to the correct state.
         */
         
        if (mCamera != null) {
        	mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
            mLight.setImageResource(R.drawable.lightbulb_off);
			mStatusText.setText(R.string.on_text);
        }
    }
    
    @Override
    public Object onRetainNonConfigurationInstance() {
    	/*
    	 * When a configuration change is detected, to 
    	 * restart in the correct state, save the tag
    	 * that the light asset is set to. This will
    	 * identify of the light was on or off at the time.
    	 */
		return mLight.getTag();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
    	case R.id.preferences:
    		startActivity(new Intent(getApplicationContext(), Preferences.class));
    		return true;
    	default:
            return super.onOptionsItemSelected(item);
    	}
    }
}