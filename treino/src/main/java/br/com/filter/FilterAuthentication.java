package br.com.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import br.com.entities.Person;


@WebFilter(urlPatterns = {"/*"})
public class FilterAuthentication implements Filter {
	
    @Override  
	public void destroy() {}
    
    @Override
	public void doFilter(ServletRequest request, ServletResponse response, 
			FilterChain chain) throws IOException, ServletException {
    	
    	HttpServletRequest req = (HttpServletRequest) request;
    	HttpSession session = req.getSession();
    	
    	Person userLoaded = (Person) session.getAttribute("userLoaded");
    	String url = req.getServletPath();
    	
    	if(!url.equalsIgnoreCase("index.jsf") && userLoaded == null) {
    		
    		RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsf?faces-redirect=true");
    		
    		dispatcher.forward(request,response);
    		
    		return;
    		
    	}else {
    		chain.doFilter(request, response);
    	}
	}
    @Override
	public void init(FilterConfig fConfig) throws ServletException {
		
	}

}
