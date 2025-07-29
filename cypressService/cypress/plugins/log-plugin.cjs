

const fs = require('fs');
const path = require('path');

module.exports = (on, config) => {
  const logPath = path.resolve('/app/cypress-logs/cypress.log');

  // Initialization before all tests
  on('before:run', () => {
    try {
      fs.mkdirSync(path.dirname(logPath), { recursive: true });
      fs.writeFileSync(logPath, '=== Logs started ===\n', { mode: 0o666 });
    } catch (error) {
      console.error('Log initialization error:', error);
    }
  });

  // Log management
  on('task', {
    log(message) {
      try {
        fs.appendFileSync(logPath, `[${new Date().toISOString()}] ${message}\n`);
        return true;
      } catch (error) {
        console.error('Log write error:', error);
        return false;
      }
    }
  });

  return config;
};