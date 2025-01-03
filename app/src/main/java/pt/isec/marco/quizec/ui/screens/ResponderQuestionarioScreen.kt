package pt.isec.marco.quizec.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import pt.isec.marco.quizec.ui.viewmodels.FirebaseViewModel

@Composable
fun ResponderQuestionarioScreen(
    viewModel: FirebaseViewModel,
    navController: NavHostController,

    ) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var userInput by remember { mutableStateOf("") }

        Column {
            TextField(
                value = userInput,
                onValueChange = { newText ->
                    userInput = newText
                },
                label = { Text("Introduz codigo questionario") },
                placeholder = { Text("Escreve aqui...") }
            )
        }
        Button(
            onClick = { navController.navigate("responder-questionario")
            {
                popUpTo("responder-questionario") {
                    inclusive = true
                }
            }
            }
        ) {
            Text("Entrar")
        }
    }
}