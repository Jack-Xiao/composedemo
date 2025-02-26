package com.tianyejia.composedemo.week3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

/**
 * 第3周：状态管理和生命周期
 * 
 * 这个示例展示了Jetpack Compose中的状态管理和生命周期相关概念：
 * 1. 本地状态管理 (remember, mutableStateOf)
 * 2. 状态提升 (State Hoisting)
 * 3. 副作用处理 (LaunchedEffect, DisposableEffect)
 * 4. ViewModel集成
 * 5. 可组合函数的生命周期
 */
class StateManagementActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                TaskManagerApp()
            }
        }
    }
}

/**
 * 任务数据类
 */
data class Task(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val isCompleted: Boolean = false,
    val createdAt: Date = Date()
)

/**
 * 任务优先级枚举
 */
enum class TaskPriority(val color: Color, val label: String) {
    HIGH(Color(0xFFE57373), "高"),
    MEDIUM(Color(0xFFFFB74D), "中"),
    LOW(Color(0xFF81C784), "低")
}

/**
 * 任务ViewModel - 管理任务列表状态
 */
class TaskViewModel : ViewModel() {
    // 任务列表状态
    private val _tasks = mutableStateOf<List<Task>>(emptyList())
    val tasks: State<List<Task>> = _tasks
    
    // 加载状态
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading
    
    // 错误状态
    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error
    
    // 初始化时加载任务
    init {
        loadTasks()
    }
    
    /**
     * 模拟从数据源加载任务
     */
    fun loadTasks() {
        _isLoading.value = true
        _error.value = null
        
        // 模拟网络延迟
        // 在实际应用中，这里会是一个协程调用数据库或网络API
        Thread {
            try {
                Thread.sleep(1500)
                val demoTasks = listOf(
                    Task(title = "完成Compose学习", priority = TaskPriority.HIGH),
                    Task(title = "准备技术分享", priority = TaskPriority.MEDIUM),
                    Task(title = "阅读技术文章", priority = TaskPriority.LOW),
                    Task(title = "健身一小时", priority = TaskPriority.MEDIUM)
                )
                _tasks.value = demoTasks
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = "加载任务失败: ${e.message}"
                _isLoading.value = false
            }
        }.start()
    }
    
    /**
     * 添加新任务
     */
    fun addTask(task: Task) {
        _tasks.value = _tasks.value + task
    }
    
    /**
     * 删除任务
     */
    fun deleteTask(taskId: String) {
        _tasks.value = _tasks.value.filter { it.id != taskId }
    }
    
    /**
     * 切换任务完成状态
     */
    fun toggleTaskCompletion(taskId: String) {
        _tasks.value = _tasks.value.map { 
            if (it.id == taskId) it.copy(isCompleted = !it.isCompleted) else it 
        }
    }
}

/**
 * 主应用界面
 */
@Composable
fun TaskManagerApp(viewModel: TaskViewModel = viewModel()) {
    // 获取ViewModel中的状态
    val tasks by viewModel.tasks
    val isLoading by viewModel.isLoading
    val error by viewModel.error
    
    // 新任务对话框状态
    var showAddTaskDialog by remember { mutableStateOf(false) }
    
    // 当前时间状态 - 演示DisposableEffect
    var currentTime by remember { mutableStateOf(System.currentTimeMillis()) }
    
    // 使用LaunchedEffect更新时间 - 演示副作用处理
    LaunchedEffect(key1 = "timer") {
        while (true) {
            delay(1000)
            currentTime = System.currentTimeMillis()
        }
    }
    
    // 使用remember记住协程作用域 - 用于启动协程
    val coroutineScope = rememberCoroutineScope()
    
    // 刷新按钮旋转动画状态
    val refreshRotation = remember { mutableStateOf(0f) }
    val animatedRotation by animateFloatAsState(targetValue = refreshRotation.value)
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("任务管理器") },
                actions = {
                    // 刷新按钮
                    IconButton(
                        onClick = { 
                            // 启动协程执行刷新动画
                            coroutineScope.launch {
                                refreshRotation.value += 360f
                                viewModel.loadTasks()
                            }
                        }
                    ) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = "刷新",
                            modifier = Modifier.rotate(animatedRotation)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddTaskDialog = true }
            ) {
                Icon(Icons.Default.Add, contentDescription = "添加任务")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 显示当前时间 - 演示状态更新
            Text(
                text = "当前时间: ${SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(currentTime))}",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            // 主内容区域
            when {
                // 加载状态
                isLoading -> {
                    LoadingIndicator(modifier = Modifier.align(Alignment.Center))
                }
                
                // 错误状态
                error != null -> {
                    ErrorMessage(
                        message = error ?: "未知错误",
                        onRetry = { viewModel.loadTasks() },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                
                // 空列表状态
                tasks.isEmpty() -> {
                    EmptyListMessage(
                        modifier = Modifier.align(Alignment.Center),
                        onAddClick = { showAddTaskDialog = true }
                    )
                }
                
                // 显示任务列表
                else -> {
                    TaskList(
                        tasks = tasks,
                        onTaskClick = { viewModel.toggleTaskCompletion(it.id) },
                        onDeleteTask = { viewModel.deleteTask(it.id) }
                    )
                }
            }
            
            // 添加任务对话框
            if (showAddTaskDialog) {
                AddTaskDialog(
                    onDismiss = { showAddTaskDialog = false },
                    onTaskAdded = { task ->
                        viewModel.addTask(task)
                        showAddTaskDialog = false
                    }
                )
            }
        }
    }
    
    // DisposableEffect示例 - 在组件离开组合时执行清理操作
    DisposableEffect(key1 = "logger") {
        val startTime = System.currentTimeMillis()
        println("TaskManagerApp 进入组合")
        
        // 返回一个清理函数，在组件离开组合时调用
        onDispose {
            val duration = System.currentTimeMillis() - startTime
            println("TaskManagerApp 离开组合，持续时间: ${duration}ms")
        }
    }
}

/**
 * 任务列表组件
 */
@Composable
fun TaskList(
    tasks: List<Task>,
    onTaskClick: (Task) -> Unit,
    onDeleteTask: (Task) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(tasks, key = { it.id }) { task ->
            // 使用key参数确保在列表更新时保持正确的状态
            TaskItem(
                task = task,
                onClick = { onTaskClick(task) },
                onDelete = { onDeleteTask(task) }
            )
        }
    }
}

/**
 * 单个任务项组件
 */
@Composable
fun TaskItem(
    task: Task,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    // 使用remember记住展开状态
    var expanded by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 任务标题和优先级标签
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // 优先级指示器
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(task.priority.color)
                    )
                    
                    // 任务标题
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        // 如果任务已完成，添加删除线效果
                        color = if (task.isCompleted) 
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        else 
                            MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.alpha(if (task.isCompleted) 0.5f else 1f)
                    )
                }
                
                // 任务操作按钮
                Row {
                    // 完成/未完成复选框
                    Checkbox(
                        checked = task.isCompleted,
                        onCheckedChange = { onClick() }
                    )
                    
                    // 删除按钮
                    IconButton(onClick = onDelete) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "删除任务",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
            
            // 展开区域 - 显示任务详情
            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Divider()
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // 优先级
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "优先级:",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = task.priority.label,
                            style = MaterialTheme.typography.bodyMedium,
                            color = task.priority.color,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    // 创建时间
                    Text(
                        text = "创建时间: ${SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(task.createdAt)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

/**
 * 添加任务对话框
 */
@Composable
fun AddTaskDialog(
    onDismiss: () -> Unit,
    onTaskAdded: (Task) -> Unit
) {
    // 使用remember记住对话框内的状态
    var taskTitle by remember { mutableStateOf("") }
    var selectedPriority by remember { mutableStateOf(TaskPriority.MEDIUM) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("添加新任务") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 任务标题输入框
                OutlinedTextField(
                    value = taskTitle,
                    onValueChange = { taskTitle = it },
                    label = { Text("任务标题") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                // 优先级选择器
                Text(
                    text = "优先级:",
                    style = MaterialTheme.typography.bodyMedium
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TaskPriority.values().forEach { priority ->
                        PriorityChip(
                            priority = priority,
                            isSelected = selectedPriority == priority,
                            onClick = { selectedPriority = priority }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (taskTitle.isNotBlank()) {
                        onTaskAdded(
                            Task(
                                title = taskTitle,
                                priority = selectedPriority
                            )
                        )
                    }
                },
                enabled = taskTitle.isNotBlank()
            ) {
                Text("添加")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}

/**
 * 优先级选择芯片
 */
@Composable
fun PriorityChip(
    priority: TaskPriority,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                color = if (isSelected) priority.color else Color.Gray.copy(alpha = 0.3f),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick),
        color = if (isSelected) priority.color.copy(alpha = 0.2f) else Color.Transparent
    ) {
        Text(
            text = priority.label,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            color = if (isSelected) priority.color else Color.Gray,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

/**
 * 加载指示器
 */
@Composable
fun LoadingIndicator(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "加载中...",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * 错误消息
 */
@Composable
fun ErrorMessage(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "出错了!",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("重试")
        }
    }
}

/**
 * 空列表消息
 */
@Composable
fun EmptyListMessage(
    modifier: Modifier = Modifier,
    onAddClick: () -> Unit
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "暂无任务",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "点击下方按钮添加新任务",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onAddClick) {
            Text("添加任务")
        }
    }
}

/**
 * 预览函数
 */
@Preview(showBackground = true)
@Composable
fun TaskManagerPreview() {
    MaterialTheme {
        TaskManagerApp()
    }
} 