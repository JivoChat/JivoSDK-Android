-keep,allowoptimization,allowshrinking,allowobfuscation class androidx.lifecycle.LiveData
-keep,allowoptimization,allowshrinking,allowobfuscation class com.jivosite.sdk.network.response.ApiResponse

-keep,allowoptimization,allowshrinking,allowobfuscation class com.jivosite.sdk.model.pojo.config.Config
-keep,allowoptimization,allowshrinking,allowobfuscation class com.jivosite.sdk.model.pojo.history.HistoryResult
-keep,allowoptimization,allowshrinking,allowobfuscation class com.jivosite.sdk.model.pojo.file.AccessResponse
-keep,allowoptimization,allowshrinking,allowobfuscation class com.jivosite.sdk.model.pojo.media.MediaSignResponse
-keep,allowoptimization,allowshrinking,allowobfuscation class com.jivosite.sdk.model.pojo.response.Response

-if interface com.jivosite.sdk.api.** { @retrofit2.http.* public *** *(...); }
-keep,allowoptimization,allowshrinking,allowobfuscation class <3>

-keep class com.jivosite.sdk.Jivo {
    public static <methods>;
}

-keep enum com.jivosite.sdk.push.JivoPushProvider { *; }

-keep interface com.jivosite.sdk.model.repository.history.NewMessageListener {
    void onNewMessage(boolean);
}

-keep interface com.jivosite.sdk.ui.chat.NotificationPermissionListener {
    public <methods>;
}

-keep public class com.jivosite.sdk.ui.views.JivoChatButton {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keep,allowobfuscation class com.jivosite.sdk.push.JivoFirebaseMessagingService {
    public <init>();
    protected <methods>;
}
