# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Tools\AndroidSDK/tools/proguard/proguard-android.txt
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
#-ignorewarnings
-keep class com.apkfuns.logutils.** { *;}
-dontwarn com.apkfuns.logutils.**

-keep class com.github.** { *;}
-dontwarn com.github.**
-dontwarn org.slf4j.**
-dontwarn ch.qos.**

-keep class ch.qos.** { *; }
-keep class org.slf4j.** { *; }
-keepattributes *Annotation*

#log4j
-keep class de.mindpipe.android.logging.log4j.** { *;}
-dontwarn de.mindpipe.android.logging.**
#okhttp3
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-dontwarn okio.**
#okhttpfinal
-keep class cn.finalteam.** { *;}
-dontwarn cn.finalteam.**
-keep class top.zibin.** { *;}
-dontwarn top.zibin.**

-keep class com.md.dzbp.ui.view.** { *;}
-dontwarn com.md.dzbp.ui.view.**
-keep class com.md.dzbp.data.** { *;}
-dontwarn com.md.dzbp.data.**
-keep class com.md.dzbp.utils.** { *;}
-dontwarn com.md.dzbp.utils.**

#apache
-keep class org.apache.** { *; }
-keep class android.net.http.** { *; }
-dontwarn org.apache**
-dontwarn android.net.http.**

-keep class android.app.smdt.** { *; }
-keep class de.mindpipe.android.logging.** { *; }
-dontwarn android.app.smdt.**

#butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

#glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# for DexGuard only
-keepresourcexmlelements manifest/application/meta-data@value=GlideModule

#EventBus3.0
-dontwarn org.greenrobot.**
  -keep class org.greenrobot.** { *; }
 -keepattributes *Annotation*
  -keepclassmembers class ** {
      @org.greenrobot.eventbus.Subscribe <methods>;
  }
#fastjson  fresco
  -dontwarn com.alibaba.fastjson.**
  -keep class com.alibaba.fastjson.** { *; }
  -keepattributes Signature

  #logutils
  -keep class com.facebook.** { *;}
  -dontwarn com.facebook.**
  -keep class com.apkfuns.logutils.** { *;}
  -dontwarn com.apkfuns.logutils.**
  -keep class com.github.Raizlabs.DBFlow.** { *;}
  -dontwarn com.github.Raizlabs.DBFlow.**
  -keepattributes *Annotation*
  -keepclassmembers class ** {
      @org.greenrobot.eventbus.Subscribe <methods>;
  }
  -keep enum org.greenrobot.eventbus.ThreadMode {
        *;
  }
  # Only required if you use AsyncExecutor
  -keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
      <init>(java.lang.Throwable);
  }
  -keep class * extends com.raizlabs.android.dbflow.config.DatabaseHolder { *; }


  #EventBus3.0
  -keepattributes *Annotation*
  -keepclassmembers class ** {
      @org.greenrobot.eventbus.Subscribe <methods>;
  }
  -keep enum org.greenrobot.eventbus.ThreadMode { *; }
  # Only required if you use AsyncExecutor
  -keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
      <init>(java.lang.Throwable);
  }
-keep class com.company.** { *;}

# glide 的混淆代码
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
# banner 的混淆代码
-keep class com.zk.banner.** {
    *;
 }

 -dontwarn com.hjq.permissions.**