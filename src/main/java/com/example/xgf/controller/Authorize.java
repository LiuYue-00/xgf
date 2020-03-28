package com.example.xgf.controller;

import com.example.xgf.DTO.AccessTokenDTO;
import com.example.xgf.DTO.github_user;
import com.example.xgf.Provider.GitHubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class Authorize {
    @Autowired
    private GitHubProvider gitHubProvider;
    //加载配置文件中的信息
    @Value("${github.client.id}")
    private String client_id;
    @Value("${github.client.secret}")
    private String client_secret;
    @Value("${github.redirect.uri}")
    private String redirect_uri;
    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code")String code,
                           @RequestParam(name = "state") String state,
                            //将上下文中的request拿到（包括cookie），用于后面的使用
                            HttpServletRequest request) {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        accessTokenDTO.setClient_id(client_id);
        accessTokenDTO.setClient_secret(client_secret);
        accessTokenDTO.setRedirect_uri(redirect_uri);
        String accessToken = gitHubProvider.getAccessToken(accessTokenDTO);
        github_user user=gitHubProvider.gitHubUser(accessToken);
        // 判断用户是否登录
        if(user != null){
            // 已经登录了
            // session 创建了对象user;
            request.getSession().setAttribute("user", user);
            return "redirect:/";
        }
        else {
            // 未登录
            return "redirect:/";
        }
    }
}
