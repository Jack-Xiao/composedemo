# 第四周学习计划：Compose 主题和动画

## 学习目标

根据整体学习计划，第四周的学习内容包括：
- 学习 Material Design 主题在 Compose 中的应用
- 掌握自定义主题和样式
- 学习 Compose 中的动画系统
- 了解过渡动画和手势动画
- 实践：为应用添加主题切换和动画效果

## 详细学习内容

### 1. Material Design 主题基础

#### 1.1 MaterialTheme 组件
- 了解 MaterialTheme 的构成：colorScheme, typography, shapes
- 学习如何使用 MaterialTheme 包装整个应用或部分界面
- 掌握如何在组件中引用主题属性

#### 1.2 主题颜色系统
- 学习 lightColorScheme 和 darkColorScheme 的使用
- 了解 Material 3 主题颜色系统
- 掌握颜色语义化命名（primary, onPrimary 等）

#### 实践项目：
1. 创建一个简单的 ThemeDemo 应用，展示 MaterialTheme 的基本使用

### 2. 自定义主题和样式

#### 2.1 自定义颜色方案
- 学习如何创建自定义的颜色方案
- 掌握动态生成颜色方案的方法（如基于种子颜色）

#### 2.2 自定义字体样式
- 学习 Typography 使用和自定义
- 掌握自定义字体的加载和应用

#### 2.3 主题切换
- 实现浅色/深色主题切换
- 学习响应系统主题变化

#### 实践项目：
1. 扩展 ThemeDemo，添加自定义主题和主题切换功能

### 3. Compose 基础动画

#### 3.1 状态动画
- 学习 animateXxxAsState 系列函数
- 掌握不同的动画规格（tween, spring 等）

#### 3.2 可见性动画
- 学习 AnimatedVisibility 的使用
- 掌握各种进入和退出效果

#### 3.3 多值动画
- 学习 updateTransition 的使用
- 掌握 Transition 中多值协同动画

#### 实践项目：
1. 创建 AnimationBasicDemo，演示各种基础动画效果

### 4. 高级动画效果

#### 4.1 无限动画
- 学习 InfiniteTransition 的使用
- 掌握循环动画和脉动效果

#### 4.2 内容动画
- 学习 AnimatedContent 的使用
- 掌握内容变化时的动画效果

#### 4.3 手势动画
- 学习将手势与动画结合
- 掌握拖拽、滑动等手势与动画联动

#### 实践项目：
1. 创建 AdvancedAnimationDemo，演示高级动画效果

### 5. 综合实践项目

#### 5.1 主题与动画结合
- 创建具有动画主题切换效果的应用
- 实现平滑的颜色过渡效果

#### 5.2 创建动画组件库
- 封装常用的动画效果为可复用组件
- 创建动画工具类

#### 最终项目：
开发一个简单的应用，包含：
1. 自定义主题设计
2. 浅色/深色主题切换（带平滑过渡）
3. 各种界面动画效果
4. 手势驱动的动画交互

## 学习资源

1. 官方文档：
   - [Material Design in Compose](https://developer.android.com/jetpack/compose/themes/material)
   - [Compose 动画文档](https://developer.android.com/jetpack/compose/animation)

2. 代码示例：
   - [Compose Samples - Crane](https://github.com/android/compose-samples/tree/main/Crane)（主题示例）
   - [Compose Samples - Animation](https://github.com/android/compose-samples/tree/main/Animation)

3. 视频教程：
   - Material Design in Compose (Android Developers YouTube)
   - Animation in Compose (Android Developers YouTube)

## 学习步骤

### 第1天：Material 主题基础
- 学习 MaterialTheme 的基本结构
- 尝试应用默认主题并修改主题颜色

### 第2天：自定义主题
- 创建自定义颜色方案
- 自定义 Typography 和 Shapes

### 第3天：主题切换
- 实现浅色/深色主题切换
- 测试响应系统主题变化

### 第4天：基础动画
- 学习 animateXxxAsState 系列动画
- 尝试不同的动画规格

### 第5天：可见性和内容动画
- 学习 AnimatedVisibility
- 学习 AnimatedContent

### 第6天：高级动画
- 学习 InfiniteTransition
- 尝试多值协同动画

### 第7天：手势与动画结合
- 学习手势驱动的动画
- 实现简单的手势动画效果

### 第8-10天：综合项目开发
- 将所学主题和动画知识应用到综合项目中
- 实现一个功能完整、视觉效果出色的应用 