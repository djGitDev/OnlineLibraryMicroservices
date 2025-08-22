FROM cypress/included:12.17.0

ENV DISPLAY=host.docker.internal:0\
    QT_X11_NO_MITSHM=1 \
    _X11_NO_MITSHM=1 \
    _MITSHM=0

RUN rm -f /etc/apt/sources.list.d/google-chrome.list || true && \
    apt-get update && \
    apt-get install -y \
    xauth \
    libgtk2.0-0 \
    libnotify-dev \
    libgconf-2-4 \
    libnss3 \
    libxss1 \
    libasound2 && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY . .

RUN mkdir -p /app/cypress/screenshots /app/cypress/videos && \
    chown -R node:node /app/cypress

RUN mkdir -p /app/cypress-logs && \
    chown -R node:node /app/cypress-logs && \
    chmod -R 777 /app/cypress-logs && \
    touch /app/cypress-logs/cypress.log && \
    chmod 666 /app/cypress-logs/cypress.log

RUN npm install pg

RUN chown -R node /app
USER node

CMD ["npx", "cypress", "open"]


