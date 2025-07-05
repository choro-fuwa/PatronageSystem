package com.PatronageSystem.controller;

import com.PatronageSystem.domain.IpInfo;
import com.PatronageSystem.domain.vo.IpDataVo;
import com.PatronageSystem.domain.vo.ResultVO;
import com.PatronageSystem.service.IpInfoService;
import com.PatronageSystem.utils.DateUtil;
import com.PatronageSystem.utils.RedisUtils;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import com.PatronageSystem.utils.IPUtil;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class IpController {
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private IpInfoService ipInfoService;

    /**
     * 跳转首页
     */
    @GetMapping("/")
    public ModelAndView index(HttpServletRequest request) {
        // return new ModelAndView("index");

        String ip = IPUtil.getIpAddress(request);
        String ua = request.getHeader("user-agent");
        // 1. 先存Redis（会自动补全地理信息）
        ipInfoService.saveRedis(ip, ua);
        // 2. 取出Redis中的IpInfo对象，存数据库
        IpInfo ipInfo = (IpInfo) redisUtils.hget("ipInfo", ip);
        if (ipInfo != null) {
            ipInfoService.save(ipInfo);
        }
        return new ModelAndView("index");
    }

    @GetMapping("/test")
    public ModelAndView test() {
        return new ModelAndView("test");
    }

    /*
     * 获取今日访问数据
     */
    @GetMapping("/api/today")
    @ResponseBody
    public ResultVO getToday() {
        List<Object> ipInfos = redisUtils.hgetAll("ipInfo");
        List<IpInfo> result = ipInfos.stream().map(obj -> (IpInfo) obj).collect(Collectors.toList());
        // 需要根据日期排序
        result.sort(Comparator.comparing(IpInfo::getTime));
        return new ResultVO(200, "OK", result);
    }

    /*
     * 获取昨日访问数据
     */
    @GetMapping("/api/yesterday")
    @ResponseBody
    public ResultVO getYesterday() {
        Date start = DateUtil.getYesday();
        Date end = DateUtil.initDateByDay();
        List<IpInfo> ipInfos = ipInfoService.queryYesterday(start, end);
        ipInfos.sort(Comparator.comparing(IpInfo::getTime));
        return new ResultVO(200, "OK", ipInfos);
    }


    /*
     * 获取历史访问的总数居
     */
    @GetMapping("/api/total")
    @ResponseBody
    public ResultVO getTotle() {
        List<IpInfo> ipInfos = ipInfoService.queryAll();
        List<Object> infoToday = redisUtils.hgetAll("ipInfo");
        List<IpInfo> result = infoToday.stream().map(obj -> (IpInfo) obj).collect(Collectors.toList());
        result.addAll(ipInfos);
        return new ResultVO(200, "OK", result);
    }

    /*
     * 获取最近100条IP的访问数据
     */
    @GetMapping("/api/limit")
    @ResponseBody
    public ResultVO getTop100() {
        // 提供100条数据的策略：先从redis里查询数据，如果少于100条数据，则不够的从数据库里面取最后几条数据
        List<Object> ipInfos = redisUtils.hgetAll("ipInfo");
        List<IpInfo> result = ipInfos.stream().map(obj -> (IpInfo) obj).collect(Collectors.toList());
        if (result.size() <= 100) {
            int sub = 100 - result.size();
            Pageable request = PageRequest.of(0, sub);
            List<IpInfo> ipInfosSub = ipInfoService.findAllByOrderByIdDesc(request);
            result.addAll(ipInfosSub);
        } else {
            result = result.subList(0, 100);
        }
        return new ResultVO(200, "OK", result);
    }
}
