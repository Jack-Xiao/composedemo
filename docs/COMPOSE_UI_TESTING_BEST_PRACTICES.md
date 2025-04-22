# Compose UI 测试最佳实践

## 基本原则

1. **单一职责**：每个测试应该专注于测试一个特定的功能或行为。
2. **独立性**：测试应该彼此独立，不依赖于其他测试的执行结果。
3. **可重复性**：测试应该在任何环境中都能产生相同的结果。
4. **可读性**：测试代码应该清晰易读，便于其他开发者理解。
5. **可维护性**：测试应该易于维护和更新。

## Compose UI 测试特定技巧

### 组件查找

```kotlin
// 通过语义属性查找组件
composeTestRule.onNodeWithText("标题").assertIsDisplayed()
composeTestRule.onNodeWithTag("submit_button").assertIsDisplayed()
composeTestRule.onNodeWithContentDescription("返回按钮").assertIsDisplayed()

// 使用组合条件
composeTestRule.onNode(
    hasText("提交") and hasClickAction() and isEnabled()
).assertIsDisplayed()
```

### 交互测试

```kotlin
// 点击操作
composeTestRule.onNodeWithText("提交").performClick()

// 输入文本
composeTestRule.onNodeWithTag("username_input").performTextInput("用户名")

// 滑动操作
composeTestRule.onNodeWithTag("slider").performTouchInput { 
    swipeRight() 
}

// 长按操作
composeTestRule.onNodeWithText("项目").performTouchInput {
    longClick()
}
```

### 异步操作处理

```kotlin
// 等待特定条件满足
composeTestRule.waitUntil(timeoutMillis = 5000) {
    composeTestRule.onAllNodesWithText("加载完成").fetchSemanticsNodes().size == 1
}

// 使用 awaitIdle 等待组合完成
composeTestRule.awaitIdle()
```

### 测试生命周期感知组件

1. **LaunchedEffect 测试**：
   - 验证初始状态
   - 触发启动条件
   - 验证副作用结果

2. **DisposableEffect 测试**：
   - 验证资源创建
   - 验证资源释放

3. **SideEffect 测试**：
   - 验证外部状态变化

4. **rememberCoroutineScope 测试**：
   - 验证协程任务的执行状态
   - 测试任务完成后的状态更新

## 常见陷阱与解决方案

1. **闪烁测试**：避免依赖于时间的测试，使用 `waitUntil` 或 `awaitIdle` 替代固定延迟。

2. **状态管理**：每个测试开始时重置状态，避免测试间的状态泄漏。

3. **过度指定**：避免测试实现细节，而应测试行为。

4. **隐式依赖**：明确所有测试的依赖项，避免隐式依赖。

5. **动画处理**：在测试中禁用动画以提高稳定性：
   ```kotlin
   @get:Rule
   val composeTestRule = createComposeRule().apply {
       mainClock.autoAdvance = false
   }
   ```

## 调试技巧

1. **打印语义树**：
   ```kotlin
   composeTestRule.onRoot().printToLog("组件树")
   ```

2. **自定义失败消息**：
   ```kotlin
   composeTestRule.onNodeWithText("提交")
       .assertIsDisplayed("提交按钮应该可见")
   ```

3. **捕获屏幕截图**：使用 `UiAutomator` 或自定义扩展函数捕获测试失败时的屏幕截图。

## 测试架构

1. **页面对象模式**：为每个屏幕创建页面对象类，封装与该屏幕相关的所有交互。

2. **Robot 模式**：创建"机器人"类来执行特定的用户流程，如登录、导航等。

## 性能考虑

1. **测试分类**：根据执行速度和复杂性对测试进行分类（快速、中等、慢速）。

2. **测试并行化**：配置测试以并行运行，提高执行速度。

3. **资源清理**：确保每个测试在完成后都释放所有资源。 