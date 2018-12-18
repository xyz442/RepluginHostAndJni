package android.xyz.com.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.qihoo360.replugin.RePlugin;

/**
 * @desc ${TODD}
 */

public class NextPluginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin_text);
    }

    public void onDownloadTextPlugin(View view) {
        // 插件下载地址 https://raw.githubusercontent.com/xyz442/webview-plugin/master/
        String urlPath = "https://raw.githubusercontent.com/xyz442/webview-plugin/master/app/src/main/res/raw/webview.apk";
        // 插件下载后的存放路径
        String downloadDir = Environment.getExternalStorageDirectory().getAbsolutePath();

        Intent intent = new Intent(this, DownloadService.class);
        intent.putExtra("urlPath", urlPath);
        intent.putExtra("downloadDir", downloadDir);
        startService(intent);
    }

    public void onOpenTextPlugin(View view) {
        // 打开一个插件的Activity
        RePlugin.startActivity(NextPluginActivity.this,
                RePlugin.createIntent("webview", "com.xyz.replugin.sample.webview.MainActivity"));
    }

    public void onUpdateTextPlugin(View view) {
        // 插件下载地址
        String urlPath = "https://github.com/xyz442/webview-plugin/blob/master/app/src/main/res/raw/webview.apk";
        // 插件下载后的存放路径
        String downloadDir = Environment.getExternalStorageDirectory().getAbsolutePath();

        Intent intent = new Intent(this, DownloadService.class);
        intent.putExtra("urlPath", urlPath);
        intent.putExtra("downloadDir", downloadDir);
        startService(intent);
    }

    public void unUpdateTextPlugin(View view) {
        RePlugin.uninstall("webview");
    }
}
