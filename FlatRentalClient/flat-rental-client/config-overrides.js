const { override, fixBabelImports, addLessLoader } = require('customize-cra');

module.exports = override(
  fixBabelImports('import', {
    libraryName: 'antd',
    libraryDirectory: 'es',
    style: true,
  }),
  addLessLoader({
    javascriptEnabled: true,
      modifyVars: {
          "@primary-color": "#d35400",
          "@layout-body-background": "#FFFFFF",
          "@layout-header-background": "#FFFFFF",
          "@layout-footer-background": "#FFFFFF"
      },
  }),
);
