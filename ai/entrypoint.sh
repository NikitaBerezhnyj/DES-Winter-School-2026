#!/bin/bash
set -e

MODEL_NAME="${OLLAMA_MODEL:-llama3.2:1b}"

echo "Starting Ollama server..."
/bin/ollama serve &
SERVER_PID=$!

echo "Waiting for Ollama API to be ready..."
MAX_ATTEMPTS=60
ATTEMPT=0

while [ $ATTEMPT -lt $MAX_ATTEMPTS ]; do
    if /bin/ollama list >/dev/null 2>&1; then
        echo "Ollama API is ready!"
        sleep 3  # Додаткова затримка для стабільності
        break
    fi
    ATTEMPT=$((ATTEMPT+1))
    echo "Attempt $ATTEMPT/$MAX_ATTEMPTS..."
    sleep 1
done

if [ $ATTEMPT -eq $MAX_ATTEMPTS ]; then
    echo "ERROR: Ollama failed to start"
    exit 1
fi

# Перевіряємо та завантажуємо модель
echo "Checking for model: $MODEL_NAME"
if ! /bin/ollama list | grep -q "$MODEL_NAME"; then
    echo "Pulling $MODEL_NAME (this may take a few minutes)..."
    /bin/ollama pull "$MODEL_NAME"
    echo "Model $MODEL_NAME pulled successfully!"
else
    echo "Model $MODEL_NAME already exists."
fi

echo "Ollama is ready!"
wait $SERVER_PID