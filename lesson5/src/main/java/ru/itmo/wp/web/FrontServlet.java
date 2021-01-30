package ru.itmo.wp.web;

import com.google.common.base.Strings;
import freemarker.template.*;
import ru.itmo.wp.web.exception.NotFoundException;
import ru.itmo.wp.web.exception.RedirectException;
import ru.itmo.wp.web.page.IndexPage;
import ru.itmo.wp.web.page.NotFoundPage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class FrontServlet extends HttpServlet {
    private static final String BASE_PACKAGE = FrontServlet.class.getPackage().getName() + ".page";
    private static final String DEFAULT_ACTION = "action";

    private Configuration sourceConfiguration;
    private Configuration targetConfiguration;

    private Configuration newFreemarkerConfiguration(String templateDirName, boolean debug) throws ServletException {
        File templateDir = new File(templateDirName);
        if (!templateDir.isDirectory()) {
            return null;
        }

        Configuration configuration = new Configuration(Configuration.VERSION_2_3_30);
        try {
            configuration.setDirectoryForTemplateLoading(templateDir);
        } catch (IOException e) {
            throw new ServletException("Can't create freemarker configuration [templateDir=" + templateDir + "]");
        }
        configuration.setDefaultEncoding(StandardCharsets.UTF_8.name());
        configuration.setTemplateExceptionHandler(debug ? TemplateExceptionHandler.HTML_DEBUG_HANDLER :
                TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setLogTemplateExceptions(false);
        configuration.setWrapUncheckedExceptions(true);

        return configuration;
    }

    @Override
    public void init() throws ServletException {
        sourceConfiguration = newFreemarkerConfiguration(
                getServletContext().getRealPath("/") + "../../src/main/webapp/WEB-INF/templates", true);
        targetConfiguration = newFreemarkerConfiguration(getServletContext().getRealPath("WEB-INF/templates"), false);
        sourceConfiguration.setLocale(Locale.ENGLISH);
        targetConfiguration.setLocale(Locale.ENGLISH);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    private static boolean isLang(String lang) {
        return lang != null && lang.matches("[a-z][a-z]");
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String lang = request.getParameter("lang");
        if (isLang(lang)) {
            session.setAttribute("lang", lang);
        }
        Route route = Route.newRoute(request);
        try {
            process(route, request, response);
        } catch (NotFoundException e) {
            try {
                process(Route.newNotFoundRoute(), request, response);
            } catch (NotFoundException notFoundException) {
                throw new ServletException(notFoundException);
            }
        }
    }

    private boolean tryInvokeMethod(Method method, Object page, Map<String, Object> view, HttpServletRequest request)
            throws InvocationTargetException, IllegalAccessException {
        method.setAccessible(true);
        Class<?>[] parameters = method.getParameterTypes();
        if (parameters.length > 2) {
            return false;
        }
        if (parameters.length == 2) {
            if (parameters[0].isAssignableFrom(Map.class)
                    && parameters[1].isAssignableFrom(HttpServletRequest.class)) {
                method.invoke(page, view, request);
            } else if (parameters[0].isAssignableFrom(HttpServletRequest.class)
                    && parameters[1].isAssignableFrom(Map.class)) {
                method.invoke(page, request, view);
            } else {
                return false;
            }
        } else if (parameters.length == 1) {
            if (parameters[0].isAssignableFrom(Map.class)) {
                method.invoke(page, view);
            } else if (parameters[0].isAssignableFrom(HttpServletRequest.class)) {
                method.invoke(page, request);
            } else {
                return false;
            }
        } else {
            method.invoke(page);
        }
        return true;
    }

    private void process(Route route, HttpServletRequest request,
                         HttpServletResponse response) throws NotFoundException, ServletException, IOException {
        Class<?> pageClass;
        try {
            pageClass = Class.forName(route.getClassName());
        } catch (ClassNotFoundException e) {
            throw new NotFoundException();
        }

        Object page;
        try {
            page = pageClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ServletException("Can't create page [pageClass=" + pageClass + "]");
        }

        Map<String, Object> view = new HashMap<>();

        boolean methodInvoked = false;
        classCycle : for (Class<?> clazz = pageClass; clazz != null; clazz = clazz.getSuperclass()) {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method: methods) {
                if (method.getName().equals(route.getAction())) {
                    try {
                        if (tryInvokeMethod(method, page, view, request)) {
                            methodInvoked = true;
                            break classCycle;
                        }
                    } catch (IllegalAccessException ignored) {
                        // ignored
                    } catch (InvocationTargetException e) {
                        Throwable cause = e.getCause();
                        if (cause instanceof RedirectException) {
                            RedirectException redirectException = (RedirectException) cause;
                            response.sendRedirect(redirectException.getTarget());
                            return;
                        }
                    }
                }
            }
        }
        if (!methodInvoked) {
            throw new NotFoundException();
        }

        String lang = (String) request.getSession().getAttribute("lang");
        Template template = null;
        if (lang != null) {
            try {
                template = newTemplate(pageClass.getSimpleName() + "_" + lang + ".ftlh");
            } catch (TemplateNotFoundException ignore) {
                // ignore
            }
        }
        if (template == null) {
            template = newTemplate(pageClass.getSimpleName() + ".ftlh");
        }
        response.setContentType("text/html");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        try {
            template.process(view, response.getWriter());
        } catch (TemplateException e) {
            throw new ServletException("Can't render template [pageClass=" + pageClass + "]", e);
        }
    }

    private Template newTemplate(String templateName) throws ServletException, TemplateNotFoundException {
        Template template = null;

        if (sourceConfiguration != null) {
            try {
                template = sourceConfiguration.getTemplate(templateName);
            } catch (TemplateNotFoundException e) {
                throw e;
            } catch (IOException e) {
                throw new ServletException("Can't load template [templateName=" + templateName + "]", e);
            }
        }

        if (template == null && targetConfiguration != null) {
            try {
                template = targetConfiguration.getTemplate(templateName);
            } catch (TemplateNotFoundException ignored) {
                // No operations.
            } catch (IOException e) {
                throw new ServletException("Can't load template [templateName=" + templateName + "]", e);
            }
        }

        if (template == null) {
            throw new ServletException("Can't find template [templateName=" + templateName + "]");
        }

        return template;
    }

    private static class Route {
        private final String className;
        private final String action;

        private Route(String className, String action) {
            this.className = className;
            this.action = action;
        }

        private String getClassName() {
            return className;
        }

        private String getAction() {
            return action;
        }

        private static Route newNotFoundRoute() {
            return new Route(
                    NotFoundPage.class.getName(),
                    DEFAULT_ACTION
            );
        }

        private static Route newIndexRoute() {
            return new Route(
                    IndexPage.class.getName(),
                    DEFAULT_ACTION
            );
        }

        private static Route newRoute(HttpServletRequest request) {
            String uri = request.getRequestURI();

            List<String> classNameParts = Arrays.stream(uri.split("/"))
                    .filter(part -> !Strings.isNullOrEmpty(part))
                    .collect(Collectors.toList());

            if (classNameParts.isEmpty()) {
                return newIndexRoute();
            }

            StringBuilder simpleClassName = new StringBuilder(classNameParts.get(classNameParts.size() - 1));
            int lastDotIndex = simpleClassName.lastIndexOf(".");
            simpleClassName.setCharAt(lastDotIndex + 1,
                    Character.toUpperCase(simpleClassName.charAt(lastDotIndex + 1)));
            classNameParts.set(classNameParts.size() - 1, simpleClassName.toString());

            String className = BASE_PACKAGE + "." + String.join(".", classNameParts) + "Page";

            String action = request.getParameter("action");
            if (Strings.isNullOrEmpty(action)) {
                action = DEFAULT_ACTION;
            }

            return new Route(className, action);
        }
    }
}
