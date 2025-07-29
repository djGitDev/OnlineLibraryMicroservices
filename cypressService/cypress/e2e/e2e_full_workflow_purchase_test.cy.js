
describe('Workflow Processing Test', () => {
    beforeEach(() => {
        cy.task('log', `Test start: ${Cypress.currentTest.title}`);
        cy.visit('http://webapp:5173');
    });

    afterEach(() => {
        cy.task('log', 'Forced database cleanup').then(() => {
            // 1. Clean invoices first (without verification)
            cy.task('cleanPaymentTestDB', { force: true }).then(() => {
                // 2. Sequential cleanup of the rest
                return cy.task('cleanProfilTestDB', { force: true });
            }).then(() => {
                return cy.task('cleanCartTestDB', { force: true });
            }).then(() => {
                return cy.task('cleanOrderTestDB', { force: true });
            }).then(() => {
                cy.task('log', '✅ Forced cleanup completed');
            });
        });
    });

    it('should process workflow and display API response', () => {
        let extractedEmail;
        let userId;
        let books = [];

        // 1. Initial verification
        cy.task('log', '[TEST] Step 1: Main page').then(() => {
            cy.get('h1').should('contain', 'Supported Workflows List');
            cy.get('.file-list').should('exist');
        });

        // 2. File selection
        cy.task('log', '[TEST] Step 2: Selection').then(() => {
            cy.get('.file-list li:first-child .file-button').click();
        });

        // 3. Content verification + email extraction (corrected version)
        cy.task('log', '[TEST] Step 3: Email extraction').then(() => {
            cy.get('.selected-content h2').should('contain', 'Selected File:');
            cy.get('.json-box').should('exist');

            return cy.get('pre.json-box').invoke('text').then((text) => {
                const content = JSON.parse(text);
                extractedEmail = content[0].user.email;
                books = content[4].books;

                // Async log
                return Cypress.Promise.all([
                    cy.task('log', `📚 Books (${books.length}): ${JSON.stringify(books)}`),
                    cy.task('log', `Extracted email: ${extractedEmail}`),
                    ...books.map((book, index) =>
                        cy.task('log', `Book ${index + 1}: ID=${book.book_id}, Quantity=${book.quantity}`)
                    )
                ]);
            });
        });

        // 4. Workflow processing
        cy.task('log', '[TEST] Step 4: Process').then(() => {
            return cy.get('.action-button.process').click().wait(3000);
        });

        // 5. API verification
        cy.task('log', '[TEST] Step 5: API response').then(() => {
            return cy.get('.selected-content h2', { timeout: 10000 })
                .should('contain', 'API Response');
        });

        // 6. Return to list
        cy.task('log', '[TEST] Step 6: Return').then(() => {
            return cy.get('.action-button.cancel').click()
                .get('h1').should('contain', 'Supported Workflows List');
        });

        // 7. Delayed database verification (corrected version)
        cy.task('log', '[TEST] Step 7: Database verification').then(() => {
            return cy.task('queryFetchUserData', { email: extractedEmail }).then((user) => {
                expect(user).to.exist;
                expect(user.email).to.equal(extractedEmail);
                userId = user.id;

                return cy.task('verifyEmptyCartTable');
            }).then((cartResult) => {
                expect(cartResult.isEmpty, 'Cart should be empty').to.be.true;
                return cy.task('verifyInvoiceAddedInInvoiceTable');
            }).then((invoiceResult) => {
                expect(invoiceResult.hasInvoices, 'Invoice should exist').to.be.true;
                return cy.task('verifyOrderForUser', { userId });
            }).then((orderResult) => {
                expect(orderResult.exists, 'Order should exist').to.be.true;
            });
        });

        books.forEach((book) => {
            cy.task('verifyOrderLines', {
                userId,
                bookId: book.book_id,
                quantity: book.quantity
            }).then((result) => {
                expect(result.count, `Lines found for book ${book.book_id}`).to.be.greaterThan(0);
            });
        });

        cy.task('countUserDeliveries', { userId }).then((result) => {
            expect(result.count, 'Delivery count').to.be.greaterThan(0);
            expect(result.hasDeliveries, 'User should have deliveries').to.be.true;
        });
    });
});