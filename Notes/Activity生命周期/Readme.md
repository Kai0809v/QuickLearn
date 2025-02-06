### Activity生命周期

activity 的生命周期内有几种方法，并且按顺序执行这些方法，需要对这些方法修改时，使用@override ……
```mermaid
graph TD
    A[App启动] --> B[onCreate()]
    B --> C[onStart()]
    C --> D[onResume()]
    D --> E[Activity运行中]
    E --> F[onPause()]
    F --> G{判断是否完全不可见}
    G -->|是| H[onStop()]
    H --> I[onDestroy()]
    I --> J[App结束]
    G -->|否| E
    H --> K[onRestart()]
    K --> C
