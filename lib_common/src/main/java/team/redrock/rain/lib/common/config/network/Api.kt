package team.redrock.rain.lib.common.config.network

/**
 * ...
 * @author 985892345 (Guo Xiangrui)
 * @email 2767465918@qq.com
 * @date 2022/5/29 22:33
 */

const val DEBUG_URL = "https://api.telegram.org/"
const val RELEASE_URL = DEBUG_URL

fun getBaseUrl() = DEBUG_URL