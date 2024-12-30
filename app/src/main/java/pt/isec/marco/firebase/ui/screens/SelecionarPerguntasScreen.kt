package pt.isec.marco.firebase.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import pt.isec.marco.firebase.ui.viewmodels.FirebaseViewModel

@Composable
fun SelecionarPerguntasScreen(
    viewModel: FirebaseViewModel,
    navController: NavHostController,
    showComplete: Boolean = false,
) {
    val perguntas by remember { viewModel.perguntasAux }

    LaunchedEffect(Unit) {
        viewModel.startPerguntasObserver()
    }
    if (perguntas.isEmpty()) {
        Text("Nenhuma pergunta disponível")
    } else {
        LazyColumn {
            items(perguntas) { pergunta ->
                Column {
                    Text("Pergunta: ${pergunta.titulo}")
                    TipoPerguntaCard(
                        pergunta = pergunta,
                        showComplete = showComplete,
                    )
                }
            }
        }
    }
}