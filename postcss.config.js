module.exports = {
  plugins: {
    tailwindcss: {},
    autoprefixer: {},
    'postcss-hash': {
      algorithm: 'sha256',
      trim: 20,
      manifest: './src/main/resources/postcss-manifest.json'
  },
  },
}
