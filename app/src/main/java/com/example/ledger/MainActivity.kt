package com.example.ledger

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

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

data class LexiconEntry(val id: String, val hebrew: String, val transliteration: String, val meaning: String)
data class LexiconData(val lexicon: List<LexiconEntry>)

@Composable
fun VaultProcessorUI() {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Measure", "Time", "Align", "Cosmos", "Lexicon", "Library")
    val context = LocalContext.current

    val lexiconList = remember {
        try {
            val inputStream = context.assets.open("hebrew-lexicon.json")
            val reader = InputStreamReader(inputStream)
            val type = object : TypeToken<LexiconData>() {}.type
            val data: LexiconData = Gson().fromJson(reader, type)
            data.lexicon
        } catch (e: Exception) {
            emptyList<LexiconEntry>()
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("THE OPHAN TUTOR", color = Color(0xFFFFF0A6), fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 16.dp))
        
        ScrollableTabRow(selectedTabIndex = selectedTab, containerColor = Color(0xFF0A1428), contentColor = Color(0xFF7FDBFF), edgePadding = 0.dp) {
            tabs.forEachIndexed { index, title ->
                Tab(selected = selectedTab == index, onClick = { selectedTab = index }, text = { Text(title, color = if (selectedTab == index) Color(0xFFD4AF37) else Color(0xFF7FDBFF)) })
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        
        when (selectedTab) {
            0 -> TabContent("Measure", "Common Cubit, Royal Cubit, Reed, Handbreadth.")
            1 -> TimeTab()
            2 -> AlignTab()
            3 -> TabContent("Cosmos", "The Physical Constants (360°, atomic cycles).")
            4 -> LexiconTab(lexiconList)
            5 -> TabContent("Library", "The Source Text list.")
        }
    }
}

@Composable
fun TabContent(title: String, description: String) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(title, color = Color(0xFFD4AF37), fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(description, color = Color(0xFFE8F4F8), fontSize = 16.sp)
    }
}

@Composable
fun TimeTab() {
    var inputSeconds by remember { mutableStateOf("") }
    var resultText by remember { mutableStateOf("") }
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        Text("Time Calculator", color = Color(0xFFD4AF37), fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = inputSeconds, onValueChange = { inputSeconds = it }, label = { Text("Enter Seconds", color = Color(0xFF7FDBFF)) }, colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFFD4AF37), unfocusedBorderColor = Color(0xFF7FDBFF), focusedTextColor = Color(0xFFE8F4F8), unfocusedTextColor = Color(0xFFE8F4F8)), modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { val secs = inputSeconds.toLongOrNull() ?: 0L; if (secs > 0) { val days = secs / 86400.0; val pYears = days / 360.0; val jub = pYears / 49.0; resultText = "Days: ${String.format("%.2f", days)}\nProphetic Years: ${String.format("%.2f", pYears)}\nJubilees: ${String.format("%.4f", jub)}" } else { resultText = "Invalid input" } }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0A1428), contentColor = Color(0xFFD4AF37))) { Text("Calculate") }
        Spacer(modifier = Modifier.height(16.dp))
        if (resultText.isNotEmpty()) Text(resultText, color = Color(0xFFE8F4F8))
    }
}

@Composable
fun AlignTab() {
    var yearInput by remember { mutableStateOf("") }
    var alignText by remember { mutableStateOf("") }
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        Text("Alignment Scanner", color = Color(0xFFD4AF37), fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = yearInput, onValueChange = { yearInput = it }, label = { Text("Enter Year", color = Color(0xFF7FDBFF)) }, colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFFD4AF37), unfocusedBorderColor = Color(0xFF7FDBFF), focusedTextColor = Color(0xFFE8F4F8), unfocusedTextColor = Color(0xFFE8F4F8)), modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { val y = yearInput.toIntOrNull() ?: 0; if (y > 0) { val mOffset = Math.abs(y - (-1027)); val sab = mOffset % 7; val jub = mOffset % 49; val met = y % 19; alignText = "Year: $y\nMastercore Offset: $mOffset\nSabbath: ${if (sab == 0) "Sabbath Year" else "$sab yrs past"}\nJubilee: ${if (jub == 0) "Jubilee Year" else "$jub yrs past"}\nMetonic: Phase $met" } else { alignText = "Invalid year" } }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0A1428), contentColor = Color(0xFFD4AF37))) { Text("Scan") }
        Spacer(modifier = Modifier.height(16.dp))
        if (alignText.isNotEmpty()) Text(alignText, color = Color(0xFFE8F4F8))
    }
}

@Composable
fun LexiconTab(entries: List<LexiconEntry>) {
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        Text("Hebrew Lexicon", color = Color(0xFFD4AF37), fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        if (entries.isEmpty()) Text("No data loaded. Check JSON.", color = Color.Red)
        else entries.forEach { entry -> 
            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF0A1428))) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("${entry.id}: ${entry.hebrew}", color = Color(0xFF7FDBFF), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text(entry.transliteration, color = Color(0xFFD4AF37), fontSize = 14.sp)
                    Text(entry.meaning, color = Color(0xFFE8F4F8), fontSize = 14.sp)
                }
            }
        }
    }
}
