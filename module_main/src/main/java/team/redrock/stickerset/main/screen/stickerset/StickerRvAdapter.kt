package team.redrock.stickerset.main.screen.stickerset

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import team.redrock.stickerset.main.R
import team.redrock.stickerset.main.databinding.ItemStickerBinding
import team.redrock.stickerset.main.model.database.entity.StickerEntity
import java.io.File

class StickerRvAdapter(private val onClick: (data: StickerEntity) -> Unit) : ListAdapter<StickerEntity, StickerRvAdapter.Holder>(ItemDiffCallBack()) {
    inner class Holder(val binding: ItemStickerBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                onClick(getItem(bindingAdapterPosition))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(ItemStickerBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val data = getItem(position)!!
        holder.binding.apply {
            Glide.with(sivSticker)
                .load(File(data.imgPath))
                .placeholder(R.drawable.ic_baseline_error_24)
                .into(sivSticker)
        }
    }

    class ItemDiffCallBack : DiffUtil.ItemCallback<StickerEntity>() {
        override fun areItemsTheSame(oldItem: StickerEntity, newItem: StickerEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: StickerEntity, newItem: StickerEntity): Boolean {
            return oldItem == newItem
        }
    }
}