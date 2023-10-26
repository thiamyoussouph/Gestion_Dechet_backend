package com.nola.gestiondechet.Security;

import com.nola.gestiondechet.Entities.JWT;
import com.nola.gestiondechet.Services.UtilisateurService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Service
public class JwtFilter extends OncePerRequestFilter {

private UtilisateurService utilisateurService;

private JwtService jwtService;
    public JwtFilter(UtilisateurService utilisateurService, JwtService jwtService) {
        this.utilisateurService = utilisateurService;
        this.jwtService = jwtService;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        JWT tokenDansLaBase = null;
        String token=null;
        String username=null;
        boolean isTokenExpired = true;
        final String authorization=request.getHeader("Authorization");
        if(authorization!=null && authorization.startsWith("Bearer ")){
            token=authorization.substring(7);
            tokenDansLaBase=this.jwtService.tokenbyvalue(token);
            isTokenExpired = jwtService.isTokenExpired(token);
            username=this.jwtService.extractUsername(token);

    }
        if(
                !isTokenExpired  && tokenDansLaBase.getUtilisateur().getEmail().equals(username)
                        &&SecurityContextHolder.getContext().getAuthentication()==null){
             UserDetails userDetails=this.utilisateurService.loadUserByUsername(username);
              UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
              SecurityContextHolder.getContext().setAuthentication(authenticationToken);


        }
        filterChain.doFilter(request,response);
    }
}
