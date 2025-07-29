// cypress/support/logs.js
const fs = require('fs');
const LOG_FILE = '/app/cypress-logs/cypress.log';

// Init + write
fs.writeFileSync(LOG_FILE, '=== Start logs ===\n', { flag: 'w' });

Cypress.on('log:added', (log) => {
  fs.appendFileSync(LOG_FILE, `[${new Date().toISOString()}] ${log.message}\n`);
});




