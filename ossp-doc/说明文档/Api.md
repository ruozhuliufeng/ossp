# Api说明文档

## 模块：ossp-uaa
> OSSP授权认证相关API

## 模块：ossp-user
> 用户中心相关API

### 接口：获取用户列表
* 地址：/user/list
* 类型：GET
* 状态码：200
* 简介：用户中心管理员获取用户列表
* Rap地址：[http://rap2.taobao.org/repository/editor?id=293861&mod=487739&itf=2134607](http://rap2.taobao.org/repository/editor?id=293861&mod=487739&itf=2134607)
* 请求接口格式：

```matlab
├─ page: Number (必选) (页数 分页参数)
└─ limit: Number (必选) (每页限制数量)
```

* 返回接口格式：

```
├─ code: Number  (返回编码)
├─ msg: String  (返回说明)
└─ data: Array 
   ├─ id: Number  (用户主键)
   ├─ createTime: String  (创建时间)
   ├─ updateTime: String  (修改时间)
   ├─ username: String  (用户名称)
   ├─ nickname: String  (用户昵称)
   ├─ password: String  (加密后的密码)
   ├─ headImgUrl: String  (头像地址)
   ├─ mobile: String  (手机号)
   ├─ email: String  (电子邮箱)
   ├─ sex: Number  (性别 1-男 0-女)
   ├─ enabled: Boolean  (是否启用 true：启用)
   ├─ type: String  (类型 App webApp)
   ├─ openId: String  (开放平台ID)
   ├─ roleId: String  (角色ID)
   ├─ roleIds: Array 
   │  ├─ id: Number  (角色ID)
   │  ├─ createTime: String  (创建时间)
   │  ├─ updateTime: String  (修改时间)
   │  ├─ code: String  (角色编码)
   │  ├─ name: String  (角色名称)
   │  ├─ describe: String  (角色描述)
   │  └─ userId: String  (用户ID)
   ├─ oldPassword: String  (旧密码)
   ├─ newPassword: String  (新密码)
   └─ del: Boolean  (是否删除 false：否)

```
