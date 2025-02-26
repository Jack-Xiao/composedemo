package com.tianyejia.composedemo.week3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.*
import kotlin.random.Random

/**
 * 第3周：ViewModel 与 Compose 集成
 * 
 * 这个示例展示了 ViewModel 与 Compose 的集成方式：
 * 1. 使用 viewModel() 获取 ViewModel 实例
 * 2. 在 ViewModel 中使用 StateFlow 管理状态
 * 3. 在 Compose 中使用 collectAsState 收集 StateFlow
 * 4. ViewModel 工厂的使用
 * 5. 状态提升与单向数据流
 */
class ViewModelIntegrationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                CounterApp()
            }
        }
    }
}

/**
 * 计数器状态数据类
 */
data class CounterState(
    val count: Int = 0,
    val history: List<CounterEvent> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

/**
 * 计数器事件数据类
 */
data class CounterEvent(
    val id: String = UUID.randomUUID().toString(),
    val type: CounterEventType,
    val value: Int,
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * 计数器事件类型枚举
 */
enum class CounterEventType(val label: String, val color: Color) {
    INCREMENT("增加", Color(0xFF81C784)),
    DECREMENT("减少", Color(0xFFE57373)),
    RESET("重置", Color(0xFF64B5F6))
}

/**
 * 计数器 ViewModel
 */
class CounterViewModel : ViewModel() {
    // 使用 MutableStateFlow 管理状态
    private val _state = MutableStateFlow(CounterState())
    
    // 对外暴露不可变的 StateFlow
    val state: StateFlow<CounterState> = _state.asStateFlow()
    
    /**
     * 增加计数
     */
    fun increment() {
        _state.update { currentState ->
            val newCount = currentState.count + 1
            val event = CounterEvent(
                type = CounterEventType.INCREMENT,
                value = newCount
            )
            currentState.copy(
                count = newCount,
                history = currentState.history + event
            )
        }
    }
    
    /**
     * 减少计数
     */
    fun decrement() {
        _state.update { currentState ->
            val newCount = currentState.count - 1
            val event = CounterEvent(
                type = CounterEventType.DECREMENT,
                value = newCount
            )
            currentState.copy(
                count = newCount,
                history = currentState.history + event
            )
        }
    }
    
    /**
     * 重置计数
     */
    fun reset() {
        _state.update { currentState ->
            val event = CounterEvent(
                type = CounterEventType.RESET,
                value = 0
            )
            currentState.copy(
                count = 0,
                history = currentState.history + event
            )
        }
    }
    
    /**
     * 清除历史记录
     */
    fun clearHistory() {
        _state.update { it.copy(history = emptyList()) }
    }
    
    /**
     * 模拟随机计数
     */
    fun randomize() {
        _state.update { currentState ->
            // 先设置加载状态
            currentState.copy(isLoading = true, error = null)
        }
        
        // 模拟网络延迟
        Thread {
            try {
                Thread.sleep(1000)
                
                // 随机生成新计数
                val randomValue = Random.nextInt(-10, 11)
                
                _state.update { currentState ->
                    val newCount = randomValue
                    val event = CounterEvent(
                        type = if (newCount >= 0) CounterEventType.INCREMENT else CounterEventType.DECREMENT,
                        value = newCount
                    )
                    currentState.copy(
                        count = newCount,
                        history = currentState.history + event,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        error = "随机化失败: ${e.message}"
                    )
                }
            }
        }.start()
    }
}

/**
 * 自定义 ViewModel 工厂示例
 */
class CounterViewModelFactory(private val initialCount: Int) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CustomCounterViewModel::class.java)) {
            return CustomCounterViewModel(initialCount) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

/**
 * 带初始值的自定义计数器 ViewModel
 */
class CustomCounterViewModel(initialCount: Int) : ViewModel() {
    // 使用 MutableStateFlow 管理状态
    private val _count = MutableStateFlow(initialCount)
    val count = _count.asStateFlow()
    
    fun increment() {
        _count.value++
    }
    
    fun decrement() {
        _count.value--
    }
}

/**
 * 主应用界面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CounterApp() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ViewModel 与 Compose 集成") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // 标准 ViewModel 示例
            StandardViewModelExample()
            
            Divider()
            
            // 自定义 ViewModel 工厂示例
            CustomViewModelFactoryExample()
        }
    }
}

/**
 * 标准 ViewModel 示例
 */
@Composable
fun StandardViewModelExample() {
    // 使用 viewModel() 获取 ViewModel 实例
    val viewModel: CounterViewModel = viewModel()
    
    // 使用 collectAsState 收集 StateFlow
    val state by viewModel.state.collectAsState()
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "标准 ViewModel 示例",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        
        // 计数器显示
        CounterDisplay(
            count = state.count,
            isLoading = state.isLoading,
            error = state.error
        )
        
        // 计数器控制按钮
        CounterControls(
            onIncrement = { viewModel.increment() },
            onDecrement = { viewModel.decrement() },
            onReset = { viewModel.reset() },
            onRandomize = { viewModel.randomize() }
        )
        
        // 历史记录
        if (state.history.isNotEmpty()) {
            Text(
                text = "历史记录",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(top = 8.dp)
            )
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                EventHistoryList(
                    events = state.history,
                    onClearHistory = { viewModel.clearHistory() }
                )
            }
        }
    }
}

/**
 * 自定义 ViewModel 工厂示例
 */
@Composable
fun CustomViewModelFactoryExample() {
    // 使用自定义工厂创建 ViewModel
    val viewModel: CustomCounterViewModel = viewModel(
        factory = CounterViewModelFactory(initialCount = 10)
    )
    
    // 收集状态
    val count by viewModel.count.collectAsState()
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "自定义 ViewModel 工厂示例",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        
        // 简单计数器显示
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = count.toString(),
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { viewModel.decrement() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("-")
                    }
                    
                    Button(
                        onClick = { viewModel.increment() }
                    ) {
                        Text("+")
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "使用工厂创建的 ViewModel，初始值为 10",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/**
 * 计数器显示组件
 */
@Composable
fun CounterDisplay(
    count: Int,
    isLoading: Boolean,
    error: String?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator()
                }
                error != null -> {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                }
                else -> {
                    Text(
                        text = count.toString(),
                        style = MaterialTheme.typography.displayLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

/**
 * 计数器控制按钮组件
 */
@Composable
fun CounterControls(
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onReset: () -> Unit,
    onRandomize: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // 减少按钮
        Button(
            onClick = onDecrement,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text("-")
        }
        
        // 重置按钮
        Button(
            onClick = onReset,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Text("重置")
        }
        
        // 随机按钮
        Button(
            onClick = onRandomize,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary
            )
        ) {
            Icon(Icons.Default.Refresh, contentDescription = "随机")
        }
        
        // 增加按钮
        Button(
            onClick = onIncrement,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text("+")
        }
    }
}

/**
 * 事件历史列表组件
 */
@Composable
fun EventHistoryList(
    events: List<CounterEvent>,
    onClearHistory: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // 清除历史按钮
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(
                onClick = onClearHistory,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "清除历史",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("清除历史")
            }
        }
        
        // 事件列表
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(events.reversed()) { event ->
                EventItem(event = event)
            }
        }
    }
}

/**
 * 单个事件项组件
 */
@Composable
fun EventItem(event: CounterEvent) {
    val eventTime = remember(event.timestamp) {
        val date = Date(event.timestamp)
        val hours = date.hours.toString().padStart(2, '0')
        val minutes = date.minutes.toString().padStart(2, '0')
        val seconds = date.seconds.toString().padStart(2, '0')
        "$hours:$minutes:$seconds"
    }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(event.type.color.copy(alpha = 0.1f))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 事件类型指示器
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(event.type.color)
        )
        
        Spacer(modifier = Modifier.width(8.dp))
        
        // 事件信息
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "${event.type.label} 到 ${event.value}",
                fontWeight = FontWeight.Medium
            )
            Text(
                text = eventTime,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
} 