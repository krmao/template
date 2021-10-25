/**
 * 使用本地服务代理
 * 处理跨域问题
 */
const koaRouter = require("koa-router");
const next = require("next");
const koa = require("koa"); // 开启一个本地服务
const kos2Connect = require("koa2-connect"); // 使得在 Koa 中可以使用 express 社区的中间件(比如 http-proxy-middleware), 起到了一个中转或者适配的作用
const {createProxyMiddleware} = require("http-proxy-middleware"); // 代理: 使得所有指向 localhost:3000/api 的请求全部重新指向 xxx.com/api
const nextApp = next({dev: process.env.NODE_ENV !== "production"});
const {APP_DOMAIN_PROD, APP_REQUEST_PATH_PREFIX} = require("./src/basic/basic-constants");

const port = process.argv[2] === "-p" && !!process.argv[3] ? process.argv[3] : 8000;

nextApp.prepare().then(() => {
    // 等待 nestjs 页面编译完成
    const koaServer = new koa(); // 创建一个 Koa 对象表示 web app 本身
    const koaServerRouter = new koaRouter();

    koaServer.use(async (context, next) => {
        // 对于任何请求，app将调用该异步函数处理请求
        // console.log("koaServer context:", context, "next:", next);
        if (context.url.startsWith(APP_REQUEST_PATH_PREFIX)) {
            context.respond = false;
            await kos2Connect(
                createProxyMiddleware({target: APP_DOMAIN_PROD, changeOrigin: true, pathRewrite: {"^/api": ""}})
            )(context, next); // 等待代理执行完成
        } else {
            await next(); // 等待 nextjs 执行完成
        }
    });

    // https://github.com/koajs/router/issues/76#issuecomment-613715351
    koaServerRouter.all("(.*)", async (context) => {
        await nextApp.getRequestHandler()(context.req, context.res); // 等待 nextjs 执行完成
    });

    koaServer.use(koaServerRouter.routes());

    koaServer.listen(port, () => {
        console.log(`> Ready on http://localhost:${port}`);
    });
});
