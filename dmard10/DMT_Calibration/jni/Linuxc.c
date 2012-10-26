#include <jni.h>
#include "Linuxc.h"

#include <stdio.h>
#include <stdlib.h>	
#include <fcntl.h>
#include <errno.h>
#include <unistd.h>
#include <sys/ioctl.h>

#define DEVICE_SENSOR_DMT "/dev/dmt"
#define DEVICE_SENSOR_03 "/dev/dmard03"
#define DEVICE_SENSOR_05 "/dev/dmard05"
#define DEVICE_SENSOR_06 "/dev/dmard06"
#define DEVICE_SENSOR_07 "/dev/dmard07"
#define DEVICE_SENSOR_08 "/dev/dmard08"
#define DEVICE_SENSOR_09 "/dev/dmard09"
#define DEVICE_SENSOR_10 "/dev/dmard10"
#define DEVICE_SENSOR_11 "/dev/dmard11"
#define DEVICE_SENSOR_12 "/dev/dmard12"
#define DEVICE_SENSOR_13 "/dev/dmard13"
#define DEVICE_SENSOR_18 "/dev/dmard18"
#define DEVICE_SENSOR_RK "/dev/gs_dmard06"
#define DEVICE_SENSOR_RK22 "/dev/mma7660_daemon"

int fd = 0;
JNIEXPORT jint JNICALL Java_tw_com_dmt_gsensor_Linuxc_open
 (JNIEnv *env, jclass mc)
{
	if((fd= open(DEVICE_SENSOR_DMT,O_RDONLY)) > 0) return fd;
	if((fd= open(DEVICE_SENSOR_RK,O_RDONLY)) > 0) return fd;
	if((fd= open(DEVICE_SENSOR_RK22,O_RDONLY)) > 0) return fd;
	if((fd= open(DEVICE_SENSOR_03,O_RDONLY)) > 0) return fd;
	if((fd= open(DEVICE_SENSOR_05,O_RDONLY)) > 0) return fd;
	if((fd= open(DEVICE_SENSOR_06,O_RDONLY)) > 0) return fd;
	if((fd= open(DEVICE_SENSOR_07,O_RDONLY)) > 0) return fd;
	if((fd= open(DEVICE_SENSOR_08,O_RDONLY)) > 0) return fd;
	if((fd= open(DEVICE_SENSOR_09,O_RDONLY)) > 0) return fd;
	if((fd= open(DEVICE_SENSOR_10,O_RDONLY)) > 0) return fd;
	if((fd= open(DEVICE_SENSOR_11,O_RDONLY)) > 0) return fd;
	if((fd= open(DEVICE_SENSOR_12,O_RDONLY)) > 0) return fd;
	if((fd= open(DEVICE_SENSOR_13,O_RDONLY)) > 0) return fd;
	if((fd= open(DEVICE_SENSOR_18,O_RDONLY)) > 0) return fd;
 	return fd;
}

JNIEXPORT jint JNICALL Java_tw_com_dmt_gsensor_Linuxc_close
 (JNIEnv *env, jclass mc)
{
 close(fd);
 return 0;
}

JNIEXPORT jint JNICALL Java_tw_com_dmt_gsensor_Linuxc_reset
(JNIEnv *env, jclass mc)
{
 //ioctl(fd,SENSOR_RESET);
 return 0;               
}

JNIEXPORT jint JNICALL Java_tw_com_dmt_gsensor_Linuxc_cab
(JNIEnv *env, jclass mc, jintArray arr)
{
 int tmp[3];
 jint* body = (*env)->GetIntArrayElements(env, arr, 0);
 tmp[0] = body[0];
 tmp[1] = 0;
 tmp[2] = 0;
 ioctl(fd,SENSOR_CALIBRATION, &tmp);
 body[0] = tmp[0];
 body[1] = tmp[1];
 body[2] = tmp[2];
 return 0;               
}

JNIEXPORT jint JNICALL Java_tw_com_dmt_gsensor_Linuxc_getoffset
(JNIEnv *env, jclass mc, jintArray arr)
{
 int tmp[3];
 ioctl(fd,SENSOR_GET_OFFSET, &tmp);
 jint* body = (*env)->GetIntArrayElements(env, arr, 0);
 body[0] = tmp[0];
 body[1] = tmp[1];
 body[2] = tmp[2]; 
 return 0;      
}

JNIEXPORT jint JNICALL Java_tw_com_dmt_gsensor_Linuxc_setoffset
(JNIEnv *env, jclass mc, jintArray arr)
{
 
 jint* body = (*env)->GetIntArrayElements(env, arr, 0);
 int tmp[3];
 tmp[0] = body[0];
 tmp[1] = body[1];
 tmp[2] = body[2];
 
 //ioctl(fd,SENSOR_SET_OFFSET,&tmp);
 return 0;               
}

JNIEXPORT jint JNICALL Java_tw_com_dmt_gsensor_Linuxc_readxyz
(JNIEnv *env, jclass mc, jintArray arr)
{
 int tmp[3];
 //ioctl(fd,SENSOR_READ_ACCEL_XYZ,&tmp);
 
 jint* body = (*env)->GetIntArrayElements(env, arr,0);
 
 body[0] = tmp[0];
 body[1] = tmp[1];
 body[2] = tmp[2];
 return 0;             
}
