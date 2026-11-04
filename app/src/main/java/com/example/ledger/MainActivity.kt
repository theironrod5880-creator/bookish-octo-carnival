package com.example.ledger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF050914)) {
                    VaultProcessorUI()
                }
            }
        }
    }
}

@Composable
fun VaultProcessorUI() {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Measure", "Time", "Align", "Cosmos", "Lexicon", "Library")

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Header
        Text(
            text = "THE OPHAN TUTOR",
            color = Color(0xFFFFF0A6),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 16.dp)
        )

        // Tab Row
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color(0xFF0A1428),
            contentColor = Color(0xFF7FDBFF),
            edgePadding = 0.dp
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title, color = if (selectedTab == index) Color(0xFFD4AF37) else Color(0xFF7FDBFF)) }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Tab Content
        when (selectedTab) {
            0 -> MeasureTab()
            1 -> TimeTab()
            2 -> AlignTab()
            3 -> CosmosTab()
            4 -> LexiconTab()
            5 -> LibraryTab()
        }
    }
}

@Composable
fun TabContent(title: String, description: String) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = title, color = Color(0xFFD4AF37), fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = description, color = Color(0xFFE8F4F8), fontSize = 16.sp)
    }
}

@Composable
fun MeasureTab() = TabContent("Measure", "Common Cubit, Royal Cubit, Reed, Handbreadth.")
@Composable
fun TimeTab() = TabContent("Time", "Seconds to Jubilees (using 360-day and 365.242-day years).")
@Composable
fun AlignTab() = TabContent("Align", "The Alignment Scanner (Sabbath, Metonic, Jubilee, Mastercore).")
@Composable
fun CosmosTab() = TabContent("Cosmos", "The Physical Constants (360°, atomic cycles).")
@Composable
fun LexiconTab() = TabContent("Lexicon", "The Hebrew Records (HEB-S001 to S018).")
@Composable
fun LibraryTab() = TabContent("Library", "The Source Text list.")
