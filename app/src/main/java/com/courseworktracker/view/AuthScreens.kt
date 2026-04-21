package com.courseworktracker.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.courseworktracker.R
import com.courseworktracker.ui.theme.NdejjeCourseworkTrackerTheme

@Composable
fun TrackerTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false
) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        trailingIcon = {
            if (isPassword) {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = stringResource(id = R.string.toggle_password)
                    )
                }
            }
        }
    )
}

@Composable
fun LoginScreen(onNavigateToRegister: () -> Unit, onLoginSuccess: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.padding_large)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.welcome_back),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(id = R.string.login_subtitle),
            color = Color.Gray
        )
        
        Spacer(modifier = Modifier.height(32.dp))

        TrackerTextField(
            value = email,
            onValueChange = { email = it },
            label = stringResource(id = R.string.email_label)
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))
        TrackerTextField(
            value = password,
            onValueChange = { password = it },
            label = stringResource(id = R.string.password_label),
            isPassword = true
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_large)))

        Button(
            onClick = onLoginSuccess,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(stringResource(id = R.string.login_button))
        }

        TextButton(onClick = onNavigateToRegister) {
            Text(stringResource(id = R.string.register_prompt))
        }
    }
}

@Composable
fun RegisterScreen(onNavigateToLogin: () -> Unit, onRegisterSuccess: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.padding_large)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.create_account),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(32.dp))

        TrackerTextField(
            value = name,
            onValueChange = { name = it },
            label = stringResource(id = R.string.full_name_label)
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))
        TrackerTextField(
            value = email,
            onValueChange = { email = it },
            label = stringResource(id = R.string.email_label)
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))
        TrackerTextField(
            value = password,
            onValueChange = { password = it },
            label = stringResource(id = R.string.password_label),
            isPassword = true
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_large)))

        Button(
            onClick = onRegisterSuccess,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(stringResource(id = R.string.create_account))
        }

        TextButton(onClick = onNavigateToLogin) {
            Text(stringResource(id = R.string.already_have_account))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    NdejjeCourseworkTrackerTheme {
        LoginScreen(onNavigateToRegister = {}, onLoginSuccess = {})
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterPreview() {
    NdejjeCourseworkTrackerTheme {
        RegisterScreen(onNavigateToLogin = {}, onRegisterSuccess = {})
    }
}
