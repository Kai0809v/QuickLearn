    很长一段时间都被一个问题困扰，那就是为什么有新数据插入的时候，页面不会自动刷新，而删除数据的时候，页面会自动刷新。
后来发现是观察的viewModel不一致，history观察的是history的viewModel，而插入操作是在NotificationMonitor中，所以导致了页面不会自动刷新。
### 问题原因——ViewModel 实例隔离

### 插入操作无法刷新的原因

history Activity 通过 ViewModelProvider(this).get(NotificationViewModel.class) 获取的 ViewModel 实例与当前 Activity 绑定。
</br>
NotificationMonitor（作为服务）通过 new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(NotificationViewModel.class) 创建的是一个新的全局实例，与 Activity 的 ViewModel 不是同一个对象。
</br>
因此，当 NotificationMonitor 插入数据并调用 viewModel.insertNotification() 时，更新的是独立的 ViewModel 实例，而 history 观察的是另一个实例的 LiveData，导致界面无法刷新。

### 删除操作正常刷新的原因

删除操作是在 history 中通过 viewModel.deleteAllNotifications() 触发的，此处的 viewModel 是 Activity 绑定的正确实例，因此能触发 LiveData 更新。

    TingFeng app = (TingFeng) getApplication();
这段代码里，(TingFeng) 是类型转换操作符，其作用是把 getApplication() 方法返回的对象强制转换为 TingFeng 类型。</br>
    详细解释
getApplication() 方法：这是 Context 类的一个方法，在 NotificationMonitor 类里可以调用，它返回的是应用的 Application 对象。</br>
默认情况下，getApplication() 返回的是 android.app.Application 类型的对象。</br>
类型转换：要是你自定义了一个继承自 android.app.Application 的类，例如 TingFeng 类，并且在 AndroidManifest.xml 里把这个自定义的 Application 类指定为应用的 Application，那么 getApplication() 返回的实际上就是 TingFeng 类的实例。不过，由于 getApplication() 方法的返回类型是 Application，所以需要使用 (TingFeng) 进行强制类型转换，从而把返回的对象当作 TingFeng 类型来使用。