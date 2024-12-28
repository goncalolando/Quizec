package pt.isec.marco.firebase.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import kotlinx.coroutines.suspendCancellableCoroutine
import pt.isec.marco.firebase.ui.viewmodels.FirebaseViewModel
import pt.isec.marco.firebase.ui.viewmodels.Pergunta
import pt.isec.marco.firebase.ui.viewmodels.Questionario
import pt.isec.marco.firebase.utils.FStorageUtil
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

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
            Button(
                onClick = {
                    navController.navigate("ver-questionario") {
                        popUpTo("ver-questionario") { inclusive = true }
                    }
                },
            ) {
                Text("Ver questionario")
            }
        }
        var confirmaDialog by remember { mutableStateOf(false) }
        var showSuccessMessage by remember { mutableStateOf(false) }

        Button(
                onClick = { confirmaDialog = true },
                modifier = Modifier
                    .align(Alignment.BottomCenter)) {
                Text("Terminar Questionário")
            }
        if (confirmaDialog) {
            val perguntasIds = viewModel.perguntas.value
            var perguntas by remember { mutableStateOf<List<Pergunta>>(emptyList()) }
            LaunchedEffect(perguntasIds) {
                perguntas = getPerguntasByIds(perguntasIds)}
            ConfirmaDialog(
                onConfirm = {
                    confirmaDialog = false
                     viewModel.addQuestioanrioToFirestore(
                            Questionario(
                                id = "",
                                descricao = "Questionáriofuncionado",
                                perguntas = perguntas
                            )
                        )
                     showSuccessMessage = true
                },
                onDismiss = { confirmaDialog = false }
            )
        }

        if (showSuccessMessage) {
            SuccessMessageDialog(
                message = "Questionário criado com sucesso!",
                onDismiss = {
                    showSuccessMessage = false
                    navController.navigate("menu-criador") {
                        popUpTo("menu-criador") { inclusive = true }
                    }}
            )
        }
        }
    }

suspend fun getPerguntasByIds(perguntasIds: List<String>): List<Pergunta> {
    val perguntas = mutableListOf<Pergunta>()
    // Aguarda a recuperação de todas as perguntas
    for (perguntaId in perguntasIds) {
        val pergunta = getPerguntaByIdSuspended(perguntaId)
        if (pergunta != null) {
            perguntas.add(pergunta)
        }
    }
    return perguntas
}
suspend fun getPerguntaByIdSuspended(perguntaId: String): Pergunta? {
    return suspendCancellableCoroutine { continuation ->
        // Chama a função original com um callback
        FStorageUtil.getPerguntaById(perguntaId) { pergunta, error ->
            if (error != null) {
                // Se houver erro, "resumimos" a coroutine com a exceção
                continuation.resumeWithException(error)
            } else {
                // Se a pergunta foi encontrada, "resumimos" com o resultado
                continuation.resume(pergunta)
            }
        }
    }
}

@Composable
    fun ConfirmaDialog(
        onConfirm: () -> Unit,
        onDismiss: () -> Unit
    ) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .background(Color.White)
                .padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Confirmar",
                    fontSize = 20.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Tem a certeza de que deseja terminar o questionário?",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Button(onClick = onDismiss) {
                        Text("Cancelar")
                    }
                    Button(onClick = onConfirm) {
                        Text("Terminar")
                    }
                }
            }
        }
    }
}

@Composable
fun SuccessMessageDialog(message: String, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .background(Color.White)
                .padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Sucesso",
                    fontSize = 20.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = message,
                    fontSize = 16.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onDismiss) {
                    Text("OK")
                }
            }
        }
    }
}
