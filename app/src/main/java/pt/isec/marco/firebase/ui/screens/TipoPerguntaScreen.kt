package pt.isec.marco.firebase.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.rememberPagerState

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import pt.isec.marco.firebase.ui.viewmodels.FirebaseViewModel
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import pt.isec.marco.firebase.R
import pt.isec.marco.firebase.ui.viewmodels.Pergunta
import kotlin.math.roundToInt

sealed class ShowAnswer {
    object NotAnswered : ShowAnswer()
    data class BooleanAnswer(val value: Boolean) : ShowAnswer()
    data class IntAnswer(val value: Int?) : ShowAnswer()
    data class ListAnswer(val value: List<Int>) : ShowAnswer()
    data class StringAnswer(val value: String?) : ShowAnswer()
}


@Composable
fun TipoPerguntaCard(
    pergunta: Pergunta,
    showComplete: Boolean,
) {
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
                respostaIndex[i] = index2 - pergunta.respostas.size/2 + 1
            }
            ShowAnswer.ListAnswer(respostaIndex)
        }
        "P05" -> {
            val respostaIndex = List(pergunta.respostas.size) {-1}.toMutableList()
            for(i in 0 until pergunta.respostaCerta.size) {
                val index = pergunta.respostaCerta.indexOf(pergunta.respostas[i])
                respostaIndex[index] = i + 1
            }
            ShowAnswer.ListAnswer(respostaIndex)
        }
        "P06" -> {
            val frase = pergunta.respostas.getOrNull(0)
            var i = 1  // Para numerar os elementos da lista
            val result = StringBuilder()  // Para construir a string de resultado

            var j = 0

            if (frase != null) {
                for (char in frase) {
                    if (char == '_' && j < pergunta.respostaCerta.size) {
                        result.append("[$i. \"${pergunta.respostaCerta[j]}\"]")
                        i++
                        j++
                    } else {

                        result.append(char)
                    }
                }
            }
            ShowAnswer.StringAnswer(result.toString())
        }

        else -> ShowAnswer.NotAnswered
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(255,224,192)
        )
    ) {
        when(pergunta.tipo){
            "P01" -> PerguntaVF(pergunta, showComplete,answer)
            "P02" -> PerguntaEM(pergunta, showComplete,answer,false)
            "P03" -> PerguntaEM(pergunta, showComplete,answer,true)
            "P04" -> PerguntaCorrespondecia(pergunta,showComplete,answer)
            "P05" -> PerguntaOrdenacao(pergunta,showComplete,answer)
            "P06" -> PerguntaEspacosEmBranco(pergunta,showComplete,answer)
//            "P07" -> PerguntaEspacosEmBranco(pergunta)
//            "P08" -> PerguntaEspacosEmBranco(pergunta)
            else -> {
                Text("Tipo de pergunta desconhecido")
            }
        }
    }
}

@Composable
fun PerguntaVF(
    pergunta: Pergunta,
    showComplete: Boolean,
    showAnswer: ShowAnswer?
){
    val showAnswerBoolean = when (showAnswer) {
        is ShowAnswer.BooleanAnswer -> showAnswer.value
        else -> null
    }
    Text(stringResource(R.string.P01_name))
    Spacer(modifier = Modifier.height(16.dp))
    Text("Pergunta: ${pergunta.titulo}")
    if(showComplete){
        Spacer(modifier = Modifier.height(16.dp))
            Column{
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    var isChecked  by remember { mutableStateOf<Boolean?>(null) }

                    val checkboxColor = when (showAnswerBoolean) {
                        true -> Color.Green
                        false -> Color.Red
                        else -> Color.Gray
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ){
                        Text("Verdadeiro")
                        Checkbox(
                            checked = isChecked == true || showAnswerBoolean == true,
                            onCheckedChange = { isChecked = if (it) true else null },
                            enabled = showAnswerBoolean == null,
                            colors = CheckboxDefaults.colors(
                                checkedColor = checkboxColor,
                                uncheckedColor = checkboxColor.copy(alpha = 0.6f)
                            )
                        )
                    }
                    Column(modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ){
                        Text("Falso")
                        Checkbox(
                            checked = isChecked == false || showAnswerBoolean == false,
                            onCheckedChange = { isChecked = if (it) false else null },
                            enabled = showAnswerBoolean == null,
                            colors = CheckboxDefaults.colors(
                                checkedColor = checkboxColor,
                                uncheckedColor = checkboxColor.copy(alpha = 0.6f)
                            )
                        )
                    }
                }
        }
    }
}

@Composable
fun PerguntaEM(
    pergunta: Pergunta,
    showComplete: Boolean,
    showAnswer: ShowAnswer?,
    mults: Boolean
){
    val selectedAnswerIndex = when (showAnswer) {
        is ShowAnswer.IntAnswer -> showAnswer.value
        else -> null
    }

    val selectedAnswerList = when (showAnswer) {
        is ShowAnswer.ListAnswer -> showAnswer.value
        else -> null
    }

    val isAnswerCorrect = showAnswer != null
    val checkboxColor = if (isAnswerCorrect) Color.Green else Color.Gray
    if(mults)
        Text(stringResource(R.string.P02_name))
    else
        Text(stringResource(R.string.P03_name))
    Spacer(modifier = Modifier.height(8.dp))
    Text("Pergunta: ${pergunta.titulo}")

    if (showComplete) {
        Spacer(modifier = Modifier.height(16.dp))

        var selected by remember { mutableStateOf<Int?>(null) }
        var selectedMultiple by remember { mutableStateOf<List<Int>>(emptyList()) }

        pergunta.respostas.forEachIndexed { index, resposta ->
            Row(
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Text(
                    text = resposta,
                    fontSize = 16.sp
                )
                Checkbox(
                    checked = if (mults) {
                        selectedMultiple.contains(index) || (showAnswer != null && selectedAnswerList?.contains(index) == true)
                    } else {
                        selected == index || (showAnswer != null && selectedAnswerIndex == index)
                    },
                    onCheckedChange = {
                        if (showAnswer == null) {
                            if (mults) {
                                selectedMultiple = if (it) {
                                    selectedMultiple + index
                                } else {
                                    selectedMultiple.filter { it != index }
                                }
                            } else {
                                selected = if (it) index else null
                            }
                        }
                    },
                    enabled = showAnswer == null,
                    colors = CheckboxDefaults.colors(
                        checkedColor = checkboxColor,
                        uncheckedColor = checkboxColor.copy(alpha = 0.6f)
                    )
                )
            }
        }
    }
}

@Composable
fun PerguntaCorrespondecia(
    pergunta: Pergunta,
    showComplete: Boolean = false,
    showAnswer: ShowAnswer?,
    modifier: Modifier = Modifier
){
    val selectedAnswerList = when (showAnswer) {
        is ShowAnswer.ListAnswer -> showAnswer.value
        else ->  List(pergunta.respostas.size) { "" }
    }
    Text(stringResource(R.string.P04_name))
    Spacer(modifier = Modifier.height(16.dp))
    Text("Pergunta: ${pergunta.titulo}")
    Spacer(modifier = Modifier.height(16.dp))
    if (showComplete) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ){
            Column(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxWidth()
            ) {
                Text("Resposta:")
                for (i in 0 until pergunta.respostas.size / 2) {
                    Row(
                        modifier = Modifier
                            .padding(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(all = 3.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color.LightGray)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${('A' + i)}.",
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(start = 8.dp),
                                    color = Color.Blue
                                )
                                Text(
                                    text = pergunta.respostas[i],
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(start = 4.dp),
                                    color = Color.Black
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(all = 3.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color.LightGray)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${i + 1}. ",
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(start = 8.dp),
                                    color = Color.Blue
                                )
                                Text(
                                    text = pergunta.respostas[i + pergunta.respostas.size / 2],
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(start = 4.dp),
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Text("Resposta:")
                for (i in 0 until pergunta.respostas.size / 2) {
                    Row(
                        modifier = Modifier
                            .padding(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(all = 3.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color.LightGray)
                        ) {
                            Text(
                                text = ('A' + i).toString(),
                                fontSize = 16.sp,
                                modifier = Modifier.padding(start = 8.dp),
                                color = Color.Blue
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(all = 3.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color.LightGray)
                        ) {
                            var upperCaseText by remember { mutableStateOf("") }
                            if(showAnswer != null){
                                Text(
                                    text = "${selectedAnswerList[i]}",
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(start = 8.dp),
                                    color = Color.Blue
                                )
                            }
                            TextField(
                                value = upperCaseText,
                                onValueChange = { newText ->
                                    if (newText.length == 1 && newText[0].isLetter()) {
                                        upperCaseText = newText.uppercase()
                                    }
                                },
                                modifier = Modifier.padding(start = 8.dp)
                                    .padding(all = 8.dp)
                                    .background(Color.Transparent)
                                    .width(32.dp)
                                    .height(32.dp)
                                    .background(Color.LightGray),
                                readOnly = showAnswer != null,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PerguntaOrdenacao(
    pergunta: Pergunta,
    showComplete: Boolean = false,
    showAnswer: ShowAnswer?,

){
    val selectedAnswerList = when (showAnswer) {
        is ShowAnswer.ListAnswer -> showAnswer.value
        else ->  List(pergunta.respostas.size) { "" }
    }
    Text(stringResource(R.string.P04_name))
    Spacer(modifier = Modifier.height(16.dp))
    Text("Pergunta: ${pergunta.titulo}")
    Spacer(modifier = Modifier.height(16.dp))
    if (showComplete) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ){
                Column(
                    modifier = Modifier
                        .weight(2f)
                        .fillMaxWidth()
                ) {
                    Text("Pergunta:")
                    for (i in 0 until pergunta.respostas.size) {
                        Row(
                            modifier = Modifier
                                .padding(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .height(40.dp)
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .padding(all = 3.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color.LightGray)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${(i+1)}.",
                                        fontSize = 16.sp,
                                        modifier = Modifier.padding(start = 8.dp),
                                        color = Color.Blue
                                    )
                                    Text(
                                        text = pergunta.respostas[i],
                                        fontSize = 16.sp,
                                        modifier = Modifier.padding(start = 4.dp),
                                        color = Color.Black
                                    )
                                }
                            }
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    Text("Ordem:")
                    for (i in 0 until pergunta.respostas.size) {
                        Row(
                            modifier = Modifier
                                .padding(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .height(40.dp)
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .padding(all = 3.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color.LightGray)
                            ) {
                                var upperCaseText by remember { mutableStateOf("") }
                                if(showAnswer != null){
                                    Text(
                                        text = "${selectedAnswerList[i]}",
                                        fontSize = 16.sp,
                                        modifier = Modifier.padding(start = 8.dp),
                                        color = Color.Blue
                                    )
                                }else{
                                    TextField(
                                        value = upperCaseText,
                                        onValueChange = { newText ->
                                            if (newText.length == 1 && newText[0].isLetter()) {
                                                upperCaseText = newText.uppercase()
                                            }
                                        },
                                        modifier = Modifier.padding(start = 8.dp)
                                            .padding(all = 8.dp)
                                            .background(Color.Transparent)
                                            .width(32.dp)
                                            .height(32.dp)
                                            .background(Color.LightGray),
                                        readOnly = showAnswer != null,
                                    )
                                }
                            }
                        }
                    }
                    }
                }
            }
        }

@Composable
fun PerguntaEspacosEmBranco(
    pergunta: Pergunta,
    showComplete: Boolean = false,
    showAnswer: ShowAnswer?,
) {
    val selectedAnswerString = when (showAnswer) {
        is ShowAnswer.StringAnswer -> showAnswer.value
        else ->  ""
    }
    Text(stringResource(R.string.P06_name))
    Spacer(modifier = Modifier.height(16.dp))
    Text("Pergunta: ${pergunta.titulo}")
    Spacer(modifier = Modifier.height(16.dp))



    if (showComplete) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize()
        ){
            if (selectedAnswerString != null) {
                Text(selectedAnswerString)
            }
            T06_Opcoes(
                pergunta.respostaCerta.size,
                pergunta.respostaCerta,
                isNomeInvalidList = List(pergunta.respostaCerta.size) { false },
                onNomeChange = { _, _ -> }
            )
        }
    }
}



@Composable
    fun TipoPerguntaScreen(
        viewModel: FirebaseViewModel,
        navController: NavHostController,
        onPerguntaSelected: (Int) -> Unit
    ) {
        val tiposPerguntas = listOf(
            Pergunta(
                id = "Q1",
                idUtilizador = FirebaseAuth.getInstance().currentUser?.uid ?: "",
                titulo = "A água ferve a 100°C?",
                imagem = "imagem_pergunta1",
                respostas = listOf(""),
                respostaCerta = listOf("true"),
                tipo = "P01"
            ),
            Pergunta(
                id = "Q2",
                idUtilizador = FirebaseAuth.getInstance().currentUser?.uid ?: "",
                titulo = "Qual é a capital da França?",
                imagem = "imagem_pergunta2",
                respostas = listOf("Londres", "Berlim", "Paris", "Madrid"),
                respostaCerta = listOf("2"),
                tipo = "P02"
            ),
            Pergunta(
                id = "Q3",
                idUtilizador = FirebaseAuth.getInstance().currentUser?.uid ?: "",
                titulo = "Selecione os continentes",
                imagem = "imagem_pergunta3",
                respostas = listOf("Ásia", "Europa", "Oceania", "Antártica", "Atlântico"),
                respostaCerta = listOf("2", "3"),
                tipo = "P03"
            ),
            Pergunta(
                id = "Q3",
                idUtilizador = FirebaseAuth.getInstance().currentUser?.uid ?: "",
                titulo = "Selecione os continentes",
                imagem = "imagem_pergunta3",
                respostas = listOf("Ásia", "Europa", "Oceania", "Antártica", "Atlântico","MAreica"),
                respostaCerta = listOf("Ásia", "Europa", "Oceania", "Antártica", "Atlântico","MAreica"),
                tipo = "P04"
            ),
            Pergunta(
                id = "Q3",
                idUtilizador = FirebaseAuth.getInstance().currentUser?.uid ?: "",
                titulo = "Selecione os continentes",
                imagem = "imagem_pergunta3",
                respostas = listOf("Ásia", "Europa", "Oceania", "Antártica"),
                respostaCerta = listOf(  "Oceania","Europa", "Antártica","Ásia"),
                tipo = "P05"
            ),
            Pergunta(
                id = "Q3",
                idUtilizador = FirebaseAuth.getInstance().currentUser?.uid ?: "",
                titulo = "Selecione os continentes",
                imagem = "imagem_pergunta3",
                respostas = listOf("Estou na _ e vou para _ "),
                respostaCerta = listOf("Ásia", "Europa"),
                tipo = "P06"
            ),
            Pergunta(
                id = "Q3",
                idUtilizador = FirebaseAuth.getInstance().currentUser?.uid ?: "",
                titulo = "Selecione os continentes",
                imagem = "imagem_pergunta3",
                respostas = listOf("Ásia", "Europa", "Oceania", "Antártica", "Atlântico","MAreica"),
                respostaCerta = listOf("Ásia", "Europa", "Oceania", "Antártica"),
                tipo = "P07"
            ),
            Pergunta(
                id = "Q3",
                idUtilizador = FirebaseAuth.getInstance().currentUser?.uid ?: "",
                titulo = "Selecione os continentes",
                imagem = "imagem_pergunta3",
                respostas = listOf("Ásia", "Europa", "Oceania", "Antártica", "Atlântico","MAreica"),
                respostaCerta = listOf("Ásia", "Europa", "Oceania", "Antártica"),
                tipo = "P08"
            )

        )

        var selectedPage by remember { mutableStateOf(-1) }

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val pagerState = rememberPagerState(pageCount = {
                tiposPerguntas.size
            })
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                val pergunta = tiposPerguntas[page]
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .padding(2.dp)
                ) {
                    TipoPerguntaCard(
                        pergunta, true
                    )
                }
            }
            Button(
                onClick = {
                    selectedPage = pagerState.currentPage
                    onPerguntaSelected(selectedPage)
                    navController.navigate("criar-pergunta") {
                        popUpTo("criar-pergunta") { inclusive = true }
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(64.dp)
            ) {
                Text("Escolher")
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

// TODO FINAL
// trocar o answers para dentro do composable