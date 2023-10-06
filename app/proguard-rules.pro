# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class com.ovolk.dictionary.domain.model.select_languages.Language{ *; }
-keep class com.ovolk.dictionary.domain.model.migrate_2_3.OldHints{ *; }
-keep class com.ovolk.dictionary.domain.model.migrate_2_3.OldTranslate{ *; }
-keep class com.ovolk.dictionary.data.database.migration.Lang{ *; }
-keep class com.ovolk.dictionary.data.model.NearestFeatureFirestore{ *; }
-keep class com.ovolk.dictionary.data.model.NearestFeatureFirestore$*{ *; }