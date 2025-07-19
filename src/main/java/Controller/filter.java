package Controller;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebFilter(filterName = "MyUrlFilter", urlPatterns = {"/*"})
public class filter implements Filter {

    private static final boolean debug = true;
    private FilterConfig filterConfig = null;

    public filter() {
    }

    private void doBeforeProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (debug) {
            log("MyUrlFilter:DoBeforeProcessing");
        }
    }

    private void doAfterProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (debug) {
            log("MyUrlFilter:DoAfterProcessing");
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        if (debug) {
            log("MyUrlFilter:doFilter()");
        }

        doBeforeProcessing(request, response);

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String url = httpServletRequest.getServletPath();

        // ✅ FIX: Chặn truy cập trực tiếp file .jsp, .html, .js
        if (url.endsWith(".jsp") ) {
            httpServletResponse.sendRedirect("home");
            return;
        }

        // ✅ FIX: Cho phép các file tĩnh CSS, images, fonts
        if (url.startsWith("/css/") || url.startsWith("/js/") ||
                url.startsWith("/images/") || url.startsWith("/fonts/") ||
                url.startsWith("/assets/") || url.contains(".css") ||
                url.contains(".png") || url.contains(".jpg") ||
                url.contains(".jpeg") || url.contains(".gif") ||
                url.contains(".ico") || url.contains(".woff") ||
                url.contains(".ttf")) {
            chain.doFilter(request, response);
            return;
        }

        Throwable problem = null;
        try {
            // ✅ FIX: Chỉ gọi chain.doFilter() một lần
            chain.doFilter(request, response);
        } catch (Throwable t) {
            problem = t;
            t.printStackTrace();
        }

        doAfterProcessing(request, response);

        // ✅ FIX: Xử lý exception
        if (problem != null) {
            if (problem instanceof ServletException) {
                throw (ServletException) problem;
            }
            if (problem instanceof IOException) {
                throw (IOException) problem;
            }
            sendProcessingError(problem, response);
        }
    }

    /**
     * Return the filter configuration object for this filter.
     */
    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**
     * Destroy method for this filter
     */
    @Override
    public void destroy() {
        // Cleanup code if needed
    }

    /**
     * Init method for this filter
     */
    @Override
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        if (filterConfig != null) {
            if (debug) {
                log("MyUrlFilter:Initializing filter");
            }
        }
    }

    /**
     * Return a String representation of this object.
     */
    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("MyUrlFilter()");
        }
        StringBuilder sb = new StringBuilder("MyUrlFilter(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
    }

    private void sendProcessingError(Throwable t, ServletResponse response) {
        String stackTrace = getStackTrace(t);

        if (stackTrace != null && !stackTrace.equals("")) {
            try {
                response.setContentType("text/html");
                PrintStream ps = new PrintStream(response.getOutputStream());
                PrintWriter pw = new PrintWriter(ps);
                pw.print("<html>\n<head>\n<title>Error</title>\n</head>\n<body>\n");
                pw.print("<h1>The resource did not process correctly</h1>\n<pre>\n");
                pw.print(stackTrace);
                pw.print("</pre></body>\n</html>");
                pw.close();
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            try {
                PrintStream ps = new PrintStream(response.getOutputStream());
                t.printStackTrace(ps);
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static String getStackTrace(Throwable t) {
        String stackTrace = null;
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
            sw.close();
            stackTrace = sw.getBuffer().toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return stackTrace;
    }

    public void log(String msg) {
        if (filterConfig != null) {
            filterConfig.getServletContext().log(msg);
        }
    }
}
