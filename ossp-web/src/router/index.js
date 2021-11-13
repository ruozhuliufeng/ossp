import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import Index from '../views/Index.vue'
import User from '../views/system/User.vue'
import Menu from '../views/system/Menu.vue'
import Role from '../views/system/Role.vue'
import axios from "axios";
import store from "../store"

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'Home',
        component: Home,
        children: [
            {
                path: '/index',
                name: 'Index',
                meta: {
                    title: "首页"
                },
                component: Index
            },
            {
                path: '/users',
                name: 'SysUser',
                meta: {
                    title: "用户管理"
                },
                component: User
            }, {
                path: '/roles',
                name: 'SysRole',
                meta: {
                    title: "角色管理"
                },
                component: Role
            },
            {
                path: '/menus',
                name: 'SysMenu',
                meta: {
                    title: "菜单管理"
                },
                component: Menu
            }
        ]
    },
    {
        path: '/login',
        name: 'Login',
        component: () => import('../views/Login.vue')
    }
]

const router = new VueRouter({
    mode: "history",
    base: process.env.BASE_URL,
    routes
})

router.beforeEach((to, from, next) => [
    axios.get("/sys/menu/nav", {
        headers: {
            Authorization: localStorage.getItem("token")
        }
    }).then(res => {
        console.log(res.data.data)
        // 拿到menuList
        store.commit("setMenuList", res.data.data.nav)
        // 获取用户权限
        store.commit("setPermList", res.data.data.authorities)
    }),
    next()
])

export default router
