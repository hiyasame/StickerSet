package team.redrock.stickerset.main.utils

import android.R.attr
import android.graphics.drawable.Drawable
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlin.math.roundToInt


fun RequestBuilder<Drawable?>.autoFitWidth(imageView: ImageView): RequestBuilder<Drawable?> {
    return listener(object : RequestListener<Drawable?> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable?>?,
            isFirstResource: Boolean
        ): Boolean {
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable?>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            if (imageView.scaleType !== ImageView.ScaleType.FIT_XY) {
                imageView.scaleType = ImageView.ScaleType.FIT_XY
            }
            val params: ViewGroup.LayoutParams = imageView.layoutParams
            val vw: Int =
                imageView.width - imageView.paddingLeft - imageView.paddingRight
            val scale = vw.toFloat() / resource!!.intrinsicWidth.toFloat()
            val vh =
                (resource.intrinsicHeight * scale).roundToInt()
            params.height = vh + imageView.paddingTop + imageView.paddingBottom
            imageView.layoutParams = params
            return false
        }
    })
}