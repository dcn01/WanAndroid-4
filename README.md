# info
MVVM+Coroutine+koin实现的玩Android

# 特点
- 使用协程Coroutine
- 基于Navigation，1个Activity多个Fragment
- koin实现依赖注入
- buildSrc + kotlin管理第三库依赖；动态管理版本号和tag

# 构建和使用
- 1. 需求：必须要Android Studio 4.0 以上



# Todo
- [] WebView也放置Fragment中，可能会引起很明显的内存泄漏(不是WebView泄漏，而是导致的各个Fragment中SwipeRefreshLayout内存泄漏)，需要处理。万一处理不了，只能放置在Activity中
- [] Splash引导页面必须放置Activity中，因为Navigation必须要有一个startDestion，而且还要在首页中实现AppBar/DrawerLayout/BottomNavigation,
  目前不知道怎么把Splash也放置Fragment中
- [] 升级youthbanner
- [] 升级[BaseRecyclerViewAdapterHelper](https://github.com/CymChad/BaseRecyclerViewAdapterHelper/releases)
- [] 历史记录分页查找`select * from table_name limit 10 offset 0`

