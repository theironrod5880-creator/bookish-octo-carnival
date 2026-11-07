#!/data/data/com.termux/files/usr/bin/bash
set -e

echo "[*] Updating Termux packages..."
pkg update -y && pkg upgrade -y

echo "[*] Installing Java 17, Git, and Gradle..."
pkg install -y openjdk-17 git gradle make

echo "[*] Configuring Java Environment..."
export JAVA_HOME=/data/data/com.termux/files/usr/lib/jvm/java-17-openjdk
export PATH=$JAVA_HOME/bin:$PATH

echo "[*] Verifying Java installation..."
java -version

echo "[*] Running Gradle Build..."
if [ -f "gradlew" ]; then
    chmod +x gradlew
    ./gradlew assembleDebug
else
    gradle assembleDebug
fi

echo "[*] Success! Your debug APK is located at: app/build/outputs/apk/debug/app-debug.apk"
