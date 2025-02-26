package com.tianyejia.composedemo.week3

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * 第3周：Compose 副作用和生命周期
 * 
 * 这个示例专注于展示 Jetpack Compose 中的各种副作用处理机制：
 * 1. LaunchedEffect - 在组合时启动协程
 * 2. DisposableEffect - 需要清理的副作用
 * 3. SideEffect - 将 Compose 状态同步到非 Compose 代码
 * 4. rememberCoroutineScope - 获取协程作用域用于事件处理
 * 5. rememberUpdatedState - 捕获可能变化的值
 * 6. derivedStateOf - 创建依赖于其他状态的计算状态
 * 7. produceState - 将非 Compose 状态转换为 Compose 状态
 * 8. snapshotFlow - 将 Compose 状态转换为 Flow
 */
class ComposeEffectsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                EffectsShowcase()
            }
        }
    }
}

/**
 * 主界面 - 展示各种副作用示例
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EffectsShowcase() {
    val scrollState = rememberScrollState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Compose 副作用示例") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // 标题
            Text(
                text = "Jetpack Compose 副作用和生命周期",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            
            // 各种副作用示例
            LaunchedEffectExample()
            DisposableEffectExample()
            SideEffectExample()
            RememberCoroutineScopeExample()
            RememberUpdatedStateExample()
            DerivedStateOfExample()
            ProduceStateExample()
            SnapshotFlowExample()
        }
    }
}

/**
 * LaunchedEffect 示例
 * 
 * LaunchedEffect 用于在组合时启动协程，当键值变化时会取消并重新启动
 */
@Composable
fun LaunchedEffectExample() {
    EffectCard(title = "LaunchedEffect") {
        var count by remember { mutableStateOf(0) }
        var timerRunning by remember { mutableStateOf(false) }
        
        // 计时器状态
        var elapsedTime by remember { mutableStateOf(0) }
        
        // 使用 LaunchedEffect 实现计时器
        // 当 timerRunning 变化时，协程会被取消并重新启动
        LaunchedEffect(key1 = timerRunning) {
            // 只有当计时器运行时才执行
            if (timerRunning) {
                // 重置计时器
                elapsedTime = 0
                
                // 启动计时循环
                while (true) {
                    delay(1000) // 延迟1秒
                    elapsedTime++ // 增加计时
                }
            }
        }
        
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 显示计时器状态
            Text(
                text = if (timerRunning) "计时器运行中: ${elapsedTime}秒" else "计时器已停止",
                fontWeight = FontWeight.Medium
            )
            
            // 计数器显示
            Text(
                text = "点击次数: $count",
                style = MaterialTheme.typography.bodyMedium
            )
            
            // 按钮行
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 计时器控制按钮
                Button(
                    onClick = { timerRunning = !timerRunning },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (timerRunning) 
                            MaterialTheme.colorScheme.error 
                        else 
                            MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(if (timerRunning) "停止" else "开始")
                }
                
                // 计数按钮
                Button(
                    onClick = { count++ }
                ) {
                    Text("计数")
                }
            }
            
            // 说明文本
            Text(
                text = "LaunchedEffect 会在组合时启动协程，当键值变化时会取消并重新启动",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

/**
 * DisposableEffect 示例
 * 
 * DisposableEffect 用于需要清理的副作用，如注册和注销监听器
 */
@Composable
fun DisposableEffectExample() {
    EffectCard(title = "DisposableEffect") {
        // 获取当前生命周期所有者
        val lifecycleOwner = LocalLifecycleOwner.current
        
        // 记录生命周期事件
        val lifecycleEvents = remember { mutableStateListOf<String>() }
        
        // 使用 DisposableEffect 监听生命周期事件
        DisposableEffect(key1 = lifecycleOwner) {
            // 创建生命周期观察者
            val observer = LifecycleEventObserver { _, event ->
                // 添加事件到列表，并保持最多显示5个事件
                val timestamp = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                lifecycleEvents.add(0, "$timestamp: ${event.name}")
                if (lifecycleEvents.size > 5) {
                    lifecycleEvents.removeAt(lifecycleEvents.lastIndex)
                }
            }
            
            // 注册观察者
            lifecycleOwner.lifecycle.addObserver(observer)
            
            // 返回清理函数，在 DisposableEffect 离开组合时调用
            onDispose {
                // 注销观察者
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
        
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "生命周期事件监听",
                fontWeight = FontWeight.Medium
            )
            
            // 显示生命周期事件列表
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (lifecycleEvents.isEmpty()) {
                    Text(
                        text = "等待生命周期事件...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                } else {
                    lifecycleEvents.forEach { event ->
                        Text(
                            text = event,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
            
            // 说明文本
            Text(
                text = "DisposableEffect 用于需要清理的副作用，如注册和注销监听器",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

/**
 * SideEffect 示例
 * 
 * SideEffect 用于将 Compose 状态同步到非 Compose 代码
 */
@Composable
fun SideEffectExample() {
    EffectCard(title = "SideEffect") {
        val context = LocalContext.current
        var clickCount by remember { mutableStateOf(0) }
        
        // 使用 SideEffect 将 Compose 状态同步到非 Compose 代码
        // 每次重组时都会执行
        SideEffect {
            // 这里可以将 Compose 状态同步到非 Compose 代码
            // 例如，更新 Analytics 或其他外部系统
            println("当前点击次数: $clickCount")
        }
        
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "点击次数: $clickCount",
                fontWeight = FontWeight.Medium
            )
            
            Button(
                onClick = { 
                    clickCount++
                    // 显示 Toast 消息
                    Toast.makeText(context, "点击次数: $clickCount", Toast.LENGTH_SHORT).show()
                }
            ) {
                Text("点击我")
            }
            
            // 说明文本
            Text(
                text = "SideEffect 在每次重组时执行，用于将 Compose 状态同步到非 Compose 代码",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

/**
 * rememberCoroutineScope 示例
 * 
 * rememberCoroutineScope 用于获取协程作用域，用于在事件处理中启动协程
 */
@Composable
fun RememberCoroutineScopeExample() {
    EffectCard(title = "rememberCoroutineScope") {
        // 获取协程作用域
        val coroutineScope = rememberCoroutineScope()
        
        // 状态
        var isLoading by remember { mutableStateOf(false) }
        var result by remember { mutableStateOf("点击按钮开始模拟任务") }
        
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 显示结果
            Text(
                text = result,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
            
            // 加载指示器
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
            }
            
            // 按钮
            Button(
                onClick = {
                    // 使用协程作用域启动协程
                    coroutineScope.launch {
                        isLoading = true
                        result = "任务执行中..."
                        
                        // 模拟耗时任务
                        delay(2000)
                        
                        // 更新结果
                        result = "任务完成！"
                        isLoading = false
                    }
                },
                enabled = !isLoading
            ) {
                Text("执行任务")
            }
            
            // 说明文本
            Text(
                text = "rememberCoroutineScope 提供一个与组合生命周期绑定的协程作用域",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

/**
 * rememberUpdatedState 示例
 * 
 * rememberUpdatedState 用于捕获可能变化的值，确保长时间运行的效应始终使用最新值
 */
@Composable
fun RememberUpdatedStateExample() {
    EffectCard(title = "rememberUpdatedState") {
        // 模拟可能变化的回调
        var callbackMessage by remember { mutableStateOf("初始回调") }
        
        // 倒计时状态
        var countdown by remember { mutableStateOf(5) }
        var isCountdownRunning by remember { mutableStateOf(false) }
        
        // 使用 rememberUpdatedState 捕获最新的回调
        val latestCallback by rememberUpdatedState(newValue = callbackMessage)
        
        // 启动倒计时
        LaunchedEffect(key1 = isCountdownRunning) {
            if (isCountdownRunning) {
                // 重置倒计时
                countdown = 5
                
                // 倒计时循环
                while (countdown > 0) {
                    delay(1000)
                    countdown--
                }
                
                // 倒计时结束，执行回调
                // 这里使用 latestCallback 而不是直接使用 callbackMessage
                // 确保即使在倒计时过程中 callbackMessage 发生变化，也能使用最新值
                Toast.makeText(LocalContext.current, "执行回调: $latestCallback", Toast.LENGTH_SHORT).show()
                isCountdownRunning = false
            }
        }
        
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 显示当前回调
            Text(
                text = "当前回调: $callbackMessage",
                fontWeight = FontWeight.Medium
            )
            
            // 倒计时显示
            if (isCountdownRunning) {
                Text(
                    text = "倒计时: $countdown",
                    style = MaterialTheme.typography.titleLarge
                )
            }
            
            // 按钮行
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 更改回调按钮
                Button(
                    onClick = { 
                        callbackMessage = "回调已更新 (${System.currentTimeMillis() % 1000})"
                    }
                ) {
                    Text("更改回调")
                }
                
                // 启动倒计时按钮
                Button(
                    onClick = { isCountdownRunning = true },
                    enabled = !isCountdownRunning
                ) {
                    Text("启动倒计时")
                }
            }
            
            // 说明文本
            Text(
                text = "rememberUpdatedState 捕获可能变化的值，确保长时间运行的效应始终使用最新值",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

/**
 * derivedStateOf 示例
 * 
 * derivedStateOf 用于创建依赖于其他状态的计算状态
 */
@Composable
fun DerivedStateOfExample() {
    EffectCard(title = "derivedStateOf") {
        // 基础状态
        var sliderValue by remember { mutableStateOf(0f) }
        
        // 使用 derivedStateOf 创建依赖于 sliderValue 的计算状态
        // 只有当计算结果实际变化时才会触发重组
        val sliderLevel by remember {
            derivedStateOf {
                when {
                    sliderValue < 0.3f -> "低"
                    sliderValue < 0.7f -> "中"
                    else -> "高"
                }
            }
        }
        
        // 使用 derivedStateOf 计算颜色
        val sliderColor by remember {
            derivedStateOf {
                when {
                    sliderValue < 0.3f -> Color(0xFF81C784) // 绿色
                    sliderValue < 0.7f -> Color(0xFFFFB74D) // 橙色
                    else -> Color(0xFFE57373) // 红色
                }
            }
        }
        
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 显示滑块值和级别
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "数值: ${(sliderValue * 100).toInt()}",
                    style = MaterialTheme.typography.bodyMedium
                )
                
                // 级别指示器
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(sliderColor)
                        .border(1.dp, Color.Black.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = sliderLevel,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
            
            // 滑块
            Slider(
                value = sliderValue,
                onValueChange = { sliderValue = it },
                modifier = Modifier.fillMaxWidth()
            )
            
            // 说明文本
            Text(
                text = "derivedStateOf 创建依赖于其他状态的计算状态，只有当计算结果变化时才会触发重组",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

/**
 * produceState 示例
 * 
 * produceState 用于将非 Compose 状态转换为 Compose 状态
 */
@Composable
fun ProduceStateExample() {
    EffectCard(title = "produceState") {
        // 模拟外部数据源的触发器
        var refreshTrigger by remember { mutableStateOf(0) }
        
        // 使用 produceState 将非 Compose 状态转换为 Compose 状态
        // 当 refreshTrigger 变化时，会重新获取数据
        val dataState = produceState(initialValue = "加载中...", key1 = refreshTrigger) {
            // 模拟从外部数据源获取数据
            value = "加载中..." // 设置加载状态
            
            // 模拟网络请求延迟
            delay(1500)
            
            // 更新状态值
            value = "数据 #${refreshTrigger + 1} (${SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())})"
        }
        
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 显示数据状态
            Text(
                text = dataState.value,
                fontWeight = FontWeight.Medium
            )
            
            // 刷新按钮
            Button(
                onClick = { refreshTrigger++ }
            ) {
                Text("刷新数据")
            }
            
            // 说明文本
            Text(
                text = "produceState 将非 Compose 状态(如网络请求)转换为 Compose 状态",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

/**
 * snapshotFlow 示例
 * 
 * snapshotFlow 用于将 Compose 状态转换为 Flow
 */
@Composable
fun SnapshotFlowExample() {
    EffectCard(title = "snapshotFlow") {
        // 基础状态
        var counter by remember { mutableStateOf(0) }
        
        // 记录事件
        val events = remember { mutableStateListOf<String>() }
        
        // 使用 snapshotFlow 将 Compose 状态转换为 Flow
        LaunchedEffect(key1 = Unit) {
            // 创建从 counter 状态派生的 Flow
            snapshotFlow { counter }
                // 只关注偶数值
                .collect { value ->
                    // 添加事件记录
                    val timestamp = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                    val event = "$timestamp: 计数器值变为 $value"
                    events.add(0, event)
                    
                    // 保持最多显示5个事件
                    if (events.size > 5) {
                        events.removeAt(events.lastIndex)
                    }
                }
        }
        
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 显示计数器
            Text(
                text = "计数器: $counter",
                fontWeight = FontWeight.Medium
            )
            
            // 增加计数按钮
            Button(
                onClick = { counter++ }
            ) {
                Text("增加计数")
            }
            
            // 显示事件列表
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Flow 事件:",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                
                if (events.isEmpty()) {
                    Text(
                        text = "等待事件...",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    events.forEach { event ->
                        Text(
                            text = event,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
            
            // 说明文本
            Text(
                text = "snapshotFlow 将 Compose 状态转换为 Flow，可以在协程中收集和处理",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

/**
 * 效果卡片容器
 */
@Composable
fun EffectCard(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // 卡片标题
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Divider()
            Spacer(modifier = Modifier.height(12.dp))
            
            // 卡片内容
            content()
        }
    }
} 