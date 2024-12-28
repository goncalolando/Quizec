package pt.isec.marco.firebase.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import pt.isec.marco.firebase.ui.viewmodels.FirebaseViewModel
import pt.isec.marco.firebase.ui.viewmodels.Pergunta
import pt.isec.marco.firebase.utils.FStorageUtil

@Composable
fun CriarQuestionarioScreen(
    viewModel: FirebaseViewModel,
    navController: NavHostController,
    showComplete: Boolean = false
) {
    val perguntasIds = viewModel.perguntas.value
    var perguntas by remember { mutableStateOf<List<Pergunta>>(emptyList()) }

    LaunchedEffect(perguntasIds) {
        perguntas = mutableListOf()

        perguntasIds.forEach { perguntaId ->
            FStorageUtil.getPerguntaById(perguntaId) { pergunta, _ ->
                if (pergunta != null) {
                    perguntas = perguntas + pergunta
                }
            }
        }
    }
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
            if (perguntas.isNotEmpty()) {
                perguntas.forEach { pergunta ->
                    TipoPerguntaCard(pergunta,showComplete = showComplete)
                }
            }
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
            Button(
                onClick = {
                    navController.navigate("seleciona-perguntas") {
                        popUpTo("seleciona-perguntas") { inclusive = true }
                    }
                }
            ) {
                Text("Terminar criação do questionario")
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
