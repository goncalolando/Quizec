package pt.isec.marco.firebase.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isec.marco.firebase.R
import pt.isec.marco.firebase.ui.viewmodels.Pergunta

sealed class ShowAnswer {
    object NotAnswered : ShowAnswer()
    data class BooleanAnswer(val value: Boolean) : ShowAnswer()
    data class IntAnswer(val value: Int?) : ShowAnswer()
}


@Composable
fun TipoPerguntaCard(
    pergunta: Pergunta,
    showComplete: Boolean,
    showAnswer: ShowAnswer?
) {
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
            "P01" -> PerguntaVF(pergunta, showComplete,showAnswer)
            "P02" -> PerguntaEM(pergunta, showComplete,showAnswer)
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
    Text("Pergunta: ${pergunta.titulo}")
    if(showComplete){
        Spacer(modifier = Modifier.height(16.dp))
        var i = 0
        while(pergunta.respostas.size > i) {
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
                    Column(
                        modifier = Modifier
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
            i++
        }
    }
}

@Composable
fun PerguntaEM(
    pergunta: Pergunta,
    showComplete: Boolean,
    showAnswer: ShowAnswer?
){
    val selectedAnswerIndex = when (showAnswer) {
        is ShowAnswer.IntAnswer -> showAnswer.value
        else -> null
    }

    val isAnswerCorrect = showAnswer != null
    val checkboxColor = if (isAnswerCorrect) Color.Green else Color.Gray

    Text(stringResource(R.string.P02_name))
    Spacer(modifier = Modifier.height(8.dp))
    Text("Pergunta: ${pergunta.titulo}")

    if (showComplete) {
        Spacer(modifier = Modifier.height(16.dp))

        var selected by remember { mutableStateOf<Int?>(null) }

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
                    checked = selected == index || (showAnswer != null && selectedAnswerIndex == index),
                    onCheckedChange = {
                        if (showAnswer == null) {
                            selected = if (it) index else null
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
    modifier: Modifier = Modifier
){
    Text(stringResource(R.string.P04_name))
    Text("Pergunta: ${pergunta.titulo}")
    Spacer(modifier = Modifier.height(16.dp))

}

@Composable
fun PerguntaEspacosEmBranco(
    pergunta: Pergunta,
    modifier: Modifier = Modifier
){
    Text(stringResource(R.string.P06_name))
    Text("Pergunta: ${pergunta.titulo}")
    Spacer(modifier = Modifier.height(16.dp))
}



@Composable
fun TipoPerguntaScreen(
    viewModel: FirebaseViewModel,
    navController: NavHostController,
    onPerguntaSelected: (Int) -> Unit
) {
    val tiposPerguntas = listOf(
        Pergunta(
            "identificador", "Exemplo de pergunta",
            "img", listOf("Resposta 1"), listOf("true"),"P01"
        ),
        Pergunta(
            "identificador", "TItulo",
            "img", listOf("Opcao 1", "Opcao 2"), listOf("Resposta 3"),"P02"
        ),
        Pergunta(
            "identificador", "TItulo",
            "img", listOf("Resposta 3", "Resposta 4"), listOf("Resposta 3"),"BF"
        )
    )

    var selectedPage by remember { mutableStateOf(-1) } // Guardar a pagina selecionada

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
                    pergunta, true,null
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

