package team.redrock.stickerset.main.screen.about

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.transition.Fade
import android.transition.Visibility
import android.view.MenuItem
import android.view.ViewTreeObserver
import android.view.animation.OvershootInterpolator
import team.redrock.rain.lib.common.BaseApp.Companion.app
import team.redrock.rain.lib.common.extensions.makeSceneTransitionAnimation
import team.redrock.rain.lib.common.ui.BaseBindActivity
import team.redrock.stickerset.main.databinding.ActivityAboutBinding

class AboutActivity : BaseBindActivity<ActivityAboutBinding>() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 动画
        window.enterTransition = buildEnterTransition()
        postponeEnterTransition()
        val decorView = window.decorView
        window.decorView.viewTreeObserver.addOnPreDrawListener(object :
            ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                decorView.viewTreeObserver.removeOnPreDrawListener(this)
                supportStartPostponedEnterTransition()
                return true
            }
        })
        binding.apply {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            tvVersion.text = "Version: ${app.version}"
            llGithub.setOnClickListener {
                openGitHubRepo()
            }
        }
    }

    private fun openGitHubRepo() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/ColdRain-Moro/StickerSet/")))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finishAfterTransition()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun buildEnterTransition(): Visibility {
        val slide = Fade()
        slide.duration = 500
        slide.interpolator = OvershootInterpolator(0.5F)
        return slide
    }

    companion object {
        @JvmStatic
        fun start(activity: Activity) {
            val starter = Intent(activity, AboutActivity::class.java)
            activity.startActivity(starter, activity.makeSceneTransitionAnimation())
        }
    }
}