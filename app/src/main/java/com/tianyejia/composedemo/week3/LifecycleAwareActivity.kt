package com.tianyejia.composedemo.week3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tianyejia.composedemo.ui.theme.ComposeDemoTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class LifecycleAwareActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeDemoTheme {
                LifecycleAwareScreen(onBackClick = { finish() })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LifecycleAwareScreen(onBackClick: () -> Unit) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("生命周期感知组件") },
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
        LifecycleAwareContent(modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun LifecycleAwareContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "生命周期感知组件示例",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        
        // 1. 副作用示例：LaunchedEffect
        SectionTitleLifecycle("1. LaunchedEffect - 键控副作用")
        LaunchedEffectExample()
        
        // 2. 副作用示例：SideEffect
        SectionTitleLifecycle("2. SideEffect - 每次重组执行")
        SideEffectExample()
        
        // 3. 副作用示例：DisposableEffect
        SectionTitleLifecycle("3. DisposableEffect - 需要清理的副作用")
        DisposableEffectExample()
        
        // 4. 副作用示例：rememberCoroutineScope
        SectionTitleLifecycle("4. rememberCoroutineScope - 组合外启动协程")
        RememberCoroutineScopeExample()
        
        // 5. 副作用示例：produceState
        SectionTitleLifecycle("5. produceState - 将非Compose状态转为Compose状态")
        ProduceStateExample()
        
        // 6. 副作用示例：derivedStateOf
        SectionTitleLifecycle("6. derivedStateOf - 派生状态")
        DerivedStateOfExample()
    }
}

@Composable
private fun SectionTitleLifecycle(title: String) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.secondary,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

// 1. LaunchedEffect示例
@Composable
fun LaunchedEffectExample() {
    var count by remember { mutableStateOf(0) }
    var isRunning by remember { mutableStateOf(false) }
    
    // 当isRunning为true时，启动一个协程每秒递增计数器
    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (true) {
                delay(1000)
                count++
            }
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "计数器: $count",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            
            Button(
                onClick = { isRunning = !isRunning }
            ) {
                Text(if (isRunning) "停止" else "开始")
            }
            
            Text(
                text = "LaunchedEffect会在键值(isRunning)变化时取消旧协程并启动新协程",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.outline,
                textAlign = TextAlign.Center
            )
        }
    }
}

// 2. SideEffect示例
@Composable
fun SideEffectExample() {
    var recomposeCounter by remember { mutableStateOf(0) }
    var externalState by remember { mutableStateOf(0) }
    
    // SideEffect在每次成功重组后执行
    SideEffect {
        // 这通常用于与非Compose系统同步状态
        externalState = recomposeCounter
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "重组计数: $recomposeCounter",
                fontSize = 18.sp
            )
            
            Text(
                text = "外部状态: $externalState",
                fontSize = 18.sp
            )
            
            Button(
                onClick = { recomposeCounter++ }
            ) {
                Text("触发重组")
            }
            
            Text(
                text = "SideEffect在每次重组后执行，用于同步Compose状态到非Compose系统",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.outline,
                textAlign = TextAlign.Center
            )
        }
    }
}

// 3. DisposableEffect示例
@Composable
fun DisposableEffectExample() {
    var isActive by remember { mutableStateOf(false) }
    var logMessages by remember { mutableStateOf(listOf<String>()) }
    val currentTime = remember { SimpleDateFormat("HH:mm:ss", Locale.getDefault()) }
    
    // DisposableEffect在进入组合时执行并在离开组合时清理
    DisposableEffect(isActive) {
        val timestamp = currentTime.format(Date())
        logMessages = logMessages + "[$timestamp] 资源已初始化，状态: ${if (isActive) "活跃" else "非活跃"}"
        
        // 返回一个清理函数
        onDispose {
            val closeTimestamp = currentTime.format(Date())
            logMessages = logMessages + "[$closeTimestamp] 资源已清理"
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("资源状态: ${if (isActive) "活跃" else "非活跃"}")
                
                Button(
                    onClick = { isActive = !isActive }
                ) {
                    Text(if (isActive) "停用资源" else "激活资源")
                }
            }
            
            Divider()
            
            Text(
                text = "日志:",
                fontWeight = FontWeight.Bold
            )
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                for (message in logMessages.takeLast(5)) {
                    Text(
                        text = message,
                        fontSize = 14.sp
                    )
                }
                
                if (logMessages.isEmpty()) {
                    Text(
                        text = "暂无日志",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
            
            if (logMessages.size > 5) {
                Text(
                    text = "仅显示最新5条记录 (总计: ${logMessages.size})",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.outline
                )
            }
            
            Text(
                text = "DisposableEffect用于管理需要清理的资源，例如事件监听器或传感器注册",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.outline,
                textAlign = TextAlign.Center
            )
            
            Button(
                onClick = { logMessages = emptyList() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text("清除日志")
            }
        }
    }
}

// 4. rememberCoroutineScope示例
@Composable
fun RememberCoroutineScopeExample() {
    val scope = rememberCoroutineScope()
    var message by remember { mutableStateOf("尚未执行任务") }
    var isTaskRunning by remember { mutableStateOf(false) }
    
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = message,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
            
            Button(
                onClick = {
                    // 在组合外启动协程
                    isTaskRunning = true
                    scope.launch {
                        message = "任务正在执行..."
                        delay(2000) // 模拟长时间运行的任务
                        message = "任务已完成！"
                        isTaskRunning = false
                    }
                },
                enabled = !isTaskRunning
            ) {
                Text(if (isTaskRunning) "任务执行中..." else "执行任务")
            }
            
            if (isTaskRunning) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Text(
                text = "rememberCoroutineScope提供一个与组合生命周期绑定的协程作用域，\n可以在事件处理程序中启动协程",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.outline,
                textAlign = TextAlign.Center
            )
        }
    }
}

// 5. produceState示例 - 将非Compose状态转为Compose状态
@Composable
fun ProduceStateExample() {
    // 模拟一个数据源，通常这可能是从网络或其他非Compose源获取的
    fun getDataFlow(): Flow<String> = flow {
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        var counter = 0
        while (true) {
            val time = timeFormat.format(Date())
            emit("数据 #${counter++} - $time")
            delay(3000) // 每3秒发射一次数据
        }
    }
    
    // 使用produceState将Flow转换为Compose状态
    val latestData by produceState(initialValue = "正在获取数据...") {
        getDataFlow().collect { newData ->
            value = newData
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "实时数据流",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = latestData,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth()
            )
            
            Text(
                text = "produceState将非Compose状态源(如Flow)转换为Compose可观察的State",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.outline,
                textAlign = TextAlign.Center
            )
        }
    }
}

// 6. derivedStateOf示例 - 派生状态
@Composable
fun DerivedStateOfExample() {
    var sliderValue by remember { mutableStateOf(0f) }
    
    // 预先获取颜色
    val errorColor = MaterialTheme.colorScheme.error
    val primaryColor = MaterialTheme.colorScheme.primary
    val tertiaryColor = MaterialTheme.colorScheme.tertiary
    
    // 使用derivedStateOf计算派生状态
    val progress by remember {
        derivedStateOf { (sliderValue * 100).toInt() }
    }
    
    // 基于进度值派生的状态分类
    val progressCategory by remember {
        derivedStateOf {
            when {
                progress < 30 -> "低"
                progress < 70 -> "中"
                else -> "高"
            }
        }
    }
    
    // 基于类别派生的颜色
    val progressColor by remember {
        derivedStateOf {
            when (progressCategory) {
                "低" -> errorColor
                "中" -> primaryColor
                else -> tertiaryColor
            }
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
                text = "派生状态示例",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            
            Slider(
                value = sliderValue,
                onValueChange = { sliderValue = it },
                modifier = Modifier.fillMaxWidth()
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "进度: $progress%",
                    fontSize = 16.sp
                )
                
                Text(
                    text = "类别: $progressCategory",
                    fontSize = 16.sp,
                    color = progressColor,
                    fontWeight = FontWeight.Bold
                )
            }
            
            LinearProgressIndicator(
                progress = { sliderValue },
                modifier = Modifier.fillMaxWidth(),
                color = progressColor
            )
            
            Text(
                text = "derivedStateOf用于创建依赖于其他状态的计算值，\n仅当计算结果实际改变时才触发重组",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.outline,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LifecycleAwareScreenPreview() {
    ComposeDemoTheme {
        LifecycleAwareScreen(onBackClick = {})
    }
} 