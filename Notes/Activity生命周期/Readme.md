### Activity生命周期

activity 的生命周期内有几种方法，并且按顺序执行这些方法，需要对这些方法修改时，使用@override ……
```mermaid
graph TD
    A[应用启动] -->|创建| B[onCreate]
    B --> C[onStart]
    C --> D[onResume]
    D --> E[Activity运行]
    E -->|新Activity运行，进入前台| F[onPause]
    F -->|Activity返回前台| D
    F -->|Activity已经不可见| G[onStop]
    G -->|Activity正在停止或即将被摧毁| H[onDestroy]
    G -->|Activity返回前台| J[onRestart]
    J --> C
    H --> I[Activity关闭]
    G -->|高优先级的应用需要内存| K[应用被杀死]
    K -->|应用返回Activity| B


    class A,B,C,D,E,F,G fill:#f9f,stroke:#333,stroke-width:2px;
