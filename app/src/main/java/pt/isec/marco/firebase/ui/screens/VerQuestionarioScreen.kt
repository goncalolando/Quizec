package pt.isec.marco.firebase.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
fun VerQuestionarioScreen(
    viewModel: FirebaseViewModel,
    navController: NavHostController,
    showComplete: Boolean = false,
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

    if (perguntas.isEmpty()) {
        Text("Nenhuma pergunta disponível")
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(perguntas) { pergunta ->
                    TipoPerguntaCard(
                        pergunta = pergunta,
                        showComplete = showComplete
                    )
                }
            }
            Button(
                onClick = {
                    navController.navigate("criar-questionario") {
                        popUpTo("criar-questionario") {
                            inclusive = true
                        }
                    }

                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            ) {
                Text("Voltar")
            }
        }
    }
}