package pt.isec.marco.firebase.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import pt.isec.marco.firebase.ui.viewmodels.FirebaseViewModel
import pt.isec.marco.firebase.ui.viewmodels.Pergunta

@Composable
fun CriarQuestionarioScreen(
    viewModel: FirebaseViewModel,
    navController: NavHostController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("User: ${viewModel.user.value?.email ?: ""}")
            repeat(viewModel.perguntas.value.size) { iteration ->
                val pergunta = viewModel.perguntas.value[iteration]

            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Questionario")
            Button(
                onClick = {
                    navController.navigate("tipo-pergunta") {
                        popUpTo("tipo-pergunta") { inclusive = true }
                    }
                }
            ) {
                Text("Criar pergunta")
            }
            Button(
                onClick = {
                    navController.navigate("seleciona-perguntas") {
                        popUpTo("seleciona-perguntas") { inclusive = true }
                    }
                }
            ) {
                Text("Adicionar perguntas já existentes")
            }
        }

        Button(
            onClick = {
                navController.navigate("ver-questionario") {
                    popUpTo("ver-questionario") { inclusive = true }
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
        ) {
            Text("Ver questionario")
        }
    }
}
