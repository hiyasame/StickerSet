package team.redrock.stickerset.main.screen.stickerset

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import team.redrock.rain.lib.common.extensions.appContext
import team.redrock.stickerset.main.R
import team.redrock.stickerset.main.databinding.ActivityStickerSetBinding
import team.redrock.stickerset.main.model.database.entity.StickerEntity
import team.redrock.stickerset.main.model.database.entity.StickerSetEntity
import team.redrock.stickerset.main.mvi.MVIActivity
import team.redrock.stickerset.main.screen.category.CategoryViewAction
import team.redrock.stickerset.main.utils.FetchStatus
import java.io.File

class StickerSetActivity : MVIActivity<StickerSetViewModel, ActivityStickerSetBinding, StickerViewEvent, StickerViewStates>() {

    private val data by intent<StickerSetEntity>()
    private val rvAdapter: StickerRvAdapter = StickerRvAdapter {
        shareSticker(it)
    }

    override fun initView() {
        viewModel.data = data
        binding.rvStickerSet.apply {
            adapter = rvAdapter
            layoutManager = GridLayoutManager(this@StickerSetActivity, 5)
        }
        setSupportActionBar(binding.toolbar)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            title = data.title
            subtitle = data.name
        }
        binding.srlStickerSet.setOnRefreshListener {
            viewModel.dispatch(StickerViewAction.OnSwipeRefresh)
        }
    }

    override fun StateFlow<StickerViewStates>.launchEffects() {
        useEffect(StickerViewStates::stickerSets) {
            rvAdapter.submitList(it)
        }
        useEffect(StickerViewStates::fetchStatus) {
            when (it) {
                FetchStatus.NoFetched -> {
                    lifecycleScope.launch {
                        viewModel.dispatch(StickerViewAction.FetchData)
                    }
                    binding.srlStickerSet.isRefreshing = false
                }
                FetchStatus.Fetched -> {
                    binding.srlStickerSet.isRefreshing = false
                }
                FetchStatus.Fetching -> {
                    binding.srlStickerSet.isRefreshing = true
                }
            }
        }
    }

    override fun renderViewEvent(viewEvent: StickerViewEvent) {
        when (viewEvent) {
            is StickerViewEvent.Toast -> {
                Toasty.success(appContext, viewEvent.content, Toasty.LENGTH_SHORT).show()
            }
            StickerViewEvent.Exit -> finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.sticker_set_toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete -> {
                // 删除分类
                viewModel.dispatch(StickerViewAction.DeleteStickerSet)
            }
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun shareSticker(sticker: StickerEntity) {
        Intent.createChooser(
            Intent().apply {
                action = Intent.ACTION_SEND
                type = "image/*"
                putExtra(
                    Intent.EXTRA_STREAM,
                    FileProvider.getUriForFile(appContext, "team.redrock.stickerset.file_provider", File(sticker.imgPath))
                )
            },
            "分享"
        ).let { startActivity(it) }
    }

    companion object {
        @JvmStatic
        fun start(context: Context, entity: StickerSetEntity) {
            val starter = Intent(context, StickerSetActivity::class.java)
                .putExtra("data", entity)
            context.startActivity(starter)
        }
    }
}