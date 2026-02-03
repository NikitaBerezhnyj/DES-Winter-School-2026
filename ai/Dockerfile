FROM ollama/ollama

COPY ./pull-llama3.sh /pull-llama3.sh

RUN sed -i 's/\r$//' /pull-llama3.sh && \
    chmod +x /pull-llama3.sh

ENTRYPOINT ["/pull-llama3.sh"]