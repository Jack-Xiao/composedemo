package com.tianyejia.composedemo.week2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tianyejia.composedemo.R
import com.tianyejia.composedemo.ui.theme.ComposeDemoTheme
import kotlinx.coroutines.launch

/**
 * 第二周：高级布局和组件示例
 * 本示例展示了更复杂的Compose布局和组件，包括：
 * 1. LazyColumn和LazyRow的使用
 * 2. 卡片布局和自定义项目
 * 3. 简单的动画效果
 * 4. SnackBar和协程的使用
 * 5. 状态提升和共享状态
 */
class AdvancedLayoutActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeDemoTheme {
                AdvancedLayoutScreen(onBackClick = { finish() })
            }
        }
    }
}

/**
 * 数据模型：代表一个食品项目
 * @param id 唯一标识符
 * @param name 食品名称
 * @param description 食品描述
 * @param imageRes 图片资源ID
 * @param price 价格
 * @param rating 评分（1-5）
 */
data class FoodItem(
    val id: Int,
    val name: String,
    val description: String,
    val imageRes: Int,
    val price: Double,
    val rating: Float
)

/**
 * 示例数据列表
 */
val sampleFoodItems = listOf(
    FoodItem(
        id = 1,
        name = "意大利面",
        description = "经典意大利面配番茄酱和帕玛森奶酪",
        imageRes = R.drawable.ic_launcher_foreground, // 使用默认图标作为示例
        price = 12.99,
        rating = 4.5f
    ),
    FoodItem(
        id = 2,
        name = "披萨",
        description = "手工制作的玛格丽特披萨，配新鲜的罗勒和马苏里拉奶酪",
        imageRes = R.drawable.ic_launcher_foreground,
        price = 15.99,
        rating = 4.8f
    ),
    FoodItem(
        id = 3,
        name = "汉堡",
        description = "美味多汁的牛肉汉堡，配上新鲜的生菜、番茄和特制酱料",
        imageRes = R.drawable.ic_launcher_foreground,
        price = 10.99,
        rating = 4.2f
    ),
    FoodItem(
        id = 4,
        name = "沙拉",
        description = "健康的凯撒沙拉，配上脆脆的面包丁和帕玛森奶酪",
        imageRes = R.drawable.ic_launcher_foreground,
        price = 8.99,
        rating = 4.0f
    ),
    FoodItem(
        id = 5,
        name = "寿司",
        description = "新鲜的三文鱼寿司卷，配上芥末和酱油",
        imageRes = R.drawable.ic_launcher_foreground,
        price = 18.99,
        rating = 4.7f
    )
)

/**
 * 示例数据：食品分类
 */
val foodCategories = listOf("全部", "意大利", "中餐", "日本料理", "快餐", "甜点", "饮料")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdvancedLayoutScreen(onBackClick: () -> Unit) {
    // 创建一个Scaffold脚手架，包含顶部应用栏和底部SnackBar
    val snackbarHostState = remember { SnackbarHostState() }
    // 创建协程作用域，用于启动协程
    val coroutineScope = rememberCoroutineScope()
    
    // 使用状态提升，创建一个收藏列表
    var favoriteItems by remember { mutableStateOf(setOf<Int>()) }
    
    // 当前选中的分类
    var selectedCategory by remember { mutableStateOf(foodCategories[0]) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("高级布局与组件") },
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
        ) {
            // 顶部分类选择器
            CategorySelector(
                categories = foodCategories,
                selectedCategory = selectedCategory,
                onCategorySelected = { selectedCategory = it }
            )
            
            // 主要内容：食品列表
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(sampleFoodItems) { foodItem ->
                    // 检查当前项目是否被收藏
                    val isFavorite = favoriteItems.contains(foodItem.id)
                    
                    FoodItemCard(
                        foodItem = foodItem,
                        isFavorite = isFavorite,
                        onFavoriteClick = { 
                            // 切换收藏状态
                            favoriteItems = if (isFavorite) {
                                favoriteItems - foodItem.id
                            } else {
                                favoriteItems + foodItem.id
                            }
                            
                            // 显示Snackbar
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    message = if (isFavorite) "已从收藏中移除" else "已添加到收藏",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}

/**
 * 分类选择器组件
 * 使用LazyRow横向滚动显示所有分类
 */
@Composable
fun CategorySelector(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    // 使用LazyRow创建水平滚动列表
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            // 判断当前分类是否被选中
            val isSelected = category == selectedCategory
            
            // 使用动画效果改变背景颜色
            val backgroundColor by animateColorAsState(
                targetValue = if (isSelected) 
                    MaterialTheme.colorScheme.primaryContainer 
                else 
                    MaterialTheme.colorScheme.surface,
                label = "背景颜色动画"
            )
            
            // 使用动画效果改变边框宽度
            val borderWidth by animateDpAsState(
                targetValue = if (isSelected) 2.dp else 1.dp,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                ),
                label = "边框宽度动画"
            )
            
            // 分类选择器项
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .border(
                        width = borderWidth,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(50)
                    )
                    .clickable { onCategorySelected(category) },
                color = backgroundColor
            ) {
                Text(
                    text = category,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    color = if (isSelected) 
                        MaterialTheme.colorScheme.onPrimaryContainer 
                    else 
                        MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

/**
 * 食品项卡片组件
 * 展示食品信息，包括图片、名称、描述、价格和评分
 */
@Composable
fun FoodItemCard(
    foodItem: FoodItem,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit
) {
    // 使用Card组件创建卡片式布局
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            // 食品图片
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                // 图片
                Image(
                    painter = painterResource(id = foodItem.imageRes),
                    contentDescription = foodItem.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                
                // 收藏按钮，放在右上角
                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(36.dp)
                        .background(Color.White.copy(alpha = 0.7f), CircleShape)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = if (isFavorite) "取消收藏" else "收藏",
                        tint = if (isFavorite) Color.Red else Color.Gray
                    )
                }
                
                // 价格标签，放在左下角
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp),
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "¥${foodItem.price}",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            // 食品信息
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // 名称和评分
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 名称
                    Text(
                        text = foodItem.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    // 评分
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "评分",
                            tint = Color(0xFFFFC107) // 黄色星星
                        )
                        Text(
                            text = foodItem.rating.toString(),
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // 描述
                Text(
                    text = foodItem.description,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // 添加到购物车按钮
                Button(
                    onClick = { /* 添加到购物车逻辑 */ },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("添加到购物车")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdvancedLayoutScreenPreview() {
    ComposeDemoTheme {
        AdvancedLayoutScreen(onBackClick = {})
    }
} 