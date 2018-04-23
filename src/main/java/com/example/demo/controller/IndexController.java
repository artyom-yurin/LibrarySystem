package com.example.demo.controller;

import com.example.demo.common.Privileges;
import com.example.demo.exception.AccessDeniedException;
import com.example.demo.exception.UnauthorizedException;
import com.example.security.ParserToken;
import com.example.security.TokenAuthenticationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {
    public IndexController() {
    }

    @GetMapping("/")
    public ModelAndView login(HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null) {
            return new ModelAndView("login");
        }
        else if("librarian".equals(token.role)){
            return new ModelAndView("books");
        }
        else if("admin".equals(token.role))
        {
            return new ModelAndView("librarians");
        }
        else {
            return new ModelAndView("catalog");
        }
    }

    @GetMapping("/admin/librarians")
    public ModelAndView getLibrarians(HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null) throw new UnauthorizedException();
        if (!token.role.equals("admin")) throw new AccessDeniedException();
        return new ModelAndView("librarians");
    }

    @GetMapping("/admin/log")
    public ModelAndView getLogs(HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null) throw new UnauthorizedException();
        if (!token.role.equals("admin")) throw new AccessDeniedException();
        return new ModelAndView("loging");
    }

    @GetMapping("/catalog")
    public ModelAndView catalog(HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null) throw new UnauthorizedException();
        if (!(token.role.equals("patron") || token.role.equals("faculty") || token.role.equals("vp"))) throw new AccessDeniedException();
        return new ModelAndView("catalog");
    }

    @GetMapping("/myBooks")
    public ModelAndView myBooks(HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null) throw new UnauthorizedException();
        if (!(token.role.equals("patron") || token.role.equals("faculty") || token.role.equals("vp"))) throw new AccessDeniedException();
        return new ModelAndView("mybooks");
    }

    @GetMapping("/librarian/documents")
    public ModelAndView allDocuments(HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null) throw new UnauthorizedException();
        if (!token.role.equals("librarian")) throw new AccessDeniedException();
        if (Privileges.Privilege.Priv1.compareTo(Privileges.convertStringToPrivelege(token.position)) > 0)
            return new ModelAndView("books1");
        else if (Privileges.Privilege.Priv2.compareTo(Privileges.convertStringToPrivelege(token.position)) > 0)
            return new ModelAndView("books2");
        else return new ModelAndView("books3");
    }

    @GetMapping("/librarian/users")
    public ModelAndView allUsers(HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null) throw new UnauthorizedException();
        if (!token.role.equals("librarian")) throw new AccessDeniedException();
        if (Privileges.Privilege.Priv1.compareTo(Privileges.convertStringToPrivelege(token.position)) > 0)
            return new ModelAndView("users1");
        else if (Privileges.Privilege.Priv2.compareTo(Privileges.convertStringToPrivelege(token.position)) > 0)
            return new ModelAndView("users2");
        else return new ModelAndView("users3");
    }

    @GetMapping("/librarian/requests")
    public ModelAndView allRequests(HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null) throw new UnauthorizedException();
        if (!token.role.equals("librarian")) throw new AccessDeniedException();
        return new ModelAndView("request");
    }

    @GetMapping("/librarian/confirmation")
    public ModelAndView allConfirmation(HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null) throw new UnauthorizedException();
        if (!token.role.equals("librarian")) throw new AccessDeniedException();
        return new ModelAndView("confirmation");
    }

    @GetMapping("/notifications")
    public ModelAndView allAvailable(HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null) throw new UnauthorizedException();
        if (!(token.role.equals("patron") || token.role.equals("faculty") || token.role.equals("vp"))) throw new AccessDeniedException();
        return new ModelAndView("notifications");
    }


}
