package net.androgames.level;

import java.text.DecimalFormat;
import net.androgames.level.config.Provider;
import net.androgames.level.orientation.Orientation;
import net.androgames.level.orientation.OrientationListener;
import net.androgames.level.orientation.OrientationProvider;
import net.androgames.level.view.LevelView;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.preference.PreferenceManager;
import android.text.util.Linkify;
import android.util.FloatMath;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
/*
 *  This file is part of Level (an Android Bubble Level).
 *  <https://github.com/avianey/Level>
 *  
 *  Copyright (C) 2012 Antoine Vianey
 *  
 *  Level is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Level is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Level. If not, see <http://www.gnu.org/licenses/>
 */
public class Level extends Activity implements OrientationListener {
	
	private static Level CONTEXT;
	
	private static final int DIALOG_CALIBRATE_ID = 1;
	private static final int TOAST_DURATION = 10000;
	
	private OrientationProvider provider;
	
    private LevelView view;
    private WakeLock wakeLock;
    
	/** Gestion du son */
	private SoundPool soundPool;
	private boolean soundEnabled;
	private int bipSoundID;
	private int bipRate;
	private long lastBip;
	
	private Button mClose;
	private CheckBox mShowDetail;
	private LinearLayout mLayout;
	/* DMT Calibration*/
	String str[] = { "Fastest", "Game  ", "UI     ", "Normal " };
	public  int fd = 0;
	int[] xyz = new int[3];
	DecimalFormat of = new DecimalFormat("  #000;-#000");
	DecimalFormat nf = new DecimalFormat("  #0.000;-#00.000  ");
    DecimalFormat tds = new DecimalFormat(" #,###,000");
	private Button cabiration1;
	private Button close_device;
	private Button delay;
	private TextView accelerometer_x;
	private TextView accelerometer_y;
	private TextView accelerometer_z;
	private TextView acc_offset_x;
	private TextView acc_offset_y;
	private TextView acc_offset_z;
	private TextView acc_sigma_x;
	private TextView acc_sigma_y;
	private TextView acc_sigma_z;
	//private TextView magnetic;
	private TextView magnetic_x;
	private TextView magnetic_y;
	private TextView magnetic_z;
	private TextView mag_sigma_x;
	private TextView mag_sigma_y;
	private TextView mag_sigma_z;
	//private TextView orientation;
	private TextView orientation_x;
	private TextView orientation_y;
	private TextView orientation_z;
	private TextView ori_sigma_x;
	private TextView ori_sigma_y;
	private TextView ori_sigma_z;
	private SensorManager mSensorManager01;
	int delay_mode=2;
	long TimeNewACC;
	long delayACC;
	static long TimeOldACC;
	  
	float[] sum_acc_XYZ = new float[3];
	float[] sumAccSquare= new float[3];
	float[] sigma_acc=new float[3];
	int countS_acc=0;
	float[] sum_mag_XYZ = new float[3];
	float[] sumMagSquare= new float[3];
	float[] sigma_mag=new float[3];
	int countS_mag=0;
	float[] sum_ori_XYZ = new float[3];
	float[] sumOriSquare= new float[3];
	float[] sigma_ori=new float[3];
	int countS_ori=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        CONTEXT = this;
        view = (LevelView) findViewById(R.id.level);
        // sound
    	soundPool = new SoundPool(1, AudioManager.STREAM_RING, 0);
    	bipSoundID = soundPool.load(this, R.raw.bip, 1);
    	bipRate = getResources().getInteger(R.integer.bip_rate);
    	
    	/* ��oSensorManager */
        mSensorManager01 =(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        accelerometer_x = (TextView) findViewById(R.id.accelerometer_x);
        accelerometer_y = (TextView) findViewById(R.id.accelerometer_y);
        accelerometer_z = (TextView) findViewById(R.id.accelerometer_z);
        acc_offset_x = (TextView) findViewById(R.id.acc_offset_x);
        acc_offset_y = (TextView) findViewById(R.id.acc_offset_y);
        acc_offset_z = (TextView) findViewById(R.id.acc_offset_z);
        acc_sigma_x = (TextView) findViewById(R.id.acc_sigma_x);
        acc_sigma_y = (TextView) findViewById(R.id.acc_sigma_y);
        acc_sigma_z = (TextView) findViewById(R.id.acc_sigma_z);
        accelerometer_x.setText("  X : " + "123455656" );
        accelerometer_y.setText("  Y : " + "123455656" );
        accelerometer_z.setText("  Z : " + "123455656" );
        cabiration1 =(Button) findViewById(R.id.cab_1);
        close_device = (Button) findViewById(R.id.close);
		delay = (Button) findViewById(R.id.delay);
        cabiration1.setOnClickListener(cab1_listener);
        close_device.setOnClickListener(close_listener);
		delay.setOnClickListener(delay_listener);
        mSensorManager01.registerListener 
        ( 
          mSensorListener, 
          mSensorManager01.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), bipRate, null
        );
        
        //mClose = (Button) findViewById(R.id.cab_cancel);
		mLayout = (LinearLayout) findViewById(R.id.detail_layout);
		mShowDetail = (CheckBox) findViewById(R.id.show_detail);
		//mClose.setOnClickListener(close_listener);
		if (mShowDetail.isChecked()) {
			mLayout.setVisibility(View.VISIBLE);
		} else {
			mLayout.setVisibility(View.GONE);
		}
		mShowDetail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							mLayout.setVisibility(View.VISIBLE);
						} else {
							mLayout.setVisibility(View.GONE);
						}

					}
				});
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return true;
	}
    
	private Button.OnClickListener cab1_listener= new Button.OnClickListener(){
		  public void onClick(View v)
      {	
			if(fd==0)
			{
				fd = Linuxc.open();
				if(fd>0)
					setTitle("open device success! ");
			}
	        if (fd < 0){
		        	setTitle("open device false!");
		        	//toast實現含圖片
				      //ImageView mView01 = new ImageView(Led_Control.this);
				      TextView mTextView = new TextView(Level.this);
				      LinearLayout lay = new LinearLayout(Level.this);   
				    //設定mTextView去抓取string值
				      mTextView.setText("open device false!");
				      Linkify.addLinks(mTextView,Linkify.WEB_URLS|Linkify.EMAIL_ADDRESSES|Linkify.PHONE_NUMBERS);  
				      Toast toast = Toast.makeText(Level.this, mTextView.getText(), Toast.LENGTH_LONG);        
				      View textView = toast.getView();
				      lay.setOrientation(LinearLayout.HORIZONTAL);
				      //mView01.setImageResource(R.drawable.s); // 在Toast裡加上圖片
				      //lay.addView(mView01);     // 在Toast裡顯示圖片
				      lay.addView(textView);    // 在Toast裡顯示文字
				      toast.setView(lay);
				      toast.show();
		             //打開設備文件失敗的話，就退出 
		        	finish(); 
		        }
		        else {
		        	
		        	//TextView mTextView = new TextView(DMT_GSENSORActivity.this);
				      LinearLayout lay = new LinearLayout(Level.this);   
				    //設定mTextView去抓取string值
				    //  mTextView.setText("open device success!");
				    //  Linkify.addLinks(mTextView,Linkify.WEB_URLS|Linkify.EMAIL_ADDRESSES|Linkify.PHONE_NUMBERS);  
				     // Toast toast = Toast.makeText(DMT_GSENSORActivity.this, mTextView.getText(), Toast.LENGTH_LONG);        
				     // View textView = toast.getView();
				      lay.setOrientation(LinearLayout.HORIZONTAL);
				      //mView01.setImageResource(R.drawable.s); // 在Toast裡加上圖片
				      //lay.addView(mView01);     // 在Toast裡顯示圖片
				     // lay.addView(textView);    // 在Toast裡顯示文字
				     // toast.setView(lay);
				     // toast.show();
		        }
		        xyz[0] = 1;
			  Linuxc.cab(xyz);
			  TextView mTextView = new TextView(Level.this);
		      LinearLayout lay = new LinearLayout(Level.this);   
		    //設定mTextView去抓取string值
		      //mTextView.setText("Offset value (x,y,z)= : " + of.format(xyz[0]) + " " + of.format(xyz[1]) + " " + of.format(xyz[2]));
		      mTextView.setText("Calibration Success");
		      acc_offset_x.setText("  X : " + of.format(xyz[0]) + "  ");
			  acc_offset_y.setText("  Y : " + of.format(xyz[1]) + "  ");
			  acc_offset_z.setText("  Z : " + of.format(xyz[2]) + "  ");
		      Linkify.addLinks(mTextView,Linkify.WEB_URLS|Linkify.EMAIL_ADDRESSES|Linkify.PHONE_NUMBERS);  
		      Toast toast = Toast.makeText(Level.this, mTextView.getText(), Toast.LENGTH_LONG);        
		      View textView = toast.getView();
		      lay.setOrientation(LinearLayout.HORIZONTAL);
		      lay.addView(textView);    
		      toast.setView(lay);
		      toast.show();
      }
	};
	private Button.OnClickListener close_listener = new Button.OnClickListener() {
		public void onClick(View v) {
			TextView mTextView = new TextView(Level.this);
			LinearLayout lay = new LinearLayout(Level.this);
			// 設定mTextView去抓取string值
			mTextView.setText("close device!");
			Linkify.addLinks(mTextView, Linkify.WEB_URLS
					| Linkify.EMAIL_ADDRESSES | Linkify.PHONE_NUMBERS);
			Toast toast = Toast.makeText(Level.this,
					mTextView.getText(), Toast.LENGTH_LONG);
			View textView = toast.getView();
			lay.setOrientation(LinearLayout.HORIZONTAL);
			// mView01.setImageResource(R.drawable.s); // 在Toast裡加上圖片
			// lay.addView(mView01); // 在Toast裡顯示圖片
			lay.addView(textView); // 在Toast裡顯示文字
			toast.setView(lay);
			toast.show();
			/* 關閉設備文件 */
			Linuxc.close();
			/* 退出運用程序 */
			finish();
		}
	};
	private Button.OnClickListener delay_listener = new Button.OnClickListener() {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			//delay_mode++;
			//delay_mode %= 4;

			mSensorManager01.registerListener(mSensorListener, mSensorManager01
					.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), delay_mode);
			delay.setText("Delay mode=" + str[delay_mode]);

		}

	};
	
	final SensorEventListener mSensorListener = new SensorEventListener()
    {
      private float[] mGravity = new float[3];
      
      @Override
      public void onAccuracyChanged(Sensor sensor, int accuracy)
      {
        // TODO Auto-generated method stub
      }

      @Override
      public void onSensorChanged(SensorEvent event)
      {
		// TODO Auto-generated method stub
          DecimalFormat tds = new DecimalFormat(" #,###,000");
       switch (event.sensor.getType())
        {
          case Sensor.TYPE_ACCELEROMETER:
            System.arraycopy(event.values, 0, mGravity, 0, 3);
            accelerometer_x.setText("  X : " + nf.format(mGravity[0]) );
            accelerometer_y.setText("  Y : " + nf.format(mGravity[1]) );
            accelerometer_z.setText("  Z : " + nf.format(mGravity[2]) );
            TimeNewACC = event.timestamp;
            delayACC = (long)((TimeNewACC - TimeOldACC)/1000000);//
            Log.d("@@@Sensor.TYPE_ACCELEROMETER@@@", delayACC + " ms");
            delay.setText("Delay mode="+str[delay_mode]+tds.format(delayACC)+" ms");
            TimeOldACC = TimeNewACC;
            
            sum_acc_XYZ[0] +=mGravity[0];
            sum_acc_XYZ[1] +=mGravity[1];
            sum_acc_XYZ[2] +=mGravity[2];
            sumAccSquare[0]+=mGravity[0]*mGravity[0];
            sumAccSquare[1]+=mGravity[1]*mGravity[1];
            sumAccSquare[2]+=mGravity[2]*mGravity[2];
            countS_acc++;
            if(countS_acc==100)
            {
            	sum_acc_XYZ[0]/=100;
            	sum_acc_XYZ[1]/=100;
            	sum_acc_XYZ[2]/=100;
             	sigma_acc[0]=FloatMath.sqrt(sumAccSquare[0]/100-sum_acc_XYZ[0]*sum_acc_XYZ[0]);
            	sigma_acc[1]=FloatMath.sqrt(sumAccSquare[1]/100-sum_acc_XYZ[1]*sum_acc_XYZ[1]);
            	sigma_acc[2]=FloatMath.sqrt(sumAccSquare[2]/100-sum_acc_XYZ[2]*sum_acc_XYZ[2]);
            	sum_acc_XYZ[0] =0;
            	sum_acc_XYZ[1] =0;
            	sum_acc_XYZ[2] =0;
                sumAccSquare[0]=0;
                sumAccSquare[1]=0;
                sumAccSquare[2]=0;
            	countS_acc=0;            	
            	
            	acc_sigma_x.setText("  X : " + nf.format(sigma_acc[0]) );
                acc_sigma_y.setText("  Y : " + nf.format(sigma_acc[1]) );
                acc_sigma_z.setText("  Z : " + nf.format(sigma_acc[2]) );
            	Log.v("sigma_acc(x,y,z)=",sigma_acc[0]+","+sigma_acc[1]+","+sigma_acc[2]);
            }
          break;
          case Sensor.TYPE_MAGNETIC_FIELD:
            break;
          default:
            return;
        }
      }
    };
    
    /* Handles item selections */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
	        case R.id.calibrate:
	            showDialog(DIALOG_CALIBRATE_ID);
	            return true;
	        case R.id.preferences:
	            startActivity(new Intent(this, LevelPreferences.class));
	            return true;
        }
        return false;
    }
    
    protected Dialog onCreateDialog(int id) {
        Dialog dialog;
        switch(id) {
	        case DIALOG_CALIBRATE_ID:
	        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        	builder.setTitle(R.string.calibrate_title)
	        			.setIcon(null)
	        			.setCancelable(true)
	        			.setPositiveButton(R.string.calibrate, new DialogInterface.OnClickListener() {
	        	           	public void onClick(DialogInterface dialog, int id) {
	        	        	   	Log.i("Level", " R.string.calibrate_title");
	        	    				fd = Linuxc.open();
	        	    				if(fd > 0){
	        	    					setTitle(R.string.calibrate_title_success);
	        	    					xyz[0] = 1;
	        	    					Linuxc.cab(xyz);
	        	    					Linuxc.close();
	        	    				}
	        	    				else{
	        	    		        	setTitle(R.string.calibrate_title_false);
	        	    		        	provider.saveCalibration();
	        	    		        }
	        	    		    //設定mTextView去抓取string值;
	        	    			  //Log.i("Level"," X: " + of.format(xyz[0])+" Y: " + of.format(xyz[1])+" Z: " + of.format(xyz[2]));
	        	           	}
	        			})
	        	       	.setNegativeButton(R.string.cancel, null)
	        	       	.setNeutralButton(R.string.reset, new DialogInterface.OnClickListener() {
	        	           	public void onClick(DialogInterface dialog, int id) {
	        	           		fd = Linuxc.open();
	        	           		if(fd > 0){
	        	           			xyz[0] = 0;
	        	           			xyz[1] = 0;
	        	           			xyz[2] = 0;
	        	           			Linuxc.setoffset(xyz);
	        	           			//Log.i("Level"," X: " + of.format(xyz[0])+" Y: " + of.format(xyz[1])+" Z: " + of.format(xyz[2]));  
	        	           			//Log.i("Level", " R.string.cancel");
	        	           			Linuxc.close();
	        	           		}
	        	           		else{
	        	           			provider.resetCalibration();
	        	           		}
	        	           	}
	        	       	})
	        	       	.setMessage(R.string.calibrate_message);
	        	dialog = builder.create();
	            break;
	        default:
	            dialog = null;
        }
        return dialog;
    }
    
    protected void onResume() {
    	super.onResume();
    	Log.d("Level", "Level resumed");
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    	provider = Provider.valueOf(
    			prefs.getString(LevelPreferences.KEY_SENSOR, 
    					LevelPreferences.PROVIDER_ACCELEROMETER)).getProvider();
    	// chargement des effets sonores
        soundEnabled = prefs.getBoolean(LevelPreferences.KEY_SOUND, false);
        // orientation manager
        if (provider.isSupported()) {
    		provider.startListening(this);
    	} else {
    		Toast.makeText(this, getText(R.string.not_supported), TOAST_DURATION).show();
    	}
        // wake lock
        wakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE)).newWakeLock(
        		PowerManager.SCREEN_BRIGHT_WAKE_LOCK, this.getClass().getName());
        wakeLock.acquire();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (provider.isListening()) {
        	provider.stopListening();
    	}
		wakeLock.release();
    }
    
    @Override
    public void onDestroy() {
		if (soundPool != null) {
			soundPool.release();
		}
		super.onDestroy();
    }

	@Override
	public void onOrientationChanged(Orientation orientation, float pitch, float roll) {
		if (soundEnabled 
				&& orientation.isLevel(pitch, roll, provider.getSensibility())
				&& System.currentTimeMillis() - lastBip > bipRate) {
			AudioManager mgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
			float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_RING);
			float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_RING);
			float volume = streamVolumeCurrent / streamVolumeMax;
			lastBip = System.currentTimeMillis();
			soundPool.play(bipSoundID, volume, volume, 1, 0, 1);
		}
		view.onOrientationChanged(orientation, pitch, roll);
	}

	@Override
	public void onCalibrationReset(boolean success) {
		Toast.makeText(this, success ? 
				R.string.calibrate_restored : R.string.calibrate_failed, 
				Level.TOAST_DURATION).show();
	}

	@Override
	public void onCalibrationSaved(boolean success) {
		Toast.makeText(this, success ? 
				R.string.calibrate_saved : R.string.calibrate_failed,
				Level.TOAST_DURATION).show();
	}

    public static Level getContext() {
		return CONTEXT;
	}
    
    public static OrientationProvider getProvider() {
    	return getContext().provider;
    }
    
}
