// babel.config.js
//presets: ['@babel/preset-env', '@babel/preset-react'],
module.exports = {
  presets: [
    "@babel/preset-env",
    "@babel/preset-react"
  ],
  plugins: [
    ["@babel/transform-runtime"],
    {
        "regenerator": true
    }  // <= Add it here
  ]  
}
