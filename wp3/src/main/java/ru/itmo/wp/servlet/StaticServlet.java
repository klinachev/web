package ru.itmo.wp.servlet;

import com.google.gson.Gson;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;


public class StaticServlet extends HttpServlet {

    private final List<Message> messages = new ArrayList<>();


    private File getFileFromName(String uri) {
        File file = new File(getServletContext().getRealPath(""));
        file = new File(file, "../../src/main/webapp/static/" + uri);
        if (!file.isFile()) {
            file = new File(getServletContext().getRealPath("/static/" + uri));
        }
        return file;
    }

    private static class Message {
        String user, text;

        public Message(String username, String text) {
            this.user = username;
            this.text = text;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        String uri = request.getRequestURI();
        HttpSession session = request.getSession();
        switch (uri) {
            case "/message/auth": {
                String username = request.getParameter("user");
                if (username == null) {
                    username = (String) session.getAttribute("username");
                    if (username == null)
                        username = "";
                }
                username =  URLDecoder.decode(username, "UTF-8");
                session.setAttribute("username", username);
                response.getWriter().print(new Gson().toJson(username));
                break;
            }
            case "/message/findAll": {
//                response.getWriter().print(new Gson().toJson(messages));
                response.getOutputStream().write(new Gson().toJson(messages).getBytes(StandardCharsets.UTF_8));
                break;
            }
            case "/message/add": {
                String text = URLDecoder.decode(request.getParameter("text"), "UTF-8");
                String username = (String) session.getAttribute("username");
                messages.add(new Message(username, text));
                break;
            }
        }
        response.getWriter().flush();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] uris = request.getRequestURI().split("\\+");
        for (String path : uris) {
            File file = getFileFromName(path);
            if (!file.isFile()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
        }
        response.setContentType(getContentTypeFromName(uris[0]));
        OutputStream outputStream = response.getOutputStream();
        for (String path : uris) {
            File file = getFileFromName(path);
            Files.copy(file.toPath(), outputStream);
        }
        outputStream.flush();
    }

    private String getContentTypeFromName(String name) {
        name = name.toLowerCase();

        if (name.endsWith(".png")) {
            return "image/png";
        }

        if (name.endsWith(".jpg")) {
            return "image/jpeg";
        }

        if (name.endsWith(".html")) {
            return "text/html";
        }

        if (name.endsWith(".css")) {
            return "text/css";
        }

        if (name.endsWith(".js")) {
            return "application/javascript";
        }

        throw new IllegalArgumentException("Can't find content type for '" + name + "'.");
    }
}
