path = require('path')
{ WebpackManifestPlugin }  = require("webpack-manifest-plugin")

WMPSettings = 
    fileName: path.resolve(__dirname,'src/main/resources/webpack-manifest.json')

module.exports = 
    mode: 'development'
    plugins: [
        new WebpackManifestPlugin(WMPSettings)
    ]
    entry: 
        index: 
            import: [
                path.resolve(__dirname,'src/main/frontend/index.js')
            ]
            filename: 'js/[name].js'
        react:
            import: [
                path.resolve(__dirname,'src/main/frontend/react/react.js')
            ]
            filename: 'js/[name].js'
    output: 
        path: path.resolve(__dirname,'src/main/resources/static/dist')
        publicPath: 'dist/'
    optimization:
        moduleIds: 'deterministic'
    devtool: false
    module:
        rules: [
            {
                test: /\.(js|jsx)$/
                include: path.resolve(__dirname, 'src') 
                use: {
                    loader: 'babel-loader'
                    options: {
                        presets: ['@babel/preset-env', '@babel/preset-react']
                    }
                }
                resolve: {
                    extensions: ['.js', '.jsx']
                }
            }
        ]
    
