# Cartas Alfa — app Android

La app es un contenedor nativo (Capacitor) que abre **https://apexora.github.io/TCG/** en pantalla completa.
Cada vez que haces `npm run deploy` en tu juego, la app se actualiza sola (la próxima vez que se abra). No hay que recompilar el APK para cambios del juego.

## Opción A — compilar en la nube (sin instalar nada)
1. Crea un repo en GitHub (ej. `TCG-android`) y sube esta carpeta completa.
2. Pestaña **Actions** → "Build Android APK" → **Run workflow**.
3. Al terminar (≈5 min), descarga el artefacto **CartasAlfa-apk** → `app-debug.apk` → instálalo en el teléfono.

## Opción B — Android Studio
`npm install` → `npx cap sync android` → `npx cap open android` → Run ▶ (o Build › Build APK).

## Para publicar en Play Store
Android Studio → Build › Generate Signed Bundle (AAB). Guarda el keystore en un lugar seguro.
Cambia `appId` en `capacitor.config.json` solo ANTES de publicar (después no se puede).

## Mejoras en la web (opcional, recomendado)
Pega `web-patch/native-app-snippet.html` dentro del `<head>` de tu `index.html` y haz deploy:
quita selección de texto/zoom/rebote y desactiva `backdrop-filter` solo dentro de la app (fluidez).

## Qué hace la app por dentro
- Horizontal bloqueado, pantalla completa inmersiva, pantalla siempre encendida.
- Pide la tasa de refresco más alta del panel (90/120 Hz).
- Splash con arte del juego, icono adaptativo, pantalla "Sin conexión" con botón reintentar.
- User-Agent con `ApexoraApp` para que la web detecte que está en la app (`html.native-app`).
# tcg-mobile
