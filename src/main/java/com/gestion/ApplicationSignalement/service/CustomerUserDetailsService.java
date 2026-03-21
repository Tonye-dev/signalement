package com.gestion.ApplicationSignalement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gestion.ApplicationSignalement.config.CustomerUserDetails;
import com.gestion.ApplicationSignalement.entity.Utilisateur;
import com.gestion.ApplicationSignalement.repository.UtilisateurRepository;



@Service
public class CustomerUserDetailsService implements UserDetailsService{
    
@Autowired
private UtilisateurRepository utilisateurRepository;

@Override
public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
  
    Utilisateur utilisateur = utilisateurRepository.findByEmail(email).orElseThrow( () -> new UsernameNotFoundException("utilisateur non trouve avec l'email: " + email));
    return new CustomerUserDetails(utilisateur);

}



}
