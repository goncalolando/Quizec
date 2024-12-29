package pt.isec.marco.firebase.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.DialogHost
import pt.isec.marco.firebase.ui.viewmodels.FirebaseViewModel
import pt.isec.marco.firebase.ui.viewmodels.Pergunta
import pt.isec.marco.firebase.ui.viewmodels.Questionario
import pt.isec.marco.firebase.utils.FStorageUtil

@Composable
fun HistoricoQuestionarioScreen(
    viewModel: FirebaseViewModel,
    navController: NavHostController,
    showComplete: Boolean = false,
) {
    val questionarioIds = viewModel.questionarios.value
    //var questionarios by remember { mutableStateOf<List<Questionario>>(emptyList()) }
    var perguntas by remember { mutableStateOf<List<Pergunta>>(emptyList()) }
    val questionarios by remember { viewModel.questionarios.collectAsState() }  // Observando a lista de perguntas

    LaunchedEffect(questionarioIds) {
        questionarios = mutableListOf()

        questionarios = questionarioIds.mapNotNull { questionarioId ->
            FStorageUtil.getQuestionarioByIdSuspend(questionarioId)
        }

        if (questionarios.isNotEmpty()) {
            perguntas = questionarios.flatMap { questionario ->
                questionario.perguntas.mapNotNull { perguntaId ->
                    FStorageUtil.getPerguntaByIdSuspend(perguntaId)
                }
            }
        }
    }

    Column(Modifier.fillMaxSize()) {
        if (questionarios.isNotEmpty()) {
            questionarios.forEach { questionario ->
                var answer by remember { mutableStateOf<ShowAnswer?>(null) }
                Text("Questionario1: ${questionario.descricao}")
                Text("Questionario3: ${questionario.id}")
                perguntas.forEach { pergunta ->
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
                        else -> ShowAnswer.NotAnswered
                    }
                    Text("Pergunta: ${pergunta.titulo}")
                    TipoPerguntaCard(
                        pergunta = pergunta,
                        showComplete = showComplete,
                        showAnswer = answer
                    )
                }
            }
        }
    }
}
