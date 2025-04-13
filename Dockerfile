FROM ubuntu:latest
LABEL authors="heojin"

ENTRYPOINT ["top", "-b"]