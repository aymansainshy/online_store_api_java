# Online Store Api Java (Spring Boot)

## Dependencies :

* Project Lombok : For code generations.


## NOTS :
* In Spring , the default scope of a bean is singleton, which means that Spring will create a single instance of the
  bean and will share it across the entire application context, However, if you want to explicitly specify that a bean
  should be singleton, you can use the @Scope annotation with the <sub> ConfigurableBeanFactory.SCOPE_SINGLETON </sub>
  constant.

```Java
    public interface MyService {
        void performAction();
    }
    
    @Service
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) 
    public class MyServiceImpl implements MyService {
        @Override
        public void performAction() {
            System.out.println("Action performed by MyServiceImpl");
        }
    }

```
* Exceptions thrown in filters are not automatically handled by Spring's @ControllerAdvice and @ExceptionHandler mechanisms. Instead, you need to handle these exceptions within the filter itself or use a custom Filter for centralized exception handling.

```Java
@WebFilter("/*")
public class ExceptionHandlingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } catch (JwtExpirationException ex) {
            handleException((HttpServletResponse) response, HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
        } catch (Exception ex) {
            handleException((HttpServletResponse) response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }

    private void handleException(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write("{\"timestamp\":\"" + new java.util.Date() + "\", \"message\":\"" + message + "\", \"details\":\"\"}");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization logic, if needed
    }

    @Override
    public void destroy() {
        // Cleanup logic, if needed
    }
}
```