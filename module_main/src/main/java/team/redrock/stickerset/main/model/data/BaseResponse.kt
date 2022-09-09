package team.redrock.stickerset.main.model.data

import team.redrock.rain.lib.common.network.ApiException

data class BaseResponse<T>(
    val ok: Boolean,
    val result: T
) {
    fun warp(): Result<T> {
        if (ok) {
            return Result.success(result)
        }
        return Result.failure(RuntimeException("请求失败！"))
    }
}