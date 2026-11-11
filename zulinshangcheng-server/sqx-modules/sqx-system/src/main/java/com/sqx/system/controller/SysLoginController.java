//package com.sqx.system.controller;
//
//
//import com.sqx.common.core.domain.Result;
//import com.sqx.domain.repository.entity.sys.SysUserEntity;
//import com.sqx.system.domain.form.SysLoginForm;
//import com.sqx.system.service.SysCaptchaService;
//import com.sqx.system.service.SysUserService;
//import com.sqx.system.service.SysUserTokenService;
//import io.swagger.annotations.Api;
//import org.apache.shiro.crypto.hash.Sha256Hash;
//import org.apache.tomcat.util.http.fileupload.IOUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.imageio.ImageIO;
//import javax.servlet.ServletOutputStream;
//import javax.servlet.http.HttpServletResponse;
//import java.awt.image.BufferedImage;
//import java.io.IOException;
//
///**
// * 登录相关
// *
// */
//@RestController
//@Api(value = "登录相关-管理端", tags = {"登录相关-管理端"})
//public class SysLoginController extends AbstractController {
//	@Autowired
//	private SysUserService sysUserService;
//	@Autowired
//	private SysUserTokenService sysUserTokenService;
//	@Autowired
//	private SysCaptchaService sysCaptchaService;
//
//	/**
//	 * 验证码
//	 */
//	@GetMapping("captcha.jpg")
//	public void captcha(HttpServletResponse response, String uuid)throws IOException {
//		response.setHeader("Cache-Control", "no-store, no-cache");
//		response.setContentType("image/jpeg");
//
//		//获取图片验证码
//		BufferedImage image = sysCaptchaService.getCaptcha(uuid);
//
//		ServletOutputStream out = response.getOutputStream();
//		ImageIO.write(image, "jpg", out);
//		IOUtils.closeQuietly(out);
//	}
//
//	/**
//	 * 登录
//	 */
//	@PostMapping("/sys/login")
//	public Result login(@RequestBody SysLoginForm form)throws IOException {
//		boolean captcha = sysCaptchaService.validate(form.getUuid(), form.getCaptcha());
//		if(!captcha){
//			return Result.failed("验证码不正确");
//		}
//
//		//用户信息
//		SysUserEntity user = sysUserService.queryByUserName(form.getUsername());
//
//		//账号不存在、密码错误
//		if(user == null || !user.getPassword().equals(new Sha256Hash(form.getPassword(), user.getSalt()).toHex())) {
//			return Result.failed("账号或密码不正确");
//		}
//
//		//账号锁定
//		if(user.getStatus() == 0){
//			return Result.failed("账号已被锁定,请联系管理员");
//		}
//
//		//生成token，并保存到数据库
//		return sysUserTokenService.createToken(user.getUserId());
//	}
//
//
//	/**
//	 * 退出
//	 */
//	@PostMapping("/sys/logout")
//	public Result logout() {
//		sysUserTokenService.logout(getUserId());
//		return Result.success();
//	}
//
//}
