import axios from "axios"
import route from "./router"
import Element from "element-ui";

// 默认URL
// axios.defaults.baseURL = "https://localhost:88/api"

const request = axios.create({
    // baseURL:"/api",
    // 设计超时时间
    timeout: 5000,
    headers:{
        'Content-Type':"application/json,charset=utf-8"
    }
})

// 请求前置拦截
request.interceptors.request.use(config=>{
    // 请求前携带jwt认证信息
    config.headers["Authorization"] = localStorage.getItem("token")
    return config
})
// 请求后置拦截 ==
request.interceptors.response.use(response=>{
    // 判断请求状态
    let res = response.data;
    if (res.code === 200){
        return response
    }else {
        Element.Message.error(!res.msg?'系统异常':res.msg)
        // 返回有误，拒绝下面的请求
        return Promise.reject(response.data.msg());
    }

},error => {
    // 服务器异常处理
    if (error.response.data){
        error.message = error.response.data.msg
    }

    if (error.response.status === 401){
        // 没有权限，跳转到登录页
        route.push("/login")
    }
     Element.Message.error(error.message,{duration:3000})

    return Promise.reject(error)
})

export default request
