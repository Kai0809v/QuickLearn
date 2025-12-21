# Plugin [id: 'com.android.application', version: '8.12.3', apply: false] was not found in any of the following sources:
- 这种如果改toml文件，改为了对应版本没用，就点右边的大象图标，取消offline mode

# service.gradle……timeout
- 项目级settings.gradle.ktssettings.gradle.kts中，添加镜像源
- 或者使用offline mode

# 我曾怎么解决的？
- 无梯情况下，gradle下载了大部分资源，除了gradle 8.1x……bin，我手动下载，放到对应目录后使用offline mode再build，就成功了