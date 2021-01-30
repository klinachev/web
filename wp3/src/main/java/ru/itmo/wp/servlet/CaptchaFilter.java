package ru.itmo.wp.servlet;

import ru.itmo.wp.util.ImageUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.Random;

public class CaptchaFilter extends HttpFilter {

    private static final String CAPTCHA_IMAGE_REQUEST_NAME = "/captcha";

    private static final String CAPTCHA_PARAMETER_NAME = "captcha";

    private static final String HTML_CAPTCHA = "<!DOCTYPE html>\n" +
            "<html lang=\"en\"\n>" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <title>Codeforces</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "<main>\n" + "\n" +
            "<img src=\"" + CAPTCHA_IMAGE_REQUEST_NAME + "\" alt=\"Captcha\" title=\"Captcha\"/>\n" +
            "            <form method=\"get\">\n" +
            "                <label>Enter captcha:</label>\n" +
            "                <input name=\"" + CAPTCHA_PARAMETER_NAME + "\">\n" +
            "            </form>\n" +
            "    </main>\n" +
            "</body>\n" +
            "</html>\n";


    private void generateCaptcha(HttpSession session, HttpServletResponse response) throws IOException {
        String expectedValue = Integer.toString(new Random().nextInt(900) + 100);
        session.setAttribute("expectedValue", expectedValue);
        response.getWriter().print(HTML_CAPTCHA);
        response.getWriter().flush();
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpSession session = request.getSession();
        Boolean captchaPassed = (Boolean) session.getAttribute("captchaPassed");
        if (captchaPassed == null) {
            session.setAttribute("captchaPassed", false);
            generateCaptcha(session, response);
            return;
        }
        if (!captchaPassed) {
            String expectedValue = (String) session.getAttribute("expectedValue");
            if (request.getRequestURI().equals(CAPTCHA_IMAGE_REQUEST_NAME)) {
                response.getOutputStream().write(ImageUtils.toPng(expectedValue));
                response.getWriter().flush();
                return;
            }
            String captchaValue = request.getParameter(CAPTCHA_PARAMETER_NAME);
            if (captchaValue == null) {
                response.getWriter().print(HTML_CAPTCHA);
                response.getWriter().flush();
                return;
            }
            if(!captchaValue.equals(expectedValue)) {
                generateCaptcha(session, response);
                return;
            }
            session.setAttribute("captchaPassed", true);
        }
        super.doFilter(request, response, chain);
    }
}
