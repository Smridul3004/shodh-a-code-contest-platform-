#!/bin/bash

# Get the language and code from environment variables
LANGUAGE=${1:-java}
CODE=${2:-""}

# Create temporary files
TEMP_DIR="/tmp/execution_$$"
mkdir -p "$TEMP_DIR"

# Set time limit (default 5 seconds)
TIME_LIMIT=${3:-5}

# Function to cleanup
cleanup() {
    rm -rf "$TEMP_DIR"
    # Kill any remaining processes
    pkill -f "java.*Main" 2>/dev/null || true
    pkill -f "python3.*main.py" 2>/dev/null || true
    pkill -f "node.*main.js" 2>/dev/null || true
}

# Set trap for cleanup
trap cleanup EXIT

case $LANGUAGE in
    "java")
        echo "$CODE" > "$TEMP_DIR/Main.java"
        cd "$TEMP_DIR"
        timeout $TIME_LIMIT javac Main.java 2>/dev/null
        if [ $? -eq 0 ]; then
            timeout $TIME_LIMIT java Main
        else
            echo "COMPILATION_ERROR"
            exit 1
        fi
        ;;
    "python")
        echo "$CODE" > "$TEMP_DIR/main.py"
        cd "$TEMP_DIR"
        timeout $TIME_LIMIT python3 main.py
        ;;
    "javascript")
        echo "$CODE" > "$TEMP_DIR/main.js"
        cd "$TEMP_DIR"
        timeout $TIME_LIMIT node main.js
        ;;
    *)
        echo "UNSUPPORTED_LANGUAGE"
        exit 1
        ;;
esac

exit_code=$?
if [ $exit_code -eq 124 ]; then
    echo "TIME_LIMIT_EXCEEDED"
    exit 1
fi

exit $exit_code
