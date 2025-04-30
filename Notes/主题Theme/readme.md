主题可以在res/values/下的styles.xml文件中修改。同时，主题的切换可以在SettingsActivity中修改。不同的页面可以使用不同的主题，在activity对应的xml文件中修改。
```
<android:theme="@style/……"/>
```

### 关于暗色模式的切换
res/
├── values/           # 默认 (日间模式)
└── values-night/     # 暗色模式专用
建议将两种主题分开管理
是 Android 系统识别的官方限定符，如果改为其他名称，系统将无法自动识别