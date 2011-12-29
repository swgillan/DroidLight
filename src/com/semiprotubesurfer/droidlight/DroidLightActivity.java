package com.semiprotubesurfer.droidlight;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.semiprotubesurfer.droidlight.R;

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
        
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Open the default i.e. the first rear facing camera.
        mCamera = Camera.open();
        mCamera.startPreview();
        mCameraParameters = mCamera.getParameters();
        mCameraParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        mCamera.setParameters(mCameraParameters);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Because the Camera object is a shared resource, it's very
        // important to release it when the activity is paused.
        if (mCamera != null) {
        	mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }
}