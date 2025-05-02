package com.a221103819.uts221103837;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText _urlEditText;
    private WebView _webView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buatNotificationChannel();
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        _urlEditText = findViewById(R.id.urlEditText);
        _webView1 = findViewById(R.id.webView1);
    }

    @SuppressLint("MissingPermission")
    public void tampilkanButton_onClick(View view) {
        String url = _urlEditText.getText().toString().trim();

        if (!url.startsWith("https://")) {
            new AlertDialog.Builder(this)
                    .setTitle("URL TIDAK VALID")
                    .setMessage("URL harus diawali dengan https://")
                    .setPositiveButton("Oke", null)
                    .show();
        } else {
            _webView1.setWebViewClient(new WebViewClient());
            _webView1.getSettings().setJavaScriptEnabled(true);
            _webView1.loadUrl(url);

            // Kustom Notifikasi
            RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.custom_notification);
            remoteViews.setTextViewText(R.id.text_nim, "NIM: 221103819");
            remoteViews.setTextViewText(R.id.text_nama, "Nama: Nama Kamu");
            remoteViews.setTextViewText(R.id.text_url, "Membuka: " + url);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id_notifikasi")
                    .setSmallIcon(R.drawable.ic_launcher_foreground) // atau ikon lain
                    .setCustomContentView(remoteViews)
                    .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(1,builder.build());
        }
    }


    private void buatNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "channel_id_notifikasi",
                    "Notifikasi WA Style",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Channel untuk notifikasi kustom mirip WhatsApp");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}