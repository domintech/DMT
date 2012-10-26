package tw.com.dmt.gsensor;

import android.util.Log;

public class Linuxc {
	static {
        try {
            Log.i("JNI", "Trying to load libgsensor.so");
            /* 調用gsensor.so */
            System.loadLibrary("gsensor"); 
        }
        catch (UnsatisfiedLinkError ule) {
            Log.e("JNI", "WARNING: Could not load libgsensor.so");
        }}
	 
	public static native int open();
	public static native int close();
	
	public static native int reset();
	public static native int cab(int[] arr);
	public static native int getoffset(int[] arr);
	public static native int setoffset(int[] arr);
	public static native int readxyz(int[] arr);
}
