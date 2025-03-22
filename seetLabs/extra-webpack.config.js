module.exports = {
    module: {
      rules: [
        {
          test: /\.html$/i,
          use: ['raw-loader']
        },
      ],
    },
    resolve: {
        extensions: [".ts", ".js", ".html"], // Ensure Webpack recognizes .html files
    },
  };