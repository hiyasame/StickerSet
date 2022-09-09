# StickerSet

> 一款表情包管理app，支持从`telegram`导入`StickerSet`

## Tech-Stack

- MVI架构
- jetpack navigation (用得不多)

## To-do-lists

- [ ] 多协程并发下载文件
- [ ] 使用Hook技术实现的丝滑换肤
- [ ] 弱网适配/网络请求异常处理

## MVI-Architecture

所谓MVI，分三层，Model-View-Intent。Model和View层对我们来说都是老生常谈的话题，关键在于Intent层。不同于MVVM架构中我们使用ViewModel来桥接View层与Model层间的数据流动，MVI架构中我们使用Intent进行View层与Model层数据的双向流转。而在MVI架构中，ViewModel作为Intent对应处理逻辑的载体仍然是必须的。

在本项目中，Intent这一概念可以向下细分为三个概念

- Action - view层通过向viewModel发送Action实现与viewModel的通信
- Event - viewModel向view层订阅的一个SharedFlow中发送Event，用来发送Toast，展示Dialog等
- State - view层界面的状态数据，viewModel负责更新state，view层订阅State的变化来更新UI

MVI架构让我们定义一个界面的State，并且通过在view层订阅State的变化进而更新视图，比较类似与React和Compose，这种架构主张数据驱动UI。比起MVVM（借助DataBinding），它能更好的实现数据的双向流动。

~~~kotlin
data class CategoryViewStates(
    val fetchStatus: FetchStatus = FetchStatus.NoFetched,
    val stickerSets: List<StickerSetEntity> = listOf()
) : ViewState
~~~

而我们的view层又要如何跟model层通信呢？答案是主动向ViewModel中发送一个Action

~~~kotlin
sealed class CategoryViewAction : ViewAction {
    object FabClicked : CategoryViewAction()
    object OnSwipeRefresh : CategoryViewAction()
    object FetchData : CategoryViewAction()
    class FetchStickerSet(val name: String) : CategoryViewAction()
}

// View层
srlCategory.setOnRefreshListener {
   	viewModel.dispatch(CategoryViewAction.OnSwipeRefresh)
}
fabCategory.setOnClickListener {
    viewModel.dispatch(CategoryViewAction.FabClicked)
}

// ViewModel层
override fun dispatch(action: CategoryViewAction) {
  	when (action) {
     	CategoryViewAction.OnSwipeRefresh, CategoryViewAction.FetchData -> fetchStickers()
     	CategoryViewAction.FabClicked -> fabClicked()
     	is CategoryViewAction.FetchStickerSet -> fetchStickerSets(action.name)
   	}
}
~~~

在收到一些Action时，ViewModel会执行改变State的逻辑，而View层订阅了State的变化，根据State来更新UI的状态

~~~kotlin
// ViewModel
private fun fetchStickers() {
    setState { copy(fetchStatus = FetchStatus.Fetching) }
    // 取消之前观察数据库的协程
    observeStickerSetChangeJob?.cancel()
    observeStickerSetChangeJob = viewModelScope.launch {
        TelegramRepository.fetchStickerSets().collectLatest {
          setState { copy(fetchStatus = FetchStatus.Fetched, stickerSets = it) }
        }
    }
}

// View
// useEffect是自己封装的函数，它的作用跟React的useEffect基本一致
// 在State变化时，useEffect中的逻辑会执行
useEffect(CategoryViewStates::fetchStatus) {
	when (it) {
       FetchStatus.NoFetched -> {
           lifecycleScope.launch {
                viewModel.dispatch(CategoryViewAction.FetchData)
           }
           binding.srlCategory.isRefreshing = false
       }
       FetchStatus.Fetched -> {
           binding.srlCategory.isRefreshing = false
       }
       FetchStatus.Fetching -> {
           binding.srlCategory.isRefreshing = true
       }
  	}
}
useEffect(CategoryViewStates::stickerSets) {
   	adapter.submitList(it)
}
~~~

