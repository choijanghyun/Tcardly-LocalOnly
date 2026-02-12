# ── T-CARDLY ProGuard Rules ──

# 기본 Android 보존
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keepattributes InnerClasses

# ── Room ──
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keep @androidx.room.Dao class *

# ── Hilt ──
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep @dagger.hilt.android.lifecycle.HiltViewModel class * { *; }

# ── Compose (Compose ships its own consumer rules; only keep stability) ──
-dontwarn androidx.compose.**

# ── Gson (JSON 직렬화) ──
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
# 도메인 모델 보존 (Gson 역직렬화용)
-keep class com.tcardly.domain.model.** { *; }
-keep class com.tcardly.core.database.entity.** { *; }

# ── Google Play Billing ──
-keep class com.android.vending.billing.** { *; }
-keep class com.google.android.gms.internal.** { *; }

# ── ML Kit ──
-keep class com.google.mlkit.** { *; }
-dontwarn com.google.mlkit.**

# ── CameraX ──
-keep class androidx.camera.** { *; }

# ── SQLCipher ──
-keep class net.sqlcipher.** { *; }
-dontwarn net.sqlcipher.**

# ── Firebase (ships its own consumer rules) ──
-dontwarn com.google.firebase.**

# ── AdMob ──
-keep class com.google.android.gms.ads.** { *; }

# ── 결제 보안 - 핵심 클래스는 난독화 유지 (keep 제거) ──
# security/billing 클래스는 난독화되도록 별도 keep 규칙을 두지 않음
-keepclassmembers class com.tcardly.core.security.** { public <methods>; }
-keepclassmembers class com.tcardly.core.billing.** { public <methods>; }

# ── OkHttp (ships its own consumer rules) ──
-dontwarn okhttp3.**
-dontwarn okio.**

# ── Kotlin Coroutines ──
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.coroutines.** { volatile <fields>; }

# ── Enum 보존 ──
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# ── Parcelable ──
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# ── Serializable ──
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
