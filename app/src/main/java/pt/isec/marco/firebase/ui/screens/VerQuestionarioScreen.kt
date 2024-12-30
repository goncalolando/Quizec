package pt.isec.marco.firebase.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
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
    showAnswer: Boolean? = null
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
                var answer by remember { mutableStateOf<ShowAnswer?>(null) }

                answer = when (pergunta.tipo) {
                    "P01" -> {
                        when (pergunta.respostaCerta.getOrNull(0)) {
                            "true" -> ShowAnswer.BooleanAnswer(true)
                            "false" -> ShowAnswer.BooleanAnswer(false)
                            else -> ShowAnswer.NotAnswered
                        }
                    }
                    "P02" -> {
                        val respostaIndex = pergunta.respostaCerta.getOrNull(0)?.toIntOrNull()
                        ShowAnswer.IntAnswer(respostaIndex)
                    }
                    "P03" -> {
                       val respostaIndex = pergunta.respostaCerta.mapNotNull { it.toIntOrNull() }
                        ShowAnswer.ListAnswer(respostaIndex)
                    }
                    "P04" -> {
                        val respostaIndex = List(pergunta.respostas.size/2) {-1}.toMutableList()
                        for(i in 0 until pergunta.respostaCerta.size/2){
                            val index = pergunta.respostaCerta.indexOf(pergunta.respostas[i])
                            val index2 = pergunta.respostas.indexOf(pergunta.respostaCerta[index+pergunta.respostas.size/2])
                            respostaIndex[i] = index2 - pergunta.respostas.size/2
                        }
                        ShowAnswer.ListAnswer(respostaIndex)
                    }

                    else -> ShowAnswer.NotAnswered
                }

                TipoPerguntaCard(
                    pergunta = pergunta,
                    showComplete = showComplete,
                    showAnswer = answer
                )
            }
        }
    }

}