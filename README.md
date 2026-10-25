# Délcio Second Space — starter Android project

## O que esta versão inicial faz
- Interface Android nativa em Kotlin + Jetpack Compose.
- Permite criar um PIN de 4 a 8 dígitos e confirmá-lo.
- Guarda um hash PBKDF2 com salt, não o PIN em texto simples.
- Desbloqueia um ecrã privado demonstrativo e permite bloquear novamente.
- Não pede acesso à Internet nem permissões de fotos/contactos.

## Limitações importantes
Isto ainda NÃO é um segundo utilizador Android nem duplica WhatsApp/Facebook.
O ecrã privado é uma base para adicionar cofre de ficheiros cifrados. Antes de
guardar dados importantes, implementar e testar cifragem autenticada de ficheiros,
limite progressivo de tentativas, ocultação de conteúdo em screenshots/recents,
recuperação segura e testes de segurança.

## Como compilar
1. Instalar Android Studio num computador.
2. Abrir a pasta deste projeto no Android Studio.
3. Permitir sincronização do Gradle e instalar o Android SDK indicado.
4. Ligar o TECNO POP 8 por USB com depuração USB autorizada, ou usar um emulador.
5. Escolher Build > Build APK(s). O APK de debug aparece em
   `app/build/outputs/apk/debug/app-debug.apk`.

## Requisitos
- Android Studio com JDK 17.
- Android SDK Platform 35 (o dispositivo pode continuar a executar Android 13).
- Gradle Wrapper pode ser gerado pelo Android Studio na primeira abertura.
