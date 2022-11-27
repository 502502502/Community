package com.ningct.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class XMLworkController {
    @RequestMapping(path = "/work/1",method = RequestMethod.GET)
    public String getwrok1(){
        return "/XMLwork/1";
    }
    @RequestMapping(path = "/work/2",method = RequestMethod.GET)
    public String getwrok2(){
        return "/XMLwork/2";
    }
    @RequestMapping(path = "/work/3",method = RequestMethod.GET)
    public String getwrok3(){
        return "/XMLwork/3";
    }
    @RequestMapping(path = "/work/4",method = RequestMethod.GET)
    public String getwrok4(){
        return "/XMLwork/4";
    }
    @RequestMapping(path = "/work/5",method = RequestMethod.GET)
    public String getwrok5(){
        return "/XMLwork/5";
    }
    @RequestMapping(path = "/work/component",method = RequestMethod.GET)
    public String getwrokcomponent(){
        return "/XMLwork/component";
    }
    @RequestMapping(path = "/work/java",method = RequestMethod.GET)
    public String getwrokjava(){
        return "/XMLwork/java";
    }
    @RequestMapping(path = "/work/analize",method = RequestMethod.GET)
    public String getwrokanalize(){
        return "/XMLwork/analize";
    }
    @RequestMapping(path = "/work/math",method = RequestMethod.GET)
    public String getwrokmath(){
        return "/XMLwork/math";
    }
    @RequestMapping(path = "/work/web",method = RequestMethod.GET)
    public String getwrokweb(){
        return "/XMLwork/web";
    }
    @RequestMapping(path = "/work/success",method = RequestMethod.GET)
    public String getwroksuccess(){
        return "/XMLwork/success";
    }
    @RequestMapping(path = "/work/score",method = RequestMethod.GET)
    public String getwrokScore(){
        return "/XMLwork/score.xml";
    }
}
