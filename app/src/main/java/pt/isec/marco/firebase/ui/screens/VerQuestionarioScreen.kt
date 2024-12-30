package pt.isec.marco.firebase.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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

    Column(Modifier.fillMaxSize()) {
        if (perguntas.isNotEmpty()) {
            perguntas.forEach { pergunta ->
                TipoPerguntaCard(
                    pergunta = pergunta,
                    showComplete = showComplete
                )
            }
        }
    }

}