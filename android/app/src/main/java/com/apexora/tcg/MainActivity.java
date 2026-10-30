package com.apexora.tcg;

import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow();

        // Pantalla siempre encendida mientras se juega
        w.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Dibujar también bajo el notch / cámara (la web ya usa env(safe-area-inset-*))
        if (Build.VERSION.SDK_INT >= 28) {
            w.getAttributes().layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        requestHighestRefreshRate(w);
        tuneWebView();
        hideSystemBars();
    }

    /** Pide 90/120/144 Hz si el panel los soporta (con la misma resolución). */
    private void requestHighestRefreshRate(Window w) {
        if (Build.VERSION.SDK_INT < 23) return;
        try {
            Display d = getWindowManager().getDefaultDisplay();
            Display.Mode cur = d.getMode();
            Display.Mode best = cur;
            for (Display.Mode m : d.getSupportedModes()) {
                if (m.getPhysicalWidth() == cur.getPhysicalWidth()
                        && m.getPhysicalHeight() == cur.getPhysicalHeight()
                        && m.getRefreshRate() > best.getRefreshRate()) {
                    best = m;
                }
            }
            WindowManager.LayoutParams lp = w.getAttributes();
            lp.preferredDisplayModeId = best.getModeId();
            w.setAttributes(lp);
        } catch (Throwable ignored) { }
    }

    /** Ajustes del WebView para que se sienta como una app y no como un navegador. */
    private void tuneWebView() {
        try {
            WebView wv = getBridge().getWebView();
            WebSettings s = wv.getSettings();
            s.setMediaPlaybackRequiresUserGesture(false);   // sonido sin esperar un toque extra
            s.setCacheMode(WebSettings.LOAD_DEFAULT);        // respeta la caché HTTP: abre rápido y se actualiza con la web
            s.setSupportZoom(false);
            s.setBuiltInZoomControls(false);
            s.setTextZoom(100);                              // ignora el tamaño de fuente del sistema
            wv.setOverScrollMode(View.OVER_SCROLL_NEVER);
            wv.setLongClickable(false);
            wv.setOnLongClickListener(v -> true);            // sin menú de "copiar" al mantener pulsado
            wv.setHapticFeedbackEnabled(false);
            wv.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } catch (Throwable ignored) { }
    }

    private void hideSystemBars() {
        Window w = getWindow();
        WindowCompat.setDecorFitsSystemWindows(w, false);
        WindowInsetsControllerCompat c = WindowCompat.getInsetsController(w, w.getDecorView());
        if (c == null) return;
        c.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        c.hide(WindowInsetsCompat.Type.systemBars());
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) hideSystemBars();
    }
}
