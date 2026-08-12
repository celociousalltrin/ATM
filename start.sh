#!/bin/bash

# Exit immediately if a command exits with a non-zero status
set -e

echo "======================================"
echo "        ATM Application Startup"
echo "======================================"

# --------------------------------------
# Locate Project Directory
# --------------------------------------

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

cd "$SCRIPT_DIR"

# --------------------------------------
# Function: Check Java 17
# --------------------------------------

check_java() {

    # --------------------------------------
    # macOS
    # --------------------------------------

    if [[ "$OSTYPE" == "darwin"* ]]; then

        # Check whether Java 17 is already installed
        JAVA_17_HOME=$(/usr/libexec/java_home -v 17 2>/dev/null || true)

        if [ -n "$JAVA_17_HOME" ]; then

            export JAVA_HOME="$JAVA_17_HOME"
            export PATH="$JAVA_HOME/bin:$PATH"

        else

            echo ""
            echo "[!] Java 17 is not installed."

            if [ -t 0 ]; then

                read -p "Would you like to install OpenJDK 17? (y/n): " ANSWER

                case "$ANSWER" in
                    y|Y)
                        ;;
                    *)
                        echo ""
                        echo "Startup aborted."
                        echo "Please install Java 17 and run this script again."
                        exit 1
                        ;;
                esac

            else

                echo "[!] Non-interactive environment detected."
                echo "[!] Automatic package installation skipped."
                echo "Please install Java 17 manually."
                exit 1

            fi

            # --------------------------------------
            # Install Java 17 using Homebrew
            # --------------------------------------

            if command -v brew &> /dev/null; then

                echo ""
                echo "Installing OpenJDK 17 using Homebrew..."

                brew install openjdk@17

            else

                echo ""
                echo "[X] Homebrew is not installed."
                echo ""
                echo "Please install Homebrew first."
                echo "Then run ./start.sh again."
                exit 1

            fi

            # --------------------------------------
            # Find Java 17 after installation
            # --------------------------------------

            JAVA_17_HOME=$(/usr/libexec/java_home -v 17 2>/dev/null || true)

            if [ -z "$JAVA_17_HOME" ]; then

                # Homebrew Apple Silicon
                if [ -d "/opt/homebrew/opt/openjdk@17" ]; then
                    JAVA_17_HOME="/opt/homebrew/opt/openjdk@17"
                fi

                # Homebrew Intel Mac
                if [ -d "/usr/local/opt/openjdk@17" ]; then
                    JAVA_17_HOME="/usr/local/opt/openjdk@17"
                fi

            fi

            if [ -z "$JAVA_17_HOME" ]; then

                echo ""
                echo "[X] Java 17 was installed but could not be located."
                echo "Please restart your terminal and run ./start.sh again."

                exit 1

            fi

            export JAVA_HOME="$JAVA_17_HOME"
            export PATH="$JAVA_HOME/bin:$PATH"

        fi

    # --------------------------------------
    # Linux
    # --------------------------------------

    elif [[ "$OSTYPE" == "linux-gnu"* ]]; then

        JAVA_17_HOME=""

        # --------------------------------------
        # Try to locate Java 17 using JAVA_HOME
        # --------------------------------------

        if [ -n "${JAVA_HOME:-}" ] && [ -x "$JAVA_HOME/bin/java" ]; then

            JAVA_VERSION=$("$JAVA_HOME/bin/java" -version 2>&1 | awk -F '"' '/version/ {print $2}')

            JAVA_MAJOR_VERSION=$(echo "$JAVA_VERSION" | cut -d'.' -f1)

            if [ "$JAVA_MAJOR_VERSION" = "17" ]; then
                JAVA_17_HOME="$JAVA_HOME"
            fi

        fi

        # --------------------------------------
        # Try common Linux Java 17 locations
        # --------------------------------------

        if [ -z "$JAVA_17_HOME" ]; then

            for JAVA_PATH in \
                /usr/lib/jvm/java-17-openjdk \
                /usr/lib/jvm/java-17-openjdk-amd64 \
                /usr/lib/jvm/java-17-openjdk-arm64 \
                /usr/lib/jvm/jdk-17 \
                /usr/lib/jvm/jdk-17-openjdk
            do

                if [ -x "$JAVA_PATH/bin/java" ]; then
                    JAVA_17_HOME="$JAVA_PATH"
                    break
                fi

            done

        fi

        # --------------------------------------
        # Try update-alternatives
        # --------------------------------------

        if [ -z "$JAVA_17_HOME" ] && command -v update-alternatives &> /dev/null; then

            JAVA_17_PATH=$(update-alternatives --list java 2>/dev/null | grep '/java-17-' | head -n 1 || true)

            if [ -n "$JAVA_17_PATH" ]; then
                JAVA_17_HOME="${JAVA_17_PATH%/bin/java}"
            fi

        fi

        # --------------------------------------
        # Java 17 not found
        # --------------------------------------

        if [ -z "$JAVA_17_HOME" ]; then

            CURRENT_JAVA_VERSION=""

            if command -v java &> /dev/null; then
                CURRENT_JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}')
            fi

            if [ -n "$CURRENT_JAVA_VERSION" ]; then
                echo ""
                echo "[!] Java $CURRENT_JAVA_VERSION is currently active."
                echo "[!] Java 17 is required."
            else
                echo ""
                echo "[!] Java is not installed."
            fi

            if [ -t 0 ]; then

                read -p "Would you like to attempt installing OpenJDK 17? (y/n): " ANSWER

                case "$ANSWER" in
                    y|Y)
                        ;;
                    *)
                        echo ""
                        echo "Startup aborted."
                        echo "Please install Java 17 and run this script again."
                        exit 1
                        ;;
                esac

            else

                echo "[!] Non-interactive environment detected."
                echo "[!] Automatic package installation skipped."
                echo "Please install Java 17 manually."
                exit 1

            fi

            echo ""
            echo "Installing OpenJDK 17..."

            if command -v apt &> /dev/null; then

                sudo apt update
                sudo apt install -y openjdk-17-jdk

            elif command -v dnf &> /dev/null; then

                sudo dnf install -y java-17-openjdk-devel

            elif command -v yum &> /dev/null; then

                sudo yum install -y java-17-openjdk-devel

            else

                echo ""
                echo "[X] Unsupported package manager."
                echo "Please install Java 17 manually."
                exit 1

            fi

            echo ""
            echo "[+] Java 17 installation completed."

            # --------------------------------------
            # Locate Java 17 after installation
            # --------------------------------------

            if [ -n "${JAVA_HOME:-}" ] && [ -x "$JAVA_HOME/bin/java" ]; then

                JAVA_VERSION=$("$JAVA_HOME/bin/java" -version 2>&1 | awk -F '"' '/version/ {print $2}')
                JAVA_MAJOR_VERSION=$(echo "$JAVA_VERSION" | cut -d'.' -f1)

                if [ "$JAVA_MAJOR_VERSION" = "17" ]; then
                    JAVA_17_HOME="$JAVA_HOME"
                fi

            fi

            if [ -z "$JAVA_17_HOME" ]; then

                for JAVA_PATH in \
                    /usr/lib/jvm/java-17-openjdk \
                    /usr/lib/jvm/java-17-openjdk-amd64 \
                    /usr/lib/jvm/java-17-openjdk-arm64 \
                    /usr/lib/jvm/jdk-17 \
                    /usr/lib/jvm/jdk-17-openjdk
                do

                    if [ -x "$JAVA_PATH/bin/java" ]; then
                        JAVA_17_HOME="$JAVA_PATH"
                        break
                    fi

                done

            fi

        fi

        # --------------------------------------
        # Java 17 still not found
        # --------------------------------------

        if [ -z "$JAVA_17_HOME" ]; then

            echo ""
            echo "[X] Could not locate Java 17."
            echo "Please install Java 17 manually and run ./start.sh again."
            exit 1

        fi

        # --------------------------------------
        # Use Java 17
        # --------------------------------------

        export JAVA_HOME="$JAVA_17_HOME"
        export PATH="$JAVA_HOME/bin:$PATH"

    else

        echo ""
        echo "[X] Unsupported operating system."
        echo "[X] This start.sh supports macOS and Linux."
        exit 1

    fi

    # --------------------------------------
    # Verify Java 17
    # --------------------------------------

    JAVA_VERSION=$("$JAVA_HOME/bin/java" -version 2>&1 | awk -F '"' '/version/ {print $2}')

    if [ -z "$JAVA_VERSION" ]; then

        echo ""
        echo "[X] Could not determine Java version."
        exit 1

    fi

    JAVA_MAJOR_VERSION=$(echo "$JAVA_VERSION" | cut -d'.' -f1)

    # Java 8 format: 1.8.x
    if [ "$JAVA_MAJOR_VERSION" = "1" ]; then
        JAVA_MAJOR_VERSION=$(echo "$JAVA_VERSION" | cut -d'.' -f2)
    fi

    # --------------------------------------
    # Require EXACTLY Java 17
    # --------------------------------------

    if [ "$JAVA_MAJOR_VERSION" != "17" ]; then

        echo ""
        echo "======================================"
        echo "       Unsupported Java Version"
        echo "======================================"
        echo ""
        echo "[X] This ATM application requires Java 17."
        echo "[X] Current Java version: $JAVA_VERSION"
        echo ""
        echo "Please install/switch to Java 17."
        exit 1

    fi

    echo ""
    echo "[+] Java 17 detected."
    echo "[+] Java Version: $JAVA_VERSION"
    echo "[+] JAVA_HOME: $JAVA_HOME"
}

# --------------------------------------
# Check Java
# --------------------------------------

check_java

# --------------------------------------
# Check Port 8080
# --------------------------------------

if command -v lsof &> /dev/null && \
   lsof -i :8080 -sTCP:LISTEN &> /dev/null; then

    echo ""
    echo "======================================"
    echo "        Port 8080 Already In Use"
    echo "======================================"

    echo ""
    echo "[!] Port 8080 is already being used."
    echo ""

    lsof -i :8080 -sTCP:LISTEN

    # --------------------------------------
    # Handle non-interactive environment
    # --------------------------------------

    if [ ! -t 0 ]; then

        echo ""
        echo "[X] Non-interactive environment detected."
        echo "[X] Cannot ask permission to stop the process."
        echo "[X] Please free port 8080 manually."
        exit 1

    fi

    echo ""
    read -p "Would you like to stop the process using port 8080? (y/n): " ANSWER

    case "$ANSWER" in

        y|Y)

            PID=$(lsof -ti :8080 -sTCP:LISTEN)

            if [ -n "$PID" ]; then

                echo ""
                echo "[+] Stopping process: $PID"

                kill "$PID"

                sleep 2

                # --------------------------------------
                # Verify port is free
                # --------------------------------------

                if lsof -i :8080 -sTCP:LISTEN &> /dev/null; then

                    echo ""
                    echo "[X] Port 8080 is still in use."
                    echo "[X] Please stop the process manually and try again."
                    exit 1

                fi

                echo "[+] Port 8080 is now available."

            fi

            ;;

        *)

            echo ""
            echo "Startup aborted."
            echo "Please stop the process using port 8080 and run ./start.sh again."
            exit 1

            ;;

    esac

fi

# --------------------------------------
# Make Maven Wrapper Executable
# --------------------------------------

if [ -f "./mvnw" ]; then

    chmod +x ./mvnw

else

    echo ""
    echo "[X] Maven wrapper ('mvnw') not found."
    echo "[X] Make sure this script is inside the ATM project."
    exit 1

fi

# --------------------------------------
# Verify Maven uses Java 17
# --------------------------------------

echo ""
echo "======================================"
echo "        Maven Environment"
echo "======================================"

./mvnw -version

# --------------------------------------
# Build Application
# --------------------------------------

echo ""
echo "======================================"
echo "      Building ATM Application"
echo "======================================"

./mvnw clean package -DskipTests

# --------------------------------------
# Locate Built JAR
# --------------------------------------

JAR_FILE=$(find target -maxdepth 1 -type f -name "*.jar" \
    ! -name "*.original" \
    ! -name "*-sources.jar" \
    ! -name "*-javadoc.jar" \
    | head -n 1)

if [ -z "$JAR_FILE" ]; then

    echo ""
    echo "[X] Could not find built JAR file."
    echo "[X] Check the target/ directory."
    exit 1

fi

# --------------------------------------
# Start Application
# --------------------------------------

echo ""
echo "======================================"
echo "        Starting ATM Application"
echo "======================================"

echo "[+] Java: 17"
echo "[+] JAR: $JAR_FILE"
echo "[+] URL: http://localhost:8080"
echo ""

exec "$JAVA_HOME/bin/java" -jar "$JAR_FILE"