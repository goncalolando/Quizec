package pt.isec.marco.firebase.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import pt.isec.marco.firebase.ui.viewmodels.FirebaseViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.rememberPagerState

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import pt.isec.marco.firebase.ui.viewmodels.Questionario

@Composable
fun Card(
    viewModel: FirebaseViewModel,
    questionario: Questionario,
    showComplete: Boolean
) {
    val questionariosAux by remember { viewModel.questionariosAux }

    LaunchedEffect(Unit) {
        viewModel.startQuestionariosObserver()
    }

    // Exibir o questionário se houver
    if (questionario != null) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(255, 224, 192))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Título do Questionário
                Text(
                    text = "Título do Questionário: ${questionario.descricao}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                // Se houver perguntas, renderize-as
                questionario.perguntas?.forEach { pergunta ->
                    Text("Pergunta: ${pergunta.titulo}")
                    TipoPerguntaCard(
                        pergunta = pergunta,
                        showComplete = showComplete,
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    } else {
        // Caso não haja questionário para exibir
        Text("Nenhum questionário disponível")
    }
}


@Composable
fun HistoricoQuestionarioScreen(
    viewModel: FirebaseViewModel,
    navController: NavHostController,
    showComplete: Boolean
) {
    var selectedPage by remember { mutableStateOf(-1) }
    val questionariosAux by remember { viewModel.questionariosAux }

    LaunchedEffect(Unit) {
        viewModel.startQuestionariosObserver()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val pagerState = rememberPagerState(pageCount = {
            questionariosAux.size
        })
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val pergunta = questionariosAux[page]
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .padding(2.dp)
            ) {
                Card (
                    viewModel = viewModel,
                    questionario = questionariosAux[page],
                    showComplete = showComplete
                )
            }
        }
        Row(
            Modifier
                .align(Alignment.BottomCenter)
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color =
                    if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(16.dp)
                )
            }
        }
    }
}

//// TODO FINAL
//// trocar o answers para dentro do composable
//@Composable
//fun Card(
//    viewModel: FirebaseViewModel,
//    navController: NavHostController,
//    showComplete: Boolean = false,
//) {
//    val questionariosAux by remember { viewModel.questionariosAux }
//
//    LaunchedEffect(Unit) {
//        viewModel.startQuestionariosObserver()
//    }
//
//    if (questionariosAux.isEmpty()) {
//        Text("Nenhum questionário disponível")
//    } else {
//        LazyColumn {
//            items(questionariosAux) { questionario ->
//                Card(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(16.dp),
//                ) {
//                    Column(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(16.dp)
//                    ) {
//                        // Aqui você pode adicionar o conteúdo que quiser dentro de cada Card
//                        Text(
//                            text = "Título do Questionário: ${questionario.descricao}",
//                            fontSize = 18.sp,
//                            fontWeight = FontWeight.Bold
//                        )
//                        var answer by remember { mutableStateOf<ShowAnswer?>(null) }
//
//                        questionario.perguntas?.forEach { pergunta ->
//                            answer = when (pergunta.tipo) {
//                                "P01" -> {
//                                    when (pergunta.respostaCerta.getOrNull(0)) {
//                                        "true" -> ShowAnswer.BooleanAnswer(true)
//                                        "false" -> ShowAnswer.BooleanAnswer(false)
//                                        else -> ShowAnswer.NotAnswered
//                                    }
//                                }
//
//                                "P02" -> {
//                                    val respostaIndex =
//                                        pergunta.respostaCerta.getOrNull(0)?.toIntOrNull()
//                                    ShowAnswer.IntAnswer(respostaIndex)
//                                }
//
//                                else -> ShowAnswer.NotAnswered
//                            }
//                            Text("Pergunta: ${pergunta.titulo}")
//                            TipoPerguntaCard(
//                                pergunta = pergunta,
//                                showComplete = showComplete,
//                            )
//                        }
//                        // Aqui você pode adicionar mais campos ou lógica conforme necessário
//                        Spacer(modifier = Modifier.height(8.dp))
//                    }
//                }
//            }
//        }
//    }
//    }
//    val questionariosAux by remember { viewModel.questionariosAux }
//
//    LaunchedEffect(Unit) {
//        viewModel.startQuestionariosObserver()
//    }
//
//    if (questionariosAux.isEmpty()) {
//        Text("Nenhum questionário disponível")
//    } else {
//        LazyColumn {
//            items(questionariosAux) { questionario ->
//                Text("Descrição: ${questionario.descricao}")
//                var answer by remember { mutableStateOf<ShowAnswer?>(null) }
//
//                questionario.perguntas?.forEach { pergunta ->
//                    answer = when (pergunta.tipo) {
//                        "P01" -> {
//                            when (pergunta.respostaCerta.getOrNull(0)) {
//                                "true" -> ShowAnswer.BooleanAnswer(true)
//                                "false" -> ShowAnswer.BooleanAnswer(false)
//                                else -> ShowAnswer.NotAnswered
//                            }
//                        }
//
//                        "P02" -> {
//                            val respostaIndex =
//                                pergunta.respostaCerta.getOrNull(0)?.toIntOrNull()
//                            ShowAnswer.IntAnswer(respostaIndex)
//                        }
//
//                        else -> ShowAnswer.NotAnswered
//                    }
//                    Text("Pergunta: ${pergunta.titulo}")
//                    TipoPerguntaCard(
//                        pergunta = pergunta,
//                        showComplete = showComplete,
//                        showAnswer = answer
//                    )
//                }
//            }
//        }
//    }


//    val questionarioIds = viewModel.questionarios.value
//    var questionarios by remember { mutableStateOf<List<Questionario>>(emptyList()) }
//    val perguntasIds = viewModel.perguntas.value
//    var perguntas by remember { mutableStateOf<List<Pergunta>>(emptyList()) }
//
//    LaunchedEffect(questionarioIds) {
//        questionarios = mutableListOf()
//
//        questionarioIds.forEach { questionarioId ->
//            FStorageUtil.getQuestionarioById(questionarioId) { questionario, _ ->
//                if (questionario != null) {
//                    questionarios = questionarios + questionario
//                }
//            }
//            questionarios = questionarioIds.mapNotNull { questionarioId ->
//                FStorageUtil.getQuestionarioByIdSuspend(questionarioId)
//            }
//
//            if (questionarios.isNotEmpty()) {
//                questionarios.forEach { questionario ->
//                    questionario.perguntas.forEach { perguntaId ->
//                        FStorageUtil.getPerguntaById(perguntaId) { pergunta, _ ->
//                            if (pergunta != null) {
//                                perguntas = perguntas + pergunta
//                            }
//                        }
//                        perguntas = questionarios.flatMap { questionario ->
//                            questionario.perguntas.mapNotNull { perguntaId ->
//                                FStorageUtil.getPerguntaByIdSuspend(perguntaId)
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//    Column(Modifier.fillMaxSize()) {
//        if (questionarios.isNotEmpty()) {
//            questionarios.forEach { questionario ->
//                var answer by remember { mutableStateOf<ShowAnswer?>(null) }
//                Text("Questionario1: ${questionario.descricao}")
//                Text("Questionario3: ${questionario.id}")
//                perguntas.forEach { pergunta ->
//                    answer = when (pergunta.tipo) {
//                        "P01" -> {
//                            when (pergunta.respostaCerta.getOrNull(0)) {
//                                "true" -> ShowAnswer.BooleanAnswer(true)
//                                "false" -> ShowAnswer.BooleanAnswer(false)
//                                else -> ShowAnswer.NotAnswered
//                            }
//                        }
//
//                        "P02" -> {
//                            val respostaIndex =
//                                pergunta.respostaCerta.getOrNull(0)?.toIntOrNull()
//                            ShowAnswer.IntAnswer(respostaIndex)
//                        }
//
//                        else -> ShowAnswer.NotAnswered
//                    }
//                    Text("Pergunta: ${pergunta.titulo}")
//                    TipoPerguntaCard(
//                        pergunta = pergunta,
//                        showComplete = showComplete,
//                        showAnswer = answer
//                    )
//                }
//            }
//        }
//    }

