package com.techsphere.techsphere.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.techsphere.techsphere.models.Account;
import com.techsphere.techsphere.models.Comment;
import com.techsphere.techsphere.repository.AccountRepository;
import com.techsphere.techsphere.service.CommentService;

@Controller
@RequestMapping("/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private AccountRepository accountRepository; 

    @PostMapping("/add")
    public String addComment(@RequestParam String headingId, @RequestParam String content, Principal principal) {
        Account account = null;

        if (principal != null) { 
            account = accountRepository.findOneByEmailIgnoreCase(principal.getName()).orElseThrow();
        }

        Comment comment = new Comment();
        comment.setHeadingId(headingId);
        comment.setAccount(account); 
        comment.setContent(content);
        commentService.saveComment(comment);

        return "redirect:/posts/" + headingId;  
    }

    @PostMapping("/delete/{id}")
    public String deleteComment(@PathVariable Long id, Principal principal) {
        Comment comment = commentService.getCommentById(id);

        if (comment == null) {
            return "redirect:/error"; 
        }

        if (!principal.getName().equals(comment.getAccount().getEmail())) {
            return "redirect:/access-denied";
        }

        commentService.deleteComment(id);
        return "redirect:/posts/" + comment.getHeadingId(); 
    }
}
