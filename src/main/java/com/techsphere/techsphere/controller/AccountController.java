package com.techsphere.techsphere.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.techsphere.techsphere.models.Account;
import com.techsphere.techsphere.service.AccountService;
import com.techsphere.techsphere.util.AppUtil;
import com.techsphere.techsphere.util.RandomStringUtil;

import jakarta.validation.Valid;


@Controller
public class AccountController {

    @Autowired
    private AccountService accountService;
    
    @GetMapping("/register")
    public String register(Model model) {
        Account account = new Account();
        model.addAttribute("account", account);
        return "account_views/register";
    }

    @PostMapping("/register")
    public String register_user(@Valid @ModelAttribute Account account, BindingResult result) {
        if (result.hasErrors()) {
            return "account_views/register";
        }

        accountService.save(account);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "account_views/login";
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String profile(Model model, Principal principal) {
        String authUser = principal != null ? principal.getName() : "email";
        Optional<Account> optionalAccount = accountService.findOneByEmail(authUser);

        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            String profilePhoto = (account.getPhoto() != null && !account.getPhoto().isEmpty()) 
                    ? account.getPhoto() 
                    : "/images/Anon.png"; 

            model.addAttribute("account", account);
            model.addAttribute("photo", profilePhoto);
            return "account_views/profile";
        } else {
            return "redirect:/?error";
        }
    }

    @PostMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String post_profile(@Valid @ModelAttribute Account account, BindingResult bindingResult,
            Principal principal) {
        if (bindingResult.hasErrors()) {
            return "account_views/profile";
        }
        String authUser = "email";
        if (principal != null) {
            authUser = principal.getName();
        }
        Optional<Account> optionalAccount = accountService.findOneByEmail(authUser);
        if (optionalAccount.isPresent()) {
            Account account_by_id = accountService.findById(account.getId()).get();
            account_by_id.setAge(account.getAge());
            account_by_id.setDate_of_birth(account.getDate_of_birth());
            account_by_id.setFirstname(account.getFirstname());
            account_by_id.setGender(account.getGender());
            account_by_id.setLastname(account.getLastname());

            accountService.save(account_by_id);
            SecurityContextHolder.clearContext();
            return "redirect:/";
        } else {
            return "redirect:/?error";
        }
    }

    @PostMapping("/update_photo")
    @PreAuthorize("isAuthenticated()")
    public String update_photo(@RequestParam("file") MultipartFile file,
            RedirectAttributes attributes, Principal principal) {
        if (file.isEmpty()) {
            attributes.addFlashAttribute("error", "No file uploaded");
            return "redirect:/profile";
        } else {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());

            try {
                int length = 10;
                String generatedString = RandomStringUtil.generateRandomString(length);
                String final_photo_name = generatedString + fileName;
                String absolute_fileLocation = AppUtil.get_upload_path(final_photo_name);

                Path path = Paths.get(absolute_fileLocation);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                attributes.addFlashAttribute("message", "You successfully uploaded");

                String authUser = "email";
                if (principal != null) {
                    authUser = principal.getName();
                }

                Optional<Account> optional_Account = accountService.findOneByEmail(authUser);
                if (optional_Account.isPresent()) {
                    Account account = optional_Account.get();
                    Account account_by_id = accountService.findById(account.getId()).get();

                    String relative_fileLocation = "/uploads/" + final_photo_name;
                    account_by_id.setPhoto(relative_fileLocation);
                    accountService.save(account_by_id);
                }
                TimeUnit.SECONDS.sleep(1);
                return "redirect:/profile";

            } catch (Exception e) {
                return "redirect:/profile?error";
            }
        }
    }
}
