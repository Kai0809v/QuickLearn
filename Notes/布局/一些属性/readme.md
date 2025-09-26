### ?属性
? 符号表示引用当前主题中的属性值
假如在主题中定义了一个颜色属性：colorAccent，那么想使用这个颜色，就可以使用：
```#xml
android:textColor="?attr/colorAccent"
app:tint="?attr/colorAccent"
……
```
使用 ?attr/ 引用主题属性的好处是：
- 可以轻松实现主题切换
- 保持应用 UI 的一致性
- 方便统一修改颜色方案

# android:layout_weight="1"，布局权重为1，会平分剩余空间。