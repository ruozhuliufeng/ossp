// 引用Mock.js
const Mock = require('mockjs')

const Random = Mock.Random

let Result = {
    code:200,
    msg:'操作成功',
    data:null
}

Mock.mock('/captcha','get',()=>{

    Result.data = {
        token:Random.string(32),
        captchaImg:Random.dataImage('100x40','p7n5w')
    }
    return Result
})
// 登录测试
Mock.mock('/login','post',()=>{
    Result.code =500
    Result.msg = "验证码有误"
    return Result
})
// 获取用户信息
Mock.mock('/sys/userInfo','get',()=>{

    Result.data = {
        id: "1",
        username: "若竹流风",
        avatar: "http://img.aixuxi.cn/20210313202334.png"
    }
    return Result
})
// 退出
Mock.mock("/logout","post",()=>{
    return Result
})
// 登录
Mock.mock('/sys/menu/nav','get',()=>{
    let nav = [
        {
            title: '系统管理',
            name: 'SysManage',
            icon: 'el-icon-s-operation',
            component: '',
            path: '',
            children: [
                {
                    title: '用户管理',
                    name: 'SysUser',
                    icon: 'el-icon-s-operation',
                    path: '/sys/users',
                    component: 'system/User',
                    children: []
                }
            ]
        },
        {
            title: '系统工具',
            name: 'SysTools',
            icon: 'el-icon-s-tools',
            path: '',
            component: '',
            children: [
                {
                    title: '系统字典',
                    name: 'SysDict',
                    icon: 'el-icon-s-order',
                    path: '/sys/dicts',
                    component: 'system/Dict',
                    children: []
                }
            ]
        }
    ];
    let authorities = []
    Result.data = {
        nav:nav,
        authorities:authorities
    }
    return Result
})
