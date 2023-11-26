package com.yjq.programmer.bean;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2020-09-19 23:14
 */

/**
 * 错误码统一处理类，所有的错误码统一定义在这里
 */
public class CodeMsg {

    private Integer code;//错误码

    private String msg;//错误信息

    /**
     * 构造函数私有化即单例模式
     * 该类负责创建自己的对象，同时确保只有单个对象被创建。这个类提供了一种访问其唯一的对象的方式，可以直接访问，不需要实例化该类的对象。
     * @param code
     * @param msg
     */
    private CodeMsg(Integer code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public CodeMsg() {

    }

    public Integer getCode() {
        return code;
    }



    public void setCode(Integer code) {
        this.code = code;
    }



    public String getMsg() {
        return msg;
    }



    public void setMsg(String msg) {
        this.msg = msg;
    }

    //通用错误码定义
    //处理成功消息码
    public static CodeMsg SUCCESS = new CodeMsg(0, "success");
    //通用数据错误码
    public static CodeMsg DATA_ERROR = new CodeMsg(-1, "非法数据！");
    public static CodeMsg VALIDATE_ENTITY_ERROR = new CodeMsg(-2, "");
    public static CodeMsg CAPTCHA_EMPTY = new CodeMsg(-3, "验证码不能为空!");
    public static CodeMsg NO_PERMISSION = new CodeMsg(-4, "您没有当前操作的权限哦！");
    public static CodeMsg CAPTCHA_ERROR = new CodeMsg(-5, "验证码错误！");
    public static CodeMsg USER_SESSION_EXPIRED = new CodeMsg(-6, "还未登录或会话失效，请重新登录！");
    public static CodeMsg UPLOAD_PHOTO_SUFFIX_ERROR = new CodeMsg(-7, "图片格式不正确！");
    public static CodeMsg PHOTO_SURPASS_MAX_SIZE = new CodeMsg(-8, "上传的图片不能超过1MB！");
    public static CodeMsg PHOTO_FORMAT_NOT_CORRECT = new CodeMsg(-9, "上传的图片格式不正确！");
    public static CodeMsg SAVE_FILE_EXCEPTION = new CodeMsg(-10, "保存文件异常！");
    public static CodeMsg FILE_EXPORT_ERROR = new CodeMsg(-11, "文件导出失败！");
    public static CodeMsg SYSTEM_ERROR = new CodeMsg(-12, "系统出现了错误，请联系管理员！");
    public static CodeMsg NO_AUTHORITY = new CodeMsg(-13, "不好意思，您没有权限操作哦！");
    public static CodeMsg CAPTCHA_EXPIRED = new CodeMsg(-14, "验证码已过期，请刷新验证码！");
    public static CodeMsg COMMON_ERROR = new CodeMsg(-15, "");
    public static CodeMsg PHOTO_EMPTY = new CodeMsg(-16, "上传的图片不能为空！");
    public static CodeMsg FILE_EMPTY = new CodeMsg(-17, "上传的文件不能为空！");
    public static CodeMsg FILE_SURPASS_MAX_SIZE = new CodeMsg(-18, "上传的文件不能超过100MB！");

    //用户管理类错误码
    public static CodeMsg USER_ADD_ERROR = new CodeMsg(-1000, "用户信息添加失败，请联系管理员！");
    public static CodeMsg USER_NOT_EXIST  = new CodeMsg(-1001, "该用户不存在！");
    public static CodeMsg USER_EDIT_ERROR = new CodeMsg(-1002, "用户信息编辑失败，请联系管理员！");
    public static CodeMsg USER_DELETE_ERROR = new CodeMsg(-1003, "用户信息删除失败，请联系管理员！");
    public static CodeMsg USERNAME_EXIST = new CodeMsg(-1004, "用户名称重复，请换一个！");
    public static CodeMsg USERNAME_EMPTY = new CodeMsg(-1005, "用户名称不能为空！");
    public static CodeMsg PASSWORD_EMPTY = new CodeMsg(-1006, "用户密码不能为空！");
    public static CodeMsg USERNAME_PASSWORD_ERROR = new CodeMsg(-1007, "学号/学工号或密码错误！");
    public static CodeMsg REPASSWORD_EMPTY = new CodeMsg(-1008, "确认密码不能为空！");
    public static CodeMsg REPASSWORD_ERROR = new CodeMsg(-1009, "确认密码不一致！");
    public static CodeMsg USER_REGISTER_ERROR = new CodeMsg(-1010, "注册用户失败，请联系管理员！");
    public static CodeMsg USER_NOT_IS_ADMIN = new CodeMsg(-1011, "只有管理员角色才能登录后台系统！");
    public static CodeMsg ROLE_EMPTY = new CodeMsg(-1012, "用户角色不能为空！");
    public static CodeMsg USER_NO_EMPTY = new CodeMsg(-1013, "学号/学工号不能为空！");
    public static CodeMsg USER_NO_EXIST = new CodeMsg(-1014, "学号/学工号重复，请换一个！");

    //竞赛管理类错误码
    public static CodeMsg CONTEST_ADD_ERROR = new CodeMsg(-2000, "竞赛信息添加失败，请联系管理员！");
    public static CodeMsg CONTEST_EDIT_ERROR = new CodeMsg(-2001, "竞赛信息编辑失败，请联系管理员！");
    public static CodeMsg CONTEST_DELETE_ERROR = new CodeMsg(-2002, "竞赛信息删除失败，请联系管理员！");
    public static CodeMsg CONTEST_NOT_EXIST = new CodeMsg(-2003, "竞赛信息不存在！");
    public static CodeMsg CONTEST_STATE_ERROR = new CodeMsg(-2004, "此竞赛已结束报名！");
    public static CodeMsg CONTEST_CONDITION_ERROR = new CodeMsg(-2005, "未达到报名要求，报名失败！");

    //报名管理类错误码
    public static CodeMsg SIGN_ADD_ERROR = new CodeMsg(-3000, "报名失败，请联系管理员！");
    public static CodeMsg SIGN_DELETE_ERROR = new CodeMsg(-3001, "报名信息删除失败，请联系管理员！");
    public static CodeMsg SIGN_EXIST = new CodeMsg(-3002, "您已经报名过此竞赛，请换一个！");
    public static CodeMsg SIGN_EDIT_ERROR = new CodeMsg(-3003, "报名信息审核失败，请联系管理员！");

    //作品管理类错误码
    public static CodeMsg WORK_ADD_ERROR = new CodeMsg(-4000, "作品信息添加失败，请联系管理员！");
    public static CodeMsg WORK_EDIT_ERROR = new CodeMsg(-4001, "作品信息编辑失败，请联系管理员！");
    public static CodeMsg WORK_DELETE_ERROR = new CodeMsg(-4002, "作品信息删除失败，请联系管理员！");
    public static CodeMsg WORK_ALREADY_UPLOAD = new CodeMsg(-4003, "此竞赛您已经上传过作品了，请在作品列表查看！");
    public static CodeMsg WORK_IS_OVER = new CodeMsg(-2004, "此竞赛已结束，不能再上传作品！");
    //成绩管理类错误码
    public static CodeMsg RESULT_ADD_ERROR = new CodeMsg(-5000, "成绩信息添加失败，请联系管理员！");
    public static CodeMsg RESULT_EDIT_ERROR = new CodeMsg(-5001, "成绩信息编辑失败，请联系管理员！");
    public static CodeMsg RESULT_DELETE_ERROR = new CodeMsg(-5002, "成绩信息删除失败，请联系管理员！");
    public static CodeMsg RESULT_ALREADY_EXIST = new CodeMsg(-5003, "此作品已经打分，请在成绩列表查看！");
    public static CodeMsg RESULT_IS_OVER = new CodeMsg(-5004, "此竞赛已结束，无法保存分数！");

    //评论管理类错误码
    public static CodeMsg COMMENT_ADD_ERROR = new CodeMsg(-6000, "评论信息添加失败，请联系管理员！");
    public static CodeMsg COMMENT_EDIT_ERROR = new CodeMsg(-6001, "评论信息编辑失败，请联系管理员！");
    public static CodeMsg COMMENT_DELETE_ERROR = new CodeMsg(-6002, "评论信息删除失败，请联系管理员！");

    //公告管理类错误码
    public static CodeMsg ANNOUNCE_ADD_ERROR = new CodeMsg(-7000, "公告信息添加失败，请联系管理员！");
    public static CodeMsg ANNOUNCE_EDIT_ERROR = new CodeMsg(-7001, "公告信息编辑失败，请联系管理员！");
    public static CodeMsg ANNOUNCE_DELETE_ERROR = new CodeMsg(-7002, "公告信息删除失败，请联系管理员！");
}
