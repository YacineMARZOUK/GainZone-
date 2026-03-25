/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}",
  ],
  theme: {
    extend: {
      colors: {
        'dark-bg': '#121212',    // Fond sombre Premium
        'volt': '#CDFB47',       // Lime Électrique (Accent)
        'dim-gray': '#5B5B5B',   // Texte secondaire
      }
    },
  },
  plugins: [],
}
