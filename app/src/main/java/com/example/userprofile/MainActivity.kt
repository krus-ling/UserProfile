package com.example.userprofile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.userprofile.ui.theme.UserFormTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UserFormApp()
        }
    }
}

@Composable
fun UserFormApp() {
    var isDarkTheme by rememberSaveable { mutableStateOf(false) }

    UserFormTheme(darkTheme = isDarkTheme) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            UserFormContent(isDarkTheme, onThemeChange = { isDarkTheme = it })
        }
    }
}

@Composable
fun UserFormContent(
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    var name by rememberSaveable { mutableStateOf("") }
    var age by rememberSaveable { mutableStateOf(18f) }
    var gender by rememberSaveable { mutableStateOf("Мужской") }
    var newsletter by rememberSaveable { mutableStateOf(false) }
    var showSummary by remember { mutableStateOf(false) }
    var nameError by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 🖼️ Аватар
                Image(
                    painter = painterResource(id = R.drawable.ic_avatar),
                    contentDescription = "Аватар",
                    modifier = Modifier.size(100.dp)
                )

                // ✏️ Поле имени
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        if (it.isNotBlank()) nameError = false
                    },
                    label = { Text(stringResource(id = R.string.name_hint)) },
                    isError = nameError,
                    modifier = Modifier.fillMaxWidth()
                )
                if (nameError) {
                    Text(
                        text = stringResource(id = R.string.error_name),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                // 🎚️ Возраст
                Text(text = stringResource(R.string.age_label, age.toInt()))
                Slider(
                    value = age,
                    onValueChange = { age = it },
                    valueRange = 1f..100f,
                    steps = 99,
                    modifier = Modifier.fillMaxWidth()
                )

                // 🚻 Пол
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    listOf("Мужской", "Женский").forEach { option ->
                        Row(
                            Modifier
                                .selectable(
                                    selected = (gender == option),
                                    onClick = { gender = option }
                                )
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (gender == option),
                                onClick = { gender = option }
                            )
                            Text(text = option)
                        }
                    }
                }

                // ✉️ Подписка
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = newsletter, onCheckedChange = { newsletter = it })
                    Text(text = stringResource(id = R.string.newsletter))
                }

                // 🧾 Кнопка "Отправить"
                Button(
                    onClick = {
                        if (name.isBlank()) {
                            nameError = true
                            showSummary = false
                        } else {
                            showSummary = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(id = R.string.submit))
                }

                // 🎉 Сводка (анимировано)
                AnimatedVisibility(
                    visible = showSummary,
                    enter = fadeIn() + slideInVertically()
                ) {
                    val subscription = if (newsletter) "✅ Подписан" else "❌ Не подписан"
                    Text(
                        text = stringResource(
                            id = R.string.summary,
                            name,
                            age.toInt(),
                            gender,
                            subscription
                        ),
                        modifier = Modifier.padding(top = 8.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 🌗 Переключатель темы
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = if (isDarkTheme) "🌙 Тёмная тема" else "☀️ Светлая тема")
                    Spacer(modifier = Modifier.width(8.dp))
                    Switch(
                        checked = isDarkTheme,
                        onCheckedChange = { onThemeChange(it) }
                    )
                }
            }
        }
    }
}

@Preview(name = "Light Mode", showBackground = true, widthDp = 400, heightDp = 800)
@Composable
fun PreviewUserFormLight() {
    UserFormTheme(darkTheme = false) {
        UserFormContent(isDarkTheme = false, onThemeChange = {})
    }
}

@Preview(name = "Dark Mode", showBackground = true, widthDp = 400, heightDp = 800)
@Composable
fun PreviewUserFormDark() {
    UserFormTheme(darkTheme = true) {
        UserFormContent(isDarkTheme = true, onThemeChange = {})
    }
}

@Preview(name = "Landscape Mode", showBackground = true, widthDp = 800, heightDp = 400)
@Composable
fun PreviewUserFormLandscape() {
    UserFormTheme(darkTheme = false) {
        UserFormContent(isDarkTheme = false, onThemeChange = {})
    }
}
