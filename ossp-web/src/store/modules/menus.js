import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default{
    state: {
        // 菜单信息
        menuList: [],
        // 权限信息
        permList: [],
    },
    mutations: {
        setMenuList(state, menus) {
            state.menuList = menus
        },
        setPermList(state, perms) {
            state.permList = perms
        },
    },
    actions: {},
}
