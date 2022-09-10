package team.redrock.rain.lib.common.extensions

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityOptionsCompat

fun Activity.makeSceneTransitionAnimation(
    vararg args: Pair<View, String>
): Bundle? {
    return ActivityOptionsCompat.makeSceneTransitionAnimation(this, *args.map { androidx.core.util.Pair.create(it.first, it.second) }.toTypedArray()).toBundle()
}