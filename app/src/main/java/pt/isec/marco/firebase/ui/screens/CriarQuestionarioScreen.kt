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
import androidx.compose.material3.TextField
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
import com.google.firebase.auth.FirebaseAuth
import pt.isec.marco.firebase.ui.viewmodels.FirebaseViewModel
import pt.isec.marco.firebase.ui.viewmodels.Pergunta
import pt.isec.marco.firebase.ui.viewmodels.Questionario
import pt.isec.marco.firebase.utils.FStorageUtil.Companion.getPerguntaByIdSuspend

@Composable
fun CriarQuestionarioScreen(
    viewModel: FirebaseViewModel,
    navController: NavHostController
) {
    var confirmaDialog by remember { mutableStateOf(false) }
    var mostraMsgSucesso by remember { mutableStateOf(false) }
    var nomeQuestionario by remember { mutableStateOf("") } // Variável para armazenar o nome do questionário

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
            Text("Questionário")
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
                }
            ) {
                Text("Ver questionário")
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { confirmaDialog = true }
            ) {
                Text("Terminar Questionário")
            }
        }
    }

    if (confirmaDialog) {
        val perguntasIds = viewModel.perguntas.value
        var perguntas by remember { mutableStateOf<List<Pergunta>>(emptyList()) }
        LaunchedEffect(perguntasIds) {
            perguntas = getPerguntasByIds(perguntasIds)
        }

        GuardaQuestionario (
            onConfirm = {
                confirmaDialog = false
                viewModel.addQuestioanrioToFirestore(
                    Questionario(
                        id = "",
                        idUtilizador = FirebaseAuth.getInstance().currentUser?.uid ?: "",
                        descricao = nomeQuestionario,
                        perguntas = perguntas
                    )
                )
                viewModel.perguntas.value = emptyList()
                mostraMsgSucesso = true
            },
            onDismiss = { confirmaDialog = false },
            nomeQuestionario = nomeQuestionario,
            onNomeChange = { nomeQuestionario = it }
        )
    }

    if (mostraMsgSucesso) {
        MsgSucesso (
            message = "Questionário criado com sucesso!",
            onDismiss = {
                mostraMsgSucesso = false
                navController.navigate("menu-criador") {
                    popUpTo("menu-criador") { inclusive = true }
                }
            }
        )
    }
}

@Composable
fun GuardaQuestionario(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    nomeQuestionario: String,
    onNomeChange: (String) -> Unit
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
                    text = "Guardar Questionário",
                    fontSize = 20.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "De um nome ao seu questionário:",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
                TextField(
                    value = nomeQuestionario,
                    onValueChange = { onNomeChange(it) },
                    placeholder = { Text("Escreve aqui...") }
                )
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
fun MsgSucesso(message: String, onDismiss: () -> Unit) {
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
suspend fun getPerguntasByIds(perguntasIds: List<String>): List<Pergunta> {
    val perguntas = mutableListOf<Pergunta>()
    for (perguntaId in perguntasIds) {
        val pergunta = getPerguntaByIdSuspend(perguntaId)
        if (pergunta != null) {
            perguntas.add(pergunta)
        }
    }
    return perguntas
}