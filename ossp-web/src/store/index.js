import Vue from 'vue'
import Vuex from 'vuex'
import menus from "./modules/menus"

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        token: ''
    },
    mutations: {
        // 将传递的TOKEN放入到localStorage中
        SET_TOKEN: (state, token) => {
            state.token = token
            localStorage.setItem("token", token)
        },
        // 清除登录信息
        resetState: (state) => {
            state.token = ''
        }
    },
    actions: {},
    modules: {
        menus,
    }
})
