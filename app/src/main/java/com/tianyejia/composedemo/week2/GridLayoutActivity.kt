package com.tianyejia.composedemo.week2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tianyejia.composedemo.ui.theme.ComposeDemoTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 第二周：网格布局和自定义组件示例
 * 本示例展示了：
 * 1. LazyVerticalGrid的使用
 * 2. 自定义选择器组件
 * 3. 动画效果
 * 4. 协程与状态管理的结合
 */
class GridLayoutActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeDemoTheme {
                GridLayoutScreen(onBackClick = { finish() })
            }
        }
    }
}

/**
 * 颜色选择器数据模型
 */
data class ColorOption(
    val id: Int,
    val name: String,
    val color: Color
)

/**
 * 示例颜色数据
 */
val colorOptions = listOf(
    ColorOption(1, "红色", Color(0xFFE57373)),
    ColorOption(2, "粉色", Color(0xFFF06292)),
    ColorOption(3, "紫色", Color(0xFFBA68C8)),
    ColorOption(4, "深紫", Color(0xFF9575CD)),
    ColorOption(5, "靛蓝", Color(0xFF7986CB)),
    ColorOption(6, "蓝色", Color(0xFF64B5F6)),
    ColorOption(7, "浅蓝", Color(0xFF4FC3F7)),
    ColorOption(8, "青色", Color(0xFF4DD0E1)),
    ColorOption(9, "蓝绿", Color(0xFF4DB6AC)),
    ColorOption(10, "绿色", Color(0xFF81C784)),
    ColorOption(11, "浅绿", Color(0xFFAED581)),
    ColorOption(12, "黄绿", Color(0xFFDCE775)),
    ColorOption(13, "黄色", Color(0xFFFFD54F)),
    ColorOption(14, "琥珀", Color(0xFFFFB74D)),
    ColorOption(15, "橙色", Color(0xFFFF8A65)),
    ColorOption(16, "深橙", Color(0xFFFF7043)),
    ColorOption(17, "棕色", Color(0xFF8D6E63)),
    ColorOption(18, "灰色", Color(0xFF90A4AE))
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun GridLayoutScreen(onBackClick: () -> Unit) {
    // 创建一个Scaffold脚手架
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    
    // 选中的颜色
    var selectedColorId by remember { mutableStateOf<Int?>(null) }
    
    // 加载状态
    var isLoading by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("网格布局与自定义组件") },
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
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 标题和说明
            Text(
                text = "颜色选择器",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Text(
                text = "请从下面的网格中选择一个颜色",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            // 选中的颜色预览
            selectedColorId?.let { id ->
                val selectedColor = colorOptions.find { it.id == id }
                selectedColor?.let {
                    ColorPreview(color = it)
                }
            }
            
            // 网格布局
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 100.dp),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = colorOptions,
                    // 使用animateItemPlacement可以在项目位置变化时添加动画
                    key = { it.id }
                ) { colorOption ->
                    val isSelected = selectedColorId == colorOption.id
                    
                    ColorItem(
                        colorOption = colorOption,
                        isSelected = isSelected,
                        onClick = {
                            selectedColorId = colorOption.id
                            
                            // 显示Snackbar
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "已选择${colorOption.name}",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    )
                }
            }
            
            // 确认按钮
            Button(
                onClick = {
                    // 模拟加载过程
                    coroutineScope.launch {
                        isLoading = true
                        delay(1500) // 模拟网络请求
                        isLoading = false
                        
                        // 显示确认消息
                        snackbarHostState.showSnackbar(
                            message = if (selectedColorId != null) 
                                "颜色设置成功！" 
                            else 
                                "请先选择一个颜色",
                            duration = SnackbarDuration.Short
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    // 显示加载指示器
                    CircularProgressIndicator(
                        modifier = Modifier.size(50.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 20.dp
                    )
                } else {
                    Text("确认选择")
                }
            }
        }
    }
}

/**
 * 颜色预览组件
 */
@Composable
fun ColorPreview(color: ColorOption) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color.color),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = color.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            
            Text(
                text = "已选择",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * 颜色项组件
 * 展示单个颜色选项
 */
@Composable
fun ColorItem(
    colorOption: ColorOption,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    // 使用动画效果
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1.0f,
        label = "缩放动画"
    )
    
    Box(
        modifier = Modifier
            .scale(scale)
            .size(100.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(colorOption.color)
            .clickable(onClick = onClick)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 如果选中，显示选中指示器
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "已选择",
                        tint = Color.White
                    )
                }
                
                Spacer(modifier = Modifier.height(4.dp))
            }
            
            Text(
                text = colorOption.name,
                color = Color.White,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * 自定义组合组件：数量选择器
 * 展示如何创建可复用的自定义组件
 */
@Composable
fun QuantitySelector(
    quantity: Int,
    onQuantityChanged: (Int) -> Unit,
    modifier: Modifier = Modifier,
    minQuantity: Int = 1,
    maxQuantity: Int = 10
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 减少按钮
        IconButton(
            onClick = { 
                if (quantity > minQuantity) {
                    onQuantityChanged(quantity - 1)
                }
            },
            enabled = quantity > minQuantity
        ) {
            Text(
                text = "−",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = if (quantity > minQuantity) 
                    MaterialTheme.colorScheme.primary 
                else 
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
        }
        
        // 数量显示
        Text(
            text = quantity.toString(),
            modifier = Modifier
                .width(36.dp)
                .padding(horizontal = 4.dp),
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        // 增加按钮
        IconButton(
            onClick = { 
                if (quantity < maxQuantity) {
                    onQuantityChanged(quantity + 1)
                }
            },
            enabled = quantity < maxQuantity
        ) {
            Text(
                text = "+",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = if (quantity < maxQuantity) 
                    MaterialTheme.colorScheme.primary 
                else 
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GridLayoutScreenPreview() {
    ComposeDemoTheme {
        GridLayoutScreen(onBackClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun QuantitySelectorPreview() {
    ComposeDemoTheme {
        var quantity by remember { mutableStateOf(1) }
        QuantitySelector(
            quantity = quantity,
            onQuantityChanged = { quantity = it },
            modifier = Modifier.padding(16.dp)
        )
    }
} 