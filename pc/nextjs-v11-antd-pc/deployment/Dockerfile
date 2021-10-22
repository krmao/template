FROM node:12.19.0-alpine
MAINTAINER Joe

RUN mkdir -p /runDIR/web
COPY ./ /runDIR/web
# RUN npm install -g pm2
ENV HOST 0.0.0.0
ENV PORT 80
RUN cd /runDIR/web && ls && pwd
WORKDIR /runDIR/web
EXPOSE 8080 80

ENTRYPOINT ["npm","run","prod"]
