#include <jni.h>
#include <string>
#include <stdio.h>
#include<iostream>

using namespace std;
extern "C" JNIEXPORT jstring

JNICALL
Java_android_xyz_com_myapplication_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    char str[] = "abcdbaddecebajjababbaabaaebaaba";
//    printf("%s\n", str);
    hello = str;
    hello = "INPUT:"+ hello + "\nOUTPUT:\n";
    int arr[10] ;
    int countA = 0;
    for (int i = 0 ; i < sizeof(str)/sizeof(str[0]); i++ ){
        if(str[i] == 'a'){
            arr[countA] = i;
            countA ++;
        }
        if(str[i] == 'b'){
            for(int n = 0; (n < countA); n++){
                if(arr[n] <  i){
                    string tem;
                    for(int j = arr[n];j <= i;j++){
//                        printf("%c", str[j]);
                        tem = tem + str[j];
                    }
                    hello += tem + "\n";
                }
            }
        }
    }
    return env->NewStringUTF(hello.c_str());
}
