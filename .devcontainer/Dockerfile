FROM mcr.microsoft.com/devcontainers/java:21

USER vscode

RUN sudo apt-get update \
    && sudo apt-get install -y maven \
    && sudo apt-get clean \
    && sudo rm -rf /var/lib/apt/lists/*
