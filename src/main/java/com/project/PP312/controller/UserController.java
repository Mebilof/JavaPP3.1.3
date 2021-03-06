package com.project.PP312.controller;

import com.project.PP312.model.Role;
import com.project.PP312.model.User;
import com.project.PP312.service.RoleService;
import com.project.PP312.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

@Controller
public class UserController {

    private final UserService userService;

    private final UserDetailsService userDetailsService;

    private final RoleService roleService;

    UserController(UserService userService, UserDetailsService userDetailsService, RoleService roleService) {
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.roleService = roleService;
    }

    @GetMapping("/admin")
    public String printUsers(ModelMap model, Principal principal) {
        model.addAttribute("users", userService.getListUsers());
        model.addAttribute("authUser", userService.getUserByName(principal.getName()));
        model.addAttribute("newUser", new User());
        return "users";
    }

    @PostMapping("/admin/new")
    public String createUser(@ModelAttribute("user") User user, @RequestParam("newRoles[]") String[] roles) {
        Set<Role> roleSet = new HashSet<>();
        for (String r: roles){
            roleSet.add(roleService.getRoleByName(new Role(r)));
        }
        user.setRoles(roleSet);
        userService.addUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/user")
    public String checkUser(Principal principal, ModelMap model) {
        model.addAttribute("user", userService.getUserByName(principal.getName()));
        return "user";
    }

    @PostMapping ("/admin/edit")
    public String updateUser(@ModelAttribute("user") User user, @RequestParam("newRoles[]") String[] roles) {
        Set<Role> roleSet = new HashSet<>();
        for (String r: roles){
            roleSet.add(roleService.getRoleByName(new Role(r)));
        }
        user.setRoles(roleSet);
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @PostMapping("/admin/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    @ModelAttribute("auth")
        public UserDetails authUser(Principal principal) {
        return userDetailsService.loadUserByUsername(principal.getName());
    }
}
