import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import Index from '../views/Index.vue'
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

router.beforeEach((to, from, next) => {
    // 判断是否获取过路由
    let hasRoute = store.state.hasRoute
    if (!hasRoute) {
        axios.get("/sys/menu/nav", {
            headers: {
                Authorization: localStorage.getItem("token")
            }
        }).then(res => {
            // 拿到menuList
            store.commit("setMenuList", res.data.data.nav)
            // 获取用户权限
            store.commit("setPermList", res.data.data.authorities)
            // 动态绑定路由
            let newRoutes = router.options.routes
            res.data.data.nav.forEach(menu => {
                // 第一级是菜单，没有路由
                if (menu.children) {
                    menu.children.forEach(e => {
                        // 转成路由
                        let route = menuToRoute(e)
                        // 把路由添加到路由管理中
                        if (route) {
                            newRoutes[0].children.push(route)
                        }
                    })
                }
            })
            router.addRoutes(newRoutes)
            // 已查询过路由
            hasRoute = true
            store.commit("changeRouteStatus", hasRoute)
        })
    }
    next()
})

// 导航转成路由
const menuToRoute = (menu) => {
    if (!menu.component) {
        return null
    }
    let route = {
        name: menu.name,
        path: menu.path,
        meta: {
            icon: menu.icon,
            title: menu.title
        }
    }
    route.component = () => import('@/views/' + menu.component + '.vue')
    return route
}
export default router
