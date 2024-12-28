package pt.isec.marco.firebase.ui.screens

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
import pt.isec.marco.firebase.ui.viewmodels.FirebaseViewModel
import pt.isec.marco.firebase.ui.viewmodels.Pergunta
import pt.isec.marco.firebase.ui.viewmodels.Questionario
import pt.isec.marco.firebase.utils.FStorageUtil

@Composable
fun HistoricoQuestionarioScreen(
    viewModel: FirebaseViewModel,
    navController: NavHostController,
    showComplete: Boolean = false,
    showAnswer: Boolean? = null
) {
    val questionarioIds = viewModel.questionarios.value
    var questionarios by remember { mutableStateOf<List<Questionario>>(emptyList()) }

    LaunchedEffect(questionarioIds) {
        questionarios = mutableListOf()

        questionarioIds.forEach { questionarioId ->
            FStorageUtil.getQuestionarioById(questionarioId) { questionario, _ ->
                if (questionario != null) {
                    questionarios = questionarios + questionario
                }
            }
        }
    }

    Column(Modifier.fillMaxSize()){
        var i =0;
        if (questionarios.isNotEmpty()) {
            questionarios.forEach { questionario ->
                var answer by remember { mutableStateOf<Boolean?>(null) }

                Text("Questionario1: ${questionario.descricao}")
                Text("Questionario3: ${questionario.id}")
                questionario.perguntas.forEach { pergunta ->
                    TipoPerguntaCard(
                        pergunta,
                        showComplete = showComplete,
                        showAnswer = answer
                    )

                }
            }
        }
    }
}