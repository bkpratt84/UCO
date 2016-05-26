package general;

import java.io.IOException;
import java.io.Serializable;
import java.security.Principal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

public class LoginFilter implements Filter {

    @Resource(name = "jdbc/cs_db_Datasource")
    private DataSource ds;
    String username;
    Boolean activeUser;

    protected FilterConfig filterConfig;

// Called once when this filter is instantiated. If this is mapped to
// j_security_check, called very first time j_security_check is invoked.
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    public void destroy() {
        this.filterConfig = null;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        Principal principal = req.getUserPrincipal();
        String requestUrl = req.getRequestURI();
        Object redirectUrl = req.getSession().getAttribute("from");

        //Getting the information about the user
        //Is user an active user?
        if (principal != null) {
            username = principal.getName();
        }

        try {

            Connection conn = ds.getConnection();

            try {
                PreparedStatement getValues = conn.prepareStatement("Select * From USER_INFO WHERE LOWER(email) = ? ");
                getValues.setString(1, username);
                ResultSet result = getValues.executeQuery();
                if (result.next()) {
                    activeUser = result.getBoolean("ACTIVE");
                }
            } finally {
                conn.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginFilter.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (requestUrl.endsWith("/sdd-project/index.html")
                || requestUrl.endsWith("/sdd-project/")
                || requestUrl.endsWith("/sdd-project/faces/index.xhtml")) {
            if (req.isUserInRole("admin") && activeUser == true) {
                res.sendRedirect("/sdd-project/faces/admin/index.xhtml");
            } else if (req.isUserInRole("student") && activeUser == true) {
                res.sendRedirect("/sdd-project/faces/students/index.xhtml");
            } else if (req.isUserInRole("instructor") && activeUser == true){
                res.sendRedirect("/sdd-project/faces/instructors/index.xhtml");
            }
            else if (activeUser != null && activeUser == false) {
                req.logout();
                res.sendRedirect("/sdd-project/faces/login.xhtml");
            } else {
                chain.doFilter(request, response);
            }
        } else if (principal == null) {
            req.getSession().setAttribute("from", req.getRequestURI());
            res.sendRedirect("/sdd-project/faces/login.xhtml");
        } else if (redirectUrl != null && redirectUrl.toString() != "") {
            res.sendRedirect(redirectUrl.toString());
        } else {
            chain.doFilter(request, response);
        }

    }
}
