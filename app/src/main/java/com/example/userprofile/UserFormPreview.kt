package com.example.userprofile

import android.view.Surface
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.userprofile.ui.theme.UserFormTheme

@Preview(name = "Light Mode", showBackground = true)
@Composable
fun UserFormPreviewLight() {
    UserFormTheme(darkTheme = false) {
        Surface(modifier = Modifier.fillMaxSize()) {
            UserFormPreviewContent()
        }
    }
}

@Preview(name = "Dark Mode", showBackground = true)
@Composable
fun UserFormPreviewDark() {
    UserFormTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            UserFormPreviewContent()
        }
    }
}

@Composable
fun UserFormPreviewContent() {
    // Контент анкеты со стандартными значениями
}
