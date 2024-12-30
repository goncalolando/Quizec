package pt.isec.marco.firebase.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import pt.isec.marco.firebase.ui.viewmodels.FirebaseViewModel
import pt.isec.marco.firebase.ui.viewmodels.Pergunta

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
        Box(
            modifier = Modifier.fillMaxSize() // Box que ocupa toda a tela
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize() // A LazyColumn vai ocupar o restante espaço
            ) {
                items(perguntas) { pergunta ->
                    var isSelected by remember { mutableStateOf(false) }

                    val backgroundColor = if (isSelected) Color.Green else Color(108, 147, 201, 255)

                    Box(
                        modifier = Modifier
                            .shadow(4.dp)
                            .background(backgroundColor)
                            .clickable {
                                isSelected = !isSelected
                                if(isSelected) {
                                    Log.d("SelecionarPerguntasScreen", "Adicionou pergunta ${pergunta.titulo}")
                                    viewModel.perguntas.value += pergunta.id
                                }
                                else {
                                    Log.d("SelecionarPerguntasScreen", "Removeu pergunta ${pergunta.titulo}")
                                    viewModel.perguntas.value -= pergunta.id
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(8.dp)
                                .shadow(
                                    4.dp,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .background(
                                    Color.LightGray,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(16.dp)
                        ) {
                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Pergunta: ${pergunta.titulo}",
                                        color = Color.Blue,
                                        modifier = Modifier
                                            .weight(1f)
                                    )
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Seta para frente",
                                        tint = Color.Blue,
                                        modifier = Modifier
                                            .clickable {

                                            }
                                    )
                                }
                                TipoPerguntaCard(
                                    pergunta = pergunta,
                                    showComplete = showComplete,
                                )
                            }
                        }
                    }
                }
            }
            Button(
                onClick = {
                    navController.navigate("ver-questionario") {
                        popUpTo("ver-questionario") {
                            inclusive = true
                        }
                    }

                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            ) {
                Text("Adicionar perguntas")
            }
        }
    }
}