package com.example.userprofile

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
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
    var age by rememberSaveable { mutableFloatStateOf(18f) }
    var gender by rememberSaveable { mutableStateOf("Мужской") }
    var newsletter by rememberSaveable { mutableStateOf(false) }
    var showSummary by remember { mutableStateOf(false) }
    var nameError by remember { mutableStateOf(false) }
    var selectedImageUri by rememberSaveable { mutableStateOf<String?>(null) }
    var showAvatarDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val imagePickerLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            selectedImageUri = uri?.toString()
        }
    val focusManager = LocalFocusManager.current

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState())
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    focusManager.clearFocus()
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .clickable { showAvatarDialog = true },
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedImageUri != null) {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(selectedImageUri)
                                .crossfade(true)
                                .build(),
                            contentDescription = "Выбранный аватар",
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.ic_avatar),
                            contentDescription = "Аватар по умолчанию",
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                // 💬 Диалог выбора аватара
                if (showAvatarDialog) {
                    AlertDialog(
                        onDismissRequest = { showAvatarDialog = false },
                        title = { Text("Выберите аватар") },
                        text = {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    modifier = Modifier.padding(vertical = 8.dp)
                                ) {
                                    listOf(
                                        R.drawable.avatar_1,
                                        R.drawable.avatar_2,
                                        R.drawable.avatar_3
                                    ).forEach { res ->
                                        Image(
                                            painter = painterResource(id = res),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(72.dp)
                                                .clip(CircleShape)
                                                .clickable {
                                                    selectedImageUri = "android.resource://${context.packageName}/$res"
                                                    showAvatarDialog = false
                                                },
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Button(onClick = {
                                    imagePickerLauncher.launch("image/*")
                                    showAvatarDialog = false
                                }) {
                                    Text("Выбрать из галереи")
                                }
                            }
                        },
                        confirmButton = {}
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // ✏️ Поле имени
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        if (it.isNotBlank()) nameError = false
                    },
                    label = { Text(stringResource(id = R.string.name_hint)) },
                    isError = nameError,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    shape = MaterialTheme.shapes.medium
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.submit),
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                // 🎉 Сводка (анимировано)
                AnimatedVisibility(
                    visible = showSummary,
                    enter = fadeIn() + slideInVertically()
                ) {
                    val subscription = if (newsletter) "✅" else "❌"
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

                /// 🌗 Переключение темы одной кнопкой
                Box(
                    modifier = Modifier
                        .size(72.dp) // чуть больше зона клика
                        .clickable { onThemeChange(!isDarkTheme) },
                    contentAlignment = Alignment.Center
                ) {
                    Crossfade(targetState = isDarkTheme, label = "themeCrossfade") { dark ->
                        Icon(
                            imageVector = if (dark) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = if (dark) "Светлая тема" else "Тёмная тема",
                            modifier = Modifier.size(56.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
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
