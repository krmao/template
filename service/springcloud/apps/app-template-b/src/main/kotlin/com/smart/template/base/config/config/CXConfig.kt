package com.smart.template.base.config.config

import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.io.File

object CXConfig {
    var VERSION = 1
    var DEFAULT_SQL_ID = -1
    var DEFAULT_PAGE_INDEX = 1
    var DEFAULT_PAGE_SIZE = 20

    // "/usr/local/apache-tomcat-8.0.47/webapps"
    val PROJECT_DIR: File = File((RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request.session.servletContext.getRealPath("/"))

    // "/usr/local/apache-tomcat-8.0.47/webapps/template-files"
    val DEFAULT_FILES_DIR: File = File(PROJECT_DIR.parent, PROJECT_DIR.name + "-files")
        get() {
            if (!field.exists()) field.mkdirs()
            return field
        }

    override fun toString(): String {
        return """
            --
            VERSION:$VERSION
            DEFAULT_PAGE_INDEX:$DEFAULT_PAGE_INDEX
            DEFAULT_PAGE_SIZE:$DEFAULT_PAGE_SIZE
            PROJECT_DIR:${PROJECT_DIR.absolutePath}
            DEFAULT_FILES_DIR:${DEFAULT_FILES_DIR.absolutePath}
            --
            """
    }

}
