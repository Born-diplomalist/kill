package com.born.secKill.server.controller;/**
 * Created by Administrator on 2020/3/13.
 */

import com.born.secKill.server.utils.CpachaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 基础controller
 * @Author:gyk
 * @Date: 2020/3/13 23:36
 **/
@Controller
@RequestMapping("base")
public class BaseController {

    private static final Logger log= LoggerFactory.getLogger(BaseController.class);


    @RequestMapping(value = "/error",method = RequestMethod.GET)
    public String error(){
        return "basic/error";
    }

    @RequestMapping(value = "/success",method = RequestMethod.GET)
    public String success(){
        return "basic/success";
    }


    /**
	 * 生成验证码 设定本系统的验证码都用该函数生成
	 *
	 * @param vcodeLen   验证码长度，非必传，默认值4
	 * @param width      验证码宽度，非必传，默认值100
	 * @param height     验证码高度，非必传，默认值30
	 * @param cpachaType 不同的生成位置使用该字符串参数作区分，默认值是loginCpacha，该参数必传
	 * @param request
	 * @param response
	 */
	@GetMapping("/get_cpacha")
	@ResponseBody
	public void generateCpacha(
            // name会作为请求URL中的参数，所修饰的变量在当前方法使用
            @RequestParam(name = "v1", required = false, defaultValue = "4") Integer vcodeLen,
            @RequestParam(name = "w", required = false, defaultValue = "100") Integer width,
            @RequestParam(name = "h", required = false, defaultValue = "30") Integer height,
            @RequestParam(name = "type", required = true, defaultValue = "loginCpacha") String cpachaType,
            HttpServletRequest request, HttpServletResponse response) {
		CpachaUtil cpachaUtil = new CpachaUtil(vcodeLen, width, height);
		String generatorVCode = cpachaUtil.generatorVCode();
		//为避免缓存，先删掉再添加
		//request.getSession().removeAttribute(cpachaType);
		request.getSession().setAttribute(cpachaType, generatorVCode);
		BufferedImage generatorVCodeImage = cpachaUtil.generatorVCodeImage(generatorVCode, true);
		try {
			ImageIO.write(generatorVCodeImage, "gif", response.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
            log.error("验证码生成失败！");
			e.printStackTrace();
		}
	}

}























