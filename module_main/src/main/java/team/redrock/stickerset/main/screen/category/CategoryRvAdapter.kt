package team.redrock.stickerset.main.screen.category

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import team.redrock.rain.lib.common.extensions.gone
import team.redrock.rain.lib.common.extensions.visible
import team.redrock.stickerset.main.R
import team.redrock.stickerset.main.databinding.ItemCategoryBinding
import team.redrock.stickerset.main.model.database.entity.StickerSetEntity
import team.redrock.stickerset.main.utils.autoFitWidth
import java.io.File

class CategoryRvAdapter(private val onClick: (entity: StickerSetEntity) -> Unit) : ListAdapter<StickerSetEntity, CategoryRvAdapter.Holder>(ItemDiffCallBack()) {
    inner class Holder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                onClick(getItem(bindingAdapterPosition))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val data = getItem(position)
        holder.binding.apply {
            progressBar.visible()
            tvId.text = data.name
            tvTitle.text = data.title
            data.imgPath
            Glide.with(sivCategory)
                .load(data.imgPath?.let { File(it) })
                .placeholder(R.drawable.ic_baseline_error_24)
                .listener(object : RequestListener<Drawable?> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable?>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.gone()
                        return true
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable?>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.gone()
                        return false
                    }
                }).into(sivCategory)
        }
    }

    class ItemDiffCallBack : DiffUtil.ItemCallback<StickerSetEntity>() {
        override fun areItemsTheSame(
            oldItem: StickerSetEntity,
            newItem: StickerSetEntity
        ): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(
            oldItem: StickerSetEntity,
            newItem: StickerSetEntity
        ): Boolean {
            return oldItem == newItem
        }
    }
}