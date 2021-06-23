package tech.codingclub.helix.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import tech.codingclub.helix.database.GenericDB;
import tech.codingclub.helix.entity.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;

/**
 * User: rishabh
 */
@Controller
@RequestMapping("/")
public class MainController extends BaseController {

    private static Logger logger = Logger.getLogger(MainController.class);

    @RequestMapping(method = RequestMethod.GET, value = "/helloworld")
    public String getQuiz(ModelMap modelMap, HttpServletResponse response, HttpServletRequest request) {
        return "hello";
    }


    @RequestMapping(method = RequestMethod.GET, value = "/signup")
    public String signUp(ModelMap modelMap, HttpServletResponse response, HttpServletRequest request) {
        return "signup";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/uitest")
    public String uitest(ModelMap modelMap, HttpServletResponse response, HttpServletRequest request) {
        return "uitest";
    }



    @RequestMapping(method = RequestMethod.POST, value = "/signup")
    public
    @ResponseBody
    signupResponse signUpData(@RequestBody Member member, HttpServletRequest request, HttpServletResponse response)
    {

        boolean user_created=false;
        String message = "";

        if(GenericDB.getCount(tech.codingclub.helix.tables.Member.MEMBER, tech.codingclub.helix.tables.Member.MEMBER.EMAIL.eq(member.email)) > 0)
        {
            message="User already exists";
        }else {
            new GenericDB<Member>().addRow(tech.codingclub.helix.tables.Member.MEMBER, member);
            member.role = "cm";
            message="user Created";
            user_created=true;
        }
        return new signupResponse(message,user_created);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/time")
    public @ResponseBody String getTime(ModelMap modelMap, HttpServletResponse response, HttpServletRequest request) {
        TimeApi timeApi = new TimeApi(new Date().toString(),new Date().getTime());
        return new Gson().toJson(timeApi);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/wiki/gson")
    public @ResponseBody String wikiResponse(ModelMap modelMap,@RequestParam("country") String country , HttpServletResponse response, HttpServletRequest request) {
        wikipediaDownloader x = new wikipediaDownloader(country);
        wikiResult w = x.getResponse();

        return new Gson().toJson(w);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/wiki/html")
    public String wikiHtml(ModelMap modelMap,@RequestParam("country") String country, HttpServletResponse response, HttpServletRequest request) {
        wikipediaDownloader x = new wikipediaDownloader(country);
        wikiResult w = x.getResponse();

        modelMap.addAttribute("IMAGE",w.image_url);
        modelMap.addAttribute("DESCRIPTION",w.text_result);
        return "wikiApi";

    }

    @RequestMapping(method = RequestMethod.POST, value = "/handle")
    public
    @ResponseBody
    String handleEncrypt(@RequestBody String data, HttpServletRequest request, HttpServletResponse response) {
        return "ok";
    }
}