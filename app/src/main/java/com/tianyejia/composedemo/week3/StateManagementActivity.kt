package com.tianyejia.composedemo.week3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tianyejia.composedemo.ui.theme.ComposeDemoTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StateManagementActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeDemoTheme {
                StateManagementScreen(onBackClick = { finish() })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StateManagementScreen(onBackClick: () -> Unit) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("状态管理和生命周期") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        StateManagementContent(modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun StateManagementContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "第三周：状态管理和生命周期",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        
        // 1. 基础状态管理
        SectionTitleState("1. 基础状态管理")
        SimpleCounterExample()
        
        // 2. 副作用处理
        SectionTitleState("2. 副作用处理")
        SideEffectsExample()
        
        // 3. ViewModel与Compose结合
        SectionTitleState("3. ViewModel与Compose结合")
        val todoViewModel: TodoViewModel = viewModel()
        TodoManagerExample(todoViewModel)
        
        // 4. Compose与传统View集成
        SectionTitleState("4. Compose与传统View集成")
        ComposeWithViewIntegration()
    }
}

@Composable
private fun SectionTitleState(title: String) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.secondary,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

// 1. 简单计数器示例 - 基础状态管理
@Composable
fun SimpleCounterExample() {
    var count by remember { mutableStateOf(0) }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "计数器：$count",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { count-- },
                    enabled = count > 0
                ) {
                    Text("-1")
                }
                
                Button(
                    onClick = { count++ }
                ) {
                    Text("+1")
                }
                
                Button(
                    onClick = { count = 0 },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text("重置")
                }
            }
            
            // 说明
            Text(
                text = "使用 remember 和 mutableStateOf 管理本地UI状态",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

// 2. 副作用处理示例
@Composable
fun SideEffectsExample() {
    var trigger by remember { mutableStateOf(false) }
    var elapsedTime by remember { mutableStateOf(0) }
    var isTimerRunning by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    
    // LaunchedEffect示例 - 当trigger改变时执行一次性操作
    LaunchedEffect(trigger) {
        if (trigger) {
            // 模拟一个一次性操作
            delay(2000)
            trigger = false
        }
    }
    
    // DisposableEffect示例 - 管理需要清理的资源
    DisposableEffect(isTimerRunning) {
        var job: kotlinx.coroutines.Job? = null
        
        if (isTimerRunning) {
            job = scope.launch {
                while (isTimerRunning) {
                    delay(1000)
                    elapsedTime++
                }
            }
        }
        
        // 返回一个清理函数
        onDispose {
            job?.cancel()
        }
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "副作用示例",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            
            // LaunchedEffect示例
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("LaunchedEffect示例:")
                Button(
                    onClick = { trigger = true },
                    enabled = !trigger
                ) {
                    Text(if (trigger) "处理中..." else "触发效果")
                }
            }
            
            // DisposableEffect示例
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("定时器: ${elapsedTime}秒")
                Button(
                    onClick = { isTimerRunning = !isTimerRunning }
                ) {
                    Text(if (isTimerRunning) "停止" else "开始")
                }
            }
            
            if (isTimerRunning) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    progress = (elapsedTime % 60) / 60f
                )
            }
            
            Text(
                text = "LaunchedEffect：执行一次性副作用\nDisposableEffect：管理需要清理的资源",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

// 3. ViewModel与Compose结合 - 任务管理示例
// 任务数据类
data class TodoItem(
    val id: Int,
    val text: String,
    var isCompleted: Boolean = false
)

// ViewModel
class TodoViewModel : ViewModel() {
    // 任务列表状态
    private val _todoItems = mutableStateOf<List<TodoItem>>(emptyList())
    val todoItems: State<List<TodoItem>> = _todoItems
    
    // 添加任务
    fun addTodoItem(text: String) {
        if (text.isNotBlank()) {
            val newId = (_todoItems.value.maxOfOrNull { it.id } ?: 0) + 1
            _todoItems.value = _todoItems.value + TodoItem(id = newId, text = text)
        }
    }
    
    // 切换任务完成状态
    fun toggleTodoItem(id: Int) {
        _todoItems.value = _todoItems.value.map { 
            if (it.id == id) it.copy(isCompleted = !it.isCompleted) else it 
        }
    }
    
    // 删除任务
    fun removeTodoItem(id: Int) {
        _todoItems.value = _todoItems.value.filter { it.id != id }
    }
}

@Composable
fun TodoManagerExample(viewModel: TodoViewModel) {
    var newTodoText by rememberSaveable { mutableStateOf("") }
    val todoItems by viewModel.todoItems
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "任务管理器",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            
            // 添加任务输入框
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = newTodoText,
                    onValueChange = { newTodoText = it },
                    placeholder = { Text("添加新任务...") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                
                IconButton(
                    onClick = {
                        viewModel.addTodoItem(newTodoText)
                        newTodoText = ""
                    },
                    enabled = newTodoText.isNotBlank()
                ) {
                    Icon(Icons.Default.Add, contentDescription = "添加任务")
                }
            }
            
            // 任务列表
            if (todoItems.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "暂无任务，请添加新任务",
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(todoItems, key = { it.id }) { item ->
                        TodoItemRow(
                            item = item,
                            onToggleComplete = { viewModel.toggleTodoItem(item.id) },
                            onDelete = { viewModel.removeTodoItem(item.id) }
                        )
                    }
                }
            }
            
            // 说明
            Text(
                text = "使用ViewModel管理UI状态，实现数据与UI的分离",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
fun TodoItemRow(
    item: TodoItem,
    onToggleComplete: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (item.isCompleted) 
                MaterialTheme.colorScheme.surfaceVariant 
            else 
                MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(onClick = onToggleComplete) {
                Icon(
                    imageVector = if (item.isCompleted) Icons.Default.Check else Icons.Default.Check,
                    contentDescription = "完成状态",
                    tint = if (item.isCompleted) 
                        MaterialTheme.colorScheme.primary 
                    else 
                        MaterialTheme.colorScheme.outline
                )
            }
            
            Text(
                text = item.text,
                modifier = Modifier.weight(1f),
                textDecoration = if (item.isCompleted) TextDecoration.LineThrough else null,
                color = if (item.isCompleted) MaterialTheme.colorScheme.outline else Color.Unspecified
            )
            
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "删除任务",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

// 4. Compose与传统View集成示例
@Composable
fun ComposeWithViewIntegration() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Compose与传统View集成",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            
            Text(
                text = "在实际项目中，我们可能需要将Compose UI与传统的View系统集成。\n\n主要集成方式有两种：\n1. 在传统View中嵌入Compose UI (ComposeView)\n2. 在Compose中嵌入传统View (AndroidView)",
                fontSize = 16.sp
            )
            
            Divider()
            
            Text(
                text = "示例代码说明:\n\n" +
                "// 在XML布局中使用ComposeView\n" +
                "<androidx.compose.ui.platform.ComposeView\n" +
                "    android:id=\"@+id/compose_view\"\n" +
                "    android:layout_width=\"match_parent\"\n" +
                "    android:layout_height=\"wrap_content\" />\n\n" +
                "// 在Activity中设置内容\n" +
                "findViewById<ComposeView>(R.id.compose_view).setContent {\n" +
                "    Text(\"这是嵌入在传统View中的Compose UI\")\n" +
                "}\n\n" +
                "// 在Compose中嵌入传统View\n" +
                "AndroidView(\n" +
                "    factory = { context ->\n" +
                "        TextView(context).apply {\n" +
                "            text = \"这是嵌入在Compose中的传统View\"\n" +
                "        }\n" +
                "    },\n" +
                "    update = { view ->\n" +
                "        // 更新View的属性\n" +
                "    }\n" +
                ")",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StateManagementPreview() {
    ComposeDemoTheme {
        StateManagementScreen(onBackClick = {})
    }
} 