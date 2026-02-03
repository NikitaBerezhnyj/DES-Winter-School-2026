#!/bin/bash
set -e

echo "Starting Ollama server..."
/bin/ollama serve &
pid=$!

echo "Waiting for Ollama server to start..."
sleep 10

echo "Pulling llama3 model..."
ollama pull llama3

echo "Model pulled successfully. Keeping server running..."
wait $pid