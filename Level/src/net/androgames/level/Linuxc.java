package net.androgames.level;

import android.util.Log;
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
