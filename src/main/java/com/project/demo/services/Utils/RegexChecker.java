package com.project.demo.services.Utils;
import com.project.demo.logic.entity.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

@Service
public class RegexChecker {

    public boolean checkGoogleImage(String urlImage) {
//        String url = "https://lh3.googleusercontent.com/a/ACg8ocIEPkkZdWRYCAlUHH8htDlaQmyRsnp3mzX8PO0yFqa5tbQtHxM=s96-c";
        String regex = "^https:\\/\\/lh3\\.googleusercontent\\.com\\/a\\/[A-Za-z0-9_-]+=s\\d+-c$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(urlImage);

        if (matcher.matches()) {
            System.out.println("URL is valid.");
            return true;
        } else {
            System.out.println("URL is invalid.");
            return false;
        }
    }


}
