package com.tianyejia.composedemo

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tianyejia.composedemo.ui.theme.ComposeDemoTheme
import com.tianyejia.composedemo.week1.BasicComposeActivity
import com.tianyejia.composedemo.week2.AdvancedLayoutActivity
import com.tianyejia.composedemo.week2.GridLayoutActivity
import com.tianyejia.composedemo.week3.StateManagementActivity
import com.tianyejia.composedemo.week3.LifecycleAwareActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeDemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Jetpack Compose 学习",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = "请选择学习内容",
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // 第一周学习内容入口
        LearningCard(
            title = "第一周：Compose 基础",
            description = "学习Compose的基本概念、组件和状态管理",
            onClick = {
                val intent = Intent(context, BasicComposeActivity::class.java)
                context.startActivity(intent)
            }
        )
        
        // 第二周学习内容入口 - 高级布局
        LearningCard(
            title = "第二周：布局和基础组件",
            description = "学习Row, Column, Box等布局组件",
            onClick = {
                val intent = Intent(context, AdvancedLayoutActivity::class.java)
                context.startActivity(intent)
            }
        )
        
        // 第二周学习内容入口 - 网格布局
        LearningCard(
            title = "第二周：网格布局与自定义组件",
            description = "学习Grid布局和自定义组件",
            onClick = {
                val intent = Intent(context, GridLayoutActivity::class.java)
                context.startActivity(intent)
            }
        )
        
        // 第三周学习内容入口 - 状态管理
        LearningCard(
            title = "第三周：状态管理",
            description = "学习Compose中的状态管理和ViewModel集成",
            onClick = {
                val intent = Intent(context, StateManagementActivity::class.java)
                context.startActivity(intent)
            }
        )
        
        // 第三周学习内容入口 - 生命周期
        LearningCard(
            title = "第三周：生命周期感知组件",
            description = "学习Compose中的副作用和生命周期相关API",
            onClick = {
                val intent = Intent(context, LifecycleAwareActivity::class.java)
                context.startActivity(intent)
            }
        )
    }
}

@Composable
fun LearningCard(
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = description,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = onClick,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("开始学习")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    ComposeDemoTheme {
        MainScreen()
    }
}