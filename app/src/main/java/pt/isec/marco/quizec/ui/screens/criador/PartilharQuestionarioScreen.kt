package pt.isec.marco.quizec.ui.screens.criador

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pt.isec.marco.quizec.ui.viewmodels.FirebaseViewModel
import pt.isec.marco.quizec.ui.viewmodels.Partilha


@Composable
fun PartilharQuestionarioScreen(
    viewModel: FirebaseViewModel,
    codigoQuestionario: MutableState<String>,
    tempoEspera: MutableState<Long>,
    duracao: MutableState<Long>,
) {

    var partilha by remember { mutableStateOf<Partilha?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
        ) {
            OutlinedTextField(
                value = codigoQuestionario.value,
                isError = codigoQuestionario.value.isEmpty(),
                label = { Text("Código de Partilha:") },
                onValueChange = { newText ->
                    codigoQuestionario.value = newText
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = tempoEspera.value.toString(),
                isError = tempoEspera.value <= 0,
                label = { Text("Tempo de Espera (em segundos):") },
                onValueChange = { newText ->
                    tempoEspera.value = newText.toLongOrNull() ?: 0L
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = duracao.value.toString(),
                isError = duracao.value <= 0,
                label = { Text("Duração do Questionário (em segundos):") },
                onValueChange = { newText ->
                    duracao.value = newText.toLongOrNull() ?: 0L
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))

            // Botão para iniciar a partilha
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        viewModel.addPartilhaToFirestore(
                            Partilha(
                                id = "",
                                idQuestionario = codigoQuestionario.value,
                                tempoEspera = tempoEspera.value,
                                duracao = duracao.value
                            )
                        )
                    }
                ) {
                    Text("Começar Partilha")
                }
            }
        }
    }
}
