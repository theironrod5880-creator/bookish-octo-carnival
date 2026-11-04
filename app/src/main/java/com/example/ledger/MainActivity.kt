package com.example.ledger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.floor

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
fun TimeTab() {
    var inputSeconds by remember { mutableStateOf("") }
    var resultText by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        Text("Time Calculator", color = Color(0xFFD4AF37), fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = inputSeconds,
            onValueChange = { inputSeconds = it },
            label = { Text("Enter Seconds", color = Color(0xFF7FDBFF)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFD4AF37),
                unfocusedBorderColor = Color(0xFF7FDBFF),
                focusedTextColor = Color(0xFFE8F4F8),
                unfocusedTextColor = Color(0xFFE8F4F8)
            ),
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = {
                val secs = inputSeconds.toLongOrNull() ?: 0L
                if (secs > 0) {
                    val days = secs / 86400.0
                    val propheticYears = days / 360.0
                    val jubilees = propheticYears / 49.0
                    resultText = "Days: ${String.format("%.2f", days)}\nProphetic Years (360d): ${String.format("%.2f", propheticYears)}\nJubilees: ${String.format("%.4f", jubilees)}"
                } else {
                    resultText = "Please enter a valid number."
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0A1428), contentColor = Color(0xFFD4AF37))
        ) {
            Text("Calculate")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        if (resultText.isNotEmpty()) {
            Text(resultText, color = Color(0xFFE8F4F8), fontSize = 16.sp)
        }
    }
}

@Composable
fun AlignTab() {
    var yearInput by remember { mutableStateOf("") }
    var alignmentText by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        Text("Alignment Scanner", color = Color(0xFFD4AF37), fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = yearInput,
            onValueChange = { yearInput = it },
            label = { Text("Enter Year (e.g., 2024)", color = Color(0xFF7FDBFF)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFD4AF37),
                unfocusedBorderColor = Color(0xFF7FDBFF),
                focusedTextColor = Color(0xFFE8F4F8),
                unfocusedTextColor = Color(0xFFE8F4F8)
            ),
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = {
                val year = yearInput.toIntOrNull() ?: 0
                if (year > 0) {
                    // Simplified Mastercore calculation (Center: 1027 BCE)
                    val mastercoreYears = Math.abs(year - (-1027))
                    val sabbath = mastercoreYears % 7
                    val jubilee = mastercoreYears % 49
                    val metonic = year % 19
                    
                    alignmentText = "Year: $year CE\n" +
                            "Mastercore Offset (from 1027 BCE): $mastercoreYears years\n" +
                            "Sabbath Cycle: ${if (sabbath == 0) "Sabbath Year" else "$sabbath years past"}\n" +
                            "Jubilee Cycle: ${if (jubilee == 0) "Jubilee Year" else "$jubilee years past"}\n" +
                            "Metonic Cycle: Phase $metonic"
                } else {
                    alignmentText = "Please enter a valid positive year."
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0A1428), contentColor = Color(0xFFD4AF37))
        ) {
            Text("Scan Alignment")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        if (alignmentText.isNotEmpty()) {
            Text(alignmentText, color = Color(0xFFE8F4F8), fontSize = 16.sp)
        }
    }
}

@Composable
fun CosmosTab() = TabContent("Cosmos", "The Physical Constants (360°, atomic cycles).")
@Composable
fun LexiconTab() = TabContent("Lexicon", "The Hebrew Records (HEB-S001 to S018).")
@Composable
fun LibraryTab() = TabContent("Library", "The Source Text list.")
