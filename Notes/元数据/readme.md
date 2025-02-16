### 使用元数据注册应用快捷方式时，android:name 属性通常必须设置为 "android.app.shortcuts"
```xml
<meta-data android:name="android.app.shortcuts" android:resource="……"/>
```
### 快捷方式的标签（Label）
shortcutShortLabel：是快捷方式的短标签，通常用于在空间有限的地方显示，比如主屏幕上长按应用图标弹出的快捷方式菜单。
shortcutLongLabel：是快捷方式的长标签，提供更详细的描述，一般在有足够空间时显示，例如快捷方式的上下文菜单。
