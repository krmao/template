package com.smart.housekeeper.controller;

import com.smart.housekeeper.base.utils.HKCacheControlManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;

@Controller
@RequestMapping("/download")
public class HKDownloadController {

    private ServletContext servletContext;

    @Autowired
    public HKDownloadController(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @RequestMapping(value = "/v0/{fileName}", method = RequestMethod.GET)
    public void download(HttpServletResponse response, @PathVariable("fileName") String fileName) throws IOException {
        fileName += ".json";
        String filePath = servletContext.getRealPath("/WEB-INF/static/files/" + fileName);
        File file = new File(filePath);

        System.out.println("fileName:" + fileName);
        System.out.println("filePath:" + file.getPath());

        if (file.exists()) {
            String mimeType = "application/json"; //URLConnection.guessContentTypeFromName(file.getName());
            //mimeType = mimeType == null ? "application/octet-stream" : mimeType;
            System.out.println("mimeType:" + mimeType);

            response.setContentType(mimeType);

            // "Content-Disposition : inline" will show viewable types [like images/text/pdf/anything viewable by browser] right on browser
            // while others(zip e.g) will be directly downloaded [may provide save as popup, based on your browser setting.]
            response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");

            // "Content-Disposition : attachment" will be directly download, may provide save as popup, based on your browser setting*/
            // response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
            response.setContentLength((int) file.length());

            InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
            FileCopyUtils.copy(inputStream, response.getOutputStream());
            return;
        }
        response.setContentType("application/json");
        OutputStream outputStream = response.getOutputStream();
        outputStream.write("Sorry. The file you are looking for does not exist".getBytes(Charset.forName("UTF-8")));
        outputStream.close();
    }

    private static long modelLastModifiedDate = System.currentTimeMillis();

    @RequestMapping(value = "/v1/{fileName}", method = RequestMethod.GET)
    public void downloadWithCustomCache(HttpServletRequest request, HttpServletResponse response, @PathVariable("fileName") String fileName) throws IOException {
        HKCacheControlManager.checkHeaderCache(request, response, modelLastModifiedDate, 30);
        download(response, fileName);
    }

    @RequestMapping(value = "/v2/{fileName}", method = RequestMethod.GET)
    public void downloadByRedirect(HttpServletResponse response, @PathVariable("fileName") String fileName) throws IOException {
        response.sendRedirect("/static/files/" + fileName + ".json");
    }

    @RequestMapping(value = "/v3/{fileName}", method = RequestMethod.GET)
    public void downloadByForward(HttpServletRequest request, HttpServletResponse response, @PathVariable("fileName") String fileName) throws IOException, ServletException {
        request.getRequestDispatcher("/static/files/" + fileName + ".json").forward(request, response);
    }
}
