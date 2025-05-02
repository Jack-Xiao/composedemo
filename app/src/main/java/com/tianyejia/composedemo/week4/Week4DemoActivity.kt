package com.tianyejia.composedemo.week4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

/**
 * 第四周示例: 主题与动画
 */
class Week4DemoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Week4Navigation()
                }
            }
        }
    }
}

@Composable
fun Week4Navigation() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            Week4HomeScreen(navController)
        }
        composable("material_theme_basics") {
            MaterialThemeBasicsDemo()
        }
        composable("theme_switcher") {
            ThemeSwitcherDemo()
        }
        composable("basic_animation") {
            BasicAnimationDemo()
        }
        composable("advanced_animation") {
            AdvancedAnimationDemo()
        }
        composable("animated_theme") {
            AnimatedThemeDemo()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Week4HomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "第四周：主题与动画",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(vertical = 24.dp)
        )
        
        Text(
            text = "在这个模块中，您将学习如何使用 Material Design 主题和 Compose 动画系统来创建有吸引力和互动性的用户界面。",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        // 示例列表
        Week4DemoCard(
            title = "Material Theme 基础",
            description = "了解 MaterialTheme 的基本组成部分：颜色、字体和形状",
            onClick = { navController.navigate("material_theme_basics") }
        )
        
        Week4DemoCard(
            title = "主题切换器",
            description = "学习如何在浅色和深色主题之间切换，以及如何创建自定义主题",
            onClick = { navController.navigate("theme_switcher") }
        )
        
        Week4DemoCard(
            title = "基础动画",
            description = "探索 Compose 中的基础动画：状态动画、可见性动画和内容动画",
            onClick = { navController.navigate("basic_animation") }
        )
        
        Week4DemoCard(
            title = "高级动画",
            description = "学习更复杂的动画效果：无限动画和手势驱动的动画",
            onClick = { navController.navigate("advanced_animation") }
        )
        
        Week4DemoCard(
            title = "动画主题",
            description = "结合主题和动画，创建平滑的主题切换效果",
            onClick = { navController.navigate("animated_theme") }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Week4DemoCard(title: String, description: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
} 