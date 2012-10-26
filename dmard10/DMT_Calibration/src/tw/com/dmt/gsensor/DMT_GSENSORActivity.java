package tw.com.dmt.gsensor;

import java.text.DecimalFormat;
import android.widget.CheckBox;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.FloatMath;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DMT_GSENSORActivity extends Activity {
    /** Called when the activity is first created. */
	private Button get_offset;
    private Button close_device;
    private Button cabiration1;
    private Button delay;
    private CheckBox CheckBox;
	public  int fd = 0;
	int[] xyz = new int[3];
	public static boolean bIfDebug = false;
	public static String TAG = "Sensors";
	int count=0;  
	  private TextView myTextView01;
	  //private TextView accelerometer;
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
	  DecimalFormat nf = new DecimalFormat("  #0.000;-#00.000  ");
	  DecimalFormat of = new DecimalFormat("  #000;-#000");
      DecimalFormat tds = new DecimalFormat(" #,###,000");
	  int delay_mode=0;
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
	  //float[] sqrtXYZ = new float[3];
	  	
	 String str[]={"Fastest","Game  ","UI     ","Normal "};
	private Button.OnClickListener cab1_listener= new Button.OnClickListener(){
		  public void onClick(View v)
        {	
			if(fd==0)
			{
				count+=1;
				fd = Linuxc.open();
				if(fd>0)
					setTitle("open device success! "+count);
			}
	        if (fd < 0){
		        	setTitle("open device false!");
		        	//toast實現含圖片
				      //ImageView mView01 = new ImageView(Led_Control.this);
				      TextView mTextView = new TextView(DMT_GSENSORActivity.this);
				      LinearLayout lay = new LinearLayout(DMT_GSENSORActivity.this);   
				    //設定mTextView去抓取string值
				      mTextView.setText("open device false!");
				      Linkify.addLinks(mTextView,Linkify.WEB_URLS|Linkify.EMAIL_ADDRESSES|Linkify.PHONE_NUMBERS);  
				      Toast toast = Toast.makeText(DMT_GSENSORActivity.this, mTextView.getText(), Toast.LENGTH_LONG);        
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
				      LinearLayout lay = new LinearLayout(DMT_GSENSORActivity.this);   
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
			  TextView mTextView = new TextView(DMT_GSENSORActivity.this);
		      LinearLayout lay = new LinearLayout(DMT_GSENSORActivity.this);   
		    //設定mTextView去抓取string值
		      mTextView.setText("Offset value (x,y,z)= : " + of.format(xyz[0]) + " " + of.format(xyz[1]) + " " + of.format(xyz[2]));
		      acc_offset_x.setText("  X : " + of.format(xyz[0]) + "  ");
			  acc_offset_y.setText("  Y : " + of.format(xyz[1]) + "  ");
			  acc_offset_z.setText("  Z : " + of.format(xyz[2]) + "  ");
		      Linkify.addLinks(mTextView,Linkify.WEB_URLS|Linkify.EMAIL_ADDRESSES|Linkify.PHONE_NUMBERS);  
		      Toast toast = Toast.makeText(DMT_GSENSORActivity.this, mTextView.getText(), Toast.LENGTH_LONG);        
		      View textView = toast.getView();
		      lay.setOrientation(LinearLayout.HORIZONTAL);
		      lay.addView(textView);    
		      toast.setView(lay);
		      toast.show();
        }
	};
	
	private Button.OnClickListener get_offset_listener= new Button.OnClickListener(){
		  public void onClick(View v)
      {	
			if(fd==0)
			{
				count+=1;
				fd = Linuxc.open();
				if(fd>0)
					setTitle("open device success! "+count);
			}
	        if (fd < 0){
		        	setTitle("open device false!");
		        	//toast實現含圖片
				      //ImageView mView01 = new ImageView(Led_Control.this);
				      TextView mTextView = new TextView(DMT_GSENSORActivity.this);
				      LinearLayout lay = new LinearLayout(DMT_GSENSORActivity.this);   
				    //設定mTextView去抓取string值
				      mTextView.setText("open device false!");
				      Linkify.addLinks(mTextView,Linkify.WEB_URLS|Linkify.EMAIL_ADDRESSES|Linkify.PHONE_NUMBERS);  
				      Toast toast = Toast.makeText(DMT_GSENSORActivity.this, mTextView.getText(), Toast.LENGTH_LONG);        
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
				      LinearLayout lay = new LinearLayout(DMT_GSENSORActivity.this);   
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
			  Linuxc.getoffset(xyz);
			  acc_offset_x.setText("  X : " + of.format(xyz[0]) + "  ");
			  acc_offset_y.setText("  Y : " + of.format(xyz[1]) + "  ");
			  acc_offset_z.setText("  Z : " + of.format(xyz[2]) + "  ");
	      }
	};
	
	private Button.OnClickListener close_listener= new Button.OnClickListener(){
		 public void onClick(View v)
         {
			 TextView mTextView = new TextView(DMT_GSENSORActivity.this);
		      LinearLayout lay = new LinearLayout(DMT_GSENSORActivity.this);   
		    //設定mTextView去抓取string值
		      mTextView.setText("close device!");
		      Linkify.addLinks(mTextView,Linkify.WEB_URLS|Linkify.EMAIL_ADDRESSES|Linkify.PHONE_NUMBERS);  
		      Toast toast = Toast.makeText(DMT_GSENSORActivity.this, mTextView.getText(), Toast.LENGTH_LONG);        
		      View textView = toast.getView();
		      lay.setOrientation(LinearLayout.HORIZONTAL);
		      //mView01.setImageResource(R.drawable.s); // 在Toast裡加上圖片
		      //lay.addView(mView01);     // 在Toast裡顯示圖片
		      lay.addView(textView);    // 在Toast裡顯示文字
		      toast.setView(lay);
		      toast.show();
			 /* 關閉設備文件 */
       	   Linuxc.close();
             /* 退出運用程序 */
       	   finish();
           }
	}; 
	
	private Button.OnClickListener delay_listener= new Button.OnClickListener()
	{

		public void onClick(View v)
     {
	       // TODO Auto-generated method stub
		delay_mode++;
		delay_mode%=4;
	      
		        mSensorManager01.registerListener 
		        ( 
		          mSensorListener, 
		          mSensorManager01.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
		          delay_mode
		        );
		        delay.setText("Delay mode="+str[delay_mode]);
     }	   
		    
        
	};
	final SensorEventListener mSensorListener = new SensorEventListener()
    {
      private float[] mGravity = new float[3];
      private float[] mGeomagnetic = new float[3];
      private float[] mOrientation = new float[3];
      private float[] mRotationM = new float[9];
      private float[] mRemapedRotationM = new float[9];
      //private boolean mFailed;
      //private float mAzimuth;
	private long TimeNewACC;
      
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
            //Log.d("@@@Sensor.TYPE_ACCELEROMETER@@@", delayACC + " ms");
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
             	sigma_acc[0]=FloatMath.sqrt(sumAccSquare[0]/100-sum_acc_XYZ[0]*sum_acc_XYZ[0]);//
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
            	//Log.v("sigma_acc(x,y,z)=",sigma_acc[0]+","+sigma_acc[1]+","+sigma_acc[2]);
            }
          break;
          case Sensor.TYPE_MAGNETIC_FIELD:
            System.arraycopy(event.values, 0, mGeomagnetic, 0, 3);
            magnetic_x.setText("  X : " + nf.format(mGeomagnetic[0]) );
            magnetic_y.setText("  Y : " + nf.format(mGeomagnetic[1]) );
            magnetic_z.setText("  Z : " + nf.format(mGeomagnetic[2]) );
            
            sum_mag_XYZ[0] +=mGeomagnetic[0];
            sum_mag_XYZ[1] +=mGeomagnetic[1];
            sum_mag_XYZ[2] +=mGeomagnetic[2];
            sumMagSquare[0]+=mGeomagnetic[0]*mGeomagnetic[0];
            sumMagSquare[1]+=mGeomagnetic[1]*mGeomagnetic[1];
            sumMagSquare[2]+=mGeomagnetic[2]*mGeomagnetic[2];
            countS_mag++;
            if(countS_mag==100)
            {
            	sum_mag_XYZ[0]/=100;
            	sum_mag_XYZ[1]/=100;
            	sum_mag_XYZ[2]/=100;
             	sigma_mag[0]=FloatMath.sqrt(sumMagSquare[0]/100-sum_mag_XYZ[0]*sum_mag_XYZ[0]);//
            	sigma_mag[1]=FloatMath.sqrt(sumMagSquare[1]/100-sum_mag_XYZ[1]*sum_mag_XYZ[1]);
            	sigma_mag[2]=FloatMath.sqrt(sumMagSquare[2]/100-sum_mag_XYZ[2]*sum_mag_XYZ[2]);
            	sum_mag_XYZ[0] =0;
            	sum_mag_XYZ[1] =0;
            	sum_mag_XYZ[2] =0;
                sumMagSquare[0]=0;
                sumMagSquare[1]=0;
                sumMagSquare[2]=0;
            	countS_mag=0;            	
            	
            	mag_sigma_x.setText("  X : " + nf.format(sigma_mag[0]) );
                mag_sigma_y.setText("  Y : " + nf.format(sigma_mag[1]) );
                mag_sigma_z.setText("  Z : " + nf.format(sigma_mag[2]) );
            	//Log.v("sigma_mag(x,y,z)=",sigma_mag[0]+","+sigma_mag[1]+","+sigma_mag[2]);
            }
            break;
          case Sensor.TYPE_ORIENTATION:
            System.arraycopy(event.values, 0, mOrientation, 0, 3);
            orientation_x.setText("X : " + nf.format(mOrientation[0]) );
            orientation_y.setText("Y : " + nf.format(mOrientation[1]) );
            orientation_z.setText("Z : " + nf.format(mOrientation[2]) );
            
            sum_ori_XYZ[0] +=mOrientation[0];
            sum_ori_XYZ[1] +=mOrientation[1];
            sum_ori_XYZ[2] +=mOrientation[2];
            sumOriSquare[0]+=mOrientation[0]*mOrientation[0];
            sumOriSquare[1]+=mOrientation[1]*mOrientation[1];
            sumOriSquare[2]+=mOrientation[2]*mOrientation[2];
            countS_ori++;
            if(countS_ori==100)
            {
            	sum_ori_XYZ[0]/=100;
            	sum_ori_XYZ[1]/=100;
            	sum_ori_XYZ[2]/=100;
             	sigma_ori[0]=FloatMath.sqrt(sumOriSquare[0]/100-sum_ori_XYZ[0]*sum_ori_XYZ[0]);//
            	sigma_ori[1]=FloatMath.sqrt(sumOriSquare[1]/100-sum_ori_XYZ[1]*sum_ori_XYZ[1]);
            	sigma_ori[2]=FloatMath.sqrt(sumOriSquare[2]/100-sum_ori_XYZ[2]*sum_ori_XYZ[2]);
            	sum_ori_XYZ[0] =0;
            	sum_ori_XYZ[1] =0;
            	sum_ori_XYZ[2] =0;
                sumOriSquare[0]=0;
                sumOriSquare[1]=0;
                sumOriSquare[2]=0;
            	countS_ori=0;            	
            	
            	ori_sigma_x.setText("  X : " + nf.format(sigma_ori[0]) );
                ori_sigma_y.setText("  Y : " + nf.format(sigma_ori[1]) );
                ori_sigma_z.setText("  Z : " + nf.format(sigma_ori[2]) );
            	//Log.v("sigma_ori(x,y,z)=",sigma_ori[0]+","+sigma_ori[1]+","+sigma_ori[2]);
            }
            break;
          default:
            return;
        }
        
        if(SensorManager.getRotationMatrix(mRotationM, null, mGravity, mGeomagnetic))
        {
          SensorManager.remapCoordinateSystem(mRotationM, SensorManager.AXIS_X, SensorManager.AXIS_Z, mRemapedRotationM);
          SensorManager.getOrientation(mRemapedRotationM, mOrientation);
          //onSuccess();
        }
        else
        {
          //onFailure();
        }
      }
      /*
      void onSuccess()
      {
        if (mFailed)
        {
          mFailed = false;
        }     
        mTextView01.setText("Open Device Success");
      }

      void onFailure()
      {
        if (!mFailed)
        {
          mFailed = true;
          mTextView01.setText("Failed to retrive rotation Matrix");
        }
      }
      */
    };
    public void exitActivity(int exitMethod)
    {
      //throw new RuntimeException("Exit!");
      try
      {
        switch(exitMethod)
        {
          case 0:
              System.exit(0);
            break;
          case 1:
            android.os.Process.killProcess(android.os.Process.myPid());
            break;
          case 2:
            finish();
            break;
        }
      }
      catch(Exception e)
      {
        finish();
      }
    }
    
    @Override
    protected void onResume()
    {
      // TODO Auto-generated method stub
    	mSensorManager01.registerListener 
        ( 
        	mSensorListener, 
        	mSensorManager01.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
        	delay_mode
        );
        delay.setText("Delay mode="+str[delay_mode]);
	    mSensorManager01.registerListener 
	    ( 
	        mSensorListener, 
	        mSensorManager01.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), 
	        SensorManager.SENSOR_DELAY_FASTEST
	    );
	   
	    mSensorManager01.registerListener 
	    ( 
	        mSensorListener, 
	        mSensorManager01.getDefaultSensor(Sensor.TYPE_ORIENTATION), 
	        SensorManager.SENSOR_DELAY_FASTEST
	    );
	    super.onResume();
    }

    @Override
    protected void onPause()
    {
      // TODO Auto-generated method stub
      /* ����USensorEventListener */
      mSensorManager01.unregisterListener(mSensorListener);
      exitActivity(1);
      super.onPause();
    }
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        myTextView01 =(TextView) findViewById(R.id.horizontal);
        //accelerometer = (TextView) findViewById(R.id.accelerometer);
        accelerometer_x = (TextView) findViewById(R.id.accelerometer_x);
        accelerometer_y = (TextView) findViewById(R.id.accelerometer_y);
        accelerometer_z = (TextView) findViewById(R.id.accelerometer_z);
        acc_offset_x = (TextView) findViewById(R.id.acc_offset_x);
        acc_offset_y = (TextView) findViewById(R.id.acc_offset_y);
        acc_offset_z = (TextView) findViewById(R.id.acc_offset_z);
        acc_sigma_x = (TextView) findViewById(R.id.acc_sigma_x);
        acc_sigma_y = (TextView) findViewById(R.id.acc_sigma_y);
        acc_sigma_z = (TextView) findViewById(R.id.acc_sigma_z);
        //magnetic = (TextView) findViewById(R.id.magnetic);
        magnetic_x = (TextView) findViewById(R.id.magnetic_x);
        magnetic_y = (TextView) findViewById(R.id.magnetic_y);
        magnetic_z = (TextView) findViewById(R.id.magnetic_z);
        mag_sigma_x = (TextView) findViewById(R.id.mag_sigma_x);
        mag_sigma_y = (TextView) findViewById(R.id.mag_sigma_y);
        mag_sigma_z = (TextView) findViewById(R.id.mag_sigma_z);
        //orientation = (TextView) findViewById(R.id.orientation);
        orientation_x = (TextView) findViewById(R.id.orientation_x);
        orientation_y = (TextView) findViewById(R.id.orientation_y);
        orientation_z = (TextView) findViewById(R.id.orientation_z);
        ori_sigma_x = (TextView) findViewById(R.id.ori_sigma_x);
        ori_sigma_y = (TextView) findViewById(R.id.ori_sigma_y);
        ori_sigma_z = (TextView) findViewById(R.id.ori_sigma_z);
        /* ��oSensorManager */
        mSensorManager01 =(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        
        get_offset =(Button) findViewById(R.id.get_offset);
        close_device =(Button) findViewById(R.id.close);
        delay =(Button) findViewById(R.id.delay);
        cabiration1 =(Button) findViewById(R.id.cab_1);
        CheckBox =(CheckBox) findViewById(R.id.CheckBox);
        CheckBox.setChecked(true);
        cabiration1.setEnabled(true);
         /*使用setOnClickListener來監聽事件*/
        get_offset.setOnClickListener(get_offset_listener);
        close_device.setOnClickListener(close_listener);
        delay.setOnClickListener(delay_listener);
               cabiration1.setOnClickListener(cab1_listener);
        //int[] arr = new int[3];
 		//Linuxc.readxyz(xyz);
               CheckBox.setOnClickListener(new CheckBox.OnClickListener()
             {
             	@Override
                 public void onClick(View v)
                 {
                        // TODO Auto-generated method stub
                        if(CheckBox.isChecked())
                        {
                     	 cabiration1.setEnabled(true); 
                          myTextView01.setText(R.string.str_horizontal);
                        }
                        else
                        {
                     	   cabiration1.setEnabled(false);
                          //myTextView01.setText(R.string.text1);
                          /*�bTextView2����ܥX"�ФĿ�ڦP�N"*/
                          myTextView01.setText(R.string.str_horizontal);          
                        }
                 }
             });
        
    }
    
}