import Vue from 'vue'
import Vuex from 'vuex'
import {isDef} from "element-ui";

Vue.use(Vuex)

export default{
    state: {
        // 菜单信息
        menuList: [],
        // 权限信息
        permList: [],
        // 是否请求过路由
        hasRoute:false,

        // 动态标签页
        editableTabsValue: 'Index',
        editableTabs: [{
            title: '首页',
            name: 'Index',
        }],
    },
    mutations: {
        setMenuList(state, menus) {
            state.menuList = menus
        },
        setPermList(state, perms) {
            state.permList = perms
        },
        // 更新状态，并保存到session中
        changeRouteStatus(state,hasRoute){
            state.hasRoute = hasRoute
            sessionStorage.setItem("hasRoute",hasRoute)
        },
        addTab(state,tab){
            // 先判断是否存在tab
            let index = state.editableTabs.findIndex(e=> e.name === tab.name)
            if (index === -1){
                state.editableTabs.push({
                    title:tab.title,
                    name:tab.name,
                });
            }

            state.editableTabsValue = tab.name;
        },
        // 清除登录信息
        resetState: (state) => {
            state.menuList = []
            state.permList = []

            state.hasRoute = false
            state.editableTabsValue = 'Index'
            state.editableTabs =  [{
                title: '首页',
                name: 'Index',
            }]

        }
    },
    actions: {},
}
