import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String redirectUrl = request.getParameter("redirectUrl");

    UserService userService = UserServiceFactory.getUserService();
    String logoutUrl = userService.createLogoutURL(redirectUrl);
    response.sendRedirect(logoutUrl);
  }
}