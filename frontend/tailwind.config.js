export default {
  content: ["./index.html", "./src/**/*.{js,jsx,ts,tsx}"],
  darkMode: 'class',
  theme: {
    extend: {
      colors: {
        brand: { 50:'#eef2ff',500:'#6366f1',600:'#4f46e5',700:'#4338ca' }
      },
      backgroundImage: {
        'hero-gradient': 'radial-gradient(at 20% 20%, #6366f1 0%, transparent 50%), radial-gradient(at 80% 60%, #ec4899 0%, transparent 50%), radial-gradient(at 40% 80%, #06b6d4 0%, transparent 50%)'
      }
    }
  },
  plugins: []
};
