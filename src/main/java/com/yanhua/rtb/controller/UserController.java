//package com.yanhua.rtb.controller;
//
//
//import com.alibaba.fastjson.JSONObject;
//import com.yanhua.rtb.entity.User;
//import com.yanhua.rtb.service.IUserService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiImplicitParam;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.UUID;
//
///**
// * <p>
// *  前端控制器
// * </p>
// *
// * @author Emiya
// * @since 2020-09-28
// */
//@RestController
//@RequestMapping("/user")
//@Api(tags = "用户控制器")
//public class UserController {
//
//    @Autowired
//    private IUserService userService;
//
//    @ApiOperation(value = "打招呼", notes = "测试方法")
////    @ApiImplicitParam(name = "name", value = "姓名")
//    @PostMapping("/getUser")
//    public User getUser(){
//        return userService.getById(1);
//    }
//
//
//    @ApiOperation(value = "获取所有用户", notes = "查询分页数据")
//    @PostMapping("/findAllUser")
//    public List<User> findAllUser(){
//        return userService.findAllUser();
//    }
//
//    @ApiOperation(value = "addOrUpdateUser",notes = "新增或者更新用户",httpMethod = "POST")
//    @RequestMapping(value = "addOrUpdateUser",method = RequestMethod.POST)
//    public JSONObject addOrUpdateUser(@RequestBody User user, @RequestParam String type){
//        JSONObject jsonObject = new JSONObject();
//        user.setId(1);
//        boolean saveOrUpdate = userService.insertOrUpdate(user);
//        if(saveOrUpdate){
//            jsonObject.put("status","true");
//            jsonObject.put("msg","update".equals(type)?"更新成功":"新增成功");
//        }else{
//            jsonObject.put("status","false");
//            jsonObject.put("msg","update".equals(type)?"更新失败":"新增失败");
//        }
//        return jsonObject;
//    }
//    @ApiOperation(value = "findAll",httpMethod = "POST",notes = "查找所有数据")
//    @RequestMapping(value = "findAll",method = RequestMethod.POST)
//    public JSONObject findAll(@RequestParam Integer pageSize,@RequestParam Integer startPage){
//        JSONObject jsonObject = new JSONObject();
//        List<?> users = userService.selectUserAll(pageSize, startPage);
//        if(users != null && users.size() != 0){
//            jsonObject.put("status","true");
//            jsonObject.put("data",users);
//        }else{
//            jsonObject.put("status","false");
//            jsonObject.put("msg","暂无数据");
//        }
//        return jsonObject;
//    }
//    @ApiOperation(value = "findByName",notes = "根据名字进行查找分页",httpMethod = "POST")
//    @RequestMapping(value = "findByName",method = RequestMethod.POST)
//    public JSONObject findByName(@RequestParam String name,@RequestParam Integer pageSize,@RequestParam Integer startPage){
//        JSONObject jsonObject = new JSONObject();
//        List<?> users = userService.selectUserPage(name, pageSize, startPage);
//        if(users != null && users.size() != 0){
//            jsonObject.put("status","true");
//            jsonObject.put("data",users);
//        }else{
//            jsonObject.put("status","false");
//            jsonObject.put("msg","暂无数据");
//        }
//        return jsonObject;
//    }
//
//}
