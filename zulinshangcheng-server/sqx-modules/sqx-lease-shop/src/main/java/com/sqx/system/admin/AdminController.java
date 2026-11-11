package com.sqx.system.admin;

import com.sqx.auth.system.annotation.IgnoreSecurity;
import com.sqx.common.core.domain.Result;
import com.sqx.common.core.domain.ResultUtil;
import com.sqx.common.core.enums.ExceptionEnum;
import com.sqx.common.core.utils.MD5Util;
import com.sqx.domain.repository.entity.sys.AdminInfo;
import com.sqx.domain.repository.entity.sys.CharacterInfo;
import com.sqx.domain.repository.entity.sys.MenuInfo;
import com.sqx.domain.repository.entity.sys.SysRole;
import com.sqx.domain.repository.mapper.admin.AdminJpaRepository;
import com.sqx.domain.repository.mapper.admin.CharacterJpaRepository;
import com.sqx.domain.repository.mapper.admin.MenuJpaRepository;
import com.sqx.domain.repository.mapper.sysRole.SysRoleRepository;
import com.sqx.domain.repository.service.admin.IAdminSellingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(value = "管理平台用户管理", tags = {"管理平台用户api"})
@RequestMapping(value = "/admin")
public class AdminController {
    @Autowired
    private IAdminSellingService iAdminSellingService;
    @Autowired
    private AdminJpaRepository adminJpaRepository;
    @Autowired
    private MenuJpaRepository menuJpaRepository;
    @Autowired
    private CharacterJpaRepository characterJpaRepository;
    @Autowired
    private SysRoleRepository sysRoleRepository;

    @RequestMapping(value = "/menu/{id}", method = RequestMethod.GET)
    @ApiOperation("管理平台菜单详情")
    @ResponseBody
    public Result getMenu(@PathVariable Long id) {
        MenuInfo menuInfo = menuJpaRepository.findById(id).orElse(null);
        return ResultUtil.success(menuInfo);
    }

    @RequestMapping(value = "/menu/list", method = RequestMethod.GET)
    @ApiOperation("管理平台菜单列表")
    @ResponseBody
    public Result getMenuList() {
        return ResultUtil.success(menuJpaRepository.findAll());
    }

    @RequestMapping(value = "/menu/save", method = RequestMethod.POST)
    @ApiOperation("管理平台保存菜单")
    @ResponseBody
    public Result saveMenu(@RequestBody MenuInfo menuInfo) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        menuInfo.setCreateAt(sdf.format(now));
        return ResultUtil.success(menuJpaRepository.save(menuInfo));
    }

    @RequestMapping(value = "/character/{id}", method = RequestMethod.GET)
    @ApiOperation("管理平台角色详情")
    @ResponseBody
    public Result getCharacter(@PathVariable Long id) {
        return ResultUtil.success(characterJpaRepository.findById(id).orElse(null));
    }

    @RequestMapping(value = "/character/list", method = RequestMethod.GET)
    @ApiOperation("管理平台角色列表")
    @ResponseBody
    public Result getCharacterList() {
        return ResultUtil.success(characterJpaRepository.findAll());
    }

    @RequestMapping(value = "/character/save", method = RequestMethod.POST)
    @ApiOperation("管理平台保存角色")
    @ResponseBody
    public Result saveCharacter(@RequestBody CharacterInfo character) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        character.setCreateAt(sdf.format(now));
        return ResultUtil.success(characterJpaRepository.save(character));
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation("管理平台用户详情")
    @ResponseBody
    public Result getAdmin(@PathVariable Integer id) {
        return iAdminSellingService.findOne(Long.valueOf(id));
    }


    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("管理平台添加管理员")
    @ResponseBody
    public Result addAdmin(@RequestBody AdminInfo adminInfo) {
        return iAdminSellingService.saveBody(adminInfo);
    }


    @ApiOperation("管理平台管理员注册")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public Result registerUser(@RequestBody AdminInfo foodBanner) {
        //校验手机号为空 提示
        if (foodBanner.getPhone().isEmpty()) {
            return ResultUtil.error(ExceptionEnum.USER_PHONE_ERROR);
        } else {
            //校验用户是否注册
            AdminInfo userByPhone = iAdminSellingService.getUserByPhone(foodBanner.getPhone());
            if (userByPhone == null) {
                iAdminSellingService.saveBody(foodBanner);
                return ResultUtil.success();
            } else {
                return ResultUtil.error(ExceptionEnum.USER_IS_EXITS);
            }
        }

    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation("管理员信息修改")
    @ResponseBody
    public Result uUpdate(@RequestBody AdminInfo foodBanner) {
        return iAdminSellingService.updateAdminInfo(foodBanner);
    }

    @RequestMapping(value = "/delete/{id}")
    @ApiOperation("删除管理员信息")
    public Result deleteBanner(@PathVariable int id) {
        return iAdminSellingService.delete(id);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ApiOperation("管理平台获取管理员列表")
    @ResponseBody
    public Result getAdminList() {
        return iAdminSellingService.findAll();
    }

    @RequestMapping(value = "/adminInfoDetails", method = RequestMethod.GET)
    @ApiOperation("管理员详细信息")
    @ResponseBody
    public Result adminInfoDetails(Long id) {
        Map<String, Object> map = iAdminSellingService.selectAdminInfoDetails(id);
        List<SysRole> roleList = sysRoleRepository.selectSysRoleListByUserId(Long.parseLong(map.get("id").toString()));
        Map<String,Object> map1=new HashMap<>();
        map1.put("user",map);
        map1.put("roleList",roleList);
        return ResultUtil.success(map1);
    }

    @RequestMapping(value = "/adminInfoList", method = RequestMethod.GET)
    @ApiOperation("管理平台获取管理员列表")
    @ResponseBody
    public Result adminInfoList(int page,int size) {
        Sort.Order order = new Sort.Order(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, size, Sort.by(order));
        return ResultUtil.success(iAdminSellingService.selectAdminInfoList(pageable));
    }

    @RequestMapping(value = "/list/{state}", method = RequestMethod.GET)
    @ApiOperation("管理平台根据状态获取管理员")
    @ResponseBody
    public Result findByMerchant(@PathVariable String state) {
        return iAdminSellingService.findByMerchant(state);
    }

    @RequestMapping(value = "/page/{page}/{size}/{state}", method = RequestMethod.GET)
    @ResponseBody
    public Result getBodyPage(@PathVariable String state, @PathVariable Integer page,
                              @PathVariable int size) {
        Sort.Order order = new Sort.Order(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, size, Sort.by(order));
        return iAdminSellingService.find(state, pageable);
    }

    @IgnoreSecurity
    @RequestMapping(value = "/login/{username}/{password}", method = RequestMethod.POST)
    @ApiOperation("管理平台管理员登录")
    @ResponseBody
    public Result login(@PathVariable("password") String password, @PathVariable("username") String name,String uuid,String captcha) {
        return iAdminSellingService.Login(password, name,uuid,captcha);
    }

    @RequestMapping(value = "/password/{id}/{oldPwd}/{newPassword}", method = RequestMethod.POST)
    @ApiOperation("管理平台管理员修改密码")
    @ResponseBody
    public Result restPassword(@PathVariable("id") String id, @PathVariable("oldPwd") @NotBlank String oldPwd, @PathVariable("newPassword") @NotBlank String newPassword) {
        AdminInfo one = adminJpaRepository.findById(Long.valueOf(id)).orElse(null);
        if (one == null) {
            return ResultUtil.error(-1, "用户不存在");
        }
        if (!MD5Util.checkMD5(oldPwd, one.getPwd())) {
            return ResultUtil.error(-1, "原始密码不正确");
        }
        one.setPwd(MD5Util.encoderByMd5(newPassword));
        AdminInfo save = adminJpaRepository.save(one);
        return ResultUtil.success(save);
    }

    @RequestMapping(value = "/updateState/{id}", method = RequestMethod.POST)
    @ApiOperation("管理平台管理员状态修改")
    @ResponseBody
    public Result updateState(@PathVariable("id") String id) {
        AdminInfo adminInfo = adminJpaRepository.findById(Long.valueOf(id)).orElse(null);
        if (adminInfo == null) {
            return ResultUtil.error(-1, "用户不存在");
        }
        if (adminInfo.getState().equals("true")) {
            adminInfo.setState("false");
        } else {
            adminInfo.setState("true");
        }
        AdminInfo save = adminJpaRepository.save(adminInfo);
        return ResultUtil.success(save);
    }

    @RequestMapping(value = "/updatePwd/{pwd}/{phone}", method = RequestMethod.POST)
    @ApiOperation("管理平台管理员修改密码")
    @ResponseBody
    public Result updatePwd(@PathVariable("phone") String phone, @PathVariable("pwd") String pwd) {
        return iAdminSellingService.updatePwd(phone, pwd);
    }

    @RequestMapping(value = "/removeToken/{uuid}", method = RequestMethod.GET)
    @ApiOperation("管理平台删除token")
    @ResponseBody
    public Result removeToken(@PathVariable int uuid) {
        return iAdminSellingService.delete(uuid);
    }


}
