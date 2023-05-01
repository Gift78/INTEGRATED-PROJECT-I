const { defineConfig } = require("cypress");

module.exports = defineConfig({
  e2e: {
    specPattern: "cypress/e2e/**/*.{cy,spec}.{js,jsx,ts,tsx}",
    baseUrl: "http://ip22kw2.sit.kmutt.ac.th/",
    experimentalSessionAndOrigin: true,
  },
});
