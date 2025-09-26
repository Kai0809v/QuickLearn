这里通过对比 Java 和 Kotlin 的函数结构来帮助理解：
Java 函数结构示例：
```java
// 访问修饰符 返回类型 函数名(参数类型 参数名) { 函数体 }
public String getMessage(int count) {
    return "Count: " + count;
}
```
Kotlin 函数结构示例：
```kotlin
// 访问修饰符 返回类型 函数名(参数类型 参数名) { 函数体 }
fun getMessage(count: Int): String {
    return "Count: $count"
}
```
主要差异解析：
- 关键字顺序不同 Java：public 返回类型 函数名(参数)
public void showDialog(String title) { ... }
Kotlin：可见性修饰符 fun 函数名(参数): 返回类型
fun showDialog(title: String): Unit { ... } // Unit 可省略
- 参数声明方式 Java：类型在前，参数名在后
(int width, int height)
Kotlin：参数名在前，类型在后（用冒号分隔）
(width: Int, height: Int)
- 单表达式简写（Kotlin 特有）
// 当函数体只有一行时，用 = 代替大括号
fun square(n: Int) = n * n

// 等价于
fun square(n: Int): Int {
return n * n
}
- 默认参数值（Kotlin 特有）
// 给参数设置默认值
fun connect(timeout: Int = 5000, retry: Boolean = true) {
...
}

// 调用时可选择性传参
connect() // 使用默认值
connect(retry = false) // 使用命名参数
- 函数体处理差异
必须用大括号 {} 包裹多行代码
无返回值时返回类型可声明为 Unit 或省略
使用 return 需要显式声明返回类型
扩展函数示例（Kotlin 特有）：
```kotlin
// 给 String 类添加扩展函数
fun String.reverse(): String {
    return this.reversed()
}

// 调用方式
val text = "Hello".reverse() // 得到 "olleH"
```
完整结构对照表：
组成部分           |Java 示例                    | Kotlin 示例
无参无返回值          |void init() { ... }         |fun init() { ... }
带参数             |void log(String msg)         |fun log(msg: String)
带返回值 |int sum(int a, int b)|fun sum(a: Int, b: Int): Int
表达式函数 |无| fun sum(a: Int, b: Int) = a + b
带默认值参数 |不支持| fun greet(name: String = "Guest")
当您看到类似这样的结构时：
```kotlin
fun getUser(id: Int): User? {
    // 函数体
    return userDao.find(id)
}
```
对应Java的：
```java
public User getUser(int id) {
    return userDao.find(id);
}
```
