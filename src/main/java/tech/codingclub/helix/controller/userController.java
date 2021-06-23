package tech.codingclub.helix.controller;

import org.apache.commons.lang3.ObjectUtils;
import org.jooq.Condition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.codingclub.helix.database.GenericDB;
import tech.codingclub.helix.entity.Follower;
import tech.codingclub.helix.entity.Member;
import tech.codingclub.helix.entity.Tweet;
import tech.codingclub.helix.entity.TweetUI;
import tech.codingclub.helix.global.SysProperties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;


/**
 * User: rishabh
 */
@Controller
@RequestMapping("/user")
public class userController extends BaseController {

    @RequestMapping(method = RequestMethod.POST, value = "/create-post")
    public
    @ResponseBody
    String createTweet(@RequestBody String data, HttpServletResponse response, HttpServletRequest request) {
        new GenericDB<Tweet>().addRow(tech.codingclub.helix.tables.Tweet.TWEET,new Tweet(data,null,new Date().getTime(),ControllerUtils.getUserId(request)));
        return "Posted Successfully";
    }


    @RequestMapping(method = RequestMethod.POST, value = "/follow-member/{member_id}")
    public
    @ResponseBody
    String followMember(@PathVariable("member_id") Long memberId , HttpServletResponse response, HttpServletRequest request) {

        Long currentId = ControllerUtils.getUserId(request);
        if(currentId!=null && memberId!=null && !currentId.equals(memberId))
        {
            Follower follower = new Follower(currentId,memberId);
            new GenericDB<Follower>().addRow(tech.codingclub.helix.tables.Follower.FOLLOWER,follower);
            return "Connected Successfully";
        }

        return "Not Permitted";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/un-follow-member/{member_id}")
    public
    @ResponseBody
    String unfollowMember(@PathVariable("member_id") Long memberId , HttpServletResponse response, HttpServletRequest request) {

        Long currentId = ControllerUtils.getUserId(request);
        if(currentId!=null && memberId!=null && !currentId.equals(memberId))
        {
            Condition condition = tech.codingclub.helix.tables.Follower.FOLLOWER.USER_ID.eq(currentId).and(tech.codingclub.helix.tables.Follower.FOLLOWER.FOLLOWING_ID.eq(memberId));
            Boolean success = GenericDB.deleteRow(tech.codingclub.helix.tables.Follower.FOLLOWER,condition);
            if(success)
                return "Un Followed  Successfully";
            else
                return "Failed";
        }

        return "Backend Error";
    }


    private void preLoadVariables(ModelMap modelMap, Long memberId)
    {
        modelMap.addAttribute("USER_IMAGE","/images/profile-image/" + memberId +".jpeg");
    }


    @RequestMapping(method = RequestMethod.GET, value = "/recomendations")
    public String welcomeRecomendations(ModelMap modelMap, HttpServletResponse response, HttpServletRequest request) {

        Member member = ControllerUtils.getCurrentMember(request);
        List<Member> members= (List<Member>) GenericDB.getRows(tech.codingclub.helix.tables.Member.MEMBER,Member.class,null,10,tech.codingclub.helix.tables.Member.MEMBER.ID.desc());


        ArrayList<Long> MemberIds = new ArrayList<Long>();
        for(Member m:members)
        {
            MemberIds.add(m.id);
        }

        Condition condition = tech.codingclub.helix.tables.Follower.FOLLOWER.USER_ID.eq(member.id).and(tech.codingclub.helix.tables.Follower.FOLLOWER.FOLLOWING_ID.in(MemberIds));
        List<Follower> followerRows = (List<Follower>) GenericDB.getRows(tech.codingclub.helix.tables.Follower.FOLLOWER,Follower.class,condition,null);

        Set<Long> followedMemberIds = new HashSet<Long>();
        for(Follower f:followerRows)
        {
            followedMemberIds.add(f.following_id);
        }

        for(Member m : members)
        {
            if(followedMemberIds.contains(m.id))
            {
                m.is_followed = true;
            }
        }


        modelMap.addAttribute("NAME",member.name);
        preLoadVariables(modelMap, member.id);
        modelMap.addAttribute("RECOMENDATIONS",members);


        return "recomendations";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/followed")
    public String followed(ModelMap modelMap, HttpServletResponse response, HttpServletRequest request) {

        Long memberId = ControllerUtils.getUserId(request);
        Condition condition = tech.codingclub.helix.tables.Follower.FOLLOWER.USER_ID.eq(memberId);
        List<Long> followedIds= new GenericDB<Long>().getColumnRows(tech.codingclub.helix.tables.Follower.FOLLOWER.FOLLOWING_ID, tech.codingclub.helix.tables.Follower.FOLLOWER,Long.class,condition,100);

        Condition selectMember = tech.codingclub.helix.tables.Member.MEMBER.ID.in(followedIds);
        List<Member> followedMembers = (List<Member>) GenericDB.getRows(tech.codingclub.helix.tables.Member.MEMBER,Member.class,selectMember,100);


        preLoadVariables(modelMap,memberId);
        modelMap.addAttribute("FOLLOWED",followedMembers);


        return "followed";
    }


    @RequestMapping(method = RequestMethod.GET, value = "/welcome")
    public String welcome(ModelMap modelMap, HttpServletResponse response, HttpServletRequest request) {

        Member member = ControllerUtils.getCurrentMember(request);
        return "welcome";
    }


    @RequestMapping(method = RequestMethod.POST, value = "/public-tweet/{id}")
    public
    @ResponseBody
    List<TweetUI> fetchTweet(@PathVariable("id") Long id , HttpServletResponse response, HttpServletRequest request) {

        Condition condition = tech.codingclub.helix.tables.Tweet.TWEET.ID.lessThan(id);
        List<Tweet> data = (List<Tweet>) GenericDB.getRows(tech.codingclub.helix.tables.Tweet.TWEET,Tweet.class,condition,3,tech.codingclub.helix.tables.Tweet.TWEET.ID.desc());

        Set<Long> memberIds = new HashSet<Long>();

        for(Tweet tweet : data)
        {
            memberIds.add(tweet.author_id);
        }

        HashMap<Long,Member> memberHashMap = new HashMap<Long, Member>();
        Condition memberCondition = tech.codingclub.helix.tables.Member.MEMBER.ID.in(memberIds);
        List<Member> members = (List<Member>) GenericDB.getRows(tech.codingclub.helix.tables.Member.MEMBER,Member.class,memberCondition,null);

        for(Member member:members)
        {
            memberHashMap.put(member.id, member);
        }


        ArrayList<TweetUI> tweetUIS = new ArrayList<TweetUI>();
        for(Tweet tweet : data)
        {
            Member member = memberHashMap.get(tweet.author_id);
            TweetUI tweetUI = new TweetUI(tweet,member);
            tweetUIS.add(tweetUI);
        }



        return tweetUIS;
    }







    @RequestMapping(method = RequestMethod.GET, value = "/update")
    public String updateUser(ModelMap modelMap, HttpServletResponse response, HttpServletRequest request) {

        return "updateuser";
    }

    private static String saveUploadedFile(MultipartFile file, Long userId){
        try {
            String path = SysProperties.getBaseDir()+"/images/profile-image/"+userId+".jpeg";
            file.transferTo( new File(path));
            return "/images/profile-image/"+userId+".jpeg";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }


    @RequestMapping(method = RequestMethod.POST, value = "/profile-image/upload")
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile uploadfile, HttpServletRequest request) {
        if (uploadfile.isEmpty()) {
            return new ResponseEntity("please select a file!", HttpStatus.OK);
        }
        String path = "";
        try {
            Long currentMemberId = ControllerUtils.getUserId(request);
            path = saveUploadedFile(uploadfile,currentMemberId);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(path, new HttpHeaders(), HttpStatus.OK);
    }



}