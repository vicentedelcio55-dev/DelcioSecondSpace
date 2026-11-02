# Délcio Second Space

## Estado atual
- Interface Android nativa em Kotlin + Jetpack Compose.
- Permite criar um PIN de 4 a 8 dígitos e confirmá-lo.
- Guarda um hash PBKDF2 com salt, não o PIN em texto simples.
- Desbloqueia um ecrã privado demonstrativo e permite bloquear novamente.
- Não pede acesso à Internet nem permissões de fotos/contactos.

## Limitações atuais
Isto ainda NÃO é um segundo utilizador Android nem duplica WhatsApp/Facebook.
O ecrã privado é uma base para adicionar cofre de ficheiros cifrados. Antes de
guardar dados importantes, a cifragem autenticada, recuperação segura, limite
progressivo de tentativas, proteção de screenshots/recents e os testes de
segurança devem estar implementados e validados.

## Como compilar
1. Instalar Android Studio ou o Android SDK com Platform 35 e Build Tools 35.0.0.
2. Abrir a pasta deste projeto no Android Studio ou configurar `ANDROID_HOME`.
3. Executar `./gradlew test lintDebug assembleDebug`.
4. O APK de debug aparece em `app/build/outputs/apk/debug/app-debug.apk`.
5. O GitHub Actions executa automaticamente testes, lint e build em `main`.

## Requisitos
- Android Studio com JDK 17 ou superior compatível.
- Android SDK Platform 35 (o dispositivo pode continuar a executar Android 13).
- Gradle Wrapper 8.7 incluído no projeto.
