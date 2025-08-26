
const { defineConfig } = require('cypress');
const { Pool } = require('pg');

module.exports = defineConfig({
  e2e: {
    specPattern: '/app/cypress/e2e/*.cy.{js,jsx,ts,tsx}',
    supportFile: false,
    video: true,
    videoCompression: 15,
    screenshotOnRunFailure: true,
    trashAssetsBeforeRuns: false,
    defaultCommandTimeout: 30000,
    chromeWebSecurity: false,
    experimentalInteractiveRunEvents: true,
    experimentalSessionAndOrigin: true,
    env: {
      DISPLAY: process.env.DISPLAY || ':0',
      QT_X11_NO_MITSHM: 1,
    },
    setupNodeEvents(on, config) {
      require('./cypress/plugins/log-plugin.cjs')(on, config);

      // Set all environment variables before tasks
      config.env = {
        ...config.env,
        apiUrl: 'http://webapp:5173',
        dbProfilTestHost: 'db-profil-test',
        dbProfilTestDatabase: 'ProfilServiceTestDB',
        dbCartTestHost: 'db-cart-test',
        dbCartTestDatabase: 'CartServiceTestDB',
        dbPaymentTestHost: 'db-payment-test',
        dbPaymentTestDatabase: 'PaymentServiceTestDB',
        dbOrderTestHost: 'db-order-test',
        dbOrderTestDatabase: 'OrderServiceTestDB',
        user: 'postgres',
        password: 'mypass',
        port: 5432,
      };

      // Declare all tasks
      on('task', {
        async queryInitDb({ host, database, query, values = [] }) {
          const pool = new Pool({
            host,
            database,
            user: config.env.user,
            password: config.env.password,
            port: config.env.port,
          });
          try {
            const res = await pool.query(query, values);
            await pool.end();
            return res.rowCount ?? res.rows;
          } catch (error) {
            console.error(`Database query failed on ${database}:${config.env.port}:`, error);
            return null;
          }
        },

        async queryFetchUserData({ email }) {
          const pool = new Pool({
            host: config.env.dbProfilTestHost,
            database: config.env.dbProfilTestDatabase,
            user: config.env.user,
            password: config.env.password,
            port: config.env.port,
          });
          const query = `
            SELECT u.id, u.first_name, u.last_name, u.email,
                   up.street, up.city, up.postal_code, up.province, up.country
            FROM users u
            JOIN user_profiles up ON u.id = up.user_id
            WHERE u.email = $1
          `;
          try {
            const res = await pool.query(query, [email]);
            await pool.end();
            return res.rows[0] || null;
          } catch (err) {
            await pool.end();
            throw err;
          }
        },

        async verifyEmptyCartTable() {
          const pool = new Pool({
            host: config.env.dbCartTestHost,
            database: config.env.dbCartTestDatabase,
            user: config.env.user,
            password: config.env.password,
            port: config.env.port,
          });
          try {
            const result = await pool.query('SELECT COUNT(*) FROM carts');
            const count = parseInt(result.rows[0].count);
            await pool.end();
            return {
              isEmpty: count === 0,
              count: count,
            };
          } catch (error) {
            await pool.end();
            throw error;
          }
        },

        async verifyInvoiceAddedInInvoiceTable() {
          const pool = new Pool({
            host: config.env.dbPaymentTestHost,
            database: config.env.dbPaymentTestDatabase,
            user: config.env.user,
            password: config.env.password,
            port: config.env.port,
          });

          try {
            const result = await pool.query('SELECT COUNT(*) FROM invoices');
            const count = parseInt(result.rows[0].count);
            await pool.end();
            return {
              hasInvoices: count > 0,
              count: count
            };
          } catch (error) {
            await pool.end();
            throw error;
          }
        },

        async verifyOrderForUser({ userId }) {
          const pool = new Pool({
            host: config.env.dbOrderTestHost,
            database: config.env.dbOrderTestDatabase,
            user: config.env.user,
            password: config.env.password,
            port: config.env.port,
          });

          try {
            const result = await pool.query(
              'SELECT COUNT(*) FROM orders WHERE user_id = $1',
              [userId]
            );
            return {
              exists: parseInt(result.rows[0].count) > 0,
              count: parseInt(result.rows[0].count)
            };
          } catch (error) {
            throw error;
          } finally {
            await pool.end();
          }
        },

        async verifyOrderLines({ userId, bookId, quantity }) {
          const pool = new Pool({
            host: config.env.dbOrderTestHost,
            database: config.env.dbOrderTestDatabase,
            user: config.env.user,
            password: config.env.password,
            port: config.env.port,
          });

          try {
            const result = await pool.query(`
                SELECT COUNT(*) 
                FROM order_lines ol
                JOIN orders o ON ol.order_id = o.id
                WHERE o.user_id = $1 
                AND ol.book_id = $2
                AND ol.quantity = $3
            `, [userId, bookId, quantity]);

            return {
              exists: parseInt(result.rows[0].count) > 0,
              count: parseInt(result.rows[0].count),
              userId: userId,
              bookId: bookId,
              quantity: quantity
            };
          } catch (error) {
            throw error;
          } finally {
            await pool.end();
          }
        },

        async countUserDeliveries({ userId }) {
          const pool = new Pool({
            host: config.env.dbOrderTestHost,
            database: config.env.dbOrderTestDatabase,
            user: config.env.user,
            password: config.env.password,
            port: config.env.port
          });

          try {
            const res = await pool.query('SELECT COUNT(*) AS count FROM deliveries');
            const count = parseInt(res.rows[0].count, 10);
            return {
              count,
              hasDeliveries: count > 0,
              userId
            };
          } catch (error) {
            throw error;
          } finally {
            await pool.end();
          }
        },

        async cleanProfilTestDB() {
          const pool = new Pool({
            host: config.env.dbProfilTestHost,
            database: config.env.dbProfilTestDatabase,
            user: config.env.user,
            password: config.env.password,
            port: config.env.port
          });

          try {
            await pool.query('TRUNCATE TABLE users, user_profiles RESTART IDENTITY CASCADE');
            return { success: true };
          } catch (error) {
            return { success: false, error: error.message };
          } finally {
            await pool.end();
          }
        },

        async cleanCartTestDB() {
          const pool = new Pool({
            host: config.env.dbCartTestHost,
            database: config.env.dbCartTestDatabase,
            user: config.env.user,
            password: config.env.password,
            port: config.env.port
          });

          try {
            await pool.query('TRUNCATE TABLE carts, cart_items RESTART IDENTITY CASCADE');
            return { success: true };
          } catch (error) {
            return { success: false, error: error.message };
          } finally {
            await pool.end();
          }
        },

        async cleanPaymentTestDB() {
          const pool = new Pool({
            host: config.env.dbPaymentTestHost,
            database: config.env.dbPaymentTestDatabase,
            user: config.env.user,
            password: config.env.password,
            port: config.env.port
          });

          try {
            await pool.query('TRUNCATE TABLE invoices RESTART IDENTITY CASCADE');
            return { success: true };
          } catch (error) {
            return { success: false, error: error.message };
          } finally {
            await pool.end();
          }
        },

        async cleanOrderTestDB() {
          const pool = new Pool({
            host: config.env.dbOrderTestHost,
            database: config.env.dbOrderTestDatabase,
            user: config.env.user,
            password: config.env.password,
            port: config.env.port
          });

          try {
            await pool.query('TRUNCATE TABLE orders, order_lines, deliveries RESTART IDENTITY CASCADE');
            return { success: true };
          } catch (error) {
            return { success: false, error: error.message };
          } finally {
            await pool.end();
          }
        },
      });

      return config;
    },
  },
  viewportWidth: 1920,
  viewportHeight: 1080,
  numTestsKeptInMemory: 5,
});