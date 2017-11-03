package com.smart.housekeeper.base.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * http://www.jdon.com/40381
 * <p>
 * Last-Modified和Expires针对浏览器，而ETag则与客户端无关，所以可适合REST架构中。两者都应用在浏览器端的区别是：Expires日期到达前，浏览器不会再发出新的请求，除非用户按浏览器的刷新，所以，Last-Modified和Expires基本是降低浏览器向服务器发出请求的次数，而ETag更侧重客户端和服务器之间联系。
 * 先谈Last-Modified和Expires，最新的Tomcat 7 将ExpireFilter加入其容器中，这样，Java WEB也可以象Apache的Mod_expire模块一样对Http头部进行统一设置了，不过它只对响应文档类型进行统一设置判断，如text/html或text/image 或/css等等，如果想对个别URL输出的jsp进行定制就不行，urlrewrite据说是可以，但是要把URL在其配置文件再配置一下，麻烦，一旦jsp改动影响面大，还有一个问题就是web.xml配置了Tomcat 7容器的ExpireFilter，与容器耦合，移植性差(移植到Resin就不行了)。
 * 所以，我在jivejdon 4.2最新版本中，通过加入下面一段代码在服务器端对来自客户端的Last-Modified以及当前时间进行判断，如未过期，response.setStatus设为304，可以终止后面的各种Jsp界面计算，直接返回浏览器一个304的响应包，JSP页面也不会输出到客户端，将带宽节省给更加需要互动实时性的请求。
 * 再谈谈ETag，ETag定义：RFC2616(也就是HTTP/1.1)中没有说明ETag该是什么格式的，只要确保用双引号括起来就行了，所以你可以用文件的hash，甚至是直接用Last-Modified，以下是服务器端返回的格式：
 * ETag: "50b1c1d4f775c61:df3" 客户端向服务端发出的请求：If-None-Match: W/"50b1c1d4f775c61:df3" 这样，在J2EE/JavaEE服务器端，我们判断如果ETag没改变也是返回状态304，起到类似Last-Modified和Expires效果。
 * 与Last-Modified和Expires区别是：如果过了Expires日期，服务器肯定会再次发出JSP完整响应；或者用户强按浏览器的刷新按钮，服务器也必须响应，apache等静态页面输出也是这样，但是这时动态页面就发挥了作用，如果JSP涉及的业务领域模型还是没有更新，和原来一样，那么就不必再将动态页面输出了(浏览器客户端已有一份)，从Etag中获取上次设置的领域模型对象修改日期，和现在内存中领域模型(In-memory Model)修改日期进行比较，如果修改日期一致，表示领域模型没有被更新过，那么返回响应包304，浏览器将继续用本地缓存的该页面，再次节省了带宽传输。
 * 通过上述Expire和 Etag 两次缓存，可以大大降低服务器的响应负载，如果你的应用不是状态集中并发修改和实时输出，而是分散修改然后分发，如个人空间 个人博客(每个人只是修改它们自己的状态，不影响全局)或QQ类似个人工具，那么采取这样的方法效果非常明显，实际就是一种动态页面静态化技术，但比通常事先进行页面静态化要灵活强大。
 * InfoQ的那篇:http://www.infoq.com/articles/etags还用MD5计算放入其中，Md5计算稍微复杂点，负载大了点，有的人结合Hibernate或数据库触发器来判断数据库数据是否更新，以决定Etag的更新，这将表现层和持久层耦合在一起，由于JiveJdon采取的是MDD/DDD模型驱动架构，表现层的Etag更新是根据中间业务层的模型对象修改日期来决定，不涉及数据库层，而且起到服务器缓存的更新和http的Etag更新一致的效果，在松耦合设计和性能上取得综合平衡。
 */
public class HKCacheControlManager {

    public static void checkHeaderCache(HttpServletRequest request, HttpServletResponse response, long modelLastModifiedDate, long cachePeriod) {
        request.setAttribute("myExpire", cachePeriod);

        // convert seconds to ms.
        long header = request.getDateHeader("If-Modified-Since");
        long now = System.currentTimeMillis();
        if (header > 0 && cachePeriod > 0) {
            if (modelLastModifiedDate > header) {
                // duration = 0; // reset
                response.setStatus(HttpServletResponse.SC_OK);
                return;
            }
            if (header + cachePeriod > now) {
                // during the period happened modified
                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                return;
            }
        }

        // if over expire data, see the Etags;
        // ETags if ETags no any modified
        String previousToken = request.getHeader("If-None-Match");
        if (previousToken != null && previousToken.equals(Long.toString(modelLastModifiedDate))) {
            // not modified
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            return;
        }
        // if th model has modified , setup the new modified date
        response.setHeader("ETag", Long.toString(modelLastModifiedDate));
        setRespHeaderCache(cachePeriod, request, response);

    }

    private static void setRespHeaderCache(long cachePeriod, HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("myExpire", cachePeriod);

        response.setHeader("Cache-Control", "max-age=" + cachePeriod);
        response.setStatus(HttpServletResponse.SC_OK);
        response.addDateHeader("Last-Modified", System.currentTimeMillis());
        response.addDateHeader("Expires", System.currentTimeMillis() + cachePeriod);
    }
}
