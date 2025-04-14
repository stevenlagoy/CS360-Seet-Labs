module.exports = {
    module: {
      rules: [
        {
          test: /\.html$/i,
          use: ['raw-loader']
        },
        {
          test: /\.java$/,
          use: ['raw-loader'], // or 'file-loader'
        },
      ],
    },
    resolve: {
        extensions: [".ts", ".js", ".html", ".java"], // Ensure Webpack recognizes .html files
    },
  };