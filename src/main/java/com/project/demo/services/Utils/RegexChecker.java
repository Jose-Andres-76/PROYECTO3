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
        String regex = "^https:\\/\\/lh3\\.googleusercontent\\.com\\/a\\/[A-Za-z0-9_-]+=s\\d+-c$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(urlImage);

        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }


}
