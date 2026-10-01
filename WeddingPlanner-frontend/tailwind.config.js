export default {
  content: [
  './index.html',
  './src/**/*.{js,ts,jsx,tsx}'
],
  theme: {
    extend: {
      colors: {
        wine: {
          50: '#FAF2F3',
          100: '#F2E0E3',
          200: '#E3BFC5',
          300: '#CC919B',
          500: '#8C3A4A',
          600: '#74303D',
          700: '#5E2331',
          800: '#4B1B27',
          900: '#37131C',
        },
        gold: {
          50: '#FBF7EE',
          100: '#F5ECD8',
          200: '#EADBB6',
          300: '#DBC38E',
          400: '#C8A868',
          500: '#B08E4E',
          600: '#8F713A',
          700: '#6D562C',
        },
        ivory: { DEFAULT: '#FAF7F2', 100: '#F4EFE7', 200: '#ECE5D9' },
        surface: '#FFFDFA',
        ink: {
          DEFAULT: '#1F1B1A',
          50: '#F3F1EF',
          100: '#E7E3DF',
          300: '#B5ACA6',
          400: '#8C837D',
          500: '#6B625D',
          600: '#4F4744',
          700: '#3A3331',
        },
        line: { DEFAULT: '#E9E2D8', strong: '#D8CEC1' },
        success: { 50: '#EEF4EF', 100: '#D9E7DC', 600: '#4E7A5B', 700: '#3D6149' },
        warning: { 50: '#FCF4E6', 100: '#F6E3BF', 600: '#B7791F', 700: '#8C5B14' },
        danger: { 50: '#FBEFED', 100: '#F3D6D2', 600: '#B0473D', 700: '#8C372F' },
        bride: { 50: '#F6F1F5', 100: '#EBDFE8', 200: '#D8C3D3', 600: '#7A5673', 700: '#5F4259' },
        groom: { 50: '#EFF3F6', 100: '#DDE6EC', 200: '#BFCEDA', 600: '#4A6478', 700: '#394E5E' },
      },
      fontFamily: {
        serif: ['"Playfair Display"', 'Georgia', 'serif'],
        sans: ['Inter', 'system-ui', 'sans-serif'],
      },
      boxShadow: {
        card: '0 1px 2px rgba(31,27,26,0.04)',
        pop: '0 12px 32px -8px rgba(31,27,26,0.18), 0 2px 6px rgba(31,27,26,0.06)',
        modal: '0 24px 64px -12px rgba(31,27,26,0.28)',
      },
    },
  },
};
