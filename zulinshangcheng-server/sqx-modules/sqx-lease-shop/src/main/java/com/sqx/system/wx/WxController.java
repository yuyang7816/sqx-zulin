package com.sqx.system.wx;

import com.alibaba.fastjson.JSON;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;
import com.sqx.common.core.domain.Result;
import com.sqx.common.core.domain.ResultUtil;
import com.sqx.domain.repository.entity.sys.MessageInfo;
import com.sqx.domain.repository.entity.wx.Menu;
import com.sqx.domain.repository.entity.wx.WordInfo;
import com.sqx.domain.repository.mapper.wx.WordJpaRepository;
import com.sqx.domain.repository.mapper.wx.WordRepository;
import com.sqx.domain.repository.service.app.UserMoneyService;
import com.sqx.domain.repository.service.app.UserService;
import com.sqx.domain.repository.service.sys.ICommonInfoService;
import com.sqx.domain.repository.service.sys.MessageService;
import com.sqx.domain.repository.service.wx.AccountService;
import com.sqx.domain.repository.service.wx.AccountTokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import weixin.popular.api.MenuAPI;
import weixin.popular.api.UserAPI;
import weixin.popular.bean.BaseResult;
import weixin.popular.bean.user.FollowResult;
import weixin.popular.bean.user.User;
import weixin.popular.bean.xmlmessage.XMLMessage;
import weixin.popular.bean.xmlmessage.XMLTextMessage;
import weixin.popular.support.ExpireKey;
import weixin.popular.support.expirekey.DefaultExpireKey;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


@Slf4j
@RestController
@Api(value = "微信账号管理", tags = {"微信账号管理"})
@RequestMapping("/wx")
public class WxController {

    @Autowired
    private WordJpaRepository wordJpaRepository;
    @Autowired
    private ICommonInfoService commonInfoService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private AccountTokenService accountTokenService;
    @Autowired
    private UserService userService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private UserMoneyService userMoneyService;
    @Autowired
    private WordRepository wordRepository;

    //重复通知过滤
    private static ExpireKey expireKey = new DefaultExpireKey();


    @RequestMapping(value = "/saveWord", method = RequestMethod.POST)
    @ApiOperation("保存关键字")
    public Result saveWord(@RequestBody WordInfo wordInfo) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        wordInfo.setCreateAt(sdf.format(now));
        return ResultUtil.success(wordJpaRepository.save(wordInfo));
    }

    @PostMapping("/getWordList")
    @ApiOperation("保存关键字")
    public Result getWordList() {
        return ResultUtil.success(wordJpaRepository.findAll());
    }

    @PostMapping("/deleteById/{id}")
    @ApiOperation("删除关键字")
    public Result deleteById(@PathVariable Long id) {
        wordJpaRepository.deleteById(id);
        return ResultUtil.success();
    }


    @GetMapping("/createQr")
    @ApiOperation("用户端创建公众号免单商品二维码")
    public Result createQr(@RequestParam String relation) {
        return accountService.createQr(relation);
    }


    @RequestMapping(value = "/sqx/sendMsg", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("回复公众号消息")
    @ResponseBody
    public Result sendMsg(String content, Long id, HttpServletResponse response) throws IOException {
        MessageInfo messageInfo = messageService.getById(id);
        ServletOutputStream outputStream = response.getOutputStream();
        WXBizMsgCrypt wxBizMsgCrypt = (WXBizMsgCrypt) JSON.parse(messageInfo.getWxBizMsgCrypt());
        //创建回复
        XMLMessage xmlTextMessage = new XMLTextMessage(
                messageInfo.getFromUserName(),
                messageInfo.getToUserName(),
                content);
        //回复
        xmlTextMessage.outputStreamWrite(outputStream, wxBizMsgCrypt);
        return ResultUtil.success();
    }




    /**
     * 数据流输出
     */
    private void outputStreamWrite(OutputStream outputStream, String text) {
        try {
            outputStream.write(text.getBytes("utf-8"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * token
     * 获取token
     *
     * @return openid
     */
    @GetMapping("/token")
    @ApiOperation("管理平台获取token")
    public String getToken() {
        return accountTokenService.getWxToken();
    }


//    @GetMapping("sendMsg")
//    @ApiOperation("消息推送")
//    public void sendMessage(@RequestParam String title, @RequestParam String content,@RequestParam String client) {
//        pushToSingle(title, content,client);
//    }


    /**
     * token
     * 获取菜单
     *
     * @return openid
     */
    @GetMapping("/menu")
    @ApiOperation("管理平台获取菜单")
    public weixin.popular.bean.menu.Menu getMenu() {
        return MenuAPI.menuGet(accountTokenService.getWxToken());
    }

    /**
     * token
     * 创建菜单
     *
     * @return openid
     */
    @PostMapping("/create")
    @ApiOperation("管理平台创建菜单")
    public Result createMenu(@RequestBody Menu menu) {
        String s = JSON.toJSONString(menu);
        BaseResult baseResult = MenuAPI.menuCreate(accountTokenService.getWxToken(), s);
        return ResultUtil.success(baseResult);
    }

    /**
     * 检查是否关注公众号
     *
     * @param openid openid
     * @return boolean
     */
    @GetMapping("/follow/{openId:.+}")
    @ApiOperation("用户端检查是否关注公众号")
    public boolean checkIsFollow(@PathVariable("openId") String openid) {
//        log.info("判断用户是否关注：openid={}", openid);
        return accountService.checkIsFollow(openid);
    }

    /**
     * 获取openid
     * 获取用户信息
     *
     * @param openId openId
     * @return openid
     */
    @GetMapping("/userInfo/{openId:.+}")
    @ApiOperation("用户端通过openid获取微信用户信息")
    public User getUserInfo(@PathVariable("openId") String openId) {
        return accountService.getUserInfo(openId);
    }

    /**
     * 获取签名
     * 监测js签名
     *
     * @param url request的url
     * @return String
     */
    @GetMapping("/js/sign")
    @ApiOperation("用户端获取js签名")
    public String getJSSign(@RequestParam String url) {
        return accountService.getJSSign(url);
    }

    @GetMapping("/users")
    @ApiOperation("获取关注公众号用户列表")
    public FollowResult getUserList() {
        return UserAPI.userGet(accountTokenService.getWxToken(), null);
    }

    @GetMapping("/getUserTotal")
    @ApiOperation("获取关注公众号用户总数")
    public Result getUserTotal() {
        return ResultUtil.success(UserAPI.userGet(accountTokenService.getWxToken(), null).getTotal());
    }



}
