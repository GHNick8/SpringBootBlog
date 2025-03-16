package com.techsphere.techsphere.config;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.techsphere.techsphere.models.Account;
import com.techsphere.techsphere.models.Authority;
import com.techsphere.techsphere.models.Post;
import com.techsphere.techsphere.service.AccountService;
import com.techsphere.techsphere.service.AuthorityService;
import com.techsphere.techsphere.service.PostService;
import com.techsphere.techsphere.util.constants.Privillages;
import com.techsphere.techsphere.util.constants.Roles;

@Component
public class SeedData implements CommandLineRunner{

    @Autowired
    private PostService postService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthorityService authorityService;

    @Override
    public void run(String... args) throws Exception {

       for(Privillages auth: Privillages.values()){
            Authority authority = new Authority();
            authority.setId(auth.getId());
            authority.setName(auth.getPrivillage());
            authorityService.save(authority);
       }
        
       Account account01 = new Account();
       Account account02 = new Account();
       Account account03 = new Account();
       Account account04 = new Account();

       account01.setEmail("user@user.com");
       account01.setPassword("pass987");
       account01.setFirstname("User");
       account01.setLastname("lastname");
       account01.setAge(25);
       account01.setDate_of_birth(LocalDate.parse("1990-01-01"));
       account01.setGender("Male");

       account02.setEmail("admin@techsphere.com");
       account02.setPassword("pass987");
       account02.setFirstname("Admin");
       account02.setLastname("lastname");
       account02.setRole(Roles.ADMIN.getRole());
       account02.setAge(25);
       account02.setDate_of_birth(LocalDate.parse("1990-01-01"));
       account02.setGender("Famale");

       account03.setEmail("editor@editor.com");
       account03.setPassword("pass987");
       account03.setFirstname("Editor");
       account03.setLastname("lastname");
       account03.setRole(Roles.EDITOR.getRole());
       account03.setAge(55);
       account03.setDate_of_birth(LocalDate.parse("1975-01-01"));
       account03.setGender("Male");

       account04.setEmail("super_editor@editor.com");
       account04.setPassword("pass987");
       account04.setFirstname("Editor");
       account04.setLastname("lastname");
       account04.setRole(Roles.EDITOR.getRole());
       account04.setAge(40);
       account04.setDate_of_birth(LocalDate.parse("1980-01-01"));
       account04.setGender("Female");
       
       Set<Authority> authorities = new HashSet<>();
       authorityService.findById(Privillages.ACCESS_ADMIN_PANEL.getId()).ifPresent(authorities::add);
       authorityService.findById(Privillages.RESET_ANY_USER_PASSWORD.getId()).ifPresent(authorities::add);
       account04.setAuthorities(authorities);

       accountService.save(account01);
       accountService.save(account02);
       accountService.save(account03);
       accountService.save(account04);
       
       List<Post> posts = postService.findAll();
       if (posts.size() == 0){
            Post post01 = new Post();
            post01.setTitle("Perl: A Hidden Gem in the World of Scripting");
            post01.setBody("""
               In the ever-changing landscape of programming languages, 
               Perl remains a powerful and adaptable tool that continues to serve developers across various domains. 
               Originally created by Larry Wall in 1987, Perl was designed for text processing and system administration, 
               but it has since found its way into web development, automation, and even bioinformatics.

               <br><br>
               <a href="/images/perl.jpg">
                  <img src="/images/perl.jpg" 
                        alt="Perl Raptor Logo" 
                        width="350" height="250">
               </a>
               <br>
               
               <h5><strong>Why Perl?</strong></h5>
               <strong>Powerful Text Processing</strong><br>
               Perl is known for its regular expressions and text manipulation capabilities, 
               making it an excellent choice for data parsing, log processing, and automation scripts.<br><br>
               <strong>Flexibility & Expressiveness</strong><br>
               The Perl philosophy is "There's more than one way to do it." 
               This flexibility allows programmers to write code in different styles, 
               whether concise one-liners or structured object-oriented code.<br><br>
               <strong>CPAN: A Rich Library of Modules</strong><br>
               Perl boasts a massive ecosystem of reusable modules via CPAN (Comprehensive Perl Archive Network), 
               providing pre-built solutions for almost any task.<br><br>
               <strong>Cross-Platform Compatibility</strong><br>
               Perl runs on Windows, Linux, MacOS, and even some legacy systems, 
               making it an excellent choice for system administration and automation across different environments.<br><br>
               <h5><strong>Hello, Perl! (Programming example)</strong></h5>
               Want to try Perl? Here's a classic “Hello, World” example:<br><br>
               #!/usr/bin/perl<br>
               use strict;<br>
               use warnings;<br>
               print "Hello, World!\n";<br><br>
               <h5><strong>Run it with:</strong></h5>
               perl hello.pl<br><br>
               <h5><strong>Learning Perl can be a rewarding experience</strong></h5>
               Perl may not be as popular as it once was, but it still holds a place in automation, 
               text processing, and legacy codebases. If you enjoy scripting and solving complex problems 
               in creative ways, learning Perl can be a rewarding experience.<br><br>
            """);
            post01.setAccount(account01);
            postService.save(post01);
       }
    }
}