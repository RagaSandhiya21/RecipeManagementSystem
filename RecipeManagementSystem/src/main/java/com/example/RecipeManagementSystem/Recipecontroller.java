package com.example.RecipeManagementSystem;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Controller
public class Recipecontroller {

    String jdbcurl = "jdbc:mysql://localhost:3306/recipe";

    @GetMapping("/index")
    public String getindex() {
        return "index";
    }

    @GetMapping("about")
    public String getabout() {
        return "about";
    }
    @GetMapping("admin")
    public String getadmin() {
        return "admin";
    }
    @GetMapping("contact")
    public String getcontact() {
        return "contact";
    }
    @GetMapping("adminLogin")
    public String getadminlogin() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession session, Model model) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(jdbcurl, "root", "admin");
            String sql = "select * from users where username=? and password=?";
            PreparedStatement pstatement = connection.prepareStatement(sql);
            pstatement.setString(1, username);
            pstatement.setString(2, password);
            ResultSet resultSet = pstatement.executeQuery();

            if (resultSet.next()) {
                session.setAttribute("username", username);
                session.setAttribute("userId", resultSet.getString("userId")); // Assuming you have userId in your database
                return "admin";
            } else {
                model.addAttribute("error", "Invalid username or password");
                return "login";
            }
        } catch (Exception e) {
            System.out.println("Exception occurred: " + e);
            model.addAttribute("error", "An error occurred. Please try again later.");
            return "login";
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                System.out.println("Exception while closing connection: " + e);
            }
        }
    }
}
