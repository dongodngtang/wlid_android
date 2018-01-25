# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/zaitu/Android/Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#百度地图混淆
#start
-keep class com.baidu.** {*;}
-keep class vi.com.** {*;}
-dontwarn com.baidu.**
#end

#eventBus混淆
#start
-keepclassmembers class ** {
    public void onEvent*(***);
}
#end
#


#蒲公英混淆
#start
-dontwarn com.pgyersdk.**
-keep class com.pgyersdk.** { *; }
#end

#友盟混淆
#start
# 以下类过滤不混淆
-keep public class * extends com.umeng.**
# 以下包不进行过滤
-keep class com.umeng.** { *; }
-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}

-keep public class net.doyouhike.app.wildbird.R$*{
public static final int *;
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
#end
-dontwarn org.dom4j.**
-keep class org.dom4j.**
-dontwarn org.apache.**
-keep class org.apache.**

-ignorewarnings

#--------------- BEGIN: 注入到页面的接口类防混淆 ----------
-keepclassmembers class net.doyouhike.app.wildbird.ui.webview.HostJsScope{ *; }
#--------------- END ----------

##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson

##---------------End: proguard configuration for Gson  ----------

# # -------------------------------------------


##---------------BEGIN: greenDao混淆  ----------

# # -------------------------------------------
#
-keep class de.greenrobot.dao.** {*;}
-keepclassmembers class * extends de.greenrobot.dao.AbstractDao {
    public static java.lang.String TABLENAME;
}
-keep class **$Properties
#
##---------------End: greenDao混淆  ----------
# # -------------------------------------------